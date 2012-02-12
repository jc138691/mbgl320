package atom.energy.part_wave;
import atom.wf.WFQuadr;
import math.func.FuncVec;
import math.integral.Quadr;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 11:46:18
 */
public abstract class PartH {
  public static Log log = Log.getLog(PartH.class);
  protected WFQuadr quadr;

  public PartH(WFQuadr quadr) {
    this.quadr = quadr;
  }
  public abstract double calcKin(int L, FuncVec wf, FuncVec wf2);

  public double calcPot(FuncVec pot, FuncVec wf, FuncVec wf2) {
    double res = quadr.calcPot(pot, wf, wf2);  log.dbg("res=", res);
    return res;
  }
  public double calcDrv2(FuncVec wf, FuncVec wf2) {
    double drv2 = quadr.calc(wf.getDrv(), wf2.getDrv());
    //          1 d^2
    // dir2 = - - --
    //          2 dr^2
    // note 0.5 instead of (-0.5), that is because U' * U' replaced U*U"
    // B            B               |B
    // I dr RR" = - I dr R'R' + RR' |  = - I drR'R' + RR'(B) - RR'(A)
    // A            A               |A
    double corrA = wf.getFirst() * wf2.getDrv().getFirst();
    double corrB = wf.getLast() * wf2.getDrv().getLast();
    double res = 0.5 * (drv2 - corrB + corrA);          log.dbg("res=", res);
    return res;
  }

}
