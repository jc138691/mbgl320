package qm_station.ucm;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunTask;

import javax.utilx.log.Log;
import javax.swingx.progress.UCMonitorTaskProgress;

import qm_station.QMS;
import qm_station.ucm.save.lcr.*;
import qm_station.ucm.save.UCSaveFuncArrPotR;
import qm_station.ucm.plot.lcr.*;
import qm_station.ui.MenuScattUI;
import qm_station.ui.scatt.JmPotOptLCRView;
import qm_station.ui.scatt.JmPotOptView;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 24/09/2008, Time: 11:21:43
 */
public class UCShowJmPotLCR_UI extends UCShowJmPotUI {
  public static Log log = Log.getLog(UCShowJmPotR_UI.class);

  public boolean run() { log.dbg("run UCShowJmPotLCR_UI");
    return super.run();
  }
  protected JmPotOptView makeOptView(QMS project) {
    return new JmPotOptLCRView(project);
  }
  protected UCRunTask<QMS> makeApply(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCRunJmPotLCR(unitUI));
  }
  protected UCRunTask<QMS> makeTest(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCTestJmPotLCR(unitUI));
  }

  // Laguerre
  protected UCRunTask<QMS> makeLgrrPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotJmLagrrLCR(unitUI));
  }
  protected UCRunTask<QMS> makeLgrrSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotLCR(new UCPlotJmLagrrLCR(unitUI));
  }

//  // SIN
//  protected UCRunTask<QMS> makeSinPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotSinPWaveLCR(unitUI));
//  }
//  protected UCRunTask<QMS> makeSinSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotLCR(new UCPlotSinPWaveLCR(unitUI));
//  }
//
//  // COS
//  protected UCRunTask<QMS> makeCosPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotCosPWaveLCR(unitUI));
//  }
//  protected UCRunTask<QMS> makeCosSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotLCR(new UCPlotCosPWaveLCR(unitUI));
//  }

//  // Bi-diagonal
//  protected UCRunTask<QMS> makeBiPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotJmLagrrBiLCR(unitUI));
//  }
//  protected UCRunTask<QMS> makeBiSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotLCR(new UCPlotJmLagrrBiLCR(unitUI));
//  }

  // Orthonormal
  protected UCRunTask<QMS> makeOrthPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotJmLagrrOrthLCR(unitUI));
  }
  protected UCRunTask<QMS> makeOrthSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotLCR(new UCPlotJmLagrrOrthLCR(unitUI));
  }

  // Potential
  protected UCRunTask<QMS> makePotPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotPotLCR(unitUI));
  }
  protected UCRunTask<QMS> makePotSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotLCR(new UCPlotPotLCR(unitUI));
  }

  // Numerov
  protected UCRunTask<QMS> makeNmrvPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotPotNumerovLCR(unitUI));
  }
  protected UCRunTask<QMS> makeNmrvSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotLCR(new UCPlotPotNumerovLCR(unitUI));
  }

  // Eigvec
  protected UCRunTask<QMS> makePotEigFuncPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotPotEigFuncLCR(unitUI));  }
  protected UCRunTask<QMS> makePotEigFuncSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotR(new UCPlotPotEigFuncLCR(unitUI));   }

  // EigenValues
  protected UCRunTask<QMS> makePotEigValPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotPotEigValLCR(unitUI));  }
  protected UCRunTask<QMS> makePotEigValSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotR(new UCPlotPotEigValLCR(unitUI)); }

  protected void setPotUI(MenuScattUI scattUI, DefaultTaskUI<QMS> unitUI) {
    scattUI.setPotLCR(unitUI);
  }


}