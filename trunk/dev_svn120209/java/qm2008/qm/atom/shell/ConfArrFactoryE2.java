package atom.shell;

import math.func.FuncVec;
import math.func.arr.FuncArr;

import javax.utilx.log.Log;

/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:17:36
*/
public class ConfArrFactoryE2 {
public static Log log = Log.getLog(ConfArrFactoryE2.class);

public static ConfArr makeTwoElec(Ls LS, int N, int L, FuncArr fromArr) {
  ConfArr res = new ConfArr();
  for (int n = 0; n < N; n++) {
    Shell sh = new ShellQ2(n, fromArr.get(n), L, LS);
    log.dbg("q=2, sh=", sh);
    if (sh.isValid()) {
      Conf fc = new ShPair(sh);
      log.dbg("fc(n=n2)=", fc);
      res.add(fc);
    }
    sh = new Shell(n, fromArr.get(n), L);
    log.dbg("sh=", sh);
    for (int n2 = n + 1; n2 < N; n2++) {
      Shell sh2 = new Shell(n2, fromArr.get(n2), L);
      log.dbg("sh2=", sh2);
      Conf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
      res.add(fc);
    }
  }
  log.dbg("fromArr=\n", fromArr);
  log.dbg("from=\n", res);
  return res;
}

public static ConfArr makePoetConfE1(FuncArr arr) {  //[14Apr2011] converted from makeSModelE2() for testing/debuging
  int L = 0;
  ConfArr res = new ConfArr();
  int minN = arr.size();
  FuncArr minA = arr;
  for (int n = 0; n < minN; n++) { // n=0,...,(N-1)
    Shell sh = new Shell(n, minA.get(n), L);      log.dbg("sh=", sh);
    Conf fc = new Conf(sh);                       log.dbg("fc=", fc);
    res.add(fc);
  }
  log.dbg("from arr=\n", arr);
  log.dbg("res=\n", res);
  return res;
}

public static ConfArr makeSModelE2(Ls LS, FuncArr arr, FuncArr arr2) {
  int L = 0;
  ConfArr res = new ConfArr();
  int minN = arr.size();
  int maxN = arr2.size();
  FuncArr minA = arr;
  FuncArr maxA = arr2;
  return makeSModelE2(LS, minA, minN, maxA, maxN);
}
public static ConfArr makeSModelMmE2(Ls LS, FuncArr arr, FuncArr arr2) {
int L = 0;
  ConfArr res = new ConfArr();
  int minN = arr.size();
  int maxN = arr2.size();
  FuncArr minA = arr;
  FuncArr maxA = arr2;
  return makePoetConfMmE2(LS, minA, minN, maxA, maxN);
}

public static ConfArr makeSModelE2(Ls LS, FuncArr arr, int minN) {
  int maxN = arr.size();
  FuncArr minA = arr;
  FuncArr maxA = arr;
  return makeSModelE2(LS, minA, minN, maxA, maxN);
}
public static ConfArr makeSModelMmE2(Ls LS, FuncArr arr, int minN) {
  int maxN = arr.size();
  FuncArr minA = arr;
  FuncArr maxA = arr;
  return makeSModelE2(LS, minA, minN, maxA, maxN);
}

public static ConfArr makeSModelE2(Ls LS, FuncArr minA, int minN, FuncArr maxA, int maxN) {
  int L = 0;
  ConfArr res = new ConfArr();
  res.setLs(LS);
  if (minN > maxN) {
    throw new IllegalArgumentException(log.error("arr2.size() < arr.size()!!!!!!!!"));
  }
  for (int n = 0; n < minN; n++) { // n=0,...,(N-1)
    Shell sh = new ShellQ2(n, minA.get(n), L, LS); log.dbg("q=2 at sh=", sh);
    if (sh.isValid()) {
      Conf fc = new ShPair(sh);                  log.dbg("fc(n=n2)=", fc);
      res.add(fc);
    }
    sh = new Shell(n, minA.get(n), L);
    log.dbg("sh=", sh);
    for (int n2 = n + 1; n2 < minN; n2++) {
      Shell sh2 = new Shell(n2, minA.get(n2), L);
      log.dbg("sh2=", sh2);
      Conf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
      res.add(fc);
    }

    // PLUS extra with (n>=N-1)
    for (int n2 = minN; n2 < maxN; n2++) {
      Shell sh2 = new Shell(n2, maxA.get(n2), L);
      log.dbg("sh2=", sh2);
      Conf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
//        Conf fc = new ShPair(sh).addShell(sh2, LS);   log.dbg("fc=", fc);
      res.add(fc);
    }
  }
  log.dbg("from minA=\n", minA);
  log.dbg("from maxA=\n", maxA);
  log.dbg("res=\n", res);
  return res;
}

public static ConfArr makePoetConfMmE2_DBG(Ls LS, FuncArr minA, int minN, FuncArr maxA, int maxN) {
  int L = 0;
  ConfArr res = new ConfArr();
  res.setLs(LS);
  if (minN > maxN) {
    throw new IllegalArgumentException(log.error("arr2.size() < arr.size()!!!!!!!!"));
  }
  for (int n = 0; n < minN; n++) { // n=0,...,(N-1)
    Shell sh = new ShellQ2(n, minA.get(n), L, LS); log.dbg("q=2 at sh=", sh);
    if (sh.isValid()) {
      Conf fc = new ShPair(sh);                  log.dbg("fc(n=n2)=", fc);
      res.add(fc);
    }
    sh = new Shell(n, minA.get(n), L);
    log.dbg("sh=", sh);
    for (int n2 = 0; n2 < minN; n2++) { // FOR MM-model
//    for (int n2 = 0; n2 < n; n2++) { // FOR MM-model
      if (n2 == n)
        continue;
      Shell sh2 = new Shell(n2, minA.get(n2), L);
      log.dbg("sh2=", sh2);
      Conf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
      res.add(fc);
    }

    // PLUS extra with (n>=N-1)
    for (int n2 = minN; n2 < maxN; n2++) {
      Shell sh2 = new Shell(n2, maxA.get(n2), L);
      log.dbg("sh2=", sh2);
      Conf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
//        Conf fc = new ShPair(sh).addShell(sh2, LS);   log.dbg("fc=", fc);
      res.add(fc);
    }
  }
  log.dbg("from minA=\n", minA);
  log.dbg("from maxA=\n", maxA);
  log.dbg("res=\n", res);
  return res;
}

public static ConfArr makePoetConfMmE2(Ls LS, FuncArr minA, int minN, FuncArr maxA, int maxN) {
  int L = 0;
  ConfArr res = new ConfArr();
  res.setLs(LS);
  if (minN > maxN) {
    throw new IllegalArgumentException(log.error("arr2.size() < arr.size()!!!!!!!!"));
  }
  Shell sh, sh2;
  for (int n = 0; n < minN; n++) { // n=0,...,(N-1)
    sh = new ShellQ2(n, minA.get(n), L, LS); log.dbg("q=2 at sh=", sh);
    if (sh.isValid()) {
      Conf fc = new ShPair(sh);                  log.dbg("fc(n=n2)=", fc);
      res.add(fc);
    }
    sh = new Shell(n, minA.get(n), L);
    log.dbg("sh=", sh);
//    for (int n2 = n+1; n2 < minN; n2++) { // FOR MM-model
      for (int n2 = 0; n2 < minN; n2++) { // FOR MM-model
//    for (int n2 = 0; n2 < n; n2++) { // FOR MM-model
      if (n2 == n)
        continue;
      if (n2 < n) {
        sh2 = new Shell(n2, minA.get(n2), L);
      } else {
        sh2 = new Shell(n2, maxA.get(n2), L);
      }

      log.dbg("sh2=", sh2);
      Conf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
      res.add(fc);
    }

    // PLUS extra with (n>=N-1)
    for (int n2 = minN; n2 < maxN; n2++) {
      sh2 = new Shell(n2, maxA.get(n2), L);
      log.dbg("sh2=", sh2);
      Conf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
//        Conf fc = new ShPair(sh).addShell(sh2, LS);   log.dbg("fc=", fc);
      res.add(fc);
    }
  }
  log.dbg("from minA=\n", minA);
  log.dbg("from maxA=\n", maxA);
  log.dbg("res=\n", res);
  return res;
}

// this should be deleted if the jmV2 idea is no good.
public static ConfArr makeTwoElec_forJmV2(Ls LS, int L, FuncArr arr, int n2, FuncVec func2) {
  ConfArr res = new ConfArr();
  for (int n = 0; n < arr.size(); n++) { // n=0,...,(N-1)
    if (n == n2) { // why adding a different function with the same n?
      String mssg = "adding n=n2=" + n2;
      throw new IllegalArgumentException(log.error(mssg));
    }
    Shell sh = new Shell(n, arr.get(n), L);
    log.dbg("sh=", sh);
    Shell sh2 = new Shell(n2, func2, L);
    log.dbg("sh2=", sh2);
    Conf fc = new ShPair(sh, sh2, LS);
    log.dbg("fc=", fc);
//      Conf fc = new ShPair(sh).addShell(sh2, LS);   log.dbg("fc=", fc);
    res.add(fc);
  }
  log.dbg("from arr=\n", arr);
  log.dbg("from func2=\n", func2);
  log.dbg("res=\n", res);
  return res;
}

public static ConfArr makeTwoElecSameN(Ls LS, int N, FuncArr from) {
  ConfArr res = new ConfArr();
  for (int n = 0; n < N; n++) {
    Shell sh = new Shell(n, from.get(n), 2, 0, LS);
    Conf c = new ShPair(sh);
    res.add(c);
  }
  log.dbg("from=\n", from);
  log.dbg("res=\n", res);
  return res;
}

public static ConfArr makeOneElec(int N, int L, FuncArr from) {
  log.dbg("makeOneElec(N=", N);
  log.dbg("L=", L);
  log.dbg("from basisN=", from);
  ConfArr res = new ConfArr();
  for (int n = 0; n < N; n++) {
    Shell sh = new Shell(n, from.get(n), L);
    log.dbg("shell[n=", n);
    log.dbg("=", sh);
    Conf c = new Conf(sh);
    log.dbg("shell config=", c);
    res.add(c);
  }
  log.dbg("makeOneElec()=", res);
  return res;
}
}
