package io.ace;

import dna.SeqStr;
import dna.contig.Contig;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 12/10/2009, 9:45:05 AM
 */
public class AceContig extends Contig {
  private int numBases;
  private int numReads;
  private int numSegments;
  private SeqStr consens; // consensus

  public AceContig() {
    consens = new SeqStr();
  }

  public void setNumBases(int numBases) {
    this.numBases = numBases;
  }

  public int getNumBases() {
    return numBases;
  }

  public void setNumReads(int numReads) {
    this.numReads = numReads;
  }

  public int getNumReads() {
    return numReads;
  }

  public void setNumSegments(int numSegments) {
    this.numSegments = numSegments;
  }

  public int getNumSegments() {
    return numSegments;
  }

  public SeqStr getConsens() {
    return consens;
  }

}
