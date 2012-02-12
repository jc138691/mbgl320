package lapack4j.utils.tests;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,4/10/11,2:21 PM
 */
public class TestUtils {
  public static final String EOL = System.getProperty("line.separator");
  public static boolean DEBUG = true;
  public static double ABS_MAX_ERROR = Math.pow(0.5, Double.SIZE / 1.5);   //1.4323643994144677E-13
//  public static double ABS_MAX_ERROR = 1E-12;
//  public static double ABS_MAX_ERROR = Math.pow(0.5, Double.SIZE / 2);   //2.3283064365386963E-10
  public static void assertMaxAbsErr(double expected, double result) {
    if (Math.abs(expected - result) > ABS_MAX_ERROR)
      throw new AssertionError("Math.abs(expected="+expected+" - result="+result+") > ABS_MAX_ERROR="+ABS_MAX_ERROR);
    else
      print("Math.abs(expected=" + expected + " - result=" + result + ") <= ABS_MAX_ERROR=" + ABS_MAX_ERROR);
  }
  public static  void assertEquals(double expected, double result) {
    if (expected != result)
      throw new AssertionError("expected="+expected+" != result="+result);
  }
  public static void debug(String line) {
    if (DEBUG)
      print(line);
  }
  public static void print(String str) {
    System.out.println(str);
  }
  public static void testIn(String str) {
    System.out.print(str + ": running ");
  }
  public static void testOk(String str) {
    print(str + ": ok");
  }
  public static void monitor(String str) {
    System.out.print(str);
  }
  public static void monitor(int i) {
    monitor(" " + i + " ");
  }
}
