package scatt;
import flanagan.complex.Cmplx;
import math.complex.CmplxMtrx;
import math.complex.CmplxMtrxDbgView;
import math.mtrx.api.Mtrx;
import math.mtrx.MtrxDbgView;
import math.mtrx.MtrxFactory;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 16:27:22
 */
public class Scatt {
public static Log log = Log.getLog(Scatt.class);
  public static double calcMomFromE(double E) { // momentum
    return Math.sqrt(2. * E);
  }
  public static double calcEFromMom(double p) {
    return 0.5 * p * p;
  }
public static Cmplx calcSFromK(double R) {
  Cmplx res = new Cmplx(1., R).div(new Cmplx(1., -R));
  return res;
}
public static Cmplx calcSFromF(double f, double momP) {
  Cmplx res = new Cmplx(1, 2.* momP * f);
  return res;
}
public static double calcKFromS(Cmplx s) {
  Cmplx mi = Cmplx.ONE.minus(s);
  Cmplx ad = Cmplx.ONE.add(s);
  Cmplx res = (mi.div(ad)).mult(Cmplx.i);
  return res.getRe();
}
public static double calcSigmaPiFromS(Cmplx s, double scattE) {
  double k = Scatt.calcMomFromE(scattE);
  double k2 = k * k;
  double res = Math.PI * (s.add(-1)).abs2() / k2;
  return res;
}
public static CmplxMtrx calcSFromK(Mtrx mK, int openChN) {
  int chNum = openChN;
  double[][] diag = MtrxFactory.makeOneDiag(chNum).getArr2D();
  double[][] k = mK.getArr2D();
  CmplxMtrx zp = new CmplxMtrx(chNum, chNum); // (1+iK)
  CmplxMtrx zm = new CmplxMtrx(chNum, chNum); // (1-iK)
  for (int r = 0; r < chNum; r++) {
    for (int c = 0; c < chNum; c++) {
      zp.set(r, c, new Cmplx(diag[r][c],  k[r][c]));
      zm.set(r, c, new Cmplx(diag[r][c], -k[r][c]));
    }
  }
  CmplxMtrx zmInv = null;
  try {
    zmInv = zm.inverse();
    log.dbg("(1-iR)^{-1}=\n", new CmplxMtrxDbgView(zm));
  } catch (java.lang.ArithmeticException ae) {
    log.info("K=\n", new MtrxDbgView(mK));
    log.info("(1-iR)=\n", new CmplxMtrxDbgView(zm));
    throw ae;
  }
  CmplxMtrx res = zp.times(zmInv);
  return res;
}
public static double calcSdcsNormE2E(double eA, double eB, double e0) {
  double ka = Scatt.calcMomFromE(eA);
  double kb = Scatt.calcMomFromE(eB);
  double k0 = Scatt.calcMomFromE(e0);
  double res = 8. / (ka * kb * k0 * k0 * k0);
  return res;
}
}
