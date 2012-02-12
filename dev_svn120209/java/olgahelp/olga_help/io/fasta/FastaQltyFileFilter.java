package io.fasta;

import olga.SnpStationProject;

import javax.swingx.filechooserx.SingleFileFilter;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 16/03/2009, 2:52:05 PM
 */
public class FastaQltyFileFilter extends SingleFileFilter {
  public static Log log = Log.getLog(FastaQltyFileFilter.class);
  public FastaQltyFileFilter() {
    super("FASTA Quality file extension", "qul");
    String ext = SnpStationProject.getInstance().getFastaOpt().getQltyFileExt();
    setExtension(ext);
    setDescription("FASTA Quality files (*." + getExtension() + ")");
  }
}
