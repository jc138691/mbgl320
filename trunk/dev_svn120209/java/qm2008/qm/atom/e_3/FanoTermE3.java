package atom.e_3;

import atom.fano.FanoTermE2;
import atom.shell.LsConf;
import atom.shell.ShInfo;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,09/11/2010,1:43:24 PM
 */
public class FanoTermE3 extends FanoTermE2 {
  public final ShInfo b;

  public FanoTermE3(ShInfo b, ShInfo r, ShInfo s, LsConf conf) {
    super(r, s, conf);
    this.b = b;
  }

  public int get2B() {
    return getLs(b.idx).getS2();
  }
  public double hatSb() {
    return getLs(b.idx).hatS();
  }
  public int b() {
    return getLs(b.idx).getS();
  }

  public String toString() {
     return super.toString()
       + "\n, b (lambda)=" + b
       ;
  }
}
