package papers.hy_swave.ees;
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
protected FuncArr phiC;
public EesMthdBaseE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
protected void loadTrialWfs(int sysIdx, LgrrOrthLcr orthN, int chNum) {
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  freeS = new FuncArr(x);
  phiS = new FuncArr(x);
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
    FuncVec tPsi = EesMethodE1.calcChPsiReg(tScattE, orthN);
    freeS.add(tPsi);

    FuncVec tPhiS = EesMethodE1.calcChPhiS(tScattE, orthN);
    phiS.add(tPhiS);

    FuncVec tPhiC = EesMethodE1.calcChPhiC(tScattE, orthN);
    phiC.add(tPhiC);
  }
}
}
