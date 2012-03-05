package math.integral.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 16:51:43 */
import math.func.deriv.DerivPts5;
import math.func.intrg.IntgPts5_TODO;
import math.func.simple.*;
import math.integral.QuadrStep4;
import math.integral.QuadrStep5;
import math.vec.grid.StepGrid;
import math.func.*;
import math.Calc;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;
public class QuadrStepTest extends FlowTest {
public static Log log = Log.getLog(QuadrStepTest.class);
public QuadrStepTest() {
  super(QuadrStepTest.class);  // NOTE!!! this is needed for FlowTest
}

public void testBooleIntegral() throws Exception {    log.setDbg();
  StepGrid grid = new StepGrid(0., 1., 5);
  QuadrStep5 w = new QuadrStep5(grid);
  assertEquals(0.5 * 7.0 / 45, w.get(0), Calc.EPS_16);
  assertEquals(0.5 * 32.0 / 45, w.get(1), Calc.EPS_16);
  assertEquals(0.5 * 12.0 / 45, w.get(2), Calc.EPS_16);
  assertEquals(0.5 * 32.0 / 45, w.get(3), Calc.EPS_16);
  assertEquals(0.5 * 7.0 / 45, w.get(4), Calc.EPS_16);
  FuncVec func = new FuncVec(grid, new FuncConst(1.0));
  assertEquals(1.0, w.calc(func), Calc.EPS_16);

  // fi(x) = Int_0^x 1 = x;
  FuncVec fi = w.calcFuncIntOK(func);
  // Int_0^1 fi(x) = 0.5
  assertEquals(0.5, w.calc(fi), Calc.EPS_16);
  FuncVec fi2 = new DerivPts5(func);                   log.info("DerivPts5(func)=", fi2);
  fi2 = new IntgPts5_TODO(func);                            log.info("IntgPts5(func)=", fi2);
  assertEquals(0.5, w.calc(fi2), Calc.EPS_16);

  grid = new StepGrid(0., 1., 9);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncConst(1.0));
  fi = w.calcFuncIntOK(func);
  assertEquals(0.5, w.calc(fi), Calc.EPS_16);
  fi2 = new DerivPts5(func);                   log.info("DerivPts5(func)=", fi2);
  fi2 = new IntgPts5_TODO(func);                            log.info("IntgPts5(func)=", fi2);
  assertEquals(0.5, w.calc(fi2), Calc.EPS_16);

  grid = new StepGrid(0., 1., 13);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncConst(1.0));
  fi = w.calcFuncIntOK(func);
  assertEquals(0.5, w.calc(fi), Calc.EPS_16);
  fi2 = new DerivPts5(func);                   log.info("DerivPts5(func)=", fi2);
  fi2 = new IntgPts5_TODO(func);                            log.info("IntgPts5(func)=", fi2);
  assertEquals(0.5, w.calc(fi2), Calc.EPS_16);

  StepGrid grid4 = new StepGrid(0., 1., 4);
  QuadrStep4 w4 = new QuadrStep4(grid4);
  FuncVec f4 = new FuncVec(grid4, new FuncConst(1.0));
  assertEquals(1.0, w4.calc(f4), Calc.EPS_16);

  double[] c = {1, -1, 1, -1, 1};
  func = new FuncVec(grid, new FuncPolynom(c));
  assertEquals(1. - 1. / 2 + 1. / 3 - 1. / 4 + 1. / 5, w.calc(func), Calc.EPS_15);

  f4 = new FuncVec(grid4, new FuncPolynom(c));
  assertEquals(1. - 1. / 2 + 1. / 3 - 1. / 4 + 1. / 5, w4.calc(f4), Calc.EPS_2);

  grid4 = new StepGrid(0., 1., 7);
  w4 = new QuadrStep4(grid4);
  f4 = new FuncVec(grid4, new FuncPolynom(c));
  assertEquals(1. - 1. / 2 + 1. / 3 - 1. / 4 + 1. / 5, w4.calc(f4), Calc.EPS_3);

  double[] c2 = {0, 1};
  func = new FuncVec(grid, new FuncPolynom(c2));
  assertEquals(0.5, w.calc(func), Calc.EPS_16);

  // fi(x) = Int_0^x r = 0.5 r^2;
  fi = w.calcFuncIntOK(func);
  // Int_0^1 fi(x) = 0.5/3
  assertEquals(0.5 / 3, w.calc(fi), Calc.EPS_16);
  fi2 = new IntgPts5_TODO(func);                            log.info("IntgPts5(func)=", fi2);
  assertEquals(0.5 / 3, w.calc(fi2), Calc.EPS_16);

  f4 = new FuncVec(grid4, new FuncPolynom(c2));
  assertEquals(0.5, w4.calc(f4), Calc.EPS_16);

  double[] c3 = {0, 0, 1};
  func = new FuncVec(grid, new FuncPolynom(c3));
  assertEquals(1. / 3, w.calc(func), Calc.EPS_16);

  // fi(x) = Int_0^x r^2 = 1/3 r^3;
  fi = w.calcFuncIntOK(func);
  // Int_0^1 fi(x) = 1/3 1/4
  assertEquals(1. / (3 * 4), w.calc(fi), Calc.EPS_16);
  fi2 = new IntgPts5_TODO(func);                            log.info("IntgPts5(func)=", fi2);
  assertEquals(1. / (3 * 4), w.calc(fi2), Calc.EPS_16);

  f4 = new FuncVec(grid4, new FuncPolynom(c3));
  assertEquals(1. / 3, w4.calc(f4), Calc.EPS_16);
}
public void testBodeWeights2() {
  FuncVec func2;
  FuncVec func3;
  StepGrid grid = new StepGrid(0., Math.PI, 5);
  QuadrStep5 w = new QuadrStep5(grid);
  FuncVec func = new FuncVec(grid, new FuncSin());
  assertEquals(2., w.calc(func), 2e-3);

  grid = new StepGrid(0., Math.PI, 9);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncSin());
  func2 = new FuncVec(grid, new FuncCos());        // int_0^x sin(r) = 1-cos(x)
  func3 = new FuncVec(grid,  new FuncConst(1.0));  log.info("1-cos(x)=", func3);
  func3.addMultSafe(-1, func2);                    log.info("1-cos(x)=", func3);
  assertEquals(2., w.calc(func), 2e-5);

  // fi(x) = Int_0^x sin(r) = 1 - cos(x);
  FuncVec fi = w.calcFuncIntOK(func);                 log.info("calcFuncIntOLD(Sin)=", fi);
  // Int_0^PI [1 - cos(x)] = PI
  assertEquals(Math.PI, w.calc(fi), 1e-3);

  FuncVec fi2 = new DerivPts5(func);                   //log.info("DerivPts5(Sin)=", fi2);
  fi2 = new IntgPts5_TODO(func);                            log.info("IntgPts5(Sin)=", fi2);
  assertEquals(Math.PI, w.calc(fi2), 1e-2);// !!!!!

  grid = new StepGrid(0., Math.PI, 13);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncSin());
  fi = w.calcFuncIntOK(func);
  assertEquals(Math.PI, w.calc(fi), 1e-4);
  fi2 = new IntgPts5_TODO(func);                            log.info("IntgPts5(func)=", fi2);
  assertEquals(Math.PI, w.calc(fi2), 1e-2);

  grid = new StepGrid(0., Math.PI, 21);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncSin());
  fi = w.calcFuncIntOK(func);
  assertEquals(Math.PI, w.calc(fi), 1e-5);

  StepGrid grid4 = new StepGrid(0., Math.PI, 10);
  QuadrStep4 w4 = new QuadrStep4(grid4);
  FuncVec f4 = new FuncVec(grid4, new FuncSin());
  assertEquals(2., w4.calc(f4), 4e-4);

  grid = new StepGrid(0., 1., 9);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncExp(1));
  assertEquals(Math.exp(1.) - 1., w.calc(func), 1e-7);

  grid4 = new StepGrid(0., 1., 10);
  w4 = new QuadrStep4(grid4);
  f4 = new FuncVec(grid4, new FuncExp(1));
  assertEquals(Math.exp(1.) - 1., w4.calc(f4), 4e-6);

  grid = new StepGrid(-1., 1., 9);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncExp(-1.));
  assertEquals(Math.exp(1.) - Math.exp(-1.), w.calc(func), 2e-6);

  grid = new StepGrid(-1., 1., 17);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncExp(-1.));
  assertEquals(Math.exp(1.) - Math.exp(-1.), w.calc(func), 1e-7);

  fi = w.calcFuncIntOK(func);   // exp(1)-exp(-x)
  assertEquals(Math.exp(1.) + Math.exp(-1.), w.calc(fi), 2e-5);

  grid = new StepGrid(-1., 1., 13);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncExp(-1.));
  assertEquals(0., Math.abs(Math.exp(1.) - Math.exp(-1.) - w.calc(func)), 2e-7);
  grid = new StepGrid(-1., 1., 5);
  w = new QuadrStep5(grid);
  func = new FuncVec(grid, new FuncExp(-1.));
  assertEquals(0., Math.abs(Math.exp(1.) - Math.exp(-1.) - w.calc(func)), 7e-5);
}
}