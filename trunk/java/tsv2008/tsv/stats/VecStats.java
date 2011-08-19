package stats;
import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/08/11,10:33 AM
 */
public class VecStats {
  public static Log log = Log.getLog(VecStats.class);
  public static double rmse(double[] x, double[] y, int len)
  {
    if (x == null  ||  y == null) {
      throw new IllegalArgumentException(log.error("(x == null  ||  y == null)"));
    }
    if (x.length < len  ||  y.length < len) {
      throw new IllegalArgumentException(log.error("x.length < len  ||  y.length < len"));
    }
    if (len <= 0) {
      throw new IllegalArgumentException(log.error("(len <= 0)"));
    }
    double res = 0;
    for (int i = 0; i < len; i++) {
      double v = x[i] - y[i];
      res += (v * v);
    }
    return Math.sqrt(res / (double)len);
  }
}
