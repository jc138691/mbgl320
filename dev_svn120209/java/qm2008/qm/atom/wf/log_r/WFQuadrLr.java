package atom.wf.log_r;
import atom.wf.WFQuadrR;
import math.vec.grid.StepGrid;
import math.vec.Vec;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 16:43:59
 */
public class WFQuadrLr extends WFQuadrR {
  private WFQuadrLr wR2;
  private final TransLrToR lrToR;
  public WFQuadrLr(StepGrid grid) {
    super(grid);
    lrToR = new TransLrToR(getX());
  }

  public TransLrToR getLrToR() {
    return lrToR;
  }

public WFQuadrLr getWithR2() {
  if (wR2 == null) {
    wR2 = new WFQuadrLr(getStepGrid());
    wR2.multSelf(new Vec(lrToR.getR2()));
  }
  return wR2;
}

}
