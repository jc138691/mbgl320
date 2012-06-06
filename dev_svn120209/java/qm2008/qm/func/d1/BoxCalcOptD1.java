package func.d1;
import math.vec.grid.StepGridOpt;
import papers.project_setup.ProjTestOpt;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 9:26 AM
 */
public class BoxCalcOptD1 {
private ProjTestOpt testOpt;
private StepGridOpt gridOpt;
private BoxTrigOpt boxTrigOpt;
public BoxCalcOptD1() {
  init();
}
private void init() {
  testOpt = new ProjTestOpt();
  gridOpt = new StepGridOpt();
  boxTrigOpt = new BoxTrigOpt();
//  saveFileName = "file name";
//  gridName = "grid";
}
public ProjTestOpt getTestOpt() {
  return testOpt;
}
public void setTestOpt(ProjTestOpt testOpt) {
  this.testOpt = testOpt;
}
public StepGridOpt getGridOpt() {
  return gridOpt;
}
public void setGridOpt(StepGridOpt gridOpt) {
  this.gridOpt = gridOpt;
}
public BoxTrigOpt getBoxTrigOpt() {
  return boxTrigOpt;
}
public void setBoxTrigOpt(BoxTrigOpt boxTrigOpt) {
  this.boxTrigOpt = boxTrigOpt;
}
}
