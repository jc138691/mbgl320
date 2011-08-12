package dna.pheno;

import javax.utilx.FastId;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/08/2009, 12:34:05 PM
 */
public class PhenoRead {
  private FastId readId;
  private Pheno pheno;

  public final void setReadId(String str) {
    this.readId = new FastId(str);
  }
  public final void setPheno(String str) {
    this.pheno = new Pheno(str);
  }
  public int getReadIdKey() {
    return readId.getInt();
  }
  public String getReadIdStr() {
    if (readId == null)
      return null;
    return readId.getId();
  }

  public Pheno getPheno() {
    return pheno;
  }
}
