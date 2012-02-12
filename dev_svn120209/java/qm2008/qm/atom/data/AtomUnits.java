package atom.data;
import math.func.Func;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,24/03/11,3:40 PM
 */
public class AtomUnits {
  // Accessed 25Mar2011, http://physics.nist.gov/cuu/Constants/index.html
  // P. J. Mohr, B. N. Taylor, and D. B. Newell, Rev. Mod. Phys 80(2), 633-730(2008)
  public static final double ENERGY_TO_EV = 27.21138386;
  public static final double HARTREE_TO_EV = ENERGY_TO_EV;
  public static final double ENERGY_FROM_EV = 1. / ENERGY_TO_EV;

  public static final Func funcToEV = new Func() {
    public double calc(double au) {
      return au * ENERGY_TO_EV;
    }
  };

  public static double fromEV(double ev) {
    return ev * ENERGY_FROM_EV;
  }
  public static double toEV(double au) {
    return au * ENERGY_TO_EV;
  }

}
