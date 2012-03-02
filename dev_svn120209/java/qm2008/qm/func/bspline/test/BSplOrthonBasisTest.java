package func.bspline.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 10:27:40 */
import junit.framework.*;
import func.bspline.BSplOrthonBasis;
import math.integral.QuadrStep5;
import math.vec.grid.StepGrid;
import math.integral.OrthonFactory;

import javax.utilx.log.Log;
public class BSplOrthonBasisTest extends TestCase {
  public static Log log = Log.getLog(BSplOrthonBasisTest.class);
  public void testBSplineBasis() throws Exception {
    calcArrayK(3);
    calcArrayK(5);
  }
  public void calcArrayK(int k) {
    log.setDbg();
    Log.getLog(BSplOrthonBasis.class).setDbg();
    Log.getLog(OrthonFactory.class).setDbg();
    double FIRST = 0;
    double LAST = 5;
    int NUM_STEPS = 101;
    StepGrid x = new StepGrid(FIRST, LAST, NUM_STEPS);
    QuadrStep5 w = new QuadrStep5(x);
    StepGrid knots = new StepGrid(FIRST, LAST, Math.round((float) LAST + 1));
    BSplOrthonBasis arr = new BSplOrthonBasis(w, knots, k);
//    saveArrayK(x, arr, k, "basisN");
    OrthonFactory.makeOrthon(arr, w);
    double normErr = OrthonFactory.calcMaxOrthonErr(arr, w); //double normErr = w.calcMaxNormError(arr);
//    saveArrayK(x, arr, k, "orthog");
    assertEquals(0, normErr, 3.e-15);
  }
//  public void saveArrayK(Vec x, FuncArr arr, int k, String name) {
//    log.openFile(".", "wf", "bspline_" + name + "_k" + k + ".csv");
//    log.dbg(VecToString.toCsv(x));
//    for (int i = 0; i < arr.size(); i++) {
//      log.dbg(VecToString.toCsv(arr.get(i)));
//    }
//  }
}