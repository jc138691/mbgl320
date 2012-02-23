package qm_station.ucm;
import project.ucm.UCController;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunTask;

import javax.utilx.log.Log;

import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.QMSMainUI;
import qm_station.QMSFrame;
import qm_station.ui.MenuScattUI;
import qm_station.ui.scatt.JmPotOptView;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 22/10/2008, Time: 12:46:12
 */
public abstract class UCShowJmPotUI implements UCController {
  public static Log log = Log.getLog(UCShowJmPotR_UI.class);

  public boolean run() { log.dbg("run UCShowJmPotUI()");
    QMS project = QMSProject.getInstance();
    QMSMainUI ui = QMSMainUI.getInstance();

    MenuScattUI scattUI = ui.getScattUI(); // NOTE: it makes it as well
    JmPotOptView optView = makeOptView(project); // to override
    DefaultTaskUI<QMS> flowUI = new DefaultTaskUI<QMS>(optView, QMSFrame.getInstance());

    flowUI.setApplyAction(makeApply(flowUI));
    optView.setRunTest(makeTest(flowUI));
        
//    optView.setRunPlot(makeLgrrPlot(flowUI), CalcOptE1.OPT_LGRR);
//    optView.setRunSave(makeLgrrSave(flowUI), CalcOptE1.OPT_LGRR);
//
//    optView.setRunPlot(makeOrthPlot(flowUI), CalcOptE1.OPT_ORTH);
//    optView.setRunSave(makeOrthSave(flowUI), CalcOptE1.OPT_ORTH);
//
//    optView.setRunPlot(makePotPlot(flowUI), CalcOptE1.OPT_POT);
//    optView.setRunSave(makePotSave(flowUI), CalcOptE1.OPT_POT);
//
//    optView.setRunPlot(makeNmrvPlot(flowUI), CalcOptE1.OPT_NUMEROV);
//    optView.setRunSave(makeNmrvSave(flowUI), CalcOptE1.OPT_NUMEROV);
//
//    optView.setRunPlot(makeEngKNmrvPlot(flowUI), CalcOptE1.OPT_K_MTRX_ENG);
//    optView.setRunSave(makeEngKNmrvSave(flowUI), CalcOptE1.OPT_K_MTRX_ENG);
//
//    optView.setRunPlot(makeKNmrvArrPlot(flowUI), CalcOptE1.OPT_K_MTRX_FUNC_ARR);
//    optView.setRunSave(makeKNmrvArrSave(flowUI), CalcOptE1.OPT_K_MTRX_FUNC_ARR);
//
//    optView.setRunPlot(makeEngGnnPlot(flowUI), CalcOptE1.OPT_GNN_ENG);
//    optView.setRunSave(makeEngGnnSave(flowUI), CalcOptE1.OPT_GNN_ENG);
//
//    optView.setRunPlot(makeEngKnnPlot(flowUI), CalcOptE1.OPT_KNN_ENG);
//    optView.setRunSave(makeEngKnnSave(flowUI), CalcOptE1.OPT_KNN_ENG);

    setPotUI(scattUI, flowUI);
    return true;
  }
  // @override
  protected void setPotUI(MenuScattUI scattUI, DefaultTaskUI<QMS> unitUI) {
  }
  // @override
  protected JmPotOptView makeOptView(QMS project) {
    return null;
  }
  // @override
  protected UCRunTask<QMS> makeApply(DefaultTaskUI<QMS> unitUI) {
    return null;
  }
  // @override
  protected UCRunTask<QMS> makeTest(DefaultTaskUI<QMS> unitUI) {
    return null;
  }

  protected UCRunTask<QMS> makeLgrrPlot(DefaultTaskUI<QMS> unitUI) { return null; }
  protected UCRunTask<QMS> makeLgrrSave(DefaultTaskUI<QMS> unitUI) { return null; }

//  protected UCRunTask<QMS> makeSinPlot(DefaultTaskUI<QMS> unitUI) { return null; }
//  protected UCRunTask<QMS> makeSinSave(DefaultTaskUI<QMS> unitUI) { return null; }
//
//  protected UCRunTask<QMS> makeCosPlot(DefaultTaskUI<QMS> unitUI) { return null; }
//  protected UCRunTask<QMS> makeCosSave(DefaultTaskUI<QMS> unitUI) { return null; }
//
//  protected UCRunTask<QMS> makeBiPlot(DefaultTaskUI<QMS> unitUI) { return null; }
//  protected UCRunTask<QMS> makeBiSave(DefaultTaskUI<QMS> unitUI) { return null; }
//
  protected UCRunTask<QMS> makeOrthPlot(DefaultTaskUI<QMS> unitUI) { return null; }
  protected UCRunTask<QMS> makeOrthSave(DefaultTaskUI<QMS> unitUI) { return null; }

  protected UCRunTask<QMS> makePotPlot(DefaultTaskUI<QMS> unitUI) { return null; }
  protected UCRunTask<QMS> makePotSave(DefaultTaskUI<QMS> unitUI) { return null; }

  protected UCRunTask<QMS> makeNmrvPlot(DefaultTaskUI<QMS> unitUI) { return null; }
  protected UCRunTask<QMS> makeNmrvSave(DefaultTaskUI<QMS> unitUI) { return null; }

  protected UCRunTask<QMS> makeEngKNmrvPlot(DefaultTaskUI<QMS> unitUI) { return null; }
  protected UCRunTask<QMS> makeEngKNmrvSave(DefaultTaskUI<QMS> unitUI) { return null; }

  protected UCRunTask<QMS> makeKNmrvArrPlot(DefaultTaskUI<QMS> unitUI) { return null; }
  protected UCRunTask<QMS> makeKNmrvArrSave(DefaultTaskUI<QMS> unitUI) { return null; }

//  protected UCRunTask<QMS> makePotEigFuncPlot(DefaultTaskUI<QMS> unitUI) { return null; }
//  protected UCRunTask<QMS> makePotEigFuncSave(DefaultTaskUI<QMS> unitUI) { return null; }
//
//  protected UCRunTask<QMS> makePotEigValPlot(DefaultTaskUI<QMS> unitUI) { return null; }
//  protected UCRunTask<QMS> makePotEigValSave(DefaultTaskUI<QMS> unitUI) { return null; }
//
  protected UCRunTask<QMS> makeEngGnnPlot(DefaultTaskUI<QMS> unitUI) { return null; }
  protected UCRunTask<QMS> makeEngGnnSave(DefaultTaskUI<QMS> unitUI) { return null; }

  protected UCRunTask<QMS> makeEngKnnPlot(DefaultTaskUI<QMS> unitUI) { return null; }
  protected UCRunTask<QMS> makeEngKnnSave(DefaultTaskUI<QMS> unitUI) { return null; }

}