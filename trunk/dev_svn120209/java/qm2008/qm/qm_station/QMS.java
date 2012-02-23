package qm_station;
import project.Project;
import project.ProjectModel;

import javax.utilx.log.Log;

import qm_station.ui.scatt.CalcOptR;
import qm_station.ui.scatt.CalcOptLcr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 14:42:52
 */
public class QMS extends Project {
  public static Log log = Log.getLog(QMS.class);
  public static final String PROJECT_FILE_EXTENSION = "qms";

  private String resultsFileName;
  private CalcOptR jmPotOptR;
  private CalcOptLcr jmPotOptLcr;

  public QMS() {
    init();
  }
  private void init() {
    jmPotOptR = new CalcOptR();
    jmPotOptLcr = new CalcOptLcr();
  }
  public void loadDefault(String appName, String appVersion) {    log.dbg("loadDefault(appName=", appName);
    log.dbg("appVersion=", appVersion);
    super.loadDefault(appName, appVersion);
    loadDefault();
  }
  public void loadDefault() {
    setErrorLogFileName("ErrorLog.txt");
    setProjectFileExtension(PROJECT_FILE_EXTENSION);
    super.loadDefault();

    jmPotOptR.loadDefault();
    jmPotOptLcr.loadDefault();
  }
  public void copyTo(ProjectModel to) {
    super.copyTo(to);
    if (!(to instanceof QMS)) {
      log.error("Copy destination is not a JM object");
    }
    QMS model = (QMS) to;
    model.setErrorLogFileName(getErrorLogFileName());
    model.setProjectFileName(getProjectFileName());
    model.setResultsFileName(getResultsFileName());

    model.setJmPotOptR(getJmPotOptR());
    model.setJmPotOptLcr(getJmPotOptLcr());
  }
  public String getResultsFileName() {
    return resultsFileName;
  }
  public void setResultsFileName(String resultsFileName) {
    this.resultsFileName = resultsFileName;
  }
  public CalcOptR getJmPotOptR() {
    return jmPotOptR;
  }
  public void setJmPotOptR(CalcOptR jmPotOptR) {
    this.jmPotOptR = jmPotOptR;
  }
  public CalcOptLcr getJmPotOptLcr() {
    return jmPotOptLcr;
  }
  public void setJmPotOptLcr(CalcOptLcr jmPotOptLcr) {
    this.jmPotOptLcr = jmPotOptLcr;
  }
}
