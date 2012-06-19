package math.integral;
import math.func.FuncVec;
import math.func.intrg.IntgPts7;
import math.mtrx.api.Mtrx;
import math.vec.Vec;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 16:42:54
 */
public class QuadrPts5 extends QuadrStep { // mistakenly known as Bode
public static final int PTS_N = 5;
public static Log log = Log.getLog(QuadrPts5.class);
private Mtrx mIntr;
public QuadrPts5(StepGrid grid) {
  super(grid, 5);
  if (isValid(size())) {
    loadWeights(grid.getGridStep());
  } else {
    throw new IllegalArgumentException(log.error("invalid size=" + size()));
  }
}
//public Vec makeQuadrFuncInt(double step) {
//  double a[] = makeQuadrArr(step);
//  return new Vec(a);
//}
//public static double[] makeQuadrArr(double step) {
//  double tmp = 2.0 * step / 45.0;
//  double a[] = {tmp * 7, tmp * 32, tmp * 12, tmp * 32, tmp * 7};
//  return a;
//}
private void loadWeights(double step) {
  double tmp = 2.0 * step / 45.0;
  double a[] = {tmp * 14, tmp * 32, tmp * 12, tmp * 32};
  for (int i = 0; i < size(); i++) {
    set(i, a[i % 4]); // note: first i==0 not 1
  }
  arr[0] *= 0.5;
  arr[size() - 1] *= 0.5;
}
public FuncVec calcFuncIntOK(Vec funcV) {
  double[] f = funcV.getArr();
  FuncVec resF = new FuncVec(getX());
  double[] res = resF.getArr();
  double step = getStepGrid().getGridStep();
  double[] a2 = QuadrStep2.makeQuadrArr(step);
  double[] a32 = QuadrStep3.makeQuadrArr_2From3(step);
  double[] a3 = QuadrStep3.makeQuadrArr(step);
  double[] a4 = QuadrStep4.makeQuadrArr(step);
  double[] a5 = IntgPts7.makePts5(step);
  double currTot = 0;
  double curr = 0;
  int ptsPerStep = getNextN();
  res[0] = 0; // integral from A to A
  for (int i = 1; i < size(); i++) {               // NOTE: starts from 1
    int switchType = (i - 1) % ptsPerStep;
    switch (switchType) {
      case 0:
//        curr = a2[0] * f[i-1] + a2[1] * f[i];
        curr = a32[0] * f[i - 1] + a32[1] * f[i] + a32[2] * f[i + 1];
        break;
      case 1:
        curr = a3[0] * f[i - 2] + a3[1] * f[i - 1] + a3[2] * f[i];
        break;
      case 2:
        curr = a4[0] * f[i - 3] + a4[1] * f[i - 2] + a4[2] * f[i - 1] + a4[3] * f[i];
        break;
      case 3:
        curr = a5[0] * f[i - 4] + a5[1] * f[i - 3] + a5[2] * f[i - 2] + a5[3] * f[i - 1] + a5[4] * f[i];
        currTot += curr;
        curr = 0;
        break;
      default:
    }
    res[i] = currTot + curr;
  }
  return resF;
}
}