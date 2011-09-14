package papers.he_swave;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.ConfHMtrx;
import atom.energy.part_wave.PartHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.smodel.HeSWaveAtomNt50_LMBD4p0;
import atom.wf.log_cr.LcrFactory;
import math.func.arr.FuncArr;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e2.JmResonancesE2;
import scatt.jm_2008.e3.JmDe3;
import scatt.jm_2008.e3.JmMethodJmBasisE3;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.laguerre.JmLgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.JmLgrrOrthLcr;
import scatt.jm_2008.jm.target.JmTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/05/11,11:30 AM
 */
public class HeSWaveBasisJm extends HeSWaveScatt {
  public static Log log = Log.getLog(HeSWaveBasisJm.class);
  public static void main(String[] args) {
    // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HeSWaveBasisJm runMe = new HeSWaveBasisJm();
    runMe.setUp();
    runMe.testRun();
  }

  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HeSWaveBasisJm", "110606");
    TARGET_Z = AtomHe.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_NAME = "HeSModelBasisJm";
    MODEL_DIR = MODEL_NAME;
    LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;
    // Note: run one at a time as only one set of result files is produced
    runJob();
  }

  public void setUp() {
    super.setUp();
    log.info("log.info(HeSWaveBasisJm)");
    JmResonancesE2.log.setDbg();
    log.setDbg();
  }

  public void runJob() {
//    // Nt= 80
//    int currN = 81;
//    LCR_FIRST = -7;
//    LCR_N = 1601;
//    R_LAST = 450;

//    // Nt= 70
//    int currN = 71;
//    LCR_FIRST = -5;
//    LCR_N = 1201;
//    R_LAST = 330;

    // upto N=50
//    LCR_FIRST = -5;  //-5
//    LCR_N = 1001;  //901
//    R_LAST = 250;

    // upto N=40
    LCR_FIRST = -5;
    LCR_N = 701;
    R_LAST = 200;

    Nc = 10;
    int currNt = 20;
    int currN = 21;
//    int currN = currNt + 1;
    IGNORE_BUG_PoetHeAtom = true;
    CALC_DENSITY = false;

    SPIN = Spin.ELECTRON;
    calcJm(currN, currNt);
//    calcJm(12, 11);
//    calcJm(13, 12);
//    calcJm(14, 13);
  }


  public void calcJm(int newN, int newNt) {
    log.info("-->calcJm(newN"+newN+", newNt="+newNt);
    N = newN;
    Nt = newNt;
    initProject();
    jmPotTestOk();     // out: jmBasisN, orthonN, biorthN
    jmHyTestOk(AtomHy.Z);      // out: pot (for Hy), orthonNt
    jmHyTestOk(AtomHe.Z);      // out: pot (for He), orthonNt
    jmHeTestOk();      // out: re-loading pot (for He)
    initLiJm();
    SlaterLcr slater = new SlaterLcr(quadrLcr);
    calcHe(slater);    //verified: SysHe_OLD and SysHe yield exactly the same results.
    calcLi(slater);

    // Making He+ eigen-states from Nc (core N).   this is only to calc ionization threshold
    JmLgrrModel lgrrOptNc = new JmLgrrModel(basisOptN); // for the target N, i.e. N_t
    lgrrOptNc.setN(Nc);                                    log.dbg("Laguerr model (N_c)=", lgrrOptNc);
    orthonNc = new JmLgrrOrthLcr(quadrLcr, lgrrOptNc);     log.dbg("JmLgrrOrthLcr(N_c) = ", orthonNc);
    trgtPartH = new PartHMtrxLcr(L, orthonNc, pot);        log.dbg("trgtPartH=", trgtPartH);
    Vec basisEngs = trgtPartH.getEigVal();                 log.dbg("eigVal=", new VecDbgView(basisEngs));
    FileX.writeToFile(basisEngs.toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_NcEngs_" + makeLabelNc());

    trgtBasisNt = orthonNt;
    trgtBasisN = orthonN;
    JmTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtBasisNt);
    jmTrgt.setInitTrgtIdx(FROM_CH);
    jmTrgt.setIonGrndEng(basisEngs.getFirst());
    jmTrgt.removeClosed(jmOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);
    jmTrgt.setNt(trgtBasisNt.size());
    jmTrgt.replaceTrgtEngs(HeSWaveAtomNt50_LMBD4p0.E_SORTED);   log.info("REPLACING trgt engs with=", HeSWaveAtomNt50_LMBD4p0.E_SORTED);
    jmTrgt.loadSdcsW();
    saveTrgtInfo(jmTrgt);

    ConfHMtrx sysH = makeSysBasisN(slater);

    JmMethodJmBasisE3 method = new JmMethodJmBasisE3(jmOpt);
    method.setTrgtE3(jmTrgt);
    method.setSysH(sysH);
    Vec D = new JmDe3(biorthN, orthonN, method.getJmOpt().getJmTest());   log.dbg("D_{i<Nt}=must be ZERO=", D); // MUST BE ALL ZERO!!!!!
    method.setOverD(D);

    if (CALC_DENSITY) {          log.info("if (CALC_DENSITY) {");
      FuncArr sysDens = sysH.getDensity();
      FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadrLcr);  // NOTE!! convering density to R (not wf)
      FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelNc(method));
    }

    JmRes res;
    if (scttEngs != null) {
      res = method.calc(scttEngs);                  log.dbg("res=", res);
    }
    else {
      res = method.calcEngGrid();                  log.dbg("res=", res);
    }
    setupJmRes(res, method);

//    JmResonancesE2.saveResRadDist(RES_MAX_LEVEL, res, sysH);
    res.writeToFiles();
  }

}