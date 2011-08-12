package atom.angular;
import math.Mathx;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 13:32:29
 */
public class Reduced3j {
  public static double calc(float a, float b, float c) {
//      double v = dwig3j_(a(), b(), c(), x(), y(), z());
//      v *= pow(-1, (int)a())
//         * pow((2. * a() + 1) * (2. * c() + 1), 0.5);
//      return res(v);
    double res = Wign3j.calc(a, b, c, 0, 0, 0);
    int ia = Math.round(a);
    res *= (Mathx.pow(-1, ia) * Math.sqrt((2. * a + 1) * (2. * c + 1)));
    return res;
  }
}
