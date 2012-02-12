package io.fasta;

import olga.SnpStationProject;

import javax.swingx.filechooserx.SingleFileFilter;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 16/03/2009, 2:46:54 PM
 */
public class FastaFileFilter extends SingleFileFilter {
  public static Log log = Log.getLog(FastaFileFilter.class);
  public FastaFileFilter() {
    super("FASTA file extension", "dna");
    String ext = SnpStationProject.getInstance().getFastaOpt().getFileExt();
    setExtension(ext);
    setDescription("FASTA files (*." + getExtension() + ")");
  }
}
