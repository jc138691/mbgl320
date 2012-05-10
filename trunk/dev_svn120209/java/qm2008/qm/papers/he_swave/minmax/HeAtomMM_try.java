package papers.he_swave.minmax;
import atom.angular.Spin;
import atom.e_2.SysE2;
import atom.e_2.SysE2OldOk;
import atom.e_2.SysHe;
import atom.e_2.SysHeOldOk;
import atom.energy.ConfHMtrx;
import atom.energy.Energy;
import atom.energy.slater.SlaterLcr;
import atom.shell.Conf;
import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import atom.smodel.HeSWaveAtom;
import atom.wf.lcr.LcrFactory;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.mm.SysHeMm;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
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
private LgrrOrthLcr orthonN;
private SlaterLcr slater;
public HeAtomMM_try() {
  super(HeAtomMM_try.class);
}

public void testHeMm() {  log.setDbg();
  int LCR_FIRST = -5;
  int LCR_N = 701;
  int R_FIRST = 0;
  int R_LAST = 200;
  N = 2;
  int LAMBDA = 2;

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

  Conf cf = confs.get(0);
  Conf cf2 = confs.get(1);
  Energy e = sysE2.calcH(cf, cf2);  log.dbg("sysE2.calcH(cf, cf2)=\n" + e);
  Energy mme = mmE2.calcH(cf, cf2);  log.dbg("mmE2.calcH(cf, cf2)=\n" + mme);
}
}