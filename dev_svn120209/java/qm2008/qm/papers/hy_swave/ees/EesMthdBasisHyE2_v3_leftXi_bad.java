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
import math.mtrx.MtrxFactory;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScattRes;

import javax.triplet.Dble3;
import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/03/12, 2:17 PM
 */
public class EesMthdBasisHyE2_v3_leftXi_bad extends EesMthdBaseE2 {
public static Log log = Log.getLog(EesMthdBasisHyE2_v3_leftXi_bad.class);
private Vec vG;
private Vec vB0;
private Vec vB1;
private Mtrx mK;
private Mtrx m0;
private Mtrx m1;
private CmplxMtrx cmS; //complex-matrx
public EesMthdBasisHyE2_v3_leftXi_bad(CalcOptE1 calcOpt) {
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
    loadPWaveS_OLD(sysIdx, orthonNt, openNum);
    loadPWaveC_OLD(sysIdx, orthonNt, openNum);

    calcAllVecs(sysIdx, openNum);
    calcK(openNum);
//    calcK2(openNum);
//    calcK3(openNum);
    cmS = Scatt.calcSFromK(mK);                        log.info("(1-iR)=\n", new CmplxMtrxDbgView(cmS));
    calcCrossSecs(sysIdx, res, cmS, openNum);
  }
  return res;
}

private Dble3 calcSC(ShPair confS, ShPair confC, ShPair pXi, int sysIdx) {
  Dble3 res = new Dble3();
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
    double a = eng.kin + eng.pot;              //log.dbg("s=", s);

    eng = sysE2.calcH(sysConf, confC);         //log.dbg("eng=", eng);
    double b = eng.kin + eng.pot;              //log.dbg("c=", c);

    eng = sysE2.calcH(sysConf, pXi);         //log.dbg("eng=", eng);
    double c = eng.kin + eng.pot;              //log.dbg("c=", c);

    res.a += ( term * a );
    res.b += ( term * b );
    res.c += ( term * c );
  }
  return res;
}

protected void calcAllVecs(int sysIdx, int chNum) {
  // setup ids for continuum wfs. They must be different from target wfs
  int idx = getChNum() + 1;
  int ID_XI = idx++;
  int ID_S = idx++;   // id for s-like
  int ID_C = idx++;   // id for c-like

  Ls LS = sysConfH.getBasis().getLs();

  int L = 0;
  vG = new Vec(chNum);
  vB0 = new Vec(chNum);
  vB1 = new Vec(chNum);
  m0 = new Mtrx(chNum, chNum);
  m1 = new Mtrx(chNum, chNum);
  Vec tEngs = trgtE2.getEngs();
  FuncArr trgtWfs = getTrgtBasisN();
  Vec sEngs = getSysEngs();
  double sysTotE = sEngs.get(sysIdx);  // system total eng
  for (int g = 0; g < chNum; g++) {     log.dbg("g = ", g); // g-gamma; Target channels
    double tE = tEngs.get(g);     // target state eng
    double tScattE = sysTotE - tE;
    if (tScattE <= 0) {
      continue;
    }
    FuncVec tWf = trgtWfs.get(g);
    Shell tSh = new Shell(g, tWf, L);

    FuncVec tPhiS = phiS.get(g);
    Shell shS = new Shell(ID_S, tPhiS, L);
    ShPair pS = new ShPair(tSh, shS, LS);

    FuncVec tPhiC = phiC.get(g);
    Shell shC = new Shell(ID_C, tPhiC, L);
    ShPair pC = new ShPair(tSh, shC, LS);

    FuncVec tXi = orthonN.getLast();
    Shell shXi = new Shell(ID_XI, tXi, L);
    ShPair pXi = new ShPair(tSh, shXi, LS);

    Dble3 sc = calcSC(pS, pC, pXi, sysIdx);
    vB0.set(g, sc.a);                                      log.dbg("sc.a=", sc.a);
    vB1.set(g, sc.b);                                      log.dbg("sc.b=", sc.b);
    vG.set(g, sc.c);                                      log.dbg("sc.c=", sc.c);

    for (int g2 = 0; g2 < chNum; g2++) {     log.dbg("g2 = ", g2);  // Target channels
      double tE2 = tEngs.get(g2);     // target state eng
      double tScattE2 = sysTotE - tE2;
      if (tScattE2 <= 0) {
        continue;
      }
      double ms = calcHE(g, g2, phiS.get(g2), sysTotE, LS);         log.dbg("ms=", ms);
      m0.set(g, g2, ms);

      double mc = calcHE(g, g2, phiC.get(g2), sysTotE, LS);         log.dbg("mc=", mc);
      m1.set(g, g2, mc);
    }
  }
  log.dbg("m0=\n", new MtrxDbgView(m0));
  log.dbg("m1=\n", new MtrxDbgView(m1));
  log.dbg("vB0=", new VecDbgView(vB0));
  log.dbg("vB1=", new VecDbgView(vB1));
  log.dbg("vG=", new VecDbgView(vG));
}
private double calcHE(int g, int g2, FuncVec pw2, double sysTotE, Ls ls) {
  int L = 0;
  SysAtomE2 sysE2 = (SysAtomE2)sysConfH.getAtom();
  FuncArr trgtWfs = getTrgtBasisN();

  FuncVec tWf = trgtWfs.get(g);
  Shell shB = new Shell(g, tWf, L);    // bound #1

  FuncVec pwXi = orthonN.getLast();
  Shell shXi = new Shell(-1, pwXi, L);

  FuncVec tWf2 = trgtWfs.get(g2);
  Shell shB2 = new Shell(g2, tWf2, L); // bound #2
  Shell shF2 = new Shell(-1, pw2, L);

  double d1 = 0;
  if (g == g2)  {
    double xi = sysE2.calcOverlap(shXi, shF2);    log.dbg("xi=", xi);
    Energy he = sysE2.calcOneH(shXi, shF2);       log.dbg("he=", he);
    double tE = trgtE2.getEngs().get(g);          log.dbg("tE=", tE);
    d1 = (tE - sysTotE) * xi + he.kin + he.pot;   log.dbg("d1=", d1);
  }
  double di = sysE2.calcTwoPot(L, shB, shXi, shB2, shF2);     log.dbg("di=", di);
  double ex = sysE2.calcTwoPot(L, shB, shXi, shF2, shB2);     log.dbg("ex=", ex);
  double res = d1 + di + Mathx.pow(-1., ls.getS()) * ex;                            log.dbg("res=", res);
  return res;
}
protected void calcK(int chNum) {
  Mtrx mInv = m1.inverse();          log.dbg("m01^{-1}=\n", new MtrxDbgView(mInv));
  Mtrx mX = mInv.times(m0);          log.dbg("mX=\n", new MtrxDbgView(mX));
  Mtrx mXt = mX.transpose();          log.dbg("mXt=\n", new MtrxDbgView(mXt));

  Vec vY = mInv.times(vG);            log.dbg("vY=", new VecDbgView(vY));
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

//  for (int g = 0; g < chNum; g++) {
//    double pg = chArr[g].getAbsMom();
//    for (int g2 = 0; g2 < g; g2++) {
//      double maxV = Math.max(aK[g][g2], aK[g2][g]);
//      aK[g][g2] = maxV;
//      aK[g2][g] = maxV;
//    }
//  }
//  log.dbg("mK=\n", new MtrxDbgView(mK));
//  log.dbg("mK2=\n", new MtrxDbgView(mK2));
}
}