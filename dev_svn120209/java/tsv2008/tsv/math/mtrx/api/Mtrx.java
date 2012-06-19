package math.mtrx.api;

import Jama.Matrix;
import math.mtrx.MtrxToStr;
import math.mtrx.api.ejml.MtrxEjml;
import math.mtrx.api.jama.MtrxJama;
import math.vec.Vec;

/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:33:44
*/
public class Mtrx
//  extends MtrxJama {
//public Mtrx(MtrxJama from) {
//  super(from);
//}

  extends MtrxEjml {
public Mtrx(MtrxEjml from) {
  super(from);
}


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
public Mtrx inverse() {
  return new Mtrx(super.inverse());
}
public Mtrx copy() {
  return new Mtrx(super.copy());
}
public Mtrx plusEquals(Mtrx B) {
  super.plusEquals(B);
  return this;
}
public Mtrx minusEquals(Mtrx B) {
  super.minusEquals(B);
  return this;
}
public Mtrx timesEquals(double d) {
  super.timesEquals(d);
  return this;
}
public Mtrx times(Mtrx B) {
  return new Mtrx(super.times(B));
}
public Mtrx transpose () {
  return new Mtrx(super.transpose());
}
}
