package math.func.intrg;
import math.func.FuncVec;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 3/05/12, 12:58 PM
 */
public class IntgPts7 extends FuncVec {
public static Log log = Log.getLog(IntgPts7.class);
private static final int STEP_PTS7 = 6;
public IntgPts7(FuncVec f) {
  super(f.getX());       //log.dbg("f(x)=\n", f);
  calc(f);               //log.dbg("f'(x)=\n", this);
}
private void calc(final FuncVec f) {
  if (!(f.getX() instanceof StepGrid)) {
    throw new IllegalArgumentException(log.error("IntgPts7 can only work with StepGrid"));
  }
  if (f.size() < 7) {
    throw new IllegalArgumentException(log.error("IntgPts7 needs at least 7 grid points"));
  }
  calc_h(f);
}
protected void calc_h(final FuncVec fv) {
  double[] res = getArr();
  double[] f = fv.getArr();
  StepGrid grid = (StepGrid) fv.getX();
  double h = grid.getGridStep();

  int size = fv.size();

  double h4 = h / 24.;
  res[0] = 0;                                      // 0
  int idx = 1;
  res[idx] = res[idx - 1] + h4 * calcPts4(idx, f); // 1
  idx++;
  res[idx] = res[idx - 1] + h4 * calcPts4(idx, f); // 2
  idx++;
  res[idx] = res[idx - 1] + h4 * calcPts4(idx, f); // 3
  idx++;
  res[idx] = res[idx - 1] + h4 * calcPts4(idx, f); // 4
  idx++;
  res[idx] = res[idx - 1] + h4 * calcPts4(idx, f); // 5

  // Weddle's rule
  double h7 = 3 / 10. * h;
  for (int i = STEP_PTS7; i < size; i++) {
    res[i] = res[i- STEP_PTS7] + h7 * calcPts7(i, f);
  }
}

private double calcPts7(int idx, final double[] f) {
  double res = 0;
  res += f[idx--];  // +6
  res += f[idx--] * 5.;  // +5
  res += f[idx--];  // +4
  res += f[idx--] * 6.;  // +3
  res += f[idx--];  // +2
  res += f[idx--] * 5.;  // +1
  res += f[idx];  // +0
  return res;
}
private double calcPts4(int idx, final double[] f) {
  double res = 0;
  res += f[idx - 1] * 9.;
  res += f[idx] * 19.;
  res += f[idx + 1] * (-5.);
  res += f[idx + 2];
  return res;
}
}

