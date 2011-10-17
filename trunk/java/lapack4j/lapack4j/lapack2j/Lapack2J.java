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
  public static String SRC_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\src\\fortran";
  public static String DEST_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\lapack2j\\output";
  public static String JOB_TAG = "_java.f";
  public static void main(String[] args) {
    DEST_DIR = SRC_DIR;
    new Lapack2J().convert(SRC_DIR, "lsame_src.f", DEST_DIR);
    new Lapack2J().convert(SRC_DIR, "dstemr_src.f", DEST_DIR);
    System.exit(0);
  }
  public  void convert(String srcDir, String fileName, String destDir) {
    String srcName = srcDir + File.separatorChar + fileName;
    File srcFile = new File(srcName);
    if (srcFile == null  || !srcFile.canRead()) {
      System.out.println("if (srcFile == null  || !srcFile.canRead()) { srcFile = new File(" + srcName);
    }
    String destName = destDir + File.separatorChar + fileName;
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
      if (subroutine(srcTrim, res))  continue;
      if (func(srcTrim, res))  continue;
      if (type(srcTrim, res))  continue;
      if (endif(srcTrim, res))  continue;
      if (isReturn(srcTrim, res))  continue;
      if (end(srcTrim, res))  continue;

      String line = convertLine(srcLine);
      res.add(line.trim() + "; //" + srcTrim);
//      res.add("ERROR: TODO //" + srcTrim);
    }
    return res;
  }
  public String convertType(String typeName) {
     if (!Fortran77.type(typeName))
       return "ERROR: !type(typeName)";

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
    res = res.replace(".GE.", " >= ");
    res = res.replace(".GT.", " > ");
    res = res.replace(".LE.", " <= ");
    res = res.replace(".LT.", " < ");
    res = res.replace(".AND.", " && ");
    res = res.replace(".OR.", " || ");
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

  public boolean endif(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.endif(lineTrim))
      return false;
    dest.add("} // " + lineTrim);
    return true;
  }
  public boolean end(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.end(lineTrim))
      return false;
    dest.add("} // " + lineTrim);
    return true;
  }
  public boolean isReturn(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.isReturn(lineTrim))
      return false;
    dest.add("} // " + lineTrim);
    return true;
  }
  public boolean type(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.type(lineTrim))
      return false;
    String typeName = Fortran77.getTypeName(lineTrim);
    String name = convertType(typeName);
    String vars = Fortran77.getTypeVars(lineTrim);
    vars = convertLine(vars);
    dest.add(name + " " + vars + ";");
//    dest.add(name + " " + vars + "; //" + lineTrim);
    return true;
  }
  public boolean subroutine(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.subr(lineTrim))
      return false;
    String name = Fortran77.getSubrName(lineTrim);
    String params = Fortran77.getSubrWithParms(lineTrim);
//    dest.add("public class " + name + " { ");
//    dest.add("public static void " + params + " { ");
    dest.add("public class " + name + " { //" + lineTrim);
    dest.add("public static void " + params + " { //" + lineTrim);
    return true;
  }
  public boolean func(String lineTrim, ArrayList<String> dest) {
    if (!Fortran77.func(lineTrim))
      return false;
    String typeName = Fortran77.getTypeName(lineTrim);
    String name = Fortran77.getFuncName(lineTrim);
    String params = Fortran77.getFuncWithParms(lineTrim);
//    dest.add("public class " + name + " { ");
//    dest.add("public static void " + params + " { ");
    dest.add("public class " + name + " { //" + lineTrim);
    String javaType = convertType(typeName);
    dest.add("public static " + javaType + params + " { //" + lineTrim);
    return true;
  }
//  public boolean subr(String lineTrim, int i, ArrayList<String> src, ArrayList<String> dest) {
//    if (!Fortran77.subr(lineTrim))
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
