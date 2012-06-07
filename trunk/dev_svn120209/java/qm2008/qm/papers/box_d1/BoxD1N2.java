package papers.box_d1;
import d1.ConfsD1;
import d1.ConfArrFactoryD1;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 9:19 AM
 */
public class BoxD1N2 extends BoxD1Common {
public static Log log = Log.getLog(BoxD1N2.class);
public static void main(String[] args) {
  log.info("-->main()");
  BoxD1N2 runMe = new BoxD1N2();
  runMe.setUp();
  runMe.testRun();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  log.info("-->testRun()");
  MODEL_NAME = "BoxD1N2";
//  project = QMSProject.makeInstance(MODEL_NAME, "120606");//maybe useful to setup different running configs
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_DIR = MODEL_NAME;
  runJob();
}

public void setUp() {
  log.info("-->setUp()");
  super.setUp();
  log.info("log.info(BoxD1N2)");
  BoxD1Common.log.setDbg();
  log.setDbg();
}
public void runJob() {
  log.info("-->runJob()");
  calc();

}
public void calc() {
  log.info("-->calc()");
  initProjOpts();
  libTestsOk();
  setupProjOk();

  ConfsD1 confArr = ConfArrFactoryD1.makeBosonConfN2(basis);
  log.dbg("confArr=\n", confArr);

  int dbgLine = 0;
}
}
