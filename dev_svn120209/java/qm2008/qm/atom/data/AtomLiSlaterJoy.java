package atom.data;

import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.slater.SlaterWFFactory;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.integral.OrthonFactory;
import math.vec.Vec;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/12/2010,4:06:43 PM
 */
public class AtomLiSlaterJoy extends FlowTest {
  public static Log log = Log.getLog(AtomLiSlaterJoy.class);

  // Joy et al. JChemPhys (1967) 46, p4156-4157
  private static final double ZETA_1S = 2.691;
  private static final double ZETA_2S = 0.6396;
  private static final double E_ZETA_TOT = -7.4184820;
  private static final double E_ZETA_KIN = -E_ZETA_TOT;
  private static final double E_ZETA_POT = -2. * E_ZETA_TOT;

  protected static WFQuadrLcr quadr;
  protected FuncVec rawP1s;
  protected FuncVec rawP2s;
  protected FuncVec normP1s;
  protected FuncVec normP2s;

  public AtomLiSlaterJoy(WFQuadrLcr w) {
    super(AtomLiSlaterJoy.class);
    quadr = w;
  }

  public AtomLiSlaterJoy(Class theClass) {  // needed for subclasses
    super(theClass);
  }

  public AtomLiSlaterJoy() {
    super(AtomLiSlaterJoy.class);
  }
  public double getZeta(String name) {
    if (name.equals("2s"))
      return ZETA_2S;
    else if (name.equals("1s"))
      return ZETA_1S;
    throw new IllegalArgumentException(log.error("calling getZeta(name)"));
  }

  public double getEngTot() {
    return E_ZETA_TOT;
  }
  // see http://en.wikipedia.org/wiki/Slater-type_orbital

  /* Due to rounding errors "Raw" is not normalized to 1
  * */

  public FuncVec makeRawP1s(Vec r) {
    if (rawP1s == null || rawP1s.getX() != r) {
      rawP1s = SlaterWFFactory.makeP1s(r, ZETA_1S);
      rawP1s.mult(0.99717);
      rawP1s.addMultSafe(0.01660, SlaterWFFactory.makeP2s(r, ZETA_2S));
    }
    return rawP1s.copyY();
  }

  public FuncVec makeRawP2s(Vec r) {
    if (rawP2s == null || rawP2s.getX() != r) {
      rawP2s = SlaterWFFactory.makeP1s(r, ZETA_1S);
      rawP2s.mult(-0.18122);
      rawP2s.addMultSafe(1.01336, SlaterWFFactory.makeP2s(r, ZETA_2S));
    }
    return rawP2s.copyY();
  }

  public FuncVec makeNormP1sLcr(WFQuadrLcr quadr) {
    Vec r = quadr.getR();
    if (normP1s == null || normP1s.getX() != r) {
      FuncArr arr = new FuncArr(r, 1);
      FuncVec f = makeRawP1s(r);
      f.mult(quadr.getDivSqrtCR());
      arr.set(0, f);
      OrthonFactory.norm(arr, quadr);
      normP1s = arr.get(0);
    }
    FuncVec res = normP1s.copyY();
    res.setX(quadr.getX()); // MUST change grid for derivatives
    return res;
  }

  public FuncVec makeNormP2sLcr(WFQuadrLcr quadr) {
    Vec r = quadr.getR();
    if (normP2s == null || normP2s.getX() != r) {
      FuncArr arr = new FuncArr(r, 2);
      FuncVec f = makeRawP1s(r);
      f.mult(quadr.getDivSqrtCR());
      FuncVec f2 = makeRawP2s(r);
      f2.mult(quadr.getDivSqrtCR());
      arr.set(0, f);
      arr.set(1, f2);
      OrthonFactory.makeOrthon(arr, quadr);
      normP1s = arr.get(0);
      normP2s = arr.get(1);
    }
    FuncVec res = normP2s.copyY();
    res.setX(quadr.getX()); // MUST change grid for derivatives
    return res;
  }

  ///////////////////// TESTS   ///////////////////////////

  public void testAll() throws Exception {
    Vec r = quadr.getR();

    FuncVec f = makeRawP1s(r);
    f.mult(quadr.getDivSqrtCR());
    double res = quadr.calcOverlap(f, f);
    assertEquals("AtomLiSlaterJoy.makeRawP1s norm=", 0, res - 1, 1e-5);

    FuncVec fn = makeNormP1sLcr(quadr);
    double norm = quadr.calcOverlap(fn, fn);
    assertEquals("AtomLiSlaterJoy.makeNormP1s norm=", 0, norm - 1, 1e-15);

    FuncVec f2 = makeRawP2s(r);
    f2.mult(quadr.getDivSqrtCR());
    res = quadr.calcOverlap(f2, f2);
    assertEquals("AtomLiSlaterJoy.makeRawP2s norm=", 0, res - 1, 1e-5);

    FuncVec fn2 = makeNormP2sLcr(quadr);
    res = quadr.calcOverlap(fn2, fn2);
    assertEquals("AtomLiSlaterJoy.makeNormP2s norm=", 0, res - 1, 2e-15);

    res = quadr.calcOverlap(f, f2);
    assertEquals("AtomLiSlaterJoy.<raw 1s|raw 2s>=", 0, res, 4.e-5);

    res = quadr.calcOverlap(fn, fn2);
    assertEquals("AtomLiSlaterJoy.<norm 1s|norm 2s>=", 0, res, 1.e-15);
  }


}


