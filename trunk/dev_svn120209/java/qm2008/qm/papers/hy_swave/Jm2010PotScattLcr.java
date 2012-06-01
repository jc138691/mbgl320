package papers.hy_swave;
import atom.data.AtomHy;
import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.wf.coulomb.WfFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmMthdE1_OLD;
import scatt.jm_2008.e1.ScttMthdBaseE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.theory.JmD;

import javax.utilx.log.Log;
/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 8:47:43 AM
 */
public class Jm2010PotScattLcr extends Jm2010CommonLcr {
public static Log log = Log.getLog(Jm2010PotScattLcr.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  Jm2010PotScattLcr runMe = new Jm2010PotScattLcr();
  runMe.setUp();
  runMe.testRun();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("Jm2010PotScattLcr", "110606");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "PotScattJM";
  MODEL_DIR = MODEL_NAME;
  LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;
  // Note: run one at a time as only one set of result files is produced
  runJob();
}
public void setUp() {
  super.setUp();
//    log = Log.getRootLog();
  log.info("log.info(Jm2010PotScattLcr)");
//    JmMthdE1_OLD.log.setDbg();
  log.setDbg();
}
public void runJob() {
  LAMBDA = 1.;
  LCR_FIRST = -5;
  LCR_N = 701;
  R_LAST = 200;
  ENG_FIRST = 0.01f;
  ENG_LAST = 4.01f;
  ENG_N = 2001;
//  calc(10);
//  calc(12);
  calc(20);
//  calc(16);
//  calc(18);
//  calc(20);
//    calc(8);
}
public void calc(int newN) {
  N = newN;
  initProject();
  potScattTestOk();
  pot = WfFactory.makePotHy_1s_e(rVec);         log.dbg("V_1s(r)=", new VecDbgView(pot));
  PotHMtrx sysH = new PotHMtrxLcr(L, orthN, pot);
////    PotH partH = sysConfH.makePotH();
  Vec sysEngs = sysH.getEigEngs();                   log.dbg("eigVal=", new VecDbgView(sysEngs));
  FuncArr sysWFuncs = sysH.getEigWfs();         log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  Vec D = new JmD(lgrrBiN, sysWFuncs);              log.dbg("D_{n,N-1}=", D);
  JmMthdE1_OLD method = new JmMthdE1_OLD(calcOpt);
  method.setOverD(D);
  method.setSysEngs(sysEngs);
  ScttRes res = method.calcForScatEngModel();
  log.dbg("res=", res);
//    ScttRes res = method.calcMidSysEngs();                  log.dbg("res=", res);
//
  setupScattRes(res, method);
  res.setCalcLabel(makeLabel(method));
  res.writeToFiles();
}
protected static String makeLabel(ScttMthdBaseE1 method) {
  return Jm2010Common.makeLabelBasisOptN();
}
}