package javax.utilx.arraysx;

import javax.langx.SysProp;
import java.util.ArrayList;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 03/06/2010, 10:47:27 AM
 */
public class TArr<T> {
  private ArrayList<T> arr = new ArrayList<T>();

  public String toString() {
    StringBuffer buff = new StringBuffer();
    buff.append(SysProp.EOL);
    for (int i = 0; i < arr.size(); i++) {
      buff.append(getClass().toString() + "[" + i).append("]=");
      buff.append(arr.get(i).toString());
      buff.append(SysProp.EOL);
    }
    return buff.toString();
  }

  public T get(int idx) {
    return arr.get(idx);
  }

//  public void set(int idx, T el) {
//    arr.set(idx, el);
//  }

  public int size() {
    return arr.size();
  }

  public void add(T fc) {
    arr.add(fc);
  }

  public void addAll(TArr<T> from) {
    arr.addAll(from.arr);
  }
}
