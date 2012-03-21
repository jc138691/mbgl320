package scatt;
import flanagan.complex.Cmplx;
import math.Mathx;
import math.complex.CmplxMtrx;
import math.mtrx.Mtrx;
import math.mtrx.MtrxFactory;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 16:27:22
 */
public class Scatt {
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
public static CmplxMtrx calcSFromK(Mtrx mK) {
  int chNum = mK.getNumRows();
  double[][] diag = MtrxFactory.makeOneDiag(chNum).getArray();
  double[][] k = mK.getArray();
  CmplxMtrx zp = new CmplxMtrx(chNum, chNum); // (1+iK)
  CmplxMtrx zm = new CmplxMtrx(chNum, chNum); // (1-iK)
  for (int r = 0; r < chNum; r++) {
    for (int c = 0; c < chNum; c++) {
      zp.set(r, c, new Cmplx(diag[r][c],  k[r][c]));
      zm.set(r, c, new Cmplx(diag[r][c], -k[r][c]));
    }
  }
  CmplxMtrx zmInv = zm.inverse();
  CmplxMtrx res = zp.times(zmInv);
  return res;
}
}
