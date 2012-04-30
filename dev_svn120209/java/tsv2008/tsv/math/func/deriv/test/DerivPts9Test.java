package math.func.deriv.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:57:55 */
import junit.framework.*;
import math.func.deriv.DerivPts9;
import math.func.deriv.DerivPts3;
import math.func.deriv.DerivPts5;
import math.func.FuncVec;
import math.func.simple.FuncPolynom;
import math.func.simple.FuncExp;
import math.vec.grid.StepGrid;
import math.Calc;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

public class DerivPts9Test extends FlowTest {
public DerivPts9Test() {
super(DerivPts9Test.class);  // NOTE!!! this is needed for FlowTest
}

public void testDerivPts9() throws Exception {
  Log log = Log.getLog(DerivPts9.class); // note:  DerivPts3
  //log.setDbg();
  int idx = 0;
  double first = 0;
  double last = 1;
  int size = 9;
  StepGrid x = new StepGrid(first, last, size);
  double[] coeff = {1};
  FuncVec f = new FuncVec(x, new FuncPolynom(coeff));
  FuncVec df = new DerivPts9(f);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  assertEquals(0, df.get(idx++), Calc.EPS_18);
  double[] c2 = {0, 0.5};
  f = new FuncVec(x, new FuncPolynom(c2));
  df = new DerivPts9(f);
  idx = 0;
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  assertEquals(0.5, df.get(idx++), Calc.EPS_18);
  double[] c3 = {0, 0, 0.5};
  f = new FuncVec(x, new FuncPolynom(c3));
  double[] c3_deriv = {0, 1};
  df = new FuncVec(x, new FuncPolynom(c3_deriv));
  FuncVec ndf = new DerivPts9(f); // numeric derivative
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
  ndf = new DerivPts9(f); // numeric derivative
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
  assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
  idx++;
  assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
  idx++;
  assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
  idx++;
  assertEquals(df.get(idx), ndf.get(idx), Calc.EPS_18);
  idx++;
  f = new FuncVec(x, new FuncExp(2.));
  df = new FuncVec(x, new FuncExp(2.));
  df.mult(2.);
  ndf = new DerivPts9(f); // numeric derivative
  idx = 0;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 1e-5);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-6);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 4e-7);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-7);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-7);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-7);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 4e-7);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-6);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-5);
  idx++;
  size = 11;
  x = new StepGrid(first, last, size);
  f = new FuncVec(x, new FuncExp(2.));
  df = new FuncVec(x, new FuncExp(2.));
  df.mult(2.);
  ndf = new DerivPts9(f); // numeric derivative
  FuncVec nd3 = new DerivPts3(f);
  FuncVec nd5 = new DerivPts5(f);
//    LOG.report(this, "step**2 = " + (float) Math.pow(x.step(), 2));
//    LOG.report(this, "step**3 = " + (float) Math.pow(x.step(), 3));
//    LOG.report(this, "step**4 = " + (float) Math.pow(x.step(), 4));
//    LOG.report(this, "step**5 = " + (float) Math.pow(x.step(), 5));
  idx = 0;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 4e-2);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 1e-3);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-6);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 2e-2);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 3e-4);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-7);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 2e-2);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 2e-4);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 5e-8);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 3e-2);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 2e-4);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 3e-8);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 3e-2);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 3e-4);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 2e-8);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 4e-2);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 4e-4);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 3e-8);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 5e-2);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 4e-4);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 3e-8);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 0.1);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 5e-4);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 4e-8);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 0.1);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 1e-3);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 8e-8);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 0.1);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 1e-3);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 3e-7);
  idx++;
  assertEquals(0., Math.abs(df.get(idx) - nd3.get(idx)), 0.2);
  assertEquals(0., Math.abs(df.get(idx) - nd5.get(idx)), 4e-3);
  assertEquals(0., Math.abs(df.get(idx) - ndf.get(idx)), 3e-6);
  idx++;
}
}