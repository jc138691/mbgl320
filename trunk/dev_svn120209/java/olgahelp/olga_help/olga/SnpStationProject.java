package olga;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:25:05.
 */
public class SnpStationProject extends SnpStation {
  public static Log log = Log.getLog(SnpStationProject.class);
  private static SnpStation instance;
  private SnpStationProject() {
  }
  public static SnpStation makeInstance(String appName, String appVersion) {    log.dbg("makeInstance(appName=", appName);
    log.dbg("appVersion=", appVersion);
    if (instance != null) {
      log.error("call SnpStationProject.makeInstance() only once");
      System.exit(1);
    }
    instance = new SnpStation();
    instance.loadDefault(appName, appVersion);
    instance.loadFromDisk();
    return instance;
  }

// Note: SnpStation is returned, NOT SnpStationProject!!!  XMLEncoder didnot like static-recursive
  public static SnpStation getInstance() {
    if (instance != null)
      return instance;
    log.error("call SnpStationProject.makeInstance() first");
    return null;
  }

}