package atom.wf.lcr.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 10:29:50 */
import atom.wf.lcr.WFQuadrLcr;
import func.bspline.BSplBasisFactory;
import math.func.arr.FuncArr;
import math.vec.grid.StepGrid;
import math.integral.OrthonFactory;
import func.bspline.test.BSplOrthonBasisTest;
public class BSplLogCRBasisTest extends BSplOrthonBasisTest {
  public void testBSplineLogCR() throws Exception {
    double FIRST = -2;
    int NUM_STEPS = 101;
    double LAST = 2; //
    StepGrid x = new StepGrid(FIRST, LAST, NUM_STEPS);
    WFQuadrLcr w = new WFQuadrLcr(x);
    WFQuadrLcr wCR2 = w.getWithCR2();
    int k = 5;
    int N = 6;
    FuncArr arr = BSplBasisFactory.makeFromBasisSize(wCR2, N, k);
//    saveArrayK(x, arr, k, "orthog");
    double normErr = OrthonFactory.calcMaxOrthErr(arr, wCR2);
    assertEquals(0, normErr, 2.e-15);
  }
}