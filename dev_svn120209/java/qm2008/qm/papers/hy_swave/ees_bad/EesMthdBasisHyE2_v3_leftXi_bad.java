package papers.hy_swave.ees_bad;
import atom.shell.*;
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
    double sysTotE = sEngs.get(sysIdx);                           log.dbg("sysE = ", sysTotE);
    chArr = loadChArr(sysTotE);    // used to calc cross_sections
    double scattE = sysTotE - trgtE2.getInitTrgtEng();      log.dbg("scttE = ", scattE);
    mCrss.set(sysIdx, IDX_ENRGY, scattE);
    int openNum = calcOpenChNum(scattE);

    double sigma = 0;
    log.dbg("E_MIN=" + (float)engModel.getFirst() + ", E_MAX=" + (float)engModel.getLast() + ", scttE=" + (float)scattE);
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
    cmS = Scatt.calcSFromK(mK, openChN);                        log.info("(1-iR)=\n", new CmplxMtrxDbgView(cmS));
    saveCrossSecs(sysIdx, res, cmS, openNum);
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
    ShPair pS = ShPairFactory.makePair(tSh, phiS.get(g), ID_S, L, LS);
    ShPair pC = ShPairFactory.makePair(tSh, phiC.get(g), ID_C, L, LS);
    ShPair pXi = ShPairFactory.makePair(tSh, orthonN.getLast(), ID_XI, L, LS);

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
      double ms = calcXiM(g, g2, phiS.get(g2), sysTotE, LS);         log.dbg("ms=", ms);
      m0.set(g, g2, ms);

      double mc = calcXiM(g, g2, phiC.get(g2), sysTotE, LS);         log.dbg("mc=", mc);
      m1.set(g, g2, mc);
    }
  }
  log.dbg("m0=\n", new MtrxDbgView(m0));
  log.dbg("m1=\n", new MtrxDbgView(m1));
  log.dbg("vB0=", new VecDbgView(vB0));
  log.dbg("vB1=", new VecDbgView(vB1));
  log.dbg("vG=", new VecDbgView(vG));
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