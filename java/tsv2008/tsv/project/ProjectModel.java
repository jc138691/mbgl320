package project;
import javax.swing.*;
import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 15:03:55
 */
public class ProjectModel {
  public static Log log = Log.getLog(ProjectModel.class);
  public final static String CROSS_PLATFORM_LOOK = UIManager.getCrossPlatformLookAndFeelClassName();
  public final static String SYSTEM_LOOK = UIManager.getSystemLookAndFeelClassName();

  private String lookFeel;
  private String orgType;
  private String appName;
  private String appVersion;
  private String dirName;
  private String fileName;
  private String projectFileName;
  private String projectFileExtension;
  private String errorLogFileName;

  public void copyTo(ProjectModel to) {
    to.setLookFeel(getLookFeel());
    to.setOrgType(getOrgType());
    to.setAppName(getAppName());
    to.setAppVersion(getAppVersion());
    to.setDirName(getDirName());
    to.setFileName(getFileName());
    to.setProjectFileName(getProjectFileName());
    to.setErrorLogFileName(getErrorLogFileName());
  }
  final public void setOrgType(String v) {
    orgType = v;
  }
  final public String getOrgType() {
    return orgType;
  }
  final public void setAppName(String v) {
    appName = v;
  }
  final public String getAppName() {
    return appName;
  }
  final public void setAppVersion(String v) {
    appVersion = v;
  }
  final public String getAppVersion() {
    return appVersion;
  }
  final public void setFileName(String v) {
    fileName = v;
  }
  final public String getFileName() {
    return fileName;
  }
  final public void setDirName(String v) {
    dirName = v;
  }
  final public String getDirName() {
    return dirName;
  }
  public String getLookFeel() {
    return lookFeel;
  }
  public void setLookFeel(String lookFeel) {
    this.lookFeel = lookFeel;
  }
  public String getProjectFileName() {
    log.dbg("getLastProjectFileName()");
    log.dbg("lastProjectFileName = ", projectFileName);
    return projectFileName;
  }
  public void setProjectFileName(String v) {
    log.dbg("setLastProjectFileName(", v);
    this.projectFileName = v;
  }
  public String getProjectFileExtension() {
    log.dbg("getProjectFileExtension()");
    log.dbg("projectFileExtension = ", projectFileName);
    return projectFileExtension;
  }
  public void setProjectFileExtension(String v) {
    log.dbg("setProjectFileExtension(", v);
    this.projectFileExtension = v;
  }
  public String getErrorLogFileName() {
    return errorLogFileName;
  }
  public void setErrorLogFileName(String errorLogFileName) {
    this.errorLogFileName = errorLogFileName;
  }

}
