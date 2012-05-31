package math;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 10:38:19
*/
public class Calc {
public static final double EPS_2 = 1e-2;
public static final double EPS_3 = 1e-3;
public static final double EPS_4 = 1e-4;
public static final double EPS_5 = 1e-5;
public static final double EPS_6 = 1e-6;
public static final double EPS_7 = 1e-7;
public static final double EPS_8 = 1e-8;
public static final double EPS_9 = 1e-9;
public static final double EPS_10 = 1e-10;
public static final double EPS_11 = 1e-11;
public static final double EPS_12 = 1e-12;
public static final double EPS_13 = 1e-13;
public static final double EPS_14 = 1e-14;
public static final double EPS_15 = 1e-15;
public static final double EPS_16 = 1e-16;
public static final double EPS_17 = 1e-17;
public static final double EPS_18 = 1e-18;
public static final double EPS_32 = 1e-32;
public static final double EPS_100 = 1e-100;

private static final double IGNORE = 1e-32;
public static boolean isZero(double v) {
  return Math.abs(v) < IGNORE;
}
public static int prty(int x) { // parity: (-1)^x
  return (x%2 == 0) ? 1: -1;
}
public static double cosFromTan(double tanX) {
  double a = 1. + tanX * tanX;
  double res = Math.sqrt(1. / a);
  return res;
}
public static double sinFromCos(double cosX) {
  double a = 1. - cosX * cosX;
  double res = Math.sqrt(a);
  return res;
}
}
