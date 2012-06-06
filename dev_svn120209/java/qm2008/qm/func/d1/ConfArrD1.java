package func.d1;
import math.vec.Vec;
import scatt.jm_2008.jm.laguerre.IWFuncArr;

import javax.utilx.arraysx.TArr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 11:41 AM
 */
public class ConfArrD1 extends TArr<ConfD1> {
private IWFuncArr basis;
public ConfArrD1(IWFuncArr basis) {
  this.basis = basis;
}
public IWFuncArr getBasis() {
  return basis;
}
}
