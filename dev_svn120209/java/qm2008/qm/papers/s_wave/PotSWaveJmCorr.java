package papers.s_wave;
import atom.data.AtomHy;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.e1.JmMthdCorrE1;
import scatt.jm_2008.e1.JmMthdE1;
import scatt.jm_2008.e1.ScttMthdBaseE1;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 11:00 AM
 */
public class PotSWaveJmCorr extends PotSWaveJm {
public static Log log = Log.getLog(PotSWaveJmCorr.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  PotSWaveJmCorr runMe = new PotSWaveJmCorr();
  runMe.setUp();
  runMe.testRun();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  MODEL_NAME = "PotSWaveJmCorr";
//  MODEL_NAME = "PotSWaveJmCorr";
  MODEL_DIR = MODEL_NAME;
  project = QMSProject.makeInstance(MODEL_NAME, "120528");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  // Note: run one at a time as only one set of result files is produced

  JM_TAIL_N = 1;
  runJob();
}
protected JmMthdE1 makeMthd(JmCalcOptE1 calcOpt) {
  return new JmMthdCorrE1(calcOpt);
}
protected String makeLabel(ScttMthdBaseE1 method) {
  return lgrrOptN.makeLabel() + "_TAIL"+JM_TAIL_N + ".dat";
}
}