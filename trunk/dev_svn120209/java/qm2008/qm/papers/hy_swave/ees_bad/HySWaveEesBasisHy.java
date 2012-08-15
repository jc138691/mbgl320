package papers.hy_swave.ees_bad;
import atom.data.AtomHy;
import atom.energy.LsConfHMtrx;
import atom.energy.pw.lcr.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.shell.Ls;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.ScttTrgtE3;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 12/03/12, 10:05 AM
 */
public class HySWaveEesBasisHy extends HySWaveEes {
public static Log log = Log.getLog(HySWaveEesBasisHy.class);

public static void main(String[] args) {
  // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  HySWaveEesBasisHy runMe = new HySWaveEesBasisHy();
  runMe.setUp();
  runMe.testRun();
}
public void setUp() {
  super.setUp();
  log.info("log.info(HySWaveEesBasisHy)");
  log.setDbg();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("HySWaveEesBasisHy", "120308");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "HySWaveEesBasisHy";    MODEL_DIR = MODEL_NAME;
  CALC_TRUE_CONTINUUM = false;
  LAMBDA = 1.5; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;

  USE_CLOSED_CHANNELS = true;
  CALC_DENSITY = false;
  runJob();
}

public void calc(int newN) {      log.setDbg();
  SYS_LS = new Ls(0, SPIN);
  N = newN;
  Nt = newN;
  initProject();
  potScattTestOk();     // out: lgrrN, orthNt, lgrrBi
  hydrScattTestOk(TARGET_Z);      // out: pt (for TARGET_Z), orthNt
  SlaterLcr slater = new SlaterLcr(quadr);

  trgtPotH = new PotHMtrxLcr(L, orthNt, pot);    log.dbg("trgtPotH=", trgtPotH);
  Vec targetEngs = trgtPotH.getEigEngs();            log.dbg("eigVal=", new VecDbgView(targetEngs));
  trgtWfsNt = trgtPotH.getEigWfs();      log.dbg("lgrrN=", new FuncArrDbgView(wfN));
//  AtomUtil.trimTailSLOW(trgtWfsNt);     // todo: check if needed
  wfN = null;
  orthN = null;

  ScttTrgtE3 trgt = makeTrgtE3(slater);
  trgt.setScreenZ(TARGET_Z - 1);       // Hydrogen-like target has ONE electron
  trgt.setInitTrgtIdx(FROM_CH);
  trgt.setIonGrndEng(0);
  trgt.setNt(Nt);
  trgt.loadSdcsW();
  trgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);

  LsConfHMtrx sysH = makeSysH(SYS_LS, slater);
  Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sEngs=", sEngs);

  EesMethodBasisAnyE2 method = new EesMethodBasisAnyE2(calcOpt);
//  EesMthdBasisHyE2_v1_bad method = new EesMthdBasisHyE2_v1_bad(calcOpt);
//  EesMthdBasisHyE2_v2_bad method = new EesMthdBasisHyE2_v2_bad(calcOpt);
//  EesMthdBasisHyE2_v3_leftXi_bad method = new EesMthdBasisHyE2_v3_leftXi_bad(calcOpt);
  method.setTrgtE2(trgt);
  method.setSysEngs(sEngs);
  method.setSysConfH(sysH);
  method.setOrthNt(orthNt);
  method.setWfsE1(trgtWfsNt);

  ScttRes res = method.calcSysEngs();                  log.dbg("res=", res);
  setupScattRes(res, method);
  res.writeToFiles();
}


}