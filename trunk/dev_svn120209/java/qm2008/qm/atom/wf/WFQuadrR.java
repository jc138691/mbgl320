package atom.wf;
import math.integral.BooleQuadr;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.func.FuncVec;
import math.func.simple.FuncPowInt;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 16:30:58
 */
public class WFQuadrR extends WFQuadr {
  private WFQuadrR wDivR2;
  private WFQuadrR wDivR;
  public WFQuadrR(StepGrid x) {
    super(x);
  }
  @Override
  public double calcPot(Vec pot, Vec wf, Vec wf2) {
    return calc(pot, wf, wf2);
  }
  @Override
  public double calcOverlap(FuncVec wf, FuncVec wf2) {
    return calc(wf, wf2);
  }
  @Override
  public double calcPotDivR2(Vec wf, Vec wf2) {
    return getWithDivR2().calc(wf, wf2);
  }

  @Override
  public double calcPotDivR(Vec wf, Vec wf2, Vec wf3) {
    return getWithDivR().calc(wf, wf2, wf3);
  }

  public WFQuadrR getWithDivR() {
    if (wDivR == null) {
      wDivR = new WFQuadrR(getStepGrid());
      wDivR.mult(new FuncVec(getX(), new FuncPowInt(1., -1)));
    }
    return wDivR;
  }
  public WFQuadrR getWithDivR2() {
    if (wDivR2 == null) {
      wDivR2 = new WFQuadrR(getStepGrid());
      wDivR2.mult(new FuncVec(getX(), new FuncPowInt(1., -2)));
    }
    return wDivR2;
  }
//  private Vec getDivR() {
//    if (wDivR == null) {
//      wDivR = new WFQuadrR(getStepGrid());
//      wDivR.multSelf(new FuncVec(getX(), new FuncPowInt(1., -1)));
//    }
//    return wDivR;
//  }


}
