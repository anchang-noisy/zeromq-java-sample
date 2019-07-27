package zeromq.sample;

import org.junit.Test;

public class AppTest {
  @Test
  public void Main() throws InterruptedException {
    String[] args = new String[] {"localhost", "5556", "5557"};
    App.main(args);
  }
}