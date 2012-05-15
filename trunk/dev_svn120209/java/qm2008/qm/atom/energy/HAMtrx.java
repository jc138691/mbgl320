package atom.energy;
import math.mtrx.Mtrx;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 15/05/12, 12:22 PM
 */
public class HAMtrx {
private Mtrx H;
private Mtrx A;  // overlap matrix
public HAMtrx(int m, int n) {
  H = new Mtrx(m, n);
  A = new Mtrx(m, n);
}
public Mtrx getH() {
  return H;
}
public Mtrx getA() {
  return A;
}
}
