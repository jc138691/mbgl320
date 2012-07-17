package papers.he_swave.pra_2012_setup;
import atom.angular.Spin;
import atom.data.AtomHe;
import papers.he_swave.HeSWaveBasisJm;
import qm_station.QMSProject;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.eng.EngModelArr;

import javax.utilx.log.Log;
/**
* dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,18/08/11,11:11 AM
*/
public class HeSWaveRes2012 extends HeSWaveBasisJm {
public static Log log = Log.getLog(HeSWaveRes2012.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  HeSWaveRes2012 runMe = new HeSWaveRes2012();
  runMe.setUp();
  runMe.testRun();
}

public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  MODEL_NAME = "HeSWaveRes2012";
  project = QMSProject.makeInstance(MODEL_NAME, "110818");
  TARGET_Z = AtomHe.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_DIR = MODEL_NAME;
  CALC_DENSITY = false;
  CALC_DENSITY_MAX_NUM = 2;
  SAVE_TRGT_ENGS = true;
  H_OVERWRITE = true;
//    LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;
//    LAMBDA = 2 * 1.68750; // best single zeta
//    LAMBDA = 3.787828; // best for Nc=10, Nt=30  , n_gamma=5
//    LAMBDA = 2.526; // best for Nc=10, Nt=30  , n_gamma=7
//    LAMBDA = 2.05; // best for Nc=14, Nt=14  , n_gamma=5
  // Note: run one at a time as only one set of result files is produced
  setupResEngs_SLOW();
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
  // see HeAtomMM_try
  LCR_FIRST = -5. - 2. * Math.log(TARGET_Z);   log.dbg("LCR_FIRST=", LCR_FIRST);
  LCR_N = 3001;  //901
  R_LAST = 300;   // 200 for N=30

  // upto N=40
//    LCR_FIRST = -5;
//    LCR_N = 701;
//    R_LAST = 200;

//    LAMBDA = 4.0; // best for Nc=30, Nt=30  , n_gamma=5
//    Nc = 50;
//    int currNt = 50;
//    int currN = 51;

//    LAMBDA = 4.0; // best for Nc=30, Nt=30  , n_gamma=5
//    Nc = 45;
//    int currNt = 45;
//    int currN = 46;

//    LAMBDA = 4.0; // best for Nc=30, Nt=30  , n_gamma=5
//    Nc = 40;
//    int currNt = 40;
//    int currN = 41;

//    LAMBDA = 4.0; // best for Nc=30, Nt=30  , n_gamma=5
//    Nc = 35;
//    int currNt = 35;
//    int currN = 36;

//    LAMBDA = 4.0; // best for Nc=30, Nt=30  , n_gamma=5
//    Nc = 30;
//    int currNt = 30;
//    int currN = 31;

//    LAMBDA = 3; // best for Nc=25, Nt=25  , n_gamma=5
//    Nc = 25;
//    int currNt = 25;
//    int currN = 31;

//    LAMBDA = 2.773; // best for Nc=22, Nt=22  , n_gamma=5
//    Nc = 22;
//    int currNt = 22;
//    int currN = 31;

//    LAMBDA = 2.582; // best for Nc=20, Nt=20  , n_gamma=5
//    Nc = 20;
//    int currNt = 20;
//    int currN = 31;

//    LAMBDA = 2.397; // best for Nc=18, Nt=18  , n_gamma=5
//    LAMBDA = 2; // best for Nc=18, Nt=18  , n_gamma=5
//    Nc = 18;
//    int currNt = 18;
//    int currN = 31;

//    LAMBDA = 2.306; // best for Nc=17, Nt=17  , n_gamma=5
//    Nc = 17;
//    int currNt = 17;
//    int currN = 31;

//    LAMBDA = 2.22; // best for Nc=16, Nt=16  , n_gamma=5
//    Nc = 16;
//    int currNt = 16;
//    int currN = 31;

//    LAMBDA = 2.132; // best for Nc=15, Nt=15  , n_gamma=5
//    Nc = 15;
//    int currNt = 15;
//    int currN = 31;

//    LAMBDA = 2.048; // best for Nc=14, Nt=14  , n_gamma=5
//    Nc = 14;
//    int currNt = 14;
//    int currN = 31;

//    LAMBDA = 1.964; // best for Nc=13, Nt=13  , n_gamma=5
//    Nc = 13;
//    int currNt = 13;
//    int currN = 31;

//    LAMBDA = 1.881; // best for Nc=12, Nt=12  , n_gamma=5
//  LAMBDA = 2; // best for Nc=12, Nt=12  , n_gamma=5
//  Nc = 10;
//  int currNt = 20;
//  int currN = 55;  // 35-done; 50-done;


//    LAMBDA = 1.881; // best for Nc=12, Nt=12  , n_gamma=5
  LAMBDA = 2; // best for Nc=12, Nt=12  , n_gamma=5
  Nc = 10;
  int currNt = 20;
  int currN = 21;  // 35-done; 50-done;

//    LAMBDA = 1.807; // best for Nc=11, Nt=11  , n_gamma=5
//    Nc = 11;
//    int currNt = 11;
//    int currN = 40;

//    LAMBDA = 1.728; // best for Nc=10, Nt=10  , n_gamma=5
//    LAMBDA = 1.5;
//    Nc = 10;
//    int currNt = 10;
//    int currN = 40;

  SPIN = Spin.ELECTRON;
  calc(currN, currNt);
}
public static void setupResEngs_SLOW() {
  EngModelArr arr = new EngModelArr();
  arr.add(new EngModel(0.204,  0.704,  501));
  arr.add(new EngModel(0.704,  0.705,  1001));
  arr.add(new EngModel(0.705,  0.730,  501));
  arr.add(new EngModel(0.730,  0.740,  1001));
  arr.add(new EngModel(0.740,  0.810,  501));
  arr.add(new EngModel(0.810,  0.811,  1001));
  arr.add(new EngModel(0.811,  0.818,  501));
  arr.add(new EngModel(0.818,  0.819,  1001));
  arr.add(new EngModel(0.819,  0.842,  501));
//    arr.add(new EngModel(0.845,  3.845,  3001));
  scttEngs = EngGridFactory.makeEngs(arr);
  ENG_FIRST = scttEngs.getFirst();
  ENG_LAST = scttEngs.getLast();
  ENG_N = scttEngs.size();
}

}
