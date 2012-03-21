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
}
