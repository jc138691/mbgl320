package scatt.partial.wf.numerov;
import atom.wf.coulomb.CoulombNmrvR;
import atom.wf.coulomb.CoulombRegFunc;
import math.func.FuncVec;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import math.vec.metric.DistMaxAbsErr;
import project.workflow.task.test.FlowTest;
import scatt.Scatt;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 14:26:35
 */
public class NmrvAlgR extends FlowTest {
  public static Log log = Log.getLog(NmrvAlgR.class);
  public NmrvAlgR() { super(NmrvAlgR.class);  }
  private FuncVec pot;
  public NmrvAlgR(FuncVec pot) {
    this.pot = pot;
  }
//http://en.wikipedia.org/wiki/Numerov%27s_method
  public FuncVec calc() {
    int n = pot.size();
    StepGrid x = (StepGrid) pot.getX();
    FuncVec res = new FuncVec(x);
    double[] f = res.getArr();
    double[] V = pot.getArr();

    double h = x.getGridStep();
    double H2 = h * h / 12.;
    double H5 = 10. * H2; // 5/6

    f[0] = 0;
    f[1] = h;
    for (int i = 1; i < n-1; i++) { // first TWO points are fixed
      double t = (2. - H5 * V[i]) * f[i] - (1. + H2 * V[i-1]) * f[i-1];
      double b = 1. + H2 * V[i+1];
      f[i+1] = t / b;
    }
    return res;
  }

  public void testCalc() throws Exception {
//    String HOME_DIR = "C:\\dev\\physics\\papers\\output";
    log.dbg("testing " + NmrvAlgR.class);
    double MAX_DIFF_ERR = 1e-20;

    int L = 0;
    int Z = 1;    // e-Hydrogen
    double E = 0.5;
    int POINTS_PER_ONE = 100;
    int R_MAX = 10;
    int R_N = R_MAX * POINTS_PER_ONE + 1;
    StepGridModel calcGrid = new StepGridModel(0, R_MAX, R_N);
    StepGrid rVec = new StepGrid(calcGrid);
    FuncVec calcF = CoulombNmrvR.calc(L, Z, E, rVec);
//    FileX.writeToFile(calcF.toTab(), HOME_DIR, "wf"
//      , "CoulombNmrvR.dat");

    double k = Scatt.calcMomFromE(E);
    FuncVec testF = new FuncVec(rVec, new CoulombRegFunc(L, Z, k));
//    FileX.writeToFile(testF.toTab(), HOME_DIR, "wf"
//      , "CoulombRegFunc.dat");

    setMaxErr(MAX_DIFF_ERR);
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(calcF, testF)));
//    assertEquals("Z_0_1s = ", 0, Math.abs(DistMaxAbsErr.distSLOW(calcF, testF)));
  }
}
