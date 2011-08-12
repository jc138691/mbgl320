package io.fasta;

import io.DnaFileOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 03/04/2009, 2:02:59 PM
 */
public class FastaOpt extends DnaFileOpt {
  private String qltyFileName;
  private String qltyFileExt;
  private boolean hasQltyFile;

  public FastaOpt() {
    setFileExt("seq");
    setQltyFileExt("qul");
    setPageSize(50);
  }

  public String getQltyFileName() {
    return qltyFileName;
  }

  public void setQltyFileName(String qltyFileName) {
    this.qltyFileName = qltyFileName;
  }

  public String getQltyFileExt() {
    return qltyFileExt;
  }

  public void setQltyFileExt(String qltyFileExt) {
    this.qltyFileExt = qltyFileExt;
  }

  public boolean getHasQltyFile() {
    return hasQltyFile;
  }

  public void setHasQltyFile(boolean hasQltyFile) {
    this.hasQltyFile = hasQltyFile;
  }
}
