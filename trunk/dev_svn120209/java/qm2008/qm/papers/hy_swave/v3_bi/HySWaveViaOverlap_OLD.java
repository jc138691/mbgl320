package papers.hy_swave.v3_bi;

import atom.data.AtomHy;
import atom.e_2.SysE2OldOk;
import atom.energy.LsConfHMtrx;
import atom.energy.ConfOvMtrx;
import atom.energy.pw.lcr.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.shell.LsConfs;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import papers.hy_swave.HySWaveJmBasisHy;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.ScttTrgtE2;
import scatt.jm_2008.jm.fanox.JmMethodFanoE2;

import javax.iox.FileX;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 02/06/2010, 9:08:57 AM
 */
public class HySWaveViaOverlap_OLD extends HySWaveJmBasisHy {
  public static Log log = Log.getLog(HySWaveViaOverlap_OLD.class);

  public static void main(String[] args) {
    // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HySWaveViaOverlap_OLD runMe = new HySWaveViaOverlap_OLD();
    runMe.setUp();
    runMe.testRun();
  }

  public void calc(int newN, int newNt) {
    N = newN;
    Nt = newNt;
    initProject();
    potScattTestOk();
    hydrScattTestOk(AtomHy.Z);

    trgtPotH = new PotHMtrxLcr(L, orthNt, pot);   log.dbg("trgtPotH=", trgtPotH);
    Vec trgtEngs = trgtPotH.getEigEngs();                     log.dbg("eigVal=", new VecDbgView(trgtEngs));

    ScttTrgtE2 trgtUtils = new ScttTrgtE2();
    trgtUtils.setEngs(trgtEngs);
    trgtUtils.loadSdcsW();

    Ls sLs = new Ls(0, SPIN);         // s - for system

    // NOTE [dak 24Mar2011] Use (Nt, N). (N,N) is not allowed, see 2011 e-He paper
//    LsConfs sConfArr = ConfArrFactoryE2.makeSModelAllE2(sLs, orthNt, orthNt);   log.dbg("sConfArr=", sConfArr);
    LsConfs sConfArr = ConfArrFactoryE2.makeSModelE2(sLs, orthNt, orthN);   log.dbg("sConfArr=", sConfArr);

    LsConfs chiArr = ConfArrFactoryE2.makeTwoElec_forJmV2(sLs, L, orthNt
      , lgrrBiN.size(), lgrrBiN.getLast());   log.dbg("chiArr=", chiArr);

    SlaterLcr slater = new SlaterLcr(quadr);
    SysE2OldOk sys = new SysE2OldOk(AtomHy.Z, slater);// NOTE -1 for Hydrogen
    ConfOvMtrx chiOv = new ConfOvMtrx(sConfArr, sys, chiArr);     log.dbg("chiOv=\n", new MtrxDbgView(chiOv));
    LsConfHMtrx sysH = new LsConfHMtrx(sConfArr, sys);                  log.dbg("sysConfH=\n", new MtrxDbgView(sysH));

    JmMethodFanoE2 method = new JmMethodFanoE2(calcOpt);
    Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
//    double e0 = sEngs.get(0);
    method.setSysEngs(sEngs);
    method.setSysConfH(sysH);
    method.setTrgtE2(trgtUtils);
    method.setTargetH(trgtPotH);
    method.setChiOv(chiOv);
    ScttRes res = method.calcScttEngModel();                  log.dbg("res=", res);

    FileX.writeToFile(sEngs.toCSV(), HOME_DIR, "hy"
      , "hy_poet_sysEngs_S" + sLs.getS21() + "_" + lgrrOptN.makeLabel()+"_v3.dat");
    FileX.writeToFile(res.getCrossSecs().toTab(), HOME_DIR, "hy"
      , "hy_poet_cross_S" + sLs.getS21() + "_" + lgrrOptN.makeLabel()+"_v3.dat");
    FileX.writeToFile(res.getTics().toTab(), HOME_DIR, "hy"
      , "hy_poet_TICS_S" + sLs.getS21() + "_" + lgrrOptN.makeLabel()+"_v3.dat");

    FileX.writeToFile(res.getSdcs().toTab(), HOME_DIR, "hy"
      , "hy_poet_SDCS_S" + sLs.getS21() + "_" + lgrrOptN.makeLabel()+"_v3.dat");
  }


}