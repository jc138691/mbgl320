package io.caf;

import dna.*;
import dna.seq.SeqBytes;

import javax.utilx.FastId;
import java.util.ArrayList;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 31/03/2009, 1:59:32 PM
 */
public class CafDna extends SeqBytes {
  private CafSeq cafSeq;
  private Caf.TYPE type;
  private boolean padded;
  private boolean forward;
  private FastId ScfFile;
  protected ArrayList<DnaInterv> seqVecArr;
  protected ArrayList<DnaInterv> clippArr;
  protected ArrayList<DnaInterv> tagArr;
  protected DnaAlignArr alignArr;
  protected DnaAssemArr assemArr;

  public int calcDnaSize() {
    int size = 0;
    if (alignArr == null)
      return size;
    for (DnaAlign align : alignArr) {
      if (size < align.getS2())
        size = align.getS2();
      if (size < align.getS())
        size = align.getS();
    }
    return size;
  }

  public CafSeq getCafSeq() {
    return cafSeq;
  }

  public DnaAlignArr getAlignArr() {
    return alignArr;
  }
  public void setCafSeq(CafSeq cafSeq) {
    this.cafSeq = cafSeq;
  }
  public boolean isReady() {
    return (cafSeq != null)  &&  (size() > 0)  &&  (getQltyArr() != null);
  }

  public Caf.TYPE getType() {
    return type;
  }

  public void setType(Caf.TYPE type) {
    this.type = type;
  }

  public boolean isPadded() {
    return padded;
  }

  public void setPadded(boolean padded) {
    this.padded = padded;
  }

  public boolean isForward() {
    return forward;
  }

  public void setForward(boolean forward) {
    this.forward = forward;
  }

  public String getScfFile() {
    return ScfFile.getId();
  }

  public void setScfFile(String scfFile) {
    this.ScfFile = new FastId(scfFile);
  }

  public void addVec(int x, int x2, String text) {
    if (seqVecArr == null)
      seqVecArr = new ArrayList<DnaInterv>();
    seqVecArr.add(new DnaInterv(x, x2, text));
  }
  public void addClipp(String type, int x, int x2, String text) {
    if (clippArr == null)
      clippArr = new ArrayList<DnaInterv>();
    clippArr.add(new DnaInterv(type, x, x2, text));
  }
  public void addTag(String type, int x, int x2, String text) {
    if (tagArr == null)
      tagArr = new ArrayList<DnaInterv>();
    tagArr.add(new DnaInterv(type, x, x2, text));
  }
  public void addAlign(int x, int x2, int y1, int y2) {
    if (alignArr == null)
      alignArr = new DnaAlignArr();
    alignArr.add(new DnaAlign(x, x2, y1, y2));
  }
  public void addAssembled(String from, int x, int x2, int y1, int y2) {
    if (assemArr == null)
      assemArr = new DnaAssemArr();
    assemArr.add(new ReadAssem(from, x, x2, y1, y2));
  }
  public DnaAssemArr getAssemArr() {
    return assemArr;
  }
}
