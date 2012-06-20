package math.mtrx.api;

import math.mtrx.api.ejml.MtrxEjml;
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
}
