package math.func.polynom.laguerre;
import math.func.FactLn;
import math.func.FuncVec;
import math.func.simple.FuncConst;
import math.func.arr.FuncArr;
import math.vec.Vec;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 09:42:35
*/
public class LgrrArr extends FuncArr {
final protected int alpha;
final protected double lambda;
protected static FactLn fLn;

public LgrrArr(final Vec x, final int size
  , final int alpha, final double lambda) {
  super(x, size);
  this.alpha = alpha;
  this.lambda = lambda;
  fLn = FactLn.getInstance();
  load();
}
public double getLambda() {
  return lambda;
}
private void load() {
  if (size() == 0)
    return;
  double[] xa = getX().getArr();
  set(0, new FuncVec(getX(), new FuncConst(1.)));
  if (size() == 1)
    return;
  set(1, new FuncVec(getX(), new FuncLagrr_1(alpha, lambda)));
  if (size() == 2)
    return;
  for (int i = 0; i < getX().size(); i++) {
    double x = lambda * xa[i];
    int n = 0;
    double L_n_2 = get(n++).get(i); // L_(n-2)
    double L_n_1 = get(n++).get(i); // L_(n-1)
    for (; n < size(); n++) { // n starts from 2
      // (n+1) L_(n+1) = ((2n+a+1)-x) L_n   - (n+a)   L_n_1  // from p588 of russian Abramowitz
      //     n L_n     = ((2n+a-1)-x) L_n_1 - (n-1+a) L_n_2
      double L_n = (((2.0 * n + alpha - 1.) - x) * L_n_1 - (n - 1. + alpha) * L_n_2) / n;
      get(n).set(i, L_n); // set (n)'th function
      L_n_2 = L_n_1;
      L_n_1 = L_n;
    }
  }
}
}

