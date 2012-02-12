package io.ace;

import dna.contig.ContigArr;
import dna.contig.Contig;
import dna.ReadAssem;
import dna.seq.Seq;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 09/10/2009, 2:56:30 PM
 */
public class AceContigArr extends ContigArr {
  private int numContigs;
  private int numReads;

  public int getNumContigs() {
    return numContigs;
  }

  public void setNumContigs(int numContigs) {
    this.numContigs = numContigs;
  }

  public int getNumReads() {
    return numReads;
  }

  public void setNumReads(int numReads) {
    this.numReads = numReads;
  }

  public Seq makeSeq(ReadAssem fromAssm, Seq fromRead, Contig forContig) {
    AceReadAssem assm = (AceReadAssem)fromAssm;
    int parentReadLen = fromRead.size();
    int s = assm.getStartPos();
    int r = 1;
    assm.setR(r);
    assm.setR2(r + parentReadLen - 1);
//    if (assm.isComplemented()) {
//      assm.setS2(s);
//      assm.setS(s + parentReadLen - 1);
//    }
//    else {
    assm.setS(s);
    assm.setS2(s + parentReadLen - 1);
//    }
    return super.makeSeq(fromAssm, fromRead, forContig);
  }

  public byte getReadQlty(int readIdx, Seq read, int contigIdx, Contig contig) {
    AceRead aceRead = (AceRead)read;
    AceContig aceContig = (AceContig)contig;
    if ((readIdx+1) < aceRead.getQltyStart()
      ||  (readIdx+1) > aceRead.getQltyEnd()) {
      return AceRead.LOW_QLTY;
    }
    byte qlty = (byte)aceContig.getConsens().getQlty(contigIdx);
    if (qlty == -1)
      return AceRead.LOW_QLTY;
    return qlty;
  }

}
