package atom.energy.part_wave;
import math.func.FuncVec;
import scatt.jm_2008.jm.laguerre.IWFuncArr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 11:58:55
 */
public class PotHMtrxR extends PotHMtrx {
//  protected IWFuncArr basis;
  public PotHMtrxR(int L, IWFuncArr basis, FuncVec pot) {
    super(L, basis, pot);
//    this.basis = basis;
    setQuadr(basis.getQuadr());
    calc();
  }
  public PotH makePotH() {
    return new PotHR(getQuadr());
  }
}
