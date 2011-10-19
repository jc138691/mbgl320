package lapack4j.utils;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,18/10/11,2:20 PM
 */
public class IntrFuncs {
  public static void main(String[] args) {
    System.out.println("EPSILON(0d)=" + EPSILON(0d));
    System.out.println("EPSILON(0f)=" + EPSILON(0f));
    System.out.println("EPSILON(1d)=" + EPSILON(1d));
    System.out.println("EPSILON(1f)=" + EPSILON(1f));

    System.out.println("DIGITS(0d)=" + DIGITS(0d));
    System.out.println("DIGITS(0f)=" + DIGITS(0f));
    System.out.println("RADIX(0)=" + RADIX(0));
    System.out.println("TINY(0d)=" + TINY(0d));
    System.out.println("TINY(0f)=" + TINY(0f));
    System.out.println("HUGE(0d)=" + HUGE(0d));
    System.out.println("HUGE(0f)=" + HUGE(0f));
    System.out.println("MINEXPONENT(0d)=" + MINEXPONENT(0d));
    System.out.println("MINEXPONENT(0f)=" + MINEXPONENT(0f));
    System.out.println("MAXEXPONENT(0d)=" + MAXEXPONENT(0d));
    System.out.println("MAXEXPONENT(0f)=" + MAXEXPONENT(0f));

    System.out.println("ICHAR('A')=" + ICHAR('A'));
    System.out.println("ICHAR('a')=" + ICHAR('a'));
    System.out.println("ICHAR('Z')=" + ICHAR('Z'));
    System.out.println("ICHAR('z')=" + ICHAR('z'));
  }

  public static int ICHAR(char ch) {
    return Character.getNumericValue(ch);
  }
  public static int RADIX(double x) {
    return 2;
  }

  public static int MINEXPONENT(double x) {
    return Double.MIN_EXPONENT;
  }
  public static int MINEXPONENT(float x) {
    return Float.MIN_EXPONENT;
  }
  public static int MAXEXPONENT(double x) {
    return Double.MAX_EXPONENT;
  }
  public static int MAXEXPONENT(float x) {
    return Float.MAX_EXPONENT;
  }
  public static int DIGITS(double x) {
    double base = 1.d;
    double machEps = 1.0d;
    int count = 2;
    for (; ; count++) {
      machEps /= 10.0d;
      if ((base + (machEps/10.0d)) == base)
        break;
    }
    return count;
  }
  public static int DIGITS(float x) {
    float base = 1.f;
    float machEps = 1.0f;
    int count = 2;
    for (; ; count++) {
      machEps /= 10.0f;
      if ((base + (machEps/10.0f)) == base)
        break;
    }
    return count;
  }
  public static double MAX(double a, double b) {
    return Math.max(a, b);
  }
  public static double ABS(double x) {
    return Math.abs(x);
  }
  public static double MIN(double a, double b) {
    return Math.min(a, b);
  }
  public static double TINY(double x) {
    return Double.MIN_VALUE;
  }
  public static float TINY(float x) {
    return Float.MIN_VALUE;
  }
  public static double HUGE(double x) {
    return Double.MAX_VALUE;
  }
  public static float HUGE(float x) {
    return Float.MAX_VALUE;
  }
  //http://en.wikipedia.org/wiki/Machine_epsilon#Approximation_using_Java
  public static double EPSILON(double base) {
    double machEps = 1.0d;
    for (; ;) {
      machEps /= 2.0d;
      if ((base + (machEps/2.0d)) == base)
        break;
    }
//    System.out.println( "Calculated machine epsilon: " + machEps );
    return machEps;
  }
  public static float EPSILON(float base) {
    float machEps = 1.0f;
    for (; ;) {
      machEps /= 2.0f;
      if ((base + (machEps/2.0f)) == base)
        break;
    }
//    System.out.println( "Calculated machine epsilon: " + machEps );
    return machEps;
  }
}
