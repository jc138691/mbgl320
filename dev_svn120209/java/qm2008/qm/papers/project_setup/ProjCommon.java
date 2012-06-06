package papers.project_setup;
import project.workflow.task.test.FlowTest;
import qm_station.QMS;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 9:17 AM
 */
public class ProjCommon extends FlowTest {
protected static ProjTestOpt testOpt;
protected static String HOME_DIR = "HOME_DIR";
protected static String MODEL_NAME = "MODEL_NAME";
protected static String MODEL_DIR = "MODEL_DIR";
public void setUp() {
  Log.addGlobal(System.out);
}
}
