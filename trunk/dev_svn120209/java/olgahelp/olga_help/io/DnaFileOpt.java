package io;

import dna.table.PageOpt;
import dna.table.DnaTableInfo;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 03/04/2009, 2:03:10 PM
 */
public class DnaFileOpt extends PageOpt {
  private DnaTableInfo dnaTableInfo;
  private String fileExt;
  private String fileName;

  public DnaFileOpt() {
  }
  public String getFileExt() {
    return fileExt;
  }

  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public DnaTableInfo getDnaTableInfo() {
    if (dnaTableInfo == null)
      dnaTableInfo = new DnaTableInfo();
    return dnaTableInfo;
  }

  public void setDnaTableInfo(DnaTableInfo dnaTableInfo) {
    this.dnaTableInfo = dnaTableInfo;
  }
}
