package zeromq.sample;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.Lists;
import zeromq.sample.client.subscriber.SubscriberClient;
import zeromq.sample.clinet.publish.PublishClient;

public class App {

  private static Logger log = LogManager.getLogger(App.class);

  public static void main( String[] args ) throws InterruptedException {
    String host = args[0];
    Integer port = Integer.parseInt(args[1]);
    Integer checkPort = Integer.parseInt(args[2]);

    List<String> messages = Lists.newArrayList();
    for(int i=1;i<=5; i++) {
      String message = String.format("Message-%03d", i);
      messages.add(message);
    }

    Pub pub = new Pub(host, port, checkPort, messages);
    Sub sub = new Sub(host, port, checkPort);

    ExecutorService executor = Executors.newFixedThreadPool(2, new ThreadFactory() {
      @Override
      public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException(Thread t, Throwable e)
            {
                e.printStackTrace();
            }
        });
        return thread;
      }
    });
    executor.submit(sub);
    executor.submit(pub);

    executor.shutdown();
    executor.awaitTermination(5, TimeUnit.SECONDS);
  }

  private static class Pub implements Runnable {

    private String host = null;
    private Integer port = null;
    private Integer checkPort = null;
    private List<String> messages = Lists.newArrayList();

    private Pub(String host, Integer port, Integer checkPort, List<String> messages) {
      this.host = host;
      this.port = port;
      this.checkPort = checkPort;
      this.messages = messages;

    }

    @Override
    public void run() {

      Thread.currentThread().setName("Publisher-Thread");

      log.debug("Start run().");

      try (PublishClient publishClient = new PublishClient(host, port, checkPort)) {
        publishClient.sendMessage(messages);
      }

      log.debug("End start().");
    }
  }

  private static class Sub implements Runnable {

    private String host = null;
    private Integer port = null;
    private Integer checkPort = null;

    private Sub(String host, Integer port, Integer checkPort) {
      this.host = host;
      this.port = port;
      this.checkPort = checkPort;
    }

    @Override
    public void run() {

      Thread.currentThread().setName("Subscriber-Thread");

      log.debug("Start run().");

      try(SubscriberClient subscriberClient = new SubscriberClient(host, port, checkPort)) {
        subscriberClient.receiveMessage();
      }

      log.debug("End start().");
    }

  }
}
