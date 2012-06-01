package papers.hy_swave.ees_bad;
import atom.e_2.SysE2;
import atom.energy.Energy;
import atom.shell.*;
import math.Calc;
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
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 13/03/12, 10:14 AM
 */
public class EesMthdBasisHyE2_v1_bad extends EesMthdBaseE2 {
public static Log log = Log.getLog(EesMthdBasisHyE2_v1_bad.class);
private Vec vS;
private Vec vC;
private Vec vB;
private Mtrx mK;
private Mtrx mX;
private Mtrx mY;
private CmplxMtrx cmS; //complex-matrx

public EesMthdBasisHyE2_v1_bad(CalcOptE1 calcOpt) {
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
    loadTrialWfs(sysIdx, orthNt, openNum);
    calcAllVecs(sysIdx, openNum);
    calcK(openNum);
    cmS = Scatt.calcSFromK(mK, openChN);                        log.info("(1-iR)=\n", new CmplxMtrxDbgView(cmS));
    saveCrossSecs(sysIdx, res, cmS, openNum);
  }
  return res;
}

private double calcB(Shell tSh, Shell freeSh, int sysIdx) {
  // getting relevant sysEigVec
  double[][] sV = sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = sysConfH.getBasis();     // sBasis
  SysE2 sysE2 = (SysE2)sysConfH.getAtom();
  double res = 0;
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    Conf sysConf = sB.get(sbi);
    double term = sV[sbi][sysIdx];     //log.dbg("term=", term);
    if (Calc.isZero(term))
      continue;

    //Vbf - bound-free; could also be Vff - free-free for ionization
//    double v = sysE2.calcVbabb_OLD(tSh, freeSh, sysConf);         //log.dbg("v=", v);
    double v = 0;         //log.dbg("v=", v);
    res += ( term * v );
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
  int idx = getChNum() + 1;   // id for free s-like
  int ID_FREE_S = idx++;   // id for free s-like
  int ID_S = idx++;   // id for s-like
  int ID_C = idx++;   // id for c-like

  Ls LS = sysConfH.getBasis().getLs();
  SysE2 sysE2 = (SysE2)sysConfH.getAtom();

  int L = 0;
  vS = new Vec(chNum);
  vC = new Vec(chNum);
  vB = new Vec(chNum);
  mX = new Mtrx(chNum, chNum);
  mY = new Mtrx(chNum, chNum);
  Vec tEngs = trgtE2.getEngs();
  FuncArr trgtWfs = getWfsE1();
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

    FuncVec freePsi = sinWfs.get(g);
    Shell freeSh = new Shell(ID_FREE_S, freePsi, L);
    double b = calcB(tSh, freeSh, sysIdx);
    vB.set(g, b);

    FuncVec tPhiS = sinDelN.get(g);
    Shell shS = new Shell(ID_S, tPhiS, L);
    ShPair pS = new ShPair(tSh, shS, LS);

    FuncVec tPhiC = cosDelN.get(g);
    Shell shC = new Shell(ID_C, tPhiC, L);
    ShPair pC = new ShPair(tSh, shC, LS);

    Dble2 sc = calcSC(pS, pC, sysIdx);
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

      // channel S-like
      ShPair pSh2 = ShPairFactory.makePair(tSh2, sinDelN.get(t2), ID_S, L, LS);
//      double x = sysE2.calcVbabb_OLD(tSh, freeSh, pSh2);         //log.dbg("x=", x);
      double x = 0;
      mX.set(g, t2, x);

      // channel C-like
      pSh2 = ShPairFactory.makePair(tSh2, cosDelN.get(t2), ID_C, L, LS);
//      double y = sysE2.calcVbabb_OLD(tSh, freeSh, pSh2);         //log.dbg("y=", y);
      double y = 0;
      mY.set(g, t2, y);
    }
  }
  log.dbg("mX=\n", new MtrxDbgView(mX));
  log.dbg("mY=\n", new MtrxDbgView(mY));
  log.dbg("vS=", new VecDbgView(vS));
  log.dbg("vC=", new VecDbgView(vC));
  log.dbg("vB=", new VecDbgView(vB));
}
protected void calcK(int chNum) {
  Mtrx mOneY = MtrxFactory.makeOneDiag(chNum);  log.dbg("oneDiag=\n", new MtrxDbgView(mOneY));
  mOneY = mOneY.plusEquals(mY);       log.dbg("mOneY=\n", new MtrxDbgView(mOneY));
  Mtrx mW = mOneY.inverse();          log.dbg("mW=(1+Y)^{-1}=\n", new MtrxDbgView(mW));

  Vec vWB = mW.times(vB);             log.dbg("vWB=", new VecDbgView(vWB));
  double beta = vC.dot(vWB);         log.dbg("beta=", beta);
  double oneBeta = 1. / beta;       log.dbg("oneBeta=1./beta=", oneBeta);

  Mtrx mWX = mW.times(mX);           log.dbg("WX=\n", new MtrxDbgView(mWX));
  Mtrx mWXt = mWX.transpose();       log.dbg("WXt=\n", new MtrxDbgView(mWXt));
  Vec vZ = mWXt.times(vC);           log.dbg("atomZ=WXt * vC=", new VecDbgView(vZ));

  Vec vA = vS.copy();
  vA.addMultSafe(-1., vZ);           log.dbg("A=S-atomZ=", new VecDbgView(vA));
  vA.mult(oneBeta);                 log.dbg("A=(S-atomZ)/beta=", new VecDbgView(vA));

  Mtrx mAB = MtrxFactory.makeFromTwoVecs(vB, vA);  log.dbg("AB=\n", new MtrxDbgView(mAB));
  mAB.plusEquals(mX);                              log.dbg("AB=AB+X\n", new MtrxDbgView(mAB));
  mK = mW.times(mAB);                              log.dbg("W*(AB+X)\n", new MtrxDbgView(mK));
  mK.timesEquals(-1.);                             log.dbg("K=-W*(AB+X)\n", new MtrxDbgView(mK));
}

}
