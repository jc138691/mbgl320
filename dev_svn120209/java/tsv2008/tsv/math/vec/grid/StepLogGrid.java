package math.vec.grid;
import math.vec.Vec;
import math.vec.VecDbgView;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 27/08/12, 1:09 PM
 */
public class StepLogGrid extends StepGrid {
  public static Log log = Log.getLog(StepLogGrid.class);
  public StepLogGrid(double first, double last, int size) {
    super(first, last, size);
  }
  public StepLogGrid(StepGridOpt model) {
    this(model.getFirst(), model.getLast(), model.getNumPoints());
  }
  private void loadGrid(Range range) {
    //TODO
//    double h = step.getStep();
//    for (int i = 0; i < size(); i++) {
//      set(i, range.getLeft() + h * i);
//    }
//    log.dbg("StepGrid.loadGrid()=", new VecDbgView(this));
  }
}