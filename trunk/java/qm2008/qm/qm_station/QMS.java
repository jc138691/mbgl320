package qm_station;
import project.Project;
import project.ProjectModel;

import javax.utilx.log.Log;

import qm_station.ui.scatt.JmOptLcr;
import qm_station.ui.scatt.JmOptR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 14:42:52
 */
public class QMS extends Project {
  public static Log log = Log.getLog(QMS.class);
  public static final String PROJECT_FILE_EXTENSION = "qms";

  private String resultsFileName;
  private JmOptR jmPotOptR;
  private JmOptLcr jmPotOptLcr;

  public QMS() {
    init();
  }
  private void init() {
    jmPotOptR = new JmOptR();
    jmPotOptLcr = new JmOptLcr();
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
  public JmOptR getJmPotOptR() {
    return jmPotOptR;
  }
  public void setJmPotOptR(JmOptR jmPotOptR) {
    this.jmPotOptR = jmPotOptR;
  }
  public JmOptLcr getJmPotOptLcr() {
    return jmPotOptLcr;
  }
  public void setJmPotOptLcr(JmOptLcr jmPotOptLcr) {
    this.jmPotOptLcr = jmPotOptLcr;
  }
}
