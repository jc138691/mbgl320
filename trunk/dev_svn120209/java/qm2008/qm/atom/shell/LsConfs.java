package atom.shell;

import math.vec.Vec;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/06/2010,2:23:47 PM
 */
public class LsConfs extends AConfs {
private Ls ls;
public Vec getX() {
  if (size() == 0) {
    return null;
  }
  return get(0).getX();
}
public void setLs(Ls ls) {
  this.ls = ls;
}
public Ls getLs() {
  return ls;
}
public LsConf get(int idx) {
  return (LsConf)super.get(idx);
}
}
