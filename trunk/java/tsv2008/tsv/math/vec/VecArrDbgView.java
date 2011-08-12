package math.vec;
import javax.langx.SysProp;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/10/2008, Time: 11:57:04
 */
public class VecArrDbgView extends VecArr {
  public VecArrDbgView(VecArr from) {
    super(from);
  }
  public String toString() {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < size(); i++) {
      buff.append("VecArr["+i+"]=, " + new VecDbgView(get(i)) + SysProp.EOL);
    }
    return buff.toString();
  }
}
