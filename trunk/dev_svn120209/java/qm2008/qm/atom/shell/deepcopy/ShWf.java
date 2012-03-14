package atom.shell.deepcopy;

import math.func.FuncVec;
import math.func.arr.FuncArr;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 03/06/2010, 10:26:48 AM
 */
public class ShWf extends FuncVec {    // partial wave function
  private ShId id;

  public ShWf(int idx, int L, FuncVec rwf) {
    super(rwf);
    id = new ShId(idx, L);
  }

  public ShId getId() {
    return id;
  }
  public static void convertToShWf(FuncArr arr, int L) {
    for (int idx = 0; idx < arr.size(); idx++) {
      FuncVec f =  arr.getFunc(idx);
      if (f instanceof ShWf) {
        throw new IllegalArgumentException(log.error("f instanceof ShWf"));
      }
      ShWf wf = new ShWf(idx, L, f);
      arr.set(idx, wf);
    }
  }

  public int getL() {
    return id.getL();
  }

  public int getIdx() {
    return id.getIdx();
  }
}
