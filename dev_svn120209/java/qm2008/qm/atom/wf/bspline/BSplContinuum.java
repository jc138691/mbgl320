package atom.wf.bspline;
import func.bspline.BSplOrthonBasis;

import javax.utilx.log.Log;

import math.integral.Quadr;
import math.integral.OrthonFactory;
import math.vec.Vec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 10:22:13
 */
public class BSplContinuum extends BSplOrthonBasis {
  public static Log log = Log.getLog(BSplBoundBasis.class);
  private static int NUM_MISSING_HEAD = 1;
//  private static int NUM_MISSING_TAIL = 2;   // bound
  private static int NUM_MISSING_TAIL = 1;   // continuum
  private static int NUM_MISSING = NUM_MISSING_HEAD + NUM_MISSING_TAIL;
  public BSplContinuum(Quadr w, Vec t, int k) {
    super(w, t, k);
    OrthonFactory.makeOrthon(this, w);
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