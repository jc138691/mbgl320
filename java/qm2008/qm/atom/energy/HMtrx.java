package atom.energy;

import atom.data.AtomUnits;
import math.mtrx.Mtrx;
import math.mtrx.jamax.Eigen;
import math.vec.Vec;

import java.lang.reflect.InvocationTargetException;
/**
 * Created by Dmitry.A.Konovalov@gmail.com, 16/02/2010, 2:16:07 PM
 */
public class HMtrx extends Mtrx {
  private Eigen eig;
  public HMtrx(int m, int n) {
    super(m, n);
  }
  public Eigen eig () {
    if (eig == null) {
      eig = new Eigen(this, true); // NOTE true for isSymm
    }
    return eig;
  }
  public Vec getEigVal() {
    Eigen thisEig = eig();
    double[] res = thisEig.getRealEVals();
    return new Vec(res);
  }
  public Vec getEngEv(int fromIdx) {
    double[] engs = getEigVal().getArr();
    Vec res = new Vec(engs.length);
    for (int i = 0; i < res.size(); i++) {
      double e = AtomUnits.toEV(engs[i] - engs[fromIdx]);
      res.set(i, e);
    }
    return res;
  }
  public Mtrx getEigVec() {
    Eigen thisEig = eig();
    return thisEig.getV();
  }
  public double[][] getEigArr() {
    return getEigVec().getArray();
  }
}
