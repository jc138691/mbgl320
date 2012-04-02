package papers.hy_swave.ees;
import atom.shell.Ls;
import atom.shell.ShPair;
import atom.shell.Shell;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.IFuncArr;
import math.vec.Vec;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e2.ScttMthdBaseE2;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 22/03/12, 10:44 AM
 */
public class EesMthdBaseE2 extends ScttMthdBaseE2 {
public static Log log = Log.getLog(EesMthdBaseE2.class);
protected FuncArr freeS;
protected FuncArr phiS;
protected FuncArr pnS;    // pnS = \oveline{\psi} =  \hat{P}_N \psi = \psi - \phi
protected FuncArr phiC;
public EesMthdBaseE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
protected void loadFreeS(int sysIdx, LgrrOrthLcr orthN, int chNum) {
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  freeS = new FuncArr(x);

  Vec tEngs = trgtE2.getEngs();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double sE = sEngs.get(sysIdx);  // system total eng
    double tScattE = sE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec tPsi = EesMethodE1.calcChPsiReg(tScattE, orthN);
    freeS.add(tPsi);
  }
}
protected void loadPWaveS_OLD(int sysIdx, LgrrOrthLcr orthN, int chNum) {
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  phiS = new FuncArr(x);

  Vec tEngs = trgtE2.getEngs();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double sE = sEngs.get(sysIdx);  // system total eng
    double tScattE = sE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec tPhiS = EesMethodE1.calcPWaveS(tScattE, orthN);
    phiS.add(tPhiS);
  }
}
protected void loadPWaveS(double sysTotE, LgrrOrthLcr orthN, int chNum) {
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  phiS = new FuncArr(x);

  Vec tEngs = trgtE2.getEngs();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double tScattE = sysTotE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec tPhiS = EesMethodE1.calcPWaveS(tScattE, orthN);
    phiS.add(tPhiS);
  }
}
protected void loadPnS(int sysIdx, LgrrOrthLcr orthN, int chNum) {
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  pnS = new FuncArr(x);

  Vec tEngs = trgtE2.getEngs();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double sE = sEngs.get(sysIdx);  // system total eng
    double tScattE = sE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec tPhiS = EesMethodE1.calcPWavePnS(tScattE, orthN);
    pnS.add(tPhiS);
  }
}
protected void loadPWaveC(double sysTotE, LgrrOrthLcr orthN, int chNum) {
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  phiC = new FuncArr(x);

  Vec tEngs = trgtE2.getEngs();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double tScattE = sysTotE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec tPhiC = EesMethodE1.calcPWaveC(tScattE, orthN);
    phiC.add(tPhiC);
  }
}
protected void loadPWaveC_OLD(int sysIdx, LgrrOrthLcr orthN, int chNum) {
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  phiC = new FuncArr(x);

  Vec tEngs = trgtE2.getEngs();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double sE = sEngs.get(sysIdx);  // system total eng
    double tScattE = sE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec tPhiC = EesMethodE1.calcPWaveC(tScattE, orthN);
    phiC.add(tPhiC);
  }
}
protected void loadTrialWfs(int sysIdx, LgrrOrthLcr orthN, int chNum) {
  loadFreeS(sysIdx, orthN, chNum);
  loadPWaveS_OLD(sysIdx, orthN, chNum);
  loadPWaveC_OLD(sysIdx, orthN, chNum);
}
protected ShPair makeShPair(Shell sh, FuncVec wf, int id, int L, Ls LS) {
  Shell sh2 = new Shell(id, wf, L);
  ShPair res = new ShPair(sh, sh2, LS);
  return res;
}
}
