package papers.he_swave.minmax;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.e_2.*;
import atom.energy.LsConfHMtrx;
import atom.energy.ConfHOvMtrx;
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
import math.func.arr.FuncArr;
import math.func.deriv.test.DerivPts9Test;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import math.vec.test.FastLoopTest;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 10/05/12, 9:25 AM
 */
public class HeAtomMM_try extends FlowTest {
public static Log log = Log.getLog(HeAtomMM_try.class);
private static WFQuadrLcr quadr;
private int K = 0;
private int L = 0;
// N-related
private int N = 5;
private LgrrOpt lgrrN;
private FuncArr orthN;
private double LAMBDA_N;

private SlaterLcr slater;
private static final double HY_1S = 1;
private static final double HY_2S = 0.5;
public HeAtomMM_try() {
  super(HeAtomMM_try.class);
}

public void testHeMm2() throws Exception {  log.setDbg();
  if (!new FastLoopTest().ok()) return;
  if (!new DerivPts9Test().ok()) return;

  double LCR_FIRST = -5 - 2 * Math.log(AtomHe.Z);   log.dbg("LCR_FIRST=", LCR_FIRST);
  int LCR_N = 1001;
  double R_FIRST = 0;
//  double R_LAST = 100 / AtomHe.Z;
  double R_LAST = 100.;
  int N_MI = 2;
  int N_MA = 20;
  double LAMBDA_MI = 3.65; //
  double LAMBDA_MA = 2;    //
//  double LAMBDA_MI = 3.65; // best single min/max 3.65/3
//  double LAMBDA_MA = 3;    // E= -2.8721105

  N = 10;
  LAMBDA_N = 2; // best single zeta 3.375 E=-2.84765625
  makeLgrrN();

  LgrrOpt lgrrMi = new LgrrOpt();
  lgrrMi.setL(0);
  lgrrMi.setLambda(LAMBDA_MI);
  lgrrMi.setN(N_MI);      log.dbg("lgrrMi=\n", lgrrMi);

  LgrrOpt lgrrMa = new LgrrOpt();
  lgrrMa.setL(0);
  lgrrMa.setLambda(LAMBDA_MA);
  lgrrMa.setN(N_MA);      log.dbg("lgrrMa=\n", lgrrMa);

  StepGridOpt gridR = new StepGridOpt(R_FIRST, R_LAST, LCR_N); // R_N not used!!!
  StepGridOpt gridLcr = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, gridR);   log.dbg("gridLcr =\n", gridLcr);
  StepGrid x = new StepGrid(gridLcr);                 log.dbg("StepGrid x = new StepGrid(gridLcr) =\n", x);
  quadr = new WFQuadrLcr(x);                          log.dbg("quadr = new WFQuadrLcr(x)=\n", quadr);
  log.dbg("quadr.getR()=\n", quadr.getR());

  // BUILD
  orthN = new LgrrOrthLcr(quadr, lgrrN);         log.dbg("orth = new LgrrOrthLcr(quadr, lgrrModel)=\n", orthN);
//  Func potFunc = new FuncPowInt(-AtomHe.Z, -1);  // f(r)=-atomZ/r
//  FuncVec pot = new FuncVec(quadr.getR(), potFunc);                       log.dbg("-1/r=", new VecDbgView(pot));
//  PotHMtrx trgtPotH = new PotHMtrxLcr(L, orth, pot, quadr);    log.dbg("trgtPotH=", trgtPotH);
//  Vec heEngs = trgtPotH.getEigEngs();            log.dbg("heEngs=", new VecDbgView(heEngs));
//  orth = trgtPotH.getEigWfs();      log.dbg("trgtWfsNt=", new FuncArrDbgView(orth));

  LgrrOrthLcr orthMi = new LgrrOrthLcr(quadr, lgrrMi);  log.dbg("orthMa=\n", orthMi);
  LgrrOrthLcr orthMa = new LgrrOrthLcr(quadr, lgrrMa);  log.dbg("orthMa=\n", orthMa);
//  LagrrLcr orthMi = new LagrrLcr(quadr, lgrrMi);  log.dbg("orthMa=\n", orthMi);
//  LagrrLcr orthMa = new LagrrLcr(quadr, lgrrMa);  log.dbg("orthMa=\n", orthMa);
  slater = new SlaterLcr(quadr);

  SysE2 sysE2 = new SysHe(slater);
  SysE2OldOk oldE2 = new SysHeOldOk(slater);
  SysHeMm mmE2 = new SysHeMm(slater);

  Ls sysLs = new Ls(0, Spin.SINGLET);  // t - for target
  LsConfs confs = ConfArrFactoryE2.makeSModelAllE2(sysLs, orthN, N);
  log.dbg("confs=", confs);
//  LsConfs confsMm = ConfArrFactoryE2.makeSModelAllE2(sysLs, orth, N_MI);
//  LsConfs confsMm = ConfArrFactoryE2.makeSModelAllE2(sysLs, orth, orthMa);
  LsConfs confsMm = ConfArrFactoryE2.makeSModelMmE2(sysLs, orthMi, orthMa);
//  LsConfs confsMm = ConfArrFactoryE2.makeSModelMm(sysLs, orthMi, N_MI);
  log.dbg("confsMm=", confsMm);

  // ================
  double norm, kin, pot1, pot2, tot;
  FuncVec a = orthN.get(0);

  // TEST AA AA
  log.info("TEST AA AA");
  HkMm hkmm = new HkMm(quadr, a, a, a, a, K);
  log.dbg("HkMm(quadr, a, a, a, a, K)");
  norm = hkmm.calcOv();  log.dbg("calcOv()=" + norm);
  assertEquals(0, norm - 1, 2e-14);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHe.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHe.Z);  log.dbg("calcTotE()=" + tot);

  LsConf cfAA = confs.get(0);
  Energy engE2 = sysE2.calcH(cfAA, cfAA);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 4e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 4e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 4e-13);  // GOOD TEST!!!

  //
  LsConfHMtrx sysH = new LsConfHMtrx(confs, sysE2); log.dbg("\n sysH=\n", new MtrxDbgView(sysH));
  LsConfHMtrx oldH = new LsConfHMtrx(confs, oldE2); log.dbg("oldH=\n", new MtrxDbgView(oldH));
  ConfHOvMtrx mmH = new ConfHOvMtrx(confsMm, mmE2);
//  ConfHOvMtrx mmH = new ConfHOvMtrx(confs, mmE2);
  log.dbg("\n mmH=\n", new MtrxDbgView(mmH));
  log.dbg("\n sysH=\n", new MtrxDbgView(sysH));

  Vec sysEngs = sysH.getEigEngs();
  log.dbg("\n sysEngs=\n", new VecDbgView(sysEngs));

  Vec oldEngs = oldH.getEigEngs();
  log.dbg("oldEngs=\n", new VecDbgView(oldEngs));

  Vec mmEngs = mmH.getEigEngs();
  log.dbg("\n mmEngs=\n", new VecDbgView(mmEngs));
  log.dbg("\n sysEngs=\n", new VecDbgView(sysEngs));
  log.dbg("HeSWaveAtom.E_1S=\n" + new VecDbgView(HeSWaveAtom.E_1S));

  log.dbg("\n sysVecs=\n", new MtrxDbgView(sysH.getEigVec()));
  log.dbg("\n mmVecs=\n", new MtrxDbgView(mmH.getEigVec()));

}
public void testHyMm() throws Exception {  log.setDbg();
  if (!new FastLoopTest().ok()) return;
  if (!new DerivPts9Test().ok()) return;

  int LCR_FIRST = -5;
  int LCR_N = 1001;
  int R_FIRST = 0;
  int R_LAST = 100;
  N = 2;
  LAMBDA_N = 2;
  makeLgrrN();

  StepGridOpt gridR = new StepGridOpt(R_FIRST, R_LAST, LCR_N); // R_N not used!!!
  StepGridOpt gridLcr = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, gridR);   log.dbg("gridLcr =\n", gridLcr);
  StepGrid x = new StepGrid(gridLcr);                 log.dbg("StepGrid x = new StepGrid(gridLcr) =\n", x);
  quadr = new WFQuadrLcr(x);                          log.dbg("quadr = new WFQuadrLcr(x)=\n", quadr);
  log.dbg("quadr.getR()=\n", quadr.getR());

  orthN = new LgrrOrthLcr(quadr, lgrrN);         log.dbg("orth = new LgrrOrthLcr(quadr, lgrrModel)=\n", orthN);
  slater = new SlaterLcr(quadr);

  // HYDROGEN
  SysE2 sysE2 = new SysHyE2(slater);
  SysE2OldOk oldE2 = new SysE2OldOk(AtomHy.Z, slater);
  SysHyMmE2 mmE2 = new SysHyMmE2(slater);

  Ls sysLs = new Ls(0, Spin.SINGLET);  // t - for target
  LsConfs confs = ConfArrFactoryE2.makeSModelE2(sysLs, orthN, orthN);
  log.dbg("confs=", confs);
  LsConfHMtrx sysH = new LsConfHMtrx(confs, sysE2); log.dbg("sysH=\n", new MtrxDbgView(sysH));
  LsConfHMtrx oldH = new LsConfHMtrx(confs, oldE2); log.dbg("oldH=\n", new MtrxDbgView(oldH));
  LsConfHMtrx mmH = new LsConfHMtrx(confs, mmE2); log.dbg("mmH=\n", new MtrxDbgView(mmH));

  Vec sysEngs = sysH.getEigEngs();
  log.dbg("sysEngs=\n", new VecDbgView(sysEngs));

  Vec oldEngs = oldH.getEigEngs();
  log.dbg("oldEngs=\n", new VecDbgView(oldEngs));

  assertEquals(0, Math.abs(sysEngs.getFirst() - oldEngs.getFirst()), 1e-14);

  Vec mmEngs = mmH.getEigEngs();
  log.dbg("mmEngs=\n", new VecDbgView(mmEngs));
  log.dbg("HeSWaveAtom.E_1S=\n" + new VecDbgView(HeSWaveAtom.E_1S));

  double norm, kin, pot1, pot2, tot;
  FuncVec a = orthN.get(0);
  FuncVec b = orthN.get(1);

  // TEST AA AA
  log.info("TEST AA AA");
  HkMm hkmm = new HkMm(quadr, a, a, a, a, K);
  log.dbg("HkMm(quadr, a, a, a, a, K)");
  norm = hkmm.calcOv();  log.dbg("calcOv()=" + norm);
  assertEquals(0, norm - 1, 2e-14);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHy.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHy.Z);  log.dbg("calcTotE()=" + tot);

  LsConf cfAA = confs.get(0);
  Energy engE2 = sysE2.calcH(cfAA, cfAA);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 3e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 3e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 3e-13);  // GOOD TEST!!!

  // TEST BB BB
  log.info("TEST BB BB");
  hkmm = new HkMm(quadr, b, b, b, b, K);
  log.dbg("HkMm(quadr, B, B, B, B, K)");
  norm = hkmm.calcOv();  log.dbg("calcOv()=" + norm);
  assertEquals(0, norm - 1, 1e-13);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHy.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHy.Z);  log.dbg("calcTotE()=" + tot);

  LsConf cfBB = confs.get(2);
  engE2 = sysE2.calcH(cfBB, cfBB);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 1e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 2e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 1e-12);  // GOOD TEST!!!

  // TEST AA BB
  log.info("TEST AA BB");
  hkmm = new HkMm(quadr, a, a, b, b, K);
  log.dbg("HkMm(quadr, a, a, B, B, K)");
  norm = hkmm.calcOv();  log.dbg("calcOv()=" + norm);
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
  LAMBDA_N = 4;
  makeLgrrN();

  StepGridOpt gridR = new StepGridOpt(R_FIRST, R_LAST, LCR_N); // R_N not used!!!
  StepGridOpt gridLcr = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, gridR);   log.dbg("gridLcr =\n", gridLcr);
  StepGrid x = new StepGrid(gridLcr);                 log.dbg("StepGrid x = new StepGrid(gridLcr) =\n", x);
  quadr = new WFQuadrLcr(x);                          log.dbg("quadr = new WFQuadrLcr(x)=\n", quadr);
  log.dbg("quadr.getR()=\n", quadr.getR());

  orthN = new LgrrOrthLcr(quadr, lgrrN);         log.dbg("orth = new LgrrOrthLcr(quadr, lgrrModel)=\n", orthN);
  slater = new SlaterLcr(quadr);

  SysE2 sysE2 = new SysHe(slater);
  SysE2OldOk oldE2 = new SysHeOldOk(slater);
  SysHeMm mmE2 = new SysHeMm(slater);

  Ls sysLs = new Ls(0, Spin.SINGLET);  // t - for target
  LsConfs confs = ConfArrFactoryE2.makeSModelE2(sysLs, orthN, orthN);
  log.dbg("confs=", confs);

  // ================
  double norm, kin, pot1, pot2, tot;
  FuncVec a = orthN.get(0);
  FuncVec b = orthN.get(1);

  // TEST AA AA
  log.info("TEST AA AA");
  HkMm hkmm = new HkMm(quadr, a, a, a, a, K);
  log.dbg("HkMm(quadr, a, a, a, a, K)");
  norm = hkmm.calcOv();  log.dbg("calcOv()=" + norm);
  assertEquals(0, norm - 1, 2e-14);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHe.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHe.Z);  log.dbg("calcTotE()=" + tot);

  LsConf cfAA = confs.get(0);
  Energy engE2 = sysE2.calcH(cfAA, cfAA);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 3e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 3e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 3e-13);  // GOOD TEST!!!

  // TEST BB BB
  log.info("TEST BB BB");
  hkmm = new HkMm(quadr, b, b, b, b, K);
  log.dbg("HkMm(quadr, B, B, B, B, K)");
  norm = hkmm.calcOv();  log.dbg("calcOv()=" + norm);
  assertEquals(0, norm - 1, 1e-13);  // GOOD TEST!!!
  kin = hkmm.calcKin();  log.dbg("calcKin()=" + kin);
  pot1 = hkmm.calcPot1(AtomHe.Z);  log.dbg("calcPot1()=" + pot1);
  pot2 = hkmm.calcPot2();  log.dbg("calcPot2()=" + pot2);
  tot = hkmm.calcTotE(AtomHe.Z);  log.dbg("calcTotE()=" + tot);

  LsConf cfBB = confs.get(N); // TAKING
  engE2 = sysE2.calcH(cfBB, cfBB);  log.dbg("sysE2.calcH(cf, cf2)=\n" + engE2);
  assertEquals(0, engE2.p1 - pot1, 1e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.p2 - pot2, 1e-13);  // GOOD TEST!!!
  assertEquals(0, engE2.kin - kin, 2e-12);  // GOOD TEST!!!
  assertEquals(0, engE2.pt+engE2.kin - tot, 1e-12);  // GOOD TEST!!!

  // TEST AA BB
  log.info("TEST AA BB");
  hkmm = new HkMm(quadr, a, a, b, b, K);
  log.dbg("HkMm(quadr, a, a, B, B, K)");
  norm = hkmm.calcOv();  log.dbg("calcOv()=" + norm);
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

  //
  LsConfHMtrx sysH = new LsConfHMtrx(confs, sysE2); log.dbg("\n sysH=\n", new MtrxDbgView(sysH));
  LsConfHMtrx oldH = new LsConfHMtrx(confs, oldE2); log.dbg("oldH=\n", new MtrxDbgView(oldH));
  ConfHOvMtrx mmH = new ConfHOvMtrx(confs, mmE2);
  log.dbg("\n mmH=\n", new MtrxDbgView(mmH));
  log.dbg("\n sysH=\n", new MtrxDbgView(sysH));

  Vec sysEngs = sysH.getEigEngs();
  log.dbg("\n sysEngs=\n", new VecDbgView(sysEngs));

  Vec oldEngs = oldH.getEigEngs();
  log.dbg("oldEngs=\n", new VecDbgView(oldEngs));

  assertEquals(0, Math.abs(sysEngs.getFirst() - oldEngs.getFirst()), 1e-14);
//  assertFloorRel("E_1s1s_1S", HeSWaveAtom.E_1s1s_1S, oldEngs.get(0), 2e-4);
//  assertFloorRel("E_1s2s_1S", HeSWaveAtom.E_1s2s_1S, oldEngs.get(1), 3e-5);

  Vec mmEngs = mmH.getEigEngs();
  log.dbg("\n mmEngs=\n", new VecDbgView(mmEngs));
  log.dbg("\n sysEngs=\n", new VecDbgView(sysEngs));
  log.dbg("HeSWaveAtom.E_1S=\n" + new VecDbgView(HeSWaveAtom.E_1S));
}

private void makeLgrrN() {
  lgrrN = new LgrrOpt();
  lgrrN.setL(0);
  lgrrN.setLambda(LAMBDA_N);
  lgrrN.setN(N);      log.dbg("lgrrModel=\n", lgrrN);
}

}