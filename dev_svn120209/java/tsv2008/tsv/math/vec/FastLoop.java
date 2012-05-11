package math.vec;
import math.func.Func;

import javax.utilx.IdxAbsComparator;
import javax.utilx.log.Log;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:00:05
*/
public class FastLoop {
public static Log log = Log.getLog(FastLoop.class);
private final static int DUFF_SIZE = 8; // see p141 of C++, 3rd ed.
private final static boolean ASSERT_NULL = true;
private final static boolean ASSERT_COUNT = true;
private final static boolean ASSERT_LENGTH = true;

public static double addTest(int startIdx, int exclEnd, double[] to, double[] from) {
  double s = 0;
  for (int i = startIdx; i < exclEnd; i++) {
    to[i] += from[i];
  }
  return s;
}
public static void add(int startIdx, int exclEnd, double[] to, double[] a) {
  assert (to.length == a.length);
  if (ASSERT_LENGTH  &&  !(to.length == a.length)) {
    throw new IllegalArgumentException(log.error("!(to.length == a.length)"));
  }
  assert (startIdx >= 0);
  if (!(startIdx >= 0)) {
    throw new IllegalArgumentException(log.error("!(startIdx >= 0)"));
  }
  assert (exclEnd <= to.length);
  if (!(exclEnd <= to.length)) {
    throw new IllegalArgumentException(log.error("!(exclEnd <= to.length)"));
  }
  assert (startIdx <= exclEnd);
  if (!(startIdx <= exclEnd)) {
    throw new IllegalArgumentException(log.error("!(startIdx <= exclEnd)"));
  }
  int count = exclEnd - startIdx;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = startIdx;
  switch (count % DUFF_SIZE) {
    case 0:        to[i] += a[i];        i++;
    case 7:        to[i] += a[i];        i++;
    case 6:        to[i] += a[i];        i++;
    case 5:        to[i] += a[i];        i++;
    case 4:        to[i] += a[i];        i++;
    case 3:        to[i] += a[i];        i++;
    case 2:        to[i] += a[i];        i++;
    case 1:        to[i] += a[i];        i++;
  }
  for (; n > 0; n--) {
    to[i] += a[i];      i++; //1
    to[i] += a[i];      i++; //2
    to[i] += a[i];      i++; //3
    to[i] += a[i];      i++; //4
    to[i] += a[i];      i++; //5
    to[i] += a[i];      i++; //6
    to[i] += a[i];      i++; //7
    to[i] += a[i];      i++; //8
  }
}
public static double sum(double[] a) {
  return sum(0, a.length, a);
}
public static double mean(double[] a) {
  return sum(0, a.length, a) / a.length;
}
public static double sum(int startIdx, int exclEnd, double[] a) {
  assert (startIdx >= 0);
  if (!(startIdx >= 0)) {
    throw new IllegalArgumentException(log.error("!(startIdx >= 0)"));
  }
  assert (exclEnd <= a.length);
  if (!(exclEnd <= a.length)) {
    throw new IllegalArgumentException(log.error("!(exclEnd <= a.length)"));
  }
  assert (startIdx <= exclEnd);
  if (!(startIdx <= exclEnd)) {
    throw new IllegalArgumentException(log.error("!(startIdx <= exclEnd)"));
  }
  int count = exclEnd - startIdx;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  double s = 0;
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = startIdx;
  switch (count % DUFF_SIZE) {
    case 0:        s += a[i];        i++;
    case 7:        s += a[i];        i++;
    case 6:        s += a[i];        i++;
    case 5:        s += a[i];        i++;
    case 4:        s += a[i];        i++;
    case 3:        s += a[i];        i++;
    case 2:        s += a[i];        i++;
    case 1:        s += a[i];        i++;
  }
  for (; n > 0; n--) {
    s += a[i];      i++; //1
    s += a[i];      i++; //2
    s += a[i];      i++; //3
    s += a[i];      i++; //4
    s += a[i];      i++; //5
    s += a[i];      i++; //6
    s += a[i];      i++; //7
    s += a[i];      i++; //8
  }
  return s;
}

public static void copyEachTest(double[] from, double[] to) {
  for (int i = 0; i < from.length; i++) {
    to[i] = from[i];
  }
}
public static void copyEach(double[] a, double[] to) {
  assert(a.length == to.length);
  if (ASSERT_LENGTH  &&  !(a.length == to.length)) {
    throw new IllegalArgumentException(log.error("!(a.length == to.length)"));
  }
  int count = a.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  switch (count % DUFF_SIZE) {
    case 0:        to[i] = a[i];        i++;
    case 7:        to[i] = a[i];        i++;
    case 6:        to[i] = a[i];        i++;
    case 5:        to[i] = a[i];        i++;
    case 4:        to[i] = a[i];        i++;
    case 3:        to[i] = a[i];        i++;
    case 2:        to[i] = a[i];        i++;
    case 1:        to[i] = a[i];        i++;
  }
  for (; n > 0; n--) {
    to[i] = a[i];      i++;
    to[i] = a[i];      i++;
    to[i] = a[i];      i++;
    to[i] = a[i];      i++;
    to[i] = a[i];      i++;
    to[i] = a[i];      i++;
    to[i] = a[i];      i++;
    to[i] = a[i];      i++;
  }
}
public static double minTest(double[] a) {
  if (a == null  ||  a.length == 0) {
    throw new IllegalArgumentException(log.error("arr==null or arr.length==0"));
  }
  double res = a[0];
  int size = a.length;
  for (int i = 1; i < size; i++) {  // note from ONE
    if (res > a[i]) {
      res = a[i];
    }
  }
  return res;
}
public static double maxTest(double[] a) {
  if (a == null  ||  a.length == 0) {
    throw new IllegalArgumentException(log.error("arr==null or arr.length==0"));
  }
  double res = a[0];
  int size = a.length;
  for (int i = 1; i < size; i++) {  // note from ONE
    if (res < a[i]) {
      res = a[i];
    }
  }
  return res;
}
public static double min(double[] a) {
  if (a == null  ||  a.length == 0) {
    throw new IllegalArgumentException(log.error("arr==null or arr.length==0"));
  }
  int count = a.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  double res = a[0];
  switch (count % DUFF_SIZE) {
    case 0:        if (res > a[i]) {res = a[i];}        i++;
    case 7:        if (res > a[i]) {res = a[i];}        i++;
    case 6:        if (res > a[i]) {res = a[i];}        i++;
    case 5:        if (res > a[i]) {res = a[i];}        i++;
    case 4:        if (res > a[i]) {res = a[i];}        i++;
    case 3:        if (res > a[i]) {res = a[i];}        i++;
    case 2:        if (res > a[i]) {res = a[i];}        i++;
    case 1:        if (res > a[i]) {res = a[i];}        i++;
  }
  for (; n > 0; n--) {
    if (res > a[i]) {res = a[i];}      i++;
    if (res > a[i]) {res = a[i];}      i++;
    if (res > a[i]) {res = a[i];}      i++;
    if (res > a[i]) {res = a[i];}      i++;
    if (res > a[i]) {res = a[i];}      i++;
    if (res > a[i]) {res = a[i];}      i++;
    if (res > a[i]) {res = a[i];}      i++;
    if (res > a[i]) {res = a[i];}      i++;
  }
  return res;
}
public static int minIdxTest(double[] a) {
  if (a == null  ||  a.length == 0) {
    throw new IllegalArgumentException(log.error("arr==null or arr.length==0"));
  }
  double res = a[0];
  int idx = 0;
  int size = a.length;
  for (int i = 1; i < size; i++) {  // note from ONE
    if (res > a[i]) {
      res = a[i];
      idx = i;
    }
  }
  return idx;
}
public static int maxIdxTest(double[] a) {
  if (a == null  ||  a.length == 0) {
    throw new IllegalArgumentException(log.error("arr==null or arr.length==0"));
  }
  double res = a[0];
  int idx = 0;
  int size = a.length;
  for (int i = 1; i < size; i++) {  // note from ONE
    if (res < a[i]) {
      res = a[i];
      idx = i;
    }
  }
  return idx;
}
public static int minIdx(double[] a) {
  if (a == null  ||  a.length == 0) {
    throw new IllegalArgumentException(log.error("arr==null or arr.length==0"));
  }
  int count = a.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  int idx = 0;
  double res = a[0];
  switch (count % DUFF_SIZE) {
    case 0:        if (res > a[i]) {res = a[i];  idx = i;}        i++;
    case 7:        if (res > a[i]) {res = a[i];  idx = i;}        i++;
    case 6:        if (res > a[i]) {res = a[i];  idx = i;}        i++;
    case 5:        if (res > a[i]) {res = a[i];  idx = i;}        i++;
    case 4:        if (res > a[i]) {res = a[i];  idx = i;}        i++;
    case 3:        if (res > a[i]) {res = a[i];  idx = i;}        i++;
    case 2:        if (res > a[i]) {res = a[i];  idx = i;}        i++;
    case 1:        if (res > a[i]) {res = a[i];  idx = i;}        i++;
  }
  for (; n > 0; n--) {
    if (res > a[i]) {res = a[i];  idx = i;}      i++;
    if (res > a[i]) {res = a[i];  idx = i;}      i++;
    if (res > a[i]) {res = a[i];  idx = i;}      i++;
    if (res > a[i]) {res = a[i];  idx = i;}      i++;
    if (res > a[i]) {res = a[i];  idx = i;}      i++;
    if (res > a[i]) {res = a[i];  idx = i;}      i++;
    if (res > a[i]) {res = a[i];  idx = i;}      i++;
    if (res > a[i]) {res = a[i];  idx = i;}      i++;
  }
  return idx;
}
public static int maxIdx(double[] a) {
  if (a == null  ||  a.length == 0) {
    throw new IllegalArgumentException(log.error("arr==null or arr.length==0"));
  }
  int count = a.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  int idx = 0;
  double res = a[0];
  switch (count % DUFF_SIZE) {
    case 0:        if (res < a[i]) {res = a[i];  idx = i;}        i++;
    case 7:        if (res < a[i]) {res = a[i];  idx = i;}        i++;
    case 6:        if (res < a[i]) {res = a[i];  idx = i;}        i++;
    case 5:        if (res < a[i]) {res = a[i];  idx = i;}        i++;
    case 4:        if (res < a[i]) {res = a[i];  idx = i;}        i++;
    case 3:        if (res < a[i]) {res = a[i];  idx = i;}        i++;
    case 2:        if (res < a[i]) {res = a[i];  idx = i;}        i++;
    case 1:        if (res < a[i]) {res = a[i];  idx = i;}        i++;
  }
  for (; n > 0; n--) {
    if (res < a[i]) {res = a[i];  idx = i;}      i++;
    if (res < a[i]) {res = a[i];  idx = i;}      i++;
    if (res < a[i]) {res = a[i];  idx = i;}      i++;
    if (res < a[i]) {res = a[i];  idx = i;}      i++;
    if (res < a[i]) {res = a[i];  idx = i;}      i++;
    if (res < a[i]) {res = a[i];  idx = i;}      i++;
    if (res < a[i]) {res = a[i];  idx = i;}      i++;
    if (res < a[i]) {res = a[i];  idx = i;}      i++;
  }
  return idx;
}
public static double max(double[] a) {
  if (a == null  ||  a.length == 0) {
    throw new IllegalArgumentException(log.error("arr==null or arr.length==0"));
  }
  int count = a.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  double res = a[0];
  switch (count % DUFF_SIZE) {
    case 0:        if (res < a[i]) res = a[i];        i++;
    case 7:        if (res < a[i]) res = a[i];        i++;
    case 6:        if (res < a[i]) res = a[i];        i++;
    case 5:        if (res < a[i]) res = a[i];        i++;
    case 4:        if (res < a[i]) res = a[i];        i++;
    case 3:        if (res < a[i]) res = a[i];        i++;
    case 2:        if (res < a[i]) res = a[i];        i++;
    case 1:        if (res < a[i]) res = a[i];        i++;
  }
  for (; n > 0; n--) {
    if (res < a[i]) res = a[i];      i++;
    if (res < a[i]) res = a[i];      i++;
    if (res < a[i]) res = a[i];      i++;
    if (res < a[i]) res = a[i];      i++;
    if (res < a[i]) res = a[i];      i++;
    if (res < a[i]) res = a[i];      i++;
    if (res < a[i]) res = a[i];      i++;
    if (res < a[i]) res = a[i];      i++;
  }
  return res;
}

public static void multFirstTest(double[] v, double[] s) {
  for (int i = 0; i < v.length; i++) {
    v[i] *= s[i];
  }
}
public static void multFirstTest(double[] v, double[] s, double[] s2) {
  for (int i = 0; i < v.length; i++) {
    v[i] *= (s[i] * s2[i]);
  }
}
public static void multFirst(double[] v, double[] s) {
  assert(v.length == s.length);
  if (ASSERT_LENGTH  &&  !(v.length == s.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == s.length)"));
  }
  int count = v.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  switch (count % DUFF_SIZE) {
    case 0:        v[i] *= s[i];        i++;
    case 7:        v[i] *= s[i];        i++;
    case 6:        v[i] *= s[i];        i++;
    case 5:        v[i] *= s[i];        i++;
    case 4:        v[i] *= s[i];        i++;
    case 3:        v[i] *= s[i];        i++;
    case 2:        v[i] *= s[i];        i++;
    case 1:        v[i] *= s[i];        i++;
  }
  for (; n > 0; n--) {
    v[i] *= s[i];      i++;
    v[i] *= s[i];      i++;
    v[i] *= s[i];      i++;
    v[i] *= s[i];      i++;
    v[i] *= s[i];      i++;
    v[i] *= s[i];      i++;
    v[i] *= s[i];      i++;
    v[i] *= s[i];      i++;
  }
}

public static void multFirst(double[] v, double[] s, double[] s2) {
  assert(v.length == s.length);
  if (ASSERT_LENGTH  &&  !(v.length == s.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == s.length)"));
  }
  if (ASSERT_LENGTH  &&  !(v.length == s2.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == s.length)"));
  }
  int count = v.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  switch (count % DUFF_SIZE) {
    case 0:        v[i] *= (s[i] * s2[i]);        i++;
    case 7:        v[i] *= (s[i] * s2[i]);        i++;
    case 6:        v[i] *= (s[i] * s2[i]);        i++;
    case 5:        v[i] *= (s[i] * s2[i]);        i++;
    case 4:        v[i] *= (s[i] * s2[i]);        i++;
    case 3:        v[i] *= (s[i] * s2[i]);        i++;
    case 2:        v[i] *= (s[i] * s2[i]);        i++;
    case 1:        v[i] *= (s[i] * s2[i]);        i++;
  }
  for (; n > 0; n--) {
    v[i] *= (s[i] * s2[i]);      i++;
    v[i] *= (s[i] * s2[i]);      i++;
    v[i] *= (s[i] * s2[i]);      i++;
    v[i] *= (s[i] * s2[i]);      i++;
    v[i] *= (s[i] * s2[i]);      i++;
    v[i] *= (s[i] * s2[i]);      i++;
    v[i] *= (s[i] * s2[i]);      i++;
    v[i] *= (s[i] * s2[i]);      i++;
  }
}
public static void addMult(int startIdx, int exclEnd, double[] to, double c, double[] a) {
  assert(to.length == a.length);
  if (ASSERT_LENGTH  &&  !(to.length == a.length)) {
    throw new IllegalArgumentException(log.error("!(to.length == a.length)"));
  }
  assert (startIdx >= 0);
  if (!(startIdx >= 0)) {
    throw new IllegalArgumentException(log.error("!(startIdx >= 0)"));
  }
  assert (exclEnd <= to.length);
  if (!(exclEnd <= to.length)) {
    throw new IllegalArgumentException(log.error("!(exclEnd <= to.length)"));
  }
  assert (startIdx <= exclEnd);
  if (!(startIdx <= exclEnd)) {
    throw new IllegalArgumentException(log.error("!(startIdx <= exclEnd)"));
  }
  int count = exclEnd - startIdx;
//    int count = to.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
//    int i = 0;
  int i = startIdx;
  switch (count % DUFF_SIZE) {
    case 0:        to[i] += (c * a[i]);        i++;
    case 7:        to[i] += (c * a[i]);        i++;
    case 6:        to[i] += (c * a[i]);        i++;
    case 5:        to[i] += (c * a[i]);        i++;
    case 4:        to[i] += (c * a[i]);        i++;
    case 3:        to[i] += (c * a[i]);        i++;
    case 2:        to[i] += (c * a[i]);        i++;
    case 1:        to[i] += (c * a[i]);        i++;
  }
  for (; n > 0; n--) {
    to[i] += (c * a[i]);      i++;
    to[i] += (c * a[i]);      i++;
    to[i] += (c * a[i]);      i++;
    to[i] += (c * a[i]);      i++;
    to[i] += (c * a[i]);      i++;
    to[i] += (c * a[i]);      i++;
    to[i] += (c * a[i]);      i++;
    to[i] += (c * a[i]);      i++;
  }
}

public static void scaleTest(double[] v, double s) {
  for (int i = 0; i < v.length; i++) {
    v[i] *= s;
  }
}
public static void mult(double[] v, double s) {
  int count = v.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  switch (count % DUFF_SIZE) {
    case 0:        v[i] *= s;        i++;
    case 7:        v[i] *= s;        i++;
    case 6:        v[i] *= s;        i++;
    case 5:        v[i] *= s;        i++;
    case 4:        v[i] *= s;        i++;
    case 3:        v[i] *= s;        i++;
    case 2:        v[i] *= s;        i++;
    case 1:        v[i] *= s;        i++;
  }
  for (; n > 0; n--) {
    v[i] *= s;      i++;
    v[i] *= s;      i++;
    v[i] *= s;      i++;
    v[i] *= s;      i++;
    v[i] *= s;      i++;
    v[i] *= s;      i++;
    v[i] *= s;      i++;
    v[i] *= s;      i++;
  }
}

public static void add(double[] v, double s) {
  int count = v.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  switch (count % DUFF_SIZE) {
    case 0:        v[i] += s;        i++;
    case 7:        v[i] += s;        i++;
    case 6:        v[i] += s;        i++;
    case 5:        v[i] += s;        i++;
    case 4:        v[i] += s;        i++;
    case 3:        v[i] += s;        i++;
    case 2:        v[i] += s;        i++;
    case 1:        v[i] += s;        i++;
  }
  for (; n > 0; n--) {
    v[i] += s;      i++;
    v[i] += s;      i++;
    v[i] += s;      i++;
    v[i] += s;      i++;
    v[i] += s;      i++;
    v[i] += s;      i++;
    v[i] += s;      i++;
    v[i] += s;      i++;
  }
}

public static double dot(double[] v, double[]  v2) {
  return dot(0, v.length, v, v2);
}

public static double dotTest(double[] v, double[]  v2) {
  return dotTest(0, v.length, v, v2);
}

public static double dotTest(int startIdx, int exclEnd, double[] v, double[] v2) {
  double s = 0;
  for (int i = startIdx; i < exclEnd; i++) {
    s += v[i] * v2[i];
  }
  return s;
}
public static double dot(int startIdx, int exclEnd, double[] v, double[] v2) {
  assert (v.length == v2.length);
  if (ASSERT_LENGTH  &&  !(v.length == v2.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == v2.length)"));
  }
  assert (startIdx >= 0);
  if (!(startIdx >= 0)) {
    throw new IllegalArgumentException(log.error("!(startIdx >= 0)"));
  }
  assert (exclEnd <= v.length);
  if (!(exclEnd <= v.length)) {
    throw new IllegalArgumentException(log.error("!(exclEnd <= v.length)"));
  }
  assert (startIdx <= exclEnd);
  if (!(startIdx <= exclEnd)) {
    throw new IllegalArgumentException(log.error("!(startIdx <= exclEnd)"));
  }
  int count = exclEnd - startIdx;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  double s = 0;
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = startIdx;
  switch (count % DUFF_SIZE) {
    case 0:        s += v[i] * v2[i];        i++;
    case 7:        s += v[i] * v2[i];        i++;
    case 6:        s += v[i] * v2[i];        i++;
    case 5:        s += v[i] * v2[i];        i++;
    case 4:        s += v[i] * v2[i];        i++;
    case 3:        s += v[i] * v2[i];        i++;
    case 2:        s += v[i] * v2[i];        i++;
    case 1:        s += v[i] * v2[i];        i++;
  }
  for (; n > 0; n--) {
    s += v[i] * v2[i];      i++; //1
    s += v[i] * v2[i];      i++; //2
    s += v[i] * v2[i];      i++; //3
    s += v[i] * v2[i];      i++; //4
    s += v[i] * v2[i];      i++; //5
    s += v[i] * v2[i];      i++; //6
    s += v[i] * v2[i];      i++; //7
    s += v[i] * v2[i];      i++; //8
  }
  return s;
}

public static double dot(double[] v, double[] v2, double[] v3, double[] v4) {
  return dot(0, v.length, v, v2, v3, v4);
}

public static double dotTest(int startIdx, int exclEnd
  , double[] v, double[] v2, double[] v3, double[] v4) {
  double s = 0;
  for (int i = startIdx; i < exclEnd; i++) {
    s += v[i] * v2[i] * v3[i] * v4[i];
  }
  return s;
}
public static double dot(int startIdx, int exclEnd
  , double[] v, double[] v2, double[] v3, double[] v4) {
  assert (v.length == v2.length);
  if (ASSERT_LENGTH  &&  !(v.length == v2.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == v2.length)"));
  }
  assert (v.length == v3.length);
  if (ASSERT_LENGTH  &&  !(v.length == v3.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == v3.length)"));
  }
  assert (v.length == v4.length);
  if (ASSERT_LENGTH  &&  !(v.length == v4.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == v4.length)"));
  }
  assert (startIdx >= 0);
  if (!(startIdx >= 0)) {
    throw new IllegalArgumentException(log.error("!(startIdx >= 0)"));
  }
  assert (exclEnd <= v.length);
  if (!(exclEnd <= v.length)) {
    throw new IllegalArgumentException(log.error("!(exclEnd <= v.length)"));
  }
  assert (startIdx <= exclEnd);
  if (!(startIdx <= exclEnd)) {
    throw new IllegalArgumentException(log.error("!(startIdx <= exclEnd)"));
  }
  int count = exclEnd - startIdx;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  double s = 0;
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = startIdx;
  switch (count % DUFF_SIZE) {
    case 0:        s += v[i] * v2[i] * v3[i] * v4[i];        i++;
    case 7:        s += v[i] * v2[i] * v3[i] * v4[i];        i++;
    case 6:        s += v[i] * v2[i] * v3[i] * v4[i];        i++;
    case 5:        s += v[i] * v2[i] * v3[i] * v4[i];        i++;
    case 4:        s += v[i] * v2[i] * v3[i] * v4[i];        i++;
    case 3:        s += v[i] * v2[i] * v3[i] * v4[i];        i++;
    case 2:        s += v[i] * v2[i] * v3[i] * v4[i];        i++;
    case 1:        s += v[i] * v2[i] * v3[i] * v4[i];        i++;
  }
  for (; n > 0; n--) {
    s += v[i] * v2[i] * v3[i] * v4[i];      i++;
    s += v[i] * v2[i] * v3[i] * v4[i];      i++;
    s += v[i] * v2[i] * v3[i] * v4[i];      i++;
    s += v[i] * v2[i] * v3[i] * v4[i];      i++;
    s += v[i] * v2[i] * v3[i] * v4[i];      i++;
    s += v[i] * v2[i] * v3[i] * v4[i];      i++;
    s += v[i] * v2[i] * v3[i] * v4[i];      i++;
    s += v[i] * v2[i] * v3[i] * v4[i];      i++;
  }
  return s;
}
public static double dot(double[] v, double[] v2, double[] v3) {
  return dot(0, v.length, v, v2, v3);
}

public static double dotTest(int startIdx, int exclEnd
  , double[] v, double[] v2, double[]    v3) {
  double s = 0;
  for (int i = startIdx; i < exclEnd; i++) {
    s += v[i] * v2[i] * v3[i];
  }
  return s;
}
public static double dot(int startIdx, int exclEnd
  , double[] v, double[] v2, double[]    v3) {
  assert (v.length == v2.length);
  if (ASSERT_LENGTH  &&  !(v.length == v2.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == v2.length)"));
  }
  assert (v.length == v3.length);
  if (ASSERT_LENGTH  &&  !(v.length == v3.length)) {
    throw new IllegalArgumentException(log.error("!(v.length == v3.length)"));
  }
  assert (startIdx >= 0);
  if (!(startIdx >= 0)) {
    throw new IllegalArgumentException(log.error("!(startIdx >= 0)"));
  }
  assert (exclEnd <= v.length);
  if (!(exclEnd <= v.length)) {
    throw new IllegalArgumentException(log.error("!(exclEnd <= v.length)"));
  }
  assert (startIdx <= exclEnd);
  if (!(startIdx <= exclEnd)) {
    throw new IllegalArgumentException(log.error("!(startIdx <= exclEnd)"));
  }
  int count = exclEnd - startIdx;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  double s = 0;
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = startIdx;
  switch (count % DUFF_SIZE) {
    case 0:        s += v[i] * v2[i] * v3[i];        i++;
    case 7:        s += v[i] * v2[i] * v3[i];        i++;
    case 6:        s += v[i] * v2[i] * v3[i];        i++;
    case 5:        s += v[i] * v2[i] * v3[i];        i++;
    case 4:        s += v[i] * v2[i] * v3[i];        i++;
    case 3:        s += v[i] * v2[i] * v3[i];        i++;
    case 2:        s += v[i] * v2[i] * v3[i];        i++;
    case 1:        s += v[i] * v2[i] * v3[i];        i++;
  }
  for (; n > 0; n--) {
    s += v[i] * v2[i] * v3[i];      i++;
    s += v[i] * v2[i] * v3[i];      i++;
    s += v[i] * v2[i] * v3[i];      i++;
    s += v[i] * v2[i] * v3[i];      i++;
    s += v[i] * v2[i] * v3[i];      i++;
    s += v[i] * v2[i] * v3[i];      i++;
    s += v[i] * v2[i] * v3[i];      i++;
    s += v[i] * v2[i] * v3[i];      i++;
  }
  return s;
}

public static double polynomTest(double[] c, double x) {
  double s = 0;
  double pow_x = 1.;
  for (int i = 0; i < c.length; i++) {
    s += c[i] * pow_x;
    pow_x *= x;
  }
  return s;
}
public static double polynom(double[] c, double x) {
  int count = c.length;
  assert(count > 0);
  if (ASSERT_COUNT && !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  double s = 0;
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  double pow_x = 1.;
  switch (count % DUFF_SIZE) {
    case 0:        s += c[i] * pow_x;        i++;        pow_x *= x;
    case 7:        s += c[i] * pow_x;        i++;        pow_x *= x;
    case 6:        s += c[i] * pow_x;        i++;        pow_x *= x;
    case 5:        s += c[i] * pow_x;        i++;        pow_x *= x;
    case 4:        s += c[i] * pow_x;        i++;        pow_x *= x;
    case 3:        s += c[i] * pow_x;        i++;        pow_x *= x;
    case 2:        s += c[i] * pow_x;        i++;        pow_x *= x;
    case 1:        s += c[i] * pow_x;        i++;        pow_x *= x;
  }
  for (; n > 0; n--) {
    s += c[i] * pow_x;      i++;      pow_x *= x;
    s += c[i] * pow_x;      i++;      pow_x *= x;
    s += c[i] * pow_x;      i++;      pow_x *= x;
    s += c[i] * pow_x;      i++;      pow_x *= x;
    s += c[i] * pow_x;      i++;      pow_x *= x;
    s += c[i] * pow_x;      i++;      pow_x *= x;
    s += c[i] * pow_x;      i++;      pow_x *= x;
    s += c[i] * pow_x;      i++;      pow_x *= x;
  }
  return s;
}

public static void calcTest(double[] y, double[] x, Func f) {
  for (int i = 0; i < x.length; i++) {
    y[i] = f.calc(x[i]);
  }
}
public static void calc(double[] y, double[] x, Func f) {
  assert(y.length == x.length);
  if (ASSERT_LENGTH  &&  !(y.length == x.length)) {
    throw new IllegalArgumentException(log.error("!(y.length == x.length)"));
  }
  int count = x.length;
//    if (count <= 0)
//      return;
  if (ASSERT_COUNT  &&  !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  switch (count % DUFF_SIZE) {
    case 0:        y[i] = f.calc(x[i]);        i++;
    case 7:        y[i] = f.calc(x[i]);        i++;
    case 6:        y[i] = f.calc(x[i]);        i++;
    case 5:        y[i] = f.calc(x[i]);        i++;
    case 4:        y[i] = f.calc(x[i]);        i++;
    case 3:        y[i] = f.calc(x[i]);        i++;
    case 2:        y[i] = f.calc(x[i]);        i++;
    case 1:        y[i] = f.calc(x[i]);        i++;
  }
  for (; n > 0; n--) {
    y[i] = f.calc(x[i]);      i++;
    y[i] = f.calc(x[i]);      i++;
    y[i] = f.calc(x[i]);      i++;
    y[i] = f.calc(x[i]);      i++;
    y[i] = f.calc(x[i]);      i++;
    y[i] = f.calc(x[i]);      i++;
    y[i] = f.calc(x[i]);      i++;
    y[i] = f.calc(x[i]);      i++;
  }
}

public static double mulTest(int r, double[][] m, double[] v) {
  double res = 0;
  for (int i = 0; i < m.length; i++) {
    res += m[r][i] * v[i];
  }
  return res;
}
public static double mul(int r, double[][] m, double[] v) {
  assert(m != null);
  if (ASSERT_NULL  &&  !(m != null)) {
    throw new IllegalArgumentException(log.error("!(m != null)"));
  }
  assert(m[0] != null);
  if (ASSERT_NULL  &&  !(m[0] != null)) {
    throw new IllegalArgumentException(log.error("!(m[0] != null)"));
  }
  assert(v.length > 0);
  if (!(v.length > 0)) {
    throw new IllegalArgumentException(log.error("!(v.length > 0)"));
  }
  assert(m[0].length == v.length);
  if (ASSERT_LENGTH  &&  !(m[0].length == v.length)) {
    throw new IllegalArgumentException(log.error("!(m[0].length == v.length)"));
  }
  int count = v.length;
  if (ASSERT_COUNT  &&  !(count > 0)) {
    throw new IllegalArgumentException(log.error("!(count > 0)"));
  }
  int n = (count + DUFF_SIZE - 1) / DUFF_SIZE - 1;
  int i = 0;
  double res = 0.;
  switch (count % DUFF_SIZE) {
    case 0:        res += m[r][i] * v[i];        i++;
    case 7:        res += m[r][i] * v[i];        i++;
    case 6:        res += m[r][i] * v[i];        i++;
    case 5:        res += m[r][i] * v[i];        i++;
    case 4:        res += m[r][i] * v[i];        i++;
    case 3:        res += m[r][i] * v[i];        i++;
    case 2:        res += m[r][i] * v[i];        i++;
    case 1:        res += m[r][i] * v[i];        i++;
  }
  for (; n > 0; n--) {
    res += m[r][i] * v[i];      i++;
    res += m[r][i] * v[i];      i++;
    res += m[r][i] * v[i];      i++;
    res += m[r][i] * v[i];      i++;
    res += m[r][i] * v[i];      i++;
    res += m[r][i] * v[i];      i++;
    res += m[r][i] * v[i];      i++;
    res += m[r][i] * v[i];      i++;
  }
  return res;
}
public static void mul(double[] v, double[][] m, double[] v2) {
  assert(m != null);
  assert(m[0] != null);
  assert(v.length > 0);
  assert(m.length == v.length);
  for (int i = 0; i < v.length; i++) {
    v[i] = mul(i, m, v2);
  }
  return ;
}
}
