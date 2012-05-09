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
//  double h2 = 0.5 * h;
//  res[i] = res[i - STEP] + h2 * (f[i] + f[i - STEP]); // i=1

  double y0 = f[i - STEP];
  double y1 = f[i];
  double y2 = f[i + STEP];
  // y0 = c
  // y1 = a h^2 + b h + c
  // y2 = 4 a h^2 + 2 b h + c
//  double a = (y2 - 2. * y1 - c) / (2. * h * h);
//  double b = (4. * y1 - y2 + 3c) / (2. * h);
//  double x = a / 3. * h^3 + bh^2 / 2. + c h;
  double a = (y2 - 2. * y1 + y0) / 2.;
  double b = (4. * y1 - y2 - 3. * y0) / 2.;
  double x = (a / 3.  + b / 2. + y0) * h;
  res[i] = res[i - STEP] + x;

  i += STEP;
  double h3 = h / 3.;
  res[i] = h3 * calcPts3(i, f); // i = 3

  i += STEP;
  double h4 = 3. * h / 8.;
  res[i] = h4 * calcPts4(i, f); // i = 3

  i += STEP;
  double h5 = 2. * h / 45.;
  res[i] = h5 * calcPts5(i, f); // i = 4

  i += STEP;
  double h6 = 5. * h / 288.;
  res[i] = h6 * calcPts6(i, f); // i = 5

  double h7 = 1. / 140. * h;
  i += STEP;
  for ( ; i >= 0  &&  i < size; i += STEP) {
    res[i] = res[i- STEP * N_STEPS] + h7 * calcPts7(i, f);
  }
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

