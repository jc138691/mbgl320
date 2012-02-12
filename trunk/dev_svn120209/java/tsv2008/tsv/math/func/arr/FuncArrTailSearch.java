package math.func.arr;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 15:01:11
 */
public class FuncArrTailSearch extends FuncArr {
  public static Log log = Log.getLog(FuncArrTailSearch.class);
  private int resIdxFunc;
  private int resIdxX;
  private double resX;
  private double resFunc;
  public FuncArrTailSearch(FuncArr from) {
    super(from);
  }
  public String toString() {
    return "func["+resIdxFunc+"][ix="+resIdxX+"]="+(float)resFunc+", x["+resIdxX+"]="+(float)resX;
  }
  // greater or equal
  public void findLastGE(double eps) { log.dbg("findLastGE(eps=", eps);
    resIdxX = -1; // all smaller
    resIdxFunc = -1; // all smaller
    for (int n = 0; n < size(); n++) {
      Vec func = get(n);
      int ix = findLastGE(func, eps);
      if (resIdxX < ix) {  log.inl().dbg("func[n=", n).eol().dbg("][idx=", ix);
        resIdxX = ix;
        resX = getX().get(ix);
        resFunc = func.get(ix);
        resIdxFunc = n;
      }
    }
  }
  private int findLastGE(Vec v, double eps) {  // greater or equal
    for (int i = v.size()-1; i >= 0; i--) {
      double d = v.get(i);
      if (Math.abs(d) < eps)
        continue;
      log.inl().dbg("v[", i).dbg("]=", d).eol().dbg(" >= eps=", eps);
      return i;
    }
    log.dbg("all smaller; findLastGE()=-1");
    return -1;    //   all smaller
  }

  public int getResIdxX() {
    return resIdxX;
  }
  public double getResX() {
    return resX;
  }
  public int getResIdxFunc() {
    return resIdxFunc;
  }
  public double getResFunc() {
    return resFunc;
  }
}
