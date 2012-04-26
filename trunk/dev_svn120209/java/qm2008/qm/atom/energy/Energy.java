package atom.energy;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 11:48:46
 */
public class Energy {
public double kin = 0;
public double pot = 0;
public double pot2 = 0;  // this stores electron-electron
public Energy(Energy from) {
  this.kin = from.kin;
  this.pot = from.pot;
  this.pot2 = from.pot2;
}
public Energy() {
}
public Energy(double v, double v2) { // Only used in
  kin = v;
  pot = v2;
}

public void add(Energy e) {
  if (e == null)
    return;
  kin += e.kin;
  pot += e.pot;
  pot2 += e.pot2;
}

public void times(double x) {
  kin *= x;
  pot *= x;
  pot2 *= x;
}
public String toString() {
  return "kin=" + (float)kin
    + ", pot=" + (float)pot
    + ", pot2=" + (float)pot2
    + ", tot=" + (float)(kin + pot);
}

}