package qm_station.ucm.save;
import qm_station.QMSProject;
import qm_station.ucm.plot.UCPlotFuncArr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 13:52:36
 */
public class UCSaveFuncArrPotR extends UCSaveFuncArr {
  public UCSaveFuncArrPotR(UCPlotFuncArr plotUC) {
    super(plotUC);
  }
  public String getSaveFileName() {
    return QMSProject.getInstance().getJmPotOptR().getSaveFileName();
  }
  public void setSaveFileName(String name) {
    QMSProject.getInstance().getJmPotOptR().setSaveFileName(name);
  }
}
