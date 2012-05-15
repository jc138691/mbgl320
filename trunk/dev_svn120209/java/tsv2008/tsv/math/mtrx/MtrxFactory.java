package math.mtrx;
import math.vec.Vec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 20/03/12, 3:31 PM
 */
public class MtrxFactory {
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
public static void makeSqrt(Mtrx m) {
  double[][] arr = m.getArray();
  for (int r = 0; r < m.getNumRows(); r++) {
    for (int c = 0; c < m.getNumCols(); c++) {
      double sqrtVal = Math.sqrt(arr[r][c]);
      arr[r][c] = sqrtVal;
    }
  }
}
}
