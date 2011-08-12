package dna.snp;

import dna.snp.flank.FlankArr;

import javax.utilx.log.Log;

import math.vec.IntVec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 11/05/2009, 11:05:30 AM
 */
public class Snp extends Locus {
  static private StringBuffer buff = new StringBuffer();
  public static Log log = Log.getLog(Snp.class);
  private int minorIdx;
  private int majorIdx;
  private FlankArr flankR;
  private FlankArr flankL;
  final private int[] sortedIdx;

  public Snp(Locus from) {
    super(from);
    sortedIdx = IntVec.sortIdx(countArr);
    loadMajorMinor();
  }

  private void loadMajorMinor() {
    majorIdx = NUM_BASES - 1;
    minorIdx = NUM_BASES - 2;
    if (sortedIdx[majorIdx] == PAD) {
      minorIdx--;
      majorIdx--;
      return;
    }
    if (sortedIdx[minorIdx] == PAD) {
      minorIdx--;
    }
  }

  protected Snp(Snp from) {
    super(from);
    this.flankR = from.flankR;
    this.flankL = from.flankL;
    this.sortedIdx = from.sortedIdx;
    this.majorIdx = from.majorIdx;
    this.minorIdx = from.minorIdx;
  }

  public int getMinFlankLen() {
    int R = 0;
    int L = 0;
    if (flankR != null)
      R = flankR.size();
    if (flankL != null)
      L = flankL.size();
    return Math.min(R, L);
  }
  public int getMaxFlankLen() {
    int R = 0;
    int L = 0;
    if (flankR != null)
      R = flankR.size();
    if (flankL != null)
      L = flankL.size();
    return Math.max(R, L);
  }
  public boolean isSnp() {
    return (getMinorCount() > 0);   // isSnip if the last but one is not ZERO.
  }
  public boolean isValidSnp() {
    return isSnp();
  }
  public String getIsValidMssg() {
    return "SNP";
  }

  public void count(byte base) {//not allowed!!!!!
    throw new IllegalArgumentException(log.error("not allowed to call Snp.count()!!!"));
  }
  public String toString() {
    throw new IllegalArgumentException(log.error("use toCsv(SnpOpt opt)"));
  }

  public String toString(SnpOpt opt) {
    buff.setLength(0);
    if (getFlankL() != null)
      buff.append(getFlankL().toString(opt.getFlankFilter()));
    buff.append('[');
    buff.append(getMajorBase());
    buff.append('/');
    buff.append(getMinorBase());
    buff.append(']');
    if (getFlankR() != null)
      buff.append(getFlankR().toString(opt.getFlankFilter()));
    String res = buff.toString();
    buff.setLength(0);
    return res;
  }
  public String getHelpMssg(SnpOpt opt) {
    String res = super.toString();
    if (!isSnp()) {
      return res;
    }
    res = res + " \t" + getIsValidMssg();
    return res;
  }

  public int getMinorCount() {
    return countArr[sortedIdx[minorIdx]];
  }
  public int getMajorCount() {
    return countArr[sortedIdx[majorIdx]];
  }
  public char getMinorBase() {
    if (getMinorCount() == 0)
      return BASE_PAD;
    return getBase(sortedIdx[minorIdx]);
  }
  public int getMinorIdx() {
    if (getMinorCount() == 0)
      return PAD;
    return sortedIdx[minorIdx];
  }
  public char getMajorBase() {
    if (getMajorCount() == 0)
      return BASE_PAD;
    return getBase(sortedIdx[majorIdx]);
  }
  public int getMajorIdx() {
    if (getMajorCount() == 0)
      return PAD;
    return sortedIdx[majorIdx];
  }
  public char getIUPAC() {
    if (getMinorCount() == 0) {
      if (getMajorCount() == 0)
        return '-';
      return getBase(sortedIdx[majorIdx]);
    }
    return super.getIUPAC();
  }

  public double getMinorFreqPcnt() {
    int minor = getMinorCount();
    int count = getNumReads(); 
    if (count <= 0) {
      return 0;
    }
    return 100. * minor / count;
  }


  public FlankArr getFlankR() {
    return flankR;
  }
  public FlankArr getFlankL() {
    return flankL;
  }
  public void setFlankR(FlankArr f) {
    this.flankR = f;
  }
  public void setFlankL(FlankArr f) {
    this.flankL = f;
  }
}
