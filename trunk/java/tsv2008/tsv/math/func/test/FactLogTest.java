package math.func.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 09:51:26 */
import junit.framework.*;
import math.func.Factorial;
import math.func.FactLn;
import math.Calc;
public class FactLogTest extends TestCase {
  public void testFactlLog() throws Exception {
    Factorial fact = new Factorial(2);
    assertEquals(1., fact.calc(0), Calc.EPS_13);
    assertEquals(1., fact.calc(1), Calc.EPS_13);
    assertEquals(2., fact.calc(2), Calc.EPS_13);
    assertEquals(6., fact.calc(3), Calc.EPS_13);
    assertEquals(24., fact.calc(4), Calc.EPS_13);
    assertEquals(120., fact.calc(5), Calc.EPS_13);
    FactLn factLog = FactLn.makeInstance(2);
    assertEquals(Math.log(120.), factLog.calc(5), Calc.EPS_13);
  }
}
