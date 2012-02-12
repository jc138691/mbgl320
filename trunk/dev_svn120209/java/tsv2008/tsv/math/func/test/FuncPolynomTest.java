package math.func.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 10:54:08 */
import junit.framework.*;
import math.func.simple.FuncPolynom;
import math.func.Func;
import math.Calc;
public class FuncPolynomTest extends TestCase {
  FuncPolynom funcPolynom;
  public void testFuncPolynom() throws Exception {
    double[] coeff = {1};
    Func func = new FuncPolynom(coeff);
    double x = 1.1;
    assertEquals(coeff[0], func.calc(x), Calc.EPS_18);
  }
  public void testCalc() throws Exception {
    double[] coeff = {0.5, 2.1};
    double a = coeff[0];
    double b = coeff[1];
    Func func = new FuncPolynom(coeff);
    double x = 0.1;
    assertEquals(a + b * x, func.calc(x), Calc.EPS_18);
  }
}