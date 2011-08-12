package dna.snp.pheno;

import dna.snp.SnpPheno;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 18/08/2009, 2:38:08 PM
 */
public class SnpPhenoMinAssoc extends SnpPheno {
  private SnpPhenoOpt opt;
  private int minFreqPcnt;

  public SnpPhenoMinAssoc(SnpPheno snp, SnpPhenoOpt opt) {
    super(snp);
    this.opt = opt;
//    minFreqPcnt = opt.getMaxPValue().val();
  }

  public boolean isValidSnp() {
    int idxA = opt.getPhenoIdxA();
    int idxB = opt.getPhenoIdxB();
    if (idxA == idxB) {
      return false;
    }
    if (getMajorAssocToA(idxA, idxB) >= minFreqPcnt
      && getMinorAssocToB(idxA, idxB) >= minFreqPcnt) {
      return true;
    }
    // try swaping
    if (getMajorAssocToB(idxA, idxB) >= minFreqPcnt
      && getMinorAssocToA(idxA, idxB) >= minFreqPcnt) {
      return true;
    }
    return false;
  }
//  public boolean isValidSnp() {
//    int idxA = opt.getPhenoIdxA();
//    int idxB = opt.getPhenoIdxB();
//    if (idxA == idxB) {
//      return false;
//    }
//    if (getMajorPhenoPcnt(idxA) >= minFreqPcnt
//      && getMinorPhenoPcnt(idxB) >= minFreqPcnt) {
//      return true;
//    }
//    // try swaping
//    if (getMajorPhenoPcnt(idxB) >= minFreqPcnt
//      && getMinorPhenoPcnt(idxA) >= minFreqPcnt) {
//      return true;
//    }
//    return false;
//  }


  public String getIsValidMssg() {
    if (isValidSnp())
      return super.getIsValidMssg();

    int idxA = opt.getPhenoIdxA();
    int idxB = opt.getPhenoIdxB();
    String[] list = opt.getPhenoList();
    String phenoA = list[idxA];
    String phenoB = list[idxB];

    float mjA = (float)getMajorAssocToA(idxA, idxB);
    float mjB = (float)getMajorAssocToB(idxA, idxB);
    float miA = (float)getMinorAssocToA(idxA, idxB);
    float miB = (float)getMinorAssocToB(idxA, idxB);
    char mj = getMajorBase();
    char mi = getMinorBase();

    return "InvalidPhenoSNP: minAllowedPhenoFreq%=" + minFreqPcnt
      + ", "+mj + "("+phenoA+"-"+mjA+","+phenoB+"-"+mjB+")"
      + ", "+mi + "("+phenoA+"-"+miA+","+phenoB+"-"+miB+")"
      ;
  }
//  public String getIsValidMssg() {
//    if (isValidSnp())
//      return super.getIsValidMssg();
//
//    int idxA = opt.getPhenoIdxA();
//    int idxB = opt.getPhenoIdxB();
//    String[] list = opt.getPhenoList();
//    String phenoA = list[idxA];
//    String phenoB = list[idxB];
//
//    float mjA = (float)getMajorPhenoPcnt(idxA);
//    float mjB = (float)getMajorPhenoPcnt(idxB);
//    float miA = (float)getMinorPhenoPcnt(idxA);
//    float miB = (float)getMinorPhenoPcnt(idxB);
//    char mj = getMajorBase();
//    char mi = getMinorBase();
//
//    return "InvalidPhenoSNP: minAllowedPhenoFreq%=" + minFreqPcnt
//      + ", "+mj + "("+phenoA+"-"+mjA+","+phenoB+"-"+mjB+")"
//      + ", "+mi + "("+phenoA+"-"+miA+","+phenoB+"-"+miB+")"
//      ;
//  }
}