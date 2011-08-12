package javax.swingx;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 15:48:29
 */
public class OpenFileUI {
  //  private JFileChooser chooser = null; // why did I do that originally?
  public File selectFile(Component parent, File file) {
    JFileChooser chooser = null;
    if (chooser == null) {
      chooser = new JFileChooser(file);
      if (chooser == null)
        return null;
    }
    if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION)
      return null;
    return chooser.getSelectedFile();
  }
  public File selectFile(Component parent, File file, FileFilter filter) {
    JFileChooser chooser = null;
    if (chooser == null) {
      chooser = new JFileChooser(file);
      if (chooser == null)
        return null;
      chooser.setFileFilter(filter);
    }
    if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION)
      return null;
    return chooser.getSelectedFile();
  }
  public File[] selectFiles(Component parent, File file) {
    JFileChooser chooser = null;
    if (chooser == null) {
      chooser = new JFileChooser(file);
      if (chooser == null)
        return null;
      chooser.setMultiSelectionEnabled(true);
    }
    if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION)
      return null;
    return chooser.getSelectedFiles();
  }
//  public void setFileFilter(FileFilter filter) {
//    chooser.setFileFilter(filter);
//  }

}
