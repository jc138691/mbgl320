package papers.hy_swave;

import atom.energy.part_wave.PartHMtrx;
import atom.energy.part_wave.PartHMtrxLcr;
import atom.wf.coulomb.CoulombWFFactory;
import math.func.FuncVec;
import math.func.FuncVecToString;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmMethodE1;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.theory.JmD;

import javax.iox.FileX;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 8:47:43 AM
 */
public class Jm2010PotScattLcr extends Jm2010CommonLcr {
  public static Log log = Log.getLog(Jm2010PotScattLcr.class);
  public void setUp() {
    super.setUp();
    project = QMSProject.makeInstance("Jm2010PotScattLcr", "100215");

//    log = Log.getRootLog();
    log.info("log.info(Jm2010PotScattLcr)");
//    JmMethodE1.log.setDbg();
    log.setDbg();
  }
  public void testCalcPot() {
    LAMBDA = 1f;

    LCR_N = 301;
    R_LAST = 100;

    ENG_FIRST = 0.01f;
    ENG_LAST = 0.5f;
    ENG_N = 50;

    calcPotLcr(20);
    calcPotLcr(18);
    calcPotLcr(16);
    calcPotLcr(14);
    calcPotLcr(12);
    calcPotLcr(10);
//    calcJm(8);
  }

  public void calcPotLcr(int newN) {
    N = newN;
    initProject();
    initPotJm();

    pot = CoulombWFFactory.makePotHy_1s_e(rVec);  log.dbg("V_1s(r)=", new VecDbgView(pot));

    PartHMtrx sysH = new PartHMtrxLcr(L, orthonN, pot);
////    PartH partH = sysH.makePartH();
    Vec sysEngs = sysH.getEigVal();                     log.dbg("eigVal=", new VecDbgView(sysEngs));
    FuncArr sysWFuncs = sysH.getEigFuncArr();                 log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));

    Vec D = new JmD(biorthN, sysWFuncs);                log.dbg("D_{n,N-1}=", D);

    JmMethodE1 method = new JmMethodE1(jmOpt);
    method.setOverD(D);
    method.setSysEngs(sysEngs);
//    JmRes res = method.calcEngGrid();                  log.dbg("res=", res);
    JmRes res = method.calcMidSysEngs();                  log.dbg("res=", res);
//
    FuncVec cross = new FuncVecToString(res.getCross());
    FileX.writeToFile(cross.toString(), HOME_DIR, "cross", "cross_"+ basisOptN.makeLabel()+".csv");
  }
}