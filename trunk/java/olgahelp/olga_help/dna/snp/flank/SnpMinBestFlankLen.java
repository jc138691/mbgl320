package dna.snp.flank;

import dna.snp.Snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 28/01/2010, 11:07:29 AM
 */
public class SnpMinBestFlankLen extends Snp {
  private int len;

  public SnpMinBestFlankLen(Snp snp, int len) {
    super(snp);
    this.len = len;
  }

  public boolean isValidSnp() {
    return (getMaxFlankLen() >= len);
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
//    if (getMinFlankLen() == 0) {
//      int dbg = 1;
//    }
    return "InvalidSNP: bestFlankLen=" + getMaxFlankLen() + " < minAllowed=" + len;
  }
}