package papers.hy_swave.setup_SDCS;
import atom.angular.Spin;
import atom.data.AtomHy;
import papers.hy_swave.HySWaveJmBasisHy;
import qm_station.QMSProject;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 5/04/12, 1:47 PM
 */
public class HySWaveJmBasisHy_SDCS extends HySWaveJmBasisHy {
public static Log log = Log.getLog(HySWaveJmBasisHy_SDCS.class);

public static void main(String[] args) {
  // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  HySWaveJmBasisHy_SDCS runMe = new HySWaveJmBasisHy_SDCS();
  runMe.setUp();
  runMe.testRun();
}

public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("HySWaveBasisHySDCS", "120405");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "HySWaveBasisHySDCS";    MODEL_DIR = MODEL_NAME;
  LAMBDA = 1; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;
//    LAMBDA = 1.7;

  // Note: run one at a time as only one set of result files is produced
  setupEngSDCS();
  CALC_TRUE_CONTINUUM = true; // if TRUE, increase LCR_N by about times 2.5

  CALC_SDCS = true;
  KATO_N = 10; //
  SDCS_ENG_N = 10;

  USE_CLOSED_CHANNELS = true;
  KEEP_CLOSED_N = 1;
  CALC_DENSITY = false;
  runJob();
}
public void runJob() {
    // upto N=40
//  int currNt = 30;
//  int currN = 31;
//  LCR_FIRST = -5;
//  LCR_N = 701;
//  R_LAST = 150;

  int currNt = 30;
  int currN = 31;
  LCR_FIRST = -5;
  LCR_N = 2001;
  R_LAST = 200;

//      // upto N=40
//    int currNt = 40;
//    int currN = 41;
//    LCR_FIRST = -5;
//    LCR_N = 701;
//    R_LAST = 200;

//    // Nt= 50
//    currN = 51;
//    LCR_FIRST = -5;
//    LCR_N = 901;
//    R_LAST = 250;

//    // Nt= 60
//    currN = 61;
//    LCR_FIRST = -5;
//    LCR_N = 1101;
//    R_LAST = 300;

  SPIN = Spin.SINGLET;
  calc(currN, currNt);

//  SPIN = Spin.TRIPLET;
//  calc(currN, currNt);
}
}
