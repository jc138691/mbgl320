package dna;

import javax.utilx.FastId;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 15/04/2009, 10:11:02 AM
 */
public class ReadAssem extends DnaAlign {   // Assembled_from "Read" s1 s2 r1 r2
  private FastId readId;                     // read's id

  // see http://www.sanger.ac.uk/Software/formats/CAF/
  public ReadAssem(String from, int s, int s2, int r, int r2) {
    super(s, s2, r, r2);
    this.readId = new FastId(from);
  }
  public ReadAssem(String from) {
    this.readId = new FastId(from);
  }

  public ReadAssem(ReadAssem from) {
    super(from);
    this.readId = from.readId;
  }

  public String getReadId() {
    return readId.getId();
  }

  public int getReadIdInt() {
    return readId.getInt();
  }

  public void setReadId(String readId) {
    this.readId = new FastId(readId);
  }
}
