package dna.snp;

import project.model.IntOpt;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 31/08/2009, 4:36:27 PM
 */
public class SnpNghbrFilterOpt {
  private IntOpt size;
  private IntOpt maxMinorFreqPcnt;
  private IntOpt maxPaddingPcnt;

  public String toString() {
    String res = "";
    res = size.printOpt(res, "size");
    res = maxMinorFreqPcnt.printOpt(res, "MaxMinorFreqPcnt");
    res = maxPaddingPcnt.printOpt(res, "maxPaddingPcnt");
    return res;
  }

  public IntOpt getSize() {
    return size;
  }

  public void setSize(IntOpt size) {
    this.size = size;
  }

  public IntOpt getMaxMinorFreqPcnt() {
    return maxMinorFreqPcnt;
  }

  public void setMaxMinorFreqPcnt(IntOpt maxMinorFreqPcnt) {
    this.maxMinorFreqPcnt = maxMinorFreqPcnt;
  }

  public IntOpt getMaxPaddingPcnt() {
    return maxPaddingPcnt;
  }

  public void setMaxPaddingPcnt(IntOpt maxPaddingPcnt) {
    this.maxPaddingPcnt = maxPaddingPcnt;
  }
}
