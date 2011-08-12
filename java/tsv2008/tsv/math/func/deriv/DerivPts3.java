package math.func.deriv;
import math.func.FuncVec;
import math.vec.grid.StepGrid;
import math.mtrx.Mtrx;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 09:25:52
 */
public class DerivPts3 extends FuncVec {
  public static Log log = Log.getLog(DerivPts3.class);
  private static double coeff[] = { // from p708 Russian A.&S.
    -3, 4, -1
    , -1, 0, 1
    , 1, -4, 3
  };
  private static int NCOL = 3;
  private static int NROW = 3;
  private static int SIZE_1 = NROW - 1;
  private static Mtrx A = new Mtrx(NROW, NCOL, coeff);
  public DerivPts3(FuncVec f) {
    super(f.getX());  log.dbg("f(x)=\n", f);
    calc(f);               log.dbg("f'(x)=\n", this);
  }
  private void calc(final FuncVec f) {
    if (!(f.getX() instanceof StepGrid)) {
      throw new IllegalArgumentException(log.error("DerivPts3 can only work with StepGrid"));
    }
    if (f.size() < A.getNumRows()) {
      throw new IllegalArgumentException(log.error("DerivPts3 needs at least 3 grid points"));
    }
    StepGrid grid = (StepGrid) f.getX();
    double h2 = 1. / (2. * grid.getGridStep());
    calc_h(h2, f);
  }
  protected void calc_h(double h2, final FuncVec f) {  log.dbg("A=\n", A);
    int max_size = f.size();
    int k = 0;
    set(k, h2 * deriv(k, 0, f));
    k++;
    for (int i = 0; i < max_size - 2 * k; i++) {
      set(i + k, h2 * deriv(k, i, f));
    }
    int i = max_size - 1 - SIZE_1;
    k++;
    set(i + k, h2 * deriv(k, i, f));
  }
  private double deriv(int idx, int i, final FuncVec f) {
    int k = 0;
    double res = A.get(idx, k++) * f.get(i++);
    res += A.get(idx, k++) * f.get(i++);
    res += A.get(idx, k++) * f.get(i++);
    return res;
  }
}


