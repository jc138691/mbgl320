package atom;
import math.Calc;
import math.vec.Vec;
import math.func.arr.FuncArr;
import atom.energy.Energy;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 12:06:32
 */
public class AtomUtil extends Calc {

  public static void trimTailSLOW(Vec f) {
    int newSize = f.size();
    for (int i = f.size() - 1; i >= 0; i--) {
      if (Calc.isZero(f.get(i)))  {
        newSize--;
        f.set(i, 0);
      }
      else
        break;
    }
    f.setSize(newSize);
  }
  public static void trimTailSLOW(FuncArr arr) {
    for (int i = 0; i < arr.size(); i++) {
      trimTailSLOW(arr.get(i));
    }
  }
//  public static void setTailFrom(FuncArr arr, FuncArr fromArr) {
//    for (int i = 0; i < arr.size(); i++) {
//      arr.get(i).setSize(fromArr.get(i).size());
//    }
//  }

}
