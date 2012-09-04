package papers.he_plus_swave;
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
import papers.hy_swave.Jm2010Common;
import papers.hy_swave.Jm2010CommonLcr;
import papers.project_setup.ProjCommon;
import scatt.jm_2008.e3.JmDe3;
import scatt.jm_2008.e3.JmMthdBasisJmE3;
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
  Jm2010Common.N = newN;
  Jm2010CommonLcr.Nt = newNt;
  initProject();
  potScattTestOk();     // out: lgrrN, orthNt, lgrrBi
  hydrScattTestOk(Jm2010Common.TARGET_Z);      // out: pt (for TARGET_Z), orthNt
  SlaterLcr slater = new SlaterLcr(Jm2010CommonLcr.quadr);

  trgtWfsNt = Jm2010CommonLcr.orthNt;
  Jm2010CommonLcr.wfN = Jm2010CommonLcr.orthN;
  AtomUtil.trimTailSLOW(trgtWfsNt);
  AtomUtil.trimTailSLOW(Jm2010CommonLcr.wfN);
  ScttTrgtE3 trgt = makeTrgtE3(slater);
  trgt.setScreenZ(Jm2010Common.TARGET_Z - 1);       // Hydrogen-like target has ONE electron
  trgt.setInitTrgtIdx(FROM_CH);
  trgt.setIonGrndEng(0);
  trgt.setNt(Jm2010CommonLcr.orthNt.size());
  trgt.loadSdcsW();
  trgt.removeClosed(Jm2010Common.calcOpt.getGridEng().getLast(), FROM_CH, Jm2010CommonLcr.KEEP_CLOSED_N);

  LsConfHMtrx sysH = makeSysH(SYS_LS, slater);

  JmMthdBasisJmE3 method = new JmMthdBasisJmE3(Jm2010Common.calcOpt);
  method.setExclSysIdx(EXCL_SYS_RESON_IDX);     // [15Jun2011] TODO: remember to remove this
  method.setTrgtE3(trgt);
  Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
  method.setSysEngs(sEngs);
  method.setSysConfH(sysH);
  Vec D = new JmDe3(Jm2010CommonLcr.lgrrBiN, Jm2010CommonLcr.orthN, method.getCalcOpt().getTestOpt());   log.dbg("D_{i<Nt}=must be ZERO=", D); // MUST BE ALL ZERO!!!!!
  method.setOverD(D);

  if (CALC_DENSITY) {
    FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
    FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, Jm2010CommonLcr.quadr);     // NOTE!! convering density to R (not wf)
    FileX.writeToFile(sysDensR.toTab(), ProjCommon.HOME_DIR, ProjCommon.MODEL_DIR, ProjCommon.MODEL_NAME + "_sysDensityR_" + makeLabelTrgtS2(method));
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
  SysE1 tgrtE2 = new SysE1(-Jm2010Common.TARGET_Z, slater);
  Ls tLs = new Ls(0, Spin.ELECTRON);  // t - for target

  LsConfs tConfArr = ConfArrFactoryE2.makePoetConfE1(Jm2010CommonLcr.orthNt);     log.dbg("tConfArr=", tConfArr);

  LsConfHMtrx tH = new LsConfHMtrx(tConfArr, tgrtE2);                                   log.dbg("tH=\n", new MtrxDbgView(tH));
  FileX.writeToFile(tH.getEigEngs().toCSV(), ProjCommon.HOME_DIR, ProjCommon.MODEL_DIR, ProjCommon.MODEL_NAME + "_trgEngs_" + Jm2010Common.makeLabelBasisOptN());
  FileX.writeToFile(tH.getEngEv(0).toCSV(), ProjCommon.HOME_DIR, ProjCommon.MODEL_DIR, ProjCommon.MODEL_NAME + "_trgEngs_eV_" + Jm2010Common.makeLabelBasisOptN());

  ScttTrgtE3 res = new ScttTrgtE3();
  res.add(tH);
  res.makeReady();
  return res;
}

private LsConfHMtrx makeSysH(Ls sLs, SlaterLcr slater) {
  SysE2 sys = new SysE2(Jm2010Common.TARGET_Z, slater);// NOTE 1 for Hydrogen
  LsConfs sConfArr = ConfArrFactoryE2.makeSModelE2(sLs, Jm2010CommonLcr.orthNt, Jm2010CommonLcr.orthN);   log.dbg("sysArr=", sConfArr);
  LsConfHMtrx res = new LsConfHMtrx(sConfArr, sys);                  log.dbg("sysConfH=\n", new MtrxDbgView(res));
  return res;
}
}