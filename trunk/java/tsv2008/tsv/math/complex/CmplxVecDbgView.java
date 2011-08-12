package math.complex;

import flanagan.complex.Cmplx;
import math.vec.DbgView;
import math.vec.Vec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 05/03/2010, 12:02:08 PM
 */
public class CmplxVecDbgView extends CmplxVec {
  public CmplxVecDbgView(CmplxVec from) {
    super(from);
  }
  public String toString() {
    return toDbgString();
  }
}
