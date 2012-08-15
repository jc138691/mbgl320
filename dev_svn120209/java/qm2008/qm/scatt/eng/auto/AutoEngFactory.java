package scatt.eng.auto;
import math.vec.IntVec;
import math.vec.Vec;

import javax.utilx.arraysx.DbleArr;
import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 19/07/12, 11:37 AM
 */
public class AutoEngFactory {
public static Log log = Log.getLog(AutoEngFactory.class);
// divide last interval
public static Vec makeEngsByDivLast(Vec engs, IntVec points) {    //log.setDbg();
  DbleArr res = new DbleArr();            log.dbg("points=", points);
  for (int i = 0; i < engs.size() - 1; i++) { // NOTE -1 !!!
    double L = engs.get(i);      //log.dbg("L=", L);// left
    double R = engs.get(i+1);    //log.dbg("R=", R);// right
    int pN = points.get(0);      //log.dbg("pN=", pN);
    add(res, L, R, pN);
    double step = (R - L);     //log.dbg("step=", step);
    for (int d = 1; d < points.size(); d++) { // note d=1; // d - for depth
      step /= (pN + 1);        //log.dbg("step/=(pN+1)=", step); // use prev num points
      pN = points.get(d);      //log.dbg("pN=", pN);
      add(res, L, L + step, pN);
      add(res, R - step, R, pN);
    }
  }
  return new Vec(res.toArray()).sortSelf();
}
// divide half
public static Vec makeEngsByDivHalf(Vec engs, IntVec points) {    //log.setDbg();
  DbleArr res = new DbleArr();            log.dbg("points=", points);
  for (int i = 0; i < engs.size() - 1; i++) { // NOTE -1 !!!
    double L = engs.get(i);      //log.dbg("L=", L);// left
    double R = engs.get(i+1);    //log.dbg("R=", R);// right
    int pN = points.get(0);      //log.dbg("pN=", pN);
    add(res, L, R, pN);
    double step = (R - L);     //log.dbg("step=", step);
    for (int d = 1; d < points.size(); d++) { // note d=1; // d - for depth
//      step /= (pN + 1);        //log.dbg("step/=(pN+1)=", step); // use prev num points

      // funny 2.01 is just a small offset from 2. This is to help not to generate identical points
      step /= 2.01;        //log.dbg("step/=(pN+1)=", step); // use prev num points

      pN = points.get(d);      //log.dbg("pN=", pN);
      add(res, L, L + step, pN);
      add(res, R - step, R, pN);
    }
  }
  return new Vec(res.toArray()).sortSelf();
}
private static void add(DbleArr res, double L, double R, double n) {
  //log.dbg("add(L=", L);  log.dbg("R=", R);  log.dbg("n=", n);
  double step = (R - L) / (n + 1);  //log.dbg("step=", step);
  for (int i = 0; i < n; i++) {
    res.add(L + step * (i+1));    //log.dbg("res.getLast()=", res.getLast());
  }
}
}
