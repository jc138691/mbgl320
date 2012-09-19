package atom.wf.dcr;
import atom.wf.lcr.*;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 17/09/12, 9:08 AM
 */
public class FuncDcrToR extends FuncDcr {
// TODO: Not as good as LCR!!!!!!!!!!!!!!!!!!!
// f(x) = r,
// x = - 1/CR, CR=c+r; c > 0
// CR = -1/x; r = -[1/x + c]=-[1+cx]/x;
public FuncDcrToR(final double c) {
  super(c);
}
public double calc(double x) {
  double cx = c * x;
  return -(1. + cx)/x;
}
}
