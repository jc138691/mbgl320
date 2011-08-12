package project;
import javax.utilx.log.Log;
import javax.swing.*;
import java.io.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.NoSuchElementException;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 15:06:41
 */
public class Project extends ProjectModel {
  private static final Log log = Log.getLog(Project.class);

  public void loadDefault(String appName, String appVersion) {
    setAppName(appName);
    setAppVersion(appVersion);
    loadDefault();
  }
  public void loadDefault() {
    setLookFeel(ProjectModel.CROSS_PLATFORM_LOOK);
    setOrgType("org");
    String name = buildDefaultDirName();
    setDirName(name);
    setProjectFileName(getFileName());

    name = buildDefaultFileName();
    setFileName(name);

    setProjectFileExtension("xml");
    setErrorLogFileName("error_log.txt");
  }
  final public String buildDefaultDirFileName() {
    log.dbg("buildDirFileName");
    String name = buildDefaultDirName() + File.separator + buildDefaultFileName();
    log.dbg("name=>", name);
    return name;
  }
  public File makeFile(String name) {
    if (name == null || name.length() == 0)
      return new File("empty_file_name.txt");
    else
      return new File(name);
  }
  final public String getDefaultUserDirName() {
    String res = "";
    try {
      res = System.getProperty("user.home");
      //res = System.getProperty("user.dir");
    }
    catch (SecurityException e) {
    }
    return res;
  }
  final public String buildDefaultDirName() {
    String res = getDefaultUserDirName();
    if (res.length() != 0)
      res += File.separator;
    if (getOrgType() == null || getOrgType().length() == 0)
      res += "org";
    else
      res += getOrgType();
    if (getAppName() != null && getAppName().length() != 0)
      res += (File.separator + getAppName());
    else
      res += (File.separator + "project");
//    if (getAppVersion() != null && getAppVersion().length() != 0)
//      res += (File.separator + getAppVersion());   // e.g. "org/kinship/v2_03"
    return res;
  }
  final public String buildDefaultFileName() {
    String res = getAppName() + "ProjectModel";
    if (getAppVersion() != null && getAppVersion().length() != 0)
      res += getAppVersion();
    res += ".xml";
    return res;
  }
  public void loadFromDisk(String fileName) {
    Object bean = readProject(fileName);
    if (bean instanceof Project)
      ((Project) bean).copyTo(this);
    else
      saveProjectToDefaultLocation();
  }
  public void loadFromDisk() {
    log.dbg("loadFromDisk");
    String fileName = buildDefaultDirFileName();
    loadFromDisk(fileName);
  }
  protected Object readProject(String fileName) {
    File file = null;
    try {
      file = new File(fileName);
      if (!file.exists() || !file.canRead())
        return null;
      XMLDecoder decoder
        = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
      Object bean = decoder.readObject();
      decoder.close();
      return bean;
    }
    catch (FileNotFoundException e) {
      return null;
    }
    catch (IOException e) {
      return null;
    }
//    catch (SAXParseException e) {
//      String error = "Unable to parse file " + fileName
//        + ".\n\nError: " + e.toCsv();
//      log.severe(error);
//      JOptionPane.showMessageDialog(null, error);
//      return null;
//    }
    catch (NoSuchElementException e) {
      String error = "Unable to parse file " + fileName
        + ".\nIt may be an older version."
        + ".\n\nError: " + e.toString();
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return null;
    }
  }
  public boolean saveProjectToDefaultLocation() {
    return writeProject(buildDefaultDirFileName());
  }
  // should be static but XMLEncoder does not like statics?
  public boolean writeProject(String fileName) {
    File file = null;
    try {
      file = new File(fileName);
      if (!file.exists()) {
        File dir = file.getParentFile();
        if (dir != null)
          dir.mkdirs();
        file.createNewFile();
      }
      if (!file.canWrite()) {
        JOptionPane.showMessageDialog(null
          , "Default project file\n"
          + fileName
          + "\nCAN NOT be overwritten.\n"
          + "Check your read-write access to\n"
          + file.getCanonicalPath()
          + "\nYour changes to the project preferences will not be saved.\n");
        return false;
      }
      XMLEncoder en
        = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
      en.writeObject(this);
      en.close();
//         en.flush();
      return true;
    }
    catch (FileNotFoundException e) {
      return false;
    }
    catch (IOException e) {
      return false;
    }
  }
}
