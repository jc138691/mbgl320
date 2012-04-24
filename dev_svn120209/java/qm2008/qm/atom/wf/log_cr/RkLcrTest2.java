package atom.wf.log_cr;

import atom.wf.coulomb.CoulombWFFactory;
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
    FuncVec f = CoulombWFFactory.makeP1s(r, 1.);
    f.mult(quadr.getDivSqrtCR());
    double res = quadr.calcInt(f, f);
    setMaxErr(MAX_INTGRL_ERR);
//    setShowDbg(log.getDbg());
    assertEquals("<1s|1s>", 0, Math.abs(res - 1));
    FuncVec T = CoulombWFFactory.makeY_0_1s(r); // valid
    FuncVec Y = new YkLcr(quadr.getLcrToR(), f, f, 0).calcYk();
//    assertEquals(0, Math.abs(T.distSLOW(Y)), 2e-9);
    assertEquals("Y_0_1s", 0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)));
    res = RkLcr.calc(quadr, f, f, f, f, 0);
    // from p236 of C.F.Fischer
    //CRkH  F_1s1s(new CRk(w, r, f_1s, f_1s, f_1s, f_1s, CL(0)));
    //job.addLine( F_1s1s,     5./8,     "F_1s1s",     3e-9); // Fischer's 0.5??
    assertEquals("R_1s", 0, Math.abs(res - 5. / 8));
    FuncVec f2s = CoulombWFFactory.makeP2s(r, 1.);
    f2s.mult(quadr.getDivSqrtCR());
//    res = FastLoop.dot(f2s, f2s, quadrLcr.getWithCR2());
    res = quadr.calcInt(f2s, f2s);
    assertEquals("2s", 0, Math.abs(res - 1));
    res = RkLcr.calc(quadr, f, f2s, f, f2s, 0);
    //CRkH  F_2s1s(new CRk(w, r, f_1s, f_2s, f_1s, f_2s, CL(0)));
    //job.addLine( F_2s1s,   17./81,     "F_1s2s",   0.6e-9); // 0.7
    assertEquals("2s1s", 0, Math.abs(res - 17. / 81));
    res = RkLcr.calc(quadr, f2s, f, f2s, f, 0);
    //CRkH F_2s1s_(new CRk(w, r, f_2s, f_1s, f_2s, f_1s, CL(0)));
    //job.addLine(F_2s1s_,   17./81,    "F_1s2s_",   0.3e-9); // 0.7
    assertEquals("R1s_s2", 0, Math.abs(res - 17. / 81));
    res = RkLcr.calc(quadr, f, f2s, f2s, f, 0);
    //CRkH  G_2s1s(new CRk(w, r, f_1s, f_2s, f_2s, f_1s, CL(0)));
    //job.addLine( G_2s1s,  16./729,     "G_1s2s",   0.4e-9); // 0.136
    assertEquals("R2s", 0, Math.abs(res - 16. / 729));
//    assertEqualsRel("G_1s2s =", 16. / 729, res, true);

    res = RkLcr.calc(quadr, f2s, f, f, f2s, 0);
    //CRkH G_2s1s_(new CRk(w, r, f_2s, f_1s, f_1s, f_2s, CL(0)));
    //job.addLine(G_2s1s_,  16./729,    "G_1s2s_",   0.4e-9); // 0.136
    assertEquals("R_exc", 0, Math.abs(res - 16. / 729));
//    assertEqualsRel("G_1s2s =", 16. / 729, res, true);

    res = RkLcr.calc(quadr, f2s, f2s, f2s, f2s, 0);
    //CRkH  F_2s2s(new CRk(w, r, f_2s, f_2s, f_2s, f_2s, CL(0)));
    //job.addLine( F_2s2s,  77./512,     "F_2s2s",   0.4e-9); // 1.1
//    assertEquals(0, Math.abs(res - 77. / 512), MAX_INTGRL_ERR);
    assertEqualsRel("F_2s2s =", 77. / 512, res, true);

    FuncVec f2p = CoulombWFFactory.makeP2p(r, 1.);
    f2p.mult(quadr.getDivSqrtCR());
    res = quadr.calcInt(f2p, f2p);
    int count = 0;
    assertEquals(""+count++, 0, Math.abs(res - 1), 7e-14);

    res = RkLcr.calc(quadr, f2p, f, f2p, f, 0);
    //CRkH  F_2p1s(new CRk(w, r, f_2p, f_1s, f_2p, f_1s, CL(0)));
    //job.addLine( F_2p1s,   59./243,     "F_2p1s",    0.03e-9); // 0.277
    assertEquals(""+count++, 0, Math.abs(res - 59. / 243));

    res = RkLcr.calc(quadr, f, f2p, f, f2p, 0);
    //CRkH F_2p1s_(new CRk(w, r, f_1s, f_2p, f_1s, f_2p, CL(0)));
    //job.addLine(F_2p1s_,   59./243,    "F_2p1s_",    0.6e-9); // 0.277
    assertEquals(""+count++, 0, Math.abs(res - 59. / 243));

    res = RkLcr.calc(quadr, f2p, f, f, f2p, 1);
    //CRkH  G_2p1s(new CRk(w, r, f_2p, f_1s, f_1s, f_2p, CL(1))); // NOTE: CL(1)
    //job.addLine( G_2p1s, 112./2187,     "G_2p1s",    0.2e-9); // 0.28
    assertEquals(""+count++, 0, Math.abs(res - 112. / 2187));

    res = RkLcr.calc(quadr, f2s, f2p, f2s, f2p, 0);
    //CRkH  F_2p2s(new CRk(w, r, f_2s, f_2p, f_2s, f_2p, CL(0)));
    //job.addLine( F_2p2s,  83./512,     "F_2p2s",    0.2e-9); // 1.1
    assertEquals(""+count++, 0, Math.abs(res - 83. / 512));

    res = RkLcr.calc(quadr, f2p, f2s, f2p, f2s, 0);
    //CRkH F_2p2s_(new CRk(w, r, f_2p, f_2s, f_2p, f_2s, CL(0)));
    //job.addLine(F_2p2s_,  83./512,    "F_2p2s_",    0.2e-9); // 1.1
    assertEquals(""+count++, 0, Math.abs(res - 83. / 512));

    res = RkLcr.calc(quadr, f2s, f2p, f2p, f2s, 1);
    //CRkH  G_2p2s(new CRk(w, r, f_2s, f_2p, f_2p, f_2s, CL(1))); // NOTE: CL(1)
    //job.addLine( G_2p2s,  45./512,     "G_2p2s",    0.8e-9); // 0.17
    assertEquals(""+count++, 0, Math.abs(res - 45. / 512));

    res = RkLcr.calc(quadr, f2p, f2p, f2p, f2p, 0);
    //CRkH  F_2p2p(new CRk(w, r, f_2p, f_2p, f_2p, f_2p, CL(0)));
    //job.addLine( F_2p2p,  93./512,     "F_2p2p",   0.1e-9); // 0.65
    assertEquals(""+count++, 0, Math.abs(res - 93. / 512));
//    res = RkLcr.calc(quadrLcr, f2p, f2p, f2p, f2p, 1);
//    assertEquals(0, Math.abs(res - 0.12044270784428178), MAX_INTGRL_ERR); // at x=-4, 1/16, 220
//    res = RkLcr.calc(quadrLcr, f2p, f2p, f2p, f2p, 2);
//    assertEquals(0, Math.abs(res - 0.08789062488574255), MAX_INTGRL_ERR); // at x=-4, 1/16, 220
  }
}