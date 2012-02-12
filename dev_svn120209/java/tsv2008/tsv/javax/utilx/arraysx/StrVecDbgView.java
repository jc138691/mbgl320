package javax.utilx.arraysx;

import flanagan.complex.Cmplx;
import math.complex.CmplxVec;
import math.vec.DbgView;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,06/08/2010,12:09:20 PM
 */
public class StrVecDbgView extends StrVec {
  public StrVecDbgView(StrVec from) {
    super(from);
  }
  public String toString() {
    return toDbgString();
  }
}