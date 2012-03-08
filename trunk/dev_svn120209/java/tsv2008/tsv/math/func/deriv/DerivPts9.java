package math.func.deriv;
import math.func.FuncVec;
import math.vec.grid.StepGrid;
import math.mtrx.Mtrx;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:26:46
 */
public class DerivPts9 extends FuncVec {
  public static Log log = Log.getLog(DerivPts9.class);
  public static final int MIN_GRID_SIZE = 9;
  static double coeff[] = { // from p24 of Bickley MathGaz 25 (1941)
    -109584., 322560., -564480, 752640, -705600
    , -5040., -64224., 141120, -141120, 117600
    , 720., -11520., -38304, 80640, -50400
    , -240., 2880., -20160, -18144, 50400
    , 144., -1536., 8064, -32256, 0
    , -144., 1440., -6720, 20160, -50400
    , 240., -2304., 10080, -26880, 50400
    , -720., 6720., -28224, 70560, -117600
    , 5040., -46080., 188160, -451584, 705600
  };
  private static int N_COL = 5;
  private static int N_ROW = 9;
  private static int SIZE_1 = N_ROW - 1;
  static Mtrx A = new Mtrx(N_ROW, N_COL, coeff);

  public DerivPts9(FuncVec f) {
    super(f.getX());       //log.dbg("f(x)=\n", f);
    calc(f);               //log.dbg("f'(x)=\n", this);
  }
  private void calc(final FuncVec f) {
    if (!(f.getX() instanceof StepGrid)) {
      throw new IllegalArgumentException(log.error("DerivPts9 can only work with StepGrid"));
    }
    if (f.size() < 9) {
      throw new IllegalArgumentException(log.error("DerivPts9 needs at least 9 grid points"));
    }
    StepGrid grid = (StepGrid) f.getX();
    double h2 = 0.5 / (20160. * grid.getGridStep());
    calc_h(h2, f);
  }
  protected void calc_h(double h2, final FuncVec f) {
    int max_size = f.size();
    int k = 0;
    set(k, h2 * deriv(k, 0, f));    k++;
    set(k, h2 * deriv(k, 0, f));    k++;
    set(k, h2 * deriv(k, 0, f));    k++;
    set(k, h2 * deriv(k, 0, f));    k++;
    for (int i = 0; i < max_size - 2 * k; i++) {
      set(i + k, h2 * deriv(k, i, f));
    }
    int i = max_size - 1 - SIZE_1;      k++;
    set(i + k, h2 * deriv(k, i, f));    k++;
    set(i + k, h2 * deriv(k, i, f));    k++;
    set(i + k, h2 * deriv(k, i, f));    k++;
    set(i + k, h2 * deriv(k, i, f));
  }

  //=============================
  //   First derivative error -> step^9
  //============================
  private double deriv(int idx, int i, final FuncVec f) {
    int k = 0;
    double res = A.get(idx, k++) * f.get(i++); // 0
    res += A.get(idx, k++) * f.get(i++);  // 1
    res += A.get(idx, k++) * f.get(i++);  // 2
    res += A.get(idx, k++) * f.get(i++);  // 3
    res += A.get(idx, k--) * f.get(i++);  // 4
    int mid = A.getNumRows() / 2;
    idx = mid + mid - idx;
    res -= A.get(idx, k--) * f.get(i++);  // 5 -> 3  // NOTE -= not += !!!!!!!!!!!
    res -= A.get(idx, k--) * f.get(i++);  // 6 -> 2
    res -= A.get(idx, k--) * f.get(i++);  // 7 -> 1
    res -= A.get(idx, k--) * f.get(i++);  // 8 -> 0
    return res;
  }
}

