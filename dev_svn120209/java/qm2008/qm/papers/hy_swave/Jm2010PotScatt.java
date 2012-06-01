package papers.hy_swave;

import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxR;
import atom.wf.coulomb.WfFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmMthdE1_OLD;
import scatt.jm_2008.jm.ScttRes;
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
//    JmMthdE1_OLD.log.setDbg();
    log.setDbg();
  }

  public void testCalcPot() {
    LAMBDA = 1f;

    R_N = 3001;
    R_LAST = 100;

    ENG_FIRST = 0.01f;
    ENG_LAST = 0.5f;
    ENG_N = 50;

//    calcPot2(24);
//    calcPot2(22);
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

    pot = WfFactory.makePotHy_1s_e(rVec);  log.dbg("V_1s(r)=", new VecDbgView(pot));

    PotHMtrx H = new PotHMtrxR(L, orth, pot);
//    PotH partH = H.makePotH();
    Vec eigEng = H.getEigEngs();                     log.dbg("eigVal=", new VecDbgView(eigEng));
    FuncArr eigVec = H.getEigWfs();                 log.dbg("eigVec=", new FuncArrDbgView(eigVec));

    Vec D = new JmD(bi, eigVec);                log.dbg("D_{n,N-1}=", D);

    JmMthdE1_OLD method = new JmMthdE1_OLD(calcOpt);
    method.setOverD(D);
    method.setSysEngs(eigEng);
    ScttRes res = method.calcForScatEngModel();                  log.dbg("res=", res);
//    ScttRes res = method.calcMidSysEngs();                  log.dbg("res=", res);

//    FuncVec func = new FuncVecToString(res.getCross());
//    FileX.writeToFile(func.toString(), HOME_DIR, "cross", "cross_"+ lgrrOptN.makeLabel()+".csv");

//    func = new FuncVecToString(res.getShift());
//    FileX.writeToFile(func.toString(), HOME_DIR, "shift", "shift_"+ lgrrOptN.makeLabel()+".csv");

    setupScattRes(res, method);
    res.writeToFiles();

    // JM-properties
//    EngModel eng = calcOpt.getGridEng();    log.dbg("Incident Energies =", eng);
//    if (!new JmJnnRTest(lgrrN, eng).ok())      return;
//    if (!new JmJnmSCmRTest(lgrrN, eng).ok())      return;

  }



}