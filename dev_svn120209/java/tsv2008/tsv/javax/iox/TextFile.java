package javax.iox;
import javax.swing.*;
import javax.utilx.log.Log;
import javax.langx.SysProp;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.io.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 17:02:29
 */
public class TextFile {
  public static Log log = Log.getLog(TextFile.class);
  private final ArrayList<String> lines;
  private String fileName;
//  private ProgressMonitorX progress = null;

  public TextFile() {
    lines = new ArrayList<String>();
  }
  public void setFileName(String v) {
    fileName = v;
  }
  public String getFileName() {
    return fileName;
  }
  public void read(File file, JFrame frame) {
    if (file == null) {
      String error = "Unable to read from null file.";
      log.error(error);
      JOptionPane.showMessageDialog(frame, error);
    }
    String path = null;
    try {
      path = file.getCanonicalPath();
      BufferedReader from = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      String s = null;
      while ((s = from.readLine()) != null) lines.add(s);
    }
    catch (IOException e) {
      String error = e.toString()
        + "\nwhile reading from " + file.getName();
      lines.add(error);
      log.error(error);
      JOptionPane.showMessageDialog(frame, error);
    }
  }
  public String toString() {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < size(); i++) {
//      if (i < size() - 1 // show last
//        && i >= ProjectLogger.MAX_N_ROWS_TO_STRING) {
//        if (i == ProjectLogger.MAX_N_ROWS_TO_STRING)
//          buff.append(" ... see ProjectLogger.MAX_N_ROWS_TO_STRING ").append(IOx.eol);
//        continue;
//      }
      String line = getLine(i);
//      if (line.length() < ProjectLogger.MAX_LEN_TO_STRING)
      buff.append(line).append(SysProp.EOL);
//      else {
//        buff.append(line.substring(0, ProjectLogger.MAX_LEN_TO_STRING));
//        buff.append(" ...").append(IOx.eol);
//      }
    }
    return buff.toString();
  }
  // type safe access
  public String getLine(int i) {
    return lines.get(i);
  }
  public void addLine(String s) {
    lines.add(s);
  }
  public Object[] toArray() {
    return lines.toArray();
  }
  public int size() {
    return lines.size();
  }
  public void set(int i, String line) {
    lines.set(i, line);
  }
  public void write(File file, JFrame frame) {
    if (file == null) {
      String error = "Unable to write to null file.";
      log.error(error);
      JOptionPane.showMessageDialog(frame, error);
    }
    try {
      BufferedWriter to = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file)));
      for (int i = 0; i < lines.size(); i++) {
        to.write(getLine(i));
        to.write(SysProp.EOL);
      }
      to.flush();
    }
    catch (IOException e) {
      String error = e.toString()
        + "\nwhile writing to " + file.getName();
      lines.add(error);
      log.error(error);
      JOptionPane.showMessageDialog(frame, error);
    }
  }

  public void addLines(String lines)
  {
    StringTokenizer tokens = new StringTokenizer(lines, "\n", false);
    while (tokens.hasMoreTokens()) {
      addLine(tokens.nextToken().trim());
    }
  }

  public void trim()
  {
    for (Iterator<String> it = lines.iterator(); it.hasNext();) {
      String line = it.next();
      if (line.trim().length() == 0)
        it.remove();
    }
  }

}
