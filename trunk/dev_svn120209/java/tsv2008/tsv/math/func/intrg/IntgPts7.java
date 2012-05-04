package math.func.intrg;
import math.func.FuncVec;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 3/05/12, 12:58 PM
 */
public class IntgPts7 extends FuncVec {
public static Log log = Log.getLog(IntgPts7.class);
protected static final int N_STEPS = 6;
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
protected void calc_h(final FuncVec fv) {  log.setDbg();
  double[] res = getArr();
  double[] f = fv.getArr();
  StepGrid grid = (StepGrid) fv.getX();
  double h = grid.getGridStep();

  int size = fv.size();

  double h4 = h / 24.;
  res[0] = 0;                                      // i=0
  double d = 0;

  int i = 1;
  res[i] = res[i - 1] + h4 * calcPts4(i, f); // 1

  i++;
  res[i] = res[i - 1] + h4 * calcPts4(i, f); // 2

  i++;
  res[i] = res[i - 1] + h4 * calcPts4(i, f); // 3

  i++;
  res[i] = res[i - 1] + h4 * calcPts4(i, f); // 4
//  log.dbg("res[idx="+idx+"]=" + res[idx]);
//  d += 0.5 * h * (f[idx-1] + f[idx]);
//  log.dbg("d - res[idx]=" + (d - res[idx]));
//  res[idx] = d;


  i++;
  res[i] = res[i - 1] + h4 * calcPts4(i, f); // 5
//  log.dbg("res[idx="+idx+"]=" + res[idx]);
//  d += 0.5 * h * (f[idx-1] + f[idx]);
//  log.dbg("d - res[idx]=" + (d - res[idx]));
//  res[idx] = d;

// NOT as good as 7pts  Newton-Cotes Formulas
//  // Weddle's rule
//  double h7 = 3. / 10. * h;
//  for (i = N_STEPS; i < size; i++) {
//    res[i] = res[i- N_STEPS] + h7 * calcPts7_Weddle(i, f);
//  }

  double h7 = 1. / 140. * h;
  for (i = N_STEPS; i < size; i++) {
    res[i] = res[i- N_STEPS] + h7 * calcPts7(i, f);
  }
}

protected double calcPts7_Weddle(int idx, final double[] f) {
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
protected double calcPts7(int idx, final double[] f) {
//Newton-Cotes Formulas
  double res = 0;
  res += f[idx--] * 41.;  // +6
  res += f[idx--] * 216.;  // +5
  res += f[idx--] * 27;  // +4
  res += f[idx--] * 272.;  // +3
  res += f[idx--] * 27;  // +2
  res += f[idx--] * 216.;  // +1
  res += f[idx]   * 41.;  // +0
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
public static double[] makePts5(double step) {
  double tmp = 2.0 * step / 45.0;
  double a[] = {tmp * 7, tmp * 32, tmp * 12, tmp * 32, tmp * 7};
  return a;
}
}

