package scatt;
import flanagan.complex.Cmplx;
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
public static Cmplx calcSFromR(double R) {
  Cmplx res = new Cmplx(1., R).div(new Cmplx(1., -R));
  return res;
}
public static double calcSigmaPiFromS(Cmplx s, double scattE) {
  double k = Scatt.calcMomFromE(scattE);
  double k2 = k * k;
  double res = Math.PI * (s.add(-1)).abs2() / k2;
  return res;
}
}