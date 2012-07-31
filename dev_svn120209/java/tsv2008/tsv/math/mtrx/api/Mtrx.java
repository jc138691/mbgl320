package math.mtrx.api;

import math.mtrx.MtrxToStr;
import math.mtrx.api.ejml.MtrxEjml;
import math.mtrx.api.jama.MtrxJama;
import math.vec.Vec;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:33:44
*/
public class Mtrx
//  extends MtrxJama {
//  public Mtrx(MtrxJama from) {   super(from);}
  extends MtrxEjml {
  public Mtrx(MtrxEjml from) {  super(from);}
 //TODO: Remember to switch Jama/ejml in EigenSymm


//-- GENERIC
public Mtrx(int m, int n) {
  super(m, n);
}
public Mtrx(double[][] A) {
  super(A);
}
public Mtrx(Mtrx from) {
  super(from);
}
//public double[][] getArr2D() {
//  return super.getArr2D();
//}
//public double[] getArr1D() {
//  return super.getArr1D();
//}

public String toString() {
  return MtrxToStr.toCsv(this);
}

public String toTab(int digs) {
  return MtrxToStr.toTab(this, digs);
}

public String toTab() {
  return MtrxToStr.toTab(this);
}
public String toGnuplot() {
  return MtrxToStr.toTab(this);
}


public int getNumRows() {
  return super.getNumRows();
}

public int getNumCols() {
  return super.getNumCols();
}

public Mtrx inverse() {
  return new Mtrx(super.inverse());
}
public Mtrx copy() {
  return new Mtrx(super.copy());
}
public Mtrx addEquals(Mtrx B) {
  super.addEquals(B);
  return this;
}
public Mtrx subEquals(Mtrx B) {
  super.subEquals(B);
  return this;
}
public Mtrx multEquals(double d) {
  super.multEquals(d);
  return this;
}
public Mtrx mult(Mtrx B) {
  return new Mtrx(super.mult(B));
}
public Mtrx transpose () {
  return new Mtrx(super.transpose());
}


// TODO: this is SLOW; Replace by natives?
public Vec mult(Vec vec) {     // v = M * w
  Vec res = new Vec(getNumRows());
  for (int r = 0; r < getNumRows(); r++) {
    double sum = 0;
    for (int c = 0; c < getNumCols(); c++) {
      sum += (get(r, c) * vec.get(c));
    }
    res.set(r, sum);
  }
  return res;
}

public double[] getColCopy(int c) {
  double[] res = new double[getNumRows()];
  for (int r = 0; r < res.length; r++) {
    res[r] = get(r, c);
  }
  return res;
}
public double[] getRowCopy(int r) {
  double[] res = new double[getNumCols()];
  for (int c = 0; c < res.length; c++) {
    res[c] = get(r, c);
  }
  return res;
}
public void addSelf(int r, int c, double d) {
  set(r, c, get(r, c) + d);
}
public void multSelf(int r, int c, double d) {
  set(r, c, get(r, c) * d);
}
}
