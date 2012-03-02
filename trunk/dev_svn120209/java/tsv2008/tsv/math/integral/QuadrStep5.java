package math.integral;
import math.func.FuncVec;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import javax.utilx.log.Log;

import static math.Mathx.dlt;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 16:42:54
*/
public class QuadrStep5 extends QuadrStep { // mistakenly known as Bode
public static Log log = Log.getLog(QuadrStep5.class);
public static final int MIN_GRID_SIZE = 5;
public QuadrStep5(StepGrid grid) {
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
  double tmp = 2.0 * step / 45.0;
  double a[] = {tmp * 7, tmp * 32, tmp * 12, tmp * 32, tmp * 7};
  return a;
}
//  public int getMinGridSize() {
//    return MIN_GRID_SIZE;
//  }
private void loadWeights(double step) {
  double tmp = 2.0 * step / 45.0;
  double a[] = {tmp * 14, tmp * 32, tmp * 12, tmp * 32};
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
public FuncVec calcFuncInt(Vec f) {
  FuncVec resF = new FuncVec(getX());
  double[] res = resF.getArr();
  double step = getStepGrid().getGridStep();

  double[] a2 = QuadrStep2.makeQuadrFuncArr(step);
  double[] a3 = QuadrStep3.makeQuadrFuncArr(step);
  double[] a4 = QuadrStep4.makeQuadrFuncArr(step);
  double[] a5 = QuadrStep5.makeQuadrFuncArr(step);

  double currTot = 0;
  double curr = 0;
  res[0] = 0; // integral from A to A
  for (int i = 1; i < size(); i++) {
    int switchType = i % 5;
    switch (switchType) {
      case 0:
       break;
      case 1:
       break;
      case 2:
       break;
      case 3:
       break;
      case 4:
       break;
      default:
    }
    set(i, currTot + curr);
  }

  return res;
}

}