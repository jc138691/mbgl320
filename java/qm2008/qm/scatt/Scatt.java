package scatt;
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
}
