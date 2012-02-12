package lapack4j.utils;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,7/10/11,12:16 PM
 */
public class Fortran77 {
private static final int SPLIT_LINE_IDX = 5;
private static final int SUBROUTINE_LEN = 11;
private static final int FUNCTION_LEN = 8;
private static final int PARAMETER_LEN = 9;
private static final int IF_LEN = 2;
private static final int ELSE_IF_LEN = 7;
private static final int DO_LEN = 2;
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
public static boolean isEndif(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return (lineTrim.equals("ENDIF") || lineTrim.equals("END IF"));
}
public static boolean isEnd(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return lineTrim.equals("END");
}
public static boolean isEndDo(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return lineTrim.equals("END DO");
}
public static boolean isElse(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return lineTrim.equals("ELSE");
}
public static boolean isReturn(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return lineTrim.equals("RETURN");
}
public static boolean startsSubr(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return (lineTrim.startsWith("SUBROUTINE ") || lineTrim.startsWith("subroutine "));
}
public static boolean startsParam(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return (lineTrim.startsWith("PARAMETER ") || lineTrim.startsWith("parameter "));
}
public static boolean startsIf(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return (lineTrim.startsWith("IF(") || lineTrim.startsWith("IF ("));
}
public static boolean startsElseIf(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return (
    lineTrim.startsWith("ELSEIF(")
      || lineTrim.startsWith("ELSEIF (")
      || lineTrim.startsWith("ELSE IF(")
      || lineTrim.startsWith("ELSE IF (")
  );
}
public static boolean startsDo(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return (lineTrim.startsWith("DO ") || lineTrim.startsWith("do "));
}
public static boolean endsThen(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return (lineTrim.endsWith(" THEN") || lineTrim.endsWith(" then"));
}
public static boolean endsReturn(String lineTrim) {
  if (lineTrim.isEmpty())
    return false;
  return (lineTrim.endsWith(" RETURN") || lineTrim.endsWith(" return"));
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
  if (!startsSubr(lineTrim))
    return "ERROR: !startsSubr(lineTrim)";
  return lineTrim.substring(SUBROUTINE_LEN);
}
public static String getIfCheck(String lineTrim) {
  if (lineTrim.isEmpty())
    return "";
  if (!startsIf(lineTrim))
    return "ERROR: getIfCheck: !startsIf(lineTrim)";
  int endIdx = lineTrim.lastIndexOf(")");
  if (endIdx == -1)
    return "ERROR: getIfCheck: endIdx == -1";
  return lineTrim.substring(IF_LEN, endIdx + 1);
}
public static String getIfBody(String lineTrim) {
  if (lineTrim.isEmpty())
    return "";
  if (!startsIf(lineTrim))
    return "ERROR: getIfBody: !startsIf(lineTrim)";
  return lineTrim.substring(IF_LEN);
}
public static String getElseIfBody(String lineTrim) {
  if (lineTrim.isEmpty())
    return "";
  if (!startsElseIf(lineTrim))
    return "ERROR: !getElseIfBody(lineTrim)";
  int endIdx = lineTrim.lastIndexOf(")");
  if (endIdx == -1)
    return "ERROR: getIfCheck: endIdx == -1";
  return lineTrim.substring(ELSE_IF_LEN, endIdx + 1);
}
public static String getParamBody(String lineTrim) {
  if (lineTrim.isEmpty())
    return "";
  if (!startsParam(lineTrim))
    return "ERROR: !startsParam(lineTrim)";
  String body = lineTrim.substring(PARAMETER_LEN);
  int startIdx = body.indexOf("(");
  if (startIdx == -1)
    return "ERROR: getParamBody: startIdx == -1";
  int endIdx = body.lastIndexOf(")");
  if (endIdx == -1)
    return "ERROR: getParamBody: endIdx == -1";
  return body.substring(startIdx + 1, endIdx);
}
public static String getDoBody(String lineTrim) {
  if (lineTrim.isEmpty())
    return "";
  if (!startsDo(lineTrim))
    return "ERROR: !startsDo(lineTrim)";
  return lineTrim.substring(DO_LEN);
}
public static String getFuncWithParms(String lineTrim) {
  if (lineTrim.isEmpty())
    return "";
  if (!func(lineTrim))
    return "ERROR: !startsFunc(lineTrim)";

  String func = getTypeVars(lineTrim);
  return func.substring(FUNCTION_LEN).trim();
}
public static String getSubrName(String lineTrim) {
  if (lineTrim.isEmpty())
    return "";
  if (!startsSubr(lineTrim))
    return "ERROR: !startsSubr(lineTrim)";
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
    return "ERROR: !startsFunc(lineTrim)";

  String func = getTypeVars(lineTrim);
  String name = func.substring(FUNCTION_LEN).trim();
  int parIdx =  name.indexOf("(");
  if (parIdx == -1)  {
    return "ERROR: !startsFunc(lineTrim)";
  }
  return name.substring(0, parIdx).trim();
}
public static String getTypeName(String lineTrim) {
  if (lineTrim.isEmpty())
    return "";
  if (!type(lineTrim))
    return "ERROR: !startsType(lineTrim)";

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
    return "ERROR: !startsType(lineTrim)";

  for (int i = 0; i < TYPES.length; i++) {
    if (lineTrim.startsWith(TYPES[i])) {
      return lineTrim.substring(TYPES[i].length()).trim();
    }
  }
  return "ERROR: getTypeName(String lineTrim)";
}

}
