package io.ace;

import olga.SnpStationProject;

import javax.swingx.filechooserx.SingleFileFilter;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 08/10/2009, 10:26:25 AM
 */
public class AceFileFilter extends SingleFileFilter {
  public static Log log = Log.getLog(AceFileFilter.class);
  public AceFileFilter() {
    super("ACE file extension", "ace");
    String ext = SnpStationProject.getInstance().getAceOpt().getFileExt();
    setExtension(ext);
    setDescription("ACE files (*." + getExtension() + ")");
  }
}