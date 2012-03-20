package papers.hy_swave.method_ees;
import atom.e_2.SysAtomE2;
import atom.energy.Energy;
import atom.shell.*;
import atom.wf.log_cr.WFQuadrLcr;
import flanagan.complex.Cmplx;
import math.Calc;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.mtrx.MtrxFactory;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

import javax.utilx.log.Log;
import javax.utilx.pair.Dble2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 13/03/12, 10:14 AM
 */
public class EesMethodE2_basisHy extends EesMethodE2_oneChTest {
public static Log log = Log.getLog(EesMethodE2_basisHy.class);
private Vec vS;
private Vec vC;
private Vec vB;
private Mtrx mK;
private Mtrx mX;
private Mtrx mY;
private FuncArr freeS;
private FuncArr phiS;
private FuncArr phiC;

public EesMethodE2_basisHy(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScattRes calcSysEngs() {    log.setDbg();
  EngModel engModel = calcOpt.getGridEng();
  ScattRes res = new ScattRes();

  Vec sEngs = getSysEngs();
  int chNum = calcReportChNum(sEngs);
  int eN = sEngs.size();
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  EesMethodE1 methodE1 = new EesMethodE1(calcOpt);
  for (int sysIdx = 0; sysIdx < eN; sysIdx++) {                log.dbg("i = ", sysIdx);
    double sysTotE = sEngs.get(sysIdx);                           log.dbg("sysE = ", sysTotE);
    double scattE = sysTotE - trgtE2.getInitTrgtEng();      log.dbg("scattE = ", scattE);
    mCrss.set(sysIdx, IDX_ENRGY, scattE);

    double sigma = 0;
    if (scattE <= 0
      ||  engModel.getFirst() > scattE
      ||  scattE > engModel.getLast()
      ) {
      continue;
    }
    if (hasOneOpenCh(sysTotE)) { // just one channel

      //TODO

//      FuncArr psi = methodE1.calcPsi(scattE, orthonN);
//      Dble2 sc = calcSC(psi, scattE, sysIdx);
//      double R = -sc.a / sc.b;                               log.dbg("R = ", R);
//      Cmplx S = Scatt.calcSFromR(R);                                          log.dbg("S = ", S);
//      sigma = Scatt.calcSigmaPiFromS(S, scattE);
//      mCrss.set(sysIdx, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
      continue;
    }
    loadTrialWfs(sysIdx, orthonN, chNum);
    calcAllVecs(sysIdx, chNum);
    calcK(chNum);

//      Dble2 sc = calcSC(psi, scattE, sysIdx);
//      double R = -sc.a / sc.b;                               log.dbg("R = ", R);
      double R = 0;
  //    double sysA = calcSysA(psi, scattE, i, R);

  // todo: for e-H
  //    double newR = calcRFromPsiE(psi, scattE, i, sysA, R);   log.dbg("newR = ", newR);

      Cmplx S = Scatt.calcSFromR(R);                                          log.dbg("S = ", S);
      sigma = Scatt.calcSigmaPiFromS(S, scattE);
  //    double sigma = R;
  //    double sigma = newR;
    log.dbg("sigma = ", sigma).eol();
    mCrss.set(sysIdx, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
  }
  return res;
}
private boolean hasOneOpenCh(double sysTotE) {
  Vec tEngs = trgtE2.getEngs();
  return tEngs.get(1) >= sysTotE;  //second target channel is closed
}
private int calcReportChNum(Vec engs) {
  EngModel engModel = calcOpt.getGridEng();
  double maxScattE = engModel.getLast();
  double maxTotSysE = trgtE2.getInitTrgtEng() + maxScattE;
  int tN = getChNum();
  int chIdx = 0;
  Vec tEngs = trgtE2.getEngs();
  for (chIdx = 0; chIdx < tN; chIdx++) {     //log.dbg("t = ", t);  // Target channels
    double chE = tEngs.get(chIdx); // channel eng
    if (chE > maxTotSysE) {
      break;
    }
  }
  return chIdx + 1; // +1 to get count (not index)
}

protected void loadTrialWfs(int sysIdx, LgrrOrthLcr orthN, int chNum) {
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  freeS = new FuncArr(x);
  phiS = new FuncArr(x);
  phiC = new FuncArr(x);

  Vec tEngs = trgtE2.getEngs();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double sE = sEngs.get(sysIdx);  // system total eng
    double tScattE = sE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec tPsi = EesMethodE1.calcChPsiReg(tScattE, orthN);
    freeS.add(tPsi);

    FuncVec tPhiS = EesMethodE1.calcChPhiS(tScattE, orthN);
    phiS.add(tPhiS);

    FuncVec tPhiC = EesMethodE1.calcChPhiC(tScattE, orthN);
    phiC.add(tPhiC);
  }
}

private double calcB(Shell tSh, Shell freeSh, int sysIdx) {
  // getting relevant sysEigVec
  double[][] sV = sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = sysConfH.getBasis();     // sBasis
  SysAtomE2 sysE2 = (SysAtomE2)sysConfH.getAtom();
  double res = 0;
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    Conf sysConf = sB.get(sbi);
    double term = sV[sbi][sysIdx];     log.dbg("term=", term);
    if (Calc.isZero(term))
      continue;

    //Vbf - bound-free; could also be Vff - free-free for ionization
    double v = sysE2.calcVbf(tSh, freeSh, sysConf);         log.dbg("v=", v);
    res += ( term * v );
  }
  return res;
}
private Dble2 calcSC(ShPair confS, ShPair confC, int sysIdx) {
  Dble2 res = new Dble2();
  // getting relevant sysEigVec
  double[][] sV = sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = sysConfH.getBasis();     // sBasis
  SysAtomE2 sysE2 = (SysAtomE2)sysConfH.getAtom();
  Energy eng;
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    Conf sysConf = sB.get(sbi);
    double term = sV[sbi][sysIdx];     log.dbg("term=", term);
    if (Calc.isZero(term))
      continue;

    eng = sysE2.calcH(sysConf, confS);         log.dbg("eng=", eng);
    double s = eng.kin + eng.pot;              log.dbg("s=", s);

    eng = sysE2.calcH(sysConf, confC);         log.dbg("eng=", eng);
    double c = eng.kin + eng.pot;              log.dbg("c=", c);

    res.a += ( term * s );
    res.b += ( term * c );
  }
  return res;
}

protected void calcAllVecs(int sysIdx, int chNum) {
  Ls LS = sysConfH.getBasis().getLs();
  SysAtomE2 sysE2 = (SysAtomE2)sysConfH.getAtom();

  int L = 0;
  int FREE_IDX = -1;
  vS = new Vec(chNum);
  vC = new Vec(chNum);
  vB = new Vec(chNum);
  mX = new Mtrx(chNum, chNum);
  mY = new Mtrx(chNum, chNum);
  Vec tEngs = trgtE2.getEngs();
  FuncArr trgtWfs = getTrgtBasisN();
  Vec sEngs = getSysEngs();
  double sE = sEngs.get(sysIdx);  // system total eng
  for (int g = 0; g < chNum; g++) {     // g-gamma //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(g);     // target state eng
    double tScattE = sE - tE;
    if (tScattE <= 0) {
      vB.set(g, 0);
      continue;
    }
    FuncVec tWf = trgtWfs.get(g);
    Shell tSh = new Shell(g, tWf, L);

    FuncVec freePsi = freeS.get(g);
    Shell freeSh = new Shell(FREE_IDX, freePsi, L);
    double b = calcB(tSh, freeSh, sysIdx);
    vB.set(g, b);

    FuncVec tPhiS = phiS.get(g);
    Shell shS = new Shell(FREE_IDX, tPhiS, L);
    ShPair pairS = new ShPair(tSh, shS, LS);

    FuncVec tPhiC = phiC.get(g);
    Shell shC = new Shell(FREE_IDX, tPhiC, L);
    ShPair pairC = new ShPair(tSh, shC, LS);

    Dble2 sc = calcSC(pairS, pairC, sysIdx);
    vS.set(g, sc.a);
    vC.set(g, sc.b);

    for (int t2 = 0; t2 < chNum; t2++) {     //log.dbg("t = ", t);  // Target channels
      double tE2 = tEngs.get(t2);     // target state eng
      double tScattE2 = sE - tE2;
      if (tScattE2 <= 0) {
        mX.set(g, t2, 0);
        mY.set(g, t2, 0);
        continue;
      }

      FuncVec tWf2 = trgtWfs.get(t2);
      Shell tSh2 = new Shell(t2, tWf2, L);

      FuncVec tPhi2 = phiS.get(t2); // channel S-like
      Shell sh2 = new Shell(FREE_IDX, tPhi2, L);
      ShPair pairSh2 = new ShPair(tSh2, sh2, LS);
      double x = sysE2.calcVbf(tSh, freeSh, pairSh2);         log.dbg("x=", x);
      mX.set(g, t2, x);

      tPhi2 = phiC.get(t2); // channel C-like
      sh2 = new Shell(FREE_IDX, tPhi2, L);
      pairSh2 = new ShPair(tSh2, sh2, LS);
      double y = sysE2.calcVbf(tSh, freeSh, pairSh2);         log.dbg("y=", y);
      mY.set(g, t2, y);
    }
  }
  log.dbg("mX=", new MtrxDbgView(mX));
  log.dbg("mY=", new MtrxDbgView(mY));
  log.dbg("vS=", new VecDbgView(vS));
  log.dbg("vC=", new VecDbgView(vC));
  log.dbg("vB=", new VecDbgView(vB));
}
protected void calcK(int chNum) {
  Mtrx oneY = MtrxFactory.makeOneDiag(chNum);  log.dbg("oneDiag=", new MtrxDbgView(oneY));
  oneY = oneY.plusEquals(mY);       log.dbg("oneY=", new MtrxDbgView(oneY));
  Mtrx W = oneY.inverse();          log.dbg("W=(1+Y)^{-1}=", new MtrxDbgView(W));
}


}
