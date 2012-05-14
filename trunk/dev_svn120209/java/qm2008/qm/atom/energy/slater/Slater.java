package atom.energy.slater;
import atom.shell.Shell;
import math.integral.Quadr;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:50:59
 */
public class Slater extends Quadr {
  public static Log log = Log.getLog(Slater.class);
  public Slater(Quadr w) {
    super(w);
  }
  public FuncVec calcOneDensity(Shell sh, Shell sh2) {
    throw new IllegalArgumentException(log.error("must implement calcOneDensity(Shell"));
  }
  public double calcOneKin(Shell sh, Shell sh2) {
    throw new IllegalArgumentException(log.error("must implement calcKin(Shell"));
  }
  public double calcOneZPot(double z, Shell sh, Shell sh2) {
    throw new IllegalArgumentException(log.error("must implement calcKin(Shell"));
  }
  public double calcOverlap(Shell sh, Shell sh2) {
    throw new IllegalArgumentException(log.error("must implement calcInt(Shell"));
  }
  public double calcKin(int L, FuncVec wf, FuncVec wf2) {
    throw new IllegalArgumentException(log.error("must implement calcKin(WFunc"));
  }
  public double calcZPot(double z, Vec wf, Vec wf2) {
    throw new IllegalArgumentException(log.error("must implement calcZPot(WFunc"));
  }
  public double calcPot(Vec potFunc, Vec wf, Vec wf2) {
    double res = calc(wf, wf2, potFunc);  log.dbg("calcPot2=", res);
    return res;
  }
}