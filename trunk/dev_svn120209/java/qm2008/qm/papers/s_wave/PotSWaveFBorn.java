package papers.s_wave;
import atom.data.AtomHy;
import atom.energy.pw.PotHMtrx;
import atom.energy.pw.lcr.PotHMtrxLcr;
import atom.wf.coulomb.WfFactory;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e1.FbMthdE1;
import scatt.jm_2008.jm.ScttRes;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 28/05/12, 11:12 AM
 */
public class PotSWaveFBorn extends PotSWaveJm {
public static Log log = Log.getLog(PotSWaveFBorn.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  PotSWaveFBorn runMe = new PotSWaveFBorn();
  runMe.setUp();
  runMe.testRun();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("PotFBorn", "120528");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "PotFBorn";
  MODEL_DIR = MODEL_NAME;
  LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;
  // Note: run one at a time as only one set of result files is produced
  runJob();
}
public void calc(int newN) {
  N = newN;
  initProject();
  potScattTestOk();
//  pot = WfFactory.makePotHy_1s_e(vR);         log.dbg("V_1s(r)=", new VecDbgView(pot));
  pot = WfFactory.makePotFBornTest(vR);         log.dbg("V_1s(r)=", new VecDbgView(pot));
  PotHMtrx sysH = new PotHMtrxLcr(L, orthN, pot);
  Vec sysEngs = sysH.getEigEngs();                   log.dbg("eigVal=", new VecDbgView(sysEngs));

  FbMthdE1 method = new FbMthdE1(calcOpt);
  method.setSysEngs(sysEngs);
  method.setPotH(sysH);
  method.setOrth(orthN);

  ScttRes res = method.calcScttEngModel();      log.dbg("res=", res);

  setupScattRes(res, method);
  res.setCalcLabel(makeLabel(method));
  res.writeToFiles();
}
}
