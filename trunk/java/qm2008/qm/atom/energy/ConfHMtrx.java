package atom.energy;
import atom.AtomFano1965;
import atom.shell.ConfArr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.mtrx.jamax.Eigen;
import math.vec.Vec;
import math.vec.VecSort;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:24:45
 */
public class ConfHMtrx extends HMtrx {
  public static Log log = Log.getLog(ConfHMtrx.class);
  private final AtomFano1965 atom;
  private final ConfArr basis;
  private FuncArr density;
  public ConfHMtrx(ConfArr basis, final AtomFano1965 atom) {
    super(basis.size(), basis.size());
    this.atom = atom;
    this.basis = basis;
    load();
  }

  public ConfArr getBasis() {
    return basis;
  }
  public AtomFano1965 getAtom() {
    return atom;
  }
  private void load() {
    for (int r = 0; r < basis.size(); r++) {
      if ((10*basis.size())%(r+1) == 0) {
        log.dbg("ConfHMtrx row=", r); log.dbg("ConfHMtrx rows%=", 100.* r / basis.size());
      }
      for (int c = r; c < basis.size(); c++) { // NOTE c=r
        // Atomic system should know how to calculate itself
        Energy res = atom.calcH(basis.get(r), basis.get(c));
        set(r, c, res.kin + res.pot);
        set(c, r, res.kin + res.pot);
      }
    }
  }
//  public Energy calcH(Eigen eig, int col) {
//    Mtrx m = eig.getV();
//    Energy res = new Energy();
//    for (int r = 0; r < m.getNumRows(); r++) {
//      double val = m.get(r, col);  // BY ROW is correct!   see HydrogenJUnit.test_2s
//      for (int r2 = 0; r2 < m.getNumRows(); r2++) {
//        double val2 = m.get(r2, col);  // BY ROW is correct!   see HydrogenJUnit.test_2s
//        Energy configE = atom.calcH(jmBasisN.get(r), jmBasisN.get(r2));
//        res.kin += (configE.kin * val * val2);
//        res.pot += (configE.pot * val * val2);
//      }
//    }
//    return res;
//  }
//  public FuncVec calcDensity(Eigen eig, int col) {
//    Mtrx m = eig.getV();
//    FuncVec res = null;
//    for (int r = 0; r < m.getNumRows(); r++) {
//      double val = m.get(r, col);  // BY ROW is correct!   see HydrogenJUnit.test_2s
//      for (int r2 = 0; r2 < m.getNumRows(); r2++) {
//        double val2 = m.get(r2, col);  // BY ROW is correct!   see HydrogenJUnit.test_2s
//        FuncVec conf = atom.calcConfigDensity(jmBasisN.get(r), jmBasisN.get(r2));
//        if (conf == null)
//          continue;
//        conf.multSelf(val * val2);
//        if (res == null) {
//          res = new FuncVec(conf);
//        } else
//          res.addSafe(conf);
//      }
//    }
//    return res;
//  }

  public FuncArr getDensity(int maxNum) {
    if (density == null) {
      density = calcDensity(maxNum);
    }
    return density;
  }
  private FuncArr calcDensity(int maxNum) {
    if (basis == null  ||  basis.size() == 0  ||  maxNum == 0) {
      return null;
    }
    int size = basis.size();
    if (maxNum > 0) {
      size = Math.min(maxNum, basis.size());
    }
    Vec x = basis.getX();
    FuncArr res = new FuncArr(x, size);
    FuncVec[][] confArr = new FuncVec[basis.size()][basis.size()];
    boolean[][] doneArr = new boolean[basis.size()][basis.size()];

    Mtrx v = eig().getV();
    double[][] C = v.getArray();
    double norm = 1. / atom.getNumElec();
    for (int r = 0; r < size; r++) {
      if ((10*basis.size())%(r+1) == 0) {
        log.dbg("calcDensity row=", r); log.dbg("rows%=", 100.* r / basis.size());
      }
      FuncVec f_i = new FuncVec(x);
      for (int j = 0; j < basis.size(); j++) {
        double cij = C[j][r];  // [j][i] is correct, see  PartHMtrx;  // BY ROW is correct!   see HydrogenJUnit.test_2s
        for (int j2 = 0; j2 < basis.size(); j2++) {
          FuncVec conf = confArr[j][j2];
          if (conf == null  &&  !doneArr[j][j2]) {
            conf = atom.calcDensity(basis.get(j), basis.get(j2));
            confArr[j][j2] = conf;
            doneArr[j][j2] = true;  // this is to stop re-calculating null contributions
            confArr[j2][j] = conf;
            doneArr[j2][j] = true;
          }
          if (conf == null)
            continue;
          double c2 = C[j2][r];  // [j][i] is correct, see  PartHMtrx
          f_i.addMultSafe(cij * c2, conf);
        }
      }
      f_i.mult(norm);
      res.set(r, f_i);
    }
    return res;
  }
//  private FuncArr calcDensity() { // THIS IS WRONG!!!!!!!!!
//    if (basis == null  ||  basis.size() == 0) {
//      return null;
//    }
//    Vec x = basis.getX();
//    FuncArr res = new FuncArr(x, basis.size());
//
//    Mtrx v = eig().getV();
//    double[][] C = v.getArray();
//
//    double norm = 1. / atom.getNumElec();
//    for (int i = 0; i < basis.size(); i++) {
//      FuncVec f_i = new FuncVec(x);
//      for (int j = 0; j < basis.size(); j++) {
//        double cij = C[j][i];  // [j][i] is correct, see  PartHMtrx
////        double cij = C[i][j];  // [j][i] is correct, see  PartHMtrx
//        f_i.addMultSafe(cij * cij, basis.get(j).getDensity());
//      }
//      f_i.multSelf(norm);
//      res.set(i, f_i);
//    }
//    return res;
//  }
  public String toStringSorted(Eigen eig, int col) {
    StringBuffer buff = new StringBuffer();
    buff.append("e[" + col + "]=" + (float) (eig.getRealEVals()[col]));
    Mtrx m = eig.getV();
    double[] v = m.getColCopy(col);
    int[] idx = VecSort.sortIdxByAbs(v, false);
    for (int r = 0; r < m.getNumRows(); r++) {
      int sortedIdx = idx[r];
      buff.append("\n\tv[" + sortedIdx + "]=\t");
      buff.append((float) m.get(sortedIdx, col)).append(" * ");
      buff.append(basis.get(sortedIdx).toString());
    }
    return buff.toString();
  }
  public String toString(Eigen eig, int col) {
    StringBuffer buff = new StringBuffer();
    buff.append("e[" + col + "]=" + (float) (eig.getRealEVals()[col]));
    Mtrx m = eig.getV();
    double norm = 0;
    for (int r = 0; r < m.getNumRows(); r++) {
      buff.append("\n\tv[" + r + "]=\t");
      double val = m.get(r, col);  // BY ROW is correct!   see HydrogenJUnit.test_2s
//         double val = m.getLine(col, r);  // this is WRONG!!!
      norm += val * val;
      buff.append((float) val).append(" * ");
      buff.append(basis.get(r).toString());
    }
    buff.append("\n\tnorm=" + norm);
    return buff.toString();
  }
}
