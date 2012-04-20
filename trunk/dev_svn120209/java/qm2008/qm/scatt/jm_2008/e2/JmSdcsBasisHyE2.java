package scatt.jm_2008.e2;
import atom.data.AtomHy;
import atom.wf.log_cr.WFQuadrLcr;
import com.sun.xml.internal.bind.v2.TODO;
import math.Mathx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.DbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import papers.hy_swave.ees_bad.EesMethodE1;
import scatt.eng.EngGrid;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.JmCh;
import scatt.partial.wf.JmClmbLcr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 20/04/12, 9:34 AM
 */
public class JmSdcsBasisHyE2 extends JmKatoBasisHyE2 {
public static Log log = Log.getLog(JmSdcsBasisHyE2.class);
private JmClmbLcr clmbPsi;
private JmClmbLcr clmbPsi2;
private EngGrid sdcsEngs;
private EngGrid sdcsEngs2;

public JmSdcsBasisHyE2(JmMthdBasisHyE2 jmMthdBasisHyE2) {
  super(jmMthdBasisHyE2);
}
public void calcScds(int i, ScattRes res, int showNum) {
  log.setDbg();
  jmF = calcFFromR();     log.dbg("jmF=\n", new MtrxDbgView(jmF));
  Vec vA = calcVecA();    log.dbg("vA=", new VecDbgView(vA));

  loadClmbPsi();
  int dgb = 1;
}

protected void loadClmbPsi() {
  int L = 0;
  double sysTotE = mthd.getSysTotE();
  if (sysTotE <= 0) { // not enough energy for ionization
    return;
  }
  WFQuadrLcr quadr = mthd.getOrthonN().getQuadr();
  makeScdsEngs();
  clmbPsi = new JmClmbLcr(L, AtomHy.Z, sdcsEngs, sysTotE, quadr);      log.dbg("clmbPsi=\n", clmbPsi);
  clmbPsi2 = new JmClmbLcr(L, AtomHy.Z, sdcsEngs2, sysTotE, quadr);      log.dbg("clmbPsi2=\n", clmbPsi2);
  TODO test against bound states
}
private void makeScdsEngs() {
  CalcOptE1 calcOpt = mthd.getCalcOpt();
  int engN = calcOpt.getSdcsEngN();
  double sysTotE = mthd.getSysTotE();
  double maxE = sysTotE / 2;
  double minE = maxE / engN;
  sdcsEngs = new EngGrid(new EngModel(minE, maxE, engN));
  sdcsEngs2 = new EngGrid(new EngModel(minE, maxE, engN));
  double[] engs = sdcsEngs2.getArr();
  for (int i = 0; i < engs.length; i++) {
    engs[i] = sysTotE - engs[i];
  }
  log.dbg("sdcsEngs=", new VecDbgView(sdcsEngs));
  log.dbg("sdcsEngs2=", new VecDbgView(sdcsEngs2));
}
}
