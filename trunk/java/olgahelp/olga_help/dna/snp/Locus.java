package dna.snp;

import dna.DnaFinals;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/05/2009, 4:44:24 PM
 */
public class Locus implements DnaFinals {
  static private StringBuffer buff = new StringBuffer();
  public static Log log = Log.getLog(Locus.class);
  protected static final int A = 0;
  protected static final int C = 1;
  protected static final int T = 2;
  protected static final int G = 3;
  protected static final int PAD = 4; // padding
  protected static final int NUM_BASES = 5;
  protected int[] countArr;
  private int numReads;

  public Locus() {
    init();
  }
  public Locus(Locus from) {
    countArr = from.countArr;
    numReads = from.numReads;
  }

  private void init() {
    countArr = new int[NUM_BASES];
  }

  public String toString() {
    buff.setLength(0);
    boolean showDelimer = false;
    for (int i = 0; i < countArr.length; i++) {
      int count = countArr[i];
      if (count <= 0)
        continue;
      if (showDelimer) {
        buff.append('/');
      }
      showDelimer = true;
      buff.append(getBase(i));
      if (count > 1) {
        buff.append('x').append(count);
      }
    }
    String res = buff.toString();
    buff.setLength(0);
    return res;
  }

  public int getNumReads() {
    return numReads;
  }
  public int getNumPads() {
    return countArr[PAD];
  }
  public double getPaddingPcnt() {
    if ((numReads + countArr[PAD]) == 0)
      return 0;
    return 100. * countArr[PAD] / (numReads + countArr[PAD]);
  }
  public double getInsertsPcnt() {
    return 100.0 - getPaddingPcnt();
  }
//  public boolean isSnp() {
//    throw new IllegalArgumentException(log.error("call new Snp(Locus snp) first"));
//  }

  public void count(byte base) {
    switch (base) {
      case BASE_PAD : countArr[PAD]++; break;  // NOTE: padding does not count!!!
      case BASE_PAD_2 : countArr[PAD]++; break;  // NOTE: padding does not count!!!
      case BASE_A : countArr[A]++; numReads++; break;
      case BASE_a : countArr[A]++; numReads++; break;
      case BASE_C : countArr[C]++; numReads++; break;
      case BASE_c : countArr[C]++; numReads++; break;
      case BASE_G : countArr[G]++; numReads++; break;
      case BASE_g : countArr[G]++; numReads++; break;
      case BASE_T : countArr[T]++; numReads++; break;
      case BASE_t : countArr[T]++; numReads++; break;
      default : break;
    }
  }

  public char getBase(int i) {
    switch (i) {
      case PAD : return BASE_PAD;
      case A : return BASE_A;
      case C : return BASE_C;
      case G : return BASE_G;
      case T : return BASE_T;
      default : return 'X';
    }
  }

  public char getIUPAC() {
    int code = 0;
    if (countArr[A] != 0)  code |= CODE_A;
    if (countArr[C] != 0)  code |= CODE_C;
    if (countArr[G] != 0)  code |= CODE_G;
    if (countArr[T] != 0)  code |= CODE_T;

    switch (code) {
      case CODE_A : return BASE_A;
      case CODE_C : return BASE_C;
      case CODE_G : return BASE_G;
      case CODE_T : return BASE_T;

      case CODE_AC : return IUPAC_AC;
      case CODE_AG : return IUPAC_AG;
      case CODE_AT : return IUPAC_AT;

      case CODE_CG : return IUPAC_CG;
      case CODE_CT : return IUPAC_CT;

      case CODE_GT : return IUPAC_GT;

      case CODE_CGT : return IUPAC_CGT;
      case CODE_AGT : return IUPAC_AGT;
      case CODE_ACT : return IUPAC_ACT;
      case CODE_ACG : return IUPAC_ACG;

      case CODE_ACGT : return IUPAC_ACGT;

      default : return 'x';
    }
  }
  public boolean hasPadding() {
    return countArr[PAD] > 0;
  }
}
