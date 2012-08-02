package math.func.arr;
import math.func.FuncVecToString;
import math.vec.Vec;
import math.vec.VecArr;
import math.func.FuncVec;
import math.func.Func;

import javax.utilx.log.Log;
import javax.langx.SysProp;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 15:49:08
 */
public class FuncArr extends VecArr implements IFuncArr {
  public static Log log = Log.getLog(FuncArr.class);
  private Vec x;
  public FuncArr(final Vec x, int arrSize) {
    this.x = x;
    init(x, arrSize);
  }
  public FuncArr(final Vec x) {
    this.x = x;
  }
  public FuncArr(FuncArr from) {
    super(from);
    x = from.x;
  }
final public FuncVec getFunc(int i) {
  return (FuncVec)super.get(i);
}
final public void setFunc(int i, FuncVec fv) {
  super.set(i, fv);
}
  final public FuncVec get(int i) {
    return (FuncVec)super.get(i);
  }
final public FuncVec getLast() {
  return (FuncVec)super.getLast();
}
final public FuncVec getFirst() {
  return (FuncVec)super.getFirst();
}
  private void init(Vec x, int arrSize) {
    for (int i = 0; i < arrSize; i++) {
      add(new FuncVec(x));
    }
  }
  final public Vec getX() {
    return x;
  }
  public void setX(Vec x) {
    this.x = x;
    for (int i = 0; i < size(); i++) {
      getFunc(i).setX(x);
    }
  }
  public String toString() {
    return "x=" + x + SysProp.EOL + super.toString();
  }
  public String toTab() {
    return new FuncArrToString(this).toTab();
  }
  public String toCSV() {
    return x.toCSV() + SysProp.EOL + super.toCSV();
  }
  public void mult(final Func func) {
    FuncVec f = new FuncVec(x, func);
    mult(f);
  }

  public void copyFrom(FuncArr fromArr, int idxFirst, int idxLastExcl) {
    for (int i = idxFirst; i < idxLastExcl; i++) {
        set(i, fromArr.get(i));
    }
  }
  public FuncArr copyDeepY() {
    FuncArr res = new FuncArr(x, size());
    for (int i = 0; i < size(); i++) {
      FuncVec newCopy = getFunc(i).copyY();
      res.set(i, newCopy);
    }
    return res;
  }
}

