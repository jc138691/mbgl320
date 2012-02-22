package papers.hy_swave;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.part_wave.PartHMtrx;
import atom.energy.part_wave.PartHMtrxLcr;
import atom.wf.coulomb.CoulombWFFactory;
import math.func.FuncVec;
import math.func.FuncVecToString;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmMethodBaseE1;
import scatt.jm_2008.e1.JmMethodE1;
import scatt.jm_2008.e2.JmMethodBaseE2;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.theory.JmD;

import javax.iox.FileX;
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
  MODEL_NAME = "HySModelJm";
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
  LAMBDA = 1f;
  LCR_N = 301;
  R_LAST = 100;
  ENG_FIRST = 0.01f;
  ENG_LAST = 0.51f;
  ENG_N = 50;
  calcJm(10);
  calcJm(12);
  calcJm(14);
  calcJm(16);
//  calcJm(18);
//  calcJm(20);
//    calcJm(8);
}
public void calcJm(int newN) {
  N = newN;
  initProject();
  jmPotTestOk();
  pot = CoulombWFFactory.makePotHy_1s_e(rVec);
  log.dbg("V_1s(r)=", new VecDbgView(pot));
  PartHMtrx sysH = new PartHMtrxLcr(L, orthonN, pot);
////    PartH partH = sysH.makePartH();
  Vec sysEngs = sysH.getEigVal();
  log.dbg("eigVal=", new VecDbgView(sysEngs));
  FuncArr sysWFuncs = sysH.getEigFuncArr();
  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  Vec D = new JmD(biorthN, sysWFuncs);
  log.dbg("D_{n,N-1}=", D);
  JmMethodE1 method = new JmMethodE1(jmOpt);
  method.setOverD(D);
  method.setSysEngs(sysEngs);
  JmRes res = method.calcEngGrid();
  log.dbg("res=", res);
//    JmRes res = method.calcMidSysEngs();                  log.dbg("res=", res);
//
//  FuncVec cross = new FuncVecToString(res.getCross());
//  FileX.writeToFile(cross.toString(), HOME_DIR, "cross", "cross_" + basisOptN.makeLabel() + ".csv");
  setupJmRes(res, method);
  res.setCalcLabel(makeLabel(method));
  res.writeToFiles();
}
protected static String makeLabel(JmMethodBaseE1 method) {
  return Jm2010Common.makeLabelBasisOptN();
}

}