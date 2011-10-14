package lapack4j.utils;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,7/10/11,12:16 PM
 */
public class Fortran77 {
  private static final int SUBROUTINE_LEN = 11;
  public static double sign(double a, double b) {
//http://gcc.gnu.org/onlinedocs/gcc-3.3.6/g77/Sign-Intrinsic.html#Sign-Intrinsic
//Returns `ABS(A)*s', where s is +1 if `B.GE.0', -1 otherwise.
    double s = (b >= 0.0D) ? 1.0D : -1.0D;
    return Math.abs(a) * s;
  }
  public static boolean comment(String line) {
    if (line.isEmpty())
      return false;
    return !Character.isWhitespace(line.charAt(0));
  }
  public static boolean endif(String line) {
    if (line.isEmpty())
      return false;
    return (line.startsWith("ENDIF") || line.startsWith("END IF"));
  }
  public static boolean subroutine(String line) {
    if (line.isEmpty())
      return false;
    return (line.startsWith("SUBROUTINE") || line.startsWith("subroutine"));
  }
  public static String getSubroutine(String line) {
    if (line.isEmpty())
      return "";
    if (!subroutine(line))
      return "ERROR: !subroutine(line)";
    return line.substring(SUBROUTINE_LEN);
  }
}
