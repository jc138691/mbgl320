package math.mtrx;
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
}
