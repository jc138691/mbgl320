package papers.hy_swave.method_ees;
import atom.angular.Spin;
import atom.e_1.SysE1;
import atom.e_2.SysAtomE2;
import atom.energy.ConfHMtrx;
import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import papers.hy_swave.HyLikeSWave;
import scatt.jm_2008.e3.JmDe3;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.target.ScattTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
* Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 8/03/12, 9:20 AM
*/
public class HySWaveEes  extends HyLikeSWave {
public static Log log = Log.getLog(HySWaveEes.class);

public void calc(int newN) {
  SYS_LS = new Ls(0, SPIN);
  N = newN;
  Nt = newN;  // this is just to keep functions like hydrScattTestOk() working
  initProject();
  potScattTestOk();     // out: basisN, orthonN, biorthN
  hydrScattTestOk(TARGET_Z);      // out: pot (for TARGET_Z), orthonNt
  SlaterLcr slater = new SlaterLcr(quadrLcr);

  trgtBasisN = orthonN;
//  AtomUtil.trimTailSLOW(trgtBasisN);     // todo: check if needed
  ScattTrgtE3 trgt = makeTrgtE3(slater);
  trgt.setScreenZ(TARGET_Z - 1);       // Hydrogen-like target has ONE electron
  trgt.setInitTrgtIdx(FROM_CH);
  trgt.setIonGrndEng(0);
  trgt.setNt(orthonNt.size());
  trgt.loadSdcsW();
  trgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);

  ConfHMtrx sysH = makeSysH(SYS_LS, slater);

  EesMethodE2 method = new EesMethodE2(calcOpt);
  method.setTrgtE3(trgt);
  Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
  method.setSysEngs(sEngs);
  method.setSysConfH(sysH);
  method.setOrthonN(orthonN);

  ScattRes res = method.calcSysEngs();                  log.dbg("res=", res);
  setupScattRes(res, method);
  res.writeToFiles();
}


private ScattTrgtE3 makeTrgtE3(SlaterLcr slater) {
  SysE1 tgrtE2 = new SysE1(-TARGET_Z, slater);
  Ls tLs = new Ls(0, Spin.ELECTRON);  // t - for target

  ConfArr tConfArr = ConfArrFactoryE2.makePoetConfE1(orthonN);     log.dbg("tConfArr=", tConfArr);  // NOTE!  orthonN

  ConfHMtrx tH = new ConfHMtrx(tConfArr, tgrtE2);                  log.dbg("tH=\n", new MtrxDbgView(tH));
  FileX.writeToFile(tH.getEigVal().toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgEngs_" + makeLabelBasisOptN());
  FileX.writeToFile(tH.getEngEv(0).toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgEngs_eV_" + makeLabelBasisOptN());

  ScattTrgtE3 res = new ScattTrgtE3();
  res.add(tH);
  res.makeReady();
  return res;
}

private ConfHMtrx makeSysH(Ls sLs, SlaterLcr slater) {
  SysAtomE2 sys = new SysAtomE2(-TARGET_Z, slater);// NOTE -1 for Hydrogen
  ConfArr sConfArr = ConfArrFactoryE2.makeSModelE2(sLs, orthonN, orthonN);   log.dbg("sysArr=", sConfArr);
  ConfHMtrx res = new ConfHMtrx(sConfArr, sys);                  log.dbg("sysConfH=\n", new MtrxDbgView(res));
  return res;
}
@Override
public void calc(int newN, int newNt) {
throw new IllegalArgumentException(log.error("use calc(int newN)"));
}
}