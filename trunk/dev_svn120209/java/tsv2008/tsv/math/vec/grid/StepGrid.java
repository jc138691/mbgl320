package math.vec.grid;
import math.vec.Vec;
import math.vec.VecDbgView;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 14:52:33
 */
public class StepGrid extends Vec {
  public static Log log = Log.getLog(StepGrid.class);
  private Step step;
  public StepGrid(double first, double last, int size) {
    super(size);
    log.inl().dbg("StepGrid(first=", first).dbg(", last=", last).eol().dbg(", size=", size);
    Range range = new Range(first, last);
    step = new Step(range, size);
    loadGrid(range);
  }
  public StepGrid(StepGridModel model) {
    this(model.getFirst(), model.getLast(), model.getNumPoints());
  }
  public StepGrid(double first, int numSteps, double stepVal) {
    super(numSteps + 1);
    log.inl().dbg("StepGrid(first=", first).dbg("numSteps=", numSteps).eol().dbg("stepVal=", stepVal);
    this.step = new Step(stepVal);
    Range range = new Range(first, first + numSteps * stepVal);
    loadGrid(range);
  }
  final public Step getStep() {
    return step;
  }
  final public void setStep(Step step) {
    this.step = step;
  }
  private void loadGrid(Range range) {
    double h = step.getStep();
    for (int i = 0; i < size(); i++) {
      set(i, range.getLeft() + h * i);
    }
    log.dbg("StepGrid.loadGrid()=", new VecDbgView(this));
  }
  public double getGridStep() {
    return step.getStep();
  }
}
