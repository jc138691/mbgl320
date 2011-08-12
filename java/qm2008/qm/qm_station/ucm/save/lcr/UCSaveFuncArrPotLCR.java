package qm_station.ucm.save.lcr;
import qm_station.QMSProject;
import qm_station.ucm.save.UCSaveFuncArr;
import qm_station.ucm.plot.UCPlotFuncArr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 13:54:34
 */
public class UCSaveFuncArrPotLCR extends UCSaveFuncArr {
  public UCSaveFuncArrPotLCR(UCPlotFuncArr plotUC) {
    super(plotUC);
  }
  public String getSaveFileName() {
    return QMSProject.getInstance().getJmPotOptLcr().getSaveFileName();
  }
  public void setSaveFileName(String name) {
    QMSProject.getInstance().getJmPotOptLcr().setSaveFileName(name);
  }
}