package io.caf;

import dna.seq.SeqBytes;
import dna.DnaAlign;
import dna.DnaAlignArr;
import dna.contig.Contig;

import math.vec.ByteVec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 14/04/2009, 2:09:33 PM
 */
public class CafToDnaFactory {
  static SeqBytes makeReadFromCaf(CafDna from) {
    if (from.isPadded()) {
      return makeReadFromCafPadded(from);
    }
    return makeReadFromCafNotPadded(from);
  }

  static SeqBytes makeReadFromCafNotPadded(CafDna from) {
    SeqBytes res = new SeqBytes();
    int size = from.calcDnaSize();
    byte[] baseArr = ByteVec.make(size, (byte)'*');
    byte[] qltyArr = ByteVec.make(size, (byte)-1);

    DnaAlignArr arr = from.getAlignArr();
    try {
      for (DnaAlign align : arr) {
        int offset = align.getS() - align.getR();
        for (int i = align.getR(); i <= align.getR2(); i++) {
          baseArr[offset + i - 1] = from.getBaseByte(i-1);
          qltyArr[offset + i - 1] = (byte)from.getQlty(i-1);
        }
      }
    }
    catch (Exception e) {
      int dbg = 1;
    }
    res.setSeq(baseArr);
    res.setQltyArr(qltyArr);
    res.setId(from.getId());
    return res;
  }
  static SeqBytes makeReadFromCafPadded(CafDna from) {
    SeqBytes res = new SeqBytes(from);
    return res;
  }
  public static Contig makeContigFromCaf(CafDna caf) {
    Contig res = new Contig();
    res.setAssemArr(caf.getAssemArr());
    res.setId(caf.getId());
    return res;
  }
}
