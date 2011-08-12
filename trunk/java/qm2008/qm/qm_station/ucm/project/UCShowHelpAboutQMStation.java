package qm_station.ucm.project;
import project.ucm.UCDShowHelpAbout;
import javax.langx.SysProp;
import javax.langx.MemoryInfo;
import javax.swing.*;

import qm_station.QMSProject;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 14:52:31
 */
public class UCShowHelpAboutQMStation extends UCDShowHelpAbout {
  public UCShowHelpAboutQMStation(JFrame frame) {
    super(frame);
//    String help = "<HTML><H2><A href=\"www.dmitrykonovalov.org\">www.dmitrykonovalov.org</A>   "
    String help = "<HTML><H2>   "
      + QMSProject.getInstance().getAppName()
      + " " + QMSProject.getInstance().getAppVersion()
      + "</H2><HR>"
//      + PValue_2007_paper.REFERENCE_HTML
      + "<br>"
      + "<br>"
//      + LogBB_2007_paper.REFERENCE_HTML
      + "<br>"
      + "<br>"
      + "Copyright &copy 2008 dmitry.konovalov@jcu.edu.au"
      + "<br>"
      + "<br>"
      + "System Info:"
      + "<br>"
      + "<pre>" + new MemoryInfo() + "</pre>"
      + "<pre>" + SysProp.getInfo() + "</pre>"
      + "</HTML>"
      ;
    setHelpMessage(help);
  }
}