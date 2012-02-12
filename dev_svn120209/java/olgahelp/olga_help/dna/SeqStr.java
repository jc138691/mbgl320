package dna;

import dna.seq.Seq;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 20/03/2009, 3:14:04 PM
 */
public class SeqStr extends Seq {
  private String seq;

  public char getBaseChar(int i) {
    return seq.charAt(i);
  }
  final public int size() {
    if (seq == null)
      return 0;
    return seq.length();
  }
  public void setSeq(String s) {
    seq = s;
  }
}
