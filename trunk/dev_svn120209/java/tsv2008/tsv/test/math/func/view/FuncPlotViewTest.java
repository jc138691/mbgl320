package math.func.view;
import javax.swing.*;
import javax.swingx.dialog.ApplyDialogUI;
import javax.swingx.panelx.GridBagView;
import java.util.Random;
import java.awt.*;

import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.func.FuncVec;
import math.func.simple.FuncLinear;
import math.func.simple.FuncQuadr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 26/09/2008, Time: 16:01:10
 */
public class FuncPlotViewTest extends JFrame {
  public static void main(String[] args) {
    FuncPlotViewTest test = new FuncPlotViewTest();
    JPanel panel;
    //panel = test.showTestMinSize(10, 1);
    //panel = test.showTestTwoGraphs(100, 1);
    //panel = test.showTestThreeGraphs(100);
    panel = test.showAll();
    ApplyDialogUI dlg = new ApplyDialogUI(panel, test, true);
//    javax.iox.LOG.setTrace(true);
    dlg.setVisible(true);
    System.exit(0);
  }
  public FuncPlotViewTest() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  /**
   * Example for (4, 1.)
   * // 1234
   * // 1-34
   * // 12-4
   * // 1234
   */
  public JPanel showTestMinSize(int minSize, float a) {
    JPanel panel = new JPanel();
    Vec x = new StepGrid(a, 0., 100);
    FuncArrPlot plot = makePlot(0, a, x);
    FuncPlotView view = new FuncPlotView(plot);
    view.setMinSizeLen(minSize);
    panel.add(view);
    return panel;
  }
  public JPanel showTestTwoGraphs(int minSize, float a) {
    JPanel panel = new JPanel();
    Vec x = new StepGrid(-a, a, 100);
    FuncArrPlot plot = makePlot(0, a, x);
    plot.add(makePlot(0, -a, x));
    FuncPlotView view = new FuncPlotView(plot);
    view.setMinSizeLen(minSize);
    panel.add(view);
    return panel;
  }
  public JPanel showTestThreeGraphs(int minSize) {
    JPanel panel = new JPanel();
    Vec x = new StepGrid(-1., 1., 100);
    FuncArrPlot graph = make2(x);
    graph.add(make2(x));
    graph.add(make2(x));
    FuncPlotView view = new FuncPlotView(graph);
    view.setShowX(true);
    view.setShowY(true);
    view.setMinSizeLen(minSize);
    panel.add(view);
    return panel;
  }
  public JPanel showAll() {
    //JPanel panel = new GridBagView();
    GridBagView gridbag = new GridBagView();
    gridbag.getStart().fill = GridBagConstraints.NONE;
    gridbag.getEnd().fill = GridBagConstraints.NONE;
    gridbag.startRow(showTestMinSize(4, 1));
    gridbag.startRow(showTestMinSize(5, 1));
    gridbag.endRow(showTestMinSize(6, 1));
    gridbag.startRow(showTestMinSize(4, -1));
    gridbag.startRow(showTestMinSize(5, -1));
    gridbag.endRow(showTestMinSize(6, -1));
    gridbag.startRow(showTestMinSize(100, 1));
    gridbag.endRow(showTestMinSize(100, -1));
    gridbag.startRow(showTestTwoGraphs(100, -1));
    gridbag.endRow(showTestThreeGraphs(100));
    return gridbag;
  }
  public static FuncArrPlot makePlot(double a, double b, Vec x) {
    return new FuncArrPlot(new FuncVec(x, new FuncLinear(a, b)));
  }
  public static FuncArrPlot make2(Vec x) {
    Random rand = new Random();
    double a = 2 * rand.nextFloat() - 1;
    double b = 2 * rand.nextFloat() - 1;
    double c = 2 * rand.nextFloat() - 1;
    return new FuncArrPlot(new FuncVec(x, new FuncQuadr(a, b, c)));
  }
}
