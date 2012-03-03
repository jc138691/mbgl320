package math.func.intrg;
import math.func.FuncVec;
import math.func.deriv.DerivPts5;
import math.mtrx.Mtrx;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 3/03/12, 12:18 PM
 */
public class IntgPts5 extends DerivPts5 {
public static Log log = Log.getLog(IntgPts5.class);
protected static double arrIntg[][];
public IntgPts5(FuncVec fv) {
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
    Mtrx m = new Mtrx(arr4x4);       log.info("arr4x4=\n", m);
    double det = m.det();
    arrIntg = m.inverse().getArray();   log.info("arrIntg=\n", new Mtrx(arrIntg));
  }
  StepGrid grid = (StepGrid) fv.getX();
  double h2 = 24. * grid.getGridStep();
  calcPts(h2, arrIntg, fv.getArr());
}
}
