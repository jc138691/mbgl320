package javax.utilx.log;
import javax.utilx.log.kiss.KissLog;
import java.util.HashMap;
import java.io.PrintStream;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 11:20:21
 */
// OLD way via java.uitl.log.Logger
public class Log extends KissLog {
  private static Log globalLog;
  private static HashMap<String, Log> map = new HashMap<String, Log>();
  public static Log getLog(Class c) {
    Log res = map.get(c.getName());
    if (res == null) {
      res = new Log(c);
      map.put(c.getName(), res);
    }
    return res;
  }
  private Log(Class cl) {
    super(cl);
  }
  public static Log getRootLog() {
    if (globalLog == null)
      globalLog = new Log(Log.class);
    return globalLog;
  }
  public static void setup() {
    getRootLog().add(System.out);
    getRootLog().add(System.err);
  }
  public static void addGlobal(PrintStream ps) {
    getRootLog().add(ps);
  }
  final public void setDbg(boolean dbg) {
    if (dbg  &&  Log.getRootLog().countPrintStreams() == 0) {
      getRootLog().add(System.out);
    }
    super.setDbg(dbg);
  }

  public static String printOpt(String res, String optName, boolean optOn, String opt) {
    if (!optOn)
      return res;
    if (res.length() > 0)
      res += ", ";
    res += (optName + "=" + opt);
    return res;
  }
}
