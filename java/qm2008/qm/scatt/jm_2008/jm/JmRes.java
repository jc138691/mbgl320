package scatt.jm_2008.jm;

import math.complex.CmplxVec;
import math.func.FuncVec;
import math.mtrx.Mtrx;
import scatt.jm_2008.e2.JmMethodBaseE2;

import javax.iox.FileX;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 17:00:55
 */
public class JmRes {
  private FuncVec cross;
  private FuncVec shift;
  private Mtrx crossSecs;
  private Mtrx mtrxShift;
  private Mtrx tics;     // total ionization cross section
  private Mtrx sdcs;     // singly differential ionization cross sections
  private CmplxVec resDlts;
  private Mtrx resInfo;     // info about resonances
  private String homeDir;
  private String modelDir;
  private String calcLabel;
  private String modelName;
  private JmMethodBaseE2 methodE2;
  public Mtrx getResInfo() {
    return resInfo;
  }
  public void setResInfo(Mtrx resInfo) {
    this.resInfo = resInfo;
  }
  public void setCross(FuncVec cross) {
    this.cross = cross;
  }

  public FuncVec getCross() {
    return cross;
  }
  public void setShift(FuncVec shift) {
    this.shift = shift;
  }

  public FuncVec getShift() {
    return shift;
  }

  public void setCrossSecs(Mtrx crossSecs) {
    this.crossSecs = crossSecs;
  }

  public Mtrx getCrossSecs() {
    return crossSecs;
  }

  public void setMtrxShift(Mtrx mtrxShift) {
    this.mtrxShift = mtrxShift;
  }

  public Mtrx getMtrxShift() {
    return mtrxShift;
  }

  public void setTics(Mtrx tics) {
    this.tics = tics;
  }

  public Mtrx getTics() {
    return tics;
  }

  public Mtrx getSdcs() {
    return sdcs;
  }

  public void setSdcs(Mtrx sdcs) {
    this.sdcs = sdcs;
  }
  public void setResDlts(CmplxVec resDlts) {
    this.resDlts = resDlts;
  }
  public void setHomeDir(String homeDir) {
    this.homeDir = homeDir;
  }
  public void setModelDir(String modelDir) {
    this.modelDir = modelDir;
  }
  public void setCalcLabel(String calcLabel) {
    this.calcLabel = calcLabel;
  }
  public void writeToFiles() {
    FileX.writeToFile(getResInfo().toTab(), homeDir, modelDir, modelName + "_resInfo_" + calcLabel);  // resonances
    FileX.writeToFile(getCrossSecs().toTab(), homeDir, modelDir, modelName+"_TCS_" + calcLabel);  // total cross sections
    FileX.writeToFile(getTics().toTab(), homeDir, modelDir, modelName+"_TICS_" + calcLabel);
    FileX.writeToFile(methodE2.getSysEngs().toCSV(), homeDir, modelDir, modelName+"_sysEngs_" + calcLabel);
    if (getSdcs() != null) {
      FileX.writeToFile(getSdcs().toTab(), homeDir, modelDir, modelName + "_SDCS_" + calcLabel);
    }
  }
  public void setModelName(String modelName) {
    this.modelName = modelName;
  }
  public void setMethodE2(JmMethodBaseE2 methodE2) {
    this.methodE2 = methodE2;
  }
}
