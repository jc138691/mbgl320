package math.complex.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 1/09/2008, Time: 13:26:21 */
import flanagan.complex.Cmplx;
import junit.framework.*;

public class CmplxTest extends TestCase {
  public void testMult() throws Exception {
    Cmplx a = new Cmplx(-9.4, -6.2);
    Cmplx b = new Cmplx(2.5, 1.5);
    Cmplx c = a.mult(b);
    assertEquals("Re(c)=", c.getRe(), -14.2);
    assertEquals("Im(c)=", c.getIm(), -29.6);
  }
  public void testDiv() throws Exception {
    Cmplx a = new Cmplx(7, -2);
    Cmplx b = new Cmplx(3, 4);
    Cmplx c = a.div(b);
    assertEquals("Re(c)=", c.getRe(), 13. / 25.);
    assertEquals("Im(c)=", c.getIm(), -34. / 25.);
  }
}