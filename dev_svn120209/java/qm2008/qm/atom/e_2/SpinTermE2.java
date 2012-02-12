package atom.e_2;

import atom.angular.Wign3j;
import atom.e_3.FanoTermE3;
import atom.fano.FanoTermE2;
import atom.shell.DiEx;

import static math.Calc.prty;
import static math.Mathx.dlt;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,12:13:13 PM
 */
public class SpinTermE2 {
  public static DiEx calc(FanoTermE2 t, FanoTermE2 t2) {
    double d = 1.;
    if (!t.hasExc(t2)) {
      return new DiEx(d);
    }
    double e = prty(1 - t2.s());
    return new DiEx(d, e);
  }
}
