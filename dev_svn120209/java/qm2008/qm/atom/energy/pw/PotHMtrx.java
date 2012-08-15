package atom.energy.pw;
import atom.energy.HMtrx;
import atom.wf.WFQuadrD1;
import math.mtrx.api.Mtrx;
import math.mtrx.api.EigenSymm;
import math.vec.Vec;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.IFuncArr;

import javax.utilx.log.Log;

/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 11:42:11
*/
public abstract class PotHMtrx extends HMtrx {
public static Log log = Log.getLog(PotHMtrx.class);
private final int L;
private final IFuncArr basis;
private FuncArr eigVec;
private FuncVec pot;
private WFQuadrD1 quadr;

public PotHMtrx(int L, IFuncArr basis, FuncVec pot) {
  super(basis.size(), basis.size());
  this.L = L;
  this.basis = basis;
  this.pot = pot;
  // REMEMBER!!! call "calc()"
}
public abstract PotH makePotH();
public int getL() {
  return L;
}
public void calc() {
  PotH H = makePotH();
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

public FuncArr getEigWfs() {
  if (eigVec == null) {
    loadEigVec();
  }
  return eigVec;
}
public void loadEigVec() {
  Vec x = basis.getX();
  eigVec = new FuncArr(x, basis.size());

  // f_i = SUM_j C_ji * lgrrN(j)
  EigenSymm thisEig = eigSymm();
  Mtrx C = thisEig.getV();
//  double[][] C = v.getArr2D();
  for (int i = 0; i < basis.size(); i++) {
    FuncVec f_i = new FuncVec(x);
    for (int j = 0; j < basis.size(); j++) {
//        f_i.addMultSafe(C[i][j], lgrrN.getFunc(j));  // this is WRONG!!!
//      f_i.addMultSafe(C[j][i], basis.getFunc(j));   // this is correct!!!
      f_i.addMultSafe(C.get(j, i), basis.getFunc(j));   // this is correct!!!
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
public IFuncArr getBasis() {
  return basis;
}
public FuncVec getPot() {
  return pot;
}
public WFQuadrD1 getQuadr() {
  return quadr;
}
public void setQuadr(WFQuadrD1 quadr) {
  this.quadr = quadr;
}
}
