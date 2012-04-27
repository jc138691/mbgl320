package scatt.jm_2008.jm.target;

import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.partial.wf.JmClmbLcr;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 18/05/2010, 9:19:32 AM
 */
public class ScttTrgtE2 {   // target properties
  public static Log log = Log.getLog(ScttTrgtE2.class);
  private Vec engs;
  private JmClmbLcr contE1;
  private FuncArr statesE1;
  private Vec sdcsContW;
  private Vec sdcsW;
  private int nt; // Nt - number of target orthonorm basis functions
  private double ionGrndEng;
  private int initTrgtIdx;
  private int screenZ;
  public void setEngs(Vec targetEngs) {
    this.engs = targetEngs;
  }
  public void replaceTrgtEngs(double[] from, int toIdxExc) {
    log.info("OLD   target engs=", engs);
    log.info("REPLACE with engs=", new Vec(from));
    for (int i = 0; i < toIdxExc; i++) {
      engs.set(i, from[i]);
    }
    log.info("NEW   target engs=", engs);
  }

  public Vec getEngs() {
    return engs;
  }

  public void setContE1(JmClmbLcr continmE1) {
    this.contE1 = continmE1;
  }

  public void setStatesE1(FuncArr statesE1) {
    this.statesE1 = statesE1;
  }
public FuncArr getStatesE1() {
  return statesE1;
}
// via continuum overlap
  public void loadSdcsContW() {
    Vec res =  new Vec(engs.size());
    WFQuadrLcr quadr = contE1.getQuadrLcr();
    for (int i = 0; i < engs.size(); i++) {              log.dbg("i = ", i);
      FuncVec cont = contE1.get(i);
      FuncVec wf = statesE1.get(i);
      double e = engs.get(i);
      double w = 0;
      if (e > 0) {
        w = quadr.calcInt(cont, wf);
        w = 1. / (w * w);
      }
      res.set(i, w);
    }
    setSdcsW(res);
    setSdcsContW(res);
  }

  public void loadSdcsW() {
    Vec res =  new Vec(engs.size());
    res.set(0, 0.);
    res.set(res.size()-1, 0.);
    for (int i = 1; i < engs.size()-1; i++) {              log.dbg("i = ", i);   // NOTE from 1 and "-1"
      double e = engs.get(i);
      double e2 = engs.get(i+1);
      double ePrev = engs.get(i-1);
      double w = 0;
      if (e > 0) {
        w = 0.5 * (e2 - ePrev);
      }
      res.set(i, w);
    }
    setSdcsW(res);
  }

  public String toTab() {
    final int IDX_ENG = 0;
    final int IDX_W = 1;
    final int IDX_CONT_W = 2;
    final int N_COL = 3;
    int NUM_DIGS = 5;
    Mtrx m = new Mtrx(engs.size(), N_COL);
    for (int i = 0; i < engs.size(); i++) {
      m.set(i, IDX_ENG, engs.get(i));
      m.set(i, IDX_W, sdcsW.get(i));
      m.set(i, IDX_CONT_W, sdcsContW.get(i));
    }
    return m.toTab(NUM_DIGS);
  }


  public void setSdcsContW(Vec sdcsContW) {
    this.sdcsContW = sdcsContW;
  }

  public Vec getSdcsContW() {
    return sdcsContW;
  }

  public void setSdcsW(Vec sdcsW) {
    this.sdcsW = sdcsW;
  }

  public Vec getSdcsW() {
    return sdcsW;
  }
  public int getNt() {
    return nt;
  }
  public void setNt(int nt) {
    this.nt = nt;
  }
  public double getIonGrndEng() {
    return ionGrndEng;
  }
  public void setIonGrndEng(double ionGrndEng) {
    this.ionGrndEng = ionGrndEng;
  }
  public void setInitTrgtIdx(int initTrgtIdx) {
    this.initTrgtIdx = initTrgtIdx;
  }
  public int getInitTrgtIdx() {
    return initTrgtIdx;
  }
  public double getInitTrgtEng() {
    return engs.get(initTrgtIdx);
  }
  public int getScreenZ() {
    return screenZ;
  }
  public void setScreenZ(int initZ) {
    this.screenZ = initZ;
  }
}
