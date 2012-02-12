package dna.snp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 14/05/2009, 4:35:48 PM
 */
public class SnpMinMinorFreq extends Snp {
  private int minFreqPcnt;

  public SnpMinMinorFreq(Snp snp, int v) {
    super(snp);
    this.minFreqPcnt = v;
  }
  public boolean isValidSnp() {
    return (getMinorFreqPcnt() >= minFreqPcnt);
  }
  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
    return "InvalidSNP: minorAlleleFreq=" + (float)getMinorFreqPcnt() + "% < minAllowed=" + minFreqPcnt;
  }
}