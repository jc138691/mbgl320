package scatt.jm_2008.jm.target;
import atom.energy.AConfHMtrx;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,5:22 PM
 */
public class ChConf { // target channel with two or more electrons
  public final int fromIdx;
  public final double eng;
  public final AConfHMtrx hMtrx;
  public ChConf(int i, double eng, AConfHMtrx h) {
    this.fromIdx = i;
    this.eng = eng;
    this.hMtrx = h;
  }
}
