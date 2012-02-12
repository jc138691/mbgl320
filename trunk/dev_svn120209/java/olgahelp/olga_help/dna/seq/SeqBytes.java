package dna.seq;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 20/03/2009, 3:13:51 PM
 */
public class SeqBytes extends Seq {
  protected byte[] seq;

  public SeqBytes(SeqBytes from) {
    super(from);
    this.seq = from.seq;
  }
  public SeqBytes() {
  }

  public char getBaseChar(int i) {
    return (char)seq[i];
  }
  public byte getBaseByte(int i) {
    return seq[i];
  }
  final public int size() {
    if (seq == null)
      return 0;
    return seq.length;
  }
  public final void setSeq(byte[] seq) {
    this.seq = seq;
  }
  public void setSeq(String s) {
    seq = new byte[s.length()];
    for (int i = 0; i < seq.length; i++) {
      char c = s.charAt(i);
      byte b = (byte)c;
      seq[i] = b;
//      map.put(b, Character.toCsv(c));
    }
  }
}
