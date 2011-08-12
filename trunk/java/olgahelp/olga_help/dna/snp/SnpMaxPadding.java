package dna.snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 04/06/2009, 12:41:59 PM
 */
public class SnpMaxPadding extends Snp {
  private int maxPadPcnt;

  public SnpMaxPadding(Snp snp, int v) {
    super(snp);
    this.maxPadPcnt = v;
  }
  public boolean isValidSnp() {
    return (getPaddingPcnt() <= maxPadPcnt);
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
    return "InvalidSNP: padding=" + (float)getPaddingPcnt() + "% > maxAllowedPadding=" + maxPadPcnt;
  }
}