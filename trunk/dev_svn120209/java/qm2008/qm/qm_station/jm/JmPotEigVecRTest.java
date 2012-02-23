package qm_station.jm;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import scatt.jm_2008.jm.laguerre.LgrrOrthR;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.func.arr.FuncArrDbgView;
import math.func.arr.FuncArr;
import math.func.FuncVec;
import math.func.simple.FuncPowInt;
import junit.framework.TestCase;
import atom.energy.part_wave.PartHMtrx;
import atom.energy.part_wave.PartH;
import atom.energy.part_wave.PartHMtrxR;
import atom.wf.coulomb.CoulombWFFactory;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 15:42:20
 */
public class JmPotEigVecRTest extends FlowTest {
  private static LgrrOrthR funcArr;
  public JmPotEigVecRTest(LgrrOrthR arr) {
    super(JmPotEigVecRTest.class);
    this.funcArr = arr;
  }
  public JmPotEigVecRTest() {
  }
  public void testNorm() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();
    int L = 0;

    log.dbg("testing diagonalisation of H=K-1/r");
    StepGrid r = (StepGrid)funcArr.getX();
    FuncVec pot = new FuncVec(r, new FuncPowInt(-1., -1)); log.dbg("-1/r=", new VecDbgView(pot));
    PartHMtrx H = new PartHMtrxR(L, funcArr, pot);
    Vec eigEng = H.getEigVal();                     log.dbg("eigVal=", new VecDbgView(eigEng));
    assertEqualsRel("EigenE(1s) =", -1./2., eigEng.get(0), true);
    assertEqualsRel("EigenE(2s) =", -1./8., eigEng.get(1), true);

    log.dbg("testing diagonalisation of H=K+V_1s, where V_1s(r) is felt by an electron");
    pot = CoulombWFFactory.makePotHy_1s_e(r);  log.dbg("V_1s(r)=", new VecDbgView(pot));
    H = new PartHMtrxR(L, funcArr, pot);
    PartH partH = H.makePartH();
    eigEng = H.getEigVal();                     log.dbg("eigVal=", new VecDbgView(eigEng));
    FuncArr eigVec = H.getEigFuncArr();                 log.dbg("eigVec=", new FuncArrDbgView(eigVec));

    for (int i = 0; i < eigEng.size(); i++) {
      if (monitor != null && monitor.isCanceled(i, 0, eigEng.size())) {
        TestCase.fail();
        return;
      }
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
        TestCase.fail();
      }
      FuncVec wf = eigVec.getFunc(i);
      double kinE = partH.calcKin(L, wf, wf);  log.dbg("kinE=", kinE);
      double potE = partH.calcPot(pot, wf, wf);  log.dbg("potE=", potE);

      double Ei = eigEng.get(i);  log.dbg("EigVal["+i+"] = " + Ei);
      double calcE = kinE + potE;
      assertEqualsRel("EigenE[" + i + "] =", Ei, calcE, true);
    }
  }
}

