package scatt.jm_2008.e1;
import math.vec.grid.StepGridModel;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.TestModel;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 14:27:22
*/
public class CalcOptE1 {
private EngModel gridEng;
private StepGridModel grid;
private LgrrModel lgrrModel;
private TestModel testModel;
private String saveFileName;
private String gridName;
private int optIdx;
private boolean useClosed;
private String homeDir;
private boolean calcSdcs;
public CalcOptE1() {
  init();
}
private void init() {
  gridEng = new EngModel() ;
  grid = new StepGridModel();
  lgrrModel = new LgrrModel();
  testModel = new TestModel();
  saveFileName = "file name";
  gridName = "grid";
//    optIdx = OPT_LGRR;
}
public EngModel getGridEng() {
  return gridEng;
}
public void setGridEng(EngModel gridEng) {
  this.gridEng = gridEng;
}
public StepGridModel getGrid() {
  return grid;
}
public void setGrid(StepGridModel grid) {
  this.grid = grid;
}
public LgrrModel getLgrrModel() {
  return lgrrModel;
}
public void setLgrrModel(LgrrModel lgrrModel) {
  this.lgrrModel = lgrrModel;
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
public TestModel getTestModel() {
  return testModel;
}
public void setTestModel(TestModel testModel) {
  this.testModel = testModel;
}
public int getOptIdx() {
  return optIdx;
}
public void setOptIdx(int optIdx) {
  this.optIdx = optIdx;
}

public int getN() {
  return lgrrModel.getN();
}
public int getL() {
  return lgrrModel.getL();
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
}
