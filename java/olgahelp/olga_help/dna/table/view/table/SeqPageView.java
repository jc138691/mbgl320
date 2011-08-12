package dna.table.view.table;

import dna.table.view.table.format.SeqArrUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 22/09/2009, 2:33:48 PM
 */
public class SeqPageView extends JPanel {
  private JTable mainTable;
  private JTable colHeaders;
  private JScrollPane colHeadersSP;
  private JScrollPane mainTableSP;
//  private JTable snpsView;
//  private JScrollPane snpsViewSP;

  public SeqPageView(JTable mainTable) {
    init();
    this.mainTable = mainTable;
    colHeaders = makeColHeaders(mainTable);
    mainTableSP = new JScrollPane(mainTable);
    mainTable.setTableHeader(null);

    mainTableSP.setColumnHeaderView(colHeaders);
    mainTableSP.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListenerH());
//    mainTableSP.setColumnHeaderView(null);
//    mainTableSP.setColumnHeader(null);
    assemble();
  }

  private JTable makeColHeaders(final JTable tableModel) {
    AbstractTableModel model = new AbstractTableModel() {  //http://www.codeguru.com/java/articles/128.shtml
      public int getColumnCount() { return tableModel.getColumnCount(); }
      public int getRowCount() { return 1; }
      public String getColumnName(int col) {
        return "";
      }
      public Object getValueAt(int row, int col) {
        return tableModel.getColumnName(col);
      }
    };
    return DnaTableViewFactory.makeDefaultTable(model);
  }

  private void assemble() {
    SeqArrUtils.copyWidth(colHeaders.getColumnModel(), mainTable.getColumnModel());

    JPanel p = new JPanel(new BorderLayout());

    colHeaders.setTableHeader(null);
    colHeadersSP = new JScrollPane(colHeaders
      , ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    p.add(colHeadersSP, BorderLayout.NORTH);

    add(p, BorderLayout.NORTH);
    add(mainTableSP, BorderLayout.CENTER);
  }

  private void init() {
    setLayout(new BorderLayout());
  }

  public JScrollPane getScrollPane() {
    return mainTableSP;
  }

  public void setViewPos(Point pos) {
    mainTableSP.getViewport().setViewPosition(pos);
    Point hPoint = new Point(pos.x, 0);
    colHeadersSP.getViewport().setViewPosition(hPoint);
  }
  public void setHeadersPos(Point pos) {
//    mainTableSP.getViewport().setViewPosition(pos);
    Point hPoint = new Point(pos.x, 0);
    colHeadersSP.getViewport().setViewPosition(hPoint);
  }


  // from http://www.exampledepot.com/egs/javax.swing/scroll_SpEvt.html
  class AdjustmentListenerH implements AdjustmentListener {
    // This method is called whenever the value of a scrollbar is changed,
    // either by the user or programmatically.
    public void adjustmentValueChanged(AdjustmentEvent evt) {
      Adjustable source = evt.getAdjustable();

      // getValueIsAdjusting() returns true if the user is currently
      // dragging the scrollbar's knob and has not picked a final value
//      if (evt.getValueIsAdjusting()) {
//        // The user is dragging the knob
//        return;
//      }

      // Determine which scrollbar fired the event
      int orient = source.getOrientation();
      if (orient == Adjustable.HORIZONTAL) {
        Point pos = mainTableSP.getViewport().getViewPosition();
        setHeadersPos(pos);
        // Event from horizontal scrollbar
      } else {
        // Event from vertical scrollbar
      }
    }
  }

}
