package lapack4j.lapack2j;
import lapack4j.utils.FileUtils;
import lapack4j.utils.Fortran77;
import lapack4j.utils.tests.TestUtils;

import java.io.File;
import java.util.ArrayList;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,13/10/11,11:55 AM
 */
public class Lapack2J extends TestUtils {
  public  String SRC_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\src\\lapack";
  public  String DEST_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\lapack2j\\output";
  public  String JOB_TAG = "_java.f";
  public static final String[] FILE_NAMES = {
    "dcopy.f"
    , "dlae2.f", "dlaev2.f"
    , "dlamch.f"
    , "dlanst.f"
    , "dlarra.f", "dlarrb.f", "dlarrc.f", "dlarrd.f", "dlarre.f", "dlarrj.f", "dlarrr.f", "dlarrv.f"
    , "dlasq2.f", "dlasq3.f", "dlasq4.f", "dlasq5.f", "dlasq6.f"
    , "dscal.f"
    , "dswap.f", "lsame.f", "xerbla.f"
    , "dlassq.f"
    , "dlasrt.f"
    , "dstemr.f"
  };
  public static void main(String[] args) {
    Lapack2J runMe = new Lapack2J();
    runMe.JOB_TAG = "_java.f";
    runMe.SRC_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\src\\fortran_src";
    runMe.DEST_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\src\\java";
    for (int i = 0; i < FILE_NAMES.length; i++) {
      String fileName = FILE_NAMES[i];
      runMe.convert(fileName);
    }
    System.exit(0);
  }
  public  void convert(String fileName) {
    String srcName = SRC_DIR + File.separatorChar + fileName;
    File srcFile = new File(srcName);
    if (srcFile == null  || !srcFile.canRead()) {
      System.out.println("if (srcFile == null  || !srcFile.canRead()) { srcFile = new File(" + srcName);
    }
    String destName = DEST_DIR + File.separatorChar + fileName;
    destName = FileUtils.replaceDotExtension(destName, JOB_TAG);
    File destFile = new File(destName);
    if (destFile == null  || !destFile.canWrite()) {
      System.out.println("if (destFile == null  || !destFile.canWrite()) { destFile = new File(" + destName);
    }
    ArrayList<String> src = FileUtils.read(srcFile);
    //    FileUtils.write(src, destFile);
    ArrayList<String> res = convert2J(src);
    FileUtils.write(res, destFile);
  }
  public ArrayList<String> convert2J(ArrayList<String> src) {
    ArrayList<String> res = new ArrayList<String>();
    for (int i = 0; i < src.size(); i++) {
      String srcLine = src.get(i);
      if (srcLine == null)
        break;
      debug(srcLine);
      if (isEmpty(srcLine, res))  continue;
      if (comment(srcLine, res))  continue;

      String srcTrim = srcLine.trim();
      if (ignore(srcTrim, res))  continue;
      //      if (splitLine(line, res))  continue;
      if (startsSubr(srcTrim, res))  continue;
      if (startsParam(srcTrim, res))  continue;
      if (startsFunc(srcTrim, res))  continue;
      if (startsType(srcTrim, res))  continue;
      if (startsIf(srcTrim, res))  continue;
      if (startsElseIf(srcTrim, res))  continue;
      if (isElse(srcTrim, res))  continue;
      if (isEndIf(srcTrim, res))  continue;
      if (startsDo(srcTrim, res))  continue;
      if (isEndDo(srcTrim, res))  continue;
      if (isReturn(srcTrim, res))  continue;
      if (isEnd(srcTrim, res))  continue;

      String line = convertLine(srcLine);
      res.add(line.trim() + "; //" + srcTrim);
      //      res.add("ERROR: TODO //" + srcTrim);
    }
    return res;
  }
  public String convertType(String typeName) {
    if (!Fortran77.type(typeName))
      return "ERROR: !startsType(typeName)";

    if (typeName.equals("CHARACTER"))
      return "char";
    if (typeName.equals("LOGICAL"))
      return "boolean";
    if (typeName.equals("INTEGER"))
      return "int";
    if (typeName.equals("DOUBLE PRECISION"))
      return "double";
    return "ERROR: convertType(String typeName)";
  }

  public String convertLine(String src) {
    String res = src.replace("(*)", "[]");
    res = res.replace("( * )", "[]");
    res = res.replace("( *)", "[]");
    res = res.replace("(* )", "[]");
    res = res.replace(".EQ.", " == ");
    res = res.replace(".NE.", " != ");
    res = res.replace(".GE.", " >= ");
    res = res.replace(".GT.", " > ");
    res = res.replace(".LE.", " <= ");
    res = res.replace(".LT.", " < ");
    res = res.replace(".AND.", " && ");
    res = res.replace(".OR.", " || ");
    //    res = res.replace(" THEN ", " { ");
    return res;
  }
  public String convertDoBoby(String src) {
    String res = src.replace(",", ";");
    return res;
  }

  public boolean isEmpty(String srcLine, ArrayList<String> dest) {
    if (srcLine.isEmpty()) {
      dest.add(srcLine);
      return true;
    }
    return false;
  }
  public boolean comment(String srcLine, ArrayList<String> dest) {
    if (Fortran77.comment(srcLine)) {
      dest.add("//" + srcLine);
      return true;
    }
    return false;
  }
  public boolean ignore(String lineTrim, ArrayList<String> dest) {
    if (Fortran77.ignore(lineTrim)) {
      dest.add("//" + lineTrim);
      return true;
    }
    return false;
  }
  public boolean splitLine(String srcLine, ArrayList<String> dest) {
    if (Fortran77.splitLine(srcLine)) {
      String str = Fortran77.removeSplit(srcLine);
      dest.add(str + " //" + srcLine.trim());
      return true;
    }
    return false;
  }

  public boolean isEndIf(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.isEndif(lineTrim))
      return false;
    dest.add("} // " + lineTrim);
    return true;
  }
  public boolean isEnd(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.isEnd(lineTrim))
      return false;
    dest.add("} // " + lineTrim);
    return true;
  }
  public boolean isEndDo(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.isEndDo(lineTrim))
      return false;
    dest.add("} // " + lineTrim);
    return true;
  }
  public boolean isElse(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.isElse(lineTrim))
      return false;
    dest.add("} else { // " + lineTrim);
    return true;
  }
  public boolean isReturn(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.isReturn(lineTrim))
      return false;
    dest.add("} // " + lineTrim);
    return true;
  }
  public boolean startsType(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.type(lineTrim))
      return false;
    String typeName = Fortran77.getTypeName(lineTrim);
    String name = convertType(typeName);
    String vars = Fortran77.getTypeVars(lineTrim);
    vars = convertLine(vars);
    //    dest.add(name + " " + vars + ";");
    dest.add(name + " " + vars + "; //" + lineTrim);
    return true;
  }
  public boolean startsSubr(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.startsSubr(lineTrim))
      return false;
    String name = Fortran77.getSubrName(lineTrim);
    String params = Fortran77.getSubrWithParms(lineTrim);
    //    dest.add("public class " + name + " { ");
    //    dest.add("public static void " + params + " { ");
    dest.add("public class " + name + " { //" + lineTrim);
    dest.add("public static void " + params + " { //" + lineTrim);
    return true;
  }
  public boolean startsParam(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.startsParam(lineTrim))
      return false;
    String body = Fortran77.getParamBody(lineTrim);
    dest.add("{ " + body + "; } //" + lineTrim);
    return true;
  }
  public boolean startsIf(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.startsIf(lineTrim))
      return false;
    String check = Fortran77.getIfCheck(lineTrim);
    check = convertLine(check);
    if (Fortran77.endsThen(lineTrim)) {
      dest.add("if " + check + " { //" + lineTrim);
    }
    else if (Fortran77.endsReturn(lineTrim)) {
      dest.add("if " + check + " return; //" + lineTrim);
    }
    else {
      String body = Fortran77.getIfBody(lineTrim);
      body = convertLine(body);
      dest.add("if " + body + "; //" + lineTrim);
    }
    return true;
  }
  public boolean startsElseIf(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.startsElseIf(lineTrim))
      return false;
    String body = Fortran77.getElseIfBody(lineTrim);
    body = convertLine(body);
    if (Fortran77.endsThen(lineTrim)) {
      dest.add("} else if " + body + " { //" + lineTrim);
    }
    if (Fortran77.endsReturn(lineTrim)) {
      dest.add("} else if " + body + " return; //" + lineTrim);
    }
    return true;
  }
  public boolean startsDo(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.startsDo(lineTrim))
      return false;
    String body = Fortran77.getDoBody(lineTrim);
    body = convertLine(body);
    body = convertDoBoby(body);
    dest.add("for (" + body + ") { //" + lineTrim);
    return true;
  }
  public boolean startsFunc(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.func(lineTrim))
      return false;
    String typeName = Fortran77.getTypeName(lineTrim);
    String name = Fortran77.getFuncName(lineTrim);
    String params = Fortran77.getFuncWithParms(lineTrim);
    //    dest.add("public class " + name + " { ");
    //    dest.add("public static void " + params + " { ");
    dest.add("public class " + name + " { //" + lineTrim);
    String javaType = convertType(typeName);
    dest.add("public static " + javaType + " " + params + " { //" + lineTrim);
    return true;
  }
  //  public boolean startsSubr(String lineTrim, int i, ArrayList<String> src, ArrayList<String> dest) {
  //    if (!Fortran77.startsSubr(lineTrim))
  //      return false;
  //    String name = Fortran77.getSubrName(lineTrim);
  //    String params = Fortran77.getSubrWithParms(lineTrim);
  //    dest.add("public class " + name + " { //" + lineTrim);
  //    dest.add("public static void " + params + "//" + lineTrim);
  //    for (int nextIdx = i+1; nextIdx < src.size(); nextIdx++) {
  //      String nextLine = src.get(nextIdx);
  //      if (nextLine == null)
  //        break;
  //      debug(nextLine);
  //      if (!splitLine(nextLine, dest))
  //        break;
  //    }
  //    dest.add("{");
  //    return true;
  //  }

}
