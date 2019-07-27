package zeromq.sample.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.ZContext;

public abstract class ZeroMqClient implements AutoCloseable {

  private static Logger log = LogManager.getLogger(ZeroMqClient.class);

  protected ZContext context = null;
  protected String address = null;
  protected String checkAddress = null;

  public ZeroMqClient(String host, Integer port, Integer checkPort) {
    initializeAddress(host, port, checkPort);
    this.context = new ZContext();
  }

  protected void initializeAddress(String host, Integer port, Integer checkPort) {
    this.address = String.format("tcp://%s:%d", host, port);
    this.checkAddress = String.format("tcp://%s:%d", host, checkPort);
    log.debug("Address:{}, Check Address:{}", address, checkAddress);
  }

}
