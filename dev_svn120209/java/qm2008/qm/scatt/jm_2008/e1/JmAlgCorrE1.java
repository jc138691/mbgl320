package scatt.jm_2008.e1;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.jm.ScttRes;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 1:16 PM
 */
public class JmAlgCorrE1 extends JmAlgKatoE2 {
public static Log log = Log.getLog(JmAlgCorrE1.class);
public JmAlgCorrE1(JmMthdBaseE2 mthd) {
  super(mthd);
}
public Mtrx calcNewR() {
  log.setDbg();
  jmF = calcFFromR();  log.dbg("jmF[0]=\n", new MtrxDbgView(new Mtrx(jmF[0])));
  jmA = calcVecA();    log.dbg("jmA=", new MtrxDbgView(new Mtrx(jmA)));
  loadKatoLgrr();


  // STOPPED
  // calcChPsiReg

  return mthd.jmR; //TODO
}
}
