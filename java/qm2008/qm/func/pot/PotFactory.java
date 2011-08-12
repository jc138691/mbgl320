package func.pot;
import math.func.FuncVec;
import math.vec.Vec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/12/2008, Time: 10:52:35
 */
public class PotFactory {
  public static FuncVec makeWentzel(Vec r, double Z, double a) {
    FuncVec res = new FuncVec(r);//todo
//    FuncVec res = makePotHy_1s(r);
    res.mult(-1);
    return res;
  }

}
