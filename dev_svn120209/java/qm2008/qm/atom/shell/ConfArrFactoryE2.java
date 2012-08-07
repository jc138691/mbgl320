package atom.shell;

import math.func.FuncVec;
import math.func.arr.FuncArr;

import javax.utilx.log.Log;

/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:17:36
*/
public class ConfArrFactoryE2 {
public static Log log = Log.getLog(ConfArrFactoryE2.class);
//public static final int FAST_MAX_N12 = 2;  //-1 all

public static LsConfs makeTwoElec(Ls LS, int N, int L, FuncArr fromArr) {
  LsConfs res = new LsConfs();
  for (int n = 0; n < N; n++) {
    Shell sh = new ShellQ2(n, fromArr.get(n), L, LS);
    log.dbg("q=2, sh=", sh);
    if (sh.isValid()) {
      LsConf fc = new ShPair(sh);
      log.dbg("fc(n=n2)=", fc);
      res.add(fc);
    }
    sh = new Shell(n, fromArr.get(n), L);
    log.dbg("sh=", sh);
    for (int n2 = n + 1; n2 < N; n2++) {
      Shell sh2 = new Shell(n2, fromArr.get(n2), L);
      log.dbg("sh2=", sh2);
      LsConf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
      res.add(fc);
    }
  }
  log.dbg("fromArr=\n", fromArr);
  log.dbg("from=\n", res);
  return res;
}

public static LsConfs makePoetConfE1(FuncArr arr) {  //[14Apr2011] converted from makeSModelAllE2() for testing/debuging
  int L = 0;
  LsConfs res = new LsConfs();
  int minN = arr.size();
  FuncArr minA = arr;
  for (int n = 0; n < minN; n++) { // n=0,...,(N-1)
    Shell sh = new Shell(n, minA.get(n), L);      log.dbg("sh=", sh);
    LsConf fc = new LsConf(sh);                       log.dbg("fc=", fc);
    res.add(fc);
  }
  log.dbg("from arr=\n", arr);
  log.dbg("res=\n", res);
  return res;
}

public static LsConfs makeSModelE2(Ls LS, FuncArr arr, FuncArr arr2) {
  int L = 0;
  LsConfs res = new LsConfs();
  int minN = arr.size();
  int maxN = arr2.size();
  FuncArr minA = arr;
  FuncArr maxA = arr2;
  return makeSModel(LS, minA, minN, maxA, maxN, -1);
}
public static LsConfs makeSModelE2(Ls LS, FuncArr arr) {
  int L = 0;
  LsConfs res = new LsConfs();
  int minN = arr.size();
  int maxN = arr.size();
  FuncArr minA = arr;
  FuncArr maxA = arr;
  return makeSModel(LS, minA, minN, maxA, maxN, -1);
}
public static LsConfs makeSModelMmE2(Ls LS, FuncArr arr, FuncArr arr2) {
int L = 0;
  LsConfs res = new LsConfs();
  int minN = arr.size();
  int maxN = arr2.size();
  FuncArr minA = arr;
  FuncArr maxA = arr2;
  return makePoetConfMmE2(LS, minA, minN, maxA, maxN);
}

public static LsConfs makeSModelAllE2(Ls LS, FuncArr arr, int minN) {
  int maxN = arr.size();
  FuncArr minA = arr;
  FuncArr maxA = arr;
  return makeSModel(LS, minA, minN, maxA, maxN, -1); // -1 means all;
}
public static LsConfs makeSModelSmall(Ls LS, FuncArr arr, int minN) {
  int maxN = arr.size();
  FuncArr minA = arr;
  FuncArr maxA = arr;
  return makeSModel(LS, minA, minN, maxA, maxN, minN);
}
public static LsConfs makeSModelMm(Ls LS, FuncArr arr, int minN) {
  int maxN = arr.size();
  FuncArr minA = arr;
  FuncArr maxA = arr;
  return makeSModel(LS, minA, minN, maxA, maxN, -1);
}

public static LsConfs makeSModel(Ls LS
  , FuncArr minA, int minN
  , FuncArr maxA, int maxN
  , int maxSizeN12 // max n1 or n2
//  , int maxN12 // max sum n1+n2
) {
  int L = 0;
  LsConfs res = new LsConfs();
  res.setLs(LS);
  if (minN > maxN) {
    throw new IllegalArgumentException(log.error("arr2.size() < arr.size()!!!!!!!!"));
  }
  for (int n = 0; n < minN; n++) { // n=0,...,(N-1)
    int n2 = n;
    Shell sh = new ShellQ2(n, minA.get(n), L, LS); log.dbg("q=2 at sh=", sh);
    if (sh.isValid()) {
      LsConf fc = new ShPair(sh);                  log.dbg("fc(n=n2)=", fc);
      addValidN12(n, n2, res, fc, maxSizeN12);
//      res.add(fc);
    }
    sh = new Shell(n, minA.get(n), L);
    log.dbg("sh=", sh);
    for (n2 = n + 1; n2 < minN; n2++) {
      Shell sh2 = new Shell(n2, minA.get(n2), L);      log.dbg("sh2=", sh2);
      LsConf fc = new ShPair(sh, sh2, LS);             log.dbg("fc=", fc);
      addValidN12(n, n2, res, fc, maxSizeN12);
//      res.add(fc);
    }

    // PLUS extra with (n>=N-1)
    for (n2 = minN; n2 < maxN; n2++) {
      Shell sh2 = new Shell(n2, maxA.get(n2), L);     log.dbg("sh2=", sh2);
      LsConf fc = new ShPair(sh, sh2, LS);            log.dbg("fc=", fc);
      addValidN12(n, n2, res, fc, maxSizeN12);
//      res.add(fc);
    }
  }
  log.dbg("from minA=\n", minA);
  log.dbg("from maxA=\n", maxA);
  log.dbg("res=\n", res);
  return res;
}

public static void addValidN12(int n1, int n2, LsConfs res, LsConf fc
//  , int maxN12 // max sum n1+n2
  , int maxSizeN12 // max NUMBER of n1 and n2  (NOT index!!!!)
) {
  if (maxSizeN12 == -1      //-1 means all
    ||  (n1 < maxSizeN12  &&  n2 < maxSizeN12)     //// max NUMBER of n1 and n2  (NOT index!!!!)
//    ||  (n1 + n2) <= maxN12    // max sum n1+n2
    ||  n1 == 0
    )  {
    res.add(fc);
  }
}

public static LsConfs makeSModelSmall_TRY(int size1, int size2, int maxNN   // idx + idx2 <=maxNN
  , Ls LS, FuncArr basis) {
  int L = 0;
  LsConfs res = new LsConfs();
  res.setLs(LS);
  for (int n = 0; n < size1; n++) { // n=0,...,(N-1)
    Shell sh = new ShellQ2(n, basis.get(n), L, LS); log.dbg("q=2 at sh=", sh);
    if (sh.isValid()  && (2 * n <= maxNN)
      ) {
      LsConf fc = new ShPair(sh);                  log.dbg("fc(n=n2)=", fc);
      res.add(fc);
    }
    sh = new Shell(n, basis.get(n), L);            log.dbg("sh=", sh);
    for (int n2 = n + 1; n2 < size2; n2++) {
      if (n + n2 > maxNN)
        continue;
      Shell sh2 = new Shell(n2, basis.get(n2), L);    log.dbg("sh2=", sh2);
      LsConf fc = new ShPair(sh, sh2, LS);            log.dbg("fc=", fc);
      res.add(fc);
    }
  }
  log.dbg("from minA=\n", basis);
  log.dbg("res=\n", res);
  return res;
}

public static LsConfs makePoetConfMmE2_DBG(Ls LS, FuncArr minA, int minN, FuncArr maxA, int maxN) {
  int L = 0;
  LsConfs res = new LsConfs();
  res.setLs(LS);
  if (minN > maxN) {
    throw new IllegalArgumentException(log.error("arr2.size() < arr.size()!!!!!!!!"));
  }
  for (int n = 0; n < minN; n++) { // n=0,...,(N-1)
    Shell sh = new ShellQ2(n, minA.get(n), L, LS); log.dbg("q=2 at sh=", sh);
    if (sh.isValid()) {
      LsConf fc = new ShPair(sh);                  log.dbg("fc(n=n2)=", fc);
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
      LsConf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
      res.add(fc);
    }

    // PLUS extra with (n>=N-1)
    for (int n2 = minN; n2 < maxN; n2++) {
      Shell sh2 = new Shell(n2, maxA.get(n2), L);
      log.dbg("sh2=", sh2);
      LsConf fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
//        LsConf fc = new ShPair(sh).addShell(sh2, LS);   log.dbg("fc=", fc);
      res.add(fc);
    }
  }
  log.dbg("from minA=\n", minA);
  log.dbg("from maxA=\n", maxA);
  log.dbg("res=\n", res);
  return res;
}

public static LsConfs makePoetConfMmE2(Ls LS, FuncArr minA, int minN, FuncArr maxA, int maxN) {
  int L = 0;
  LsConfs res = new LsConfs();
  res.setLs(LS);
  if (minN > maxN) {
    throw new IllegalArgumentException(log.error("arr2.size() < arr.size()!!!!!!!!"));
  }
  Shell sh, sh2;
  int n2;
  LsConf fc;
  for (int n = 0; n < minN; n++) { // n=0,...,(N-1)
//    sh = new ShellQ2(n, minA.get(n), L, LS); log.dbg("q=2 at sh=", sh);
//    if (sh.isValid()) {
//      LsConf fc = new ShPair(sh);                  log.dbg("fc(n=n2)=", fc);
//      res.add(fc);
//    }

    sh = new Shell(n, minA.get(n), L);
    log.dbg("sh=", sh);
//    n2 = n;
//    for (n2 = n; n2 < minN; n2++) { // FOR MM-model
    for (n2 = 0; n2 < minN; n2++) { // FOR MM-model
//    for (int n2 = 0; n2 < n; n2++) { // FOR MM-model
//      if (n2 == n)
//        continue;
//      if (n2 < n) {
//        sh2 = new Shell(n2, minA.get(n2), L);
//      } else {
        sh2 = new Shell(n2, maxA.get(n2), L);
//      }

      log.dbg("sh2=", sh2);
//      LsConf fc = new ShPair(sh, sh2, LS);
      fc = new ShPair(sh2, sh, LS);
      log.dbg("fc=", fc);
      res.add(fc);

    }

    // PLUS extra with (n>=N-1)
    for (n2 = minN; n2 < maxN; n2++) {
      sh2 = new Shell(n2, maxA.get(n2), L);
      log.dbg("sh2=", sh2);
      fc = new ShPair(sh, sh2, LS);
      log.dbg("fc=", fc);
//        LsConf fc = new ShPair(sh).addShell(sh2, LS);   log.dbg("fc=", fc);
      res.add(fc);
    }
  }
  log.dbg("from minA=\n", minA);
  log.dbg("from maxA=\n", maxA);
  log.dbg("res=\n", res);
  return res;
}

// this should be deleted if the jmV2 idea is no good.
public static LsConfs makeTwoElec_forJmV2(Ls LS, int L, FuncArr arr, int n2, FuncVec func2) {
  LsConfs res = new LsConfs();
  for (int n = 0; n < arr.size(); n++) { // n=0,...,(N-1)
    if (n == n2) { // why adding a different function with the same n?
      String mssg = "adding n=n2=" + n2;
      throw new IllegalArgumentException(log.error(mssg));
    }
    Shell sh = new Shell(n, arr.get(n), L);
    log.dbg("sh=", sh);
    Shell sh2 = new Shell(n2, func2, L);
    log.dbg("sh2=", sh2);
    LsConf fc = new ShPair(sh, sh2, LS);
    log.dbg("fc=", fc);
//      LsConf fc = new ShPair(sh).addShell(sh2, LS);   log.dbg("fc=", fc);
    res.add(fc);
  }
  log.dbg("from arr=\n", arr);
  log.dbg("from func2=\n", func2);
  log.dbg("res=\n", res);
  return res;
}

public static LsConfs makeTwoElecSameN(Ls LS, int N, FuncArr from) {
  LsConfs res = new LsConfs();
  for (int n = 0; n < N; n++) {
    Shell sh = new Shell(n, from.get(n), 2, 0, LS);
    LsConf c = new ShPair(sh);
    res.add(c);
  }
  log.dbg("from=\n", from);
  log.dbg("res=\n", res);
  return res;
}

public static LsConfs makeOneElec(int N, int L, FuncArr from) {
  log.dbg("makeOneElec(N=", N);
  log.dbg("L=", L);
  log.dbg("from lgrrN=", from);
  LsConfs res = new LsConfs();
  for (int n = 0; n < N; n++) {
    Shell sh = new Shell(n, from.get(n), L);
    log.dbg("shell[n=", n);
    log.dbg("=", sh);
    LsConf c = new LsConf(sh);
    log.dbg("shell config=", c);
    res.add(c);
  }
  log.dbg("makeOneElec()=", res);
  return res;
}
}
