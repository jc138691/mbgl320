package math.integral;
import math.vec.Vec;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 2/03/12, 3:31 PM
 */
public class QuadrStep3 extends QuadrStep {
public static Log log = Log.getLog(QuadrStep3.class);
public static final int MIN_GRID_SIZE = 3;
public QuadrStep3(StepGrid grid) {
  super(grid, 3);
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
  double tmp = step / 3.0;
  double a[] = {tmp, tmp * 4, tmp};
  return a;
}
public static double[] makeQuadrArr_2From3(double step) {
  double tmp = step / 12.0;
  double a[] = {5. * tmp, 8. * tmp, -tmp};
  return a;
}
private void loadWeights(double step) {
  double tmp = step / 3.0;
  double a[] = {tmp * 2, tmp * 4};
  for (int i = 0; i < size(); i++) {
    set(i, a[i % 2]); // note: first i==0 not 1
  }
  arr[0] *= 0.5;
  arr[size() - 1] *= 0.5;
}
}