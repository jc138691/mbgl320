package atom.energy.part_wave;
import atom.energy.HMtrx;
import math.mtrx.Mtrx;
import math.mtrx.jamax.Eigen;
import math.vec.Vec;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.IFuncArr;

import javax.utilx.log.Log;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 11:42:11
 */
public abstract class PartHMtrx extends HMtrx {
  public static Log log = Log.getLog(PartHMtrx.class);
  private final int L;
  private final IFuncArr basis;
  private FuncArr eigVec;
  private FuncVec pot;
  public PartHMtrx(int L, IFuncArr basis, FuncVec pot) {
    super(basis.size(), basis.size());
    this.L = L;
    this.basis = basis;
    this.pot = pot;
    // REMEMBER!!! call "calc()"
  }
  public abstract PartH makePartH();
  public int getL() {
    return L;
  }
  public void calc() {
    PartH H = makePartH();
    for (int r = 0; r < basis.size(); r++) {      log.dbg("row=", r);
      FuncVec fr = basis.getFunc(r);
      for (int c = r; c < basis.size(); c++) {    log.dbg("col=", c);
        FuncVec fc = basis.getFunc(c);
        double kinE = H.calcKin(L, fr, fc);    log.dbg("kinE=", kinE);
        double potE = H.calcPot(pot, fr, fc);  log.dbg("potE=", potE);
        double E = kinE + potE;             log.dbg("E=", E);
        set(r, c, E);
        set(c, r, E);
      }
    }
  }

  public FuncArr getEigFuncArr() {
    if (eigVec == null) {
      loadEigVec();
    }
    return eigVec;
  }
  public void loadEigVec() {
    Vec x = basis.getX();
    eigVec = new FuncArr(x, basis.size());

    // f_i = SUM_j C_ij * jmBasisN(j)
    Eigen thisEig = eig();
    Mtrx v = thisEig.getV();
    double[][] C = v.getArray();
    for (int i = 0; i < basis.size(); i++) {
      FuncVec f_i = new FuncVec(x);
      for (int j = 0; j < basis.size(); j++) {
//        f_i.addMultSafe(C[i][j], jmBasisN.getFunc(j));  // this is WRONG!!!
        f_i.addMultSafe(C[j][i], basis.getFunc(j));   // this is correct!!!
      }
      eigVec.set(i, f_i);
    }

    // fix sign
    for (int i = 0; i < basis.size(); i++) {
      FuncVec f_i = eigVec.get(i);
      if (f_i.get(0) < 0  || f_i.get(1) < 0) { // check first and second
        f_i.mult(-1);
      }
    }
  }


}
