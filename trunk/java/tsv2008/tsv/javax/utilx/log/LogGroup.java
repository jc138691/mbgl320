package javax.utilx.log;
import javax.swingx.textx.TextView;
import java.util.HashSet;
import java.io.PrintStream;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/09/2008, Time: 13:54:21
 */
public class LogGroup {
  private HashSet<Log> group = new HashSet<Log>();
  public void addView(TextView view) {
    PrintStream ps = new PStreamToTextView(view);
    for (Log log : group) {
      log.add(ps);
    }
  }
  public void setDbg(boolean b) {
    for (Log log : group) {
      log.setDbg(b);
    }
  }
  public void add(Log log) {
    group.add(log);
  }
}
