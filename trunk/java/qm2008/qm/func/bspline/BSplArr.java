package func.bspline;
import math.func.arr.NormFuncArr;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 15:48:02
 */
public class BSplArr extends NormFuncArr {
  public static Log log = Log.getLog(BSplArr.class);
  final private Vec tt;  // true t-knots e.g. {0,0,0,1,.... 4,5,5}
  private int currK = 0;
  private final int MAX_K;
  public BSplArr(final Vec x
    , final Vec t // knots
    , final int k // order
  ) {
    super(x, calcArraySize(t, k));
    MAX_K = k;
    tt = makeTrueKnots(t, k);
    loadBOne();
    loadBSplineArr();
  }
  public static int calcArraySize(Vec g, int k) {
    return g.size() + k - 2;
  }
  // g must be g[i] < g[i+1]
  public static Vec makeTrueKnots(Vec g, int k) { log.dbg("k=", k); log.dbg("g=", g);
    Vec res = new Vec(g.size() + 2 * (k - 1));  // added t(0)=...=t(k-2) and t(n+2)=...=t(n+k)
    for (int i = 0; i < g.size(); i++) {
      res.set(i + k - 1, g.get(i));
    }                                             log.dbg("res(after loop#1)=", res);
    for (int i = 0; i < k - 1; i++) {
      res.set(i, g.getFirst());
      res.set(res.size() - 1 - i, g.getLast());
    }                                             log.dbg("res(after loop#2)=", res);
    return res;
  }
  // reverse of calcArraySize
  public static int calcKnotsNum(int arraySize, int k) {
    log.dbg("calcKnotsNum(arraySize=", arraySize); log.dbg("k=", k);
    int res = arraySize - k + 2;  log.dbg("res=", res);
    return res;
  }
  public int getOrder() {
    return MAX_K;
  }
  public static Vec makeBSplKnotsFromGrid(int knotsNum, Vec grid) {
    log.dbg("makeBSplKnotsFromGrid(knotsNum=", knotsNum);  log.dbg("grid=", grid);
    Vec res = new Vec(knotsNum);
//      t.set(0, r.first());// t[0]=r[0]
    res.set(knotsNum - 1, grid.getLast());// t[last]=r[last]
    int idxStep = (grid.size() - 1) / (knotsNum - 1);   log.dbg("idxStep=", idxStep);
    for (int i = 0; i < knotsNum - 1; i++) {
      res.set(i, grid.get(i * idxStep));
    }
    log.dbg("res=", res);
    return res;
  }
  public void loadNextK() {
    currK++;
    if (currK >= MAX_K) {
      throw new IllegalArgumentException(log.error("Trying to exceed maximum K by asking for k=" + currK));
    }
    Vec x = getX();
    for (int j = 0; j < x.size(); j++) {
      double xj = x.get(j);
      for (int i = 0; i < size(); i++) {
        double distL = tt.get(i + currK) - tt.get(i);
        double distR = tt.get(i + currK + 1) - tt.get(i + 1);
        if (distL != 0)
          distL = 1. / distL;
        if (distR != 0)
          distR = 1. / distR;
        double dL = xj - tt.get(i);//      deltal(j) = x - t(left+1-j)
        double dR = tt.get(i + currK + 1) - xj; //      deltar(j) = t(left+j) - x
        Vec Bk = get(i);
        double L = dL * Bk.get(j) * distL;
        if (i < size() - 1) {
          double R = dR * get(i + 1).get(j) * distR;
          Bk.set(j, L + R);
        } else {
          Bk.set(j, L);
        }
      }
    }
    log.dbg("loadNextK(currK=", currK);  log.dbg("this=", this);
  }
  private void loadBSplineArr() {
    for (int k = 1; k < MAX_K; k++) {   // note: loadBOne was called first
      loadNextK();
    }
  }
  private void loadBOne() {
    Vec x = getX();
    int i = 0;
    for (int j = 0; j < x.size() && i < size();) {
      double xj = x.get(j);
      double ti = tt.get(i);
      double ti2 = tt.get(i + 1);
//         log.debug("t[i]=[" + i + "]=" + ti);
//         log.debug("t[i+1]=[" + (i+1) + "]=" + ti2);
//         log.debug("x[j]=x[" + j + "]=" + xj);
//         log.debug("(ti <= xj && xj < ti1) = ("
//                 + ti + "<=" + xj + "&&" + xj + "<" + ti2
//                 + ")=" + (ti <= xj && xj < ti2));
      if (ti < ti2
        && (ti < xj  || Double.compare(ti, xj) == 0)
        && xj < ti2) {
        get(i).set(j, 1.);
        j++;
//            LOG.report(this, "getLine(i).set(j, 1)");
      } else {
        log.dbg("loadBOne for arr[", i);  log.dbg("]=", get(i));
        i++;
//            LOG.report(this, "i++");
      }
    }
  }
}