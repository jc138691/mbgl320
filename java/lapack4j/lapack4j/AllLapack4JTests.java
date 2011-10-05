package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,3:29 PM
 */
public class AllLapack4JTests {
  public static void main(String[] args) {
    AllLapack4JTests runMe = new AllLapack4JTests();
    runMe.testMe();
  }
  /*
  This will test everything
   */
  public void testMe() {
    Fast2DTest.main(null);
    PythagTest.main(null);
    Tred2Test.main(null);
  }
}
