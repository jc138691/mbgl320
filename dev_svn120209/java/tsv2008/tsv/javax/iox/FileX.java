package javax.iox;
import javax.utilx.log.Log;
import javax.swing.*;
import java.io.*;
import java.awt.*;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 15:59:36
 */
public class FileX {
  public static Log log = Log.getLog(FileX.class);

  public static void writeToFile(String text, String dirName, String fileName) {
    File file = openWriteFile(dirName + File.separator + fileName, null);
    writeToFile(text, file);
  }
  public static void writeToFile(String text, String dirName, String dir2, String fileName) {
    File file = openWriteFile(dirName + File.separator + dir2 + File.separator + fileName, null);
    writeToFile(text, file);
  }
  public static void writeToFile(String text, File file) {
    PrintWriter out = null;
    try {
//      out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
      out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
      out.print(text);
    }
    catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, log.error(e.toString()));
    } finally {
      if (out != null)
        out.close();
    }
  }

  public static File openReadFile(String fileName, Component view) {
    if (fileName == null) {
      String error = "Unable to read from file: file name==null pointer.";
      log.error(error); JOptionPane.showMessageDialog(view, error);
      return null;
    }
    if (fileName.length() == 0) {
      String error = "Unable to read from file: file name=''.";
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    File file = new File(fileName);
    if (file == null) {
      String error = "Unable to read from file: file name=["+ fileName +"]";
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    if (!file.exists()) {
      String error = "Unable to read from file: file does not exist with file name=["+ fileName +"]";
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    if (!file.canRead()) {
      String error = "Unable to read from file name=["+ fileName +"]";
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    return file;
  }

  public static File openWriteFile(String fileName, Component view) {
    if (fileName == null) {
      String error = "Unable to write to file: file name==null pointer.";
      log.error(error); JOptionPane.showMessageDialog(view, error);
      return null;
    }
    if (fileName.length() == 0) {
      String error = "Unable to write to file: file name=''.";
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    File file = new File(fileName);
    if (file == null) {
      String error = "Unable to write to file: file name=["+ fileName +"]";
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    if (file.exists() && !file.canWrite()) {
      String error = "Unable to write to existing file name=["+ fileName +"]";
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    return file;
  }

  static public String getShortFileName(String from, int levels) {
    String error = "None";
    if (levels <= 0 || from == null || from.length() == 0)
      return error;
    StringBuffer buff = new StringBuffer();
    File f = new File(from);
    buff.append(f.getName());
    levels--;
    f = f.getParentFile();
    while (f != null && levels > 0) {
      buff.insert(0, File.separatorChar);
      buff.insert(0, f.getName());
      levels--;
      f = f.getParentFile();
    }
    return buff.toString();
  }
  static public String getFileName(File file) {
    String name = "";
    try {
      name = file.getCanonicalPath();
    }
    catch (FileNotFoundException e) {
      log.error("FileNotFoundException in file=" + file + ", " + e);
    }
    catch (IOException e) {
      log.error("IOException in file=" + file + ", " + e);
    }
    return name;
  }

  /**
   * from Utils.java
   * http://java.sun.com/docs/books/tutorial/uiswing/components/examples/index.html#FileChooserDemo2
   * @param f
   * @return file extension
   */
  public static String getExtension(File f) { log.dbg("getExtension(", f);
    return getExtension(f.getName());
  }
  public static String getExtension(String s) {
    String ext = null;
    int i = s.lastIndexOf('.');
    if (i > 0 &&  i < s.length() - 1) {
      ext = s.substring(i+1).toLowerCase();
    }
    log.dbg("extension=", ext);
    return ext;
  }

  public static String replaceExtension(String fileName, String newExt) {
    String res = fileName.substring(0, fileName.lastIndexOf('.'));
    res += '.';
    return res + newExt;
  }

}
