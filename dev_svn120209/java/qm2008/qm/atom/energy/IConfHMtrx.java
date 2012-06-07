package atom.energy;
import atom.shell.IConfs;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.mtrx.jamax.EigenSymm;
import math.vec.Vec;
import math.vec.VecSort;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 7/06/12, 11:40 AM
 */
public class IConfHMtrx  extends HMtrx {
public static Log log = Log.getLog(LsConfHMtrx.class);
private final ISysH sysH;
private final IConfs confs;
private FuncArr density;
public IConfHMtrx(IConfs basis, final ISysH atom) {
  super(basis.size(), basis.size());
  this.sysH = atom;
  this.confs = basis;
  load();
}

public IConfs getConfs() {
  return confs;
}
public ISysH getSysH() {
  return sysH;
}
private void load() {
  for (int r = 0; r < confs.size(); r++) {
    if ((10* confs.size())%(r+1) == 0) {
      log.dbg("LsConfHMtrx row=" + r + ", " + (int)(100.* r / confs.size()) + "%");
    }
    for (int c = r; c < confs.size(); c++) { // NOTE c=r
      // Atomic system should know how to calculate itself
      Energy res = sysH.calcH(confs.get(r), confs.get(c));
      set(r, c, res.kin + res.pt);
      set(c, r, res.kin + res.pt);
    }
  }
}

public FuncArr getDensity(int maxNum) {
  if (density == null) {
    density = calcDensity(maxNum);
  }
  return density;
}
private FuncArr calcDensity(int maxNum) {
  if (confs == null  ||  confs.size() == 0  ||  maxNum == 0) {
    return null;
  }
  int size = confs.size();
  if (maxNum > 0) {
    size = Math.min(maxNum, confs.size());
  }

  Vec x = confs.getX();
  FuncArr res = new FuncArr(x, size);
  FuncVec[][] confArr = new FuncVec[this.confs.size()][this.confs.size()];
  boolean[][] doneArr = new boolean[this.confs.size()][this.confs.size()];

  Mtrx v = eig().getV();
  double[][] C = v.getArray();
  double norm = 1. / sysH.getNumElec();
  for (int r = 0; r < size; r++) {
    if ((10* this.confs.size())%(r+1) == 0) {
      log.dbg("calcDens row=", r); log.dbg("rows%=", 100.* r / this.confs.size());
    }
    FuncVec f_i = new FuncVec(x);
    for (int j = 0; j < this.confs.size(); j++) {
      double cij = C[j][r];  // [j][i] is correct, see  PotHMtrx;  // BY ROW is correct!   see HydrogenJUnit.test_2s
      for (int j2 = 0; j2 < this.confs.size(); j2++) {
        FuncVec conf = confArr[j][j2];
        if (conf == null  &&  !doneArr[j][j2]) {
          conf = sysH.calcDens(this.confs.get(j), this.confs.get(j2));
          confArr[j][j2] = conf;
          doneArr[j][j2] = true;  // this is to stop re-calculating null contributions
          confArr[j2][j] = conf;
          doneArr[j2][j] = true;
        }
        if (conf == null)
          continue;
        double c2 = C[j2][r];  // [j][i] is correct, see  PotHMtrx
        f_i.addMultSafe(cij * c2, conf);
      }
    }
    f_i.mult(norm);
    res.set(r, f_i);
  }
  return res;
}
public String toStringSorted(EigenSymm eig, int col) {
  StringBuffer buff = new StringBuffer();
  buff.append("e[" + col + "]=" + (float) (eig.getRealEVals()[col]));
  Mtrx m = eig.getV();
  double[] v = m.getColCopy(col);
  int[] idx = VecSort.sortIdxByAbs(v, false);
  for (int r = 0; r < m.getNumRows(); r++) {
    int sortedIdx = idx[r];
    buff.append("\n\tv[" + sortedIdx + "]=\t");
    buff.append((float) m.get(sortedIdx, col)).append(" * ");
    buff.append(confs.get(sortedIdx).toString());
  }
  return buff.toString();
}
public String toString(EigenSymm eig, int col) {
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
    buff.append(confs.get(r).toString());
  }
  buff.append("\n\tnorm=" + norm);
  return buff.toString();
}
public int getConfArrSize() {
  return confs.size();
}
}
