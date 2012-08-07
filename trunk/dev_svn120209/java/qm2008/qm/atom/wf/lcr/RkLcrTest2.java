package atom.wf.lcr;

import atom.wf.coulomb.WfFactory;
import math.func.FuncVec;
import math.vec.Vec;
import math.vec.metric.DistMaxAbsErr;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 16/02/2010, 10:23:47 AM
 */
public class RkLcrTest2 extends FlowTest {
  public static Log log = Log.getLog(RkLcrTest2.class);
  private static WFQuadrLcr quadr;
  private static final double MAX_INTGRL_ERR = 1e-10;

  public RkLcrTest2(WFQuadrLcr w) {
    super(RkLcrTest2.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
    this.quadr = w;
    setMaxErr(MAX_INTGRL_ERR);
  }
  public RkLcrTest2() {
    super(RkLcrTest2.class);
  }
  public void testCalcRkLcr() throws Exception {
    Vec r = quadr.getR();
    FuncVec f = WfFactory.makeP1s(r, 1.);
    f.multSelf(quadr.getDivSqrtCR());
    double res = quadr.calcInt(f, f);
    setMaxErr(MAX_INTGRL_ERR);
    assertEquals("<1s|1s>", 0, Math.abs(res - 1));
    FuncVec T = WfFactory.makeY_0_1s(r); // valid
    FuncVec Y = new YkLcr(quadr, f, f, 0).calcYk();
//    assertEquals(0, Math.abs(T.distSLOW(Y)), 2e-9);
    assertEquals("Y_0_1s", 0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)));
    res = RkLcr.calc(quadr, f, f, f, f, 0);

    // from p236 of C.F.Fischer
    assertEquals("R_1s", 0, Math.abs(res - 5. / 8));
    FuncVec f2s = WfFactory.makeP2s(r, 1.);
    f2s.multSelf(quadr.getDivSqrtCR());

    res = quadr.calcInt(f2s, f2s);
    assertEquals("2s", 0, Math.abs(res - 1));

    res = RkLcr.calc(quadr, f, f2s, f, f2s, 0);
    assertEquals("2s1s", 0, Math.abs(res - 17. / 81));

    res = RkLcr.calc(quadr, f2s, f, f2s, f, 0);
    assertEquals("R1s_s2", 0, Math.abs(res - 17. / 81));

    res = RkLcr.calc(quadr, f, f2s, f2s, f, 0);
    assertEquals("R2s", 0, Math.abs(res - 16. / 729));

    res = RkLcr.calc(quadr, f2s, f, f, f2s, 0);
    assertEquals("R_exc", 0, Math.abs(res - 16. / 729));

    res = RkLcr.calc(quadr, f2s, f2s, f2s, f2s, 0);
    assertEqualsRel("F_2s2s =", 77. / 512, res, true);

    FuncVec f2p = WfFactory.makeP2p(r, 1.);
    f2p.multSelf(quadr.getDivSqrtCR());
    res = quadr.calcInt(f2p, f2p);
    int count = 0;
    assertEquals(""+count++, 0, Math.abs(res - 1), 7e-14);

    res = RkLcr.calc(quadr, f2p, f, f2p, f, 0);
    assertEquals(""+count++, 0, Math.abs(res - 59. / 243));

    res = RkLcr.calc(quadr, f, f2p, f, f2p, 0);
    assertEquals(""+count++, 0, Math.abs(res - 59. / 243));

    res = RkLcr.calc(quadr, f2p, f, f, f2p, 1);
    assertEquals(""+count++, 0, Math.abs(res - 112. / 2187));

    res = RkLcr.calc(quadr, f2s, f2p, f2s, f2p, 0);
    assertEquals(""+count++, 0, Math.abs(res - 83. / 512));

    res = RkLcr.calc(quadr, f2p, f2s, f2p, f2s, 0);
    assertEquals(""+count++, 0, Math.abs(res - 83. / 512));

    res = RkLcr.calc(quadr, f2s, f2p, f2p, f2s, 1);
    assertEquals(""+count++, 0, Math.abs(res - 45. / 512));

    res = RkLcr.calc(quadr, f2p, f2p, f2p, f2p, 0);
    assertEquals(""+count++, 0, Math.abs(res - 93. / 512));
  }
}