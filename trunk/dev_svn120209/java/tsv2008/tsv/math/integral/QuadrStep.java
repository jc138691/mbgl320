package math.integral;
import math.vec.Vec;
import math.vec.grid.StepGrid;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 2/03/12, 3:49 PM
 */
public class QuadrStep extends Quadr {
private StepGrid stepGrid;
private final int ptsN; // number of points per ONE interval of integration
private final int nextN; //running num of points;

public QuadrStep(StepGrid grid, int ptsNum) {
  super(grid);
  this.ptsN = ptsNum;
  this.nextN = ptsNum - 1;
  stepGrid = grid;
}
public Vec calcFuncInt_DEV(Vec f) {
  throw new IllegalArgumentException(log.error("not implemented"));
}
public StepGrid getStepGrid() {
  return stepGrid;
}
public boolean isValid(int size) {
  if ((size - 1) % nextN != 0) {
    int n = (size - 1) / nextN;
    String error = "if ((size - 1) % "+nextN+" != 0); "
      + ((size - 1) % nextN) + "!=0; "
      + "nearest sizes = " + (nextN * n + 1) + " or " + (nextN * (n + 1) + 1);
    throw new IllegalArgumentException(log.error(error));
  }
  return true;
}
public int getPtsN() {
  return ptsN;
}
public int getNextN() {
  return nextN;
}
}