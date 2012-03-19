package papers.hy_swave.method_ees;
import atom.e_2.SysAtomE2;
import atom.shell.*;
import atom.wf.log_cr.WFQuadrLcr;
import flanagan.complex.Cmplx;
import math.Calc;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.partial.wf.CosRegK2;
import scatt.partial.wf.SinK2;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 13/03/12, 10:14 AM
 */
public class EesMethodE2_basisHy extends EesMethodE2_oneChTest {
public static Log log = Log.getLog(EesMethodE2_basisHy.class);
public EesMethodE2_basisHy(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScattRes calcSysEngs() {
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
//    FuncArr psi = methodE1.calcPsi(scattE, orthonN);
    ThisRes2 res2 = calcBXY(sysIdx, chNum);

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
//private void loadZeroCross(ScattRes res, int sysIdx) {
//  Mtrx mCrss = res.getCrossSecs();
//  int nC = mCrss.getNumCols();
//  for (int c = 0; c < nC; c++) {
//    if (c == IDX_ENRGY)
//      continue;
//    mCrss.set(sysIdx, c, 0);     // NOTE +1; first column has incident energies
//  }
//}
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

protected ThisRes2 calcBXY(int sysIdx, int chNum) {
  int L = 0;
  int FREE_IDX = -1;

  ThisRes2 res = new ThisRes2();
  Vec vB = new Vec(chNum);
  Mtrx mX = new Mtrx(chNum, chNum);
  Mtrx mY = new Mtrx(chNum, chNum);
  Vec tEngs = trgtE2.getEngs();
  FuncArr trgtWfs = getTrgtBasisN();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double sE = sEngs.get(sysIdx);  // system total eng
    double tScattE = sE - tE;
    if (tScattE <= 0) {
      vB.set(tIdx, 0);
      continue;
    }
    FuncVec tPsi = EesMethodE1.calcChPsiReg(tScattE, orthonN);
    FuncVec tWf = trgtWfs.get(tIdx);
    Shell tSh = new Shell(tIdx, tWf, L);
    Shell psiSh = new Shell(FREE_IDX, tPsi, L);
    double b = calcB(tSh, psiSh, sysIdx);
    vB.set(tIdx, b);

    for (int t2 = 0; t2 < chNum; t2++) {     //log.dbg("t = ", t);  // Target channels
      double tE2 = tEngs.get(t2);     // target state eng
      double tScattE2 = sE - tE2;
      if (tScattE2 <= 0) {
        vB.set(tIdx, 0);
        continue;
      }
    }
  }
  res.vB = vB;
  return res;
}

public ThisFuncArr calcPsi(double scattE, LgrrOrthLcr orthN) {
  int L = 0;
  double momP = Scatt.calcMomFromE(scattE);
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  FuncVec sinL = new SinK2(quadr, momP, L);   log.dbg("sinL=", sinL);
  FuncVec cosL = new CosRegK2(quadr, momP, L
    , orthN.getLambda());   log.dbg("cosL=", cosL);

  res.add(sinL.copyY());     // IDX_REG
  res.add(cosL.copyY());     // IDX_IRR
  res.add(sinL.copyY());     // IDX_P_REG
  res.add(cosL.copyY());     // IDX_P_IRR
  FuncVec resS = res.get(IDX_P_REG);          log.dbg("resS=", resS);
  FuncVec resC = res.get(IDX_P_IRR);          log.dbg("resC=", resC);

//  if (OPER_P_ON) {
    for (int i = 0; i < basis.size(); i++) {
      FuncVec fi = basis.getFunc(i);          log.dbg("fi=", fi);
      double dS = quadr.calcInt(sinL, fi);    log.dbg("dS=", dS);
      double dC = quadr.calcInt(cosL, fi);    log.dbg("dC=", dC);
      resS.addMultSafe(-dS, fi);              log.dbg("resS=", resS);
      resC.addMultSafe(-dC, fi);              log.dbg("resC=", resC);
    }
    for (int i = 0; i < basis.size(); i++) {
      FuncVec fi = basis.getFunc(i);            log.dbg("fi=", fi);
      double testS = quadr.calcInt(resS, fi);    log.dbg("testS=", testS);
      assertEquals("testS_" + i, testS, 0d);
      assertEquals(0, testS, MAX_INTGRL_ERR_E11);

      double testC = quadr.calcInt(resC, fi);    log.dbg("testC=", testC);
      assertEquals("testC_" + i, testC, 0d);
      assertEquals(0, testC, MAX_INTGRL_ERR_E11);
    }
//  }
//  FileX.writeToFile(sinL.toTab(), calcOpt.getHomeDir(), "wf", "sin_" + idxCount + ".txt");
//  FileX.writeToFile(cosL.toTab(), calcOpt.getHomeDir(), "wf", "cos_" + idxCount + ".txt");
//  FileX.writeToFile(resS.toTab(), calcOpt.getHomeDir(), "wf", "psi_sin_" + idxCount + ".txt");
//  FileX.writeToFile(resC.toTab(), calcOpt.getHomeDir(), "wf", "psi_cos_" + idxCount + ".txt");
  return res;
}

private class ThisRes2 {
  public Vec vB;
  public Mtrx mX;
  public Mtrx mY;
}
}
