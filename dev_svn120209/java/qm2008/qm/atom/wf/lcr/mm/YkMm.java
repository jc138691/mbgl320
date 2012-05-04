package atom.wf.lcr.mm;
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.lcr.YkLcr;
import math.func.FuncVec;
import math.vec.Vec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 3/05/12, 9:37 AM
 */
// YkMm(r)= Yk - Zk
public class YkMm extends YkLcr {
public YkMm(final WFQuadrLcr quadrLcr, final Vec f, final Vec f2, final int K) {
  super(quadrLcr, f, f2, K);
}
public FuncVec calcYk() {
  FuncVec yk = calcZk();
  return yk;
}
}
