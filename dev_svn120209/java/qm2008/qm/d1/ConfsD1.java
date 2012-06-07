package d1;
import atom.shell.IConfArr;
import scatt.jm_2008.jm.laguerre.IWFuncArr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 11:41 AM
 */
public class ConfsD1 extends IConfArr {
private IWFuncArr basis;
public ConfsD1(IWFuncArr basis) {
  this.basis = basis;
}
public IWFuncArr getBasis() {
  return basis;
}
}
