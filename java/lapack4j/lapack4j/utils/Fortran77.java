package lapack4j.utils;
import sun.org.mozilla.javascript.internal.Function;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,7/10/11,12:16 PM
 */
public class Fortran77 {
  private static final int SPLIT_LINE_IDX = 5;
  private static final int SUBROUTINE_LEN = 11;
  private static final int FUNCTION_LEN = 8;
  private static String[] TYPES = {"CHARACTER", "LOGICAL", "INTEGER", "DOUBLE PRECISION"};
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
  public static boolean splitLine(String line) {
    if (line.isEmpty())
      return false;
    return !Character.isWhitespace(line.charAt(SPLIT_LINE_IDX));
  }
  public static String removeSplit(String line) {
    if (!splitLine(line))
      return "ERROR: !splitLine(line)";
    String res = line.substring(SPLIT_LINE_IDX + 1);
    return res.trim();
  }
  public static boolean endif(String lineTrim) {
    if (lineTrim.isEmpty())
      return false;
    return (lineTrim.startsWith("ENDIF") || lineTrim.startsWith("END IF"));
  }
  public static boolean end(String lineTrim) {
    if (lineTrim.isEmpty())
      return false;
    return lineTrim.startsWith("END");
  }
  public static boolean isReturn(String lineTrim) {
    if (lineTrim.isEmpty())
      return false;
    return lineTrim.startsWith("RETURN");
  }
  public static boolean subr(String lineTrim) {
    if (lineTrim.isEmpty())
      return false;
    return (lineTrim.startsWith("SUBROUTINE") || lineTrim.startsWith("subroutine"));
  }
  public static boolean func(String lineTrim) {
    if (lineTrim.isEmpty())
      return false;
    if (!type(lineTrim))
      return false;
    String func = getTypeVars(lineTrim);
    return (func.startsWith("FUNCTION") || lineTrim.startsWith("function"));
  }
  public static boolean type(String lineTrim) {
    if (lineTrim.isEmpty())
      return false;
    for (int i = 0; i < TYPES.length; i++) {
      if (lineTrim.startsWith(TYPES[i]))
        return true;
    }
    return false;
  }
  public static boolean ignore(String lineTrim) {
    if (lineTrim.isEmpty())
      return true;
    if (lineTrim.startsWith("IMPLICIT"))
      return true;
    if (lineTrim.startsWith("INTRINSIC"))
      return true;
    return  false;
  }
  public static String getSubrWithParms(String lineTrim) {
    if (lineTrim.isEmpty())
      return "";
    if (!subr(lineTrim))
      return "ERROR: !subr(lineTrim)";
    return lineTrim.substring(SUBROUTINE_LEN);
  }
  public static String getFuncWithParms(String lineTrim) {
    if (lineTrim.isEmpty())
      return "";
    if (!func(lineTrim))
      return "ERROR: !func(lineTrim)";

    String func = getTypeVars(lineTrim);
    return func.substring(FUNCTION_LEN).trim();
  }
  public static String getSubrName(String lineTrim) {
    if (lineTrim.isEmpty())
      return "";
    if (!subr(lineTrim))
      return "ERROR: !subr(lineTrim)";
    String name = lineTrim.substring(SUBROUTINE_LEN).trim();
    int parIdx =  name.indexOf("(");
    if (parIdx == -1)  {
      return name;  // no parameters
    }
    return name.substring(0, parIdx).trim();
  }
  public static String getFuncName(String lineTrim) {
    if (lineTrim.isEmpty())
      return "";
    if (!func(lineTrim))
      return "ERROR: !func(lineTrim)";

    String func = getTypeVars(lineTrim);
    String name = func.substring(FUNCTION_LEN).trim();
    int parIdx =  name.indexOf("(");
    if (parIdx == -1)  {
      return "ERROR: !func(lineTrim)";
    }
    return name.substring(0, parIdx).trim();
  }
  public static String getTypeName(String lineTrim) {
    if (lineTrim.isEmpty())
      return "";
    if (!type(lineTrim))
      return "ERROR: !type(lineTrim)";

    for (int i = 0; i < TYPES.length; i++) {
      if (lineTrim.startsWith(TYPES[i])) {
        return lineTrim.substring(0, TYPES[i].length()).trim();
      }
    }
    return "ERROR: getTypeName(String lineTrim)";
  }
  public static String getTypeVars(String lineTrim) {
    if (lineTrim.isEmpty())
      return "";
    if (!type(lineTrim))
      return "ERROR: !type(lineTrim)";

    for (int i = 0; i < TYPES.length; i++) {
      if (lineTrim.startsWith(TYPES[i])) {
        return lineTrim.substring(TYPES[i].length()).trim();
      }
    }
    return "ERROR: getTypeName(String lineTrim)";
  }

}
