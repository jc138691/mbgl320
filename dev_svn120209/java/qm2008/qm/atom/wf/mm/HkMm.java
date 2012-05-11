package atom.wf.mm;
import atom.wf.lcr.WFQuadrLcr;
import math.Mathx;
import math.func.FuncVec;
import math.func.intrg.IntgPts7;
import math.vec.Vec;
import math.vec.VecDbgView;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 10/05/12, 2:39 PM
 */
public class HkMm {
public static Log log = Log.getLog(HkMm.class);
// INPUT
private final WFQuadrLcr quadr;
private final Vec a;
private final Vec b;
private final Vec a2;
private final Vec b2;
private final int K;
// derived from INPUT
private final Vec vcr2;
private final Vec vx;
//
private FuncVec zFk;  // z-function inside int_0^r
private FuncVec zF0;  // z-function inside int_0^r
private FuncVec z0;  // result of int_0^r
private FuncVec rK;    // 1/r^k
private FuncVec yF;  // y-function inside int_0^r
private FuncVec rK1;    // r^(k+1)

public HkMm(final WFQuadrLcr quadr
            , Vec a, Vec b, Vec a2, Vec b2, int K) {
  this.quadr = quadr;
  this.K = K;
  this.a = a;
  this.b = b;
  this.a2 = a2;
  this.b2 = b2;

  vcr2 = quadr.getLcrToR().getCR2();
  vx = quadr.getX();
}
public double calcNorm() {
  if (z0 == null)
    z0 = calcZ0(a, a2);
  double res = 2. * quadr.calcInt(b, b2, z0);  // NOTE!!!! *2.
  return res;
}

public FuncVec calcZ0(Vec vf, Vec vf2) {   log.setDbg();
  if (zF0 == null) {
    Vec v = vf.copy();
    v.multSelf(vcr2, vf2);
    zF0 = new FuncVec(vx, v);
  }
  FuncVec res = new IntgPts7(zF0);    log.info("IntgPts7(zF)=", new VecDbgView(res));
  return res;
}
//private void loadZFuncs(Vec vf, Vec vf2) {
//  double[] f = vf.getArr();
//  double[] f2 = vf2.getArr();
//  double[] CR2 = quadr.getLcrToR().getCR2();
//  Vec vr = quadr.getR();
//  zF0 = new FuncVec(vr);
//  double[] z = zF.getArr();
//  rK = new FuncVec(rVec);
//  double[] rk = rK.getArr();
//  z[0] = 0;
//  rk[0] = 0;
//  for (int i = 1; i < z.length; i++) {
//    z[i] = CR2[i] * f[i] * f2[i];
//    double rki = Mathx.pow(r[i], K);
//    z[i] *= rki;
//    rk[i] = 1. / rki;
//  }
//  log.info("zF=", new VecDbgView(zF));
//  log.info("rK=", new VecDbgView(rK));
//}
}
