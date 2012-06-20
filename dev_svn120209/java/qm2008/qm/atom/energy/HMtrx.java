package atom.energy;

import atom.data.AtomUnits;
import math.mtrx.api.Mtrx;
import math.mtrx.api.EigenSymm;
import math.vec.Vec;
/**
 * Created by Dmitry.A.Konovalov@gmail.com, 16/02/2010, 2:16:07 PM
 */
public class HMtrx extends Mtrx {
private EigenSymm eig;
public HMtrx(int m, int n) {
  super(m, n);
}
//public HMtrx(Mtrx mh) {
//  super(mh.getArr2D());
//}
public HMtrx(Mtrx mh) {
  super(mh);
}
public EigenSymm eigSymm() {
  return eig(false);
}
private EigenSymm eig(boolean overwrite) {
  if (eig == null) {
    eig = new EigenSymm(this, overwrite); // NOTE true for isSymm
  }
  return eig;
}
final public Vec getEigEngs() {
  return getEigVal(false);
}
public Vec getEigVal(boolean overwrite) {
  EigenSymm thisEig = eig(overwrite);
  double[] res = thisEig.getRealEVals();
  return new Vec(res);
}
public Vec getEngEv(int fromIdx) {
  double[] engs = getEigEngs().getArr();
  Vec res = new Vec(engs.length);
  for (int i = 0; i < res.size(); i++) {
    double e = AtomUnits.toEV(engs[i] - engs[fromIdx]);
    res.set(i, e);
  }
  return res;
}
final public Mtrx getEigVec() {
  return getEigVec(false);
}
public Mtrx getEigVec(boolean overwrite) {
  EigenSymm thisEig = eig(overwrite);
  return thisEig.getV();
}
public double[][] getEigArr() {
  return getEigVec().getArr2D();
}
}
