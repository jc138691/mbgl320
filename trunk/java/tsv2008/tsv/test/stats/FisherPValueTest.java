package stats;

import math.Calc;
import junit.framework.TestCase;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 02/10/2009, 12:52:10 PM
 */
public class FisherPValueTest extends TestCase {
  private static final int N_TESTS = 15;

  public void testFactlLog() throws Exception {
    int[][] argInts = new int[N_TESTS][4];
    argInts[0] = new int[]{2, 3, 6, 4};
    argInts[1] = new int[]{2, 1, 3, 0};
    argInts[2] = new int[]{3, 0, 2, 1};
    argInts[3] = new int[]{1, 2, 0, 3};
    argInts[4] = new int[]{3, 1, 1, 3};
    argInts[5] = new int[]{1, 3, 3, 1};
    argInts[6] = new int[]{0, 1, 1, 0};
    argInts[7] = new int[]{1, 0, 0, 1};
    argInts[8] = new int[]{11, 0, 0, 6};
    argInts[9] = new int[]{10, 1, 1, 5};
    argInts[10] = new int[]{5, 6, 6, 0};
    argInts[11] = new int[]{9, 2, 2, 4};
    argInts[12] = new int[]{6, 5, 5, 1};
    argInts[13] = new int[]{8, 3, 3, 3};
    argInts[14] = new int[]{7, 4, 4, 2};
    double res[] = {0.6083916083916093
      , 0.9999999999999999
      , 0.9999999999999999
      , 0.9999999999999999
      , 0.4857142857142845
      , 0.4857142857142845
      , 1.0
      , 1.0
      , 8.080155138978646E-5
      , 0.005413703943115701
      , 0.04274402068519691
      , 0.10940530058177061
      , 0.33338720103425773
      , 0.6000323206205534
      , 0.9999999999999954
    };
    for (int i = 0; i < argInts.length; i++) {
      System.out.println("\na=" + argInts[i][0] + " b=" + argInts[i][1] + " c=" + argInts[i][2] + " d=" + argInts[i][3]);
      double twoTailedP = FisherPValue.getTwoTailedP(argInts[i][0], argInts[i][1], argInts[i][2], argInts[i][3]);
      assertEquals(res[i], twoTailedP, Calc.EPS_13);
    }
  }
}