package atom.angular.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 12:02:28 */

import atom.angular.Wign3j;
import project.workflow.task.test.FlowTest;

public class Wign3jTest extends FlowTest {
  public Wign3jTest() {      // needed by FlowTest
    super(Wign3jTest.class);
  }

  public void testCalcClebsh() throws Exception {
    //
    //  <j1, m1, j2, m2| jm> = (-)**(j1-j2+m) sqrt(2j+1) (j1 j2  j)
    //                                                   (m1 m2 -m)
    //  (j1  j1 0) = (-)**(j1-m1)   1/sqrt(2 * j1 + 1)
    //  (m1 -m1 0)
    double res = Wign3j.calcClebsh(1, 1, 0, 0, 0, 0);
    assertEquals(0, Math.abs(-1. / Math.sqrt(3.) - res), 6e-23);
    res = Wign3j.calcClebsh2L(2, 2, 0, 0, 0, 0);
    assertEquals(0, Math.abs(-1. / Math.sqrt(3.) - res), 6e-23);
    res = Wign3j.calcClebsh(1, 1, 0, -1, 1, 0);
    assertEquals(0, Math.abs(1. / Math.sqrt(3.) - res), 2e-16);
    res = Wign3j.calcClebsh(1, 1, 0, 1, -1, 0);
    assertEquals(0, Math.abs(1. / Math.sqrt(3.) - res), 2e-16);
    res = Wign3j.calcClebsh(1, 0, 1, 0, 0, 0);
    assertEquals(0, Math.abs(1. - res), 6e-23);
    res = Wign3j.calcClebsh(20, 20, 0, 0, 0, 0);
    assertEquals(0, Math.abs(1. / Math.sqrt(2. * 20 + 1) - res), 5e-16);
  }
}