package dna.snp;

import dna.snp.pheno.SnpPhenoOpt;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 12/08/2009, 1:28:53 PM
 */
public class SnpPheno extends Snp {
  static private StringBuffer infoBuff = new StringBuffer();
  static private StringBuffer setBuff = new StringBuffer();
  public static Log log = Log.getLog(SnpPheno.class);
  private LocusPheno phenoLoc;
//  private ThisSnpPheno phenoSpn;

  public SnpPheno(LocusPheno from) {
    super(from);
    this.phenoLoc = from;
//    phenoSpn = new ThisSnpPheno(from);
  }
  public SnpPheno(SnpPheno from) {
    super(from);
    this.phenoLoc = from.phenoLoc;
  }
  public SnpPheno(Locus from) {
    super(from);
    throw new IllegalArgumentException(log.error("call SnpPheno(LocusPheno from)"));
  }
  public String getHelpMssg(SnpOpt opt) {
    String res = super.getHelpMssg(opt);
    res = res + " \t" + getPhenoInfo(opt.getPhenoOpt());
    return res;
  }

  public String getPhenoInfo(SnpPhenoOpt opt) {
    infoBuff.setLength(0);
    boolean showDelimer = false;
    for (int i = 0; i < countArr.length; i++) {
      int count = countArr[i];
      if (count <= 0)
        continue;
      if (showDelimer) {
        infoBuff.append(' ');
      }
      showDelimer = true;
      infoBuff.append(getBase(i));
      infoBuff.append('(').append(getPhenoSet(opt, i)).append(')') ;
    }
    String res = infoBuff.toString();
    infoBuff.setLength(0);
    return res;
  }
  public String getPhenoSet(SnpPhenoOpt opt, int idx) {
    String[] list = opt.getPhenoList();
    setBuff.setLength(0);
    boolean showDelimer = false;
    for (int phenoIdx = 0; phenoIdx < phenoLoc.getPhenoSetSize(); phenoIdx++) {
      int count = phenoLoc.getCount(idx, phenoIdx);
      if (count <= 0)
        continue;
      if (showDelimer) {
        setBuff.append('/');
      }
      showDelimer = true;
      setBuff.append(list[phenoIdx]);
      if (count > 0) {
        setBuff.append('-').append(count);
      }
    }
    String res = setBuff.toString();
    setBuff.setLength(0);
    return res;
  }
//  public String getPhenoMjMiInfo(SnpPhenoOpt opt) {
//    int idxA = opt.getPhenoIdxA();
//    int idxB = opt.getPhenoIdxB();
//    String[] list = opt.getPhenoList();
//    String phenoA = list[idxA];
//    String phenoB = list[idxB];
//
//    int mjA = getMajorPhenoReads(idxA);
//    int mjB = getMajorPhenoReads(idxB);
//    int miA = getMinorPhenoReads(idxA);
//    int miB = getMinorPhenoReads(idxB);
//    char mj = getMajorBase();
//    char mi = getMinorBase();
//
//    return ""+mj + "("+phenoA+"x"+mjA+","+phenoB+"x"+mjB+")"
//      + ", "+mi + "("+phenoA+"x"+miA+","+phenoB+"x"+miB+")";
//  }
  public String getIsValidMssg() {
    return "PhenoSNP";
  }

  public double getMajorAssocToA(int phenoA, int phenoB) {
    int a = getMajorPhenoReads(phenoA);
    int b = getMajorPhenoReads(phenoB);
    int count = a + b;
    if (a <= 0  ||  count <= 0) {
      return 0;
    }
    return 100. * a / count;
  }
  public double getMinorAssocToA(int phenoA, int phenoB) {
    int a = getMinorPhenoReads(phenoA);
    int b = getMinorPhenoReads(phenoB);
    int count = a + b;
    if (a <= 0  ||  count <= 0) {
      return 0;
    }
    return 100. * a / count;
  }
  public double getMajorAssocToB(int phenoA, int phenoB) {
    int a = getMajorPhenoReads(phenoA);
    int b = getMajorPhenoReads(phenoB);
    int count = a + b;
    if (b <= 0  ||  count <= 0) {
      return 0;
    }
    return 100. * b / count;
  }
  public double getMinorAssocToB(int phenoA, int phenoB) {
    int a = getMinorPhenoReads(phenoA);
    int b = getMinorPhenoReads(phenoB);
    int count = a + b;
    if (b <= 0  ||  count <= 0) {
      return 0;
    }
    return 100. * b / count;
  }
//  public double getMajorPhenoPcnt(int phenoIdx) {
//    int mj = getMajorPhenoReads(phenoIdx);
//    int mi = getMinorPhenoReads(phenoIdx);
//    int count = mj + mi;
//    if (mj <= 0  ||  count <= 0) {
//      return 0;
//    }
//    return 100. * mj / count;
//  }
  public int getMajorPhenoReads(int phenoIdx) {
    int idx = getMajorIdx();
    return phenoLoc.getCount(idx, phenoIdx);
  }
  public int getMinorPhenoReads(int phenoIdx) {
    int idx = getMinorIdx();
    return phenoLoc.getCount(idx, phenoIdx);
  }
//  public double getMinorPhenoPcnt(int phenoIdx) {
//    int mj = getMajorPhenoReads(phenoIdx);
//    int mi = getMinorPhenoReads(phenoIdx);
//    int count = mj + mi;
//    if (mi <= 0  ||  count <= 0) {
//      return 0;
//    }
//    return 100. * mi / count;
//  }

}
