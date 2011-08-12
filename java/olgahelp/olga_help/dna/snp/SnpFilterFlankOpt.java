package dna.snp;

import project.model.IntOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/05/2009, 12:00:12 PM
 */
public class SnpFilterFlankOpt {
  private IntOpt minReads;
//  private IntOpt minQlty;
  private IntOpt iupacMinMinorFreqPcnt;
  private IntOpt minLen;
  private IntOpt minBestLen;
  private IntOpt maxInsertsPcnt;
  private IntOpt maxDeletesPcnt;

  public SnpFilterFlankOpt() {
    init();
  }

  public String toString() {
    String res = "";
//    res = minQlty.printOpt(res, "minQlty");
    res = minReads.printOpt(res, "minReads");
    res = iupacMinMinorFreqPcnt.printOpt(res, "IUPAC_MinMinorFreqPcnt");
    res = minLen.printOpt(res, "minLen");
    res = minBestLen.printOpt(res, "minBestLen");
    res = maxInsertsPcnt.printOpt(res, "maxInsertsPcnt");
    res = maxDeletesPcnt.printOpt(res, "maxDeletesPcnt");
    return res;
  }

  private void init() {
    setIupacMinMinorFreqPcnt(new IntOpt(5));
    setMinReads(new IntOpt(1));
//    setMinQlty(new IntOpt(20));
    setMinLen(new IntOpt(10));
    setMinBestLen(new IntOpt(false, 10));
    setMaxInsertsPcnt(new IntOpt(10));
    setMaxDeletesPcnt(new IntOpt(20));
  }

  public IntOpt getMinLen() {
    return minLen;
  }

  public void setMinLen(IntOpt minLen) {
    this.minLen = minLen;
  }
  public IntOpt getMaxInsertsPcnt() {
    return maxInsertsPcnt;
  }
  public void setMaxInsertsPcnt(IntOpt maxInsertsPcnt) {
    this.maxInsertsPcnt = maxInsertsPcnt;
  }
  public IntOpt getMaxDeletesPcnt() {
    return maxDeletesPcnt;
  }
  public void setMaxDeletesPcnt(IntOpt maxDeletesPcnt) {
    this.maxDeletesPcnt = maxDeletesPcnt;
  }
  public void setMinReads(IntOpt minReads) {
    this.minReads = minReads;
  }
//  public void setMinQlty(IntOpt minQlty) {
//    this.minQlty = minQlty;
//  }
  public void setIupacMinMinorFreqPcnt(IntOpt iupacMinMinorFreqPcnt) {
    this.iupacMinMinorFreqPcnt = iupacMinMinorFreqPcnt;
  }
  public IntOpt getIupacMinMinorFreqPcnt() {
    return iupacMinMinorFreqPcnt;
  }
  public IntOpt getMinReads() {
    return minReads;
  }
//  public IntOpt getMinQlty() {
//    return minQlty;
//  }

  public IntOpt getMinBestLen() {
    return minBestLen;
  }

  public void setMinBestLen(IntOpt minBestLen) {
    this.minBestLen = minBestLen;
  }
}
