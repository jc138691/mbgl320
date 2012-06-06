package papers.hy_swave.ees_bad;
import atom.shell.Ls;
import atom.shell.ShPair;
import atom.shell.ShPairFactory;
import atom.shell.Shell;
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
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.jm.ScttRes;

import javax.triplet.Dble3;
import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 3/04/12, 12:29 PM
 */
public class EesMthdBasisHyE2_v3c_limitE_bad extends EesMthdBaseE2 {
public static Log log = Log.getLog(EesMthdBasisHyE2_v3c_limitE_bad.class);
private Mtrx mK;
private Vec vB0;
private Vec vB1;
//private Mtrx mX0;
//private Mtrx mX1;
private CmplxMtrx cmS; //complex-matrx
public EesMthdBasisHyE2_v3c_limitE_bad(JmCalcOptE1 calcOpt) {
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
    double sysTotE = sEngs.get(sysIdx); log.dbg("sysE = ", sysTotE);

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
    makeSinDelN(sysTotE, openNum);
    makeCosDelN(sysTotE, openNum);

    calcAllVecs(sysTotE, sysIdx, openNum);
    calcK(openNum);
    cmS = Scatt.calcSFromK(mK, openChN);                        log.info("(1-iR)=\n", new CmplxMtrxDbgView(cmS));
    saveCrossSecs(sysIdx, res, cmS, openNum);
  }
  return res;
}


protected void calcAllVecs(double sysTotE, int sysIdx, int gNum) {
  // setup ids for continuum wfs. They must be different from target wfs
  int idx = getChNum() + 1;
  int ID_XI = idx++;
  int ID_S = idx++;   // id for s-like
  int ID_C = idx++;   // id for c-like

  Ls LS = sysConfH.getBasis().getLs();
  int sysNum = getSysBasisSize();

  int L = 0;
//  Vec vW = new Vec(gNum);
  vB0 = new Vec(gNum);
  vB1 = new Vec(gNum);
  Vec tEngs = trgtE2.getEngs();
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

    Dble3 sc = calcSC(pS, pC, pXi, sysIdx);
    vB0.set(g, sc.a);                                      //log.dbg("sc.a=", sc.a);
    vB1.set(g, sc.b);                                      //log.dbg("sc.b=", sc.b);
//    vW.set(g, sc.c);                                      //log.dbg("sc.c=", sc.c);
  }
//  mX0 = MtrxFactory.makeFromTwoVecs(vW, vB0);
//  mX1 = MtrxFactory.makeFromTwoVecs(vW, vB1);
  log.dbg("vB0=", new VecDbgView(vB0));
  log.dbg("vB1=", new VecDbgView(vB1));
//  log.dbg("vW=", new VecDbgView(vW));
//  log.dbg("mX0=\n", new MtrxDbgView(mX0));
//  log.dbg("mX1=\n", new MtrxDbgView(mX1));
}
protected void calcK(int chNum) {
  mK = new Mtrx(chNum, chNum);
  double[][] aK = mK.getArray();
  Mtrx mK2 = mK.copy();
  double[][] aK2 = mK2.getArray();
  for (int g = 0; g < chNum; g++) {
    double pg = chArr[g].getAbsMom();
    for (int g2 = 0; g2 < chNum; g2++) {
      aK[g][g2] = - vB0.get(g) / vB1.get(g2);
      aK[g][g2] /= chNum;
      aK2[g][g2] = aK[g][g2];

      double pg2 = chArr[g2].getAbsMom();
      double c = Math.sqrt(pg/pg2);
      aK[g][g2] *= c;
      aK2[g][g2] /= c;
    }
  }
  log.dbg("mK=\n", new MtrxDbgView(mK));
  log.dbg("mK2=\n", new MtrxDbgView(mK2));

  // make symmetric
  for (int g = 0; g < chNum; g++) {
    double pg = chArr[g].getAbsMom();
    for (int g2 = 0; g2 < g; g2++) {
      double avr = 0.5 * (aK[g][g2] + aK[g2][g]);
      aK[g][g2] = avr;
      aK[g2][g] = avr;
    }
  }
  log.dbg("mK=\n", new MtrxDbgView(mK));
  log.dbg("mK2=\n", new MtrxDbgView(mK2));
}
}