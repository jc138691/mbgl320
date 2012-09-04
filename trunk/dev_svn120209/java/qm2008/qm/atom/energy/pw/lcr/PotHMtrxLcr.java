package atom.energy.pw.lcr;
import atom.energy.pw.PotH;
import atom.energy.pw.PotHMtrx;
import atom.wf.WFQuadrD1;
import math.func.FuncVec;
import math.func.arr.IFuncArr;
import scatt.jm_2008.jm.laguerre.IWFuncArr;

/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 11:43:31
*/
public class PotHMtrxLcr extends PotHMtrx {
public PotHMtrxLcr(int L, IWFuncArr basis, FuncVec pot) {
  super(L, basis, pot);
  setQuadr(basis.getQuadr());
  calc();
}

public PotHMtrxLcr(int L, IFuncArr basis, FuncVec pot, WFQuadrD1 quadr) {
  super(L, basis, pot);
  setQuadr(quadr);
  calc();
}

public PotH makePotH() {
  return new PotHLcr(getQuadr());
}
}