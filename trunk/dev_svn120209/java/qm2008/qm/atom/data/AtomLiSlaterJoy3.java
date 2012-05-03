package atom.data;

import atom.wf.lcr.WFQuadrLcr;
import atom.wf.slater.SlaterWFFactory;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,08/12/2010,9:00:42 AM
 */
public class AtomLiSlaterJoy3 extends AtomLiSlaterJoy {
  public static Log log = Log.getLog(AtomLiSlaterJoy3.class);

  // Joy et al. JChemPhys (1967) 46, p4156-4157
  private static final double ZETA_1S_1 = 2.414;
  private static final double ZETA_1S_2 = 4.357;
  private static final double ZETA_2S = 0.6510;
  private static final double E_ZETA_TOT = -7.4322894;
  private static final double E_ZETA_KIN = -E_ZETA_TOT;
  private static final double E_ZETA_POT = -2. * E_ZETA_TOT;

  public AtomLiSlaterJoy3(WFQuadrLcr w) {
    super(w);
  }

  public AtomLiSlaterJoy3() {
    super(AtomLiSlaterJoy3.class);
  }

  public double getZeta1s() {
    throw new IllegalArgumentException(log.error("call getZeta(idx)"));
  }

  public double getZeta(String name) {
    if (name.equals("2s"))
      return ZETA_2S;
    else if (name.equals("1s.1"))
      return ZETA_1S_1;
    else if (name.equals("1s.2"))
      return ZETA_1S_2;
    throw new IllegalArgumentException(log.error("calling getZeta(idx>1)"));
  }

  public double getEngTot() {
    return E_ZETA_TOT;
  }

  // see http://en.wikipedia.org/wiki/Slater-type_orbital

  public FuncVec makeRawP1s(Vec r) {
    if (rawP1s == null || rawP1s.getX() != r) {
      rawP1s = SlaterWFFactory.makeP1s(r, ZETA_1S_1);
      rawP1s.mult(0.86338);
      rawP1s.addMultSafe(0.00022, SlaterWFFactory.makeP2s(r, ZETA_2S));
      rawP1s.addMultSafe(0.15235, SlaterWFFactory.makeP1s(r, ZETA_1S_2));
    }
    return rawP1s.copyY();
  }

  public FuncVec makeRawP2s(Vec r) {
    if (rawP2s == null || rawP2s.getX() != r) {
      rawP2s = SlaterWFFactory.makeP1s(r, ZETA_1S_1);
      rawP2s.mult(-0.18792);
      rawP2s.addMultSafe(1.02065, SlaterWFFactory.makeP2s(r, ZETA_2S));
      rawP2s.addMultSafe(-0.00090, SlaterWFFactory.makeP1s(r, ZETA_1S_2));
    }
    return rawP2s.copyY();
  }

  ///////////////////// TESTS   ///////////////////////////

  public void testAll() throws Exception {
    Vec r = quadr.getR();

    FuncVec f = makeRawP1s(r);
    f.mult(quadr.getDivSqrtCR());
    double res = quadr.calcInt(f, f);
    assertEquals("AtomLiSlaterJoy.makeRawP1s norm=", 0, res - 1, 3.e-5);//was 1e-5

    FuncVec f2 = makeRawP2s(r);
    f2.mult(quadr.getDivSqrtCR());
    res = quadr.calcInt(f2, f2);
    assertEquals("AtomLiSlaterJoy.makeRawP2s norm=", 0, res - 1, 4.e-5);//was 1e-5

    res = quadr.calcInt(f, f2);
    assertEquals("AtomLiSlaterJoy.<1s|2s>=", 0, res, 9.e-5);//was 4.e-5
  }


}


