package atom.wf.coulomb;

import math.Calc;
import math.func.Func;
import math.func.FuncVec;
import math.func.simple.FuncConst;
import math.func.simple.FuncPowInt;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import project.workflow.task.test.FlowTest;
import scatt.Scatt;
import scatt.partial.wf.numerov.NmrvAlgR;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 9:09:24 AM
 */
public class CoulombNmrvR extends FlowTest {
  private static double NORM_ERR = 1e-3;
  public static String HELP = "Partial Coulomb scattering wave (via Numerov algorithm)";
  public static Log log = Log.getLog(CoulombNmrvR.class);
  public CoulombNmrvR() {      super(CoulombNmrvR.class);   }

  // atomZ = 1 for e-Hydrogen
  public static FuncVec calc(int L, double Z, double eng, StepGrid rVec) {
    FuncVec V = makeV(L, Z, eng, rVec);
    FuncVec res = new NmrvAlgR(V).calc();
    double asym = calcNormAsympt(L, Z, eng, res);
    double anal = calcNormAnalytic(L, Z, eng, res);
    if (Math.abs(asym - anal) / Math.abs(asym) > NORM_ERR) {
      log.error("normalization asym=" + asym + ", analytic=" + anal);
      assertEquals(0, Math.abs(asym - anal), NORM_ERR);
      throw new IllegalArgumentException(log.error("normalization asym=" + asym + ", analytic=" + anal));
    }
    res.mult(asym);
    return res;
  }
  public static FuncVec makeV(int L, double Z, double eng, Vec rVec) {
    Func potFunc = new FuncPowInt(-Z, -1);  // f(r)=-1./r
    FuncVec  pot = new FuncVec(rVec, potFunc); log.dbg("-1/r=", new VecDbgView(pot));
    FuncVec V = new FuncVec(rVec, new FuncConst(2. * eng)); log.dbg("2*E = ", V);
    V.addMultSafe(-2, pot);                                log.dbg("2*E - 2*V= ", V);
    return V;
  }

  public static double calcNormAsympt(int L, double Z, double E, FuncVec from) {
    double[] fa = from.getY().getArr();
    double[] ra = from.getX().getArr();
    double f = fa[fa.length-1];
    double maxAbs = Math.abs(f);
    double r = ra[fa.length-1];
    boolean found = false;
    for (int i = fa.length-2; i >=0 ; i--) {
      double fi = Math.abs(fa[i]);
      if (fi > maxAbs) {
        maxAbs = fi;
        f = fa[i];
        r = ra[i];
        found = true;
        continue;
      }
      if (found)
        break;
    }
    double corr = CoulombReg.calcEng(L, Z, E, r).getRe();
//    double corr = CoulombRegAsympt.calcEng(L, atomZ, E, r);
    return corr / f;
  }

  // analytic for small r
  public static double calcNormAnalytic(int L, double Z, double E, FuncVec from) {
    int idx = 1;
    double f = from.getY().get(idx);
    double r = from.getX().get(idx);
    if (Calc.isZero(f)) {
      idx++; // try one after first
      f = from.getY().get(idx);
      r = from.getX().get(idx);
      if (Calc.isZero(f)) {
        throw new IllegalArgumentException(log.error("Calc.isZero(f) in normToOne(): idx=" + idx + ", r=" + r));
      }
    }
    double corr = CoulombReg.calcEng(L, Z, E, r).getRe();
    return corr / f;
  }

  public void testCalc() throws Exception {
    String HOME_DIR = "C:\\dev\\physics\\papers\\output";
    log.dbg("testing " + CoulombNmrvR.class);
    double eps = 1e-8;

    int L = 0;
    double Z = 1;    // e-Hydrogen
    double E = 1;
    int POINTS_PER_ONE = 1000;
    double er = 10;
    int R_MAX = (int)(er / E);
    int R_N = R_MAX * POINTS_PER_ONE + 1;
    StepGridOpt calcGrid = new StepGridOpt(0, R_MAX, R_N);
    StepGrid rVec = new StepGrid(calcGrid);
    FuncVec calcF = CoulombNmrvR.calc(L, Z, E, rVec);
//    FileX.writeToFile(calcF.toTab(), HOME_DIR, "wf"
//      , "CoulombNmrvR.dat");

    double k = Scatt.calcMomFromE(E);
//    FuncVec testF = new FuncVec(vR, new CoulombRegAsymptFunc(L, atomZ, k));
//    FileX.writeToFile(testF.toTab(), HOME_DIR, "wf"
//      , "CoulombRegAsymptFunc.dat");

//    R_MAX = 10;
//    R_N = R_MAX * POINTS_PER_ONE + 1;
//    StepGridOpt testGrid = new StepGridOpt(0, R_MAX, R_N);
//    vR = new StepGrid(testGrid);
    k = Scatt.calcMomFromE(E);
    FuncVec testF = new FuncVec(rVec, new CoulombRegFunc(L, Z, k));
//    FileX.writeToFile(testF.toTab(), HOME_DIR, "wf"
//      , "CoulombRegFunc.dat");
  }

}