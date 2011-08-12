package stats;

import math.func.FactLn;
/**
 * Adopted from FisherExact.java, see http://sourceforge.net/projects/tassel/
 */
public class FisherPValue {
  private static FactLn f = FactLn.getInstance();

  private static double getP(int a, int b, int c, int d) {
    int n = a + b + c + d;
    double p = (f.calc(a + b) + f.calc(c + d) + f.calc(a + c) + f.calc(b + d))
      - (f.calc(a) + f.calc(b) + f.calc(c) + f.calc(d) + f.calc(n));
    return Math.exp(p);
  }

  public static double getTwoTailedP(int a, int b, int c, int d) {
    double p = 0;
    double baseP = getP(a, b, c, d);
    int initialA = a, initialB = b, initialC = c, initialD = d;
    p += baseP;
    int min = (c < b) ? c : b;
    for (int i = 0; i < min; i++) {
      double tempP = getP(++a, --b, --c, ++d);
      if (tempP <= baseP) {
        p += tempP;
      }
    }

    // reset the values to their original so we can repeat this process for the other side
    a = initialA;
    b = initialB;
    c = initialC;
    d = initialD;
    min = (a < d) ? a : d;
    for (int i = 0; i < min; i++) {
      double pTemp = getP(--a, ++b, ++c, --d);
      if (pTemp <= baseP) {
        p += pTemp;
      }
    }
    return p;
  }

  public static void main(String[] args) {

    int[][] argInts = new int[15][4];
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

    for (int i = 0; i < argInts.length; i++) {
      System.out.println("\na=" + argInts[i][0] + " b=" + argInts[i][1] + " c=" + argInts[i][2] + " d=" + argInts[i][3]);
      double twoTailedP = FisherPValue.getTwoTailedP(argInts[i][0], argInts[i][1], argInts[i][2], argInts[i][3]);
      System.out.println("\ttwoTailedP = " + twoTailedP);
    }
  }
}
