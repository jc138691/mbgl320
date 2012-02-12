package dna.snp.flank;

import dna.snp.Snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 18/05/2009, 4:50:41 PM
 */
public class SnpMinFlankLen extends Snp {
  private int minLen;
  public SnpMinFlankLen(Snp snp, int minLen) {
    super(snp);
    this.minLen = minLen;
  }
  public boolean isValidSnp() {
    return (getMinFlankLen() >= minLen);
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
//    if (getMinFlankLen() == 0) {
//      int dbg = 1;
//    }
    return "InvalidSNP: flankLen=" + getMinFlankLen() + " < minAllowed=" + minLen;
  }
}