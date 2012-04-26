package javax.utilx.arraysx;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 26/04/12, 2:30 PM
 */
public class DbleArr extends TArr<Double> {
public double[] toArray() {
  double[] res = new double[size()];
  for (int i = 0; i < res.length; i++) {
    res[i] = get(i);
  }
  return res;
}
}
