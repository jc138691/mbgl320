package atom.data.clementi;

import atom.wf.lcr.WFQuadrLcr;
import atom.wf.slater.SlaterWFFactory;
import math.func.FuncVec;
import math.vec.Vec;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/12/2010,12:02:46 PM
 */
public class AtomLiClementiRaw extends FlowTest {
  public static Log log = Log.getLog(AtomLiClementiRaw.class);

  // from p445 of Clementi Roetti, Atomic Data 14, 177 (1974)
  public static final double ZETA_1S = 2.69063;
  public static final double ZETA_2S = 0.63961;
  public static final double E_ZETA_KIN = 7.4184525;
  public static final double E_ZETA_POT = -14.836934;
  public static final double E_ZETA_TOT = -7.4184820;

  private static WFQuadrLcr quadr;

  public AtomLiClementiRaw(WFQuadrLcr w) {
    super(AtomLiClementiRaw.class);
    quadr = w;
  }

  public AtomLiClementiRaw() {
    super(AtomLiClementiRaw.class);
  }

  // see http://en.wikipedia.org/wiki/Slater-type_orbital

  public static FuncVec makeRawP1s(Vec r) {
    // see AtomLiSlaterJoy's norm functions if needed
    FuncVec res = SlaterWFFactory.makeP1s(r, ZETA_1S);
    res.mult(0.99759);
    FuncVec f2 = SlaterWFFactory.makeP2s(r, ZETA_2S);
    res.addMultSafe(0.01239, f2);
    return res;
  }

  public static FuncVec makeRawP2s(Vec r) {
    FuncVec res = SlaterWFFactory.makeP1s(r, ZETA_1S);
    res.mult(0.20444);
    FuncVec f2 = SlaterWFFactory.makeP2s(r, ZETA_2S);
    res.addMultSafe(-1.01824, f2);
    return res;
  }

  ///////////////////// TESTS   ///////////////////////////

  public void testAll() throws Exception {
    Vec r = quadr.getR();

    FuncVec f = makeRawP1s(r);
    f.mult(quadr.getDivSqrtCR());
    double res = quadr.calcInt(f, f);
    assertEquals("IT DOES NOT WORKS! See AtomLiSlaterJoy.'norm'-functions AtomLiClementi.makeRawP1s norm=", 0, res-1, 0.00001);

    FuncVec f2 = makeRawP2s(r);
    f2.mult(quadr.getDivSqrtCR());
    res = quadr.calcInt(f2, f2);
    assertEquals("AtomLiClementi.makeRawP2s norm=", 0, res-1, 0.00001);

    res = quadr.calcInt(f, f2);
    assertEquals("AtomLiClementi.<1s|2s>=", 0, res, 0.00001);
  }


}


