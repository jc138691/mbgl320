package atom.wf.bspline;
import func.bspline.BSplOrthonBasis;
import math.vec.Vec;
import math.integral.Quadr;
import math.integral.OrthFactory;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 10:21:28
 */
public class BSplBoundBasis extends BSplOrthonBasis {
  public static Log log = Log.getLog(BSplBoundBasis.class);
  private static int NUM_MISSING_HEAD = 1;
  private static int NUM_MISSING_TAIL = 2;
//  private static int NUM_MISSING_TAIL = 1;
  private static int NUM_MISSING = NUM_MISSING_HEAD + NUM_MISSING_TAIL;
  public BSplBoundBasis(Quadr w, Vec t, int k) {
    super(w, t, k);
    OrthFactory.makeOrthRotate(this, w);
  }
  protected int getNumMissingHead() {
    return NUM_MISSING_HEAD;
  }
  protected int getNumMissing() {
    return NUM_MISSING;
  }

  // reverese of calcBasisSize
  public static int calcKnotsNumFromBasisSize(int basisSize, int k) {
//    log.remind("BSplBoundBasis.NUM_MISSING_TAIL = 1, is it ok?");  // not ok!!!
    log.dbg("calcKnotsNumFromBasisSize(basisSize=", basisSize);    log.dbg("k=", k);
    int res = calcKnotsNum(basisSize + NUM_MISSING, k);   log.dbg("res=", res);
    return res;
  }
}
