package math.func.arr;
import math.vec.VecDbgView;
import math.vec.VecArrDbgView;

import javax.langx.SysProp;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/10/2008, Time: 11:53:59
 */
public class FuncArrDbgView extends FuncArr {
  public FuncArrDbgView(FuncArr from) {
    super(from);
  }
  public String toString() {
    return "x=" + new VecDbgView(getX()) + SysProp.EOL + new VecArrDbgView(this);
  }

}
