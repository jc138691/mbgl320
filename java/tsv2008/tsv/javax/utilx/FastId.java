package javax.utilx;

import java.util.TreeMap;
import java.util.HashMap;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 07/04/2009, 4:44:21 PM
 */
public class FastId implements Comparable {
  public final static int FAST_ID_NONE = -1;
  private static FastIdManager idManager = FastIdManager.getInstance();
  private int id = FAST_ID_NONE;

  public FastId() {
  }
  public FastId(String from) {
    setId(from);
  }
  public FastId(FastId from) {
    setId(from);
  }
  final public String getId() {
    return idManager.getStringId(id);
  }
  final public void setId(FastId from) {
    id = from.id;
  }
  final public void setId(String from) {
    id = idManager.getId(from);
  }
  final public boolean equals(FastId to) {
    return to.id == id;
  }
  final public boolean equals(String to) {
    return id == idManager.getId(to);
  }
  final public boolean equals(Object to) {
    return equals((FastId)to);
  }
  public String toString() {
    return getId();
  }
  final protected void copyFastIdFrom(FastId from) {
    id = from.id;
  }
  public FastId[] add(FastId[] to) {
    FastId[] newArray;
    if (to == null)
      newArray = new FastId[1];
    else
      newArray = new FastId[to.length + 1];
    int i = 0;
    for (; i < to.length; i++)
      newArray[i] = to[i];
    newArray[i] = this;
    return newArray;
  }

  public int getInt() {
    return id;
  }

  public int compareTo(Object obj) {
    return id - ((FastId)obj).id;
  }
}
class FastIdManager {
  static private int nextId = 0;
  private HashMap<String, Integer> mapStrToInt = new HashMap<String, Integer>();
  private HashMap<Integer, String> mapIntToStr = new HashMap<Integer, String>();

  public synchronized int getId(String s) {
    if (s == null || s.length() < 1)
      return FastId.FAST_ID_NONE;
    Integer id = mapStrToInt.get(s);
    if (id == null) {
      id = nextId++;
      mapStrToInt.put(s, id);
      mapIntToStr.put(id, s);
    }
    return id;
  }
  public synchronized String getStringId(int i) {
    return mapIntToStr.get(new Integer(i));
  }
  //Singleton
  private static FastIdManager instance = null;
  private FastIdManager() {
  }
  final public static FastIdManager getInstance() {
    if (instance == null)
      instance = new FastIdManager();
    return instance;
  }
}

