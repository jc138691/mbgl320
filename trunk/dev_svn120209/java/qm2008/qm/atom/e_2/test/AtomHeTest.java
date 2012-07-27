package atom.e_2.test;

import atom.AtomUtil;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.data.clementi.AtomHeClementi;
import atom.e_2.SysE2;
import atom.e_2.SysE2OldOk;
import atom.e_2.SysHe;
import atom.energy.LsConfHMtrx;
import atom.energy.Energy;
import atom.energy.slater.SlaterLcr;
import atom.shell.*;
import atom.wf.coulomb.WfFactory;
import atom.wf.lcr.LcrFactory;
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.integral.OrthonFactory;
import math.mtrx.MtrxDbgView;
import math.mtrx.api.EigenSymm;
import math.mtrx.api.Mtrx;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

import javax.utilx.log.Log;
/**
* Created by Dmitry.A.Konovalov@gmail.com, 16/02/2010, 11:12:27 AM
*/
public class AtomHeTest extends FlowTest {
public static Log log = Log.getLog(AtomHeTest.class);
private static double LCR_FIRST;
private static int LCR_N;
private static double R_LAST;
private static double LAMBDA;

public AtomHeTest() {      // needed by FlowTest
  super(AtomHeTest.class);
}
public void testSingleZeta() throws Exception  {
  double FIRST = 0;
  int NUM_STEPS = 361;
  double LAST = 3; // exp(7) = 1096
  StepGrid x = new StepGrid(FIRST, LAST, NUM_STEPS);
  WFQuadrLcr w = new WFQuadrLcr(x);
  TransLcrToR xToR = w.getLcrToR();
  Vec r = xToR;

  // from p445 of Clementi Roetti, Atomic Data 14, 177 (1974)
  double Zeff = AtomHeClementi.ZETA;// from p445 of Clementi Roetti, Atomic Data 14, 177 (1974)
  FuncVec f = WfFactory.makeP1s(r, Zeff);
  f.setX(w.getX()); // MUST change grid for derivatives
  f.multSelf(xToR.getDivSqrtCR());

  double res = w.calcInt(f, f);
  assertEquals(0, Math.abs(res - 1), 3e-11);
  Ls LS = new Ls(0, Spin.SINGLET);
  Shell sh = new Shell(1, f, 2, LS.getL(), LS);
  ShPair fc = new ShPair(sh);
  SlaterLcr slater = new SlaterLcr(w);

  SysE2OldOk sys = new SysE2OldOk(2., slater);
  double kin = sys.calcH(fc, fc).kin;
  assertEquals(0, Math.abs(AtomHeClementi.E_ZETA_KIN  - kin), 2e-11);

  SysE2 sys2 = new SysHe(slater);
  double kin2 = sys2.calcH(fc, fc).kin;
  assertEquals(kin, kin2, 1e-11);

  double pot = sys.calcH(fc, fc).pt;
//    assertEquals(0, Math.abs(AtomHeClementi.E_ZETA_POT - pot), 2e-11);
  assertEquals(0, AtomHeClementi.E_ZETA_POT - pot, 7e-9); // TODO: [3Jun12] switched to Yk_NEW
//    assertEquals(0, -2. - pot / kin, 2e-11);  // TODO:
  assertEquals(0, -2. - pot / kin, 3e-9); // TODO: [3Jun12] switched to Yk_NEW

  double pot2 = sys2.calcH(fc, fc).pt;
  assertEquals(pot, pot2, 1e-11);

  Energy eng = sys.calcH(fc, fc);
  res = eng.kin + eng.pt;
//    assertEquals(0, AtomHeClementi.E_ZETA_TOT - res, 3e-11);
  assertEquals(0, AtomHeClementi.E_ZETA_TOT - res, 7e-9);
}
public void testLimitOneConfig() throws Exception  {
//    LOG.setTrace(true);
  double FIRST = 0;
  int NUM_STEPS = 601;
  double LAST = 4; // exp(7) = 1096
  StepGrid x = new StepGrid(FIRST, LAST, NUM_STEPS);
  WFQuadrLcr w = new WFQuadrLcr(x);
//    TransLcrToR xToR = w.getLcrToR();
  int L = 0;
  double Zeff = AtomHeClementi.ZETA;// from p445 of Clementi Roetti, Atomic Data 14, 177 (1974)
  double lambda = 2. * Zeff;
  int N = 10;
  LgrrOpt model = new LgrrOpt();
  model.setL(L);
  model.setLambda((float)lambda);
  model.setN(N);
  FuncArr arr = new LgrrOrthLcr(w, model);
  AtomUtil.trimTailSLOW(arr);

  OrthonFactory.log.setDbg();
  double res = OrthonFactory.calcMaxOrthErr(arr, w.getWithCR2());

  assertEquals(0, res, 2e-8);
  SlaterLcr slater = new SlaterLcr(w);
  Ls LS = new Ls(0, Spin.SINGLET);
  SysE2OldOk sys = new SysE2OldOk(2., slater);

  // One config hartree-fock limit
  double kin = 2.8617128;// from Clementi, p185
  double pot = -5.7233927;// from Clementi, p185
  double tot = -2.8616799;
  assertEquals(kin + pot, tot, 6e-22);
  LsConfs basis = ConfArrFactoryE2.makeTwoElecSameN(LS, N, arr);
  LsConfHMtrx H = new LsConfHMtrx(basis, sys);
  EigenSymm eig = H.eigSymm();
//    LOG.report(this, "H=" + Vec.toCsv(eigSymm.getRealEigenvalues()));
  double e0 = eig.getRealEVals()[0];
//    LOG.report(this, "\nkin+pt="
//      + (kin + pt) + "\n   e[0]=" + e0);
  assertEquals(0, -2.8615628084911 - e0, 3e-6); //
  assertEquals(0, e0 - tot, 2e-4);
//    FuncVec conf = H.calcDens(eigSymm, 0);
//      LOG.saveToFile(valarray.asArray(x), valarray.asArray(conf), "wf", "He_ground_density.csv");
//    res = FastLoop.dot(conf, w);
//    assertEquals(2, res, 3e-15);
}


public void dbgBasisNc() throws Exception  {  log.setDbg();
//public void testBasisNc() throws Exception  {  log.setDbg();

  int Nt = 15;
  LCR_FIRST = -5. - 2. * Math.log(AtomHe.Z);   log.dbg("LCR_FIRST=", LCR_FIRST);
  LCR_N = 1001;
  R_LAST = 100;
  LAMBDA = 3;

  StepGridOpt gridR = new StepGridOpt(0, R_LAST, -1);
  StepGridOpt sg = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, gridR);
  StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
  WFQuadrLcr quadr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadr);
  SlaterLcr slater = new SlaterLcr(quadr);
  SysE2 sysE2 = new SysHe(slater);// NOTE -2 for Helium

  LgrrOrthLcr orthNt = new LgrrOrthLcr(quadr, new LgrrOpt(0, Nt, LAMBDA));         log.dbg("LgrrOrthLcr = ", orthNt);

  Ls S1 = new Ls(0, Spin.SINGLET);
  LsConfs confs = ConfArrFactoryE2.makeSModelE2(S1, orthNt);   log.dbg("confs=", confs);
  LsConfHMtrx sysH = new LsConfHMtrx(confs, sysE2);            log.dbg("htS1=\n", new MtrxDbgView(sysH));

  LsConfs ltdConfs = ConfArrFactoryE2.makeSModelSmallE2(Nt, Nt, Nt, S1, orthNt);   log.dbg("ltdConfs=", ltdConfs);
  LsConfHMtrx ltdH = new LsConfHMtrx(ltdConfs, sysE2);            log.dbg("htS1=\n", new MtrxDbgView(ltdH));

  Mtrx ev = ltdH.getEigVec();           log.dbg("ev=", new MtrxDbgView(ev));
  Vec sysEngs = sysH.getEigEngs();      log.dbg("sysEngs=", new VecDbgView(sysEngs));
  Vec ltdEngs = ltdH.getEigEngs();      log.dbg("ltdEngs=", new VecDbgView(ltdEngs));

}
}