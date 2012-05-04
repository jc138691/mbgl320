package atom.wf;

import math.integral.QuadrPts5;
import math.vec.Vec;
import math.vec.grid.StepGrid;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 10:25:22 AM
 */
public abstract class WFQuadr extends QuadrPts5 {
  public WFQuadr(StepGrid x) {
    super(x);
  }
  public abstract double calcPotDivR2(Vec wf, Vec wf2);
  public abstract double calcPotDivR(Vec wf, Vec wf2, Vec wf3);
//  public abstract double calcPot(Vec pot, Vec wf, Vec wf2);
  public Vec getR() {return getX();}

}
