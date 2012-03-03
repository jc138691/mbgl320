package func.bspline;
import atom.wf.bspline.BSplBoundBasis;
import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.WFQuadrR;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import math.func.deriv.DerivPts9;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 10:33:44
 */
public class BSplBasisFactory {
  public static Log log = Log.getLog(BSplBasisFactory.class);

  public static BSplBoundBasis makeFromBasisSize(WFQuadrLcr w, int basisSize, int k) {
    int knotsNum = BSplBoundBasis.calcKnotsNumFromBasisSize(basisSize, k);
    Vec t = BSplBoundBasis.makeBSplKnotsFromGrid(knotsNum, w.getX());
    return new BSplBoundBasis(w, t, k);
  }

  public static BSplBoundBasis makeDefaultLogCR(double first, double last
    , int basisSize, int nBlocksPerKnot) {
    // determine num grid points
    int wSize = WFQuadrLcr.POINTS_N;  // 5
    int drvSize = DerivPts9.MIN_GRID_SIZE;   // 9
    int kBspl = 5;
    int nKnots = BSplBoundBasis.calcKnotsNumFromBasisSize(basisSize, kBspl); log.dbg("nKnots=", nKnots);

    // Remember inside points overlap
    int blockSize = nBlocksPerKnot * (drvSize - 1) + 1;  log.dbg("blockSize=", blockSize);
    int gridSize = (nKnots - 1) * (blockSize - 1) + 1;   log.dbg("gridSize=", gridSize);
    StepGrid logCR = new StepGrid(first, last, gridSize);  log.dbg("logCR=", logCR);
    WFQuadrLcr w = new WFQuadrLcr(logCR);              log.dbg("w=", w);
    WFQuadrLcr wCR2 = w.getWithCR2();                                    log.dbg("wCR2=", w);

    Vec t = BSplBoundBasis.makeBSplKnotsFromGrid(nKnots, logCR);  log.dbg("t=", t);
    BSplBoundBasis res = new BSplBoundBasis(wCR2, t, kBspl);
    res.setRefQuadr(w);
    return res;
  }

  public static BSplBoundBasis makeDefaultR(double first, double last
    , int basisSize, int nBlocksPerKnot) {
    // determine num grid points
    int wSize = WFQuadrLcr.POINTS_N;  // 5
    int drvSize = DerivPts9.MIN_GRID_SIZE;   // 9
    int kBspl = 5;
    int nKnots = BSplBoundBasis.calcKnotsNumFromBasisSize(basisSize, kBspl); log.dbg("nKnots=", nKnots);

    // Remember inside points overlap
    int blockSize = nBlocksPerKnot * (drvSize - 1) + 1;  log.dbg("blockSize=", blockSize);
    int gridSize = (nKnots - 1) * (blockSize - 1) + 1;   log.dbg("gridSize=", gridSize);
    StepGrid r = new StepGrid(first, last, gridSize);  log.dbg("r=", r);
    WFQuadrR w = new WFQuadrR(r);              log.dbg("w=", w);

    Vec t = BSplBoundBasis.makeBSplKnotsFromGrid(nKnots, r);  log.dbg("t=", t);
    BSplBoundBasis res = new BSplBoundBasis(w, t, kBspl);
    return res;
  }
}
