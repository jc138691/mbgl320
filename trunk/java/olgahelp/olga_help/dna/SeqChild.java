package dna;

import dna.seq.SeqBytes;
import dna.seq.Seq;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/06/2009, 12:34:31 PM
 */
public class SeqChild extends SeqBytes {
  private Seq parent;
  private int maxR;
  private int minR;
  private int maxS;
  private int minS;
  private boolean rev;   // reverse

  public SeqChild(SeqBytes child, Seq parent, ReadAssem assm) {
    super(child);
    this.parent = parent;
    this.rev = assm.getS() > assm.getS2();
    maxS = assm.getMaxS();
    minS = assm.getMinS();
    maxR = assm.getMaxR();
    minR = assm.getMinR();
  }

  final public int getOffsetL(int toIdx) {
//    if (rev) {
//      return parent.size() - 1 - getOffsetR(toIdx);
//    }
    return toIdx - (minS - 1);  // trim what is visible
//    return toIdx - (minS - minR); // trim what is available
  }
  final public int getOffsetR(int toIdx) {
//    if (rev) {
//      return maxR - (toIdx - minS + 1) - 1;
//    }
//    return parent.size() - 1 - getOffsetL(toIdx);
    return maxS - toIdx - 1;  // trim what is visible
  }
}
