package qm_station.ucm;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunTask;

import javax.utilx.log.Log;
import javax.swingx.progress.UCMonitorTaskProgress;

import qm_station.QMS;
import qm_station.ucm.save.*;
import qm_station.ucm.plot.*;
import qm_station.ui.MenuScattUI;
import qm_station.ui.scatt.JmPotOptRView;
import qm_station.ui.scatt.JmPotOptView;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 10:37:01
 */
public class UCShowJmPotR_UI extends UCShowJmPotUI {
  public static Log log = Log.getLog(UCShowJmPotR_UI.class);

  public boolean run() { log.dbg("run UCShowJmPotR_UI");
    return super.run();
  }
  protected JmPotOptView makeOptView(QMS project) {
    return new JmPotOptRView(project);
  }
  protected UCRunTask<QMS> makeApply(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCRunJmPotR(unitUI));
  }
  protected UCRunTask<QMS> makeTest(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCTestJmPotR(unitUI));
  }

  // Laguerre
  protected UCRunTask<QMS> makeLgrrPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotJmLagrrR(unitUI));
  }
  protected UCRunTask<QMS> makeLgrrSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotR(new UCPlotJmLagrrR(unitUI));
  }

//  // SIN
//  protected UCRunTask<QMS> makeSinPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotSinPWaveR(unitUI));
//  }
//  protected UCRunTask<QMS> makeSinSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotR(new UCPlotSinPWaveR(unitUI));
//  }
//
//  // COS
//  protected UCRunTask<QMS> makeCosPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotCosPWaveR(unitUI));
//  }
//  protected UCRunTask<QMS> makeCosSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotR(new UCPlotCosPWaveR(unitUI));
//  }

//  // Bi-diagonal
//  protected UCRunTask<QMS> makeBiPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotJmLagrrBiR(unitUI));
//  }
//  protected UCRunTask<QMS> makeBiSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotR(new UCPlotJmLagrrBiR(unitUI));
//  }

  // Orthonormal
  protected UCRunTask<QMS> makeOrthPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotJmLagrrOrthR(unitUI));
  }
  protected UCRunTask<QMS> makeOrthSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotR(new UCPlotJmLagrrOrthR(unitUI));
  }

  // Potential
  protected UCRunTask<QMS> makePotPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotPotR(unitUI));
  }
  protected UCRunTask<QMS> makePotSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotR(new UCPlotPotR(unitUI));
  }

  // Numerov
  protected UCRunTask<QMS> makeNmrvPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotPotNmrvR(unitUI));
  }
  protected UCRunTask<QMS> makeNmrvSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotR(new UCPlotPotNmrvR(unitUI));
  }

  // K-matrix Numerov
  protected UCRunTask<QMS> makeEngKNmrvPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotPotEngKNmrvR(unitUI));
  }
  protected UCRunTask<QMS> makeEngKNmrvSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotR(new UCPlotPotEngKNmrvR(unitUI));
  }

  // K-matrix FuncArr from Numerov
  protected UCRunTask<QMS> makeKNmrvArrPlot(DefaultTaskUI<QMS> unitUI) {
    return new UCMonitorTaskProgress<QMS>(new UCPlotPotKNmrvArrR(unitUI));
  }
  protected UCRunTask<QMS> makeKNmrvArrSave(DefaultTaskUI<QMS> unitUI) {
    return new UCSaveFuncArrPotR(new UCPlotPotKNmrvArrR(unitUI));
  }

//  // Eigvec
//  protected UCRunTask<QMS> makePotEigFuncPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotPotEigFuncR(unitUI));  }
//  protected UCRunTask<QMS> makePotEigFuncSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotR(new UCPlotPotEigFuncR(unitUI));   }
//
//  // EigenValues
//  protected UCRunTask<QMS> makePotEigValPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotPotEigValR(unitUI));  }
//  protected UCRunTask<QMS> makePotEigValSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotR(new UCPlotPotEigValR(unitUI)); }

//  // Gnn
//  protected UCRunTask<QMS> makeEngGnnPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotEngGnnR(unitUI));
//  }
//  protected UCRunTask<QMS> makeEngGnnSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotR(new UCPlotEngGnnR(unitUI));
//  }

//  // Knn
//  protected UCRunTask<QMS> makeEngKnnPlot(DefaultTaskUI<QMS> unitUI) {
//    return new UCMonitorTaskProgress<QMS>(new UCPlotEngKnnR(unitUI));
//  }
//  protected UCRunTask<QMS> makeEngKnnSave(DefaultTaskUI<QMS> unitUI) {
//    return new UCSaveFuncArrPotR(new UCPlotEngKnnR(unitUI));
//  }

  protected void setPotUI(MenuScattUI scattUI, DefaultTaskUI<QMS> unitUI) {
    scattUI.setPotR(unitUI);
  }
}