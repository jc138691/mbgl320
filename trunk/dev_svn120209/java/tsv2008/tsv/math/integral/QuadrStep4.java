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
  super(grid, 4);
  if (isValid(size()))   {
    loadWeights(grid.getGridStep());
  }
  else {
    throw new IllegalArgumentException(log.error("invalid size=" + size()));
  }
}
public Vec makeQuadrFuncInt(double step) {
  double a[] = makeQuadrArr(step);
  return new Vec(a);
}
public static double[] makeQuadrArr(double step) {
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
}