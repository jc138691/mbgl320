package dna.snp.pheno;

import dna.snp.SnpPheno;
import stats.FisherPValue;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 02/10/2009, 1:27:23 PM
 */
public class SnpPhenoMaxPVal extends SnpPheno {
  private SnpPhenoOpt opt;
  private double maxPVal;

  public SnpPhenoMaxPVal(SnpPheno snp, SnpPhenoOpt opt) {
    super(snp);
    this.opt = opt;
    maxPVal = opt.getMaxPValue().val();
  }

  public boolean isValidSnp() {
    int idxA = opt.getPhenoIdxA();
    int idxB = opt.getPhenoIdxB();
    if (idxA == idxB) {
      return false;
    }
    int mjA = getMajorPhenoReads(idxA);
    int mjB = getMajorPhenoReads(idxB);
    int miA = getMinorPhenoReads(idxA);
    int miB = getMinorPhenoReads(idxB);
    double pVal = FisherPValue.getTwoTailedP(mjA, mjB, miA, miB);
    if (pVal <= maxPVal) {
      return true;
    }
    return false;
  }

  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();

    int idxA = opt.getPhenoIdxA();
    int idxB = opt.getPhenoIdxB();
    String[] list = opt.getPhenoList();
    String phenoA = list[idxA];
    String phenoB = list[idxB];

    int mjA = getMajorPhenoReads(idxA);
    int mjB = getMajorPhenoReads(idxB);
    int miA = getMinorPhenoReads(idxA);
    int miB = getMinorPhenoReads(idxB);
    char mj = getMajorBase();
    char mi = getMinorBase();
    double pVal = FisherPValue.getTwoTailedP(mjA, mjB, miA, miB);
    return "InvalidPhenoSNP: pValue= "+(float)pVal+" > maxAllowedPVal=" + (float)maxPVal
      + ", "+mj + "("+phenoA+"-"+mjA+","+phenoB+"-"+mjB+")"
      + ", "+mi + "("+phenoA+"-"+miA+","+phenoB+"-"+miB+")"
      ;
  }
}