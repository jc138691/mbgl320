package math.vec;
import javax.utilx.RandomSeed;
import java.util.List;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/08/11,11:31 AM
 */
public class VecFactory {
  public static double[] makeRand(int size) {
    RandomSeed rand = RandomSeed.getInstance();
    double[] res = new double[size];
    for (int i = 0; i < size; i++) {
      res[i] = rand.nextDouble();
    }
    return res;
  }

}
