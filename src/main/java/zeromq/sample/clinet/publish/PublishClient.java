package zeromq.sample.clinet.publish;

import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import zeromq.sample.client.ZeroMqClient;
import zeromq.sample.utils.IOUtils;

public class PublishClient extends ZeroMqClient {

  private static Logger log = LogManager.getLogger(PublishClient.class);

  private ZMQ.Socket publishSocket = null;

  private ZMQ.Socket repeatSocket = null;

  private static final String CONNECT_CONFIRM_MESSAGE = "Connect Confirm OK!!";

  public PublishClient(String host, Integer port, Integer checkPort) {
    super(host, port, checkPort);
    initializeSocket();
  }

  private void initializeSocket() {
    this.publishSocket = this.context.createSocket(SocketType.PUB);
    this.repeatSocket = this.context.createSocket(SocketType.REP);
  }

  public void sendMessage(List<String> messages) {
    log.debug("Start sendMessage().");

    publishSocket.bind(this.address);
    repeatSocket.bind(this.checkAddress);

    checkSocketConnect();

    SendMessageAction sendMessageAction = new SendMessageAction(this.publishSocket);
    messages.forEach(message -> sendMessageAction.accept(message));

    // send End Message
    sendMessageAction.accept("End");
  }

  private void checkSocketConnect() {
    log.debug("Start checkSocketConnect.");

    // receive connect check message from subscriber.
    String receiveMessage = this.repeatSocket.recvStr();
   log.debug("Receive Message:{}", receiveMessage);

    // send connect ok message to subscriber.
   log.debug("Send Connect Confirm OK Message.");
    this.repeatSocket.send(CONNECT_CONFIRM_MESSAGE);

    log.debug("End checkSocketConnect.");
  }

  private class SendMessageAction implements Consumer<String> {

    ZMQ.Socket publishSocket = null;

    private SendMessageAction(ZMQ.Socket publishSocket) {
      this.publishSocket = publishSocket;
    }

    @Override
    public void accept(String message) {
      log.debug("Send Message is {}", message);
      this.publishSocket.send(message);
    }

  }

  @Override
  public void close() {
    log.debug("Start close().");
    IOUtils.close(this.repeatSocket);
    IOUtils.close(this.publishSocket);
    IOUtils.close(this.context);
    log.debug("End close().");
  }

}
