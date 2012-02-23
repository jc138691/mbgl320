package scatt.jm_2008.jm.coulomb;

import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.lcr.LagrrBiLcr;
import scatt.partial.wf.JmCoulombLcr;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 17/05/2010, 11:43:17 AM
 */
public class JmCoulombLcrTest extends FlowTest {
  public static Log log = Log.getLog(JmCoulombLcrTest.class);
  private static final double MAX_INTGRL_ERR = 1e-4;
  private static JmCoulombLcr cont;
  private static FuncArr target;
  public JmCoulombLcrTest(JmCoulombLcr cont, FuncArr target) {
    super(JmCoulombLcrTest.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
    this.cont = cont;
    this.target = target;
    setMaxErr(MAX_INTGRL_ERR);
  }
  public JmCoulombLcrTest() {
  }
  public void testNorm() {
    log.dbg("testing " + LagrrBiLcr.HELP);
    log.dbg("running JmLagrrBiLCRRTest: JM-Laguerre and Bi-diagonal Laguerre function orthonormality");
    log.dbg("TEST_{n,m} = Intergal[0,rMax] R_n(r) * BiR_m(r) = delta(n, m)");
    log.dbg("Max relative intergation error =" + getMaxErr());
    WFQuadrLcr w = cont.getQuadrLcr();
    for (int n = 0; n < cont.size(); n++) {
      FuncVec f = cont.getFunc(n);
      for (int m = 0; m < target.size(); m++) {
        FuncVec f2 = target.getFunc(m);
        double norm = w.calcOverlap(f, f2); 
        if (n == m) {
          log.info("w.calcOverlap(f, f2)=" + norm);
        }
        else {
//          double corr = Mathx.delta(n, m);
          assertEquals("TEST[" + n + ", " + m +"]", 0, norm, true);
        }
      }
    }
  }
}