package scatt.partial.wf.k_matrix_idea;
import math.func.FuncVec;
import math.func.Func;
import math.vec.Vec;
import scatt.Scatt;
import scatt.partial.wf.CosLFunc;
import scatt.partial.wf.SinLFunc;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 10:49:27
 */
public class KMtrxR {
  public static Log log = Log.getLog(KMtrxR.class);
  public static double calc(FuncVec f, double E, int idx) {
    Vec r = f.getX();
    double x = r.get(idx);                  log.dbg("x =", x);
    double x2 = r.get(idx+1);               log.dbg("x2 =", x2);

    double y = f.get(idx);                  log.dbg("y =", y);
    double y2 = f.get(idx+1);               log.dbg("y2 =", y2);

    double p = Scatt.calcMomFromE(E);       log.dbg("p = ", p);

    Func sinF = new SinLFunc(p, 0);  // L=0
    double S = sinF.calc(x);                log.dbg("S =", S);
    double S2 = sinF.calc(x2);              log.dbg("S2 =", S2);

    Func cosF = new CosLFunc(p, 0);  // L=0
    double C = cosF.calc(x);                log.dbg("C =", C);
    double C2 = cosF.calc(x2);              log.dbg("C2 =", C2);

    double res = y * S2 - y2 * S;           log.dbg("top = y * S2 - y2 * S =", res);
    double b = y2 * C - y * C2;             log.dbg("bot = y2 * C - y * C2 =", b);
    res /= b;                               log.dbg("K = top / bot =", res);
    return res;
  }
  public static double calc(FuncVec f, double E) {
    return calc(f, E, f.size()-2);
  }
}
