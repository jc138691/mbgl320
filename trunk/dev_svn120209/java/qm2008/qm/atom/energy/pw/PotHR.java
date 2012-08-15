package atom.energy.pw;
import atom.wf.WFQuadrD1;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 11:44:33
 */
public class PotHR extends PotH {
  public static Log log = Log.getLog(PotHR.class);
  public PotHR(WFQuadrD1 quadr) {
    super(quadr);
  }
  public double calcKin(int L, FuncVec wf, FuncVec wf2) {
    //                 1 d^2    L(L+1)
    // Hamiltonian = - - --   + ------
    //                 2 dr^2    2r^2
    double res = calcDrv2(wf, wf2);
    if (L == 0)
      return res;
    double L2 = 0.5 * (L * (L + 1));
    double potL2 = quadr.calcWithDivR2(wf, wf2);     log.dbg("potL2=", potL2);
    res += L2 * potL2;                                 log.dbg("res=", res);
    return res;
  }
}
