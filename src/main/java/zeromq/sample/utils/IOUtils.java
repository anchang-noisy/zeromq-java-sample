package zeromq.sample.utils;

public class IOUtils {

  private IOUtils() {}

  public static void close(AutoCloseable obj) {
    try {
      obj.close();
    } catch (Exception e) {}
  }
}
