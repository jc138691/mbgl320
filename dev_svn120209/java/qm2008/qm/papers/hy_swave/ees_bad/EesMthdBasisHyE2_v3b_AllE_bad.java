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
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.jm.ScttRes;

import javax.triplet.Dble3;
import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 2/04/12, 4:11 PM
 */
public class EesMthdBasisHyE2_v3b_AllE_bad extends EesMthdBaseE2 {
public static Log log = Log.getLog(EesMthdBasisHyE2_v3b_AllE_bad.class);
private Mtrx mK;
private Mtrx mX0;
private Mtrx mX1;
private Mtrx mM0;
private Mtrx mM1;
private CmplxMtrx cmS; //complex-matrx
public EesMthdBasisHyE2_v3b_AllE_bad(JmCalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScttRes calcSysEngs() {    log.setDbg();
  EngModel engModel = calcOpt.getGridEng();
  ScttRes res = new ScttRes();

  Vec sEngs = getSysEngs();       log.dbg("sEngs=", sEngs);
  int showNum = calcPrntChNum();
  int eN = sEngs.size();
  eN--; log.dbg("eN-- = ", eN);// reduce by one

  Mtrx mCrss = new Mtrx(eN, showNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  Mtrx mTics = new Mtrx(eN, 2);// ionisation cross section
  res.setTics(mTics);

  EesMethodE1 methodE1 = new EesMethodE1(calcOpt);
  for (int eIdx = 0; eIdx < eN; eIdx++) {                log.dbg("i = ", eIdx);
    // TODO: calc eng arr separately if keeping this
    sysTotE = 0.5 * (sEngs.get(eIdx) + sEngs.get(eIdx+1)); log.dbg("sysE = ", sysTotE);

    chArr = loadChArr(sysTotE);    // used to calc cross_sections
    scttE = sysTotE - trgtE2.getInitTrgtEng();      log.dbg("scttE = ", scttE);
    mCrss.set(eIdx, IDX_ENRGY, scttE);
    int openNum = calcOpenChNum(scttE);

    double sigma = 0;
    log.dbg("E_MIN=" + (float)engModel.getFirst() + ", E_MAX=" + (float)engModel.getLast() + ", scttE=" + (float) scttE);
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
    makeSinDelN(sysTotE, openNum);
    makeCosDelN(sysTotE, openNum);

    calcAllVecs(sysTotE, openNum);
    calcK(openNum);
    cmS = Scatt.calcSFromK(mK, openChN);                        log.info("(1-iR)=\n", new CmplxMtrxDbgView(cmS));
    saveCrossSecs(eIdx, res, cmS, openNum);
  }
  return res;
}


protected void calcAllVecs(double sysTotE, int gNum) {
  // setup ids for continuum wfs. They must be different from target wfs
  int idx = getChNum() + 1;
  int ID_XI = idx++;
  int ID_S = idx++;   // id for s-like
  int ID_C = idx++;   // id for c-like

  Ls LS = sysConfH.getConfArr().getLs();
  int sysNum = getSysBasisSize();

  int L = 0;
  Mtrx mW = new Mtrx(gNum, sysNum);
  Mtrx mB0 = new Mtrx(sysNum, gNum);
  Mtrx mB1 = new Mtrx(sysNum, gNum);
  mM0 = new Mtrx(gNum, gNum);
  mM1 = new Mtrx(gNum, gNum);
  Vec tEngs = trgtE2.getEngs();
  double[] sEngs = getSysEngs().getArr();
  FuncArr trgtWfs = getWfsE1();
  for (int g = 0; g < gNum; g++) {     log.dbg("g = ", g); // g-gamma; Target channels
    double tE = tEngs.get(g);     // target state eng
    double tScattE = sysTotE - tE;
    if (tScattE <= 0) {
      continue;
    }
    FuncVec tWf = trgtWfs.get(g);
    Shell tSh = new Shell(g, tWf, L);

    ShPair pS = ShPairFactory.makePair(tSh, sinDelN.get(g), ID_S, L, LS);
    ShPair pC = ShPairFactory.makePair(tSh, cosDelN.get(g), ID_C, L, LS);
    ShPair pXi = ShPairFactory.makePair(tSh, orth.getLast(), ID_XI, L, LS);

    for (int sysIdx = 0; sysIdx < sysNum; sysIdx++) {     //log.dbg("sysIdx = ", sysIdx);  // Target channels
      Dble3 sc = calcSC(pS, pC, pXi, sysIdx);
      mB0.set(sysIdx, g, sc.a);                                      //log.dbg("sc.a=", sc.a);
      mB1.set(sysIdx, g, sc.b);                                      //log.dbg("sc.b=", sc.b);
      sc.c /= (sEngs[sysIdx] - sysTotE);
      mW.set(g, sysIdx, sc.c);                                      //log.dbg("sc.c=", sc.c);
    }

    for (int g2 = 0; g2 < gNum; g2++) {     log.dbg("g2 = ", g2);  // Target channels
      double tE2 = tEngs.get(g2);     // target state eng
      double tScattE2 = sysTotE - tE2;
      if (tScattE2 <= 0) {
        continue;
      }
      double ms = calcXiM(g, g2, sinDelN.get(g2), sysTotE, LS);         log.dbg("ms=", ms);
      mM0.set(g, g2, ms);

      double mc = calcXiM(g, g2, cosDelN.get(g2), sysTotE, LS);         log.dbg("mc=", mc);
      mM1.set(g, g2, mc);
    }
  }
  log.dbg("mM0=\n", new MtrxDbgView(mM0));
  log.dbg("mM1=\n", new MtrxDbgView(mM1));
  log.dbg("mW=\n", new MtrxDbgView(mW));
  log.dbg("mB0=\n", new MtrxDbgView(mB0));
  log.dbg("mB1=\n", new MtrxDbgView(mB1));
  mX0 = mW.times(mB0);
  mX1 = mW.times(mB1);
  log.dbg("mX0=\n", new MtrxDbgView(mX0));
  log.dbg("mX1=\n", new MtrxDbgView(mX1));
}
protected void calcK(int chNum) {
  Mtrx mMX0 = mM0.minusEquals(mX0);    log.dbg("mMX0=\n", new MtrxDbgView(mMX0));
  Mtrx mMX1 = mM1.minusEquals(mX1);    log.dbg("mMX1=\n", new MtrxDbgView(mMX1));
  Mtrx mInv = mMX1.inverse();          log.dbg("mInv=mMX1^{-1}=\n", new MtrxDbgView(mInv));

  mK = mInv.times(mMX0);               log.dbg("MX1^{-1} MX0 =\n", new MtrxDbgView(mK));
  mK.timesEquals(-1.);                 log.dbg("K=-MX1^{-1} MX0\n", new MtrxDbgView(mK));
  double[][] aK = mK.getArray();
  Mtrx mK2 = mK.copy();
  double[][] aK2 = mK2.getArray();
  for (int g = 0; g < chNum; g++) {
    double pg = chArr[g].getAbsMom();
    for (int g2 = 0; g2 < chNum; g2++) {
      double pg2 = chArr[g2].getAbsMom();
      double c = Math.sqrt(pg/pg2);
      aK[g][g2] *= c;
      aK2[g][g2] /= c;
    }
  }
  log.dbg("mK=\n", new MtrxDbgView(mK));
  log.dbg("mK2=\n", new MtrxDbgView(mK2));
  MtrxFactory.makeSymmByAvr(mK, chNum);
  log.dbg("mK=\n", new MtrxDbgView(mK));
  log.dbg("mK2=\n", new MtrxDbgView(mK2));
}
}