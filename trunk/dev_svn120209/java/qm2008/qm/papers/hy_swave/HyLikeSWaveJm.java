package papers.hy_swave;
import atom.AtomUtil;
import atom.angular.Spin;
import atom.e_1.SysE1;
import atom.e_2.SysE2;
import atom.energy.LsConfHMtrx;
import atom.energy.slater.SlaterLcr;
import atom.shell.LsConfs;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import atom.wf.lcr.LcrFactory;
import math.func.arr.FuncArr;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import scatt.jm_2008.e3.JmDe3;
import scatt.jm_2008.e3.JmMethodJmBasisE3;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.ScttTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
* dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/06/11,10:13 AM
*/
public abstract class HyLikeSWaveJm extends HyLikeSWave {
public static Log log = Log.getLog(HyLikeSWaveJm.class);

public void calc(int newN, int newNt) {
  SYS_LS = new Ls(0, SPIN);
  N = newN;
  Nt = newNt;
  initProject();
  potScattTestOk();     // out: lgrrN, orthNt, lgrrBi
  hydrScattTestOk(TARGET_Z);      // out: pt (for TARGET_Z), orthNt
  SlaterLcr slater = new SlaterLcr(quadr);

  trgtWfsNt = orthNt;
  wfN = orthN;
  AtomUtil.trimTailSLOW(trgtWfsNt);
  AtomUtil.trimTailSLOW(wfN);
  ScttTrgtE3 trgt = makeTrgtE3(slater);
  trgt.setScreenZ(TARGET_Z - 1);       // Hydrogen-like target has ONE electron
  trgt.setInitTrgtIdx(FROM_CH);
  trgt.setIonGrndEng(0);
  trgt.setNt(orthNt.size());
  trgt.loadSdcsW();
  trgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);

  LsConfHMtrx sysH = makeSysH(SYS_LS, slater);

  JmMethodJmBasisE3 method = new JmMethodJmBasisE3(calcOpt);
  method.setExclSysIdx(EXCL_SYS_RESON_IDX);     // [15Jun2011] TODO: remember to remove this
  method.setTrgtE3(trgt);
  Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
  method.setSysEngs(sEngs);
  method.setSysConfH(sysH);
  Vec D = new JmDe3(lgrrBiN, orthN, method.getCalcOpt().getTestOpt());   log.dbg("D_{i<Nt}=must be ZERO=", D); // MUST BE ALL ZERO!!!!!
  method.setOverD(D);

  if (CALC_DENSITY) {
    FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
    FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadr);     // NOTE!! convering density to R (not wf)
    FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelTrgtS2(method));
  }

  ScttRes res;
  if (scttEngs != null) {
    res = method.calc(scttEngs);                  log.dbg("res=", res);
  }
  else {
//    res = method.calcScttEngModel();                  log.dbg("res=", res);
    res = method.calcForMidSysEngs();                  log.dbg("res=", res);
  }
  setupScattRes(res, method);
  res.writeToFiles();
}


private ScttTrgtE3 makeTrgtE3(SlaterLcr slater) {
//    SysE1 tgrtE2 = new SysHy(slater);
  SysE1 tgrtE2 = new SysE1(-TARGET_Z, slater);
  Ls tLs = new Ls(0, Spin.ELECTRON);  // t - for target

  LsConfs tConfArr = ConfArrFactoryE2.makePoetConfE1(orthNt);     log.dbg("tConfArr=", tConfArr);

  LsConfHMtrx tH = new LsConfHMtrx(tConfArr, tgrtE2);                                   log.dbg("tH=\n", new MtrxDbgView(tH));
  FileX.writeToFile(tH.getEigEngs().toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgEngs_" + makeLabelBasisOptN());
  FileX.writeToFile(tH.getEngEv(0).toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgEngs_eV_" + makeLabelBasisOptN());

  ScttTrgtE3 res = new ScttTrgtE3();
  res.add(tH);
  res.makeReady();
  return res;
}

private LsConfHMtrx makeSysH(Ls sLs, SlaterLcr slater) {
  SysE2 sys = new SysE2(TARGET_Z, slater);// NOTE 1 for Hydrogen
  LsConfs sConfArr = ConfArrFactoryE2.makeSModelE2(sLs, orthNt, orthN);   log.dbg("sysArr=", sConfArr);
  LsConfHMtrx res = new LsConfHMtrx(sConfArr, sys);                  log.dbg("sysConfH=\n", new MtrxDbgView(res));
  return res;
}
}