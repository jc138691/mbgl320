package atom.energy;
import math.mtrx.Mtrx;
import math.mtrx.jamax.EigenSymm;
import math.vec.Vec;
import math.integral.Quadr;
import math.func.FuncVec;
import atom.energy.slater.Slater;
import atom.wf.WFArrL;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 13:52:01
 */
public class HMtrxL extends Mtrx {
  public static Log log = Log.getLog(HMtrxL.class);
  private final Slater slater;
  private final WFArrL basis;
  private WFArrL eigVec;
  private EigenSymm eig;
  private Vec potFunc;
  public HMtrxL(WFArrL basis, final Slater slater, Vec potFunc) {
    super(basis.size(), basis.size());
    this.slater = slater;
    this.basis = basis;
    this.potFunc = potFunc;
    load();
  }
  public WFArrL getBasis() {
    return basis;
  }
  public Vec getPotFunc() {
    return potFunc;
  }
  public Quadr getQuadr() {
    return slater;
  }
  public int getL() {
    return basis.getL();
  }
  public EigenSymm eig() {
    if (eig == null) {
      eig = new EigenSymm(this); // NOTE true for isSymm
    }
    return eig;
  }
  private void load() {
    for (int r = 0; r < basis.size(); r++) {  log.dbg("row=", r);
      for (int c = r; c < basis.size(); c++) { log.dbg("col=", c);
        double kinE = slater.calcKin(basis.getL(), basis.getFunc(r), basis.getFunc(c));  log.dbg("kinE=", kinE);
        double potE = slater.calcPot(potFunc, basis.get(r), basis.get(c));  log.dbg("potE=", potE);
        double E = kinE + potE;             log.dbg("E=", E);
        set(r, c, E);
        set(c, r, E);
      }
    }
  }
  public Vec getX() {
    return basis.getX();
  }
  public Vec getEigVal() {
    EigenSymm thisEig = eig();
    double[] res = thisEig.getRealEVals();
    return new Vec(res);
  }
  public WFArrL getEigVec() {
    if (eigVec == null) {
      loadEigVec();
    }
    return eigVec;
  }
  public void loadEigVec() {
    eigVec = new WFArrL(basis.getL(), basis.getNormQuadr());
    Vec x = getX();

    // f_i = SUM_j C_ij * jmBasisN(j)
    EigenSymm thisEig = eig();
    Mtrx v = thisEig.getV();
    double[][] C = v.getArray();
    for (int i = 0; i < basis.size(); i++) {
      FuncVec f_i = new FuncVec(x);
      for (int j = 0; j < basis.size(); j++) {
//        f_i.addMultSafe(C[i][j], jmBasisN.get(j));  // this is WRONG!!!
        f_i.addMultSafe(C[j][i], basis.get(j));   // this is correct!!!
      }
      eigVec.add(f_i);
    }
  }


}
