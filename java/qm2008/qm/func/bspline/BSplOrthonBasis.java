package func.bspline;
import math.vec.Vec;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.integral.Quadr;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 10:26:14
 */
public class BSplOrthonBasis extends BSplArr {    //B-splines
  public static Log log = Log.getLog(BSplOrthonBasis.class);
  public BSplOrthonBasis(Quadr w, Vec t, int k) {
    super(w.getX(), t, k);
    setNormQuadr(w);
    setRefQuadr(w);

//    FuncVec[] saved = getArray();
    int newSize = calcBasisSize(t, k);
    FuncArr newArr = new FuncArr(getX());
//    setArray(new FuncVec[calcBasisSize(t, k)]); // the first and the last two are ignored
    for (int i = 0; i < newSize; i++) {
      newArr.add(get(i + getNumMissingHead()));
//      set(i, saved[i + getNumMissingHead()]);
    }
//    OrthonFactory.makeOrthon(newArr, w);
    setArr(newArr);
  }

  // REMEMBER TO overwrite
  protected int getNumMissingHead() {
    return 0;
  }
  protected int getNumMissing() {
    return 0;
  }

  public int calcBasisSize(Vec t, int k) {
    return calcArraySize(t, k) - getNumMissing();  // removing the first and the last two
  }
  public FuncVec expand(FuncVec f) {
    Vec x = f.getX();
    FuncVec res = new FuncVec(x);
    for (int i = 0; i < size(); i++) {
      Vec fi = get(i);             log.dbg("i=", i);
      double ci = getNormQuadr().calc(fi, f);  log.dbg("<Bi|f>=", ci);
      res.addMultSafe(ci, fi);
    }
    return res;
  }

}
