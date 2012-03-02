package math.interpol.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:25:42 */
import junit.framework.*;
import math.interpol.PolynomInterpol;
import math.func.simple.FuncPolynom;
import math.func.FuncVec;
import math.func.simple.FuncPow;
import math.func.simple.FuncPow2;
import math.vec.Vec;
import project.workflow.task.test.FlowTest;
public class PolynomInterpolTest extends FlowTest {
public PolynomInterpolTest() {
  super(PolynomInterpolTest.class);  // NOTE!!! this is needed for FlowTest
}
public void testCalcPowerSLOW() throws Exception {
  double[] x = {0., 0.1, 0.3};
  Vec r = new Vec(x);
  double[] c = {0., 1};
  FuncVec f = new FuncVec(r, new FuncPolynom(c));
  double b = PolynomInterpol.calcPowerSLOW(f, 0);
  assertEquals(c[1], b, 1e-20);
  f = new FuncVec(r, new FuncPow2());
  b = PolynomInterpol.calcPowerSLOW(f, 0);
  assertEquals(0, Math.abs(b - 2), 1e-23);
  double a = 0.5;
  double t = 3.1;
  f = new FuncVec(r, new FuncPow(a, t));
  b = PolynomInterpol.calcPowerSLOW(f, 0);
  assertEquals(0, Math.abs(b - t), 1e-200);
}
}