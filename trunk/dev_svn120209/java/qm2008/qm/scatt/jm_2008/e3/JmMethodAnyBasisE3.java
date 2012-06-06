package scatt.jm_2008.e3;
import atom.energy.ConfHMtrx;
import atom.shell.Conf;
import atom.shell.ConfArr;
import atom.shell.Ls;
import atom.shell.Shell;
import math.mtrx.Mtrx;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.jm.target.ChConf;

import javax.utilx.log.Log;
import java.util.HashMap;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/05/11,3:17 PM
 */
public class JmMethodAnyBasisE3 extends JmMethodBaseE3 { // many electrons (more than 2). TODO: this should work for e-H as well
public static Log log = Log.getLog(JmMethodAnyBasisE3.class);
protected HashMap<String, String> equalSysTrgt;       // [12Apr2011] with equalSysTrgt, equals(Conf e3, Conf e2, int idx3) is much faster now!!!
protected HashMap<String, Integer> mapTrgtToSysIdx;   // [12Apr2011] with mapTrgtToSysIdx, calcC(int i, int g, int m) is much faster!!!
public JmMethodAnyBasisE3(JmCalcOptE1 calcOpt) {
  super(calcOpt);
}
//[system i][target gamma][overlap D]
protected double calcC(int i, int g, int m) { // needed in calcX()
  double[][] sV = sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = sysConfH.getBasis();     // sBasis

  ChConf conf = trgtE3.getChConf(g);
  int gt = conf.fromIdx; // gamma in target H
  ConfHMtrx tH = conf.hMtrx;
  double[][] tV = tH.getEigArr();  // tEigVec
  ConfArr tB = tH.getBasis(); // tBasis

  double res = 0;
  for (int b = 0; b < tB.size(); b++) {  // b - for basis index
    Conf tConf = tB.get(b);
    String tKey = makeTrgtKey(tConf, m);
    Integer sysIdx = mapTrgtToSysIdx.get(tKey);
    if (sysIdx == null) {
      for (int j = 0; j < sB.size(); j++) { // j - system index
        Conf jConf = sB.get(j);
        if (equals(jConf, tConf, m)) {
          sysIdx = j;
          mapTrgtToSysIdx.put(tKey, sysIdx);
          break;
        }
      }
    }
    if (sysIdx == null) {// something is very wrong
      throw new IllegalArgumentException(log.error("sysIdx == null"));
    }
    res += tV[b][gt] * sV[sysIdx][i];  // [14Apr2011] Verified order of indexes tV[b][gt] * sV[sysIdx][i] by comparing HyPoetV3 and HyPoetV4
  }
  return res;
}
/*
Load channel projections
X_i^{\alpha} = \sum_{j}  \delta_{j_1,\alpha} C_{ij} A_j D_{j_2,N-1},
*/
@Override
protected Mtrx calcX() {
  double[] D = getOverD().getArr();
  ConfArr sysBasis = sysConfH.getBasis();
  int sN = getSysBasisSize();
  int cN = getChNum();
  int N = calcOpt.getN();  // big N
  int nt = trgtE2.getNt();
  // optimization
  equalSysTrgt = new HashMap<String, String>(nt); // needed for  equals
  mapTrgtToSysIdx = new HashMap<String, Integer>(sN); // needed for  calcC
  Mtrx res = new Mtrx(cN, sN);
  for (int g = 0; g < cN; g++) {  // gIdx, g - for "little" gamma; target channel/state
    log.dbg("calcX{gamma = ", g);
    for (int i = 0; i < sN; i++) {  // iIdx, system state
      double sum = 0;
      for (int m = nt; m < N; m++) {  // mIdx, system state
        double c = calcC(i, g, m);                          //log.dbg("c = ", c);
        double d = D[m];                                    //log.dbg("d = ", d);
        sum += (c * d);                                     //log.dbg("sum = ", sum);
      }
      res.set(g, i, sum);                           //log.dbg("X[" + g + ", " + i + "]=", sum);
    }
  }
  return res;
}
private String makeTrgtKey(Conf e2, int idx3) {
  return e2.getKeyStr() + "!" + idx3;
}
public boolean equals(Conf e3, Conf e2, int idx3) { // this should work for any targets (not just E3-system and E2-target)
  String sys = e3.getKeyStr();
  String oldTrgt = equalSysTrgt.get(sys);
  if (oldTrgt != null) {  // already found
    String trgt = makeTrgtKey(e2, idx3);
    return oldTrgt.equals(trgt);
  }
  int n3 = e3.size();
  int n2 = e2.size();
  if (n3 <= 1 || n2 < 1) {
    return false;
  }
  if (n3 - n2 != 1) {
    return false;
  }
  Shell lastSh = e3.getSh(n3 - 1);
  if (lastSh.getQ() != 1) { // last E3-shell must contain only one electron
    return false;
  }
  if (lastSh.getIdx() != idx3) {
    return false;
  }
  for (int i = 0; i < n2; i++) {
    Shell sh2 = e2.getSh(i);
    Shell sh3 = e3.getSh(i);
    if (!sh2.equals(sh3)) {
      return false;
    }
    Ls ls2 = e2.getLs(i);
    Ls ls3 = e3.getLs(i);
    if (!ls2.equals(ls3)) {
      return false;
    }
  }
  String trgt = makeTrgtKey(e2, idx3);
  equalSysTrgt.put(sys, trgt);
//    log.dbg("Conf e3 =", e3);
//    log.dbg("Conf e2 =", e2);
//    log.dbg("last electron is in =", idx3);
  return true;
}
}
