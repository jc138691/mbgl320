package atom.wf.coulomb;

import atom.wf.lcr.LcrFactory;
import atom.wf.lcr.TransLcrToR;
import math.func.FuncVec;
import math.func.simple.FuncConst;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import project.workflow.task.test.FlowTest;
import scatt.Scatt;
import scatt.partial.wf.numerov.NmrvAlgR;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 4:16:26 PM
 */
public class CoulombNmrvLcr extends FlowTest {
  private static double NORM_ERR = 1e-5;
  public static Log log = Log.getLog(CoulombNmrvLcr.class);

  public CoulombNmrvLcr() {    super(CoulombNmrvLcr.class);   }

  // atomZ = 1 for e-Hydrogen
  public static FuncVec calc(int L, double Z, double eng, TransLcrToR lcrToR, boolean selfTest) {
    Vec x = lcrToR.getX();               log.dbg("x grid =", x);
    Vec r = lcrToR.getY();               log.dbg("r grid =", r);
    Vec cr2 = lcrToR.getCR2();
    Vec sqrtCr = lcrToR.getSqrtCR();
    FuncVec V = CoulombNmrvR.makeV(L, Z, eng, r);   log.dbg("V =", V);
    V.multSelf(cr2);
    FuncVec V4 = new FuncVec(r, new FuncConst(-0.25)); log.dbg("-1/4 = ", V4);
    V.addMultSafe(1, V4);
    V.setX(x);
    FuncVec res = new NmrvAlgR(V).calc();             log.dbg("res =", res);
    res.multSelf(sqrtCr);
    res.setX(r);

    double anal = CoulombNmrvR.calcNormAnalytic(L, Z, eng, res);   log.dbg("anal =", anal);
    if (selfTest) {
      double asym = CoulombNmrvR.calcNormAsympt(L, Z, eng, res);     log.dbg("asym =", asym);
      if (Math.abs(asym - anal) / Math.abs(asym) > NORM_ERR) {
        log.error("normalization asym=" + asym + ", analytic=" + anal);
        assertEquals(0, Math.abs(asym - anal), NORM_ERR);
        throw new IllegalArgumentException(log.error("normalization asym=" + asym + ", analytic=" + anal));
      }
    }
    res.mult(anal);
    return res;
  }


  public void testCalc() throws Exception {
    String HOME_DIR = "C:\\dev\\physics\\papers\\output";
    log.dbg("testing " + CoulombNmrvR.class);
    double eps = 1e-8;

    double LCR_FIRST = -5;

    int L = 0;
    double Z = 1;    // e-Hydrogen
    double E = 1;
    int POINTS_PER_ONE = 100;
    double er = 10;
    int R_MAX = (int)(er / E);
    int R_N = R_MAX * POINTS_PER_ONE + 1;

    StepGridModel modelR = new StepGridModel(0, R_MAX, R_N);
    StepGridModel calcGrid = LcrFactory.makeLcrFromR(LCR_FIRST, R_N, modelR);
    StepGrid x = new StepGrid(calcGrid);           log.dbg("x grid =", x);
    TransLcrToR lcrToR = new TransLcrToR(x); // x is Lcr; y is r

    FuncVec calcF = CoulombNmrvLcr.calc(L, Z, E, lcrToR, true);
//    FileX.writeToFile(calcF.toTab(), HOME_DIR, "wf"
//      , "CoulombNmrvLcr.dat");

    Vec rVec = lcrToR.getY();
    double k = Scatt.calcMomFromE(E);
//    FuncVec testF = new FuncVec(rVec, new CoulombRegAsymptFunc(L, atomZ, k));
//    FileX.writeToFile(testF.toTab(), HOME_DIR, "wf"
//      , "CoulombRegAsymptFunc.dat");

//    R_MAX = 10;
//    R_N = R_MAX * POINTS_PER_ONE + 1;
//    StepGridModel testGrid = new StepGridModel(0, R_MAX, R_N);
//    rVec = new StepGrid(testGrid);
    k = Scatt.calcMomFromE(E);
    FuncVec testF = new FuncVec(rVec, new CoulombRegFunc(L, Z, k));
//    FileX.writeToFile(testF.toTab(), HOME_DIR, "wf"
//      , "CoulombRegFunc.dat");
  }

}
