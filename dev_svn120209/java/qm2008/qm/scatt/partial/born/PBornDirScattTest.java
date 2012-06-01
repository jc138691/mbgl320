package scatt.partial.born;
import atom.wf.coulomb.WfFactory;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import project.workflow.task.test.FlowTest;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.partial.wf.SinWfLcr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 24/02/12, 9:09 AM
 */
public class PBornDirScattTest extends FlowTest {
public static Log log = Log.getLog(PBornDirScattTest.class);
// NOTE!! class variables are static to work with FlowTest
private static WFQuadrLcr quadr;
private static EngModel gridEng;
public PBornDirScattTest() {      // needed by FlowTest
  super(PBornDirScattTest.class);
}
public PBornDirScattTest(WFQuadrLcr quadr_, EngModel gridEng_) {
  super(PBornDirScattTest.class);
  quadr = quadr_;
  gridEng = gridEng_;
}
public void testFirstBornOk() throws Exception {
  log.info("-->testFirstBornOk()");
  Vec r = quadr.getR();
  Vec V_1s = WfFactory.makePotHy_1s(r);  log.dbg("V_1s=", V_1s);

  double pF = Scatt.calcMomFromE(gridEng.getFirst());
  double pL = Scatt.calcMomFromE(gridEng.getLast());
  int N_TESTS = 10;
  StepGrid incP = new StepGrid(pF, pL, N_TESTS);  log.dbg("incP=", incP);
  int L = 0;
  FuncVec arrB = WfFactory.calcBornDirHy_1s(incP);  log.dbg("arrB=", arrB);
  for (int i = 0; i < incP.size(); i++) {
    double corr = arrB.get(i);                log.dbg("corr=", corr);
    double p = arrB.getX().get(i);             log.info("p=", p);
    FuncVec upl = new SinWfLcr(quadr, p, L);   log.dbg("upl=", upl);
    double res = quadr.calcInt(V_1s, upl, upl);     log.info("res=", res); log.info("res-corr=", res - corr);
    assertEqualsRel("p=" + (float) p, corr, res, true);
  }
  log.info("<--testFirstBornOk()");
}
}
