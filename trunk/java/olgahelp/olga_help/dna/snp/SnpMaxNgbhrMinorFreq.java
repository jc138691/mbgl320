package dna.snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/06/2009, 4:58:19 PM
 */
public class SnpMaxNgbhrMinorFreq extends Snp {
  private int maxMinorFreqPcnt;
  private Snp badNbr;     // bad neighbour

  public SnpMaxNgbhrMinorFreq(Snp snp, int v, Snp badNbr) {
    super(snp);
    this.maxMinorFreqPcnt = v;
    this.badNbr = badNbr;
  }
  public boolean isValidSnp() {
    return (badNbr.getMinorFreqPcnt() <= maxMinorFreqPcnt);
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
    return "InvalidSNP: badNeighbourMinorAlleleFreq=" + (float)badNbr.getMinorFreqPcnt() + "% > maxAllowedMinorFreq=" + maxMinorFreqPcnt;
  }
}