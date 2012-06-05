package papers.hy_swave;
import atom.angular.Spin;
import atom.data.AtomHy;
import atom.data.AtomUnits;
import qm_station.QMSProject;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.eng.EngModelArr;

import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,13/04/11,3:28 PM
 */
public class HySWaveJm extends HyLikeSWaveJm {
  public static Log log = Log.getLog(HySWaveJm.class);

  public static void main(String[] args) {
    // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HySWaveJm runMe = new HySWaveJm();
    runMe.setUp();
    runMe.testRun();
  }
  public void setUp() {
    super.setUp();
    log.info("log.info(HySWaveJm)");
    log.setDbg();
  }
  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HySWaveJm", "110606");
    TARGET_Z = AtomHy.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_NAME = "HySWaveJm";    MODEL_DIR = MODEL_NAME;
    CALC_TRUE_CONTINUUM = false; // if TRUE, increase LCR_N by about timesSelf 2.5
    LAMBDA = 1.5; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;

    // Note: run one at a time as only one set of result files is produced
    ENG_FIRST = 0.01;
    ENG_LAST = 10;
    ENG_N = 101;
//    setupEngExcite();
//    setupResonances_n2_S1();
//    setupResonances_n2_n3_S1();
//    setupResonances_n2_n3_S3();
//    setupEngTICS();
//    setupEngSDCS();
    USE_CLOSED_CHANNELS = true;
    CALC_DENSITY = false;
//    EXCL_SYS_RESON_IDX = 18;
    runJob();

//    USE_CLOSED_CHANNELS = false;
//    runJob();
  }
  public void runJob() {
      // upto N=40
    int currNt = 20;
    int currN = 21;
    LCR_FIRST = -5;
    LCR_N = 1501;
    R_LAST = 150;

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

//    KEEP_CLOSED_N = 10;

    SPIN = Spin.SINGLET;
    calc(currN, currNt);

    SPIN = Spin.TRIPLET;
    calc(currN, currNt);
  }
  public void runJob_OLD() {
    // upto N=40
    LCR_FIRST = -5;
    LCR_N = 701;
    R_LAST = 200;

//    // Nt= 90
//    int currN = 91;
//    LCR_FIRST = -5;
//    LCR_N = 1601;
//    R_LAST = 500;

//    // Nt= 80
//    int currN = 81;
//    LCR_FIRST = -5;
//    LCR_N = 1401;
//    R_LAST = 400;

//    // Nt= 70
//    int currN = 71;
//    LCR_FIRST = -5;
//    LCR_N = 1201;
//    R_LAST = 330;

//    // Nt= 60
//    int currN = 61;
//    LCR_FIRST = -5;
//    LCR_N = 1101;
//    R_LAST = 300;

//    // Nt= 50
//    int currN = 31;
//    LCR_FIRST = -5;  //-5
//    LCR_N = 901;  //901
//    R_LAST = 250;

    // Nt= 9
    int currN = 21;
//    int currNt = 10;
    int currNt = currN - 1;
    LCR_FIRST = -5;  //-5
    LCR_N = 901;  //901
    R_LAST = 250;

    SPIN = Spin.SINGLET;
    calc(currN, currNt);

    SPIN = Spin.TRIPLET;
    calc(currN, currNt);
  }

public void setupResonances_n2_S1() {
//    ENG_FIRST = (float) AtomUnits.fromEV(10.1);
  ENG_FIRST = (float) AtomUnits.fromEV(9.0);
  ENG_LAST = (float)AtomUnits.fromEV(10.25);
  ENG_N = 15001;
}
public void setupResonances_n2_S1_TestClosed() {
//    ENG_FIRST = (float) AtomUnits.fromEV(10.1);
  ENG_FIRST = (float) AtomUnits.fromEV(10.10);
  ENG_LAST = (float)AtomUnits.fromEV(10.7);
  ENG_N = 151;
}


  public void setupResonances_n2_n3_S1() {
    EngModelArr arr = new EngModelArr();

    int n = 4001;
    float first = (float)AtomUnits.fromEV(10.1);
    float last = (float)AtomUnits.fromEV(10.5);
    arr.add(new EngModel(first, last, n));

    int n2 = 5001;
    float first2 = (float)AtomUnits.fromEV(12.0);
    float last2 = (float)AtomUnits.fromEV(12.5);
    arr.add(new EngModel(first2, last2, n2));

    scttEngs = EngGridFactory.makeEngs(arr);

    ENG_N = n + n2;
    ENG_FIRST = first;
    ENG_LAST = last2;
  }

  public void setupResonances_n2_n3_S3() {
    EngModelArr arr = new EngModelArr();

    int n = 6001;
    float first = (float)AtomUnits.fromEV(10.1);
    float last = (float)AtomUnits.fromEV(10.7);
    arr.add(new EngModel(first, last, n));

    int n2 = 5001;
    float first2 = (float)AtomUnits.fromEV(12.0);
    float last2 = (float)AtomUnits.fromEV(12.5);
    arr.add(new EngModel(first2, last2, n2));

    scttEngs = EngGridFactory.makeEngs(arr);

    ENG_N = n + n2;
    ENG_FIRST = first;
    ENG_LAST = last2;
  }

  public void setupEngTICS() {
    ENG_FIRST = 0.5f;    // TICS
    ENG_LAST = 5.5f;
    ENG_N = 501;
  }

  public void setupEngExcite() {
    ENG_FIRST = 0.3f;
    ENG_LAST = 0.5f;
    ENG_N = 501;
  }

  public void setupEngSDCS() {
    ENG_FIRST = 1f; // SDCS
    ENG_LAST = 2f;
    ENG_N = 3;
  }


}