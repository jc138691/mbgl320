package math.mtrx;
import math.mtrx.api.Mtrx;
import math.vec.Vec;
import math.vec.VecDbgView;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 20/03/12, 3:31 PM
 */
public class MtrxFactory {
public static Log log = Log.getLog(MtrxFactory.class);
public static Mtrx makeOneDiag(int size) {
  Mtrx res = new Mtrx(size, size);
  for (int i = 0; i < size; i++) {
      res.set(i, i, 1.);
  }
  return res;
}
public static Mtrx makeFromTwoVecs(Vec vR, Vec vC) {
  Mtrx res = new Mtrx(vR.size(), vC.size());
  for (int r = 0; r < vR.size(); r++) {
    double valR = vR.get(r);
    for (int c = 0; c < vC.size(); c++) {
      double valC = vC.get(c);
      res.set(r, c, valR * valC);
    }
  }
  return res;
}

public static void makeSymmByAvr(Mtrx m, int openN) {
  double[][] arr = m.getArray();
  for (int r = 0; r < openN; r++) {
    for (int c = 0; c < r; c++) {
      double avr = 0.5 * (arr[r][c] + arr[c][r]);
      arr[r][c] = avr;
      arr[c][r] = avr;
    }
  }
}
public static void makeDiagOneSqrt(Mtrx m) {
  double[][] arr = m.getArray();
  int len = Math.max(m.getNumRows(), m.getNumCols());
  for (int r = 0; r < len; r++) {
    double diag = arr[r][r];
    if (diag <= 0) {     log.setDbg();
      log.dbg("makeDiagOneSqrt(m=\n", new MtrxDbgView(m));
      String mssg = "diag <= 0";
      throw new IllegalArgumentException(log.error(mssg));
    }
    arr[r][r] = 1./Math.sqrt(diag);
  }
}
public static void sort(double[] d, double[][] V) {   //log.setDbg();
  log.dbg("--->sort(d=", new VecDbgView(d));
  log.dbg("--->sort(V=\n", new MtrxDbgView(new Mtrx(V)));

  int n = d.length;
  for (int i = 0; i < n - 1; i++) {
    int k = i;
    double p = d[i];
    for (int j = i + 1; j < n; j++) {
      if (d[j] < p) {
        k = j;
        p = d[j];
      }
    }
    if (k != i) {
      d[k] = d[i];
      d[i] = p;
      for (int j = 0; j < n; j++) {
        p = V[j][i];
        V[j][i] = V[j][k];
        V[j][k] = p;
      }
    }
  }
  log.dbg("<---sort(d=", new VecDbgView(d));
  log.dbg("<---sort(V=\n", new MtrxDbgView(new Mtrx(V)));
}

}
