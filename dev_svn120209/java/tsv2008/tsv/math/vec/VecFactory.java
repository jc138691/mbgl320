package math.vec;
import javax.utilx.RandomSeed;
import javax.utilx.arraysx.DbleArr;
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

public static Vec reverse(Vec fromVec) { // reverse
  Vec res = new Vec(fromVec.size());
  double[] arr = res.getArr();
  double[] from = fromVec.getArr();
  for (int i = 0; i < arr.length; i++) {
    arr[i] = from[arr.length - i - 1];
  }
  return res;
}
public static Vec crop(Vec from, double first, double last) {
  DbleArr res = new DbleArr();
  for (int i = 0; i < from.size(); i++) {
    double v = from.get(i);
    if (first <= v  && v <= last) {
      res.add(v);
    }
  }
  return new Vec(res.toArray());
}
}
