package qm_station.ucm.save;
import project.workflow.task.UCRunTask;
import project.workflow.task.DefaultResView;
import project.workflow.task.UCRunDefaultTask;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.QMSFrame;
import qm_station.ucm.plot.UCPlotJmLagrrR;
import qm_station.ucm.plot.UCPlotFuncArr;

import javax.utilx.log.Log;
import javax.swingx.SaveFileUI;
import javax.swingx.textx.TextView;
import javax.swing.*;
import javax.iox.FileX;
import javax.iox.TextFile;
import java.io.File;

import math.vec.grid.StepGrid;
import math.func.view.FuncArrPlot;
import math.func.view.FuncPlotView;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 16:11:50
 */
public abstract class UCSaveFuncArr extends UCRunDefaultTask<QMS> {
  public static Log log = Log.getLog(UCPlotJmLagrrR.class);
  private static SaveFileUI ui;
  protected UCPlotFuncArr plotUC;

  public UCSaveFuncArr(UCPlotFuncArr plotUC) {
    super(plotUC.getDefaultUi());
    this.plotUC = plotUC;
  }
  protected void setupLogs() {
    add(log);
    add(StepGrid.log);
    add(FuncArrPlot.log);
    add(FuncPlotView.log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }

  public abstract String getSaveFileName();
  public abstract void setSaveFileName(String name);

  public boolean run() {
    DefaultResView out = getOutView();
    addView(out.getSysView());    log.info("run UCSaveFuncArr");

    QMS project = QMSProject.getInstance();
    String name = getSaveFileName();   log.dbg("getSaveFileName()=", name); // ABSTRACT!!!!!!
    File file = project.makeFile(name);
    if (!file.exists())  {
      name = project.getResultsFileName();  log.dbg("getResultsFileName()=", name);
      file = project.makeFile(name);
    }
    if (ui == null) {
      ui = new SaveFileUI();
    }
    file = ui.selectFile(QMSFrame.getInstance(), file);
    if (file == null)
      return false;
    if (file.exists()) {
      if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(QMSFrame.getInstance()
        , "Replace existing \"" + file.getName() + "\" ?"))
        return false;
    }
    setSaveFileName(FileX.getFileName(file));
    project.saveProjectToDefaultLocation();

    // SAVE to file
    TextFile to = new TextFile();
    String text = plotUC.makeFuncArr().toCSV(); log.dbg("text=\n", text);
    to.addLine(text);
    to.setFileName(file.getName());

    TextView usr = out.getUsrView();
    out.setUsrView(usr);  // set focus

    usr.println("started writing to " + file.getName());
    to.write(file, QMSFrame.getInstance());

    usr.println("finished writing to " + file.getName());
//    ui.setStatus(header);
    file = null;  System.gc();

    log.info("end UCSaveFuncArr");
    return true;
  }
}