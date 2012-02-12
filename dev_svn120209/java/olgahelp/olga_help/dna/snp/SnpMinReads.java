package dna.snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 11/05/2009, 10:46:24 AM
 */
public class SnpMinReads extends Snp {
  private int minReads;
  public SnpMinReads(Snp snp, int minReads) {
    super(snp);
    this.minReads = minReads;
  }
  public boolean isValidSnp() {
    return (getNumReads() >= minReads);
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
    return "InvalidSNP: numOfReads=" + getNumReads() + " < minAllowed=" + minReads;
  }
}
