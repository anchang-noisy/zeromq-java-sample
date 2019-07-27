package zeromq.sample.publisher;

import org.zeromq.ZContext;
import zeromq.sample.utils.IOUtils;

public class Publisher implements AutoCloseable {

  private ZContext zContext;

  public Publisher() {
    this.zContext = new ZContext();
  }

  @Override
  public void close() throws Exception {
    IOUtils.close(zContext);
  }

}
