package math.complex;

import flanagan.complex.Cmplx;
import math.mtrx.TMtrx;
import math.vec.TVec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 05/03/2010, 11:48:14 AM
 */
public class CmplxVec extends TVec<Cmplx> {
  public CmplxVec(Cmplx[] from) {
    super(from);
  }
  public CmplxVec(CmplxVec from) {
    super(from.getArr());
  }
}
