package atom.energy.slater;
import atom.energy.part_wave.PartHLcr;
import atom.shell.Shell;
import atom.wf.log_cr.TransLcrToR;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 11:43:22
 */
public class SlaterLcr extends SlaterLr {
  final private TransLcrToR logCRToR; // r = exp(x)-exp(xFirst)
  final private WFQuadrLcr wLogCR;
  private PartHLcr partH;
  public SlaterLcr(WFQuadrLcr w) {
    super(w);
    this.logCRToR = w.getLcrToR();
    wLogCR = w;
    partH = new PartHLcr(w);
  }

  public final TransLcrToR getLogCRToR() {
    return logCRToR;
  }
  public final WFQuadrLcr getQuadrLCR() {
    return wLogCR;
  }
  /**
   * THIS FUNCTION IS BASIS DEPENDANT !!!!!!!!!!!
   * see derivation in PartHLcr
   * HF = -1/2F" +1/2*{1/4 + L(L+1)*(y/r)^2}*F + y^2U(r)*F
   * //  INTL dx Fn'(x) H Fn(x) = delta_n'n
   */
  protected double calcKinL(Shell sh, Shell SH2 /* make it stand out*/) {
    int L = sh.getWfL();
    if (L != SH2.getWfL())
      return 0.;
    return calcKinL(L, sh.getWf(), SH2.getWf());
  }
  public double calcKin(int L, FuncVec wf, FuncVec wf2) {
    return partH.calcKin(L, wf, wf2);
//    return calcKinDrv(wf, wf2) + calcKinL(L, wf, wf2);
  }
  public FuncVec calcOneDensity(Shell sh, Shell SH2 /* make it stand out*/) {
    int L = sh.getWfL();
    if (L != SH2.getWfL())
      return null;
//    FuncVec res = new FuncVec(sh.getWf());      // BUG!!! shallow y-copy
    FuncVec res = sh.getWf().copyY();
    res.mult(SH2.getWf());
//    res.multSelf(logCRToR.getCR2());
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

