package qm_station.jm;

import atom.energy.part_wave.PotH;
import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.wf.coulomb.CoulombWFFactory;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.func.simple.FuncPowInt;
import math.vec.Vec;
import math.vec.VecDbgView;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 17:25:16
 */
public class PotEigVecLcrTest extends FlowTest {
  // NOTE!! class variables are static to work with FlowTest
  private static LgrrOrthLcr funcArr;
  private static double Z = 1.;     // 1. for hydrogen
  private static boolean relErr;

  public PotEigVecLcrTest(double zIn, LgrrOrthLcr arr) {
    super(PotEigVecLcrTest.class);
    funcArr = arr;
    Z = zIn;
    relErr = true;
  }
  public PotEigVecLcrTest(double zIn, LgrrOrthLcr arr, boolean rel) {
    super(PotEigVecLcrTest.class);
    funcArr = arr;
    Z = zIn;
    relErr = rel;
  }

  public PotEigVecLcrTest() {      // needed by FlowTest
    super(PotEigVecLcrTest.class);
  }

  public void testNorm() throws Exception {
//    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();
    int L = 0;

    log.dbg("testing diagonalisation of H=K-Z/r; Z=" + Z);
    Vec r = funcArr.getR();
    FuncVec pot = new FuncVec(r, new FuncPowInt(-Z, -1));
    log.dbg("-Z/r=", new VecDbgView(pot));
    PotHMtrx H = new PotHMtrxLcr(L, funcArr, pot);
    Vec eigEng = H.getEigVal();
    log.dbg("eigVal=", new VecDbgView(eigEng));
    if (relErr) {
      assertEqualsRel("EigenE(1s) =", -Z * Z / 2., eigEng.get(0), true);
      assertEqualsRel("EigenE(2s) =", -Z * Z / 8., eigEng.get(1), true);
    } else {
      assertEquals("EigenE(1s) =", -Z * Z / 2., eigEng.get(0), true);
      assertEquals("EigenE(2s) =", -Z * Z / 8., eigEng.get(1), true);
    }

    log.dbg("in LCR: testing H=K+V_1s");
    pot = CoulombWFFactory.makePotHy_1s_e(r);
    log.dbg("V_1s(r)=", new VecDbgView(pot));
    H = new PotHMtrxLcr(L, funcArr, pot);
    PotH partH = H.makePotH();
    eigEng = H.getEigVal();
    log.dbg("eigVal=", new VecDbgView(eigEng));
    FuncArr eigVec = H.getEigFuncArr();
    log.dbg("eigVec=", new FuncArrDbgView(eigVec));

    for (int i = 0; i < eigEng.size(); i++) {
      FuncVec wf = eigVec.getFunc(i);
      double kinE = partH.calcKin(L, wf, wf);
      log.dbg("kinE=", kinE);
      double potE = partH.calcPot(pot, wf, wf);
      log.dbg("potE=", potE);

      double Ei = eigEng.get(i);
      log.dbg("EigVal[" + i + "] = " + Ei);
      double calcE = kinE + potE;
      assertEqualsRel("EigenE[" + i + "] =", Ei, calcE, true);
    }
  }
}

