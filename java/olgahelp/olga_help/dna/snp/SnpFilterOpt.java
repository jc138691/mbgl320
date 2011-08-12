package dna.snp;

import project.model.IntOpt;

import javax.swingx.colorx.ColorOpt;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/05/2009, 11:31:44 AM
 */
public class SnpFilterOpt {
  private IntOpt paddingPcnt;
  private IntOpt minReads;
  private IntOpt minMinorReads;
  private IntOpt minQlty;
  private IntOpt trimLen;
  private IntOpt minorFreqPcnt;

  private ColorOpt rejected;
  private ColorOpt rejectedQlty;

  private boolean bestSnpPerContig;

  public SnpFilterOpt() {
    init();
  }
  public String toString() {
    String res = "";
    res = minQlty.printOpt(res, "minQlty");
    res = minReads.printOpt(res, "minReads");
    res = minMinorReads.printOpt(res, "minMinorReads");
    res = minorFreqPcnt.printOpt(res, "minMinorFreqPcnt");
    res = paddingPcnt.printOpt(res, "maxPaddingPcnt");
    res = trimLen.printOpt(res, "trimLen");
    if (bestSnpPerContig)
      res += (", bestSnpPerContig=" + bestSnpPerContig);
    return res;
  }

  private void init() {
    setTrimLen(new IntOpt(true, 10));

    setRejected(new ColorOpt(new Color(255, 200, 0).getRGB()));
    setRejectedQlty(new ColorOpt(new Color(200, 55, 0).getRGB()));

    setPaddingPcnt(new IntOpt(50));
    setMinorFreqPcnt(new IntOpt(10));
    setMinReads(new IntOpt(10));
    setMinMinorReads(new IntOpt(2));
    setMinQlty(new IntOpt(30));
  }


  public SnpFilterColors makeColors() {
    SnpFilterColors res = new SnpFilterColors();
    res.setRejected(new Color(getRejected().getRgb()));
    res.setRejectedQlty(new Color(getRejectedQlty().getRgb()));
    return res;
  }

  public boolean getBestSnpPerContig() {
    return bestSnpPerContig;
  }

  public void setBestSnpPerContig(boolean bestSnpPerContig) {
    this.bestSnpPerContig = bestSnpPerContig;
  }
  public ColorOpt getRejected() {
    return rejected;
  }

  public void setRejected(ColorOpt rejected) {
    this.rejected = rejected;
  }
  public ColorOpt getRejectedQlty() {
    return rejectedQlty;
  }

  public void setRejectedQlty(ColorOpt rejectedQlty) {
    this.rejectedQlty = rejectedQlty;
  }

  public void setTrimLen(IntOpt trimLen) {
    this.trimLen = trimLen;
  }

  public void setPaddingPcnt(IntOpt paddingPcnt) {
    this.paddingPcnt = paddingPcnt;
  }

  public void setMinorFreqPcnt(IntOpt minorFreqPcnt) {
    this.minorFreqPcnt = minorFreqPcnt;
  }

  public void setMinReads(IntOpt minReads) {
    this.minReads = minReads;
  }

  public void setMinQlty(IntOpt minQlty) {
    this.minQlty = minQlty;
  }

  public IntOpt getMinQlty() {
    return minQlty;
  }

  public IntOpt getTrimLen() {
    return trimLen;
  }

  public IntOpt getMinReads() {
    return minReads;
  }

  public IntOpt getMinorFreqPcnt() {
    return minorFreqPcnt;
  }

  public IntOpt getPaddingPcnt() {
    return paddingPcnt;
  }

  public IntOpt getMinMinorReads() {
    return minMinorReads;
  }

  public void setMinMinorReads(IntOpt minMinorReads) {
    this.minMinorReads = minMinorReads;
  }
}
