package qm_station.ui;
import javax.swingx.panelx.GridBagView;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.text_fieldx.FloatField;
import javax.swing.*;
import javax.utilx.log.Log;

import math.vec.grid.StepGridOpt;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/09/2008, Time: 17:13:21
 */
public class StepGridView extends GridBagView {
  public static Log log = Log.getLog(StepGridView.class);
  private static final int FLOAT_SIZE = 3;
  private static final int INT_SIZE = 3;

  private JLabel firstLbl;
  protected FloatField first;

  private JLabel lastLbl;
  protected FloatField last;

  private JLabel nPtsLbl;
  private IntField nPts;

  public StepGridView(StepGridOpt model)    {
    super("Step Grid");
    init();
    loadFrom(model);
    assemble();
  }
  public StepGridView(StepGridOpt model, String title)    {
    super(title);
    init();
    loadFrom(model);
    assemble();
  }
  private void assemble() {
    startRow(firstLbl);    endRow(first);
    startRow(lastLbl);     endRow(last);
    startRow(nPtsLbl);     endRow(nPts);
  }
  private void init()   {
    firstLbl = new JLabel("First");
    first = new FloatField(FLOAT_SIZE, -1000, 1000);
    first.setToolTipText("first point");

    lastLbl = new JLabel("Last");
    last = new FloatField(FLOAT_SIZE, -1000, 1000);
    last.setToolTipText("last point");

    nPtsLbl = new JLabel("Points");
    nPts = new IntField(INT_SIZE, 2, 10000);
    nPts.setToolTipText("Number of points in this grid");
  }
  public void loadTo(StepGridOpt model) {
    model.setFirst(first.getInput());
    model.setLast(last.getInput());
    model.setNumPoints(nPts.getInput());
  }
  public void loadFrom(StepGridOpt model) {
    first.setValue(model.getFirst());
    last.setValue(model.getLast());
    nPts.setValue(model.getNumPoints());
  }


}
