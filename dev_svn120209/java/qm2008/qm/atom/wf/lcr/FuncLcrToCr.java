package atom.wf.lcr;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:42:05
 */
public class FuncLcrToCr implements Func {
  // f(x)=CR
  public double calc(double x) {
    return Math.exp(x);
  }
}
