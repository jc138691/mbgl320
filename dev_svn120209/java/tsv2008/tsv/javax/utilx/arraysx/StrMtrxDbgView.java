package javax.utilx.arraysx;

import flanagan.complex.Cmplx;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,06/08/2010,12:08:03 PM
 */
public class StrMtrxDbgView extends StrMtrx {
  public StrMtrxDbgView(String[][] from) {
    super(from);
  }
  public StrMtrxDbgView(StrMtrx from) {
    super(from.getArr());
  }

  public String toString(String[] row) {
    return new StrVecDbgView(new StrVec(row)).toString();
  }

  public String toString() {
    return toDbgString();
  }
}