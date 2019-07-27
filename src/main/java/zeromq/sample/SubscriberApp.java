package zeromq.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zeromq.sample.client.subscriber.SubscriberClient;

public class SubscriberApp {

  private static Logger log = LogManager.getLogger(SubscriberApp.class);

  public static void main(String[] args) {

    log.debug("Start SubscriberApp.");

    String host = args[0];
    Integer port = Integer.parseInt(args[1]);
    Integer checkPort = Integer.parseInt(args[2]);

    try(SubscriberClient subscriberClient = new SubscriberClient(host, port, checkPort)) {
      subscriberClient.receiveMessage();
    }

    log.debug("End SubscriberApp.");
  }
}
