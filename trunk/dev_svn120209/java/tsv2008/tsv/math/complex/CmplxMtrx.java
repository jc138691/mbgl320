package math.complex;

import flanagan.complex.Cmplx;
import flanagan.complex.ComplexMatrix;
import math.mtrx.TMtrx;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 26/02/2010, 11:41:18 AM
 */
public class CmplxMtrx extends TMtrx<Cmplx> {
  public CmplxMtrx(int nr, int nc) {
    super(new Cmplx[nr][nc]);
  }
  public CmplxMtrx(Cmplx[][] A) {
    super(A);
  }
  public CmplxMtrx inverse() {
    ComplexMatrix cm = new ComplexMatrix(getArr());
    cm = cm.inverse();
    return new CmplxMtrx(cm.getArrayPointer());
  }
  public CmplxMtrx times(CmplxMtrx cm2) {
    ComplexMatrix cm = new ComplexMatrix(getArr());
    cm = cm.times(cm2.getArr());
    return new CmplxMtrx(cm.getArrayPointer());
  }
  public void timesEquals(double d) {
    ComplexMatrix cm = new ComplexMatrix(getArr());
    cm.timesEquals(d);
    setArr(cm.getArrayPointer());
  }
  public String toString(Cmplx[] row) {
    return new CmplxVec(row).toString();
  }

  public CmplxMtrx transpose() {
    ComplexMatrix cm = new ComplexMatrix(getArr());
    ComplexMatrix cmt = cm.transpose();
    return new CmplxMtrx(cmt.getArrayPointer());
  }

}
