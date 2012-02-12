package atom.energy;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 11:48:46
 */
public class Energy {
  public double kin = 0;
  public double pot = 0;
  public Energy(Energy from) {
    this.kin = from.kin;
    this.pot = from.pot;
  }
  public Energy() {
  }
  public Energy(double v, double v2) {
    kin = v;
    pot = v2;
  }

  public void add(Energy e) {
    if (e == null)
      return;
    kin += e.kin;
    pot += e.pot;
  }

  public void times(double x) {
    kin *= x;
    pot *= x;
  }
  public String toString() {
    return "kin=" + (float)kin + ", pot=" + (float)pot + ", tot=" + (float)(kin + pot);
  }

}