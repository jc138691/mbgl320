package math.integral;
import math.vec.Vec;
import math.vec.grid.StepGrid;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 2/03/12, 3:49 PM
 */
public class QuadrStep extends Quadr {
private Vec quadrFuncInt;
private StepGrid stepGrid;
public QuadrStep(StepGrid grid) {
  super(grid);
  stepGrid = grid;
}
public Vec calcFuncInt(Vec f) {
  throw new IllegalArgumentException(log.error("not implemented"));
}
public Vec makeQuadrFuncInt() {
  throw new IllegalArgumentException(log.error("not implemented"));
}
public Vec getQuadrFuncInt() {
  if (quadrFuncInt == null) {
    quadrFuncInt = makeQuadrFuncInt();
  }
  return quadrFuncInt;
}
public StepGrid getStepGrid() {
  return stepGrid;
}
}