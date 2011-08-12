package func.bspline.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 14:50:14 */
import junit.framework.*;
import func.bspline.BSplArr;
import math.vec.VecToString;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.Calc;

import javax.utilx.log.Log;
public class BSplArrTest extends TestCase {
  public static Log log = Log.getLog(BSplArrTest.class);
  public void testMakeTrueKnots() throws Exception {
    log.setDbg();
    int k = 3;
    double LAST = 5;
    StepGrid knots = new StepGrid(0., LAST, 6);
    Vec t = BSplArr.makeTrueKnots(knots, k);
    log.dbg("knots=" + knots);
    log.dbg("t=" + t);
    int i = 0;
    assertEquals(0, t.get(i++), Calc.EPS_16);
    assertEquals(0, t.get(i++), Calc.EPS_16);
    assertEquals(0, t.get(i++), Calc.EPS_16);
    assertEquals(1, t.get(i++), Calc.EPS_16);
    assertEquals(2, t.get(i++), Calc.EPS_16);
    assertEquals(3, t.get(i++), Calc.EPS_16);
    assertEquals(4, t.get(i++), Calc.EPS_16);
    assertEquals(5, t.get(i++), Calc.EPS_16);
    assertEquals(5, t.get(i++), Calc.EPS_16);
    assertEquals(5, t.get(i++), Calc.EPS_16);
  }
  public void testBSplineArr_k_3() throws Exception {
    log.setDbg();
    double LAST = 5;
    int SIZE = 6;
    int N_PONTS = 101;
    int k = 3;
    StepGrid x = new StepGrid(0., LAST, N_PONTS);
    StepGrid knots = new StepGrid(0., LAST, SIZE);
    BSplArr arr = new BSplArr(x, knots, k);
//    log.openFile(".", "wf", "bspline_array_k" + k + ".csv");
    log.dbg(VecToString.toString(x));
    for (int i = 0; i < arr.size(); i++) {
      log.dbg(VecToString.toString(arr.get(i)));
    }
  }
  public void testBSplineArr() throws Exception {
    log.setDbg();
    double LAST = 5;
    int SIZE = 6;
    int N_PONTS = 101;
    int k = 5;
    StepGrid x = new StepGrid(0., LAST, N_PONTS);
    StepGrid knots = new StepGrid(0., LAST, SIZE);
    BSplArr arr = new BSplArr(x, knots, k);
//    log.openFile(".", "wf", "bspline_array_k" + k + ".csv");
    log.dbg(VecToString.toString(x));
    for (int i = 0; i < arr.size(); i++) {
      log.dbg(VecToString.toString(arr.get(i)));
    }
  }
}
