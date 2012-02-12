package calc.interpol;

import math.Calc;
import math.func.Func;
import math.func.simple.FuncPolynom;
import project.workflow.task.test.FlowTest;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 27/04/2010, 12:09:24 PM
 */
public class InterpolCubeTest extends FlowTest {
  public InterpolCubeTest() {
    super(InterpolCubeTest.class);  // NOTE!!! this is needed for FlowTest
  }

  public void testCube() throws Exception {
    double[] c = {0.5, 1.1, -0.3};
    Func pol = new FuncPolynom(c);
    double x = 0.1;
    double y = pol.calc(x);
    double x2 = -2;
    double y2 = pol.calc(x2);
    double x3 = 10;
    double y3 = pol.calc(x3);

    InterpCube func = new InterpCube(x, x2, x3, y, y2, y3);
    assertEquals(pol.calc(x), func.calc(x), Calc.EPS_10);
    assertEquals(pol.calc(x2), func.calc(x2), Calc.EPS_10);
    assertEquals(pol.calc(x3), func.calc(x3), Calc.EPS_10);

    x = 0;
    assertEquals(pol.calc(x), func.calc(x), Calc.EPS_10);

    x = -1;
    assertEquals(pol.calc(x), func.calc(x), Calc.EPS_10);

    x = 1;
    assertEquals(pol.calc(x), func.calc(x), Calc.EPS_10);
  }
}