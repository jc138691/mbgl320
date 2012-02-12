package atom.wf.log_cr;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:12:32
 */
public class TransLcrToR extends FuncVec {
  public static Log log = Log.getLog(TransLcrToR.class);
  private Vec CR2OverR2;
  private Vec CR2DivR;
  private Vec R2;
  private Vec CR2;
  private Vec CR; // y is used in FuncVec
  private Vec divR;
  private Vec CRDivR;
  private Vec divSqrtCR;
  private Vec sqrtCR;
  private Vec divCR;
  private final double c;
  private FuncLcrToR func;
  // Just to help keeping track
  // equal step in x=ln(CR); CR=c+r; c > 0
  // firstX=ln(param)
  public TransLcrToR(Vec x) {
    super(x);
    c = Math.exp(x.getFirst());  // assuming first r=0
    func = new FuncLcrToR(c);
    calc(func);
  }

  public Vec getR2() {
    if (R2 == null)
      R2 = new FuncVec(getX(), new FuncLcrToR2(c));
    return R2;
  }
  public Vec getCR2DivR2() {
    if (CR2OverR2 == null) {
      CR2OverR2 = new FuncVec(getX(), new FuncLcrToCr2DivR2(c));   log.dbg("CR2OverR2=", CR2OverR2);
    }
    return CR2OverR2;
  }
  public Vec getCR2DivR() {
    if (CR2DivR == null)
      CR2DivR = new FuncVec(getX(), new FuncLcrToCr2DivR(c));
    return CR2DivR;
  }
  public Vec getDivR() {
    if (divR == null)
      divR = new FuncVec(getX(), new FuncLcrToDivR(c));
    return divR;
  }
  public Vec getCRDivR() {
    if (CRDivR == null)
      CRDivR = new FuncVec(getX(), new FuncLcrToCrDivR(c));
    return CRDivR;
  }
  public Vec getDivCR() {
    if (divCR == null)
      divCR = new FuncVec(getX(), new FuncLcrToDivCr());
    return divCR;
  }
  public Vec getDivSqrtCR() {
    if (divSqrtCR == null) {
      divSqrtCR = new FuncVec(getX(), new FuncLcrToDivSqrtCr());  log.dbg("divSqrtCR=", divSqrtCR);
    }
    return divSqrtCR;
  }
  public Vec getSqrtCR() {
    if (sqrtCR == null) {
      sqrtCR = new FuncVec(getX(), new FuncLcrToSqrtCr());  log.dbg("sqrtCR=", sqrtCR);
    }
    return sqrtCR;
  }
  public Vec getCR2() {
    if (CR2 == null)
      CR2 = new FuncVec(getX(), new FuncLcrToCR2());
    return CR2;
  }
  public Vec getCR() {
    if (CR == null)
      CR = new FuncVec(getX(), new FuncLcrToCr());
    return CR;
  }

  public FuncLcrToR getFunc() {
    return func;
  }
}

