package papers.hy_swave;

import atom.AtomUtil;
import atom.data.AtomHy;
import atom.e_2.SysAtomE2;
import atom.energy.ConfHMtrx;
import atom.energy.part_wave.PartHMtrx;
import atom.energy.part_wave.PartHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import atom.wf.log_cr.LcrFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e2.JmMethodE2;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.target.JmTrgtE2;
import scatt.jm_2008.jm.coulomb.JmCoulombLcrTest;
import scatt.jm_2008.jm.theory.JmD;
import scatt.partial.wf.JmCoulombLcr;

import javax.iox.FileX;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 1:03:23 PM
 */
public class HySWaveBasisHy extends HySWaveBasisJm {
  public static Log log = Log.getLog(HySWaveBasisHy.class);

  public static void main(String[] args) {
    // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HySWaveBasisHy runMe = new HySWaveBasisHy();
    runMe.setUp();
    runMe.testRun();
  }

  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HySWaveBasisHy", "110606");
    TARGET_Z = AtomHy.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_NAME = "HySModelBasisHy";    MODEL_DIR = MODEL_NAME;
    CALC_TRUE_CONTINUUM = false; // if TRUE, increase LCR_N by about times 2.5
    LAMBDA = 2; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;
//    LAMBDA = 1.7;

    // Note: run one at a time as only one set of result files is produced
    setupEngExcite();
//    setupResonances_n2_n3_S1();
//    setupResonances_n2_S1();
//    setupEngExcite();
//    setupEngTICS();
//    setupEngSDCS();
    USE_CLOSED_CHANNELS = true;
    CALC_DENSITY = false;
    runJob();
  }

  public void setUp() {
    super.setUp();
    log.info("log.info(HySWaveBasisHy)");
    log.setDbg();
  }

  public void calcJm(int newN, int newNt) {
    SYS_LS = new Ls(0, SPIN);
    N = newN;
    Nt = newNt;
    initProject();
    jmPotTestOk();  // jmBasisN, biorthN, orthonN, quadrLcr
    jmHyTestOk(TARGET_Z);  // pot, orthonNt

    trgtPartH = new PartHMtrxLcr(L, orthonNt, pot);    log.dbg("trgtPartH=", trgtPartH);
    Vec targetEngs = trgtPartH.getEigVal();            log.dbg("eigVal=", new VecDbgView(targetEngs));
    trgtBasisNt = trgtPartH.getEigFuncArr();      log.dbg("trgtBasisNt=", new FuncArrDbgView(trgtBasisNt));
    FileX.writeToFile(trgtBasisNt.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtBasisNtLcr_" + makeLabelTrgtS2());

    FuncArr basisR = LcrFactory.wfLcrToR(trgtBasisNt, quadrLcr);
    AtomUtil.trimTailSLOW(basisR);
    FileX.writeToFile(basisR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtBasisNtR_" + makeLabelTrgtS2());

    AtomUtil.trimTailSLOW(trgtBasisNt);

    JmTrgtE2 jmTrgt = new JmTrgtE2();
    jmTrgt.setNt(orthonNt.size());
    jmTrgt.setEngs(targetEngs);
    jmTrgt.loadSdcsW();
    if (CALC_TRUE_CONTINUUM) {
      // TARGET CONTINUUM
      double maxSysEng = jmOpt.getGridEng().getLast() + targetEngs.get(0); // ASSUMED FROM H(1s)
      JmCoulombLcr clmbNt = new JmCoulombLcr(L, AtomHy.Z, targetEngs, maxSysEng, quadrLcr);      log.dbg("JmCoulombLcr =\n", clmbNt);
      AtomUtil.setTailFrom(clmbNt, trgtBasisNt);
      FileX.writeToFile(clmbNt.toTab(), HOME_DIR, "wf", "clmbNt.dat");
      if (!new JmCoulombLcrTest(clmbNt, trgtBasisNt).ok()) return;

      jmTrgt.setStatesContE1(clmbNt);
      jmTrgt.setStatesE1(trgtBasisNt);
      jmTrgt.loadSdcsContW();
      FileX.writeToFile(jmTrgt.toTab(), HOME_DIR, "wf", "target_" + basisOptN.makeLabel() + ".dat");
    }

    PartHMtrx targetHTest = new PartHMtrxLcr(L, trgtBasisNt, pot, orthonNt.getQuadr());  log.dbg("targetHTest=", targetHTest); // only for debugging
    Vec D = new JmD(biorthN, trgtBasisNt);      log.dbg("D_{n,m>=Nt}=must be ZERO=", D); // MUST BE ALL ZERO!!!!!

    SYS_LS = new Ls(0, SPIN);
    ConfArr sysArr = ConfArrFactoryE2.makeSModelE2(SYS_LS, trgtBasisNt, orthonN);    log.dbg("sysArr=", sysArr);

    // one electron basis
    trgtBasisN = orthonN;    // only the last wfs were used from  orthonN, so now we can reuse it
    orthonN = null; // making sure nobody uses old ref
    trgtBasisN.copyFrom(trgtBasisNt, 0, trgtBasisNt.size());
    D = new JmD(biorthN, trgtBasisN);   log.dbg("D_{n,N-1}=", D);

    SlaterLcr slater = new SlaterLcr(quadrLcr);
//    SysE2_OLD sys = new SysE2_OLD(-1., slater);// NOTE -1 for Hydrogen
    SysAtomE2 sys = new SysAtomE2(-TARGET_Z, slater);// NOTE -1 for Hydrogen
    ConfHMtrx sysH = new ConfHMtrx(sysArr, sys);    log.dbg("sysH=\n", new MtrxDbgView(sysH));
    Vec sysEngs = sysH.getEigVal();                 log.dbg("sysH=", sysEngs);
//    double e0 = sysEngs.get(0);

    JmMethodE2 method = new JmMethodE2(jmOpt);
    method.setTrgtE2(jmTrgt);
    method.setOverD(D);
    method.setSysH(sysH);
    method.setSysEngs(sysEngs);

    if (CALC_DENSITY) {
      FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
      FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadrLcr);  // NOTE!! convering density to R (not wf)
      FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelTrgtS2(method));
    }

    JmRes res;
    res = method.calcMidSysEngs();                  log.dbg("res=", res);
    if (scttEngs != null) {
      res = method.calc(scttEngs);                  log.dbg("res=", res);
    }
    else {
      res = method.calcEngGrid();                  log.dbg("res=", res);
    }
//    JmRes res = method.calcSysEngs();                  log.dbg("res=", res);
    setupJmRes(res, method);                        log.dbg("res=", res);
    res.writeToFiles();
  }

}