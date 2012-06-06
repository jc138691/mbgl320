package atom.wf;

import math.integral.QuadrPts5;
import math.vec.Vec;
import math.vec.grid.StepGrid;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 10:25:22 AM
 */
// todo:  WFQuadrD1 is the same as  WFQuadrR
// todo remove WFQuadrR; keep  WFQuadrD1
public abstract class WFQuadrD1 extends QuadrPts5 {
  public WFQuadrD1(StepGrid x) {
    super(x);
  }
  public abstract double calcWithDivR2(Vec wf, Vec wf2);
  public abstract double calcWithDivR(Vec wf, Vec wf2, Vec wf3);
  public Vec getR() {return getX();}

}
