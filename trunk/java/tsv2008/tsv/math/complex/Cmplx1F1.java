package math.complex;

import flanagan.complex.Cmplx;
import junit.framework.TestCase;
import math.Calc;
import math.Mathx;
import math.vec.Vec;
import project.workflow.task.ProjectProgressMonitor;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 13/05/2010, 9:18:35 AM
 */
public class Cmplx1F1 extends FlowTest {
  public static Log log = Log.getLog(Cmplx1F1.class);
  public Cmplx1F1() {
    super(Cmplx1F1.class);
  }

  // http://en.wikipedia.org/wiki/Confluent_hypergeometric_function
  public static Cmplx calc(Cmplx a, Cmplx b, Cmplx z) {
    return calc(a, b, z, Calc.EPS_18);
  }
  public static Cmplx calc(Cmplx a, Cmplx b, Cmplx z, double eps) {
    Cmplx res = new Cmplx(1);
    double n = 1;
    Cmplx t1 = a.times(z).div(b);
    Cmplx a1 = new Cmplx(a);
    Cmplx b1 = new Cmplx(b);
    double eps2 = eps * eps;
    while (t1.abs2() > eps2) {
      res.addToSelf(t1);
      a1.addToSelf(Cmplx.ONE);
      b1.addToSelf(Cmplx.ONE);
      n++;
      t1 = t1.mult(a1).times(z).div(b1.times(n));
    }
    return res;
  }

  public void testCalc() throws Exception {
    log.dbg("testing " + Cmplx1F1.class);
    double eps = 1e-8;

    Cmplx a = new Cmplx(-0.2);
    Cmplx b = new Cmplx(0.1);
    Cmplx z = new Cmplx(0.1);
    Cmplx res = Cmplx1F1.calc(a, b, z, eps);
    Cmplx answ = new Cmplx(0.79251470);
    assertEquals(0, res.minus(answ).abs(), eps);

    a = new Cmplx(-0.1);
    res = Cmplx1F1.calc(a, b, z);
    answ = new Cmplx(0.89578277);
    assertEquals(0, res.minus(answ).abs(), eps);

    a = new Cmplx(0.5);
    b = new Cmplx(0.9);
    z = new Cmplx(10.);
    res = Cmplx1F1.calc(a, b, z);
    answ = new Cmplx(5407.1590);
    assertEquals(0, res.minus(answ).abs(), 0.5e-4);

  }
}
