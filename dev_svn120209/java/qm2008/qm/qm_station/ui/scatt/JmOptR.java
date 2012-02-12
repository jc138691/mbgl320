package qm_station.ui.scatt;
import math.vec.grid.StepGridModel;
import scatt.jm_2008.e1.JmOptE1;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 14:32:19
 */
public class JmOptR extends JmOptE1 {
  public void loadDefault() {
    loadDefault(this);
  }
  public static void loadDefault(JmOptE1 model) {
    loadDefaultGridR(model.getGrid());
    model.getGridEng().loadDefault();
    model.getJmModel().loadDefault();
    model.getJmTest().loadDefault();
  }
  public static void loadDefaultGridR(StepGridModel grid) {
    grid.setFirst(0);
    grid.setLast(100);
    grid.setNumPoints(101);
  }
}
