package qm_station;
import qm_station.ui.MenuScattUI;
import javax.swingx.textx.TextView;
import javax.swing.*;
import javax.swingx.JTabbedPaneX;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 16:28:10
 */
public class QMSMainUI extends JPanel {
  private static QMSMainUI instance;
  private JTabbedPaneX ui;
  private JLabel status;

  private TextView outView;
  private int outIdx = -1;
  private JRadioButton outFocus;

  private MenuScattUI scattUI;
  private int scattIdx = -1;
  private JRadioButton scattFocus;

  private QMSMainUI() {
    init();
  }
  public static QMSMainUI getInstance() {
    if (instance == null) {
      instance = new QMSMainUI();
    }
    return instance;
  }

  synchronized public void repaint()
  {
    super.repaint();
  }

  synchronized public void validate()
  {
    super.validate();
  }

  public void rebuildAll() {
//    tabbedPane.removeAll();
//    importFileIdx = -1;
    rebuild();
  }
  private void init() {
    scattFocus = new JRadioButton();
    outFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
    group.add(scattFocus);
    group.add(outFocus);
    outFocus.setSelected(true);

    setLayout(new BorderLayout());

    ui = new JTabbedPaneX(JTabbedPane.TOP);
    add(ui, BorderLayout.CENTER);

    status = new JLabel(" ", SwingConstants.LEFT);
    add(status, BorderLayout.SOUTH);
  }
  public void setStatus(String msg) {
    status.setText(msg);
  }
  public void setStatusWithTime(String msg) {
    SimpleDateFormat date = new SimpleDateFormat();
    try {
      date.applyPattern("HH:mm:ss");
    }
    catch (IllegalArgumentException e) {
    }
    status.setText(date.format(new Date()) + " : " + msg);
  }
  private void rebuild() {
    QMS project = QMSProject.getInstance();
//    int maxCols = project.getTableDisplayOpt().getMaxNumCols();
    String maxNColsMssg = "";
//    if (maxCols > 0)
//      maxNColsMssg = " [first " + maxCols + " columns]";

    outIdx = ui.processView(outView, outIdx, outFocus.isSelected()
      , "Console", "Console output for QM-Station messages.");
    scattIdx = ui.processView(scattUI, scattIdx, scattFocus.isSelected()
      , "Scattering", "todo");

    JFrame frame = QMSFrame.getInstance();
    if (frame != null) {
      frame.validate();
      frame.repaint();
    }
    validate();
    repaint();
  }
//  public void showLoadTrainTableFirst() {
//    JOptionPane.showMessageDialog(this
//      , "First you need to import a TRAINING Z data set via MENU | FILE | IMPORT Z ...");
//  }

  public void showNotImplemented() {
    JOptionPane.showMessageDialog(this
      , "This option has not been implemented yet.");
  }
  public void setOutView(TextView view) {
    this.outView = view;
    outFocus.setSelected(true);
    rebuild();
  }
  public TextView getOutView() {
    return outView;
  }

  public MenuScattUI getScattUI()  {
    if (scattUI == null) {
      scattUI = new MenuScattUI();
    }
    setScattUI(scattUI); // NEED that to get focus
    return scattUI;
  }


  public JTable getSelectedResultsTable()
  {
//    int idx = tabbedPane.getSelectedIndex();
//    if (idx == -1)
//      return null;

//    if (zIdx == idx) { // NOTE!!!  Table's view may be truncated
//      QBench project = QBenchProject.getInstance();
//      TableDisplayOpt model = project.getTableDisplayOpt();
//      TableDisplayOpt tmp = new TableDisplayOpt();
//      tmp.setDigitsModel(model.getDigitsModel());
//      tmp.setMaxNumCols(-1); //turn off
//      tmp.setMaxNumRows(-1); //turn off
//      tmp.setCountOn(false);
//      return zTableView.makeTableView(tmp, this);
//    }
    return null;
  }

  public void resetAll()
  {
//    tabbedPane.removeAll();
//
//    importFileView = null;     importFileIdx = -1;

    rebuild();
  }
  public void setScattUI(MenuScattUI scattUI) {
    this.scattUI = scattUI;
    scattFocus.setSelected(true);
    rebuild();
  }
}


