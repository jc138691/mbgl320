package math.func.view;
import math.func.FuncVec;
import math.vec.grid.StepGrid;
import math.vec.Vec;

import javax.swing.*;
import javax.utilx.log.Log;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.NoninvertibleTransformException;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 26/09/2008, Time: 15:00:40
 */
public class FuncPlotView extends JPanel {
  public static Log log = Log.getLog(FuncPlotView.class);
  private boolean showFrame = true;
  private boolean showX = false;
  private boolean showY = false;
  private int insertSize = 2;
  private int minSizeLen = 20;
  private FuncPlot func;
  private FuncPlot axisX;
  private FuncPlot axisY;
  public void setShowFrame(boolean b) {
    showFrame = b;
  }
  public void setShowX(boolean b) {
    showX = b;
  }
  public void setShowY(boolean b) {
    showY = b;
  }
  public void setMinSizeLen(int i) {
    minSizeLen = i;
    init();
  }
  public Dimension getMinimumSize() {
    return new Dimension(minSizeLen + 2 * insertSize
      , minSizeLen + 2 * insertSize);
  }
  public FuncPlotView(FuncPlot func) {
    super();
    init();
    this.func = func;
    func.setColor(Color.BLUE);
  }
  private void init() {
    //setBorder(BorderFactory.createLoweredBevelBorder());
    setMinimumSize(getMinimumSize());
    setPreferredSize(getMinimumSize());
    setBackground(Color.WHITE);    
  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (func == null)
      return;
    Graphics2D g2 = (Graphics2D) g;
    g2.setBackground(Color.WHITE);
    Rectangle frame = drawFrame(g2);
    AffineTransform tr = scale(func, frame);
    if (axisX != null)
      axisX.plot(g2, tr);
    if (axisY != null)
      axisY.plot(g2, tr);
    func.plot(g2, tr);
  }
  private AffineTransform scale(FuncPlot graph, Rectangle rec) {
    AffineTransform tr = new AffineTransform();
    double minX = graph.getMinX();     log.dbg("minX=", minX);
    double maxX = graph.getMaxX();     log.dbg("maxX=", maxX);
    if (maxX == minX)
      return tr;
    double sx = (float) rec.width / (maxX - minX);
    double minY = graph.min();     log.dbg("minY=", minY);
    double maxY = graph.max();     log.dbg("maxY=", maxY);
    if (maxY == minY)
      return null;
    double sy = (float) rec.height / (maxY - minY);
    tr.scale(sx, -sy);
    Point2D modelTL = new Point2D.Float((float)minX, (float)maxY);// TOP-LEFT corner
    Point2D recViewTL = new Point2D.Float(rec.x, rec.y);
    Point2D recModelTL;
    try {
      recModelTL = tr.inverseTransform(recViewTL, null);
    } catch (NoninvertibleTransformException e) {
      return tr;
    }

    // recModelTL must be the same as modelTL (TOP-LEFT corner)
    tr.translate(recModelTL.getX() - modelTL.getX()
      , recModelTL.getY() - modelTL.getY());
    axisX = null;
    Vec zeros = new StepGrid(0., 0., 2);
    if (showX)
      axisX = new FuncPlot(new FuncVec(new StepGrid(minX, maxX, 2), zeros));
    axisY = null;
    if (showY) {
      axisY = new FuncPlot(new FuncVec(zeros, new StepGrid(minY, maxY, 2)));
    }
    return tr;
  }
  private Rectangle drawFrame(Graphics2D g2) {
    Rectangle rec = getBounds();     //LOG.trace(this, "getBounds=" + rec);
    rec.grow(-insertSize, -insertSize);   //LOG.trace(this, "rec - INSERT =" + rec);
    Rectangle frame = new Rectangle(insertSize, insertSize
      , rec.width - 1, rec.height - 1); // NOTE! without -1 right hand border is missing
    //LOG.trace(this, "frame=" + frame);
    if (showFrame)
      g2.draw(frame);
    Rectangle test = new Rectangle(frame);
    test.grow(2, 2);    //LOG.trace(this, "test=" + test);
    if (showFrame)
      g2.draw(test);
    return frame;
  }

}
