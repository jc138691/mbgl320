package qm_station.ui.scatt;
import math.vec.grid.StepGridOpt;
import scatt.jm_2008.e1.JmCalcOptE1;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 14:32:19
 */
public class CalcOptR extends JmCalcOptE1 {
  public void loadDefault() {
    loadDefault(this);
  }
  public static void loadDefault(JmCalcOptE1 model) {
    loadDefaultGridR(model.getGridOpt());
    model.getGridEng().loadDefault();
    model.getBasisOpt().loadDefault();
    model.getTestOpt().loadDefault();
  }
  public static void loadDefaultGridR(StepGridOpt grid) {
    grid.setFirst(0);
    grid.setLast(100);
    grid.setNumPoints(101);
  }
}
