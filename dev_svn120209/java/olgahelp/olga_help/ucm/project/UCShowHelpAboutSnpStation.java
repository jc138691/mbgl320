package ucm.project;

import project.ucm.UCDShowHelpAbout;

import javax.swing.*;
import javax.langx.MemoryInfo;
import javax.langx.SysProp;

import olga.SnpStationProject;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:33:01.
 */
public class UCShowHelpAboutSnpStation extends UCDShowHelpAbout {
  public UCShowHelpAboutSnpStation(JFrame frame) {
    super(frame);
    setHelpMessage(getHelpMessage());
  }

  public String  getHelpMessage() {
    String help = "<HTML><H2>   "
      + SnpStationProject.getInstance().getAppName()
      + " " + SnpStationProject.getInstance().getAppVersion()
      + "</H2><HR>"
//      + PValue_2007_paper.REFERENCE_HTML
      + "an open-source genome sequence assembly viewer and high-value SNP-identification tool"
      + "<br>"
      + "<br>"
//      + LogBB_2007_paper.REFERENCE_HTML
      + "<br>"
      + "<br>"
      + "Copyright &copy 2009 dmitry.a.konovalov@gmail.com"
      + "<br>"
      + "<br>"
      + "System Info:"
      + "<br>"
      + "<pre>" + new MemoryInfo() + "</pre>"
      + "<pre>" + SysProp.getInfo() + "</pre>"
      + "</HTML>"
      ;
    return help;
  }

}