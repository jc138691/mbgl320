package math;

import math.func.FactLn;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 09:44:18
 */
public class Mathx {
  private static FactLn fact = FactLn.getInstance();

  public static int mod(int a, int b) { // fortran remainder
    return a % b; // remainder
  }

  public static double factLn(int n) {
    return fact.calc(n);
  }

  public static double factorial(int n) {
    return Math.exp(factLn(n));
  }
  /*
  n!/(n-k)!=n*(n-1)*...*(n-k+1)
  */

  public static double factLn2(double n, int k) {
    if (n < 2 || n < k)
      return 0;
    double res = 0;
    for (double i = n - k + 1; i <= n; i++)
      res += Math.log(i);
    return res;
  }
  /*
  n!/[(n-k)! n^k]=n*(n-1)*...*(n-k+1)
  */

  public static double factLn3(double n, int k) {
    if (n < 2 || n < k)
      return 0;
    double res = 0;
    for (double i = n - k + 1; i <= n; i++)
      res += Math.log((double) i / n);
    return res;
  }

  public static double fact2(double n, int k) {
    return Math.exp(factLn2(n, k));
  }

  public static double pow(double x, int k) {
    double res = x;
    switch (Math.abs(k)) {
      case 0:
        return 1.;
      case 10:
        res *= x;
      case 9:
        res *= x;
      case 8:
        res *= x;
      case 7:
        res *= x;
      case 6:
        res *= x;
      case 5:
        res *= x;
      case 4:
        res *= x;
      case 3:
        res *= x;
      case 2:
        res *= x;
      case 1:
        break;
      default:
        return Math.pow(x, k);
    }
    if (Mathx.isZero(res))
      return 0.;
    return (k > 0) ? res : 1. / res;
  }

  private static boolean isZero(double res) {
    return Double.compare(res, 0) == 0;
  }

  public static int max(int a, int b, int c) {
    return Math.max(Math.max(a, b), c);
  }

  public static int max(int a, int b, int c, int d) {
    return Math.max(Math.max(a, b), Math.max(c, d));
  }

  public static int min(int a, int b, int c) {
    return Math.min(Math.min(a, b), c);
  }

  public static int min(int a, int b, int c, int d) {
    return Math.min(Math.min(a, b), Math.min(c, d));
  }

  public static byte min(byte a, byte b) {
    return a < b ? a : b;
  }

  public static byte max(byte a, byte b) {
    return a > b ? a : b;
  }

  public static int delta(Object a, Object b) {
    return (a.equals(b)) ? 1 : 0;
  }

  public static int dlt(int i, int j) {
    return (i == j) ? 1 : 0;
  }

  public static double binomialCoeffLn(int n, int k) {
    return factLn(n) - factLn(n - k) - factLn(k);
  }

  public static double binomialCoeff(int n, int k) {
    return Math.exp(Mathx.binomialCoeffLn(n, k));
  }

  public static int limit(int v, int minVal, int maxVal) {
    v = Math.min(v, maxVal);
    v = Math.max(minVal, v);
    return v;
  }

  public static int step(int i) {
    if (i <= 0)
      return 0;
    return 1;
  }
}
