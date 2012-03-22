package papers.hy_swave;
import atom.data.AtomHy;
import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.wf.coulomb.CoulombWFFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e1.ScttMthdBaseE1;
import scatt.jm_2008.e1.JmMethodE1;
import scatt.jm_2008.jm.ScattRes;
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
//    JmMethodE1.log.setDbg();
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
  calcJm(20);
//  calc(16);
//  calc(18);
//  calc(20);
//    calc(8);
}
public void calcJm(int newN) {
  N = newN;
  initProject();
  potScattTestOk();
  pot = CoulombWFFactory.makePotHy_1s_e(rVec);         log.dbg("V_1s(r)=", new VecDbgView(pot));
  PotHMtrx sysH = new PotHMtrxLcr(L, orthonN, pot);
////    PotH partH = sysConfH.makePotH();
  Vec sysEngs = sysH.getEigVal();                   log.dbg("eigVal=", new VecDbgView(sysEngs));
  FuncArr sysWFuncs = sysH.getEigFuncArr();         log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  Vec D = new JmD(biorthN, sysWFuncs);              log.dbg("D_{n,N-1}=", D);
  JmMethodE1 method = new JmMethodE1(calcOpt);
  method.setOverD(D);
  method.setSysEngs(sysEngs);
  ScattRes res = method.calcEngGrid();
  log.dbg("res=", res);
//    ScattRes res = method.calcMidSysEngs();                  log.dbg("res=", res);
//
//  FuncVec cross = new FuncVecToString(res.getCross());
//  FileX.writeToFile(cross.toString(), HOME_DIR, "cross", "cross_" + basisOptN.makeLabel() + ".csv");
  setupScattRes(res, method);
  res.setCalcLabel(makeLabel(method));
  res.writeToFiles();
}
protected static String makeLabel(ScttMthdBaseE1 method) {
  return Jm2010Common.makeLabelBasisOptN();
}
}