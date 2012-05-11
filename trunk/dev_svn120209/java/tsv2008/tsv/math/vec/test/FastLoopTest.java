package math.vec.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 09:24:12 */
import math.func.Func;
import math.vec.FastLoop;
import math.Calc;
import math.vec.Vec;
import math.vec.VecFactory;
import math.vec.metric.DistMaxAbsErr;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

public class FastLoopTest extends FlowTest  {
public static Log log = Log.getLog(FastLoopTest.class);

public FastLoopTest() {
  super(FastLoopTest.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
}
public void testMinMax() throws Exception {
  for (int size = 1; size < 100; size++) {
    double[] arr = VecFactory.makeRand(size);
    assertEquals(FastLoop.min(arr), FastLoop.minTest(arr), Calc.EPS_32);
    assertEquals(FastLoop.max(arr), FastLoop.maxTest(arr), Calc.EPS_32);
    assertEquals(FastLoop.minIdx(arr), FastLoop.minIdxTest(arr));
    assertEquals(FastLoop.maxIdx(arr), FastLoop.maxIdxTest(arr));
  }
}
public void testMultSelf() throws Exception {
  double[] f, f2, f3, c;
  for (int size = 1; size < 100; size++) {
    f = VecFactory.makeRand(size);
    c = Vec.copy(f);  // copy
    f2 = VecFactory.makeRand(size);
    FastLoop.multFirst(f, f2);
    FastLoop.multFirstTest(c, f2);
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(f, c)), 1e-20);

    f = VecFactory.makeRand(size);
    c = Vec.copy(f);  // copy
    f2 = VecFactory.makeRand(size);
    f3 = VecFactory.makeRand(size);
    FastLoop.multFirst(f, f2, f3);
    FastLoop.multFirstTest(c, f2, f3);
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(f, c)), 1e-20);
  }
}
public void testDot() throws Exception {
  for (int size = 1; size < 100; size++) {
    double[] arr = VecFactory.makeRand(size);
    double[] v = VecFactory.makeRand(size);
    assertEquals(FastLoop.dot(arr, v), FastLoop.dotTest(arr, v), Calc.EPS_32);
  }
  for (int size = 2; size < 100; size++) {
    double[] arr = VecFactory.makeRand(size);
    double[] v = VecFactory.makeRand(size);
    assertEquals(FastLoop.dot(0, size-1, arr, v), FastLoop.dotTest(0, size-1, arr, v), Calc.EPS_32);
    assertEquals(FastLoop.dot(1, size, arr, v), FastLoop.dotTest(1, size, arr, v), Calc.EPS_32);
  }
  for (int size = 3; size < 100; size++) {
    double[] arr = VecFactory.makeRand(size);
    double[] v = VecFactory.makeRand(size);
    assertEquals(FastLoop.dot(1, size-1, arr, v), FastLoop.dotTest(1, size-1, arr, v), Calc.EPS_32);
    assertEquals(FastLoop.dot(2, size, arr, v), FastLoop.dotTest(2, size, arr, v), Calc.EPS_32);
  }
}
public void testPolynom() throws Exception {
//    fail("Test not implemented yet");
}
public void testMul() throws Exception {
  double[][] m = {{1., 2.}, {-1., 2.}, {0.1, -1.1}};
  double[] v2 = {-0.2, 0.5};
  double[] v = {0.8, 1.2, -0.57};
  double[] res = {0., 0., 0.};

  assertEquals(0, FastLoop.min(v2) - (-0.2), Calc.EPS_16);
  assertEquals(0, FastLoop.max(v2) - (0.5), Calc.EPS_16);
  assertEquals(0, FastLoop.min(v) - (-0.57), Calc.EPS_16);
  assertEquals(0, FastLoop.max(v) - (1.2), Calc.EPS_16);

  double[] arr  = {0., 0., 0., 0.1, 0., 0., 0., -0.1};
  assertEquals(0, FastLoop.min(arr) - (-0.1), Calc.EPS_16);
  assertEquals(0, FastLoop.max(arr) - (0.1), Calc.EPS_16);

  double[] arr2 = {0., 0., 0., 0.1, 0., 0., 0., -0.3, -0.1};
  assertEquals(0, FastLoop.min(arr2) - (-0.3), Calc.EPS_16);
  assertEquals(0, FastLoop.max(arr2) - (0.1), Calc.EPS_16);



  //   /  1,    2    \        -0.2     1*(-.2)+1=0.8
  //   | -1,    2     |  *     0.5  =  -1*(-.2)+1=1.2
  //   \  0.1,  -1.1 /                 0.1*(-.2)+(-1.1)*0.5=-0.57
  FastLoop.mul(res, m, v2);
  assertEquals(0, Math.abs(v[0] - res[0]), Calc.EPS_16);
  assertEquals(0, Math.abs(v[1] - res[1]), Calc.EPS_16);
  assertEquals(0, Math.abs(v[2] - res[2]), Calc.EPS_15);
}
public void testMulTest() throws Exception {
//    fail("Test is not implemented");
}
public void testCalc() throws Exception {
  int MAX_SIZE = 1001;
  final double TEST_A = 0.1;
  final double TEST_B = 1.;

  for (int arrSize = 1; arrSize <= MAX_SIZE; arrSize++) { // NOTE!!! arrSize from 1
    double[] x = new double[arrSize];
    double[] fArr = new double[arrSize];

    for (int i = 0; i < arrSize; i++) {
      x[i] = i;
    }
    FastLoop.calc(fArr, x, new Func(){
      public double calc(double x) { return x * TEST_A - TEST_B; }
    });
    for (int i = 0; i < arrSize; i++) {
      assertEquals(x[i] * TEST_A - TEST_B, fArr[i], Calc.EPS_16);
    }
  }

}

public void calc(Func f) {


//    fail("Test is not implemented");
}
}