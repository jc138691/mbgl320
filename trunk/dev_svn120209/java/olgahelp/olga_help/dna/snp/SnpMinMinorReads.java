package dna.snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 26/10/2009, 2:42:01 PM
 */
public class SnpMinMinorReads extends Snp {
  private int minorReads;
  public SnpMinMinorReads(Snp snp, int minorReads) {
    super(snp);
    this.minorReads = minorReads;
  }
  public boolean isValidSnp() {
    return (getMinorCount() >= minorReads);
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
    return "InvalidSNP: numOfMinorReads=" + getMinorCount() + " < minAllowed=" + minorReads;
  }
}
