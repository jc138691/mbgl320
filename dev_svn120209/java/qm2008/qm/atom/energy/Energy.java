package atom.energy;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 11:48:46
 */
public class Energy {
public double di = 0;  // direct
public double ex = 0;  // exchange
public double kin = 0;
public double pt = 0;  // potential total
public double p1 = 0;  // this stores potential electron-z
public double p2 = 0;  // this stores potential electron-electron
public Energy(Energy from) {
  this.di = from.di;
  this.ex = from.ex;
  this.kin = from.kin;
  this.pt = from.pt;
  this.p1 = from.p1;
  this.p2 = from.p2;
}
public Energy() {
}

public void add(Energy e) {
  if (e == null)
    return;
  di += e.di;
  ex += e.ex;
  kin += e.kin;
  pt += e.pt;
  p1 += e.p1;
  p2 += e.p2;
}

public void timesSelf(double x) {
  di *= x;
  ex *= x;
  kin *= x;
  pt *= x;
  p1 *= x;
  p2 *= x;
}
public String toString() {
  return
    "kin=" + (float)kin
    + ", pt=" + (float) pt
    + ", p1=" + (float) p1
      + ", p2=" + (float) p2
      + ", di=" + (float) di
      + ", ex=" + (float) ex
    + ", tot=" + (float)(kin + pt);
}

}