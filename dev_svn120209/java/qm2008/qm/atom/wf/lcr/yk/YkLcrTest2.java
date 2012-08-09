package atom.wf.lcr.yk;

import atom.wf.coulomb.WfFactory;
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
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

  public void testZ_1() throws Exception { // cZk = 1
    Vec r = quadr.getR();
    TransLcrToR xToR = quadr.getLcrToR();
    FuncVec f = WfFactory.makeP1s(r, 1.);
    f.multSelf(quadr.getDivSqrtCR());
    double res = quadr.calcInt(f, f);
    assertEquals(0, Math.abs(res - 1), MAX_INTGRL_ERR_E10);
    String help = "< P1s | P1s > = ";
    assertEqualsRel(help, 1., res, true);   log.dbg(help, res);

    FuncVec tYk, cYk, tZk, cZk, nYk, nZk;
    tZk = WfFactory.makeZ_1_1s(r); // valid
    cZk = new YkLcr(quadr, f, f, 1).calcZk();
    double absDist = Math.abs(DistMaxAbsErr.distSLOW(tZk, cZk));   log.dbg("DistMaxAbsErr.distSLOW(trueZk, cZk)=", absDist);
    setMaxErr(MAX_WF_DIFF_ERR);
    assertEquals("Z_1_1s = ", 0, absDist);

    tZk = WfFactory.makeZ_0_1s(r); // valid
    cZk = new YkLcr(quadr, f, f, 0).calcZk();
    assertEquals("Z_0_1s = ", 0, Math.abs(DistMaxAbsErr.distSLOW(tZk, cZk)));

    tYk = WfFactory.makeY_0_1s(r); // valid
    cYk = new YkLcr(quadr, f, f, 0).calcYk();
    log.info("trueZk = WfFactory.makeY_0_1s(r)=\n", new VecDbgView(cYk));
    log.info("cYk=\n", new VecDbgView(cYk));
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tYk, cYk)));

    // 1s-2s
    FuncVec f2 = WfFactory.makeP2s(r, 1.);
    f2.multSelf(xToR.getDivSqrtCR());
    res = quadr.calcInt(f, f2);
    assertEquals(0, Math.abs(res), MAX_INTGRL_ERR_E10);
    res = quadr.calcInt(f2, f2);
    assertEquals(1, Math.abs(res), MAX_INTGRL_ERR_E10);
//    cYk = new YkLcr(xToR, f, f2, 0).calcYk();
    tZk = WfFactory.makeY_0_2s(r); // valid
    cYk = new YkLcr(quadr, f2, f2, 0).calcYk();
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tZk, cYk)), MAX_WF_DIFF_ERR);

    // 2p
    f = WfFactory.makeP2p(r, 1.);
    log.info("f_2p=", new VecDbgView(f));
    f.multSelf(xToR.getDivSqrtCR());
    res = quadr.calcInt(f, f);
    assertEquals(0, Math.abs(res - 1), MAX_INTGRL_ERR_E10);
    tZk = WfFactory.makeY_0_2p(r); // valid
    cYk = new YkLcr(quadr, f, f, 0).calcYk();
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tZk, cYk)), MAX_WF_DIFF_ERR);

    // 2p
    tZk = WfFactory.makeY_2_2p(r); // valid
    log.info("f_2p=", new VecDbgView(f));
    log.info("r=", new VecDbgView(r));
    log.info("x=", new VecDbgView(xToR.getX()));
    log.info("trueZk=", new VecDbgView(tZk));
    cYk = new YkLcr(quadr, f, f, 2).calcYk();
    log.info("cYk=", new VecDbgView(cYk));
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tZk, cYk)), MAX_WF_DIFF_ERR);

    int dbg = 1;
  }

}