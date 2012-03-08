package papers.hy_swave.method_ees;
import atom.angular.Spin;
import atom.data.AtomHy;
import atom.data.AtomUnits;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import papers.hy_swave.HyLikeSWaveJm;
import qm_station.QMSProject;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.eng.EngModelArr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 8/03/12, 2:51 PM
 */
public class HySWaveEes_Test extends HySWaveEes {
  public static Log log = Log.getLog(HySWaveEes_Test.class);

  public static void main(String[] args) {
    // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HySWaveEes_Test runMe = new HySWaveEes_Test();
    runMe.setUp();
    runMe.testRun();
  }
  public void setUp() {
    super.setUp();
    log.info("log.info(HySWaveEes_Test)");
    log.setDbg();
  }
  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HySWaveEes", "120308");
    TARGET_Z = AtomHy.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_NAME = "HySWaveEes";    MODEL_DIR = MODEL_NAME;
    CALC_TRUE_CONTINUUM = false; // if TRUE, increase LCR_N by about times 2.5
    LAMBDA = 1.5; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;

    USE_CLOSED_CHANNELS = true;
    CALC_DENSITY = false;
//    EXCL_SYS_RESON_IDX = 18;
    runJob();
  }
  public void runJob() {
    int currN = 20;
    LCR_FIRST = -5;
    LCR_N = 1501;
    R_LAST = 150;

    ENG_FIRST = 0.3;
    ENG_LAST = 0.5;

    double LAMBDA_MIN = 1;
    double LAMBDA_MAX = 1.5;
    int LAMBDA_N = 51;
    StepGrid lamGrid = new StepGrid(new StepGridModel(LAMBDA_MIN, LAMBDA_MAX, LAMBDA_N));
    for (int i = 0; i < lamGrid.size(); i++) {
      LAMBDA = lamGrid.get(i);
      SPIN = Spin.SINGLET;
      calc(currN);
    }

//    SPIN = Spin.TRIPLET;
//    calc(currN);
  }


}