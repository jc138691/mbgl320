package atom.wf.lr;
import math.func.FuncVec;
import math.func.simple.FuncExp;
import math.func.Func;
import math.vec.Vec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 16:41:32
 */
public class TransLrToR extends FuncVec {
  private Vec R2;
  private Vec divSqrtR;
  private final FuncVec mapLogRToR2;
  // Just to help keeping track
  // equal step in x=ln(r)
  public TransLrToR(Vec x) {
    super(x, new FuncExp(1));
    mapLogRToR2 = new FuncVec(x, new functorLogRToR2());
  }
  public FuncVec getMapLogRToR2() {
    return mapLogRToR2;
  }
  public Vec getR2() {
    if (R2 == null)
      R2 = new FuncVec(getX(), new functorLogRToR2());
    return R2;
  }
  public Vec getDivSqrtR() {
    if (divSqrtR == null)
      divSqrtR = new FuncVec(getX(), new FuncLogRToDivSqrtR());
    return divSqrtR;
  }
  private class FuncLogRToR implements Func {
    // f(x)=r
    public double calc(double x) {
      return Math.exp(x);
    }
  }
  private class functorLogRToR2 extends FuncLogRToR {
    // f(x)=r^2
    public double calc(double x) {
      double r = super.calc(x);
      return r * r;
    }
  }
  private class FuncLogRToDivSqrtR extends FuncLogRToR {
    // f(x)=1/sqrt(r)
    public double calc(double x) {
      double cr = super.calc(x);
      return 1. / Math.sqrt(cr);
    }
  }
}

