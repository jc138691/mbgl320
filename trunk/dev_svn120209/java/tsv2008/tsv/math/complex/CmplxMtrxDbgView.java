package math.complex;

import flanagan.complex.Cmplx;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 05/03/2010, 11:46:44 AM
 */
public class CmplxMtrxDbgView extends CmplxMtrx {
  public CmplxMtrxDbgView(CmplxMtrx from) {
    super(from.getArr());
  }

  public String toString(Cmplx[] row) {
    return new CmplxVecDbgView(new CmplxVec(row)).toString();
  }

  public String toString() {
    return toDbgString();
  }

}