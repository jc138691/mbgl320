package papers.he_plus_swave;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.data.AtomUnits;
import math.complex.Cmplx2F1;
import qm_station.QMSProject;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.jm.theory.JmTools;

import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/06/11,8:41 AM
 */
// HePOne - (He plus one)-ion
public class HePlusSWaveJm extends HyLikeSWaveJm {
  public static Log log = Log.getLog(HePlusSWaveJm.class);
  public static void main(String[] args) {
    // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HePlusSWaveJm runMe = new HePlusSWaveJm();
    runMe.setUp();
    runMe.testRun();
  }
  public void setUp() {
    super.setUp();
    log.info("log.info(HePlusSWaveJm)");
    JmMthdBaseE2.log.setDbg();
    JmTools.log.setDbg();
    Cmplx2F1.log.setDbg();
    log.setDbg();
  }
  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    MODEL_NAME = "HePlusSWaveJm";    MODEL_DIR = MODEL_NAME;
    project = QMSProject.makeInstance(MODEL_NAME, "110606");
    TARGET_Z = AtomHe.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    CALC_TRUE_CONTINUUM = false; // if TRUE, increase LCR_N by about timesSelf 2.5
    LAMBDA = 2; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;

    // Note: run one at a time as only one set of result files is produced
    setupEngTCS();
    USE_CLOSED_CHANNELS = true;
    CALC_DENSITY = false;
    runJob();
  }


  public void setupEngTCS() {
    SCTT_ENG_MIN = (float) AtomUnits.fromEV(1.0);
    SCTT_ENG_MAX = (float)AtomUnits.fromEV(101.0);
    SCTT_ENG_N = 1001;
  }

  public void runJob() {
//    // Nt= 80
//    int currN = 81;
//    LCR_FIRST = -7;
//    LCR_N = 1601;
//    R_LAST = 450;

//    // Nt= 70
//    int currN = 71;
//    LCR_FIRST = -5;
//    LCR_N = 1201;
//    R_LAST = 330;

    // upto N=50
//    LCR_FIRST = -5;  //-5
//    LCR_N = 1001;  //901
//    R_LAST = 250;

    // upto N=40
    LCR_FIRST = -5;
    LCR_N = 701;
    R_LAST = 200;

    int currNt = 20;
    int currN = 21;
//    int currN = currNt + 1;

    SPIN = Spin.SINGLET;
    calc(currN, currNt);
  }

}