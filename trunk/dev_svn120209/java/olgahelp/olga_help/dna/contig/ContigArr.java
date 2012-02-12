package dna.contig;

import dna.seq.arr.SeqArr;
import dna.*;
import dna.seq.Seq;
import dna.seq.SeqBytes;
import dna.pheno.PhenoMap;

import javax.swing.*;
import javax.utilx.log.Log;
import java.util.ArrayList;

import math.vec.ByteVec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 15/04/2009, 10:40:08 AM
 */
public class ContigArr {
  public static Log log = Log.getLog(ContigArr.class);
  private ArrayList<Contig> arr;
  private SeqArr srcReads;
  private PhenoMap phenoArr;

  public ContigArr() {
    arr = new ArrayList<Contig>();
    srcReads = new SeqArr();
  }

  public ContigArr(ContigArr from) {
    this.arr = from.arr;
    srcReads = from.srcReads;
  }

  public Contig getContig(int idx) {
    return arr.get(idx);
  }
  public SeqArr getSrcReads() {
    return srcReads;
  }

  public void addContig(Contig s) {
    arr.add(s);
    if (s.getId() == null  ||  s.getId().length() == 0) {
      s.setId("Contig#" + arr.size());
    }
    loadContig(s);
  }

  private void loadContig(Contig contig) {
    DnaAssemArr arr = contig.getAssemArr();
    for (ReadAssem assm : arr) {
      int id = assm.getReadIdInt();
      Seq readDna = srcReads.getByKey(id);    // try the parent table
      if (readDna == null) {
//        read = contig.getByKey(id);     // try itself
//        if (read == null) {
        String error = "Missing 'read' (in loadContig): Asking to load Contig without read=" + assm.getReadId();
        log.error(error);
        JOptionPane.showMessageDialog(null, error);
        throw new UnsupportedOperationException(error);
//        }
      }
      Seq dna = makeSeq(assm, readDna, contig); log.info("dna=", dna); //@Override
      contig.add(dna);
    }
  }

  public Seq makeSeq(ReadAssem fromAssm, Seq parentRead, Contig contig) {
    SeqBytes child = new SeqBytes();
    int maxS = fromAssm.getMaxS();
    int minS = fromAssm.getMinS();
    byte[] baseArr = ByteVec.make(maxS, (byte)' ');
    byte[] qltyArr = ByteVec.make(maxS, (byte)-1);

    int maxR = fromAssm.getMaxR();
    int minR = fromAssm.getMinR();
    int copySize = maxR - minR + 1;
    try {
      if (fromAssm.getS() > fromAssm.getS2()) {  // see http://www.sanger.ac.uk/Software/formats/CAF/
        for (int i = 0; i < copySize; i++) {
          int contigIdx = minS + i - 1;      // to
          int readIdx = maxR - i - 1;        // from
          byte b = DnaBytesFactory.makeReverse(parentRead.getBaseByte(readIdx));
          baseArr[contigIdx] = b;
//          qltyArr[contigIdx] = (byte)parentRead.getQlty(readIdx);
          qltyArr[contigIdx] = getReadQlty(readIdx, parentRead, contigIdx, contig);
        }
      }
      else {
        for (int i = 0; i < copySize; i++) {
          int contigIdx = minS + i - 1;
          int readIdx = minR + i - 1;
          baseArr[contigIdx] = parentRead.getBaseByte(readIdx);
//          qltyArr[contigIdx] = (byte)parentRead.getQlty(readIdx);
          qltyArr[contigIdx] = getReadQlty(readIdx, parentRead, contigIdx, contig);
        }
      }
    }
    catch (Exception e) {
      int dbg = 1;
    }
    child.setSeq(baseArr);
    child.setQltyArr(qltyArr);
    child.setId(parentRead.getId());
//    return child;
    SeqChild res = new SeqChild(child, parentRead, fromAssm);
    return res;
  }

  public byte getReadQlty(int readIdx, Seq read, int contigIdx, Contig contig) {
    return (byte)read.getQlty(readIdx);
  }

  public int size() {
    return arr.size();
  }

  public void addRead(Seq read) {
    srcReads.add(read);
  }

  public Contig getLast() {
    return arr.get(arr.size()-1);
  }

  public void setPhenoArr(PhenoMap phenoArr) {
    this.phenoArr = phenoArr;
  }

  public PhenoMap getPhenoArr() {
    return phenoArr;
  }
}
