package scatt.jm_2008.e1;
import math.vec.grid.StepGridModel;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.TestModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 14:27:22
 */
public class CalcOptE1 {
//  public static int N_OPTS = 0;
//  public static final int OPT_LGRR = N_OPTS++;
//  public static final int OPT_ORTH = N_OPTS++;
//  public static final int OPT_POT = N_OPTS++;
//  public static final int OPT_NUMEROV = N_OPTS++;
//  public static final int OPT_K_MTRX_FUNC_ARR = N_OPTS++;
//  public static final int OPT_K_MTRX_ENG = N_OPTS++;
//  public static final int OPT_GNN_ENG = N_OPTS++;
//  public static final int OPT_KNN_ENG = N_OPTS++;
//  public static final String[] OPT_NAMES = {"Lagerre"
//    , "Orthonormal"
//    , "r^2 * V_1S(r)"
//    , "Numerov"
//    , "K(E, r)"
//    , "K(E)"
//    , "G_{N-1,N-1}(E)"
//    , "K"};
//  public static final String[] OPT_HELP_TIPS = {
//    "JM Laguerre functions"
//    , "JM orthonormal Laguerre functions"
//    , "Hydrogen ground state potential as felt by an external electron (NOTE! multiplied by r^2)"
//    , "Numerical solution [d2/dr2+2E-2V]y(r)=0 via the Numerov algorithm"
//    , "K(E,r) from y(r)=sin(pr)+K(E,r)*cos(pr), y(r) is via Numerov"
//    , "K(E,r=r_max)-matrix from y(r)=sin(pr)+K(E)*cos(pr), the last K(E,r)"
//    , "G_{N-1,N-1} * J_{N,N-1}(E)"
//    , "K(E) - from the J-Matrix method"
//  };

  private EngModel gridEng;
  private StepGridModel grid;
  private LgrrModel lgrrModel;
  private TestModel testModel;
  private String saveFileName;
  private String gridName;
  private int optIdx;
  private boolean useClosed;
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
}
