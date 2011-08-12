package atom.angular.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 13:51:59 */
import junit.framework.*;
import atom.angular.Wign6j;
import project.workflow.task.test.FlowTest;

public class Wign6jTest extends FlowTest {
  public Wign6jTest() {      // needed by FlowTest
    super(Wign6jTest.class);
  }
  public void testCalc() throws Exception {
    //      // from Landau p.522
    double abc0 = Wign6j.calc(1, 1, 1
      , 0, 1, 1);
    double abc0_res = Wign6j.calc_abc0(1, 1, 1);
    double abc0_2 = Wign6j.calc(0.5f, 0.5f, 1
      , 0, 1, 0.5f);
    double abc0_2_res = Wign6j.calc_abc0(0.5f, 0.5f, 1);
    assertEquals(0, Math.abs(abc0 - abc0_res), 3e-16);
    assertEquals(0, Math.abs(abc0_2 - abc0_2_res), 2e-16);
    double res = Wign6j.calc(0.5f, 0.5f, 1
      , 1, 1, 0.5f);
    assertEquals(0, Math.abs(-1. / 3 - res), 6e-17);
    res = Wign6j.calc(0.5f, 0.5f, 1
      , 0.5f, 1, 0.5f);
    assertEquals(0, Math.abs(0 - res), 2e-26);
    res = Wign6j.calc(1, 0.5f, 1.5f
      , 0.5f, 1, 1);
    assertEquals(0, Math.abs(-1. / 6 - res), 6e-17);
  }
}