package math.integral;
import math.vec.grid.StepGrid;
import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 16:42:54
 */
public class BooleQuadr extends Quadr { // mistakenly known as Bode
  public static Log log = Log.getLog(BooleQuadr.class);
  private StepGrid stepGrid;
  public static final int MIN_GRID_SIZE = 5;
  public BooleQuadr(StepGrid grid) {
    super(grid);
    stepGrid = grid;
    if (isValid(size()))   {
      loadWeights(grid.getGridStep());
    }
    else {
      throw new IllegalArgumentException(log.error("invalid size=" + size()));
    }
  }
//  public int getMinGridSize() {
//    return MIN_GRID_SIZE;
//  }
  private void loadWeights(double step) {
    double tmp = 2.0 * step / 45.0;
    double a[] = {tmp * 14, tmp * 32, tmp * 12, tmp * 32};
//         a(0) = 32d0 * tmp
//         a(1) = 14d0 * tmp
//         a(2) = 32d0 * tmp
//         a(3) = 12d0 * tmp
    for (int i = 0; i < size(); i++) {
      set(i, a[i % 4]); // note: first i==0 not 1
    }
    arr[0] *= 0.5;
    arr[size() - 1] *= 0.5;
  }
  private boolean isValid(int size) {
    //c     Bode's w
    // 5 * n - (n - 1) = N grid points
    // 4 * n + 1 = N
    // n = (N - 1) / 4
    if ((size - 1) % 4 != 0) {
      int n = (size - 1) / 4;
      String error = "if ((size - 1) % 4 != 0); "
        + ((size - 1) % 4) + "!=0; "
        + "nearest sizes = " + (4 * n + 1) + " or " + (4 * (n + 1) + 1);
      throw new IllegalArgumentException(log.error(error));
    }
    return true;
  }
  public StepGrid getStepGrid() {
    return stepGrid;
  }
}