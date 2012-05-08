package math.vec.metric;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 10:24:14
 */
public class DistMaxAbsErr {
public static Log log = Log.getLog(DistMaxAbsErr.class);
  // maximum absolute distance
  public static double distSLOW(Vec v, Vec v2) {   log.setDbg();
    if (v.size() != v2.size()) {
      throw new IllegalArgumentException("different sizes: v.size()=" + v.size()
        + ", v2.size()=" + v2.size());
    }
    double res = Math.abs(v.get(0) - v2.get(0));
    int idx = 0;
    int dbgCount = 0;
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

        {log.dbg("MAX dist[i=" + i + "] = " + dist
          + " v=" + v.get(i) + " v2=" + v2.get(i));
          dbgCount = 1;  // JUST FOR DEBUGGING
        }
      } else if (dbgCount-- > 0) {
        log.dbg("NOT max dist[i=" + i + "] = ", dist);
      }
    }
    return res;
  }
}
