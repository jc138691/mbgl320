package papers.hy_swave.new_method;
import atom.data.AtomHy;
import atom.energy.part_wave.PartHMtrx;
import atom.energy.part_wave.PartHMtrxLcr;
import atom.wf.coulomb.CoulombWFFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
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
public class PotScattKM extends Jm2010CommonLcr {
public static Log log = Log.getLog(PotScattKM.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  PotScattKM runMe = new PotScattKM();
  runMe.setUp();
  runMe.testRun();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("PotScattKM", "110606");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "PotScattKM";
  MODEL_DIR = MODEL_NAME;
  LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;
  // Note: run one at a time as only one set of result files is produced
  runJob();
}
public void setUp() {
  super.setUp();
//    log = Log.getRootLog();
  log.info("log.info(PotScattKM)");
//    JmMethodE1.log.setDbg();
  PBornDirScattTest.log.setDbg();
  log.setDbg();
}
public void runJob() {
  LAMBDA = 1f;
  LCR_N = 301;
  R_LAST = 100;
  ENG_FIRST = 0.1f;
  ENG_LAST = 1.1f;
  ENG_N = 50;
  calc(10);
  calc(12);
  calc(14);
  calc(16);
//  calcJm(18);
//  calcJm(20);
//    calcJm(8);
}
public void calc(int newN) {
  N = newN;
  initProject();
  potScattTestOk();
  pot = CoulombWFFactory.makePotHy_1s_e(rVec);             log.dbg("V_1s(r)=", new VecDbgView(pot));
  PartHMtrx sysH = new PartHMtrxLcr(L, orthonN, pot);
  Vec sysEngs = sysH.getEigVal();            log.dbg("eigVal=", new VecDbgView(sysEngs));
  FuncArr sysWFuncs = sysH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  KmMethodE1 method = new KmMethodE1(calcOpt);
  method.setSysEngs(sysEngs);
  method.setSysH(sysH);
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