package qm_station.ucm.plot;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import qm_station.QMSProject;
import qm_station.QMS;
import qm_station.jm.JmEngSC_NN1;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.eng.EngOpt;
import project.workflow.task.DefaultTaskUI;

import javax.utilx.log.Log;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/12/2008, Time: 14:10:05
 */
public class UCPlotEngKnnR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotEngKnnR.class);
  public UCPlotEngKnnR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
    add(log);
//    add(UCPlotFuncArr.log);
    add(JmEngSC_NN1.log);
//    add(KMtrxEngR.log);
//    add(KMtrxR.log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public FuncArr makeFuncArr() {
    FuncVec nmrvK = new UCPlotPotEngKNmrvR(getDefaultUi()).makeFuncVec();
    FuncVec jmK = makeJmK();
    FuncArr res = new FuncArr(jmK.getX(), 2);  // NOTE!! only two functions
    res.set(0, jmK);
    res.set(1, nmrvK);
    return res;
  }
  public FuncVec makeJmK() {
    QMS project = QMSProject.getInstance();
    JmCalcOptE1 model = project.getJmPotOptR();

    EngOpt eng = model.getGridEng();    log.dbg("eng model=", eng);
    JmEngSC_NN1 jmSC = new JmEngSC_NN1(model.getBasisOpt(), eng);

//    UCPlotEngGnnR gnn = new UCPlotEngGnnR(getDefaultUi());
//    FuncVec g = gnn.makeFuncVec();        log.dbg("G_{N,N-1}(E)=", g);

//    FuncVec res = new FuncVec(g.getX());
//    double[] sn = jmSC.getSn().getArr();
//    double[] sn1 = jmSC.getSn1().getArr();
//    double[] cn = jmSC.getCn().getArr();
//    double[] cn1 = jmSC.getCn1().getArr();
//    for (int i = 0; i < res.size(); i++) {
//      double gi = g.get(i);
//      double K = - (sn1[i] + gi * sn[i]) / (cn1[i] + gi * cn[i]);
//      res.set(i, K);
//    }
//    return res;
    return null;
  }
}