package papers.hy_swave.new_method;
import atom.data.AtomHy;
import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.wf.coulomb.CoulombWFFactory;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
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
public class PotScattEA extends Jm2010CommonLcr {
public static Log log = Log.getLog(PotScattEA.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  PotScattEA runMe = new PotScattEA();
  runMe.setUp();
  runMe.testRun();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("PotScattEA", "110606");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "PotScattEA";
  MODEL_DIR = MODEL_NAME;
  LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;
  // Note: run one at a time as only one set of result files is produced
  runJob();
}
public void setUp() {
  super.setUp();
//    log = Log.getRootLog();
  log.info("log.info(PotScattEA)");
//    JmMethodE1.log.setDbg();
  PBornDirScattTest.log.setDbg();
  EaMethodE1v1_wrong.log.setDbg();
  log.setDbg();
}
public void runJob() {
  LAMBDA = 1.2;
  LCR_FIRST = -5;
  LCR_N = 1501;
  R_LAST = 150;
//  LCR_N = 301;
//  R_LAST = 150;
  ENG_FIRST = 0.1f;
  ENG_LAST = 1.1f;
  ENG_N = 101;
//  LAMBDA = 1.0;  calc(20);
//  LAMBDA = 1.1;  calc(20);

  double LAMBDA_MIN = 1;
  double LAMBDA_MAX = 1.5;
  int LAMBDA_N = 51;
  StepGrid lamGrid = new StepGrid(new StepGridModel(LAMBDA_MIN, LAMBDA_MAX, LAMBDA_N));
  for (int i = 0; i < lamGrid.size(); i++) {
     LAMBDA = lamGrid.get(i);
     calc(20);
  }
}
public void calc(int newN) {
  N = newN;
  initProject();
  potScattTestOk();   // basisN, biorthN, orthonN, quadrLcr
//  hydrScattTestOk(TARGET_Z);  // pot, orthonNt

  pot = CoulombWFFactory.makePotHy_1s_e(rVec);             log.dbg("V_1s(r)=", new VecDbgView(pot));
  PotHMtrx sysH = new PotHMtrxLcr(L, orthonN, pot);
  Vec sysEngs = sysH.getEigVal();            log.dbg("eigVal=", new VecDbgView(sysEngs));
//  EaMethodE1v2_idea method = new EaMethodE1v2_idea(calcOpt);
  EaMethodE1v2_ok method = new EaMethodE1v2_ok(calcOpt);
//  EaMethodE1v3_idea method = new EaMethodE1v3_idea(calcOpt);
//  method.setPot(pot);
  method.setSysEngs(sysEngs);
  method.setPotH(sysH);
  method.setOrthonN(orthonN);
//  ScattRes res = method.calcEngGrid();      log.dbg("res=", res);
  ScattRes res = method.calcSysEngs();                  log.dbg("res=", res);
  setupScattRes(res, method);
  res.setCalcLabel(makeLabel(method));
  res.writeToFiles();
}
protected static String makeLabel(ScattMethodBaseE1 method) {
  return Jm2010Common.makeLabelBasisOptN();
}
}