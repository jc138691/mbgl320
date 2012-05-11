package math.vec;
import math.func.Func;

import javax.utilx.log.Log;
import java.util.Arrays;
import java.util.ArrayList;

/**
* Copyright KinGroup Team.
* User: jc138691, Date: 9/07/2008, Time: 14:19:58
*/
public class Vec {
public static Log log = Log.getLog(Vec.class);
protected double[] arr;
private int size = -1;

//  public Vec() {
//  }
public Vec(int size) {
  this.arr = new double[size] ;
  this.size = size;
}
public Vec(double[] from) {
  this.arr = from;
  this.size = from.length;
}
public Vec(Vec from) {
  this(from.arr);
}
public final int size() {
  return size;
}
public final void setSize(int newSize) {
  if (arr.length < newSize  ||  newSize < 0) {
    throw new IllegalArgumentException(log.error("Invalid newSize=" + newSize + ", oldSize=" + this.size));
  }
  this.size = newSize;
}
final public double getLast() {
  return arr[size - 1];
}
final public double getFirst() {
  return arr[0];
}
public final double get(int i) {
  return arr[i];
}
public final void set(int i, double v) {
  arr[i] = v;
}
public final double[] getArr() {
  return arr;
}
public final void setArr(double[] arr) {
  this.arr = arr;
  this.size = arr.length;
}

public Vec copy()     {
double[] res = new double[size()];
System.arraycopy(arr, 0, res, 0, size());
return new Vec(res);
}
public Vec append(Vec v2)     {
double[] res = new double[size() + v2.size()];
System.arraycopy(arr, 0, res, 0, size());
System.arraycopy(v2.arr, 0, res, size(), v2.size());
return new Vec(res);
}
public void calc(Vec x, Func f) {
  FastLoop.calc(arr, x.getArr(), f);
}
public void calc(Func f) {
  FastLoop.calc(arr, arr, f);
}
/*
http://www.gregdennis.com/drej/api/javax/vecmath/GVector.html#scale(double,%20javax.vecmath.GVector)
scale
public final void scale(double s)
  Scales this vector by the scale factor s.
  Parameters:
      s - the scalar value
 */
public final void add(double s) {
  FastLoop.add(arr, s);
}
public final void mult(double s) {
  FastLoop.mult(arr, s);
}
public final void multSelf(Vec s) {
  FastLoop.multFirst(arr, s.arr);
}
public final void multSelf(Vec s, Vec s2) {
  FastLoop.multFirst(arr, s.arr, s2.arr);
}
public final void addMultSafe(double c, Vec from) {  // [22Jun2011]  made addMult into  addMultSafe [as per addSafe]
//    FastLoop.addMult(arr, c, from.arr);            // OLD
  if (size() >= from.size()) {
    FastLoop.addMult(0, from.size(), arr, c, from.arr);
    return;
  }
  Vec tmp = from.copy();
  FastLoop.addMult(0, size(), tmp.arr, c, arr);
  setArr(tmp.arr);
}
public double min() {
  return FastLoop.min(arr);
}
public double max() {
  return FastLoop.max(arr);
}
public int minIdx() {
  return FastLoop.minIdx(arr);
}
public int maxIdx() {
  return FastLoop.maxIdx(arr);
}

public static String toString(double[] a, int size) {
  if (a == null) {
    throw new IllegalArgumentException(log.error("a==null"));
  }
  if (a.length < size) {
    throw new IllegalArgumentException(log.error("a.length < size=" + size));
  }
//    int L = Math.min(a.length, size);
  StringBuffer buff = new StringBuffer();
  buff.append("Vec["+size+"]=");
  buff.append(VecToString.toString(a, size));
  buff.append("}");
  return buff.toString();
}
public String toString(int size) {
  return toString(arr, size);
}
public String toCSV() {
  return VecToString.toString(this);
}
public String toString() {
  return toString(arr.length);
}
public static void copyEach(double[] from, double[] to) {
  FastLoop.copyEach(from, to);
}
public double dot(Vec v2) {
  return FastLoop.dot(arr, v2.arr);
}
public double dot(double[] v2) {
  return FastLoop.dot(arr, v2);
}

/* http://homepages.inf.ed.ac.uk/rmcnally/specksim/doc/javax/vecmath/GVector.html#mul(javax.vecmath.GMatrix,%20javax.vecmath.GVector)
public final void mul(GMatrix m1,
                    GVector v1)
  Multiplies matrix m1 times Vector v1 and places the result into this vector (this = m1*v1).
  Parameters:
      m1 - The matrix in the multiplication
      v1 - The vector that is multiplied
 */
public void mult(double[][] m, Vec v2) {
  int nRows = m.length;
  int nCols = m[0].length;
  if (size() != nRows) {
    throw new IllegalArgumentException(log.error("V1=M*V2 : V1.size()="+ size() + ", M.nRows=" + nRows));
  }
  if (v2.size() != nCols) {
    throw new IllegalArgumentException(log.error("V1=M*V2 : V2.size()="+ v2.size() + ", M.nCols=" + nCols));
  }
  FastLoop.mul(arr, m, v2.getArr());
}

public void addSafe(Vec from) {
  if (size() >= from.size()) {
    FastLoop.add(0, from.size(), arr, from.arr);
    return;
  }
  Vec tmp = from.copy();
  FastLoop.add(0, size(), tmp.arr, arr);
  setArr(tmp.arr);
}

public static double calcMedianFromSorted(double[] e)
{
  if (e == null)
    throw new IllegalArgumentException(log.error("calcMedianFromSorted(null)"));
  if (e.length == 0)
    throw new IllegalArgumentException(log.error("calcMedianFromSorted(e.length == 0)"));
  int n = e.length;
  if (n == 1)
    return e[0];
  if (n % 2 == 0) {  //even
    int n2 = (n - 1) / 2;
    return 0.5 * (e[n2] + e[n2 + 1]);
  }
  else {  // odd
    return e[(n-1)/2];
  }
}
public static double mean(double[] e) {
  if (e == null)
    throw new IllegalArgumentException(log.error("Vec.mean(null)"));
  if (e.length == 0)
    throw new IllegalArgumentException(log.error("Vec.mean(e.length == 0)"));
  return FastLoop.mean(e);
}
public static double medianSLOW(double[] e)  {
  double[] tmp = Vec.copy(e);
  Arrays.sort(tmp);
  return calcMedianFromSorted(tmp);
}
public static double medianNoCopySLOW(double[] e)  {
  Arrays.sort(e);
  return calcMedianFromSorted(e);
}
public static double[] copy(double[] from)
{
  double[] res = new double[from.length];
  System.arraycopy(from, 0, res, 0, from.length);
  return res;
}
public static double[] convert(ArrayList<Integer> arr) {
  double[] res = new double[arr.size()];
  for (int i = 0; i < res.length; i++) {
    res[i] = arr.get(i);
  }
  return res;
}
public static double[] convert(int[] arr) {
  double[] res = new double[arr.length];
  for (int i = 0; i < res.length; i++) {
    res[i] = arr[i];
  }
  return res;
}
public void sort() {
Arrays.sort(arr);
}
}
