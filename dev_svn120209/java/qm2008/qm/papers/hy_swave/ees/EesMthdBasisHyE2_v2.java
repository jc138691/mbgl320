package papers.hy_swave.ees;
import atom.e_2.SysAtomE2;
import atom.energy.Energy;
import atom.shell.*;
import math.Calc;
import math.Mathx;
import math.complex.CmplxMtrx;
import math.complex.CmplxMtrxDbgView;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScattRes;

import javax.utilx.log.Log;
import javax.utilx.pair.Dble2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 26/03/12, 2:27 PM
 */
public class EesMthdBasisHyE2_v2 extends EesMthdBaseE2 {
public static Log log = Log.getLog(EesMthdBasisHyE2_v2.class);
private Vec vB0;
private Vec vB1;
private Mtrx mK;
private Mtrx m00;
private Mtrx m01;
//private Mtrx mX10;
//private Mtrx mX11;
private CmplxMtrx cmS; //complex-matrx

public EesMthdBasisHyE2_v2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScattRes calcSysEngs() {    log.setDbg();
  EngModel engModel = calcOpt.getGridEng();
  ScattRes res = new ScattRes();

  Vec sEngs = getSysEngs();       log.dbg("sEngs=", sEngs);
  int showNum = calcShowChNum();
  int eN = sEngs.size();

  Mtrx mCrss = new Mtrx(eN, showNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  Mtrx mTics = new Mtrx(eN, 2);// ionisation cross section
  res.setTics(mTics);

  EesMethodE1 methodE1 = new EesMethodE1(calcOpt);
  for (int sysIdx = 0; sysIdx < eN; sysIdx++) {                log.dbg("i = ", sysIdx);
    double sysTotE = sEngs.get(sysIdx);                           log.dbg("sysE = ", sysTotE);
    chArr = loadChArr(sysTotE);    // used to calc cross_sections
    double scattE = sysTotE - trgtE2.getInitTrgtEng();      log.dbg("scattE = ", scattE);
    mCrss.set(sysIdx, IDX_ENRGY, scattE);
    int openNum = calcOpenChNum(scattE);

    double sigma = 0;
    log.dbg("E_MIN=" + (float)engModel.getFirst() + ", E_MAX=" + (float)engModel.getLast() + ", scattE=" + (float)scattE);
    if (scattE <= 0
      ||  engModel.getFirst() > scattE
      ||  scattE > engModel.getLast()
      ) {
      continue;
    }
    // DEBUGGING ONLY
    if (openNum > 2) {
      log.dbg("DEBUGGING ONLY: if (!hasTwoOpenChs(sysTotE))");
      break;
    }
    if (openNum == 1) {
      log.dbg("if (openNum == 1)");
    }
    if (openNum == 2) {
      log.dbg("if (openNum == 2)");
    }
    loadPWaveS(sysIdx, orthonN, openNum);
    loadPnS(sysIdx, orthonN, openNum);
    loadPWaveC(sysIdx, orthonN, openNum);

    calcAllVecs(sysIdx, openNum);
    calcK(openNum);
    cmS = Scatt.calcSFromK(mK);                        log.info("(1-iR)=\n", new CmplxMtrxDbgView(cmS));
    calcCrossSecs(sysIdx, res, cmS, openNum);
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
    double term = sV[sbi][sysIdx];     //log.dbg("term=", term);
    if (Calc.isZero(term))
      continue;

    eng = sysE2.calcH(sysConf, confS);         //log.dbg("eng=", eng);
    double s = eng.kin + eng.pot;              //log.dbg("s=", s);

    eng = sysE2.calcH(sysConf, confC);         //log.dbg("eng=", eng);
    double c = eng.kin + eng.pot;              //log.dbg("c=", c);

    res.a += ( term * s );
    res.b += ( term * c );
  }
  return res;
}

protected void calcAllVecs(int sysIdx, int chNum) {
  // setup ids for continuum wfs. They must be different from target wfs
  int idx = getChNum() + 1;
  int ID_S = idx++;   // id for s-like
//  int ID_S2 = idx++;   // id for s-like
  int ID_C = idx++;   // id for c-like
//  int ID_C2 = idx++;   // id for c-like

  Ls LS = sysConfH.getBasis().getLs();

  int L = 0;
  vB0 = new Vec(chNum);
  vB1 = new Vec(chNum);
  m00 = new Mtrx(chNum, chNum);
  m01 = new Mtrx(chNum, chNum);
  Vec tEngs = trgtE2.getEngs();
  FuncArr trgtWfs = getTrgtBasisN();
  Vec sEngs = getSysEngs();
  double sysTotE = sEngs.get(sysIdx);  // system total eng
  for (int g = 0; g < chNum; g++) {     log.dbg("g = ", g); // g-gamma; Target channels
    double tE = tEngs.get(g);     // target state eng
    double tScattE = sysTotE - tE;
    if (tScattE <= 0) {
      vB0.set(g, 0);
      vB1.set(g, 0);
      continue;
    }
    FuncVec tWf = trgtWfs.get(g);
    Shell tSh = new Shell(g, tWf, L);

    FuncVec tPhiS = phiS.get(g);
    FuncVec tPnS = pnS.get(g);
    Shell shS = new Shell(ID_S, tPhiS, L);
    ShPair pS = new ShPair(tSh, shS, LS);

    FuncVec tPhiC = phiC.get(g);
    Shell shC = new Shell(ID_C, tPhiC, L);
    ShPair pC = new ShPair(tSh, shC, LS);

    Dble2 sc = calcSC(pS, pC, sysIdx);
    vB0.set(g, sc.a);                                      log.dbg("sc.a=", sc.a);
    vB1.set(g, sc.b);                                      log.dbg("sc.b=", sc.b);

    for (int g2 = 0; g2 < chNum; g2++) {     log.dbg("g2 = ", g2);  // Target channels
      double tE2 = tEngs.get(g2);     // target state eng
      double tScattE2 = sysTotE - tE2;
      if (tScattE2 <= 0) {
        m00.set(g, g2, 0);
        m01.set(g, g2, 0);
        continue;
      }
      double ms = calcHE(g, g2, phiS.get(g2), LS);         log.dbg("ms=", ms);
      m00.set(g, g2, ms);

      double mc = calcHE(g, g2, phiC.get(g2), LS);         log.dbg("mc=", mc);
      m01.set(g, g2, mc);
    }
  }
  log.dbg("m00=\n", new MtrxDbgView(m00));
  log.dbg("m01=\n", new MtrxDbgView(m01));
  log.dbg("vB0=", new VecDbgView(vB0));
  log.dbg("vB1=", new VecDbgView(vB1));
}
private double calcHE(int g, int g2, FuncVec pw2, Ls ls) {
  int L = 0;
  SysAtomE2 sysE2 = (SysAtomE2)sysConfH.getAtom();
  FuncArr trgtWfs = getTrgtBasisN();

  FuncVec tWf = trgtWfs.get(g);
  Shell shB = new Shell(g, tWf, L);    // bound #1

  FuncVec pwS = phiS.get(g);   // plain-wave
  Shell shF = new Shell(-1, pwS, L);

  FuncVec tWf2 = trgtWfs.get(g2);
  Shell shB2 = new Shell(g2, tWf2, L); // bound #2
  Shell shF2 = new Shell(-1, pw2, L);

  double d1 = 0;
  double d2 = 0;
  if (g == g2)  {
    Shell sh = new Shell(-1, pnS.get(g), L);
    d1 = -sysE2.calcOneKin(sh, shF2);     log.dbg("d1=", d1); // NOTE (-1)
    d2 = sysE2.calcOnePotZ(shF, shF2);                       log.dbg("d2=", d2);
  }
  double di = sysE2.calcTwoPot(L, shB, shF, shB2, shF2);     log.dbg("di=", di);
  double ex = sysE2.calcTwoPot(L, shB, shF, shF2, shB2);     log.dbg("ex=", ex);
  double res = d1 + d2 + di + Mathx.pow(-1., ls.getS()) * ex;                            log.dbg("res=", res);
  return res;
}
protected void calcK(int chNum) {
  Mtrx invM01 = m01.inverse();          log.dbg("m01^{-1}=\n", new MtrxDbgView(mX));
  Mtrx mX = invM01.times(m00);
  Mtrx mXt = mX.transpose();

  Vec y0 = invM01.times(vB0);
  double beta = vB1.dot(y0);
  Vec vXB1 = mXt.times(vB1);
  Vec d0 =

//  Mtrx mOneY = MtrxFactory.makeOneDiag(chNum);  log.dbg("oneDiag=\n", new MtrxDbgView(mOneY));
//  mOneY = mOneY.plusEquals(mY);       log.dbg("mOneY=\n", new MtrxDbgView(mOneY));
//  Mtrx mW = mOneY.inverse();          log.dbg("mW=(1+Y)^{-1}=\n", new MtrxDbgView(mW));
//
//  Vec vWB = mW.times(vB);             log.dbg("vWB=", new VecDbgView(vWB));
//  double beta = vC.dot(vWB);         log.dbg("beta=", beta);
//  double oneBeta = 1. / beta;       log.dbg("oneBeta=1./beta=", oneBeta);
//
//  Mtrx mWX = mW.times(mX);           log.dbg("WX=\n", new MtrxDbgView(mWX));
//  Mtrx mWXt = mWX.transpose();       log.dbg("WXt=\n", new MtrxDbgView(mWXt));
//  Vec vZ = mWXt.times(vC);           log.dbg("Z=WXt * vC=", new VecDbgView(vZ));
//
//  Vec vA = vS.copy();
//  vA.addMultSafe(-1., vZ);           log.dbg("A=S-Z=", new VecDbgView(vA));
//  vA.mult(oneBeta);                 log.dbg("A=(S-Z)/beta=", new VecDbgView(vA));
//
//  Mtrx mAB = MtrxFactory.makeFromTwoVecs(vB, vA);  log.dbg("AB=\n", new MtrxDbgView(mAB));
//  mAB.plusEquals(mX);                              log.dbg("AB=AB+X\n", new MtrxDbgView(mAB));
//  mK = mW.times(mAB);                              log.dbg("W*(AB+X)\n", new MtrxDbgView(mK));
//  mK.timesEquals(-1.);                             log.dbg("K=-W*(AB+X)\n", new MtrxDbgView(mK));
}
}