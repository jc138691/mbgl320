package atom.energy.part_wave;
import math.func.FuncVec;
import scatt.jm_2008.jm.laguerre.IWFuncArr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 11:58:55
 */
public class PartHMtrxR extends PartHMtrx {
  protected IWFuncArr basis;
  public PartHMtrxR(int L, IWFuncArr basis, FuncVec pot) {
    super(L, basis, pot);
    this.basis = basis;
    calc();
  }
  public PartH makePartH() {
    return new PartHR(basis.getQuadr());
  }
}
