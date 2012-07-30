package atom.shell;

import atom.angular.Spin;
import atom.e_3.AtomShModelE3;
import atom.fano.Atom2011;
import atom.shell.deepcopy.ShId;
import atom.shell.deepcopy.ShWf;
import atom.wf.lcr.LcrFactory;
import atom.wf.lcr.WFQuadrLcr;
import math.func.arr.FuncArr;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

import javax.triplet.Int3;
import javax.utilx.intx.ArrInt4;
import javax.utilx.log.Log;
import java.util.TreeMap;

/**
* Created by Dmitry.A.Konovalov@gmail.com, 03/06/2010, 10:48:51 AM
*/
public class ConfArrFactoryE3 extends FlowTest {
public static Log log = Log.getLog(ConfArrFactoryE3.class);

protected static int R_FIRST = 0;
protected static int R_LAST = 100;
protected static int R_N = 101;
protected static int LCR_N = 101;
protected static int LCR_FIRST = -4;
protected static int L = 0;
protected static int MAX_N = 10;
protected static double LAMBDA = 1.;
//private static final int MAX_SUM_N23 = 100;
public ConfArrFactoryE3() {
  super(ConfArrFactoryE3.class);
}

public void testMakePoet() throws Exception {
//  ConfArrFactoryE3.log.setDbg();
  StepGridOpt modelR = new StepGridOpt(R_FIRST, R_LAST, R_N); // R_N not used!!!
  StepGridOpt sg = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, modelR);
  log.dbg("x step grid model =", sg);
  StepGrid x = new StepGrid(sg);
  log.dbg("x grid =", x);
  WFQuadrLcr quadrLcr = new WFQuadrLcr(x);
  log.dbg("x weights =", quadrLcr);
  LgrrOpt basisOptN = new LgrrOpt(L, MAX_N, LAMBDA);
  LgrrOrthLcr orthonN = new LgrrOrthLcr(quadrLcr, basisOptN);
  log.dbg("LgrrOrthLcr = ", orthonN);
  ShWf.convertToShWf(orthonN, L);

  int N = 1;
  int N2 = 2;
  int N3 = 3;
  Ls LS = new Ls(0, Spin.ELECTRON);
  AtomShModelE3 modelE3 = new AtomShModelE3(N, N2, N3, LS);
  TreeMap<ShId, Int3> idMap;
  ArrInt4 validArr;
  Int3 count;
  ShId id0 = new ShId(0, 0);
  ShId id1 = new ShId(1, 0);
  ShId id2 = new ShId(2, 0);

  LsConfs arrD = ConfArrFactoryE3.makeSModelDiff(modelE3, orthonN, -1);
  log.dbg("arrD=", arrD);
  assertEquals(2, arrD.size());

  idMap = Atom2011.loadIdMap(arrD.get(0), arrD.get(0));
  log.dbg("idMap=", idMap);
  count = idMap.get(id0);
  assertEquals(1, count.a);
  assertEquals(1, count.b);
  assertEquals(1, count.c);
  count = idMap.get(id1);
  assertEquals(1, count.a);
  assertEquals(1, count.b);
  assertEquals(1, count.c);
  count = idMap.get(id2);
  assertEquals(1, count.a);
  assertEquals(1, count.b);
  assertEquals(1, count.c);
  validArr = Atom2011.loadOneSpecShell(arrD.get(0), arrD.get(0));
  log.dbg("validArr=", validArr);
  assertEquals(3, validArr.size());

  idMap = Atom2011.loadIdMap(arrD.get(0), arrD.get(1));
  count = idMap.get(id0);
  assertEquals(1, count.a);
  assertEquals(1, count.b);
  count = idMap.get(id1);
  assertEquals(1, count.a);
  assertEquals(1, count.b);
  count = idMap.get(id2);
  assertEquals(1, count.a);
  assertEquals(1, count.b);
  validArr = Atom2011.loadOneSpecShell(arrD.get(0), arrD.get(1));
  log.dbg("validArr=", validArr);
  assertEquals(3, validArr.size());

  LsConfs arrC = ConfArrFactoryE3.makeSModelClosed(modelE3, orthonN, -1);
  log.dbg("arrC=", arrC);
  assertEquals(3, arrC.size());

  idMap = Atom2011.loadIdMap(arrC.get(0), arrD.get(0));
  count = idMap.get(id0);
  assertEquals(2, count.a);
  assertEquals(1, count.b);
  count = idMap.get(id1);
  assertEquals(1, count.a);
  assertEquals(1, count.b);
  count = idMap.get(id2);
  assertEquals(0, count.a);
  assertEquals(1, count.b);

  idMap = Atom2011.loadIdMap(arrD.get(0), arrC.get(0));
  count = idMap.get(id0);
  assertEquals(1, count.a);
  assertEquals(2, count.b);
  assertEquals(1, count.c);
  count = idMap.get(id1);
  assertEquals(1, count.a);
  assertEquals(1, count.b);
  count = idMap.get(id2);
  assertEquals(1, count.a);
  assertEquals(0, count.b);
  assertEquals(0, count.c);

  idMap = Atom2011.loadIdMap(arrC.get(0), arrC.get(0));
  count = idMap.get(id0);
  assertEquals(2, count.a);
  assertEquals(2, count.b);
  count = idMap.get(id1);
  assertEquals(1, count.a);
  assertEquals(1, count.b);

  idMap = Atom2011.loadIdMap(arrC.get(0), arrC.get(1));
  count = idMap.get(id0);
  assertEquals(2, count.a);
  assertEquals(2, count.b);
  count = idMap.get(id1);
  assertEquals(1, count.a);
  assertEquals(0, count.b);
  count = idMap.get(id2);
  assertEquals(0, count.a);
  assertEquals(1, count.b);

  LsConfs arr = ConfArrFactoryE3.makeSModelAll(modelE3, orthonN);
  log.dbg("arr=", arr);
  assertEquals(5, arr.size());

  modelE3.setN1(N2);
  arr = ConfArrFactoryE3.makeSModelDiff(modelE3, orthonN, -1);
  log.dbg("arr=", arr);
  assertEquals(2, arr.size());
  arr = ConfArrFactoryE3.makeSModelClosed(modelE3, orthonN, -1);
  log.dbg("arr=", arr);
  assertEquals(4, arr.size());

  idMap = Atom2011.loadIdMap(arr.get(0), arr.get(0));
  count = idMap.get(id0);
  assertEquals(2, count.a);
  assertEquals(2, count.b);
  count = idMap.get(id1);
  assertEquals(1, count.a);
  assertEquals(1, count.b);

  idMap = Atom2011.loadIdMap(arr.get(1), arr.get(0));
  count = idMap.get(id0);
  assertEquals(2, count.a);
  assertEquals(2, count.b);
  count = idMap.get(id1);
  assertEquals(0, count.a);
  assertEquals(1, count.b);
  count = idMap.get(id2);
  assertEquals(1, count.a);
  assertEquals(0, count.b);

  idMap = Atom2011.loadIdMap(arr.get(2), arr.get(0));
  count = idMap.get(id0);
  assertEquals(1, count.a);
  assertEquals(2, count.b);
  count = idMap.get(id1);
  assertEquals(2, count.a);
  assertEquals(1, count.b);

  idMap = Atom2011.loadIdMap(arr.get(0), arr.get(3));
  count = idMap.get(id0);
  assertEquals(2, count.a);
  assertEquals(0, count.b);
  count = idMap.get(id1);
  assertEquals(1, count.a);
  assertEquals(2, count.b);
  count = idMap.get(id2);
  assertEquals(0, count.a);
  assertEquals(1, count.b);

  arr = ConfArrFactoryE3.makeSModelAll(modelE3, orthonN);
  log.dbg("arr=", arr);
  assertEquals(6, arr.size());
}

// All electrons are in DIFFERENT shells!!!!
public static LsConfs makeSModelDiff(AtomShModelE3 model, FuncArr arr
  , int maxN12 // max sum n1+n2
) {
  log.info("--->makeSModelDiff(AtomShModelE3 model=", model);
  int L = 0;
  LsConfs res = new LsConfs();
  Ls[] arrLS = {new Ls(L, Spin.SINGLET), new Ls(L, Spin.TRIPLET)};
  for (int n = 0; n < model.getN1(); n++) {
    Shell sh = new Shell(n, arr.get(n), L);      log.dbg("sh=", sh);
    if (!sh.isValid()) {
      log.dbg("NOT VALID");
      continue;
    }
    for (int n2 = n + 1; n2 < model.getN2(); n2++) {
      Shell sh2 = new Shell(n2, arr.get(n2), L);        log.dbg("sh2=", sh2);
      for (int idxLS = 0; idxLS < arrLS.length; idxLS++) {
        Ls tmpLS = arrLS[idxLS];
        for (int n3 = n2 + 1; n3 < model.getN3(); n3++) {
          LsConf fc = new ShPair(sh, sh2, tmpLS);          log.dbg("fc=", fc);
          if (!fc.isValid()) {
            log.dbg("NOT VALID");
            continue;
          }
          Shell sh3 = new Shell(n3, arr.get(n3), L);                 log.dbg("sh3=", sh3);
          fc.add(sh3, model.getLs());            log.dbg("fc=", fc);
          if (!fc.isValid()) {
            log.dbg("NOT VALID");
            continue;
          }
          addValidN123(n, n2, n3, res, fc, maxN12);
//          res.add(fc);
        }
      }
    }
  }
  log.dbg("from arr=\n", arr);
  log.dbg("res=\n", res);
  log.info("<--makePoetDiffShells");
  return res;
}
public static void addValidN123(int n1, int n2, int n3, LsConfs res, LsConf fc
  , int maxSumN12 // max sum n1+n2
) {
  int maxN23 = Math.max(n2, n3);
  int maxN123 = Math.max(n1, maxN23);

  int minN2 = Math.min(maxN123, n2);
//  if (minN2 + maxN123 > MAX_SUM_N23) {
//    return;
//  }

  int minN23 = Math.min(n2, n3);
  int minN123 = Math.min(n1, minN23);

  int maxN2 = Math.max(minN123, n2);
  ConfArrFactoryE2.addValidN12(minN123, maxN2, res, fc, maxSumN12);
}

// One electron above/below a closed shell
public static LsConfs makeSModelClosed(AtomShModelE3 model, FuncArr arr) {
  return makeSModelClosed(model, arr, -1);
}
public static LsConfs makeSModelClosed(AtomShModelE3 model, FuncArr arr
  , int maxN12 // max sum n1+n2
) {
  log.info("--->makePoetClosedShell(AtomShModelE3 model=", model);
  LsConfs res = new LsConfs();
  int size = model.getN1();
  int size2 = model.getN2();
  int size3 = model.getN3();
  if (size > size2) {
    throw new IllegalArgumentException(log.error("size > size2"));
  }
  if (size2 > size3) {
    throw new IllegalArgumentException(log.error("size2 > size3"));
  }
  int L = 0;
  Ls[] arrLS = {new Ls(L, Spin.SINGLET), new Ls(L, Spin.TRIPLET)};
  for (int idxLS = 0; idxLS < arrLS.length; idxLS++) {
    Ls tmpLS = arrLS[idxLS];
    for (int n = 0; n < size; n++) {
      int n2 = n;
      Shell sh12 = new ShellQ2(n, arr.get(n), L, tmpLS);        log.dbg("q=2 at sh12=", sh12);
      if (!sh12.isValid()) {                                    log.dbg("NOT VALID");
        continue;
      }
      for (int n3 = 0; n3 < size3; n3++) {
        if (n == n3)
          continue;
        Shell sh3 = new Shell(n3, arr.get(n3), L);             log.dbg("sh3=", sh3);
        LsConf fc;
        if (n < n3) {
          fc = new ShPair(sh12, sh3, model.getLs());
        }
        else {
          fc = new ShPair(sh3, sh12, model.getLs());
        }
        log.dbg("fc=", fc);
        if (!fc.isValid()) {
          log.dbg("NOT VALID");
          continue;
        }
        addValidN123(n, n2, n3, res, fc, maxN12);
//        res.add(fc);
      }
    }

    for (int n2 = size; n2 < size2; n2++) { // NOTE! Starting from size
      int n3 = n2;
      Shell sh23 = new ShellQ2(n2, arr.get(n2), L, tmpLS);        log.dbg("q=2 at sh23=", sh23);
      if (!sh23.isValid()) {
        log.dbg("NOT VALID");
        continue;
      }
      for (int n = 0; n < size; n++) {
        Shell sh = new Shell(n, arr.get(n), L);             log.dbg("sh=", sh);
        LsConf fc = new ShPair(sh, sh23, model.getLs());
        log.dbg("fc=", fc);
        if (!fc.isValid()) {
          log.dbg("NOT VALID");
          continue;
        }
        addValidN123(n, n2, n3, res, fc, maxN12);
//        res.add(fc);
      }
    }
  }
  log.dbg("from arr=\n", arr);
  log.dbg("res=\n", res);
  log.info("<--makePoetClosedShell");
  return res;
}

public static LsConfs makeSModelAll(AtomShModelE3 model, FuncArr arr) {
  log.info("-->makeSModel(AtomShModelE3 model=", model);
  LsConfs res = makeSModelDiff(model, arr, -1);
  LsConfs closed = makeSModelClosed(model, arr, -1);
  res.addAll(closed);
  log.info("res.size()=" + res.size());
  log.info("<--makeSModel(AtomShModelE3 model=", model);
  return res;
}
public static LsConfs makeSModelSmall(AtomShModelE3 model, FuncArr arr) {
  log.info("-->makeSModel(AtomShModelE3 model=", model);
  LsConfs res = makeSModelDiff(model, arr, ConfArrFactoryE2.FAST_MAX_N12);
  LsConfs closed = makeSModelClosed(model, arr, ConfArrFactoryE2.FAST_MAX_N12);
  res.addAll(closed);
  log.info("res.size()=" + res.size());
  log.info("<--makeSModel(AtomShModelE3 model=", model);
  return res;
}
}
