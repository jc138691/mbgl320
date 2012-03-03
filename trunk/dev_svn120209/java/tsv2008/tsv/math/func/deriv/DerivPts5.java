package math.func.deriv;
import math.vec.grid.StepGrid;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 09:34:03
*/
public class DerivPts5 extends FuncVec {
public static Log log = Log.getLog(DerivPts5.class);
protected static double arrDer[][] = { // from p708
  {-50, 96, -72, 32, -6},
  {-6, -20, 36, -12, 2},
  {2, -16, 0, 16, -2},
  {-2, 12, -36, 20, 6},
  {6, -32, 72, -96, 50}
};
protected static double arr4x4[][] = {
  {-50, 96, -72, 32},
  {-6, -20, 36, -12},
  {2, -16, 0, 16},
  {-2, 12, -36, 20},
};
private static int NCOL = 5;
private static int NROW = 5;
private static int SIZE_1 = NROW - 1;
public DerivPts5(FuncVec f) {
  super(f.getX()); log.dbg("f(x)=\n", f);
  calc(f);               log.dbg("f'(x)=\n", this);
}
public DerivPts5(FuncVec f, boolean noCalc) {
  super(f.getX()); log.dbg("f(x)=\n", f);
}
private void calc(final FuncVec fv) {
  if (!(fv.getX() instanceof StepGrid)) {
    throw new IllegalArgumentException("DerivPts5 can only work with StepGrid");
  }
  if (fv.size() < 5) {
    throw new IllegalArgumentException("DerivPts5 needs at least 5 grid points");
  }
  StepGrid grid = (StepGrid) fv.getX();
  double h2 = 1. / (24. * grid.getGridStep());
  calcPts(h2, arrDer, fv.getArr());
}
protected void calcPts(double norm, final double[][] a, final double[] f) {
  int max_size = f.length;
  int destIdx = 0;
  set(destIdx, norm * calcPts5(destIdx, 0, a, f));    destIdx++;
  set(destIdx, norm * calcPts5(destIdx, 0, a, f));    destIdx++;
  for (int i = 0; i < max_size - 2 * destIdx; i++) {
    set(i + destIdx, norm * calcPts5(destIdx, i, a, f));
  }
  int i = max_size - 1 - SIZE_1;      destIdx++;
  set(i + destIdx, norm * calcPts5(destIdx, i, a, f));    destIdx++;
  set(i + destIdx, norm * calcPts5(destIdx, i, a, f));
}
private double calcPts5(int rIdx, int i, final double[][] a, final double[] f) {
  int col = 0;
  double res = 0;
  res += a[rIdx][col++] * f[i++];  // 0
  res += a[rIdx][col++] * f[i++];  // 1
  res += a[rIdx][col++] * f[i++];  // 2
  res += a[rIdx][col++] * f[i++];  // 3
  res += a[rIdx][col++] * f[i++];  // 4
  return res;
}
}
