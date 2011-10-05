package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,4:01 PM
 */
public class DblArrTest extends TestUtils {
  public static  void assertEquals(double[] expectedArr, double[] resultArr, int len) {
    for (int i = 0; i < len; i++) {
      assertEquals(expectedArr[i], resultArr[i]);
    }
  }
  public static  boolean testEquals(double[] expectedArr, double[] resultArr, int len) {
    if (expectedArr == resultArr) {
      throw new AssertionError("expectedArr == resultArr");
    }
    for (int i = 0; i < len; i++) {
      if (expectedArr[i] != resultArr[i])
        return false;
    }
    return true;
  }
}
