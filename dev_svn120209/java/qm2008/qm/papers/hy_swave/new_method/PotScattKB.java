package papers.hy_swave.new_method;
import atom.data.AtomHy;
import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.wf.coulomb.CoulombWFFactory;
import math.vec.Vec;
import math.vec.VecDbgView;
import papers.hy_swave.Jm2010Common;
import papers.hy_swave.Jm2010CommonLcr;
import qm_station.QMSProject;
import scatt.jm_2008.e1.ScattMethodBaseE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.partial.born.PBornDirScattTest;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 23/02/12, 9:23 AM
 */
public class PotScattKB extends Jm2010CommonLcr {
public static Log log = Log.getLog(PotScattKB.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  PotScattKB runMe = new PotScattKB();
  runMe.setUp();
  runMe.testRun();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("PotScattKB", "110606");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "PotScattKB";
  MODEL_DIR = MODEL_NAME;
  LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;
  // Note: run one at a time as only one set of result files is produced
  runJob();
}
public void setUp() {
  super.setUp();
//    log = Log.getRootLog();
  log.info("log.info(PotScattKB)");
//    JmMethodE1.log.setDbg();
  PBornDirScattTest.log.setDbg();
  KbMethodE1.log.setDbg();
  log.setDbg();
}
public void runJob() {
  LAMBDA = 1f;
  LCR_N = 301;
  R_LAST = 100;
  ENG_FIRST = 0.5f;
  ENG_LAST = 0.6f;
  ENG_N = 101;
//  calc(10);
//  calc(12);
  calc(16);
//  calc(16);
//  calcJm(18);
//  calcJm(20);
//    calcJm(8);
}
public void calc(int newN) {
  N = newN;
  Nt = N -2 ;
  initProject();
  potScattTestOk();   // basisN, biorthN, orthonN, quadrLcr
  hydrScattTestOk(TARGET_Z);  // pot, orthonNt

  pot = CoulombWFFactory.makePotHy_1s_e(rVec);             log.dbg("V_1s(r)=", new VecDbgView(pot));
  PotHMtrx sysH = new PotHMtrxLcr(L, orthonNt, pot);
  Vec sysEngs = sysH.getEigVal();            log.dbg("eigVal=", new VecDbgView(sysEngs));
//  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  KbMethodE1 method = new KbMethodE1(calcOpt);
//  method.setPot(pot);
  method.setSysEngs(sysEngs);
  method.setPotH(sysH);
  method.setOrthonN(orthonN);
  ScattRes res = method.calcEngGrid();      log.dbg("res=", res);
  setupScattRes(res, method);
  res.setCalcLabel(makeLabel(method));
  res.writeToFiles();
}
protected static String makeLabel(ScattMethodBaseE1 method) {
  return Jm2010Common.makeLabelBasisOptN();
}
}