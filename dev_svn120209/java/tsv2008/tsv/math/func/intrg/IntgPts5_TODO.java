package math.func.intrg;
import math.func.FuncVec;
import math.func.deriv.DerivPts5;
import math.mtrx.Mtrx;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 3/03/12, 12:18 PM
 */
public class IntgPts5_TODO extends DerivPts5 {
public static Log log = Log.getLog(IntgPts5_TODO.class);
protected static double arrIntg[][];
public IntgPts5_TODO(FuncVec fv) {
  super(fv, false);        log.setDbg();
  calc(fv);               log.dbg("f'(x)=\n", this);
}
private void calc(final FuncVec fv) {
  if (!(fv.getX() instanceof StepGrid)) {
    throw new IllegalArgumentException("DerivPts5 can only work with StepGrid");
  }
  if (fv.size() < 5) {
    throw new IllegalArgumentException("DerivPts5 needs at least 5 grid points");
  }
  if (arrIntg == null) {
//    Mtrx m45 = new Mtrx(arr4x5);       log.info("arr4x4=\n", m45);
//    arrIntg = m45.inverse().getArray();   log.info("arrIntg=\n", new Mtrx(arrIntg));
//    Mtrx m54 = new Mtrx(arr5x4);       log.info("arr4x4=\n", m54);
//    Mtrx inv45 = m54.inverse();

//    Mtrx mTest = new Mtrx(m54.times(inv45).getArray());    log.info("mTest=\n", mTest);
//    Mtrx mTes2 = new Mtrx(inv45.times(m54).getArray());    log.info("mTes2=\n", mTes2);

//    arrIntg = inv45.getArray();   log.info("arrIntg=\n", new Mtrx(arrIntg));

    Mtrx m = new Mtrx(arr4x4);       log.info("arr4x4=\n", m);
////    double det = m.det();
    arrIntg = m.inverse().getArray();   log.info("arrIntg=\n", new Mtrx(arrIntg));
  }
  StepGrid grid = (StepGrid) fv.getX();
  double h2 = 24. * grid.getGridStep();
  calcIntgPts(h2, arrIntg, fv.getArr());
}
protected void calcIntgPts_Pts5(double norm, final double[][] a, final double[] f) {
  double runnTot = 0; // running total
  int baseIdx = 0; // running index
  set(0, 0);
  for (int i = 1; i < f.length; i++) {
    int type = (i - baseIdx) % 5;
    double g = calcPts5(type - 1, baseIdx, a, f);
    g *= norm;
    set(i, runnTot + g);
    if (type == 4) {
      runnTot += g;
      baseIdx += 4;
    }
  }
}
protected void calcIntgPts(double norm, final double[][] a, final double[] f) {
  double runnTot = 0; // running total
  int baseIdx = 0; // running index
  set(0, 0);
  for (int i = 1; i < f.length; i++) {
    int type = (i - baseIdx) % 5;
    double g = calcPts4(type-1, baseIdx + 1, a, f);
    g *= norm;
    set(i, runnTot + g);
    if (type == 4) {
      runnTot += g;
      baseIdx += 4;
    }
  }
}
protected double calcPts4(int rIdx, int fIdx, final double[][] a, final double[] f) {
  int col = 0;
  double res = 0;
  res += a[rIdx][col++] * f[fIdx++];  // 1
  res += a[rIdx][col++] * f[fIdx++];  // 2
  res += a[rIdx][col++] * f[fIdx++];  // 3
  res += a[rIdx][col++] * f[fIdx++];  // 4
  return res;
}
}
