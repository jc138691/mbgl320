package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,3:22 PM
 */
public class Dbl2DTest extends DblArrTest {
  public static void main(String[] args) {
    Dbl2DTest runMe = new Dbl2DTest();
    runMe.testMe();
  }
  public void testMe() {
    testIn("Dbl2DTest");
    testMe(1, 1);
    testMe(2, 2);
    testMe(2, 3);
    testMe(3, 2);
    for (int nRows = 1; nRows < 20; nRows++) {
      monitor(nRows);
      for (int nCols = 1; nCols < 20; nCols++) {
        testMe(nRows, nCols);
      }
    }
    testOk("Dbl2DTest");
  }
  public void testMe(int nRows, int nCols) {
    int minSize = Math.min(nRows, nCols);
    double[][] from = Dbl2DFactory.makeRand(nRows, nCols);
    double[][] to = Dbl2DFactory.makeRand(nRows, nCols);
    if (testEquals(from, to, nRows, nCols)) {
      print("from=" + Dbl2D.toString(from, nRows, nCols));
      print("to  =" + Dbl2D.toString(to, nRows, nCols));
      throw new AssertionError("testEquals(from, to, nRows, nCols)");
    }
    Dbl2D.copyFast(from, to, nRows, nCols);
    assertEquals(from, to, nRows, nCols);
  }
  public static  void assertEquals(double[][] expectedArr, double[][] resultArr, int nRows, int nCols) {
    for (int i = 0; i < nRows; i++) {
      assertEquals(expectedArr[i], resultArr[i], nCols);
    }
  }
  public static  boolean testEquals(double[][] expectedArr, double[][] resultArr, int nRows, int nCols) {
    if (expectedArr == resultArr) {
      throw new AssertionError("expectedArr == resultArr");
    }
    for (int i = 0; i < nRows; i++) {
      if (!testEquals(expectedArr[i], resultArr[i], nCols))
        return false;
    }
    return true;
  }

}
