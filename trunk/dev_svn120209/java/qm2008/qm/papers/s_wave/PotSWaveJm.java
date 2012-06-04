package papers.s_wave;
import atom.data.AtomHy;
import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.wf.coulomb.WfFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import papers.hy_swave.Jm2010Common;
import papers.hy_swave.Jm2010CommonLcr;
import qm_station.QMSProject;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e1.JmMthdE1;
import scatt.jm_2008.e1.ScttMthdBaseE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.ScttTrgtFactory;
import scatt.jm_2008.jm.theory.JmD;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 28/05/12, 10:20 AM
 */
public class PotSWaveJm extends Jm2010CommonLcr {
public static Log log = Log.getLog(PotSWaveJm.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  PotSWaveJm runMe = new PotSWaveJm();
  runMe.setUp();
  runMe.testRun();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("PotSWaveJm", "120528");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "PotSWaveJm";
  MODEL_DIR = MODEL_NAME;
  // Note: run one at a time as only one set of result files is produced
  runJob();
}
public void setUp() {
  super.setUp();
//    log = Log.getRootLog();
  log.info("log.info(PotSWaveJm)");
//    JmMthdE1_OLD.log.setDbg();
  log.setDbg();
}
public void runJob() {
  LAMBDA = 1.2;
  LCR_FIRST = -5;
//  LCR_N = 1501;
//  R_LAST = 150;
  LCR_N = 3001;
  R_LAST = 300;

//  ENG_FIRST = 0.84f;
//  ENG_LAST = 0.86;
//  ENG_N = 1001;
  ENG_FIRST = 0.01f;
  ENG_LAST = 10.f;
  ENG_N = (int)(ENG_LAST / ENG_FIRST);

  calc(10);
//  calc(12);
  calc(20);
//  calc(40);
//  calc(16);
//  calc(18);
//  calc(20);
//    calc(8);
}
public void calc(int newN) {
  N = newN;
  initProject();
  potScattTestOk();   // lgrrN, orth, lgrrBi, quadr
  pot = WfFactory.makePotHy_1s_e(rVec);         log.dbg("V_1s(r)=", new VecDbgView(pot));
//  pot = WfFactory.makePotFBornTest(rVec);         log.dbg("V_1s(r)=", new VecDbgView(pot));
  PotHMtrx potH = new PotHMtrxLcr(L, orthN, pot);
  Vec sysEngs = potH.getEigEngs();                   log.dbg("eigVal=", new VecDbgView(sysEngs));
  FuncArr sysWfsN = potH.getEigWfs();         log.dbg("sysWfsN=", new FuncArrDbgView(sysWfsN));
  Vec D = new JmD(lgrrBiN, sysWfsN);              log.dbg("D_{n,N-1}=", D);

//  JmMthdE1_OLD method = new JmMthdE1_OLD(calcOpt); // OLD
  JmMthdE1 mthd = makeMthd(calcOpt);
  mthd.setPotH(potH);
  mthd.setTrgtE2(ScttTrgtFactory.makeEmptyE2());  //EMPTY target!!!
  mthd.setOverD(D);
  mthd.setSysEngs(sysEngs);
  mthd.setWfsE1(sysWfsN); // NOTE! using trgtBasis to pass the E1-sys-basis
  mthd.setQuadr(quadr);
  mthd.setOrth(orthN);
//  mthd.setLgrrBi(lgrrBiN);
//  mthd.setLgrr(lgrrN);
  ScttRes res = mthd.calcForScatEngModel();
  log.dbg("res=", res);
//    ScttRes res = method.calcMidSysEngs();                  log.dbg("res=", res);
//
  setupScattRes(res, mthd);
  res.setCalcLabel(makeLabel(mthd));
  res.writeToFiles();
}
protected String makeLabel(ScttMthdBaseE1 method) {
  return Jm2010Common.makeLabelBasisOptN();
}
protected JmMthdE1 makeMthd(CalcOptE1 calcOpt) {
  return new JmMthdE1(calcOpt);
}
}