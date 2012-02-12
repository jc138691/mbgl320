package math.func.deriv.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 09:36:26 */
import junit.framework.*;
import math.func.deriv.DerivPts5;
import math.func.FuncVec;
import math.func.simple.FuncPolynom;
import math.vec.grid.StepGrid;
import math.Calc;

import javax.utilx.log.Log;
public class DerivPts5Test extends TestCase {
  public void testDerivPts5() throws Exception {
    Log log = Log.getLog(DerivPts5.class); // note:  DerivPts3
    log.setDbg();
    int idx = 0;
    double first = 0;
    double last = 1;
    int size = 5;
    StepGrid x = new StepGrid(first, last, size);
    double[] coeff = {1};
    FuncVec f = new FuncVec(x, new FuncPolynom(coeff));
    FuncVec df = new DerivPts5(f);
    assertEquals(0, df.get(idx++), Calc.EPS_18);
    assertEquals(0, df.get(idx++), Calc.EPS_18);
    assertEquals(0, df.get(idx++), Calc.EPS_18);
    assertEquals(0, df.get(idx++), Calc.EPS_18);
    assertEquals(0, df.get(idx++), Calc.EPS_18);
    double[] c2 = {0, 0.5};
    f = new FuncVec(x, new FuncPolynom(c2));
    df = new DerivPts5(f);
    idx = 0;
    assertEquals(0.5, df.get(idx++), Calc.EPS_18);
    assertEquals(0.5, df.get(idx++), Calc.EPS_18);
    assertEquals(0.5, df.get(idx++), Calc.EPS_18);
    assertEquals(0.5, df.get(idx++), Calc.EPS_18);
    assertEquals(0.5, df.get(idx++), Calc.EPS_18);
    double[] c3 = {0, 0, 0.5};
    f = new FuncVec(x, new FuncPolynom(c3));
    double[] c3_deriv = {0, 1};
    df = new FuncVec(x, new FuncPolynom(c3_deriv));
    FuncVec ndf = new DerivPts5(f); // numeric derivative
    idx = 0;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    double[] c4 = {0.5, 1, 2, 3, 4};
    f = new FuncVec(x, new FuncPolynom(c4));
    double[] c4_deriv = {1, 4, 9, 16};
    df = new FuncVec(x, new FuncPolynom(c4_deriv));
    ndf = new DerivPts5(f); // numeric derivative
    idx = 0;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
    assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
    idx++;
  }
}
