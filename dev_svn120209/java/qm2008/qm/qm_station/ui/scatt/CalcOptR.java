package qm_station.ui.scatt;
import math.vec.grid.StepGridModel;
import scatt.jm_2008.e1.CalcOptE1;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 14:32:19
 */
public class CalcOptR extends CalcOptE1 {
  public void loadDefault() {
    loadDefault(this);
  }
  public static void loadDefault(CalcOptE1 model) {
    loadDefaultGridR(model.getGrid());
    model.getGridEng().loadDefault();
    model.getLgrrModel().loadDefault();
    model.getTestModel().loadDefault();
  }
  public static void loadDefaultGridR(StepGridModel grid) {
    grid.setFirst(0);
    grid.setLast(100);
    grid.setNumPoints(101);
  }
}
