package atom.shell;

import math.vec.Vec;

import javax.utilx.arraysx.TArr;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/06/2010,2:23:47 PM
 */
public class ConfArr extends TArr<Conf> {
  public Vec getX() {
    if (size() == 0) {
      return null;
    }
    return get(0).getX();
  }
}
