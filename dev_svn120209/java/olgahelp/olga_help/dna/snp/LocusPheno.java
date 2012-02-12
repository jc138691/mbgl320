package dna.snp;

import dna.pheno.PhenoMap;
import dna.pheno.PhenoSet;
import dna.pheno.Pheno;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/08/2009, 3:19:25 PM
 */
public class LocusPheno extends Locus {
  public static Log log = Log.getLog(LocusPheno.class);
  private PhenoMap phenoMap;
  private PhenoSet phenoSet;
  protected int[][] countMtrx;

  public LocusPheno(PhenoMap phenoMap) {
    this.phenoMap = phenoMap;
    this.phenoSet = phenoMap.getPhenoSet();
    this.countMtrx = new int[NUM_BASES][phenoSet.size()];  //
//    this.countMtrx = new int[NUM_BASES-1][phenoSet.size()];
  }
  public LocusPheno(LocusPheno from) {  //shallow/fast copy
    super(from);
    this.phenoMap = from.phenoMap;
    this.phenoSet = from.phenoSet;
    this.countMtrx = from.countMtrx;
  }

  public void count(byte base, Pheno pheno) {
    super.count(base);
    int idx = phenoSet.getIdx(pheno);

    switch (base) {
      case BASE_PAD : countMtrx[PAD][idx]++; break;           // use this if pheno-padding is needed; use new int[NUM_BASES][phenoSet.size()] then.
      case BASE_PAD_2 : countMtrx[PAD][idx]++; break;           // use this if pheno-padding is needed; use new int[NUM_BASES][phenoSet.size()] then.
      case BASE_A : countMtrx[A][idx]++; break;
      case BASE_a : countMtrx[A][idx]++; break;
      case BASE_C : countMtrx[C][idx]++; break;
      case BASE_c : countMtrx[C][idx]++; break;
      case BASE_G : countMtrx[G][idx]++; break;
      case BASE_g : countMtrx[G][idx]++; break;
      case BASE_T : countMtrx[T][idx]++; break;
      case BASE_t : countMtrx[T][idx]++; break;
      default : break;
    }
  }

  public int getCount(int baseIdx, int phenoIdx) {
    return countMtrx[baseIdx][phenoIdx];
  }

  public int getPhenoSetSize() {
    if (phenoSet == null)
      return 0;
    return phenoSet.size();
  }
}

