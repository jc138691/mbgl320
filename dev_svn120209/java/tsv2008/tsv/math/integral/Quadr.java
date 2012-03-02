package math.integral;

import math.vec.Vec;
import math.vec.FastLoop;
import math.func.FuncVec;
import math.Mathx;

import javax.utilx.log.Log;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 16:27:21
*/
public class Quadr extends FuncVec { //Numerical Quadrature
public static Log log = Log.getLog(Quadr.class);
static private int START_IDX = 0;
public Quadr(Vec x) {
  super(x);
}
public Quadr(Quadr from) {
  super(from);
}
public double calcInt(Vec f, Vec f2) {
  return calc(f, f2);
}
public double calcInt(Vec f, Vec f2, Vec f3) {
  return calc(f, f2, f3);
}
final public double calc(Vec f) {
  int endIdx = Math.min(size(), f.size());
  return FastLoop.dot(START_IDX, endIdx, getArr(), f.getArr());
}
final public double calc(Vec f, Vec f2) {
  int endIdx = Mathx.min(size(), f.size(), f2.size());
  return FastLoop.dot(START_IDX, endIdx, getArr(), f.getArr(), f2.getArr());
}
final public double calc(Vec f, Vec f2, Vec f3) {
  int endIdx = Mathx.min(size(), f.size(), f2.size(), f3.size());
  return FastLoop.dot(START_IDX, endIdx, getArr(), f.getArr(), f2.getArr(), f3.getArr());
}
}
