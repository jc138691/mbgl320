package math.func.intrg;
import math.Calc;
import math.func.FuncVec;
import math.vec.VecFactory;
import math.vec.grid.StepGrid;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 3/05/12, 1:03 PM
 */
// Integral_0^r f(x) dx.
public class IntgInftyPts7 extends IntgPts7 {
public IntgInftyPts7(FuncVec f) {
  super(f, -1);
  mult(-1); // due to int_r^0
  add(-get(0));
}
//protected void calc_h(final FuncVec fv) {
//  double[] res = getArr();
//  double[] f = fv.getArr();
//  StepGrid grid = (StepGrid) fv.getX();
//  double h = grid.getGridStep();
//
//  int size = fv.size();
//  for (int i = 0; i < N_STEPS; i++) {
//    int idx = size - 1 - i;
//    res[idx] = 0;
//    if (!Calc.isZero(f[idx])) {
//      throw new IllegalArgumentException(log.error("!Calc.isZero(f[idx])"));
//    }
//  }
//
//
////  double h7 = 3 / 10. * h;  // Weddle's rule: not good!!!
//  double h7 = 1. / 140. * h;
//  for (int i = size - 1; i >= N_STEPS; i--) {
//    res[i- N_STEPS] = res[i] - h7 * calcPts7(i, f);
//  }
//  this.add(-res[0]); // res[0] must be ZERO
//}
}
