package scatt.eng;

import math.vec.Vec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 12/02/2010, 1:18:25 PM
 */
public class EngGridFactory {
  public static Vec makeMidPoints(Vec from) {
    Vec res = new Vec(from.size() - 2);  // NOTE!!! ignore last interval
    for (int i = 0; i < res.size(); i++) {
      res.set(i, 0.5 * (from.get(i) + from.get(i+1)) );
    }
    return res;
  }
  public static Vec makeWithMidPoints(Vec fromVec) {
    double[] from = fromVec.getArr();
    double[] res = new double[2 * from.length - 1];
    int idx = 0;
    res[idx++] = from[0];
    for (int i = 1; i < from.length; i++) { // NOTE: int i = 1
      res[idx++] = 0.5 * (from[i-1] + from[i]);
      res[idx++] = from[i];
    }
    return new Vec(res);
  }
  public static Vec makeEngs(EngModelArr arr) {
    int size = 0;
    for (EngOpt model : arr) {
      size += model.getNumPoints();
    }
    double[] engs = new double[size];
    int idx = 0;
    for (EngOpt model : arr) {
      double[] grid = new EngGrid(model).getArr();
      for (int i = 0; i < grid.length; i++) {
        engs[idx++] = grid[i];
      }
    }
    return new Vec(engs);
  }
}
