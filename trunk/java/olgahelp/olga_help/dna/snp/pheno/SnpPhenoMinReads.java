package dna.snp.pheno;

import dna.snp.SnpPheno;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 26/08/2009, 2:20:12 PM
 */
public class SnpPhenoMinReads extends SnpPheno {
  private SnpPhenoOpt opt;
  private int minReads;

  public SnpPhenoMinReads(SnpPheno snp, SnpPhenoOpt opt) {
    super(snp);
    this.opt = opt;
    minReads = opt.getMinReads().val();
  }
  public boolean isValidSnp() {
    int idxA = opt.getPhenoIdxA();
    int idxB = opt.getPhenoIdxB();
    if (idxA == idxB) {
      return false;
    }
    if (getMajorPhenoReads(idxA) >= minReads
      && getMinorPhenoReads(idxB) >= minReads) {
      return true;
    }

    // try swaping
    if (getMajorPhenoReads(idxB) >= minReads
      && getMinorPhenoReads(idxA) >= minReads) {
      return true;
    }
    return false;
  }


  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();
    return "InvalidPhenoSNP: minAllowedNmReads=" + minReads;
  }
}
