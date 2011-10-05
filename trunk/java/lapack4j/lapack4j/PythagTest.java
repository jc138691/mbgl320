package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,3:21 PM
 */
public class PythagTest extends BaseTest {
  public static void main(String[] args) {
    PythagTest runMe = new PythagTest();
    runMe.testMe();
  }

  public void testMe() {
    testMe(0, 0);
    testMe(1, 1);
    testMe(1, 1.1);
    testMe(1.1, 1);
    testMe(0, 1);
    testMe(1, 0);
    testMe(1, 2);
    testMe(2, 1);
    testMe(2, -1);
    testMe(-2, -1);
    testMe(-2, 1.2);
    testMe(1, 100);
    testMe(-1011, 1);
    testMe(-1011, 0.00000001);
    testMe(-21.1, 0.002);
    testMe(0.01333, -0.002);
  }
  public void testMe(double a, double b) {
    double res = pythag.pythag(a, b);
    double ex = Math.sqrt(a * a + b * b);
    testMaxAbsErr(ex, res);
  }
}
