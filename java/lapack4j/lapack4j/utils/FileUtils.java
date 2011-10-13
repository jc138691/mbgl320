package lapack4j.utils;
import java.io.File;
import java.util.ArrayList;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,13/10/11,4:17 PM
 */
public class FileUtils {
  public static String replaceDotExtension(String fileName, String newExt) {
    String res = fileName.substring(0, fileName.lastIndexOf('.'));
    return res + newExt;
  }
  public static ArrayList<String> readAll(File srcFile) {

    return null;
  }
}
