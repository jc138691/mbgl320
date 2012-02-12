package math.vec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 05/03/2010, 11:49:05 AM
 */
public class TVec<T> {
  private int size = -1;
  protected T[] vec;

  public TVec(T[] from) {
    setArr(from);
  }

  public T[] getArr() {
    return vec;
  }

  public void setArr(T[] arr) {
    vec = arr;
    size = arr.length;
  }

  public T get(int i) {
    return vec[i];
  }

  public void set(int i, T v) {
    vec[i] = v;
  }

  public int size() {
    return size;
  }

  public String toString() {
    return toString(0, size());
  }

  public String toDbgString() {
    if (DbgView.getNumShow() >= size()) {
      return toString(0, size());
    }
    int n = DbgView.getNumShow() / 2;
    String start = toString(0, n);
    String tail = toString(size() - n, n);
    String head = "Vec[" + size() + "] = {";
    return head + start + ", ..., " + tail + "}";
  }

  // overide this if do not want the default toString()

  public String toString(T elem) {
    return elem.toString();
  }

  public String toString(int firstIdx, int size) {
    int L = Math.min(size(), firstIdx + size);
    StringBuffer buff = new StringBuffer();
    for (int i = firstIdx; i < L; i++) {
      DbgView.append(buff, toString(get(i)));
      if (i != L - 1)
        buff.append(", ");
    }
    return buff.toString();
  }

}
