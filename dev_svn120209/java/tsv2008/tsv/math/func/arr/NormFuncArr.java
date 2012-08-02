package math.func.arr;
import math.integral.Quadr;
import math.integral.OrthFactory;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 30/07/2008, Time: 13:52:36
 */
public class NormFuncArr extends FuncArr {
  public static Log log = Log.getLog(NormFuncArr.class);
  private Quadr normQuadr;    // this quadr is used for normalization
  private Quadr refQuadr;  // needed for LogCR, normalizing weights are different
  public NormFuncArr(final Vec x, int arrSize) {
    super(x, arrSize);
  }
  public NormFuncArr(Quadr w, FuncArr from) {
    super(from);
    this.normQuadr = w;
    this.refQuadr = w;
  }
  public NormFuncArr(Quadr w) {
    super(w.getX());
    this.normQuadr = w;
    this.refQuadr = w;
  }
  public Quadr getNormQuadr() {
    return normQuadr;
  }
  public void setNormQuadr(Quadr w) {
    normQuadr = w;
  }
  public Quadr getRefQuadr() {
    return refQuadr;
  }
  public void setRefQuadr(Quadr w) {
    refQuadr = w;
  }
//  public void makeOrthRotate() {
//    OrthFactory.makeOrthRotate(this, normQuadr);
//  }
  public double calcMaxOrthonErr() {
    double normErr = OrthFactory.calcMaxOrthonErr(this);
    return normErr;
  }
}
