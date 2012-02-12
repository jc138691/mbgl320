package io.caf;

import olga.SnpStationProject;

import javax.swingx.filechooserx.SingleFileFilter;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/03/2009, 3:04:02 PM
 */
public class CafFileFilter extends SingleFileFilter {
  public static Log log = Log.getLog(CafFileFilter.class);
  public CafFileFilter() {
    super("CAF file extension", "caf");
    String ext = SnpStationProject.getInstance().getCafOpt().getFileExt();
    setExtension(ext);
    setDescription("CAF files (*." + getExtension() + ")");
  }
}