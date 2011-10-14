package lapack4j.utils;
import lapack4j.utils.tests.TestUtils;

import java.io.*;
import java.util.ArrayList;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,13/10/11,4:17 PM
 */
public class FileUtils extends TestUtils {
  public static String replaceDotExtension(String fileName, String newExt) {
    String res = fileName.substring(0, fileName.lastIndexOf('.'));
    return res + newExt;
  }
  public static ArrayList<String> read(File srcFile) {
    ArrayList<String> res = new ArrayList<String>();
    BufferedReader from = null;
    try {
      from = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
      for ( ; ; ) {
        String line = from.readLine();
        if (line == null)
          break;
        debug(line);
        res.add(line);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    from = null; System.gc();
    return res;
  }
  public static void write(ArrayList<String> src, File outFile) {
    BufferedWriter dest = null;
    try {
      dest = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
      for (int i = 0; i < src.size(); i++) {
        String line = src.get(i);
        if (line == null)
          break;
        debug(line);
        dest.write(line);
        if (i != (src.size() - 1))
          dest.newLine();
      }
      dest.flush();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    dest = null; System.gc();
  }
}
