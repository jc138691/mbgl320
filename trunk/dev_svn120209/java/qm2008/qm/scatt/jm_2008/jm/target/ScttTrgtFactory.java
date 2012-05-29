package scatt.jm_2008.jm.target;
import math.vec.Vec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 2:00 PM
 */
public class ScttTrgtFactory {
public static ScttTrgtE2 makeEmptyE2() {
  ScttTrgtE2 res = new ScttTrgtE2();
  double[] tEngs = new double[1];
  tEngs[0] = 0;
  res.setEngs(new Vec(tEngs));
  return res;
}
}
