package scatt.jm_2008.jm.laguerre;
import project.ucm.UCController;
import project.ucm.AdapterUCCToAL;
import project.ProjectFrame;

import javax.swingx.panelx.GridBagView;
import javax.swingx.buttonx.ButtonUtils;
import javax.utilx.log.Log;
import javax.swing.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 11:40:59
 */
public class SavePlotPanel extends GridBagView {
  public static Log log = Log.getLog(SavePlotPanel.class);
  protected JButton plotBttn;
  protected JButton saveBttn;

  public SavePlotPanel(String title)    {
    super(title);
    init();
  }
  private void init()   {
    ImageIcon icon = ProjectFrame.loadSaveAsFileIcon16();
    if (icon == null) {
      saveBttn = ButtonUtils.makeFlat("S");
    }
    else {
      saveBttn = ButtonUtils.makeFlat(new JButton(icon));
    }
    saveBttn.setToolTipText("Save selected data source into a file ...");

    icon = ProjectFrame.loadEditFindIcon16();
    if (icon == null) {
      plotBttn = ButtonUtils.makeFlat("P");
    }
    else {
      plotBttn = ButtonUtils.makeFlat(new JButton(icon));
    }
    plotBttn.setToolTipText("Plot selected data source ...");
  }
  public void runSave(UCController uc) {
    saveBttn.addActionListener(new AdapterUCCToAL(uc));
  }
  public void runPlot(UCController uc) {
    plotBttn.addActionListener(new AdapterUCCToAL(uc));
  }
}
