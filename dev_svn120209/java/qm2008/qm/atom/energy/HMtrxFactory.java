package atom.energy;
import atom.e_1.SysE1;
import atom.energy.slater.SlaterLcr;

import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.wf.lcr.WFQuadrLcr;
import math.vec.grid.StepGrid;
import math.func.arr.FuncArr;
import func.bspline.BSplBasisFactory;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/07/2008, Time: 15:45:51
 */
public class HMtrxFactory {
  public static ConfHMtrx makeFromBsplLogCR(double first, double last, int gridSize
    , int basisSize, int L) {
    StepGrid logCR = new StepGrid(first, last, gridSize);
    WFQuadrLcr w = new WFQuadrLcr(logCR);
    SlaterLcr slater = new SlaterLcr(w);

    int BSPLINE_K = 5;
    FuncArr arr = BSplBasisFactory.makeFromBasisSize(w, basisSize, BSPLINE_K);

    ConfArr basis = ConfArrFactoryE2.makeOneElec(basisSize, L, arr);
    SysE1 hydR = new SysE1(-1., slater);
    return new ConfHMtrx(basis, hydR);
  }
}
