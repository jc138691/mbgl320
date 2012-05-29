package scatt.jm_2008.e1;
import math.mtrx.Mtrx;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 11:03 AM
 */
public class JmMthdCorrE1 extends JmMthdE1 {
public static Log log = Log.getLog(JmMthdCorrE1.class);
private JmAlgCorrE1 jmCorr;
public JmMthdCorrE1(CalcOptE1 calcOpt) {
  super(calcOpt);
}
protected Mtrx calcCorrR() {
  if (jmCorr == null) {
    jmCorr = new JmAlgCorrE1(this);
  }
  return jmCorr.calcNewR();
}
}
