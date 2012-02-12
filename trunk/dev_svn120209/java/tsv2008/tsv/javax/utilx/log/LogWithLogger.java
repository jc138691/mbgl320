package javax.utilx.log;
import junit.framework.TestCase;

import java.util.logging.*;
import java.io.File;
import java.io.IOException;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 12/09/2008, Time: 16:00:29
 */
// DO NOT USE!!!!!!!!!!!
public class LogWithLogger {
  protected FileHandler lastFile;
  protected Logger logger;
  protected static Formatter format;
  protected LogWithLogger() {}
  final public void debug(String s, int i) {
    debug(s, Integer.toString(i));
  }
  final public void info(String s, int i) {
    info(s, Integer.toString(i));
  }
  final public void info(String s) {
    if (!logger.isLoggable(Level.INFO)) {
      return;
    }
    logger.info(s);
  }

  public void assertZero(String text, double val, double error) {
    float f = (float) Math.abs(val);
    debug(text + f);
    TestCase.assertEquals(0, f, error);
  }

  final public void debug(String s, boolean b) {
    debug(s, Boolean.toString(b));
  }
  final public void info(String s, boolean b) {
    info(s, Boolean.toString(b));
  }
  final public void info(String s, double d) {
    info(s, Float.toString((float)d));
  }
  final public void debug(String s, double d) {
    debug(s, Float.toString((float)d));
  }
  final public void info(String s, Object obj) {
    if (!logger.isLoggable(Level.INFO)) {
      return;
    }
    logger.info(s + obj.toString());
  }
  final public void debug(String s, Object obj) {
    if (!logger.isLoggable(Level.FINEST)) {
      return;
    }
    logger.finest(s + obj.toString());
  }
  final public void debug(String s) {
    if (!logger.isLoggable(Level.FINEST)) {
      return;
    }
    logger.finest(s);
//    logger.severe("my severe message");
//    logger.warning("my warning message");
//    logger.info("my info message");
//    logger.config("my config message");
//    logger.fine("my fine message");
//    logger.finer("my finer message");
//    logger.finest("my finest message");
  }
  final public String severe(String s) {
    if (!logger.isLoggable(Level.SEVERE)) {
      return s;
    }
    logger.severe(s);
    return s;
  }
  public void setDebug() {
    Handler[] arr = logger.getHandlers();
    if (arr.length == 0) {
//      logger = logger.getParent();
      arr = logger.getParent().getHandlers();
    }
    for (Handler h: arr) {
      h.setLevel(Level.FINEST);
    }
    logger.setLevel(Level.FINEST);
  }
  public void setDebugOff() {
    logger.setLevel(Level.INFO);
  }
  public void setDebug(boolean on) {
    if (on) {
      setDebug();
    }
    else {
      setDebugOff();
    }
  }
  public void openFile(String dirName, String fileName) {
    openFile(dirName + File.separator + fileName);
  }
  public void openFile(String dirName, String dir2, String fileName) {
    openFile(dirName + File.separator + dir2 + File.separator + fileName);
  }
  public void openFile(String fileName) {
    debug("openFile(fileName=", fileName);
    //http://exampledepot.com/egs/java.util.log/LogFile.html
    try {
      FileHandler h = new FileHandler(fileName);
      h.setLevel(logger.getLevel());
      h.setFormatter(format);
      logger.addHandler(h);
      lastFile = h;
    } catch (IOException e) {
      severe(e.toString());
    }
  }
  public void addHandler(Handler h) {
    Level level = logger.getLevel();
    if (level == null) {
      level = logger.getParent().getLevel();
    }
    h.setLevel(level);
    h.setFormatter(format);
    logger.addHandler(h);
  }
  public void closeLastFile() {
    if (lastFile == null) {
      return;
    }
    lastFile.flush();
    logger.removeHandler(lastFile);
    lastFile = null;
  }
  public void saveToFile(String text, String dirName, String dir2, String fileName) {
    setDebug();
    openFile(dirName, dir2, fileName);
    debug(text);
    closeLastFile();
  }
  public void remind(String s) {
    logger.info(s);
  }
}

