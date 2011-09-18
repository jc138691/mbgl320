package papers.hy_swave;
import atom.AtomUtil;
import atom.angular.Spin;
import atom.e_1.SysE1;
import atom.e_2.SysAtomE2;
import atom.energy.ConfHMtrx;
import atom.energy.slater.SlaterLcr;
import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import atom.wf.log_cr.LcrFactory;
import math.func.arr.FuncArr;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import scatt.jm_2008.e3.JmDe3;
import scatt.jm_2008.e3.JmMethodJmBasisE3;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.target.JmTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/06/11,10:13 AM
 */
public abstract class HyLikeSWaveJm extends HyLikeSWave {
  public static Log log = Log.getLog(HyLikeSWaveJm.class);

  public void calcJm(int newN, int newNt) {
    SYS_LS = new Ls(0, SPIN);
    N = newN;
    Nt = newNt;
    initProject();
    jmPotTestOk();     // out: jmBasisN, orthonN, biorthN
    jmHyTestOk(TARGET_Z);      // out: pot (for TARGET_Z), orthonNt
    SlaterLcr slater = new SlaterLcr(quadrLcr);

    trgtBasisNt = orthonNt;
    trgtBasisN = orthonN;
    AtomUtil.trimTailSLOW(trgtBasisNt);
    AtomUtil.trimTailSLOW(trgtBasisN);
    JmTrgtE3 jmTrgt = makeTrgtE3(slater);
    jmTrgt.setScreenZ(TARGET_Z - 1);       // Hydrogen-like target has ONE elelctron
    jmTrgt.setInitTrgtIdx(FROM_CH);
    jmTrgt.setIonGrndEng(0);
    jmTrgt.setNt(orthonNt.size());
    jmTrgt.loadSdcsW();
    jmTrgt.removeClosed(jmOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);

    ConfHMtrx sysH = makeSysH(SYS_LS, slater);

    JmMethodJmBasisE3 method = new JmMethodJmBasisE3(jmOpt);
    method.setExclSysIdx(EXCL_SYS_RESON_IDX);     // [15Jun2011] TODO: remember to remove this
    method.setTrgtE3(jmTrgt);
    method.setSysH(sysH);
    Vec D = new JmDe3(biorthN, orthonN, method.getJmOpt().getJmTest());   log.dbg("D_{i<Nt}=must be ZERO=", D); // MUST BE ALL ZERO!!!!!
    method.setOverD(D);

    if (CALC_DENSITY) {
      FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
      FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadrLcr);     // NOTE!! convering density to R (not wf)
      FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelTrgtS2(method));
    }

    JmRes res;
    if (scttEngs != null) {
      res = method.calc(scttEngs);                  log.dbg("res=", res);
    }
    else {
      res = method.calcEngGrid();                  log.dbg("res=", res);
    }
    setupJmRes(res, method);
    res.writeToFiles();
  }


  private JmTrgtE3 makeTrgtE3(SlaterLcr slater) {
//    SysE1 tgrtE2 = new SysHy(slater);
    SysE1 tgrtE2 = new SysE1(-TARGET_Z, slater);

    Ls tLs = new Ls(0, Spin.ELECTRON);  // t - for target
    ConfArr tConfArr = ConfArrFactoryE2.makePoetConfE1(orthonNt);     log.dbg("tConfArr=", tConfArr);
    ConfHMtrx tH = new ConfHMtrx(tConfArr, tgrtE2);                                   log.dbg("tH=\n", new MtrxDbgView(tH));
    FileX.writeToFile(tH.getEigVal().toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgEngs_" + makeLabelBasisOptN());
    FileX.writeToFile(tH.getEngEv(0).toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgEngs_eV_" + makeLabelBasisOptN());

    JmTrgtE3 res = new JmTrgtE3();
    res.add(tH);
    res.makeReady();
    return res;
  }

  private ConfHMtrx makeSysH(Ls sLs, SlaterLcr slater) { //[14Apr2011] converted from HePoetBasisJm.makeSysH() for testing/debuging
//    SysAtomE2 sys = new SysAtomE2(-AtomHy.Z, slater);// NOTE -1 for Hydrogen
    SysAtomE2 sys = new SysAtomE2(-TARGET_Z, slater);// NOTE -1 for Hydrogen
    ConfArr sConfArr = ConfArrFactoryE2.makeSModelE2(sLs, orthonNt, orthonN);   log.dbg("sysArr=", sConfArr);
    ConfHMtrx res = new ConfHMtrx(sConfArr, sys);                  log.dbg("sysH=\n", new MtrxDbgView(res));
    return res;
  }
}