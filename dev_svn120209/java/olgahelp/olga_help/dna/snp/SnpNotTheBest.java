package dna.snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 12/06/2009, 4:10:24 PM
 */
public class SnpNotTheBest  extends Snp {
  private Snp best;

  public SnpNotTheBest(Snp snp, Snp best) {
    super(snp);
    this.best = best;
  }
  public boolean isValidSnp() {
    return (best.getMinorFreqPcnt() < getMinorFreqPcnt());
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
    return "NotTheBestSNP: bestMinorAlleleFreq=" + (float)best.getMinorFreqPcnt() + "% > thisMinorFreq=" + (float)getMinorFreqPcnt();
  }
}