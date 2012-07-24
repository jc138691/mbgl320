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
public FuncVec calcZk() {       log.setDbg();
  FuncVec fvRes = new FuncVec(vR);
  double[] res = fvRes.getArr();
  res[0] = 0;
  int n = res.length;

  double f1 = cr2[0] * wf.get(0) * wf2.get(0);
  if (f1 != 0) {
    throw new IllegalArgumentException(log.error("(f1 != 0)"));
  }
  double f2 = cr2[1] * wf.get(1) * wf2.get(1);
  double f3 = cr2[2] * wf.get(2) * wf2.get(2);
  double f4 = cr2[3] * wf.get(3) * wf2.get(3);
  double a, b, intgl;

  // NOTE!!! Integrating by r (not X)
  // y(r) = a r^b + c
  // f1 = c
  // f2 = a r2^b + c
  // f3 = a r3^b + c
  // f2 - f1 = a r1^b
  // f3 - f1 = a r2^b
  // (f3-f1)/(f2-f1)= (r3/r2)^b;
  double r2 = r[1];
  double r3 = r[2];
  double rk2 = Math.pow(r2, k);
  double rk3 = Math.pow(r3, k);
  double f21 = f2 * rk2 / cr[1];
  double f31 = f3 * rk3 / cr[2];
  b = Math.log(f31 / f21) / Math.log(r3 / r2);
  if (b <= 0) {
    throw new IllegalArgumentException(log.error("(b <= 0)"));
  }
  double hb = Math.pow(r2, b);
  a = f21 / hb;
  intgl = a *hb / (b+1) * r2;
  res[1] = intgl / rk2;

  hb = Math.pow(r3, b);
  intgl = a *hb / (b+1) * r3;
  res[2] = intgl / rk3;

  // Simpson rule
  // y(x) = a x^2 + b x + c
  // f1 = c
  // f2 = a h^2 + b h + c
  // f3 = 4 a h^2 + 2 b h + c
  //  double a = (f3 - 2. * f2 - c) / (2. * h * h);
  //  double b = (4. * f2 - f3 + 3c) / (2. * h);
  // int_0^h y(x) dx = a / 3. * h^3 + b h^2 / 2. + c h;
//  a = (f3 - 2. * f2 + f1) / 2.;
//  b = (4. * f2 - f3 - 3. * f1) / 2.;
//  intgl = (a / 3.  + b / 2. + f1) * h;
//  res[1] = res[0] + intgl;
//  res[1] = h2 * f2;   // trapezoidal

//  // Simpson rule
//  a = Mathx.pow(r[1] * divR[2], k);
//  //YK(3) = YK(1)*A2 + H3*(F3 + D4*A*F2 + A2*F1)
//  res[2] = h3*(f3 + 4.*a*f2);// NOTE!!! r[0]=Zk[0]=a2=f1=0 in the LCR grid

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
    // NOTE: missing i=n-1
    f1 = f2;
    f2 = f3;
    f3 = f4;
    f4 = f5;
  }
  // NOTE: missing i=n-1
  double norm = 0;
  double rkn2 = 1;
  double rkn3 = 1;
  if (k == 0) {  // correct from last two points
    norm = quadr.calcInt(wf, wf2);
  }
  else {
    norm = quadr.calcInt(wf, wf2, quadr.getPowR(k));
    rkn2 = 1. / quadr.getPowR(k).get(n - 2);
    rkn3 = 1. / quadr.getPowR(k).get(n - 3);
  }
  double corr = norm * rkn2 - res[n - 2];      log.info("corr=", corr);
  for (int i = n-2; i > 0; i -= 2) { // do not correct i=0
    res[i] += corr;
  }
  corr = norm * rkn3 - res[n - 3];             log.info("corr=", corr);
  for (int i = n-3; i > 0; i -= 2) { // do not correct i=0
    res[i] += corr;
  }

  //last point
  double a1 = Mathx.pow(r[n-2] * divR[n-1], k);
  res[n-1] = a1 * res[n-2];
  return fvRes;
}
}
