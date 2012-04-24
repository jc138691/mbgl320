package scatt.jm_2008.e2;
import atom.data.AtomUnits;
import flanagan.complex.Cmplx;
import math.complex.CmplxMtrx;
import math.complex.CmplxMtrxDbgView;
import math.complex.CmplxVec;
import math.complex.CmplxVecDbgView;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.mtrx.MtrxInfo;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.JmCh;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import javax.utilx.arraysx.StrVec;
import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,6/06/11,9:55 AM
 */
public class JmResonE2 {
  public static Log log = Log.getLog(JmResonE2.class);
  private static int count = 0;
  public static final int RES_INFO_ID = count++;
  public static final int RES_INFO_ENG = count++;
  public static final int RES_INFO_ENG_FROM_INIT = count++;
  public static final int RES_INFO_ENG_FROM_INIT_EV = count++;
  public static final int RES_INFO_DELTA_EV = count++;
  public static final int RES_INFO_POS_EV = count++;  // position = E_i - initTrgtEng + Re[Delta_i]
  public static final int RES_INFO_GAMMA_EV = count++; // Im in eV
//  public static final int RES_INFO_ENG_IMPACT = count++;
//  public static final int RES_INFO_ABS_RATIO = count++;
  public static final int RES_INFO_SIZE = count++;

  private JmMthdBaseE2 jmm;
  public JmResonE2(JmMthdBaseE2 method) {
    this.jmm = method;
  }

  public void calc(ScttRes res, Mtrx mX) {
    CmplxVec resDlts = calc(mX);
    res.setResDlts(resDlts);
    res.setResInfo(loadResInfo(resDlts));
  }
  private MtrxInfo loadResInfo(CmplxVec resDlts) {
    int sN = jmm.getSysBasisSize();
    double[][] info = new double[sN][RES_INFO_SIZE];
    String[] hrds = new String[RES_INFO_SIZE];
    double[] sysE = jmm.getSysEngs().getArr();
    double initTrgtEng = jmm.getTrgtE2().getInitTrgtEng();
    hrds[RES_INFO_ID] = "ID=i+1";
    hrds[RES_INFO_ENG] = "ENG=sysE[i]";
    hrds[RES_INFO_ENG_FROM_INIT] = "ENG_FROM_INIT=sysE[i]-initTrgtEng(" + initTrgtEng +")";
    hrds[RES_INFO_ENG_FROM_INIT_EV] = "ENG_FROM_INIT_EV=AtomUnits.toEV(sysE[i]-initTrgtEng)";
    hrds[RES_INFO_DELTA_EV] = "DELTA_EV=AtomUnits.toEV(delta.getRe())";
    hrds[RES_INFO_POS_EV] = "POS_EV=AtomUnits.toEV(sysE[i]-initTrgtEng+delta.getRe())";
    hrds[RES_INFO_GAMMA_EV] = "GAMMA_EV=AtomUnits.toEV(-2.*delta.getIm())";
    for (int i = 0; i < sN; i++) {  // iIdx, system state
      Cmplx delta = resDlts.get(i);
      info[i][RES_INFO_ID] = i+1;
      info[i][RES_INFO_ENG] = sysE[i];
      info[i][RES_INFO_ENG_FROM_INIT] = sysE[i] - initTrgtEng;
      info[i][RES_INFO_ENG_FROM_INIT_EV] = AtomUnits.toEV(sysE[i] - initTrgtEng);
      info[i][RES_INFO_DELTA_EV] = AtomUnits.toEV(delta.getRe());
      info[i][RES_INFO_POS_EV] = AtomUnits.toEV(sysE[i] - initTrgtEng + delta.getRe());
      info[i][RES_INFO_GAMMA_EV] = AtomUnits.toEV(-2. * delta.getIm());

      // load ratio of abs(Delta) / min(E_{i-1} - E_i, E_{i+1} - E_i)
//      double minDistE = 0;
//      if (i == 0) {
//        minDistE = abs(sysE[i] - sysE[i + 1]);
//      } else if (i == sN-1) {
//        minDistE = abs(sysE[i] - sysE[i - 1]);
//      } else {
//        minDistE = min(abs(sysE[i] - sysE[i + 1]), abs(sysE[i] - sysE[i - 1]));
//      }
//      if (Calc.isZero(minDistE)) {
//        info[i][RES_INFO_ABS_RATIO] = 100;  // something is wrong, throw exception?
//      } else {
//        info[i][RES_INFO_ABS_RATIO] = delta.abs() / minDistE;
//      }
    }
    MtrxInfo res = new MtrxInfo(new Mtrx(info));      log.dbg("res=", new MtrxDbgView(res)); // RESONANCE info-matrix
    res.setColHrds(new StrVec(hrds));
    return res;
  }
  protected CmplxMtrx calcResA() {
    int sN = jmm.getSysBasisSize();
    int cN = jmm.getChNum();
    CmplxMtrx res = new CmplxMtrx(cN, sN);
    double[] sysE = jmm.getSysEngs().getArr();
    for (int i = 0; i < sN; i++) {  // iIdx, system state
      double Ei = sysE[i];
      JmCh[] cArr = jmm.loadChArr(Ei);
      for (int g = 0; g < cN; g++) {  // gIdx, g - for "little" gamma; target channel/state
        JmCh ch = cArr[g];
        Cmplx a = ch.getJnn().times(ch.getCsn()).div(ch.getCsn1());
        res.set(g, i, a);                           //log.dbg("X[" + g + ", " + i + "]=", sum);
      }
    }
    return res;
  }
  protected CmplxVec calcResDlts(Mtrx mX, CmplxMtrx mResA) {
    double[][] X = mX.getArray();
    Cmplx[][] A = mResA.getArr();
    int sN = jmm.getSysBasisSize();
    Cmplx[] res = new Cmplx[sN];
    int cN = jmm.getChNum();
    for (int i = 0; i < sN; i++) {  // iIdx, system state
      Cmplx sum = new Cmplx();
      for (int g = 0; g < cN; g++) {  // gIdx, g - for "little" gamma; target channel/state
        Cmplx a = A[g][i].times(X[g][i] * X[g][i]);
        sum = sum.add(a);
      }
      res[i] = sum;                           //log.dbg("X[" + g + ", " + i + "]=", sum);
    }
    return new CmplxVec(res);
  }
  public CmplxVec calc(Mtrx mX) {
    CmplxMtrx resA = calcResA();                 log.dbg("resA=", new CmplxMtrxDbgView(resA)); // RESONANCE A-matrix
    CmplxVec resDlts = calcResDlts(mX, resA);      log.dbg("resDlts=", new CmplxVecDbgView(resDlts)); // RESONANCE deltas
    return resDlts;
  }

}
