package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,4/10/11,2:21 PM
 */
public class BaseTest {
  public static double ABS_MAX_ERROR = Math.pow(0.5, Double.SIZE / 1.5);   //1.4323643994144677E-13
//  public static double ABS_MAX_ERROR = 1E-12;
//  public static double ABS_MAX_ERROR = Math.pow(0.5, Double.SIZE / 2);   //2.3283064365386963E-10
  protected void testMaxAbsErr(double expected, double result) {
    if (Math.abs(expected - result) > ABS_MAX_ERROR)
      throw new AssertionError("Math.abs(expected="+expected+" - result="+result+") > ABS_MAX_ERROR="+ABS_MAX_ERROR);
    else
      System.out.println("Math.abs(expected="+expected+" - result="+result+") <= ABS_MAX_ERROR="+ABS_MAX_ERROR);
  }
  protected void testEquals(double expected, double result) {
    if (expected != result)
      throw new AssertionError("expected="+expected+" != result="+result);
  }

}
