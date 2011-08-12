package math.func;
import math.mtrx.MtrxToString;
import math.vec.Vec;
import math.vec.FastLoop;
import math.func.Func;
import math.func.deriv.DerivFactory;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 15:51:49
 */
// FuncVec is a map of calc.vec to math.vec
public class FuncVec extends Vec {
  private Vec x;
  private FuncVec drv; // first derivative
  private FuncVec drv2; // second derivative
  public FuncVec(final FuncVec from) {
    super(from);
    this.x = from.x;
  }
  public FuncVec(final Vec x) {
    super(x.size());
    this.x = x;
  }
  public FuncVec(Vec x, Vec y) {
    super(y);
    this.x = x;
  }
  public FuncVec(final Vec x, final Func f) {
    super(x.size());
    this.x = x;
    if (f != null)  {
      this.calc(f);
    }
  }
  public FuncVec copyXY()     {
    Vec xCopy = x.copy();
    Vec yCopy = super.copy();
    return new FuncVec(xCopy, yCopy);
  }
  public FuncVec copyY()     {
    Vec yCopy = super.copy();
    return new FuncVec(x, yCopy);
  }
  public void calc(Func f) {
    FastLoop.calc(arr, x.getArr(), f);
  }
  final public void setX(Vec xGrid) {
    this.x = xGrid;
  }
  final public Vec getX() {
    return     x;
  }
  final public Vec getY() {
    return     this;
  }
  public String toTab() {
    return new FuncVecToString(this).toTab();
  }

  public String toString() {
    return "x=" + x
      + "\ny=" + super.toString();
//      + "\ny'=" + drv;
  }
  final public Vec getDrv() {
    if (drv == null) {
      drv = DerivFactory.makeDeriv(this);
    }
    return drv;
  }
  final public Vec getDrv2() {
    if (drv == null) {
      drv = DerivFactory.makeDeriv(this);
    }
    if (drv2 == null) {
      drv2 = DerivFactory.makeDeriv(drv);
    }
    return drv2;
  }
}
