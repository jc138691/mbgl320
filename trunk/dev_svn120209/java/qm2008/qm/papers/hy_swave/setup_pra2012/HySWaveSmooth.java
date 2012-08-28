package papers.hy_swave.setup_pra2012;
import atom.angular.Spin;
import atom.data.AtomHy;
import math.vec.IntVec;
import papers.hy_swave.HySWaveJmBasisHy;
import qm_station.QMSProject;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 24/08/12, 11:46 AM
 */
public class HySWaveSmooth extends HySWaveJmBasisHy {
public static Log log = Log.getLog(HySWaveSmooth.class);

public static void main(String[] args) {
  // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  HySWaveSmooth runMe = new HySWaveSmooth();
  runMe.setUp();
  runMe.testRun();
}

public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  MODEL_NAME = "HySWaveSmooth";
  project = QMSProject.makeInstance(MODEL_NAME, "120405");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_DIR = MODEL_NAME;
  LAMBDA = 1; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;

  // Note: run one at a time as only one set of result files is produced
  setupEngSDCS();
  CALC_TRUE_CONTINUUM = false; // if TRUE, increase LCR_N by about timesSelf 2.5

  CALC_SDCS = false;
  JM_TAIL_N = 1; //
  SDCS_ENG_N = 50;

  USE_CLOSED_CHANNELS = true;
  KEEP_CLOSED_N = 1;
  CALC_DENSITY = false;
  CALC_DENSITY_MAX_NUM = 2;
  SAVE_TRGT_ENGS = true;
  H_OVERWRITE = true;
  runJob();
}
public void runJob() {
  SCTT_ENG_N = 10; // not used
//  AUTO_ENG_POINTS = new IntVec(new int[] {100, 10, 100});
//  SCTT_ENG_MIN = 0.65;
//  SCTT_ENG_MAX = 0.9;
  AUTO_ENG_POINTS = new IntVec(new int[] {20, 11, 12, 10, 11, 12, 10, 11, 12, 10, 11, 12, 10, 11, 12}); // funny number to help with debugging
  SCTT_ENG_MIN = 0.001;
//  SCTT_ENG_MAX = 0.85;

//  SCTT_ENG_MIN = 0.85;
  SCTT_ENG_MAX = 40;

  int currNt = 30;
  int currN = 31;
  LCR_FIRST = -5;
  LCR_N = 1001;
  R_LAST = 200;
//  LCR_N = 10001;
//  R_LAST = 500;

  int MIN_NT = 29;
  int MAX_NT = 31;
  for (currNt = MIN_NT; currNt <= MAX_NT; currNt++) {
    SPIN = Spin.SINGLET;
    calc(currN, currNt);

    SPIN = Spin.TRIPLET;
    calc(currN, currNt);
  }
}
}
