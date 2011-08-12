package qm_station.ui.scatt;
import qm_station.QMS;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 24/09/2008, Time: 11:24:48
 */
public class JmPotOptLCRView extends JmPotOptView {
  public JmPotOptLCRView(QMS model) {
    super(model);
  }
  public void loadTo(QMS project) {
    JmOptLcr model = project.getJmPotOptLcr();
    loadTo(model);
  }
  public void loadFrom(QMS project) {
    JmOptLcr model = project.getJmPotOptLcr();
    model.setGridName("LCR grid");
    loadFrom(model);
  }
}
