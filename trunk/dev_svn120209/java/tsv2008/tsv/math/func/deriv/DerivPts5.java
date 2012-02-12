package math.func.deriv;
import math.vec.grid.StepGrid;
import math.func.FuncVec;
import math.mtrx.Mtrx;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 09:34:03
 */
public class DerivPts5 extends FuncVec {
  public static Log log = Log.getLog(DerivPts5.class);
  static double coeff[] = { // from p708
    -50, 96, -72, 32, -6,
    -6, -20, 36, -12, 2,
    2, -16, 0, 16, -2,
    -2, 12, -36, 20, 6,
    6, -32, 72, -96, 50
  };
  private static int NCOL = 5;
  private static int NROW = 5;
  private static int SIZE_1 = NROW - 1;
  static Mtrx A = new Mtrx(NROW, NCOL, coeff);
  public DerivPts5(FuncVec f) {
    super(f.getX()); log.dbg("f(x)=\n", f);
    calc(f);               log.dbg("f'(x)=\n", this);
  }
  private void calc(final FuncVec f) {
    if (!(f.getX() instanceof StepGrid)) {
      throw new IllegalArgumentException("DerivPts5 can only work with StepGrid");
    }
    if (f.size() < 5) {
      throw new IllegalArgumentException("DerivPts5 needs at least 5 grid points");
    }
    StepGrid grid = (StepGrid) f.getX();
    double h2 = 1. / (24. * grid.getGridStep());
    calc_h(h2, f);
  }
  protected void calc_h(double h2, final FuncVec f) {
    int max_size = f.size();
    int k = 0;
    set(k, h2 * deriv(k, 0, f));    k++;
    set(k, h2 * deriv(k, 0, f));    k++;
    for (int i = 0; i < max_size - 2 * k; i++) {
      set(i + k, h2 * deriv(k, i, f));
    }
    int i = max_size - 1 - SIZE_1;      k++;
    set(i + k, h2 * deriv(k, i, f));    k++;
    set(i + k, h2 * deriv(k, i, f));
  }
  private double deriv(int idx, int i, final FuncVec f) {
    int k = 0;
    double res = A.get(idx, k++) * f.get(i++); // 0
    res += A.get(idx, k++) * f.get(i++);  // 1
    res += A.get(idx, k++) * f.get(i++);  // 2
    res += A.get(idx, k++) * f.get(i++);  // 3
    res += A.get(idx, k++) * f.get(i++);  // 4
    return res;
  }
}
