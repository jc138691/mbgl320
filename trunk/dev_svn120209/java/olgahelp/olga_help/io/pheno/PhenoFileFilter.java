package io.pheno;

import olga.SnpStationProject;

import javax.swingx.filechooserx.SingleFileFilter;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 04/08/2009, 1:42:14 PM
 */
public class PhenoFileFilter extends SingleFileFilter {
  public static Log log = Log.getLog(PhenoFileFilter.class);
  public PhenoFileFilter() {
    super("Phenotype file extension", "txt");
    String ext = SnpStationProject.getInstance().getSnpOpt().getPhenoOpt().getFileExt();
    setExtension(ext);
    setDescription("Phenotype files (*." + getExtension() + ")");
  }
}
