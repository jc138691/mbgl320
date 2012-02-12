package math.vec.grid.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 10:32:05 */
import junit.framework.*;
import math.vec.grid.StepGrid;
import math.Calc;
public class StepGridTest extends TestCase {
  public static Test suite() {
    TestSuite suite = new TestSuite(StepGridTest.class);
    return suite;
  }
  public void testStepGrid() throws Exception {
    int num_steps = 220;
    double step = 1. / 16.;
    double first = -4.;
    double last = first + step * num_steps;
    int size = num_steps + 1;
    StepGrid grid = new StepGrid(first, last, size);
    StepGrid grid2 = new StepGrid(first, num_steps, step);
    assertEquals(size, grid.size());
    assertEquals(size, grid2.size());
    int idx = 0;
    assertEquals(first, grid.get(idx), Calc.EPS_18);
    assertEquals(first, grid2.get(idx), Calc.EPS_18);
    idx = 1;
    assertEquals(first + step, grid.get(idx), Calc.EPS_18);
    assertEquals(first + step, grid2.get(idx), Calc.EPS_18);
  }
  public void testGetOneOverStep() throws Exception {
    int num_steps = 3;
    double step = 0.3;
    double oneOverStep = 1. / step;
    double first = 1.;
    double last = first + step * num_steps;
    int size = num_steps + 1;
    StepGrid grid = new StepGrid(first, last, size);
    StepGrid grid2 = new StepGrid(first, num_steps, step);
    assertEquals(size, grid.size());
    assertEquals(size, grid2.size());
//    assertEquals(oneOverStep, grid.getOneOverStep(), Calc.EPS_18);
//    assertEquals(oneOverStep, grid2.getOneOverStep(), Calc.EPS_18);
    int idx = 0;
    assertEquals(first, grid.get(idx), Calc.EPS_18);
    assertEquals(first, grid2.get(idx), Calc.EPS_18);
    idx = 1;
    assertEquals(first + step, grid.get(idx), Calc.EPS_18);
    assertEquals(first + step, grid2.get(idx), Calc.EPS_18);
  }
}
