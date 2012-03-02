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
  double tmp = step / 3.0;
  double a[] = {tmp, tmp * 4, tmp};
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
private boolean isValid(int size) {
  if ((size - 1) % 2 != 0) {
    int n = (size - 1) / 2;
    String error = "if ((size - 1) % 2 != 0); "
      + ((size - 1) % 2) + "!=0; "
      + "nearest sizes = " + (2 * n + 1) + " or " + (2 * (n + 1) + 1);
    throw new IllegalArgumentException(log.error(error));
  }
  return true;
}
}