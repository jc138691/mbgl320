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
  public static final String SRC_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\fortran\\src";
  public static final String DEST_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\lapack2j\\output";
  public static void main(String[] args) {
    convert(SRC_DIR, "dstemr.f", DEST_DIR);
    System.exit(0);
  }
  private static void convert(String srcDir, String fileName, String destDir) {
    String srcName = srcDir + File.separatorChar + fileName;
    File srcFile = new File(srcName);
    if (srcFile == null  || !srcFile.canRead()) {
      System.out.println("if (srcFile == null  || !srcFile.canRead()) { srcFile = new File(" + srcName);
    }
    String destName = destDir + File.separatorChar + fileName;
    destName = FileUtils.replaceDotExtension(destName, "_java.f");
    File destFile = new File(destName);
    if (destFile == null  || !destFile.canWrite()) {
      System.out.println("if (destFile == null  || !destFile.canWrite()) { destFile = new File(" + destName);
    }
    ArrayList<String> src = FileUtils.read(srcFile);
//    FileUtils.write(src, destFile);
    ArrayList<String> res = convert2J(src);
    FileUtils.write(res, destFile);
  }
  private static ArrayList<String> convert2J(ArrayList<String> src) {
    ArrayList<String> res = new ArrayList<String>();
    for (int i = 0; i < src.size(); i++) {
      String line = src.get(i);
      if (line == null)
        break;
      debug(line);
      String lineTrim = line.trim();
      if (line.isEmpty()) {
        res.add(line);
        continue;
      }
      if (Fortran77.comment(line)) {
        res.add("//" + line);
        continue;
      }
      if (Fortran77.subroutine(lineTrim)) {
        String name = Fortran77.getSubroutine(lineTrim);
        res.add("public class " + name);
        continue;
      }
      if (Fortran77.endif(lineTrim)) {
        res.add("} // " + lineTrim);
        continue;
      }
    }
    return res;
  }
}
