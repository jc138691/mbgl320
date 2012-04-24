package scatt.jm_2008.e2;
import atom.data.AtomHy;
import atom.e_2.SysAtomE2;
import atom.energy.Energy;
import atom.shell.Conf;
import atom.shell.ConfArr;
import atom.shell.Ls;
import atom.shell.ShPair;
import atom.wf.log_cr.WFQuadrLcr;
import math.Calc;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.Scatt;
import scatt.eng.EngGrid;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.coulomb.ClmbHyBoundTest;
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
public void calcScds(int scttIdx, ScttRes scttRes, int prntN) {
  log.setDbg();
  jmF = calcFFromR();  log.dbg("jmF=\n", new MtrxDbgView(jmF));
  jmA = calcVecA();    log.dbg("vA=", new VecDbgView(jmA));

  loadClmbPsi();

  double[] engs = sdcsEngs.getArr();
  double[] engs2 = sdcsEngs.getArr();
  Mtrx resSdcs = scttRes.getSdcs();
  Mtrx mSdcs = new Mtrx(engs.length + 1,  resSdcs.getNumCols());
  scttRes.setSdcs(mSdcs);
  for (int idxA = 0; idxA < engs.length; idxA++) {
    double engA = engs[idxA];
    double engB = engs2[idxA];
    if (scttIdx == 0) { // store channels energies
      mSdcs.set(0, 0, 0);
      mSdcs.set(idxA + mthd.SDCS_CH_OFFSET, 0, engA);
    }
    double res = calcScds(idxA);
    mSdcs.set(idxA + mthd.SDCS_CH_OFFSET, scttIdx + mthd.SDCS_ENG_OFFSET, 0);
  }

  Vec vClmbH = calcClmbH();

  int dgb = 1;
}

protected double calcClmbH(ShPair clmbE2, int sysIdx) {
  // getting relevant sysEigVec
  double[][] sV = sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = sysConfH.getBasis();     // sBasis
  SysAtomE2 sysE2 = (SysAtomE2)sysConfH.getAtom();
  Energy eng;
  double res = 0;
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    Conf sysConf = sB.get(sbi);
    double term = sV[sbi][sysIdx];     //log.dbg("term=", term);
    if (Calc.isZero(term))
      continue;

    eng = sysE2.calcTwoPot(sysConf, clmbE2);         //log.dbg("eng=", eng);
    double a = eng.kin + eng.pot;              //log.dbg("s=", s);

    res += ( term * a );
  }
  return res;
}

protected double calcScds(int idxEngA) {
  Ls LS = mthd.sysConfH.getBasis().getLs();


  int sN = mthd.getSysBasisSize();
  double res = 0;
  double[] sysE = mthd.getSysEngs().getArr();
  for (int sysIdx = 0; sysIdx < sN; sysIdx++) {
    double ei = sysE[sysIdx];
    double ch = calcClmbH(sysIdx);
    double ah = ch * jmA.get(sysIdx);
    res += ah;
  }

  double norm = Scatt.calcSdcsNormE2E_todo(
    sdcsEngs.get(idxEngA), sdcsEngs2.get(idxEngA)
    , mthd.getScttE());
  res *= norm;
  return res;
}

protected void loadClmbPsi() {
  int L = 0;
  double sysTotE = mthd.getSysTotE();
  if (sysTotE <= 0) { // not enough energy for ionization
    return;
  }
  WFQuadrLcr quadr = mthd.getQuadrLcr();
  makeScdsEngs();
  clmbPsi = new JmClmbLcr(L, AtomHy.Z, sdcsEngs, sysTotE, quadr);      log.dbg("clmbPsi=\n", clmbPsi);
  clmbPsi2 = new JmClmbLcr(L, AtomHy.Z, sdcsEngs2, sysTotE, quadr);      log.dbg("clmbPsi2=\n", clmbPsi2);
  if (!new ClmbHyBoundTest(clmbPsi, mthd.trgtE2.getStatesE1()).ok())
    return;
  if (!new ClmbHyBoundTest(clmbPsi2, mthd.trgtE2.getStatesE1()).ok())
    return;

  int stopped = 1;
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
