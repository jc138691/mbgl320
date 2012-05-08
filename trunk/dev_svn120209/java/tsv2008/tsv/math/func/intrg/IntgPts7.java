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
private int STEP;
public IntgPts7(FuncVec f) {
  this(f, 1);       //log.dbg("f(x)=\n", f);
}
protected IntgPts7(FuncVec f, int step) {
  super(f.getX());       //log.dbg("f(x)=\n", f);
  this.STEP = step;
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
  int i = 0;
  if (STEP == -1)
    i = size - 1;
  res[i] = 0;                                      // i=0

  i += STEP;
  double h4_Wilf = h / 24.;
  res[i] = res[i - STEP] + h4_Wilf * calcPts4_Wilf(i, f); // i=1

  i += STEP;
//  res[i] = res[i - 1] + h4_Wilf * calcPts4_Wilf(i, f); // i=2
  double h3 = h / 3.;
  res[i] = h3 * calcPts3(i, f); // i = 3

  i += STEP;
//  res[i] = res[i - 1] + h4_Wilf * calcPts4_Wilf(i, f); // i=3
  double h4 = 3. * h / 8.;
  res[i] = h4 * calcPts4(i, f); // i = 3

  i += STEP;
  double h5 = 2. * h / 45.;
  res[i] = h5 * calcPts5(i, f); // i = 4

  i += STEP;
  double h6 = 5. * h / 288.;
  res[i] = h6 * calcPts6(i, f); // i = 5

// NOT as good as 7pts  Newton-Cotes Formulas
//  // Weddle's rule
//  double h7 = 3. / 10. * h;
//  for (i = N_STEPS; i < size; i++) {
//    res[i] = res[i- N_STEPS] + h7 * calcPts7_Weddle(i, f);
//  }

  double h7 = 1. / 140. * h;
  i += STEP;
  for ( ; i >= 0  &&  i < size; i += STEP) {
    res[i] = res[i- STEP * N_STEPS] + h7 * calcPts7(i, f);
  }
}

protected double calcPts7_Weddle(int idx, final double[] f) {
  double res = 0;
  res += f[idx];  // +6
  idx -= STEP;
  res += f[idx] * 5.;  // +5
  idx -= STEP;
  res += f[idx];  // +4
  idx -= STEP;
  res += f[idx] * 6.;  // +3
  idx -= STEP;
  res += f[idx];  // +2
  idx -= STEP;
  res += f[idx] * 5.;  // +1
  idx -= STEP;
  res += f[idx];  // +0
  return res;
}
protected double calcPts7(int idx, final double[] f) {
//Newton-Cotes Formulas
  double res = 0;
  res += f[idx] * 41.;  // +6
  idx -= STEP;
  res += f[idx] * 216.;  // +5
  idx -= STEP;
  res += f[idx] * 27;  // +4
  idx -= STEP;
  res += f[idx] * 272.;  // +3
  idx -= STEP;
  res += f[idx] * 27;  // +2
  idx -= STEP;
  res += f[idx] * 216.;  // +1
  idx -= STEP;
  res += f[idx] * 41.;  // +0
  return res;
}
private double calcPts4_Wilf(int idx, final double[] f) {
  double res = 0;
  idx -= STEP;
  res += f[idx] * 9.;
  idx += STEP;
  res += f[idx] * 19.;
  idx += STEP;
  res += f[idx] * (-5.);
  idx += STEP;
  res += f[idx];
  return res;
}
private double calcPts3(int idx, final double[] f) {
//Newton-Cotes Formulas
  double res = 0;
  res += f[idx];  // +2
  idx -= STEP;
  res += f[idx] * 4.;  // +1
  idx -= STEP;
  res += f[idx];  // +0
  return res;
}
private double calcPts4(int idx, final double[] f) {
//Newton-Cotes Formulas
  double res = 0;
  res += f[idx];  // +3
  idx -= STEP;
  res += f[idx] * 3;  // +2
  idx -= STEP;
  res += f[idx] * 3.;  // +1
  idx -= STEP;
  res += f[idx];  // +0
  return res;
}
private double calcPts5(int idx, final double[] f) {
//Newton-Cotes Formulas
  double res = 0;
  res += f[idx] * 7.;  // +4
  idx -= STEP;
  res += f[idx] * 32.;  // +3
  idx -= STEP;
  res += f[idx] * 12;  // +2
  idx -= STEP;
  res += f[idx] * 32.;  // +1
  idx -= STEP;
  res += f[idx] * 7.;  // +0
  return res;
}
private double calcPts6(int idx, final double[] f) {
//Newton-Cotes Formulas
  double res = 0;
  res += f[idx] * 19.;  // +5
  idx -= STEP;
  res += f[idx] * 75;  // +4
  idx -= STEP;
  res += f[idx] * 50.;  // +3
  idx -= STEP;
  res += f[idx] * 50;  // +2
  idx -= STEP;
  res += f[idx] * 75.;  // +1
  idx -= STEP;
  res += f[idx] * 19.;  // +0
  return res;
}
public static double[] makePts5(double step) {
  double tmp = 2.0 * step / 45.0;
  double a[] = {tmp * 7, tmp * 32, tmp * 12, tmp * 32, tmp * 7};
  return a;
}
}

