package scatt.jm_2008.e1;
import math.vec.grid.StepGridOpt;
import papers.project_setup.ProjTestOpt;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 14:27:22
*/
public class JmCalcOptE1 {
private EngModel gridEng;
private StepGridOpt gridOpt;
private LgrrOpt basisOpt;
private ProjTestOpt testOpt;
private String saveFileName;
private String gridName;
private int optIdx;

private boolean useClosed;
private int useClosedNum;

private String homeDir;

private boolean calcSdcs;
private int sdcsEngN;
private int jmTailN;

public JmCalcOptE1() {
  init();
}
private void init() {
  gridEng = new EngModel() ;
  gridOpt = new StepGridOpt();
  basisOpt = new LgrrOpt();
  testOpt = new ProjTestOpt();
  saveFileName = "file name";
  gridName = "gridOpt";
//    optIdx = OPT_LGRR;
}
public void setSdcsEngN(int sdcsEngN) {
  this.sdcsEngN = sdcsEngN;
}
public int getSdcsEngN() {
  return sdcsEngN;
}
public int getJmTailN() {
  return jmTailN;
}
public void setJmTailN(int jmTailN) {
  this.jmTailN = jmTailN;
}
public EngModel getGridEng() {
  return gridEng;
}
public void setGridEng(EngModel gridEng) {
  this.gridEng = gridEng;
}
public StepGridOpt getGridOpt() {
  return gridOpt;
}
public void setGridOpt(StepGridOpt gridOpt) {
  this.gridOpt = gridOpt;
}
public LgrrOpt getBasisOpt() {
  return basisOpt;
}
public void setBasisOpt(LgrrOpt basisOpt) {
  this.basisOpt = basisOpt;
}
public String getSaveFileName() {
  return saveFileName;
}
public void setSaveFileName(String saveFileName) {
  this.saveFileName = saveFileName;
}
public String getGridName() {
  return gridName;
}
public void setGridName(String gridName) {
  this.gridName = gridName;
}
public ProjTestOpt getTestOpt() {
  return testOpt;
}
public void setTestOpt(ProjTestOpt testOpt) {
  this.testOpt = testOpt;
}
public int getOptIdx() {
  return optIdx;
}
public void setOptIdx(int optIdx) {
  this.optIdx = optIdx;
}

public int getN() {
  return basisOpt.getN();
}
public int getL() {
  return basisOpt.getL();
}
public boolean getUseClosed() {
  return useClosed;
}
public void setUseClosed(boolean useClosed) {
  this.useClosed = useClosed;
}
public void setHomeDir(String homeDir) {
this.homeDir = homeDir;
}
public String getHomeDir() {
  return homeDir;
}
public void setCalcSdcs(boolean calcSdcs) {
  this.calcSdcs = calcSdcs;
}
public boolean getCalcSdcs() {
  return calcSdcs;
}
public void setUseClosedNum(int useClosedNum) {
  this.useClosedNum = useClosedNum;
}
public int getUseClosedNum() {
  return useClosedNum;
}
}
