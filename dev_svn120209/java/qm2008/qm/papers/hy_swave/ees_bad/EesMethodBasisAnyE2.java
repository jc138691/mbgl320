package papers.hy_swave.ees_bad;
import atom.e_2.SysE2;
import atom.energy.AConfHMtrx;
import atom.energy.Energy;
import atom.shell.*;
import flanagan.complex.Cmplx;
import math.Calc;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.ChConf;
import scatt.jm_2008.jm.target.ScttTrgtE3;

import javax.utilx.log.Log;
import javax.utilx.pair.Dble2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 8/03/12, 9:18 AM
 */
public class EesMethodBasisAnyE2 extends EesMthdBaseE2 {
public static Log log = Log.getLog(EesMethodBasisAnyE2.class);
public EesMethodBasisAnyE2(JmCalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScttRes calcSysEngs() {
  EngModel engModel = calcOpt.getGridEng();
  ScttRes res = new ScttRes();
  int chNum = getChNum();
  Vec sEngs = getSysEngs();
  int eN = sEngs.size();
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  EesMethodE1 methodE1 = new EesMethodE1(calcOpt);
  for (int sysIdx = 0; sysIdx < sEngs.size(); sysIdx++) {                log.dbg("i = ", sysIdx);
    sysTotE = sEngs.get(sysIdx);                           log.dbg("sysTotE = ", sysTotE);
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
    loadTrialWfs(sysTotE, openNum);

    FuncArr psi = methodE1.calcPsi(scttE, quadr, orthNt);
    Dble2 sc = calcSC(psi, scttE, sysIdx);
    double R = -sc.a / sc.b;                               log.dbg("R = ", R);

    Cmplx S = Scatt.calcSFromK(R);                                          log.dbg("S = ", S);
    sigma = Scatt.calcSigmaPiFromS(S, scttE);
//    double sigma = R;
//    double sigma = newR;
    log.dbg("sigma = ", sigma).eol();
    mCrss.set(sysIdx, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
  }
  return res;
}
protected Dble2 calcSC(FuncArr psi, double scattE, int sysIdx) {
  int ID_FREE_S = getChNum() + 1;   // id for free s-like
  int ID_S = ID_FREE_S + 1;   // id for s-like
  int ID_C = ID_S + 1;   // id for c-like

  int L = 0;
  Dble2 res = new Dble2();
  FuncVec psiS = psi.get(EesMethodE1.IDX_P_REG);          log.dbg("resS=", psiS);
  FuncVec psiC = psi.get(EesMethodE1.IDX_P_IRR);          log.dbg("resC=", psiC);
  Shell shS = new Shell(ID_S, psiS, L);
  Shell shC = new Shell(ID_C, psiC, L);

  // getting relevant sysEigVec
  double[][] sV = sysConfH.getEigArr(); // sysEigVec
  LsConfs sB = sysConfH.getConfArr();     // sBasis
  SysE2 sysE2 = (SysE2)sysConfH.getSysH();

  // getting relevant trgtEigVec
  int CH_IDX = 0; // this is temp for testing

  ChConf conf = ((ScttTrgtE3)trgtE2).getChConf(CH_IDX);
  int gt = conf.fromIdx; // gamma in target H
  AConfHMtrx tH = conf.hMtrx;

  double[][] tV = tH.getEigArr();  // tEigVec
  LsConfs tB = tH.getConfArr(); // tBasis

  Energy eng;
  for (int tbi = 0; tbi < tB.size(); tbi++) {  // target basis index
    LsConf tConf = tB.get(tbi);
    Shell tSh = tConf.getSh(0);
    for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
      LsConf sysConf = sB.get(sbi);

      double term = tV[tbi][gt] * sV[sbi][sysIdx];     log.dbg("term=", term);
      if (Calc.isZero(term))
        continue;
      Ls sLs = sysConf.getTotLS();

      ShPair confS = new ShPair(tSh, shS, sLs);
      ShPair confC = new ShPair(tSh, shC, sLs);

      eng = sysE2.calcH(sysConf, confS);         log.dbg("eng=", eng);
      double s = eng.kin + eng.pt;              log.dbg("s=", s);

      eng = sysE2.calcH(sysConf, confC);         log.dbg("eng=", eng);
      double c = eng.kin + eng.pt;              log.dbg("c=", c);

      res.a += ( term * s );
      res.b += ( term * c );
    }
  }
  return res;
}
}
