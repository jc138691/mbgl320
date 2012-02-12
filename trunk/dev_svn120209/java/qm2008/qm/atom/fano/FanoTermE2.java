package atom.fano;

import atom.shell.Conf;
import atom.shell.ConfFinal;
import atom.shell.ShInfo;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,11:19:45 AM
 */
public class FanoTermE2 extends ConfFinal {
  public final double norm;
  public final int sign;
  public final int signFano;
  public final ShInfo r;
  public final ShInfo s;

  public FanoTermE2(ShInfo r, ShInfo s, Conf conf) {
    super(conf);
    this.r = r;
    this.s = s;
    int[] qs = getArrQ();
    norm = Atom2011.calcNorm(r, s, qs);
    sign = Atom2011.calcSign(r, s, qs);
    signFano = Atom2011.calcSignFano(r, s, qs);
  }

//  public boolean hasExc() {
//    return (r.idx != s.idx); // has exchange only when BOTH r!=s and r2!=s2
//  }
  public boolean hasExc(FanoTermE2 t2) {
    return (r.idx != s.idx) && (t2.r.idx != t2.s.idx); // has exchange only when BOTH r!=s and r2!=s2
  }

  public int get2R() {
    return getLs(r.idx).getS2();
  }
  public double hatSr() {
    return getLs(r.idx).hatS();
  }
  public int r() {
    return getLs(r.idx).getS();
  }

  public int get2S() {
    return getLs(s.idx).getS2();
  }
  public double hatSs() {
    return getLs(s.idx).hatS();
  }
  public int s() {
    return getLs(s.idx).getS();
  }

  public String toString() {
     return "FanoTermE2.norm=" + (float)norm + ", sign=" + sign + ", signFano=" + signFano
       + "\n, rho=" + r
       + "\n, sigma=" + s
       ;
  }
}
