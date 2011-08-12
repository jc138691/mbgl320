package math.vec.metric;
import math.vec.Vec;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 10:24:14
 */
public class DistMaxAbsErr {
  // maximum absolute distance
  public static double distSLOW(Vec v, Vec v2) {
    if (v.size() != v2.size()) {
      throw new IllegalArgumentException("different sizes: v.size()=" + v.size()
        + ", v2.size()=" + v2.size());
    }
    double res = Math.abs(v.get(0) - v2.get(0));
    int idx = 0;
    for (int i = 1; i < v.size(); i++) {
//      if (f.x.get(i) != v2.get(i)) {
//        String error = "different grid " + (float) x.get(i) + "!=" + (float) f.x.get(i);
//        LOG.error(this, error, "");
//        throw new JMatrixException(error);
//      }
      double dist = Math.abs(v.get(i) - v2.get(i));
      if (res < dist) {
        res = dist;
        idx = i;
      }
    }
    return res;
  }
}
