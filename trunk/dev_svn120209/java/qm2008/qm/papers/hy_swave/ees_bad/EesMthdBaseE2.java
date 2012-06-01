package papers.hy_swave.ees_bad;
import atom.e_2.SysE2;
import atom.energy.Energy;
import atom.shell.*;
import atom.wf.lcr.WFQuadrLcr;
import math.Calc;
import math.Mathx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.IFuncArr;
import math.vec.Vec;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e2.ScttMthdBaseE2;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

import javax.triplet.Dble3;
import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 22/03/12, 10:44 AM
 */
public class EesMthdBaseE2 extends ScttMthdBaseE2 {
public static Log log = Log.getLog(EesMthdBaseE2.class);
protected FuncArr sinWfs;
protected FuncArr sinDelN;
protected FuncArr pnS;    // pnS = \oveline{\psi} =  \hat{P}_N \psi = \psi - \phi
protected FuncArr cosDelN;
public EesMthdBaseE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
protected void loadPnS(int sysIdx, LgrrOrthLcr orthN, int chNum) {
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  pnS = new FuncArr(x);

  Vec tEngs = trgtE2.getEngs();
  Vec sEngs = getSysEngs();
  for (int tIdx = 0; tIdx < chNum; tIdx++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(tIdx);     // target state eng
    double sE = sEngs.get(sysIdx);  // system total eng
    double tScattE = sE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec tPhiS = EesMethodE1.calcPWavePnS(tScattE, orthN);
    pnS.add(tPhiS);
  }
}
protected void loadTrialWfs(double sTotE, LgrrOrthLcr orthN, int chNum) {
  sinWfs = makeSinWfs(sTotE, chNum);
  sinDelN = makeSinDelN(sTotE, orthN, chNum);
  cosDelN = makeCosDelN(sTotE, orthN, chNum);
}
protected Dble3 calcSC(ShPair confS, ShPair confC, ShPair pXi, int sysIdx) {
  Dble3 res = new Dble3();
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
    double a = eng.kin + eng.pt;              //log.dbg("s=", s);

    eng = sysE2.calcH(sysConf, confC);         //log.dbg("eng=", eng);
    double b = eng.kin + eng.pt;              //log.dbg("c=", c);

    eng = sysE2.calcH(sysConf, pXi);         //log.dbg("eng=", eng);
    double c = eng.kin + eng.pt;              //log.dbg("c=", c);

    res.a += ( term * a );
    res.b += ( term * b );
    res.c += ( term * c );
  }
  return res;
}
protected double calcXiM(int g, int g2, FuncVec pw2, double sysTotE, Ls ls) {
  int L = 0;
  SysE2 sysE2 = (SysE2)sysConfH.getAtom();
  FuncArr trgtWfs = getWfsE1();

  FuncVec tWf = trgtWfs.get(g);
  Shell shB = new Shell(g, tWf, L);    // bound #1

  FuncVec pwXi = orthN.getLast();
  Shell shXi = new Shell(-1, pwXi, L);

  FuncVec tWf2 = trgtWfs.get(g2);
  Shell shB2 = new Shell(g2, tWf2, L); // bound #2
  Shell shF2 = new Shell(-1, pw2, L);

  double d1 = 0;
  if (g == g2)  {
    double xi = sysE2.calcOverlap(shXi, shF2);    log.dbg("xi=", xi);
    Energy he = sysE2.calcOneH(shXi, shF2);       log.dbg("he=", he);
    double tE = trgtE2.getEngs().get(g);          log.dbg("tE=", tE);
    d1 = (tE - sysTotE) * xi + he.kin + he.pt;   log.dbg("d1=", d1);
  }
  double di = sysE2.calcTwoPot(L, shB, shXi, shB2, shF2);     log.dbg("di=", di);
  double ex = sysE2.calcTwoPot(L, shB, shXi, shF2, shB2);     log.dbg("ex=", ex);
  double res = d1 + di + Mathx.pow(-1., ls.getS()) * ex;                            log.dbg("res=", res);
  return res;
}
}
