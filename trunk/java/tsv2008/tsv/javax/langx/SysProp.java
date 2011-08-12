package javax.langx;
import java.util.Properties;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 14:55:44
 */
public class SysProp {
  public static final String EOL = System.getProperty("line.separator");

  public static String getInfo() {
    String[] keys = {
      "java.version"
      , "java.home"
      , "java.specification.version"
      , "java.vm.version"
      , "java.runtime.version"
//      , "java.class.path"
//      , "sun.boot.library.path"
//      , "sun.boot.class.path"
      , "os.name"
      , "user.dir"
      , "user.name"
    };
    String res = "";
    int MAX_LENGTH = 40;
    try {
      Properties all = System.getProperties();
      for (int i = 0; i < keys.length; i++) {
        String prop = all.getProperty(keys[i]);
        if (prop != null)
        {
          if (prop.length() >= MAX_LENGTH)
            prop = prop.substring(0, MAX_LENGTH);
          res += (SysProp.EOL + keys[i] + " = [" + prop + "]");
        }
      }
    }
    catch (SecurityException e) {
      res = "Unable to get system properties due to security restrictions\n" + e;
    }
    return res;
  }
}
