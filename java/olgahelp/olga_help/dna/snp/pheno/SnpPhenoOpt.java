package dna.snp.pheno;

import project.model.IntOpt;
import project.model.FloatOpt;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 18/08/2009, 9:52:35 AM
 */
public class SnpPhenoOpt {
  private boolean hasPhenoFile;
  private String fileExt;
  private String fileName;

  private String[] phenoList;
  private int phenoIdxA;
  private int phenoIdxB;

  private FloatOpt maxPValue;
  private IntOpt minReads;
  private IntOpt minAssoc;

  public SnpPhenoOpt() {
    init();
  }
  public String toString() {
    String res = "";
    if (!hasPhenoFile)
      return res;
    res = minReads.printOpt(res, "minReads");
    res = maxPValue.printOpt(res, "maxPValue");
    res = minAssoc.printOpt(res, "minAssociationPcnt");
    res = Log.printOpt(res, "fileName", hasPhenoFile, fileName);
    return res;
  }
  private void init() {
    phenoList = new String[0];
    setFileExt("txt");
    setMaxPValue(new FloatOpt(0.01f));
    setMinReads(new IntOpt(4));
    setMinAssoc(new IntOpt(80));
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
  public String[] getPhenoList() {
    return phenoList;
  }

  public void setPhenoList(String[] phenoList) {
    this.phenoList = phenoList;
  }

  public int getPhenoIdxA() {
    return phenoIdxA;
  }
  public void setPhenoIdxA(int phenoIdxA) {
    this.phenoIdxA = phenoIdxA;
  }
  public int getPhenoIdxB() {
    return phenoIdxB;
  }
  public void setPhenoIdxB(int phenoIdxB) {
    this.phenoIdxB = phenoIdxB;
  }
  public void setHasPhenoFile(boolean hasPhenoFile) {
    this.hasPhenoFile = hasPhenoFile;
  }

  public boolean getHasPhenoFile() {
    return hasPhenoFile;
  }

  public FloatOpt getMaxPValue() {
    return maxPValue;
  }

  public void setMaxPValue(FloatOpt maxPValue) {
    this.maxPValue = maxPValue;
  }

  public void setMinReads(IntOpt minReads) {
    this.minReads = minReads;
  }

  public IntOpt getMinReads() {
    return minReads;
  }

  public IntOpt getMinAssoc() {
    return minAssoc;
  }

  public void setMinAssoc(IntOpt minAssoc) {
    this.minAssoc = minAssoc;
  }
}
