package atom.energy;
import math.mtrx.api.Mtrx;
import math.mtrx.MtrxDbgView;
import math.mtrx.MtrxFactory;
import math.mtrx.api.EigenSymm;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 15/05/12, 12:22 PM
 */
public class HOvMtrx extends HMtrx {
public static Log log = Log.getLog(HOvMtrx.class);
private HMtrx corrH; // H - corrected for overlap
private Mtrx ov;  // overlap matrix
// locals
private EigenSymm ovEig;
private Mtrx ovV;
private Mtrx ovC;

public HOvMtrx(Mtrx mh) {
  super(mh);
}
public HOvMtrx(int m, int n) {
  super(m, n);
}
public Mtrx getOv() {
  return ov;
}
public void setOv(Mtrx ov) {
  this.ov = ov;
}
private void calc() {     log.setDbg();
  log.dbg("\n \n ovH=\n", new MtrxDbgView(this));
  log.dbg("\n \n ov=\n", new MtrxDbgView(ov));
// S=mOv - overlap matrix
// S_ij = <b_i|b_j>, after diag S = V * D * V',  C = V * 1/sqrt(D)
// New basis becomes:  B_i = \sum_j C_ji b_j  [in columns!!!]
//
// New H becomes H_ij = <B_i|H|B_j> = [C' h C]_ij
// H_ij = \sum_i' \sum_j' C_i'i h_i'j' C_j'j
// where h_ij = <b_i|H|b_j>
  ovEig = new EigenSymm(ov, false);
  ovV = ovEig.getV(); log.dbg("ovEig.getVec()=\n", new MtrxDbgView(ovV));
  Mtrx D = ovEig.getD(); log.dbg("ovEig.getD()=\n", new MtrxDbgView(D));
  MtrxFactory.makeDiagOneSqrt(D); log.dbg("makeDiagOneSqrt(D)=\n", new MtrxDbgView(D));
  ovC = ovV.mult(D); log.dbg("ovC = ovV.mult(D)=\n", new MtrxDbgView(ovC));
  D = null;
  corrH = new HMtrx(ovC.transpose().mult(this.mult(ovC)));
}

public Vec getEigVal(boolean overwrite) {
  if (corrH == null)
    calc();
  return corrH.getEigVal(overwrite);
}
public Vec getEngEv(int fromIdx) {
  return null;
//  double[] engs = getEigEngs().getArr();
//  Vec res = new Vec(engs.length);
//  for (int i = 0; i < res.size(); i++) {
//    double e = AtomUnits.toEV(engs[i] - engs[fromIdx]);
//    res.set(i, e);
//  }
//  return res;
}
public Mtrx getEigVec(boolean overwrite) {
  if (corrH == null)
    calc();
  return corrH.getEigVec(overwrite);
}
public double[][] getEigArr() {
  return null;
//  return getEigVec().getArray();
}


//public void calc() {  log.setDbg();
//
//  EigenSymm ovEig = new EigenSymm(mOv, false);
//  Mtrx V = ovEig.getV();
//  log.dbg("ovEig.getVec()=\n", new MtrxDbgView(V));
//
//  Mtrx D = ovEig.getD();
//  log.dbg("ovEig.getD()=\n", new MtrxDbgView(D));
//
//  Mtrx test = V.mult(D).mult(V.transpose());
//  log.dbg("test=\n", new MtrxDbgView(test));
//
//  MtrxFactory.makeDiagOneSqrt(D);
//  log.dbg("makeDiagOneSqrt(D)=\n", new MtrxDbgView(D));
//
//  Mtrx C = V.mult(D);
//  log.dbg("C = V.mult(D)=\n", new MtrxDbgView(C));
//
//  Mtrx newH = C.transpose().mult(this.mult(C));
//  EigenSymm newEig = new EigenSymm(newH, false);
//  double[] newEngs = newEig.getRealEVals();
//  log.dbg("newEngs=\n", new VecDbgView(newEngs));
//
//  // WF   F_i = \sum_j V_ji B_j  = \sum_jj' V_ji C_j'j b_j'
//  test = C.mult(newEig.getV());
//  log.dbg("test=\n", new MtrxDbgView(test));
//}

}
