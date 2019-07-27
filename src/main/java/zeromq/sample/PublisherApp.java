package zeromq.sample;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.Lists;
import zeromq.sample.clinet.publish.PublishClient;

public class PublisherApp {

  private static Logger log = LogManager.getLogger(PublisherApp.class);

  public static void main(String[] args) {

    log.debug("Start PublisherApp.");

    String host = args[0];
    Integer port = Integer.parseInt(args[1]);
    Integer checkPort = Integer.parseInt(args[2]);
    Integer messageCount = Integer.parseInt(args[3]);

    List<String> messages = Lists.newArrayList();
    for(int i=1;i<=messageCount; i++) {
      String message = String.format("Message-%03d", i);
      messages.add(message);
    }

    try (PublishClient publishClient = new PublishClient(host, port, checkPort)) {
      publishClient.sendMessage(messages);
    }

    log.debug("End PublisherApp.");
  }

}
