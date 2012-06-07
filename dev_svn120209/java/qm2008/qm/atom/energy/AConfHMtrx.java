package atom.energy;
import atom.shell.IConfArr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.mtrx.jamax.EigenSymm;
import math.vec.Vec;
import math.vec.VecSort;

import javax.utilx.log.Log;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:24:45
*/
public class AConfHMtrx extends HMtrx {
public static Log log = Log.getLog(AConfHMtrx.class);
private final ISysH sysH;
private final IConfArr confArr;
private FuncArr density;
public AConfHMtrx(IConfArr basis, final ISysH atom) {
  super(basis.size(), basis.size());
  this.sysH = atom;
  this.confArr = basis;
  load();
}

public IConfArr getConfArr() {
  return confArr;
}
public ISysH getSysH() {
  return sysH;
}
private void load() {
  for (int r = 0; r < confArr.size(); r++) {
    if ((10* confArr.size())%(r+1) == 0) {
      log.dbg("AConfHMtrx row=" + r + ", " + (int)(100.* r / confArr.size()) + "%");
    }
    for (int c = r; c < confArr.size(); c++) { // NOTE c=r
      // Atomic system should know how to calculate itself
      Energy res = sysH.calcH(confArr.get(r), confArr.get(c));
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
  if (confArr == null  ||  confArr.size() == 0  ||  maxNum == 0) {
    return null;
  }
  int size = confArr.size();
  if (maxNum > 0) {
    size = Math.min(maxNum, confArr.size());
  }

  Vec x = confArr.getX();
  FuncArr res = new FuncArr(x, size);
  FuncVec[][] confArr = new FuncVec[this.confArr.size()][this.confArr.size()];
  boolean[][] doneArr = new boolean[this.confArr.size()][this.confArr.size()];

  Mtrx v = eig().getV();
  double[][] C = v.getArray();
  double norm = 1. / sysH.getNumElec();
  for (int r = 0; r < size; r++) {
    if ((10* this.confArr.size())%(r+1) == 0) {
      log.dbg("calcDensity row=", r); log.dbg("rows%=", 100.* r / this.confArr.size());
    }
    FuncVec f_i = new FuncVec(x);
    for (int j = 0; j < this.confArr.size(); j++) {
      double cij = C[j][r];  // [j][i] is correct, see  PotHMtrx;  // BY ROW is correct!   see HydrogenJUnit.test_2s
      for (int j2 = 0; j2 < this.confArr.size(); j2++) {
        FuncVec conf = confArr[j][j2];
        if (conf == null  &&  !doneArr[j][j2]) {
          conf = sysH.calcDensity(this.confArr.get(j), this.confArr.get(j2));
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
    buff.append(confArr.get(sortedIdx).toString());
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
    buff.append(confArr.get(r).toString());
  }
  buff.append("\n\tnorm=" + norm);
  return buff.toString();
}
public int getConfArrSize() {
  return confArr.size();
}
}
