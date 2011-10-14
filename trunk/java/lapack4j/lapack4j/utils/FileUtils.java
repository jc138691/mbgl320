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
    try {
      BufferedReader from = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
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
    return res;
  }
  public static void write(ArrayList<String> arr, File outFile) {
  }
}
