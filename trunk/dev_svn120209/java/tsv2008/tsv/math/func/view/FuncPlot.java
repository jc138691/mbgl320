package math.func.view;
import math.func.FuncVec;

import java.awt.geom.Line2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 26/09/2008, Time: 15:25:27
 */
public class FuncPlot extends FuncVec {
  private Color color = Color.BLACK;
  public FuncPlot(FuncVec from) {
    super(from);
  }
  public double getMinX() {
    return getX().getFirst();
  }
  public double getMaxX() {
    return getX().getLast();
  }
  public void plot(Graphics2D g2, AffineTransform tr) {
    g2.setPaint(color);
    double[] x = getX().getArr();
    double[] y = getArr();

    g2.setPaint(color);
    for (int i = 1; i < size(); i++) {
      Point2D p = new Point2D.Float((float)x[i - 1], (float)y[i - 1]);
      tr.transform(p, p);
      Point2D p2 = new Point2D.Float((float)x[i], (float)y[i]);
      tr.transform(p2, p2);
      Line2D.Float L = new Line2D.Float(p, p2);
      g2.draw(L);
    }
  }
  public void plotTMP(Graphics2D g2, AffineTransform tr) {
    g2.setPaint(color);
    double[] x = getX().getArr();
    double[] y = getArr();

    g2.setPaint(color);
    for (int i = 0; i < size(); i++) {
      Point2D p = new Point2D.Float((float)x[i], (float)y[i]);
      tr.transform(p, p);
      Line2D.Float L = new Line2D.Float(p, p);
      g2.draw(L);
    }
  }
  public void setColor(Color color) {
    this.color = color;
  }
}

