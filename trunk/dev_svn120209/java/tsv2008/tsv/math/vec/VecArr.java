package math.vec;
import javax.utilx.log.Log;
import javax.langx.SysProp;
import java.util.ArrayList;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 30/07/2008, Time: 14:06:00
 */
public class VecArr {
  public static Log log = Log.getLog(VecArr.class);
  private ArrayList<Vec> arr;
  public VecArr() {
    arr = new ArrayList<Vec>();
  }
  public VecArr(VecArr from) {
    arr = from.arr;
  }
  final public void setArr(VecArr from) {
    this.arr = from.arr;
  }
  final public int size() {
    return arr.size();
  }
  public Vec get(int i) {
    return arr.get(i);
  }
  public Vec getLast() {
    return get(size()-1);
  }
  final public void add(Vec v) {
    arr.add(v);
  }
  final public void set(int i, Vec v) {
    arr.set(i, v);
  }
  public void mult(final Vec v) {
    for (int n = 0; n < size(); n++) {
      get(n).multSelf(v);
    }
  }

  public String toString() {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < size(); i++) {
      buff.append("VecArr["+i+"]=, " + get(i) + SysProp.EOL);
    }
    return buff.toString();
  }
  public String toCSV() {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < size(); i++) {
      buff.append(get(i).toCSV() + SysProp.EOL);
    }
    return buff.toString();
  }
}

