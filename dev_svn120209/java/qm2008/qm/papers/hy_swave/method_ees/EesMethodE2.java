package papers.hy_swave.method_ees;
import atom.e_2.SysAtomE2;
import atom.energy.ConfHMtrx;
import atom.energy.Energy;
import atom.shell.*;
import atom.shell.deepcopy.ShWf;
import com.sun.org.apache.bcel.internal.generic.L2D;
import flanagan.complex.Cmplx;
import math.Calc;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.target.ChConf;
import scatt.jm_2008.jm.target.ScattTrgtE3;

import javax.utilx.pair.Dble2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 8/03/12, 9:18 AM
 */
public class EesMethodE2 extends EesMethodE1 {
private ScattTrgtE3 trgtE3;
private ConfHMtrx sysConfH;
public EesMethodE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public void setTrgtE3(ScattTrgtE3 trgtE3) {
  this.trgtE3 = trgtE3;
}
public void setSysConfH(ConfHMtrx sysConfH) {
  this.sysConfH = sysConfH;
}
public ScattRes calcSysEngs() {
  EngModel engModel = calcOpt.getGridEng();
  ScattRes res = new ScattRes();
  int chNum = getChNum();
  Vec engs = getSysEngs();
  int eN = engs.size();
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  for (int i = 0; i < engs.size(); i++) {                log.dbg("i = ", i);
    double sysE = engs.get(i);                           log.dbg("sysE = ", sysE);
    double scattE = sysE - trgtE3.getInitTrgtEng();      log.dbg("scattE = ", scattE);
    double sigma = 0;
    if (scattE > 0
      &&  engModel.getFirst() <= scattE
      &&  scattE <= engModel.getLast()
      ) {
      mCrss.set(i, IDX_ENRGY, scattE);
      FuncArr psi = calcPsi(scattE, i);
      Dble2 sc = calcSC(psi, scattE, i);
      double R = -sc.a / sc.b;                               log.dbg("R = ", R);
  //    double sysA = calcSysA(psi, scattE, i, R);

  // todo: for e-H
  //    double newR = calcRFromPsiE(psi, scattE, i, sysA, R);   log.dbg("newR = ", newR);

      Cmplx S = Scatt.calcSFromR(R);                                          log.dbg("S = ", S);
      sigma = Scatt.calcSigmaPiFromS(S, scattE);
  //    double sigma = R;
  //    double sigma = newR;
    }
    log.dbg("sigma = ", sigma).eol();
    mCrss.set(i, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
  }
  return res;
}
protected Dble2 calcSC(FuncArr psi, double scattE, int sysIdx) {
  int L = 0;
  int CONT_IDX = 999;
  Dble2 res = new Dble2();
  FuncVec psiS = psi.get(IDX_P_REG);          log.dbg("resS=", psiS);
  FuncVec psiC = psi.get(IDX_P_IRR);          log.dbg("resC=", psiC);
  Shell shS = new Shell(CONT_IDX, psiS, L);
  Shell shC = new Shell(CONT_IDX, psiC, L);

  // getting relevant sysEigVec
  double[][] sV = sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = sysConfH.getBasis();     // sBasis
  SysAtomE2 sysE2 = (SysAtomE2)sysConfH.getAtom();

  // getting relevant trgtEigVec
  int CH_IDX = 0; // this is temp for testing
  ChConf conf = trgtE3.getChConf(CH_IDX);
  int gt = conf.fromIdx; // gamma in target H
  ConfHMtrx tH = conf.hMtrx;
  double[][] tV = tH.getEigArr();  // tEigVec
  ConfArr tB = tH.getBasis(); // tBasis

  Energy eng;
  for (int tbi = 0; tbi < tB.size(); tbi++) {  // target basis index
    Conf tConf = tB.get(tbi);
    Shell tSh = tConf.getSh(0);
    for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
      Conf sysConf = sB.get(sbi);

      double term = tV[tbi][gt] * sV[sbi][sysIdx];     log.dbg("term=", term);
      if (Calc.isZero(term))
        continue;
      Ls sLs = sysConf.getTotLS();

      ShPair confS = new ShPair(tSh, shS, sLs);
      ShPair confC = new ShPair(tSh, shC, sLs);

      eng = sysE2.calcH(sysConf, confS);         log.dbg("eng=", eng);
      double s = eng.kin + eng.pot;              log.dbg("s=", s);

      eng = sysE2.calcH(sysConf, confC);         log.dbg("eng=", eng);
      double c = eng.kin + eng.pot;              log.dbg("c=", c);

      res.a += ( term * s );
      res.b += ( term * c );
    }
  }
  return res;
}
}
