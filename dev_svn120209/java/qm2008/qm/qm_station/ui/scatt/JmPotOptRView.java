package qm_station.ui.scatt;
import qm_station.QMS;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 15:33:30
 */
public class JmPotOptRView extends JmPotOptView {
  public JmPotOptRView(QMS model) {
    super(model);
  }
  public void loadTo(QMS project) {
    CalcOptR model = project.getJmPotOptR();
    loadTo(model);
  }
  public void loadFrom(QMS project) {
    CalcOptR model = project.getJmPotOptR();
    model.setGridName("Radial grid");
    loadFrom(model);
  }
}
