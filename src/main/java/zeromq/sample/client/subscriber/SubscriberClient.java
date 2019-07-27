package zeromq.sample.client.subscriber;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import zeromq.sample.client.ZeroMqClient;
import zeromq.sample.utils.IOUtils;

public class SubscriberClient extends ZeroMqClient {

  private static Logger log = LogManager.getLogger(SubscriberClient.class);

  private ZMQ.Socket subscribeSocket = null;

  private ZMQ.Socket requestSocket = null;

  private static final String CONNECT_CONFIRM_MESSAGE = "Connect Confirm.";

  public SubscriberClient(String host, Integer port, Integer checkPort) {
    super(host, port, checkPort);
    initializeSocket();
  }

  private void initializeSocket() {
    this.subscribeSocket = this.context.createSocket(SocketType.SUB);
    this.requestSocket = this.context.createSocket(SocketType.REQ);
  }

  public void receiveMessage() {
    log.debug("Start receiveMessage().");

    this.subscribeSocket.connect(this.address);
    this.subscribeSocket.subscribe(ZMQ.SUBSCRIPTION_ALL);
    this.requestSocket.connect(this.checkAddress);

    checkSocketConnect();

    while(true) {
      String receiveMessage = this.subscribeSocket.recvStr();
      log.debug("Receive Message:{}", receiveMessage);
      if (StringUtils.equals("End", receiveMessage)) {
        break;
      }
    }
    log.debug("End receiveMessage().");

  }

  private void checkSocketConnect() {
    log.debug("Start checkSocketConnect().");

    // send connect confirm message to publisher.
    log.debug("Send Connect Confirm Message.");
    this.requestSocket.send(CONNECT_CONFIRM_MESSAGE);

    // receive connect check ok message from publisher.
    String receiveMessage = this.requestSocket.recvStr();
    log.debug("Receive Message:{}", receiveMessage);

    log.debug("End checkSocketConnect().");
  }


  @Override
  public void close() {
    log.debug("Start close().");
    IOUtils.close(this.requestSocket);
    IOUtils.close(this.subscribeSocket);
    IOUtils.close(this.context);
    log.debug("End close().");
  }


}
