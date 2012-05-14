package papers.he_swave.minmax;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.e_1.SysHy;
import atom.e_2.*;
import atom.energy.ConfHMtrx;
import atom.energy.Energy;
import atom.energy.slater.SlaterLcr;
import atom.shell.*;
import atom.smodel.HeSWaveAtom;
import atom.wf.lcr.LcrFactory;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.mm.HkMm;
import atom.wf.mm.SysHeMm;
import atom.wf.mm.SysHyMmE2;
import math.func.FuncVec;
import math.func.deriv.test.DerivPts9Test;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import math.vec.test.FastLoopTest;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 10/05/12, 9:25 AM
 */
public class HeAtomMM_try extends FlowTest {
public static Log log = Log.getLog(HeAtomMM_try.class);
private static WFQuadrLcr quadr;
private int N = 5;
private int K = 0;
private LgrrOrthLcr orthonN;
private SlaterLcr slater;
public HeAtomMM_try() {
  super(HeAtomMM_try.class);
}

public void testHyMm() throws Exception {  log.setDbg();
  if (!new FastLoopTest().ok()) return;
  if (!new DerivPts9Test().ok()) return;

  int LCR_FIRST = -5;
  int LCR_N = 1001;
  int R_FIRST = 0;
  int R_LAST = 100;
  N = 2;
  double LAMBDA = 2;

  LgrrModel lgrrModel = new LgrrModel();
  lgrrModel.setL(0);
  lgrrModel.setLambda(LAMBDA);
  lgrrModel.setN(N);      log.dbg("lgrrModel=\n", lgrrModel);

  StepGridModel gridR = new StepGridModel(R_FIRST, R_LAST, LCR_N); // R_N not used!!!
  StepGridModel gridLcr = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, gridR);   log.dbg("gridLcr =\n", gridLcr);
  StepGrid x = new StepGrid(gridLcr);                 log.dbg("StepGrid x = new StepGrid(gridLcr) =\n", x);
  quadr = new WFQuadrLcr(x);                          log.dbg("quadr = new WFQuadrLcr(x)=\n", quadr);
  log.dbg("quadr.getR()=\n", quadr.getR());

  orthonN = new LgrrOrthLcr(quadr, lgrrModel);         log.dbg("orthonN = new LgrrOrthLcr(quadr, lgrrModel)=\n", orthonN);
  slater = new SlaterLcr(quadr);

  // HYDROGEN
  SysE2 sysE2 = new SysHyE2(slater);
  SysE2OldOk oldE2 = new SysE2OldOk(-AtomHy.Z, slater);
  SysHyMmE2 mmE2 = new SysHyMmE2(slater);

  Ls sysLs = new Ls(0, Spin.SINGLET);  // t - for target
  ConfArr confs = ConfArrFactoryE2.makeSModelE2(sysLs, orthonN, orthonN);
  log.dbg("confs=", confs);
  ConfHMtrx sysH = new ConfHMtrx(confs, sysE2); log.dbg("sysH=\n", new MtrxDbgView(sysH));
  ConfHMtrx oldH = new ConfHMtrx(confs, oldE2); log.dbg("oldH=\n", new MtrxDbgView(oldH));
  ConfHMtrx mmH = new ConfHMtrx(confs, mmE2); log.dbg("mmH=\n", new MtrxDbgView(mmH));

  Vec sysEngs = sysH.getEigVal();
  log.dbg("sysEngs=\n", new VecDbgView(sysEngs));

  Vec oldEngs = oldH.getEigVal();
  log.dbg("oldEngs=\n", new VecDbgView(oldEngs));

  assertEquals(0, Math.abs(sysEngs.getFirst() - oldEngs.getFirst()), 1e-14);

  Vec mmEngs = mmH.getEigVal();
  log.dbg("mmEngs=\n", new VecDbgView(mmEngs));
  log.dbg("HeSWaveAtom.E_1S=\n" + new VecDbgView(HeSWaveAtom.E_1S));

  double norm, kin, pot1, pot2, tot;
  FuncVec a = orthonN.get(0);
  FuncVec b = orthonN.get(1);

  // TEST AA AA
  log.info("TEST AA AA");
  HkMm hkmm = new HkMm(quadr, a, a, a, a, K);
  log.dbg("HkMm(quadr, a, a, a, a, K)");
  norm = hkmm.calcNorm();  log.dbg("calcNorm()=" + norm);
  assertEquals(0, norm - 1, 2e-14);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHy.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHy.Z);  log.dbg("calcTotE()=" + tot);

  Conf cfAA = confs.get(0);
  Energy engE2 = sysE2.calcH(cfAA, cfAA);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 3e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 3e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 3e-13);  // GOOD TEST!!!

  // TEST BB BB
  log.info("TEST BB BB");
  hkmm = new HkMm(quadr, b, b, b, b, K);
  log.dbg("HkMm(quadr, B, B, B, B, K)");
  norm = hkmm.calcNorm();  log.dbg("calcNorm()=" + norm);
  assertEquals(0, norm - 1, 1e-13);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHy.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHy.Z);  log.dbg("calcTotE()=" + tot);

  Conf cfBB = confs.get(2);
  engE2 = sysE2.calcH(cfBB, cfBB);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 1e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 2e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 1e-12);  // GOOD TEST!!!

  // TEST AA BB
  log.info("TEST AA BB");
  hkmm = new HkMm(quadr, a, a, b, b, K);
  log.dbg("HkMm(quadr, a, a, B, B, K)");
  norm = hkmm.calcNorm();  log.dbg("calcNorm()=" + norm);
  assertEquals(0, norm, 2e-14); // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHy.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHy.Z);  log.dbg("calcTotE()=" + tot);

  engE2 = sysE2.calcH(cfAA, cfBB);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 1e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 2e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 1e-12);  // GOOD TEST!!!
}
public void testHeMm() throws Exception {  log.setDbg();
  if (!new FastLoopTest().ok()) return;
  if (!new DerivPts9Test().ok()) return;

  double LCR_FIRST = -5 - 2 * Math.log(AtomHe.Z);   log.dbg("LCR_FIRST=", LCR_FIRST);
  int LCR_N = 1001;
  double R_FIRST = 0;
  double R_LAST = 100 / AtomHe.Z;
  N = 2;
  double LAMBDA = 4;

  LgrrModel lgrrModel = new LgrrModel();
  lgrrModel.setL(0);
  lgrrModel.setLambda(LAMBDA);
  lgrrModel.setN(N);      log.dbg("lgrrModel=\n", lgrrModel);

  StepGridModel gridR = new StepGridModel(R_FIRST, R_LAST, LCR_N); // R_N not used!!!
  StepGridModel gridLcr = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, gridR);   log.dbg("gridLcr =\n", gridLcr);
  StepGrid x = new StepGrid(gridLcr);                 log.dbg("StepGrid x = new StepGrid(gridLcr) =\n", x);
  quadr = new WFQuadrLcr(x);                          log.dbg("quadr = new WFQuadrLcr(x)=\n", quadr);
  log.dbg("quadr.getR()=\n", quadr.getR());

  orthonN = new LgrrOrthLcr(quadr, lgrrModel);         log.dbg("orthonN = new LgrrOrthLcr(quadr, lgrrModel)=\n", orthonN);
  slater = new SlaterLcr(quadr);

  SysE2 sysE2 = new SysHe(slater);// NOTE -2 for Helium       // USES equations from the 2011 e-He paper
  SysE2OldOk oldE2 = new SysHeOldOk(slater);// NOTE -2 for Helium // F-for Fano
  SysHeMm mmE2 = new SysHeMm(slater);// NOTE -2 for Helium       // USES equations from the 2011 e-He paper

  Ls sysLs = new Ls(0, Spin.SINGLET);  // t - for target
  ConfArr confs = ConfArrFactoryE2.makeSModelE2(sysLs, orthonN, orthonN);
  log.dbg("confs=", confs);
  ConfHMtrx sysH = new ConfHMtrx(confs, sysE2); log.dbg("sysH=\n", new MtrxDbgView(sysH));
  ConfHMtrx oldH = new ConfHMtrx(confs, oldE2); log.dbg("oldH=\n", new MtrxDbgView(oldH));
  ConfHMtrx mmH = new ConfHMtrx(confs, mmE2); log.dbg("mmH=\n", new MtrxDbgView(mmH));

  Vec sysEngs = sysH.getEigVal();
  log.dbg("sysEngs=\n", new VecDbgView(sysEngs));

  Vec oldEngs = oldH.getEigVal();
  log.dbg("oldEngs=\n", new VecDbgView(oldEngs));

  assertEquals(0, Math.abs(sysEngs.getFirst() - oldEngs.getFirst()), 1e-14);
//  assertFloorRel("E_1s1s_1S", HeSWaveAtom.E_1s1s_1S, oldEngs.get(0), 2e-4);
//  assertFloorRel("E_1s2s_1S", HeSWaveAtom.E_1s2s_1S, oldEngs.get(1), 3e-5);

  Vec mmEngs = mmH.getEigVal();
  log.dbg("mmEngs=\n", new VecDbgView(mmEngs));
  log.dbg("HeSWaveAtom.E_1S=\n" + new VecDbgView(HeSWaveAtom.E_1S));

  double norm, kin, pot1, pot2, tot;
  FuncVec a = orthonN.get(0);
  FuncVec b = orthonN.get(1);

  // TEST AA AA
  log.info("TEST AA AA");
  HkMm hkmm = new HkMm(quadr, a, a, a, a, K);
  log.dbg("HkMm(quadr, a, a, a, a, K)");
  norm = hkmm.calcNorm();  log.dbg("calcNorm()=" + norm);
  assertEquals(0, norm - 1, 2e-14);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHe.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHe.Z);  log.dbg("calcTotE()=" + tot);

  Conf cfAA = confs.get(0);
  Energy engE2 = sysE2.calcH(cfAA, cfAA);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 3e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 3e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 3e-13);  // GOOD TEST!!!

  // TEST BB BB
  log.info("TEST BB BB");
  hkmm = new HkMm(quadr, b, b, b, b, K);
  log.dbg("HkMm(quadr, B, B, B, B, K)");
  norm = hkmm.calcNorm();  log.dbg("calcNorm()=" + norm);
  assertEquals(0, norm - 1, 1e-13);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHe.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHe.Z);  log.dbg("calcTotE()=" + tot);

  Conf cfBB = confs.get(2);
  engE2 = sysE2.calcH(cfBB, cfBB);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 1e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 2e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 1e-12);  // GOOD TEST!!!

  // TEST AA BB
  log.info("TEST AA BB");
  hkmm = new HkMm(quadr, a, a, b, b, K);
  log.dbg("HkMm(quadr, a, a, B, B, K)");
  norm = hkmm.calcNorm();  log.dbg("calcNorm()=" + norm);
  assertEquals(0, norm, 2e-14); // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHe.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHe.Z);  log.dbg("calcTotE()=" + tot);

  engE2 = sysE2.calcH(cfAA, cfBB);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 1e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 2e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 1e-12);  // GOOD TEST!!!

//  // TEST AA AB
//  log.info("TEST AA BB");
//  hkmm = new HkMm(quadr, a, a, a, b, K);
//  log.dbg("HkMm(quadr, a, a, A, B, K)");
//  norm = hkmm.calcNorm();  log.dbg("calcNorm()=" + norm);
//  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
//  pot1 = hkmm.calcPot1();  log.dbg("calcPot1()=" + pot1);
//  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
//  pot2 /= norm;            log.dbg("pot2 /= norm=" + pot2);
//  tot = hkmm.calcTotE();  log.dbg("calcTotE()=" + tot);
//
//  Conf cfAB = confs.get(2);
//  engE2 = sysE2.calcH(cfAA, cfAB);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
//  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!

//
//  hkmm = new HkMm(quadr, a, a, a, b, K);
//  norm = hkmm.calcNorm();  log.dbg("HkMm(quadr, a, a, a, b, K.calcNorm()=\n" + norm);
}
}