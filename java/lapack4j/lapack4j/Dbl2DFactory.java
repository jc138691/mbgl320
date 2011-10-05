package lapack4j;
import java.util.Date;
import java.util.Random;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,3:37 PM
 */
public class Dbl2DFactory {
  private static Random rand = new Random(new Date().getTime());
  public static double[][] makeRand(int nRows, int nCols) {
    double[][] res = new double[nRows][nCols];
    for (int r = 0; r < res.length; r++) {
      double[] re = res[r];
      for (int c = 0; c < re.length; c++) {
        re[c] = rand.nextDouble();
      }
    }
    return res;
  }
}
