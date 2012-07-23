package atom.wf.lcr;
import math.Mathx;
import math.func.FuncVec;
import math.vec.Vec;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 23/07/12, 11:47 AM
 */
public class YkLcrV2 {
public static Log log = Log.getLog(YkLcrV2.class);
//private final static double[] yTmp = new double[10]; // tmp array for zk_OLD
private final Vec wf;
private final Vec wf2;
private final int k;
private final WFQuadrLcr quadr;
private final double[] cr2;//(c+r)^2
private final double[] cr;//(c+r)
private final double[] r;
private final double[] divR;
private final Vec vR;

private static double h;
private static double eh; // exp(-h)
private static double h2;
private static double h3;
private static double h90;
private static double a34;


public YkLcrV2(final WFQuadrLcr quadr, final Vec wf, final Vec wf2, final int k) {
  this.k = k;
  this.wf = wf;
  this.wf2 = wf2;
  this.quadr = quadr;
  vR = quadr.getR();
  TransLcrToR xToR = quadr.getLcrToR();
  this.r = xToR.getArr();
  this.divR = xToR.getDivR().getArr();
  this.cr2 = xToR.getCR2().getArr();
  this.cr = xToR.getCR().getArr();

  h = ((StepGrid) xToR.getX()).getGridStep();
  h2 = h / 2.0;
  h3 = h / 3.0;
  h90 = h / 90.;
  eh = Math.exp(-h);
  a34 = 34. * h90;

}
public FuncVec calcZk() {
  FuncVec fvRes = new FuncVec(vR);
  double[] res = fvRes.getArr();
  int n = res.length;

  double f1 = cr2[0] * wf.get(0) * wf2.get(0);
  double f2 = cr2[1] * wf.get(1) * wf2.get(1);
  double f3 = cr2[2] * wf.get(2) * wf2.get(2);
  double f4 = cr2[3] * wf.get(3) * wf2.get(3);
  for (int i = 4; i < n; i++) {
    double f5 = cr2[i] * wf.get(i) * wf2.get(i);//      F5 = (RR(M)*P(M,I))*P(M,J)                                        AATK4125

    // Zim = (ri/rim)^k Zi + I^im_i FF y^2 * (r/rim)^k dx;  where ri=r_i, rim=r_(i+m)
    // the same as in Froese-Fischer but replacing
    //  exp(-mhk) with (ri/rim)^k;   and
    //  r*exp(k(x-xim)) with  y^2*(r/rim)^k

    // Replace exp(-mhk) with (ri/rim)^k;  where m=2
    //      EH = DEXP(-h)   // from SUBROUTINE INIT
    //      A = EH**K
    double ovR = divR[i - 1];
    double a3 = Mathx.pow(r[i - 4] * ovR, k) * h90;//      A3 = A2*A*H90                                                     AATK4112
    double a2 = Mathx.pow(r[i - 3] * ovR, k);//      A2 = A*A                                                          AATK4110
    double a1 = Mathx.pow(r[i - 2] * ovR, k) * 114.0 * h90;//      AN = 114.D0*A*H90                                                 AATK4114
    double ai = Mathx.pow(r[i - 0] * ovR, k) * h90;//      AI = H90/A                                                        AATK4113

    // From Froese Fischer etal Computational atomic structure 2000
    // DELTA^2 F = F2 - 2*F3 + F4
    // DELTA^4 F = F1 - 4*F2 + 6*F3 - 4*F4 + F5
    // INTL F(x)dx = h(F3 + DELTA^2 F/3 - DELTA^4 F/90) + O(h^7) =
    //  = h/90 [114*F3 + 34*(F2+F4) - F1 - F5]
    //
    //      YK(M-1) = YK(M-3)*A2 + ( AN*F3 + A34*(F4+A2*F2)-F5*AI-F1*A3)
    res[i-1] = res[i-3] * a2 + (a1 * f3 + a34 * (f4 + a2 * f2) - f5 * ai - f1 * a3);
    f1 = f2;
    f2 = f3;
    f3 = f4;
    f4 = f5;
  }
  // NOTE: missing i=n-1
  if (k == 0) {  // correct from last two points
    double norm = quadr.calcInt(wf, wf2);
    double corr = norm - res[n - 2];
    for (int i = n-2; i > 0; i -= 2) { // do not correct i=0
      res[i] += corr;
    }
    corr = norm - res[n - 3];
    for (int i = n-3; i > 0; i -= 2) { // do not correct i=0
      res[i] += corr;
    }
  }
  //last point
  double a1 = Mathx.pow(r[n-2] * divR[n-1], k);
  res[n-1] = a1 * res[n-2];
  return fvRes;
}
}
