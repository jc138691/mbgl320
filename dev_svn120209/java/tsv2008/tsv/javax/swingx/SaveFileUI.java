package javax.swingx;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 15:42:38
 */
public class SaveFileUI {
  private JFileChooser chooser = null;
  public File selectFile(Component parent, File file) {
    if (chooser == null) {
      chooser = new JFileChooser(file);
      if (chooser == null)
        return null;
    }
    if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION)
      return null;
    return chooser.getSelectedFile();
  }
  public File selectFile(Component parent, File file, FileFilter filter) {
    if (chooser == null) {
      chooser = new JFileChooser(file);
      if (chooser == null)
        return null;
      chooser.setFileFilter(filter);
    }
    if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION)
      return null;
    return chooser.getSelectedFile();
  }

  public void setFileFilter(FileFilter filter) {
    chooser.setFileFilter(filter);
  }
}
