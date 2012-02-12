package dna.snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/06/2009, 4:54:29 PM
 */
public class SnpMaxNghbrPadding extends Snp {
  private int maxPadPcnt;
  private Snp badNbr;

  public SnpMaxNghbrPadding(Snp snp, int v, Snp badNbr) {
    super(snp);
    this.maxPadPcnt = v;
    this.badNbr = badNbr;
  }
  public boolean isValidSnp() {
    return (badNbr.getPaddingPcnt() <= maxPadPcnt);
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
    return "InvalidSNP: badNeighbourPadding=" + (float)badNbr.getPaddingPcnt() + "% > maxAllowedPadding=" + maxPadPcnt;
  }
}