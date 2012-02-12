package math.func.view;
import math.func.FuncVec;
import math.func.arr.FuncArr;

import javax.utilx.log.Log;
import javax.utilx.RandomSeed;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 26/09/2008, Time: 15:37:28
 */
public class FuncArrPlot extends FuncPlot {
  public static Log log = Log.getLog(FuncArrPlot.class);
  private ArrayList<FuncPlot> arr = new ArrayList<FuncPlot>();
  public FuncArrPlot(FuncVec from) {
    super(from);
  }
  public FuncArrPlot(FuncArr from) {
    super(from.getFunc(0));
    for (int i = 1; i < from.size(); i++) {
      add(from.getFunc(i));
    }
  }
  public void setColor(Color paint) {
    super.setColor(paint);
    if (arr.size() < 2) {
      return;
    }
    RandomSeed rand = RandomSeed.getInstance();
    int MAX = 200;
    for (FuncPlot fp : arr) {
      fp.setColor(new Color(rand.nextInt(MAX), rand.nextInt(MAX), rand.nextInt(MAX)));
    }
  }
  public void add(FuncPlot func) {
    arr.add(func);
  }
  public void add(FuncVec func) {
    arr.add(new FuncPlot(func));
  }
  public void plot(Graphics2D g2, AffineTransform tr) {
    super.plot(g2, tr);
    for (FuncPlot fp : arr) fp.plot(g2, tr);
  }
  public double getMinX() {
    double m = super.getMinX();        log.dbg("minX=", m);
    for (FuncPlot fp : arr) {
      m = Math.min(m, fp.getMinX());   log.dbg("new minX=", m);
    }
    return m;
  }
  public double getMaxX() {
    double m = super.getMaxX();        log.dbg("max X=", m);
    for (FuncPlot fp : arr) {
      m = Math.max(m, fp.getMaxX());   log.dbg("new max X=", m);
    }
    return m;
  }
  public double min() {
    double m = super.min();        log.dbg("min Y=", m);
    for (FuncPlot fp : arr) {
      m = Math.min(m, fp.min());   log.dbg("new min Y=", m);
    }
    return m;
  }
  public double max() {
    double m = super.max();       log.dbg("max Y=", m);
    for (FuncPlot fp : arr) {
      m = Math.max(m, fp.max());  log.dbg("new max Y=", m);
    }
    return m;
  }

}
