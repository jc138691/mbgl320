package qm_station;
import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 15:20:19
 */
public class QMSProject extends QMS {
  public static Log log = Log.getLog(QMSProject.class);
  private static QMS instance;
  private QMSProject() {
  }
  public static QMS makeInstance(String appName, String appVersion) {    log.dbg("makeInstance(appName=", appName);
    log.dbg("appVersion=", appVersion);
    if (instance != null) {
      log.error("call QMSProject.makeInstance() only once");
      System.exit(1);
    }
    instance = new QMS();
    instance.loadDefault(appName, appVersion);
    instance.loadFromDisk();
    return instance;
  }

// Note: QMS is returned, NOT QMSProject!!!  XMLEncoder didnot like static-recursive
  public static QMS getInstance() {
    if (instance != null)
      return instance;
    log.error("call QMS.makeInstance() first");
    return null;
  }

}