package atom.wf.log_cr;

import atom.wf.coulomb.CoulombWFFactory;
import math.func.FuncVec;
import math.vec.Vec;
import math.vec.metric.DistMaxAbsErr;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 2:28:42 PM
 */
public class YkLcrFlowTest extends FlowTest {
  public static Log log = Log.getLog(YkLcrFlowTest.class);
  private static WFQuadrLcr quadr;
  private static final double MAX_DIFF_ERR = 1e-7;
  private static final double MAX_INTGRL_ERR = 1e-10;

  public YkLcrFlowTest(WFQuadrLcr w) {
    super(YkLcrFlowTest.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
    quadr = w;
  }
  public YkLcrFlowTest() {
    super(YkLcrFlowTest.class);   //??
  }

  public void testZ_1() throws Exception { // zk = 1
    Vec r = quadr.getR();
    TransLcrToR xToR = quadr.getLcrToR();
    FuncVec f = CoulombWFFactory.makeP1s(r, 1.);
    f.mult(quadr.getDivSqrtCR());
    double res = quadr.calcOverlap(f, f);
    assertEquals(0, Math.abs(res - 1), MAX_INTGRL_ERR);
    String help = "< P1s | P1s > = ";
    assertEqualsRel(help, 1., res, true);   log.dbg(help, res);

    FuncVec T = CoulombWFFactory.makeZ_1_1s(r); // valid
    FuncVec zk = new YkLcr(xToR, f, f, 1).calcZk();
    double absDist = Math.abs(DistMaxAbsErr.distSLOW(T, zk));   log.dbg("DistMaxAbsErr.distSLOW(T, zk)=", absDist);
    setMaxErr(MAX_DIFF_ERR);
    setShowDbg(log.getDbg());
    assertEquals("Z_1_1s = ", 0, absDist);

    T = CoulombWFFactory.makeZ_0_1s(r); // valid
    zk = new YkLcr(xToR, f, f, 0).calcZk();
    assertEquals("Z_0_1s = ", 0, Math.abs(DistMaxAbsErr.distSLOW(T, zk)));

    T = CoulombWFFactory.makeY_0_1s(r); // valid
    FuncVec Y = new YkLcr(xToR, f, f, 0).calcYk();
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)));

    // 1s-2s
    FuncVec f2 = CoulombWFFactory.makeP2s(r, 1.);
    f2.mult(xToR.getDivSqrtCR());
    res = quadr.calcOverlap(f, f2);
    assertEquals(0, Math.abs(res), MAX_INTGRL_ERR);
    res = quadr.calcOverlap(f2, f2);
    assertEquals(1, Math.abs(res), MAX_INTGRL_ERR);
//    Y = new YkLcr(xToR, f, f2, 0).calcYk();
    T = CoulombWFFactory.makeY_0_2s(r); // valid
    Y = new YkLcr(xToR, f2, f2, 0).calcYk();
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), MAX_DIFF_ERR);

    // 2p
    f = CoulombWFFactory.makeP2p(r, 1.);
    f.mult(xToR.getDivSqrtCR());
    res = quadr.calcOverlap(f, f);
    assertEquals(0, Math.abs(res - 1), MAX_INTGRL_ERR);
    T = CoulombWFFactory.makeY_0_2p(r); // valid
    Y = new YkLcr(xToR, f, f, 0).calcYk();
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), MAX_DIFF_ERR);

    // 2p
    T = CoulombWFFactory.makeY_2_2p(r); // valid
    Y = new YkLcr(xToR, f, f, 2).calcYk();
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), MAX_DIFF_ERR);
  }

}