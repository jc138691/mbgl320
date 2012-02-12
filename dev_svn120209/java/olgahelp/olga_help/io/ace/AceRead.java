package io.ace;

import dna.SeqStr;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 12/10/2009, 12:33:11 PM
 */
public class AceRead extends SeqStr {
  private int numBases;
  private int numInfoItems;
  private int numTags;
  private int qltyStart;
  private int qltyEnd;
  private int alignStart;
  private int alignEnd;
  public static final byte LOW_QLTY = 1;

  public void setNumBases(int numBases) {
    this.numBases = numBases;
  }

//  public int getNumBases() {
//    return numBases;
//  }

  public void setNumInfoItems(int numInfoItems) {
    this.numInfoItems = numInfoItems;
  }

//  public int getNumInfoItems() {
//    return numInfoItems;
//  }

  public void setNumTags(int numTags) {
    this.numTags = numTags;
  }

//  public int getNumTags() {
//    return numTags;
//  }

  public void setQltyStart(int qltyStart) {
    this.qltyStart = qltyStart;
  }

  public int getQltyStart() {
    return qltyStart;
  }

  public void setQltyEnd(int qltyEnd) {
    this.qltyEnd = qltyEnd;
  }

  public void setAlignStart(int alignStart) {
    this.alignStart = alignStart;
  }

  public int getQltyEnd() {
    return qltyEnd;
  }

//  public int getAlignStart() {
//    return alignStart;
//  }

  public void setAlignEnd(int alignEnd) {
    this.alignEnd = alignEnd;
  }

//  public int getAlignEnd() {
//    return alignEnd;
//  }
}
