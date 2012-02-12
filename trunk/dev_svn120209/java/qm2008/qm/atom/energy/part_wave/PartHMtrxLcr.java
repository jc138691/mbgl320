package atom.energy.part_wave;
import atom.wf.WFQuadr;
import math.func.FuncVec;
import math.func.arr.IFuncArr;
import scatt.jm_2008.jm.laguerre.IWFuncArr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 11:43:31
 */
public class PartHMtrxLcr extends PartHMtrx {
  private WFQuadr quadr;
  public PartHMtrxLcr(int L, IWFuncArr basis, FuncVec pot) {
    super(L, basis, pot);
    this.quadr = basis.getQuadr();
    calc();
  }

  public PartHMtrxLcr(int L, IFuncArr basis, FuncVec pot, WFQuadr quadr) {
    super(L, basis, pot);
    this.quadr = quadr;
    calc();
  }

  public PartH makePartH() {
    return new PartHLcr(quadr);
  }
}