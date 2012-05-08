package atom.wf.lcr;

import atom.wf.coulomb.CoulombWFFactory;
import math.func.FuncVec;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.metric.DistMaxAbsErr;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 2:28:42 PM
 */
public class YkLcrTest2 extends FlowTest {
  public static Log log = Log.getLog(YkLcrTest2.class);
  private static WFQuadrLcr quadr;

  public YkLcrTest2(WFQuadrLcr w) {
    super(YkLcrTest2.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
    quadr = w;
  }
  public YkLcrTest2() {
    super(YkLcrTest2.class);   //??
  }

  public void testZ_1() throws Exception { // zk = 1
    Vec r = quadr.getR();
    TransLcrToR xToR = quadr.getLcrToR();
    FuncVec f = CoulombWFFactory.makeP1s(r, 1.);
    f.mult(quadr.getDivSqrtCR());
    double res = quadr.calcInt(f, f);
    assertEquals(0, Math.abs(res - 1), MAX_INTGRL_ERR_E10);
    String help = "< P1s | P1s > = ";
    assertEqualsRel(help, 1., res, true);   log.dbg(help, res);

    FuncVec T = CoulombWFFactory.makeZ_1_1s(r); // valid
    FuncVec zk = new YkLcr(quadr, f, f, 1).calcZk();
    double absDist = Math.abs(DistMaxAbsErr.distSLOW(T, zk));   log.dbg("DistMaxAbsErr.distSLOW(T, zk)=", absDist);
    setMaxErr(MAX_WF_DIFF_ERR);
    assertEquals("Z_1_1s = ", 0, absDist);

    T = CoulombWFFactory.makeZ_0_1s(r); // valid
    zk = new YkLcr(quadr, f, f, 0).calcZk();
    assertEquals("Z_0_1s = ", 0, Math.abs(DistMaxAbsErr.distSLOW(T, zk)));

    T = CoulombWFFactory.makeY_0_1s(r); // valid
    FuncVec Y = new YkLcr(quadr, f, f, 0).calcYk();
    log.info("T = CoulombWFFactory.makeY_0_1s(r)=\n", new VecDbgView(T));
    log.info("Y=\n", new VecDbgView(Y));
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)));

    // 1s-2s
    FuncVec f2 = CoulombWFFactory.makeP2s(r, 1.);
    f2.mult(xToR.getDivSqrtCR());
    res = quadr.calcInt(f, f2);
    assertEquals(0, Math.abs(res), MAX_INTGRL_ERR_E10);
    res = quadr.calcInt(f2, f2);
    assertEquals(1, Math.abs(res), MAX_INTGRL_ERR_E10);
//    Y = new YkLcr(xToR, f, f2, 0).calcYk();
    T = CoulombWFFactory.makeY_0_2s(r); // valid
    Y = new YkLcr(quadr, f2, f2, 0).calcYk();
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), MAX_WF_DIFF_ERR);

    // 2p
    f = CoulombWFFactory.makeP2p(r, 1.);
    log.info("f_2p=", new VecDbgView(f));
    f.mult(xToR.getDivSqrtCR());
    res = quadr.calcInt(f, f);
    assertEquals(0, Math.abs(res - 1), MAX_INTGRL_ERR_E10);
    T = CoulombWFFactory.makeY_0_2p(r); // valid
    Y = new YkLcr(quadr, f, f, 0).calcYk();
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), MAX_WF_DIFF_ERR);

    // 2p
    T = CoulombWFFactory.makeY_2_2p(r); // valid
    log.info("f_2p=", new VecDbgView(f));
    log.info("r=", new VecDbgView(r));
    log.info("x=", new VecDbgView(xToR.getX()));
    log.info("T=", new VecDbgView(T));
    Y = new YkLcr(quadr, f, f, 2).calcYk();
    log.info("Y=", new VecDbgView(Y));
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), MAX_WF_DIFF_ERR);

    int dbg = 1;
  }

}