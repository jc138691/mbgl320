package scatt.partial.wf;
import scatt.jm_2008.jm.laguerre.SavePlotPanel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 11:45:53
 */
public class SavePlotView extends SavePlotPanel {
  public SavePlotView(String title)    {
    super(title);
    assemble();
  }
  private void assemble() {
    endRow(saveBttn);   endRow(plotBttn);
  }
}
