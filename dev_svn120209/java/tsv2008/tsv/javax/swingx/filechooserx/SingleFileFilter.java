package javax.swingx.filechooserx;
import javax.swing.filechooser.FileFilter;
import javax.utilx.log.Log;
import javax.iox.FileX;
import java.io.File;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 15:51:46
 */
public class SingleFileFilter extends FileFilter {
  public static Log log = Log.getLog(SingleFileFilter.class);
  private String description;
  private String extension;

  public SingleFileFilter(String descr, String ext) {
    setDescription(descr);
    setExtension(ext);
  }

  //Accept all directories and all *.extension files.
  public boolean accept(File f) {        log.dbg("accept(", f);
    if (f.isDirectory()) {               log.dbg("return true #1");
      return true;
    }
    String ext = FileX.getExtension(f);
    if (ext != null) {
      if (ext.equals(getExtension())) {  log.dbg("return true #2");
        return true;
      } else {                           log.dbg("return false #2");
        return false;
      }
    }
    log.dbg("return false #1");
    return false;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getExtension()
  {
    return extension;
  }

  public void setExtension(String extension)
  {
    this.extension = extension;
  }
}

