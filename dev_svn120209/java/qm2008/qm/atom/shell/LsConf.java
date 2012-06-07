package atom.shell;
import atom.angular.AngMomRules;
import d1.IConf;
import math.vec.Vec;

import javax.utilx.log.Log;
import java.util.ArrayList;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,04/06/2010,2:10:36 PM
 */
public class LsConf implements IConf {  // this class is good for building a config
public static Log log = Log.getLog(LsConf.class);
private ArrayList<Shell> arr;
private ArrayList<Ls> lsArr;
private String keyStr;         // for any sort of mapping
private double norm;
protected LsConf(final LsConf from) {
  this.arr = from.arr;
  this.lsArr = from.lsArr;
}
protected LsConf(final LsConf from, boolean deepcopy) {
  this(from);
  if (deepcopy) {
    this.arr = (ArrayList<Shell>)from.arr.clone();
    this.lsArr = (ArrayList<Ls>)from.lsArr.clone();
  }
}
public LsConf(final Shell sh) {
  init();
  add(sh, sh.getLs());
}
public LsConf(final Shell sh, final Shell sh2, Ls totLs) {
  this(sh);
  add(sh2, totLs);
}
public double getNorm() {
  return norm;
}
public void setNorm(double norm) {
  this.norm = norm;
}
private void init() {
  arr = new ArrayList<Shell>();
  lsArr = new ArrayList<Ls>();
}
public int[] getArrQ() {
  throw new IllegalArgumentException(log.error("Use ConfFinal.getArrQ instead of LsConf.getArrQ()."));
}
public void add(Shell sh, Ls currLs) {
  arr.add(sh);
  lsArr.add(currLs);
  keyStr = null;
}
public Shell getSh(int idx) {
  if (idx < 0) {
    throw new IllegalArgumentException(log.error("idx < 0"));
  }
  if (idx >= arr.size()) {
    return null;
  }
  return arr.get(idx);
}
public Ls getLs(int idx) {
  if (idx < 0) {
    throw new IllegalArgumentException(log.error("idx < 0"));
  }
  if (idx >= lsArr.size()) {
    throw new IllegalArgumentException(log.error("idx >= lsArr.size()"));
    //      return null;
  }
  return lsArr.get(idx);
}
public Ls getTotLS() {
  if (lsArr.size() == 0) {
    throw new IllegalArgumentException(log.error("lsArr.size() == 0"));
  }
  return lsArr.get(lsArr.size() - 1);
}
public int getTotS2() {
  return getTotLS().getS2();
}
public int getTotL() {
  return getTotLS().getL();
}
public String toString() {
  StringBuffer buff = new StringBuffer();
  for (int i = 0; i < arr.size(); i++) {
    buff.append(getSh(i).toString());
    buff.append(":");
    buff.append(getLs(i).toString());
    buff.append(", ");
  }
  return buff.toString();
}
public String getKeyStr() {
  if (keyStr == null) {
    keyStr = makeKeyStr();
  }
  return keyStr;
}
private String makeKeyStr() {  // for any sort of mapping
  StringBuffer buff = new StringBuffer();
  for (int i = 0; i < arr.size(); i++) {
    buff.append(getSh(i).toString());
    buff.append(":");
    buff.append(getLs(i).toString());
    buff.append(",");
  }
  return buff.toString();
}
public int size() {
  return Math.min(arr.size(), lsArr.size());
}
public boolean isValid() {
  if (arr.size() == 0) {
    return false;
  }
  for (int i = 0; i < arr.size(); i++) {
    if (!getSh(i).isValid())
      return false;
  }
  if (!getSh(0).getLs().equals(getLs(0))) {
    log.dbg("getSh(0).getLs()=" + getSh(0).getLs());
    log.dbg("getLs(0)=" + getLs(0));
    return false;
  }
  for (int i = 0; i < arr.size() - 1; i++) {
    Ls ls = getLs(i);
    Shell sh2 = getSh(i + 1);
    Ls ls2 = getLs(i + 1);
    if (!AngMomRules.isValid(ls.getS21(), sh2.getLs().getS21(), ls2.getS21()))
      return false;
    if (!AngMomRules.isValid(ls.getL(), sh2.getLs().getL(), ls2.getL()))
      return false;
  }
  return true;
}
public Vec getX() {
  if (size() == 0) {
    return null;
  }
  Vec x = getSh(0).getWf().getX();
  return x;
}
}