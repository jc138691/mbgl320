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
  return distSLOW(v.getArr(), v2.getArr());
}
public static double distSLOW(double[] v, double[] v2) {   log.setDbg();
  if (v.length != v2.length) {
    throw new IllegalArgumentException("different sizes: v.length="
      + v.length  + ", v2.length=" + v2.length);
  }
  double res = Math.abs(v[0] - v2[0]);
  int idx = 0;
  int dbgCount = 0;
  for (int i = 1; i < v.length; i++) {
    double dist = Math.abs(v[i] - v2[i]);
    if (res < dist) {
      res = dist;
      idx = i;

      {log.dbg("MAX dist[i=" + i + "] = " + dist
        + " v=" + v[i] + " v2=" + v2[i]);
        dbgCount = 1;  // DEBUGGING
      }
    } else if (dbgCount-- > 0) {   // DEBUGGING
      log.dbg("NOT max dist[i=" + i + "] = ", dist);
    }
  }
  return res;
}
}
