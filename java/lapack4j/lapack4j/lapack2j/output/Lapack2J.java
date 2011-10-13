package lapack4j.lapack2j.output;
import lapack4j.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,13/10/11,11:55 AM
 */
public class Lapack2J {
  public static final String SRC_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\fortran\\src";
  public static void main(String[] args) {
    convert(SRC_DIR, "dstemr.f");
  }
  private static void convert(String srcDir, String fileName) {
    String fullName = srcDir + File.separatorChar + fileName;
    File srcFile = new File(fullName);
    if (srcFile == null  || !srcFile.canRead()) {
      System.out.println("if (srcFile == null  || !srcFile.canRead()) { srcFile = new File(" + fullName);
    }
    String outName = FileUtils.replaceDotExtension(fullName, "_.java");
    File outFile = new File(outName);
    if (outFile == null  || !outFile.canWrite()) {
      System.out.println("if (outFile == null  || !outFile.canWrite()) { outFile = new File(" + outName);
    }
    ArrayList<String> src = FileUtils.readAll(srcFile);
  }
}
