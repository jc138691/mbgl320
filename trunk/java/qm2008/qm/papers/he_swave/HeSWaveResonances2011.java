package papers.he_swave;
import atom.angular.Spin;
import atom.data.AtomHe;
import math.vec.Vec;
import qm_station.QMSProject;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.eng.EngModelArr;
import scatt.jm_2008.e2.JmResonancesE2;

import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,18/08/11,11:11 AM
 */
public class HeSWaveResonances2011 extends HeSWaveBasisJm {
  public static Log log = Log.getLog(HeSWaveResonances2011.class);
  public static void main(String[] args) {
    // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HeSWaveResonances2011 runMe = new HeSWaveResonances2011();
    runMe.setUp();
    runMe.testRun();
  }

  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HeSWaveResonances2011", "110818");
    TARGET_Z = AtomHe.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_NAME = "HeSWaveResonances2011";
    MODEL_DIR = MODEL_NAME;
    IGNORE_BUG_PoetHeAtom = true;
    CALC_DENSITY = false;
    SAVE_TRGT_ENGS = true;
//    LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;
//    LAMBDA = 2 * 1.68750; // best single zeta
//    LAMBDA = 3.787828; // best for Nc=10, Nt=30  , n_gamma=5
//    LAMBDA = 2.526; // best for Nc=10, Nt=30  , n_gamma=7
    LAMBDA = 2.05; // best for Nc=14, Nt=14  , n_gamma=5
    // Note: run one at a time as only one set of result files is produced
    setupEng01_1au_SLOW();
    runJob();
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

    Nc = 14;
    int currNt = 14;
    int currN = 31;

    SPIN = Spin.ELECTRON;
    calcJm(currN, currNt);
  }
  public static void setupScttEngTable() {
    scttEngs = new Vec(new double[] {
      0.1,
      0.2,
      0.3,
      0.4,
      0.5,
      0.6,
      0.65,
      0.66,
      0.67,
      0.68,
      0.69,
      0.695,
      0.696,
      0.697,
      0.698,
      0.699,
      0.700,
      0.701,
      0.702,
      0.703,
      0.704,
      0.705,
      0.71,
      0.72,
      0.73,
      0.74,
      0.75,
      0.76,
      0.77,
      0.78,
      0.79,
      0.8,
      0.9,
      1,
    });

    ENG_N = scttEngs.size();
    ENG_FIRST = scttEngs.getFirst();
    ENG_LAST = scttEngs.getLast();
  }
  public static void setupEng01_1au_SLOW() {
    EngModelArr arr = new EngModelArr();

    int n = 69;
    float first = 0.01f;
    float last = 0.69f;
    arr.add(new EngModel(first, last, n));

    int n2 = 1000;
    float first2 = 0.69002f;
    float last2 =  0.71f;
    arr.add(new EngModel(first2, last2, n2));

    int n3 = 1500;
    float first3 = 0.7102f;
    float last3 = 1.0100f;
    arr.add(new EngModel(first3, last3, n3));

    scttEngs = EngGridFactory.makeEngs(arr);

    ENG_N = n + n2 + n3;
    ENG_FIRST = first;
    ENG_LAST = last3;
  }

}
