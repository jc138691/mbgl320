package scatt.jm_2008.e3;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,4/04/11,9:55 AM
 */
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.jm.target.ScttTrgtE3;
import scatt.jm_2008.jm.target.ScttTrgtE2;

import javax.utilx.log.Log;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,4:54 PM
 */
public abstract class JmMethodBaseE3 extends JmMthdBaseE2 {   // many-electrons (three or more)
  public static Log log = Log.getLog(JmMethodBaseE3.class);
  protected ScttTrgtE3 trgtE3;
  public JmMethodBaseE3(CalcOptE1 calcOpt) {
    super(calcOpt);
  }

  public ScttTrgtE3 getTrgtE3() {
    return trgtE3;
  }
  public void setTrgtE3(ScttTrgtE3 trgtE3) {
    this.trgtE3 = trgtE3;
    this.trgtE2 = trgtE3;
  }

  @Override
  public void setTrgtE2(ScttTrgtE2 trgtE2) {
    throw new IllegalArgumentException(log.error("call setTrgtE3 instead"));
  }

}