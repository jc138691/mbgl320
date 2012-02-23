package papers.hy_swave;

import atom.energy.part_wave.PartHMtrx;
import atom.energy.part_wave.PartHMtrxR;
import atom.wf.coulomb.CoulombWFFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmMethodE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.theory.JmD;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 11/02/2010, 10:37:01 AM
 */
public class Jm2010PotScatt extends Jm2010CommonR {
  public static Log log = Log.getLog(Jm2010PotScatt.class);
  public void setUp() {
    super.setUp();
    project = QMSProject.makeInstance("Jm2010PotScatt", "100211");

//    log = Log.getRootLog();
    log.info("log.info(Jm2010PotScatt)");
//    JmMethodE1.log.setDbg();
    log.setDbg();
  }

  public void testCalcPot() {
    LAMBDA = 1f;

    R_N = 3001;
    R_LAST = 100;

    ENG_FIRST = 0.01f;
    ENG_LAST = 0.5f;
    ENG_N = 50;

//    calcPot(24);
//    calcPot(22);
    calcPot(20);
    calcPot(18);
    calcPot(16);
    calcPot(14);
    calcPot(12);
    calcPot(10);
    calcPot(8);
  }

  
  public void calcPot(int newN) {
    N = newN;
    initJm();

    pot = CoulombWFFactory.makePotHy_1s_e(rVec);  log.dbg("V_1s(r)=", new VecDbgView(pot));

    PartHMtrx H = new PartHMtrxR(L, orth, pot);
//    PartH partH = H.makePartH();
    Vec eigEng = H.getEigVal();                     log.dbg("eigVal=", new VecDbgView(eigEng));
    FuncArr eigVec = H.getEigFuncArr();                 log.dbg("eigVec=", new FuncArrDbgView(eigVec));

    Vec D = new JmD(bi, eigVec);                log.dbg("D_{n,N-1}=", D);

    JmMethodE1 method = new JmMethodE1(calcOpt);
    method.setOverD(D);
    method.setSysEngs(eigEng);
    ScattRes res = method.calcEngGrid();                  log.dbg("res=", res);
//    ScattRes res = method.calcMidSysEngs();                  log.dbg("res=", res);

//    FuncVec func = new FuncVecToString(res.getCross());
//    FileX.writeToFile(func.toString(), HOME_DIR, "cross", "cross_"+ basisOptN.makeLabel()+".csv");

//    func = new FuncVecToString(res.getShift());
//    FileX.writeToFile(func.toString(), HOME_DIR, "shift", "shift_"+ basisOptN.makeLabel()+".csv");

    setupScattRes(res, method);
    res.writeToFiles();

    // JM-properties
//    EngModel eng = calcOpt.getGridEng();    log.dbg("Incident Energies =", eng);
//    if (!new JmJnnRTest(jmBasisN, eng).ok())      return;
//    if (!new JmJnmSCmRTest(jmBasisN, eng).ok())      return;

  }



}