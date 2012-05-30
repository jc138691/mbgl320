package papers.hy_swave.ees_bad;
import atom.e_2.SysE2;
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
import math.mtrx.MtrxFactory;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScttRes;

import javax.utilx.log.Log;
import javax.utilx.pair.Dble2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 26/03/12, 2:27 PM
 */
public class EesMthdBasisHyE2_v2_bad extends EesMthdBaseE2 {
public static Log log = Log.getLog(EesMthdBasisHyE2_v2_bad.class);
private Vec vB0;
private Vec vB1;
private Mtrx mK;
private Mtrx m00;
private Mtrx m01;
//private Mtrx mX10;
//private Mtrx mX11;
private CmplxMtrx cmS; //complex-matrx

public EesMthdBasisHyE2_v2_bad(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScttRes calcSysEngs() {    log.setDbg();
  EngModel engModel = calcOpt.getGridEng();
  ScttRes res = new ScttRes();

  Vec sEngs = getSysEngs();       log.dbg("sEngs=", sEngs);
  int showNum = calcPrntChNum();
  int eN = sEngs.size();

  Mtrx mCrss = new Mtrx(eN, showNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  Mtrx mTics = new Mtrx(eN, 2);// ionisation cross section
  res.setTics(mTics);

  EesMethodE1 methodE1 = new EesMethodE1(calcOpt);
  for (int sysIdx = 0; sysIdx < eN; sysIdx++) {                log.dbg("i = ", sysIdx);
    sysTotE = sEngs.get(sysIdx);                           log.dbg("sysE = ", sysTotE);
    chArr = loadChArr(sysTotE);    // used to calc cross_sections
    scttE = sysTotE - trgtE2.getInitTrgtEng();      log.dbg("scttE = ", scttE);
    mCrss.set(sysIdx, IDX_ENRGY, scttE);
    int openNum = calcOpenChNum(scttE);

    double sigma = 0;
    log.dbg("E_MIN=" + (float)engModel.getFirst() + ", E_MAX=" + (float)engModel.getLast() + ", scttE=" + (float)scttE);
    if (scttE <= 0
      ||  engModel.getFirst() > scttE
      ||  scttE > engModel.getLast()
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
    loadPWaveS(sysTotE, orthonNt, openNum);
    loadPnS(sysIdx, orthonNt, openNum);
    loadPWaveC(sysTotE, orthonNt, openNum);

    calcAllVecs(sysIdx, openNum);
    calcK(openNum);
//    calcK2(openNum);
//    calcK3(openNum);
    cmS = Scatt.calcSFromK(mK, openChN);                        log.info("(1-iR)=\n", new CmplxMtrxDbgView(cmS));
    saveCrossSecs(sysIdx, res, cmS, openNum);
  }
  return res;
}

private Dble2 calcSC(ShPair confS, ShPair confC, int sysIdx) {
  Dble2 res = new Dble2();
  // getting relevant sysEigVec
  double[][] sV = sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = sysConfH.getBasis();     // sBasis
  SysE2 sysE2 = (SysE2)sysConfH.getAtom();
  Energy eng;
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    Conf sysConf = sB.get(sbi);
    double term = sV[sbi][sysIdx];     //log.dbg("term=", term);
    if (Calc.isZero(term))
      continue;

    eng = sysE2.calcH(sysConf, confS);         //log.dbg("eng=", eng);
    double s = eng.kin + eng.pt;              //log.dbg("s=", s);

    eng = sysE2.calcH(sysConf, confC);         //log.dbg("eng=", eng);
    double c = eng.kin + eng.pt;              //log.dbg("c=", c);

    res.a += ( term * s );
    res.b += ( term * c );
  }
  return res;
}

protected void calcAllVecs(int sysIdx, int chNum) {
  // setup ids for continuum wfs. They must be different from target wfs
  int idx = getChNum() + 1;
  int ID_S = idx++;   // id for s-like
  int ID_C = idx++;   // id for c-like

  Ls LS = sysConfH.getBasis().getLs();

  int L = 0;
  vB0 = new Vec(chNum);
  vB1 = new Vec(chNum);
  m00 = new Mtrx(chNum, chNum);
  m01 = new Mtrx(chNum, chNum);
  Vec tEngs = trgtE2.getEngs();
  FuncArr trgtWfs = getBasisN();
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
  SysE2 sysE2 = (SysE2)sysConfH.getAtom();
  FuncArr trgtWfs = getBasisN();

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
  Mtrx mInv = m01.inverse();          log.dbg("m01^{-1}=\n", new MtrxDbgView(mInv));
  Mtrx mX = mInv.times(m00);          log.dbg("mX=\n", new MtrxDbgView(mX));
  Mtrx mXt = mX.transpose();          log.dbg("mXt=\n", new MtrxDbgView(mXt));

  Vec vY = mInv.times(vB0);            log.dbg("vY0=", new VecDbgView(vY));
  double beta = vB1.dot(vY);            log.dbg("beta=", beta);
  Vec vXB = mXt.times(vB1);            log.dbg("vXB=", new VecDbgView(vXB));
  Vec vD = vB0.copy();                 log.dbg("vD=", new VecDbgView(vD));
  vD.addMultSafe(-1., vXB);           log.dbg("vD=", new VecDbgView(vD));
  vD.mult(1. / beta);                       log.dbg("vD=", new VecDbgView(vD));

  mK = MtrxFactory.makeFromTwoVecs(vY, vD);  log.dbg("Y x D =\n", new MtrxDbgView(mK));
  mK.plusEquals(mX);                           log.dbg("Y x D + mX\n", new MtrxDbgView(mK));
  mK.timesEquals(-1.);                         log.dbg("K=-(Y x D + mX)\n", new MtrxDbgView(mK));
  double[][] aK = mK.getArray();
  Mtrx mK2 = mK.copy();
  double[][] aK2 = mK2.getArray();
  for (int g = 0; g < chNum; g++) {
    double pg = chArr[g].getAbsMom();
    for (int g2 = 0; g2 < chNum; g2++) {
      double pg2 = chArr[g2].getAbsMom();
      double c = Math.sqrt(pg/pg2);
      aK[g][g2] /= c;
      aK2[g][g2] *= c;
    }
  }
  log.dbg("mK=\n", new MtrxDbgView(mK));
  log.dbg("mK2=\n", new MtrxDbgView(mK2));
}
protected void calcK2(int chNum) {
  Mtrx mInv = m00.inverse();          log.dbg("m01^{-1}=\n", new MtrxDbgView(mInv));
  Mtrx mX = mInv.times(m01);          log.dbg("mX=\n", new MtrxDbgView(mX));
  Mtrx mXt = mX.transpose();          log.dbg("mXt=\n", new MtrxDbgView(mXt));

  Vec vY = mInv.times(vB1);            log.dbg("vY=", new VecDbgView(vY));
  double beta = vB0.dot(vY);            log.dbg("beta=", beta);
  Vec vXB = mXt.times(vB0);            log.dbg("vXB=", new VecDbgView(vXB));
  Vec vD = vB1.copy();                 log.dbg("vD=", new VecDbgView(vD));
  vD.addMultSafe(-1., vXB);           log.dbg("vD=", new VecDbgView(vD));
  vD.mult(1. / beta);                       log.dbg("vD=", new VecDbgView(vD));

  mK = MtrxFactory.makeFromTwoVecs(vY, vD);  log.dbg("Y x D =\n", new MtrxDbgView(mK));
  mK.plusEquals(mX);                           log.dbg("Y x D + mX\n", new MtrxDbgView(mK));
  mK.timesEquals(-1.);                         log.dbg("K=-(Y x D + mX)\n", new MtrxDbgView(mK));
  double[][] aK = mK.getArray();
  Mtrx mK2 = mK.copy();
  double[][] aK2 = mK2.getArray();
  for (int g = 0; g < chNum; g++) {
    double pg = chArr[g].getAbsMom();
    for (int g2 = 0; g2 < chNum; g2++) {
      double pg2 = chArr[g2].getAbsMom();
      double c = Math.sqrt(pg/pg2);
      aK[g][g2] /= c;
      aK2[g][g2] *= c;
    }
  }
  log.dbg("mK2=\n", new MtrxDbgView(mK2));
  log.dbg("mK=\n", new MtrxDbgView(mK));
  mK = mK.inverse();
  mK2 = mK2.inverse();
  log.dbg("mK2=\n", new MtrxDbgView(mK2));
  log.dbg("mK=\n", new MtrxDbgView(mK));
}
protected void calcK3(int chNum) {
  Mtrx mInv = m01.inverse();          log.dbg("m01^{-1}=\n", new MtrxDbgView(mInv));
  Mtrx mX = mInv.times(m00);          log.dbg("mX=\n", new MtrxDbgView(mX));
  mK = mX;
  mK.timesEquals(-1.);                         log.dbg("K=-X\n", new MtrxDbgView(mK));
  double[][] aK = mK.getArray();
  Mtrx mK2 = mK.copy();
  double[][] aK2 = mK2.getArray();
  for (int g = 0; g < chNum; g++) {
    double pg = chArr[g].getAbsMom();
    for (int g2 = 0; g2 < chNum; g2++) {
      double pg2 = chArr[g2].getAbsMom();
      double c = Math.sqrt(pg/pg2);
      aK[g][g2] /= c;
      aK2[g][g2] *= c;
    }
  }
  log.dbg("mK=\n", new MtrxDbgView(mK));
  log.dbg("mK2=\n", new MtrxDbgView(mK2));
}

}