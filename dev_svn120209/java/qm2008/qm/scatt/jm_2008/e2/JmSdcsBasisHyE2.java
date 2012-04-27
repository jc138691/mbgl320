package scatt.jm_2008.e2;
import atom.data.AtomHy;
import atom.e_2.SysAtomE2;
import atom.energy.Energy;
import atom.shell.*;
import atom.wf.log_cr.WFQuadrLcr;
import math.Calc;
import math.Mathx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
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
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LagrrLcr;
import scatt.jm_2008.jm.target.JmCh;
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
private Vec vClmbVBasis;
private Mtrx mSdcs;

public JmSdcsBasisHyE2(JmMthdBasisHyE2 jmMthdBasisHyE2) {
  super(jmMthdBasisHyE2);
}
public void calcScds(int scttIdx, ScttRes scttRes, int prntN) {
  log.setDbg();
  jmF = calcFFromR();  log.dbg("jmF=\n", new MtrxDbgView(jmF));
  jmA = calcVecA();    log.dbg("vA=", new VecDbgView(jmA));

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
    if (scttIdx == 0) { // store channels energies
      mSdcs.set(0, 0, 0);
      mSdcs.set(idxA + mthd.SDCS_CH_OFFSET, 0, engA);
    }
    double res = calcScds(idxA);      log.dbg("calcScds res=", res);
    mSdcs.set(idxA + mthd.SDCS_CH_OFFSET, scttIdx + mthd.SDCS_ENG_OFFSET, res);
  }
  int dgb = 1;
}
protected double calcScds(int idxEngA) {
  int L = 0;
  Ls LS = mthd.sysConfH.getBasis().getLs();
  FuncVec cA = clmbPsi.get(idxEngA);
  FuncVec cB = clmbPsi2.get(idxEngA);
  int ID_E2_A = mthd.trgtE2.getNt();
  int ID_E2_B = ID_E2_A + 1;  // NOTE!!! This will only work for E2!!! TODO: Check this if doing E3+
  ShPair clmbE2 = ShPairFactory.makePair(cA, ID_E2_A, L, cB, ID_E2_B, L, LS);

  vClmbVBasis = calcClmbVBasis(clmbE2); // different for each idxEngA
  log.dbg("vClmbVBasis=", new VecDbgView(vClmbVBasis));

  double partA = calcPartA(clmbE2);
  double partK = calcPartK(clmbE2);
  double ampl = partA + partK;

  double norm = Scatt.calcSdcsNormE2E(
    sdcsEngs.get(idxEngA), sdcsEngs2.get(idxEngA)
    , mthd.getScttE());  log.dbg("calcScds norm=", norm);
  double res = norm * ampl * ampl;
  return res;
}

private double calcPartA(ShPair clmbE2) {
  int sN = mthd.getSysBasisSize();
  double res = 0;  // amplitude
  for (int sysIdx = 0; sysIdx < sN; sysIdx++) {
    double ch = calcClmbVSys(clmbE2, sysIdx);  //log.dbg("calcScds ch=", ch);
    double ah = ch * jmA.get(sysIdx);          //log.dbg("calcScds ah=", ah);
    //log.dbg("calcScds old res=", ampl);
    res += ah;    //log.dbg("calcScds new res=", ampl);
  }
  return res;
}
private double calcPartK(ShPair clmbE2) {
  int L = 0;
  Ls LS = mthd.sysConfH.getBasis().getLs();

  int ID_E2_XI = 1 + Math.max(clmbE2.a.getIdx(), clmbE2.b.getIdx());

  CalcOptE1 calcOpt = mthd.getCalcOpt();
  int tN = mthd.openChN;
  int katoN = calcOpt.getKatoN();
  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  LgrrModel jmModel = calcOpt.getLgrrModel();
  int N = jmModel.getN();
  FuncArr trgtStates = mthd.trgtE2.getStatesE1();
  double res = 0;  // amplitude
  for (int t = 0; t < tN; t++) {     //log.dbg("t = ", t);  // Target channels
    FuncVec tWf = trgtStates.get(t);
    Shell tSh = new Shell(t, tWf, L);

    for (int xiIdx = 0; xiIdx < katoN; xiIdx++) {
      double f = jmF.get(t, xiIdx);        //log.dbg("f=", f);
      FuncVec xi = katoLgrr.get(N + xiIdx);//TODO: check (N + xiIdx) getting right func
      ShPair xiConf = ShPairFactory.makePair(tSh, xi, ID_E2_XI, L, LS);
      double v2 = calcTwoPot(clmbE2, xiConf); //log.dbg("v2=", v2);
      res += (f * v2);       //log.dbg("res=", res);
    }  log.dbg("res(katoN=" + katoN + ")=", res);
  }
  return res;
}
protected double calcClmbVSys(ShPair clmbE2, int sysIdx) {
  double[][] sV = mthd.sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = mthd.sysConfH.getBasis();     // sBasis
  double res = 0;
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    double term = sV[sbi][sysIdx];     //log.dbg("term=", term);
    if (Calc.isZero(term))
      continue;
    double a = vClmbVBasis.get(sbi);         //log.dbg("eng=", eng);
    res += ( term * a );
  }
  return res;
}

protected Vec calcClmbVBasis(ShPair clmbE2) {
  ConfArr sB = mthd.sysConfH.getBasis();     // sBasis
  Vec res = new Vec(sB.size());
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    Conf basisConf = sB.get(sbi);
    double a = calcTwoPot(clmbE2, basisConf);         //log.dbg("eng=", eng);
    res.set(sbi, a);
  }
  return res;
}

protected double calcTwoPot(ShPair clmbE2, Conf conf) {
  SysAtomE2 sysE2 = (SysAtomE2)mthd.sysConfH.getAtom();
  Energy res = sysE2.calcH(clmbE2, conf);   //log.dbg("calcTwoPot res=", res);
  return res.pot2;
}


protected void loadClmbPsi() {
  int L = 0;
  double sysTotE = mthd.getSysTotE();
  if (sysTotE <= 0) { // not enough energy for ionization
    return;
  }
  WFQuadrLcr quadr = mthd.getQuadrLcr();
  clmbPsi = new JmClmbLcr(L, AtomHy.Z, sdcsEngs, sysTotE, quadr);    log.dbg("clmbPsi=\n", new FuncArrDbgView(clmbPsi));
  clmbPsi2 = new JmClmbLcr(L, AtomHy.Z, sdcsEngs2, sysTotE, quadr);  log.dbg("clmbPsi2=\n", new FuncArrDbgView(clmbPsi2));
  if (!new ClmbHyBoundTest(clmbPsi, mthd.trgtE2.getStatesE1()).ok())
    return;
  if (!new ClmbHyBoundTest(clmbPsi2, mthd.trgtE2.getStatesE1()).ok())
    return;

  int stopped = 1;
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
  CalcOptE1 calcOpt = mthd.getCalcOpt();
  double sysTotE = mthd.getSysTotE();
  double maxE = sysTotE / 2;

  DbleArr engs = new DbleArr();
  DbleArr engs2 = new DbleArr();

  Vec tEngs = mthd.trgtE2.getEngs();
  int engIdx = 0;
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
