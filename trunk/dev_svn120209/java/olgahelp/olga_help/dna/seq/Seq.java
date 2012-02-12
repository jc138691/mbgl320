package dna.seq;

import math.vec.ByteVec;

import javax.utilx.FastId;

import dna.DnaFinals;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 17/02/2009 at 12:03:05.
 */
public abstract class Seq {
  private FastId id;
  private byte[] qltyArr;

  public Seq() {
  }
  public Seq(Seq from) {
    this.id = from.id;
    this.qltyArr = from.qltyArr;
  }

  abstract public char getBaseChar(int i);
  public byte getBaseByte(int i) {
    return (byte)getBaseChar(i);
  }
  public final String getBaseStr(int i) {
//    String s = map.getContig(seq[i]);  // this is to make sure that only one copy of each base string is created, .e.g. "C"
//    if (s != null)
//      return s;
    return Character.toString(getBaseChar(i));
  }
  abstract public int size();
  abstract public void setSeq(String s);
  public int getOffsetL(int toIdx) {
    return toIdx;
  }
  public int getOffsetR(int toIdx) {
    return size() - 1 - toIdx;
  }

  public final String getId() {
    if (id == null)
      return null;
    return id.getId();
  }

  public final int getIdKey() {
    return id.getInt();
  }

  public final void setId(String str) {
    this.id = new FastId(str);
  }

  public int getQlty(int colIdx) {
    if (size() <= colIdx)
      return -1;
    if (qltyArr == null  ||   qltyArr.length <= colIdx   ||  colIdx < 0)
      return -1;
    return qltyArr[colIdx];
  }
  protected final byte[] getQltyArr() {
    return qltyArr;
  }

  public final void setQltyArr(byte[] qltyArr) {
    this.qltyArr = qltyArr;
  }
//  public final void setQlty(int idx, byte q) {
//    if (qltyArr == null  ||  idx >= qltyArr.length)
//      return;
//    qltyArr[idx] = q;
//  }

  final public int calcMaxQlty() {
    if (qltyArr == null  || qltyArr.length == 0)
      return 0;
    return ByteVec.max(qltyArr);
  }

  final public int calcMinQlty() {
    if (qltyArr == null  || qltyArr.length == 0)
      return 0;
    return ByteVec.min(qltyArr);
  }

  public boolean isPadding(int colIdx) {
    if (size() <= colIdx)
      return true;
    return (getBaseByte(colIdx) == DnaFinals.BASE_PAD)
    || (getBaseByte(colIdx) == DnaFinals.BASE_PAD_2);
  }

}
