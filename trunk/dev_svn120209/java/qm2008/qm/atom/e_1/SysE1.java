package atom.e_1;
import atom.AtomFano1965;
import atom.energy.Energy;
import atom.energy.slater.Slater;
import atom.shell.Conf;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:02:44
 */
public class SysE1 extends AtomFano1965 {
  private final static int ONE_ELEC = 1;
  public SysE1(double z
    , Slater si) { // grid/basisN dependent!!!
    super(z, si);
  }
  public int getNumElec() {
    return ONE_ELEC;
  }
  public Energy calcH(Conf fc, Conf fc2) {
    assertLS(fc, fc2);
    Energy res = new Energy();
    res.kin = calcOneKin(fc.getSh(0), fc2.getSh(0));
    res.pt = calcOnePotZ(fc.getSh(0), fc2.getSh(0)); // potential only
    return res;
  }
  public double calcOverlap(Conf fc, Conf fc2) {
    assertLS(fc, fc2);
    double res = calcOverlap(fc.getSh(0), fc2.getSh(0));
    return res;
  }
}

