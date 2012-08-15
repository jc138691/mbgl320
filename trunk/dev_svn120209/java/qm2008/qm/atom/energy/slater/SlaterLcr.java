package atom.energy.slater;
import atom.energy.pw.lcr.PotHLcr;
import atom.shell.Shell;
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 11:43:22
 */
public class SlaterLcr extends SlaterLr {
  final private TransLcrToR logCRToR; // r = exp(x)-exp(xFirst)
  final private WFQuadrLcr wLogCR;
  private PotHLcr partH;
  public SlaterLcr(WFQuadrLcr w) {
    super(w);
    this.logCRToR = w.getLcrToR();
    wLogCR = w;
    partH = new PotHLcr(w);
  }

  public final TransLcrToR getLogCRToR() {
    return logCRToR;
  }
  public final WFQuadrLcr getQaudrLcr() {
    return wLogCR;
  }
  /**
   * THIS FUNCTION IS BASIS DEPENDANT !!!!!!!!!!!
   * see derivation in PotHLcr
   * HF = -1/2F" +1/2*{1/4 + L(L+1)*(y/r)^2}*F + y^2U(r)*F
   * //  INTL dx Fn'(x) H Fn(x) = delta_n'n
   */
//  protected double calcKinL(Shell sh, Shell SH2) {
//    int L = sh.getWfL();
//    if (L != SH2.getWfL())
//      return 0.;
//    return calcKinL(L, sh.getWf(), SH2.getWf());
//  }
  public double calcKin(int L, FuncVec wf, FuncVec wf2) {
    return partH.calcKin(L, wf, wf2);
  }
  public FuncVec calcOneDensity(Shell sh, Shell SH2) {
    int L = sh.getWfL();
    if (L != SH2.getWfL())
      return null;
    FuncVec res = sh.getWf().copyY();
    res.multSelf(SH2.getWf());
    return res;
  }
  public double calcOneZPot(double z, Shell sh, Shell SH2) {
    if (sh.getWfL() != SH2.getWfL())
      return 0.;
    double int_z = calc(sh.getWf(), SH2.getWf(), logCRToR.getCR2DivR());
    double res = z * int_z;
    return res;
  }
  public double calcOverlap(Shell sh, Shell SH2) {
    if (sh.getWfL() != SH2.getWfL())
      return 0.;
    double res = calc(sh.getWf(), SH2.getWf(), logCRToR.getCR2());
    return res;
  }
}

