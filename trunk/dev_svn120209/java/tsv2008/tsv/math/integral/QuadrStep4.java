package math.integral;
import math.vec.Vec;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 2/03/12, 3:30 PM
 */
public class QuadrStep4 extends QuadrStep {
public static Log log = Log.getLog(QuadrStep4.class);
public static final int MIN_GRID_SIZE = 4;
public QuadrStep4(StepGrid grid) {
  super(grid);
  if (isValid(size()))   {
    loadWeights(grid.getGridStep());
  }
  else {
    throw new IllegalArgumentException(log.error("invalid size=" + size()));
  }
}
public Vec makeQuadrFuncInt(double step) {
  double a[] = makeQuadrFuncArr(step);
  return new Vec(a);
}
public static double[] makeQuadrFuncArr(double step) {
  double tmp = 3.0 * step / 8.0;
  double a[] = {tmp, tmp * 3, tmp * 3, tmp};
  return a;
}
private void loadWeights(double step) {
  double tmp = 3.0 * step / 8.0;
  double a[] = {tmp * 2, tmp * 3, tmp * 3};
  for (int i = 0; i < size(); i++) {
    set(i, a[i % 3]); // note: first i==0 not 1
  }
  arr[0] *= 0.5;
  arr[size() - 1] *= 0.5;
}
private boolean isValid(int size) {
  if ((size - 1) % 3 != 0) {
    int n = (size - 1) / 3;
    String error = "if ((size - 1) % 3 != 0); "
      + ((size - 1) % 3) + "!=0; "
      + "nearest sizes = " + (3 * n + 1) + " or " + (3 * (n + 1) + 1);
    throw new IllegalArgumentException(log.error(error));
  }
  return true;
}
}