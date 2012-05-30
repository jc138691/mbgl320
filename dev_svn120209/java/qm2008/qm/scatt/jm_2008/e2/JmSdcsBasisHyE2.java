package scatt.jm_2008.e2;
import atom.data.AtomHy;
import atom.shell.*;
import atom.wf.lcr.WFQuadrLcr;
import flanagan.complex.Cmplx;
import math.complex.CmplxMtrx;
import math.complex.CmplxMtrxDbgView;
import math.func.FuncVec;
import math.func.arr.FuncArrDbgView;
import math.mtrx.Mtrx;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.Scatt;
import scatt.eng.EngGrid;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.coulomb.ClmbHyBoundTest;
import scatt.partial.wf.JmClmbLcr;

import javax.utilx.arraysx.DbleArr;
import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 20/04/12, 9:34 AM
 */
public class JmSdcsBasisHyE2 extends JmKatoBasisHyE2 {
public static Log log = Log.getLog(JmSdcsBasisHyE2.class);
private JmClmbLcr clmbPsi;
private JmClmbLcr clmbPsi2;
private Vec sdcsEngs;
private Vec sdcsEngs2;
private Mtrx mSdcs;
private int ID_A_OFFSET = 1;
private int ID_B_OFFSET = 2;

public JmSdcsBasisHyE2(JmMthdBasisHyE2 jmMthdBasisHyE2) {
  super(jmMthdBasisHyE2);
}
public void calcScds(int scttIdx, ScttRes scttRes, int prntN) {
  log.setDbg();

  jmCF = calcCFFromR();  log.dbg("jmF[0]=\n", new CmplxMtrxDbgView(new CmplxMtrx(jmCF[0])));
  jmCA = calcVecCA();    log.dbg("jmA=", new CmplxMtrxDbgView(new CmplxMtrx(jmCA)));

  Mtrx resSdcs = scttRes.getSdcs();
//  makeScdsEngGrid(resSdcs);
  makeScdsTrgtEngs(resSdcs);
  log.dbg("sdcsEngs=", new VecDbgView(sdcsEngs));
  log.dbg("sdcsEngs2=", new VecDbgView(sdcsEngs2));

  loadClmbPsi();
  loadKatoLgrr();

  double[] engs = sdcsEngs.getArr();
  scttRes.setSdcs(mSdcs);
  for (int idxA = 0; idxA < engs.length; idxA++) {
    double engA = engs[idxA];
//    if (scttIdx == 0) { // store channels energies
    mSdcs.set(0, 0, 0);
    mSdcs.set(idxA + mthd.SDCS_CH_OFFSET, 0, engA);
//    }
    double res = calcScds(idxA);      log.dbg("calcScds res=", res);
    mSdcs.set(idxA + mthd.SDCS_CH_OFFSET, scttIdx + mthd.SDCS_ENG_OFFSET, res);
  }
}
protected double calcScds(int idxEngA) {
  int N = mthd.getN();
  int L = 0;
  Ls LS = mthd.sysConfH.getBasis().getLs();
  FuncVec cA = clmbPsi.get(idxEngA);
  FuncVec cB = clmbPsi2.get(idxEngA);
  int ID_E2_A = ID_A_OFFSET + N;
  int ID_E2_B = ID_B_OFFSET + N;  // NOTE!!! This will only work for E2!!! TODO: Check this if doing E3+
  Conf clmbE2 = ShPairFactory.makePair(cA, ID_E2_A, L, cB, ID_E2_B, L, LS);

//  if (DEBUG_JM1) {
//    ConfArr sB = mthd.sysConfH.getBasis();     // sBasis
//    clmbE2 = sB.get(idxEngA);
//  }

  vHEBasis = calcHEBasis(clmbE2); // different for each idxEngA
  log.dbg("vHEBasis=", new VecDbgView(vHEBasis));

  Cmplx partCA = calcSumCA(clmbE2);
  Cmplx amplC = partCA;

//  Cmplx partCK = calcSumCK(clmbE2);
//  Cmplx amplC = partCA.add(partCK);

  double norm = Scatt.calcSdcsNormE2E(
    sdcsEngs.get(idxEngA), sdcsEngs2.get(idxEngA)
    , mthd.getScttE());  log.dbg("calcScds norm=", norm);
  double resC = norm * amplC.abs2();   log.dbg("calcScds resC=", resC);
  return resC;
}

protected void loadClmbPsi() {
  int L = 0;
  double sysTotE = mthd.getSysTotE();
  if (sysTotE <= 0) { // not enough energy for ionization
    return;
  }
  WFQuadrLcr quadr = mthd.getQuadr();
  clmbPsi = new JmClmbLcr(L, AtomHy.Z, sdcsEngs, sysTotE, quadr);    log.dbg("clmbPsi=\n", new FuncArrDbgView(clmbPsi));
  clmbPsi2 = new JmClmbLcr(L, AtomHy.Z, sdcsEngs2, sysTotE, quadr);  log.dbg("clmbPsi2=\n", new FuncArrDbgView(clmbPsi2));
  if (!new ClmbHyBoundTest(clmbPsi, mthd.trgtE2.getStatesE1()).ok())
    return;
  if (!new ClmbHyBoundTest(clmbPsi2, mthd.trgtE2.getStatesE1()).ok())
    return;
}
private void makeScdsEngGrid(Mtrx resSdcs) {
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

  if (mSdcs == null) {   // create this mtrx only once
    mSdcs = new Mtrx(engs.length + 1,  resSdcs.getNumCols());
  }

}
private void makeScdsTrgtEngs(Mtrx resSdcs) {
  double sysTotE = mthd.getSysTotE();
  double maxE = sysTotE;
//  double maxE = sysTotE / 2;

  DbleArr engs = new DbleArr();
  DbleArr engs2 = new DbleArr();

  Vec tEngs = mthd.trgtE2.getEngs();
  for (int chIdx = 0; chIdx < tEngs.size(); chIdx++) {
    double chE = tEngs.get(chIdx); // channel eng
    if (chE <= mthd.trgtE2.getIonGrndEng()) {
      continue;
    }
    if (chE >= maxE) {
      break;
    }
    engs.add(chE);
    engs2.add(sysTotE - chE);
  }
  sdcsEngs = new Vec(engs.toArray());
  sdcsEngs2 = new Vec(engs2.toArray());

  if (mSdcs == null) {   // create this mtrx only once
    int trgtN = mthd.calcNumTrgtCont();
    mSdcs = new Mtrx(trgtN + 1,  resSdcs.getNumCols());
  }
}
}
