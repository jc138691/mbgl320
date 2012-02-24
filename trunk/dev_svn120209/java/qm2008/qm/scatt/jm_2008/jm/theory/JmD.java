package scatt.jm_2008.jm.theory;

import atom.wf.WFQuadr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.vec.Vec;
import scatt.jm_2008.jm.laguerre.IWFuncArr;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 11/02/2010, 1:28:10 PM
 */
public class JmD extends Vec {
  // chi_n = Sum_m D_{nm} * phi_m(r)
  // D_{nm} = < bi_m | chi_n >
  // D_{n, N-1} = < bi_{N-1} | chi_n > 
  public JmD(IWFuncArr bi, FuncArr basisArr) {
    super(basisArr.size());
    WFQuadr w = bi.getQuadr();
    FuncVec lastBi = bi.getFunc(bi.size() - 1);
    for (int i = 0; i < size(); i++) {
      double overlap = w.calcInt(lastBi, basisArr.get(i));
      set(i, overlap);
    }
  }
}
