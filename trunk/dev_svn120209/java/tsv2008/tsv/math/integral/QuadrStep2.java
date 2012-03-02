package math.integral;
import math.vec.Vec;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 2/03/12, 3:31 PM
 */
public class QuadrStep2 extends QuadrStep {
public static Log log = Log.getLog(QuadrStep2.class);
public static final int MIN_GRID_SIZE = 2;
public QuadrStep2(StepGrid grid) {
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
  double tmp = step * 0.5;
  double a[] = {tmp, tmp};
  return a;
}
private void loadWeights(double step) {
  double a = step;
  for (int i = 0; i < size(); i++) {
    set(i, a); // note: first i==0 not 1
  }
  arr[0] *= 0.5;
  arr[size() - 1] *= 0.5;
}
private boolean isValid(int size) {
  return true;
}
}