package math.integral;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.IFuncArr;
import math.func.arr.NormFuncArr;
import math.mtrx.api.Mtrx;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 10:37:57
 */
public class OrthFactory {
  public static Log log = Log.getLog(OrthFactory.class);

public static FuncVec keepN(FuncVec wf, Quadr quadr, IFuncArr basis) {
  Vec x = quadr.getX();
  FuncVec res = new FuncVec(x);
  for (int i = 0; i < basis.size(); i++) {
    FuncVec fi = basis.getFunc(i);         //log.dbg("fi=", fi);
    double dS = quadr.calcInt(wf, fi);     //log.dbg("d=", dS);
    res.addMultSafe(dS, fi);              //log.dbg("res=", res);
  }
//  for (int i = 0; i < basis.size(); i++) {
//    FuncVec fi = basis.getFunc(i);           //log.dbg("fi=", fi);
//    double testInt = quadr.calcInt(res, fi);   //log.dbg("testS=", testS);
//    assertEquals("testInt_" + i, testInt, 0d);
//    assertEquals(0, testInt, MAX_INTGRL_ERR_E11);
//  }
  return res;
}

public static void keepN(IFuncArr dest, Quadr quadr, IFuncArr basis) {
  for (int i = 0; i < dest.size(); i++) {
    FuncVec wf = dest.getFunc(i);         //log.dbg("fi=", fi);
    FuncVec wfN = keepN(wf, quadr, basis);         //log.dbg("fi=", fi);
    dest.setFunc(i, wfN);
  }
  return ;
}
  public static double calcMaxOrthonErr(NormFuncArr arr) {
    return calcMaxOrthErr(arr, arr.getNormQuadr());
  }
  public static double calcMaxOrthErr(FuncArr arr, Quadr w) {
    double res = 0;
    for (int n = 0; n < arr.size(); n++) {
      for (int n2 = 0; n2 <= n; n2++) {
        double norm = w.calcInt(arr.get(n), arr.get(n2));
        log.dbg("norm error[" + n + ", " + n2 + "]=" + (float) norm);
        if (n == n2)
          norm = Math.abs(1. - norm);
        else
          norm = Math.abs(norm);
        if (res < norm) {
          res = norm;
        }
      }
    }
    return res;
  }

  //http://en.wikipedia.org/wiki/Gram-Schmidt_process
  public static void makeOrthRotate(FuncArr from, Quadr w) {   // via GramSchmidt
    Mtrx BB = new Mtrx(from.size(), from.size());
    for (int r = 0; r < from.size(); r++) {
      for (int c = 0; c <= r; c++) {
        double q = w.calcInt(from.get(r), from.get(c));
        BB.set(r, c, q);
        BB.set(c, r, q);
      }
    }                                                        log.dbg("BB=\n", BB);
    Vec[] CC = new Vec[from.size()];
    // F_0 = B_0
    int i = 0;
    CC[i] = new Vec(i + 1);
    CC[i].set(i, 1.);  // i:=0
    i++;

    // F_1 = B_1 + C_0 * B_0
    // <F_1 B_0> = BB_01 + C_0 BB_00 = 0
    CC[i] = new Vec(i + 1);
    CC[i].set(i, 1.);
    CC[i].set(i - 1, -BB.get(0, 1) / BB.get(0, 0));
    i++;

    // F_i = B_i + SUM(j<i) C_j * B_j
    // <F_i B_j'> = BB_ij' + SUM(j<i) C_j BB_jj'= 0
    // M * C = D
    // C = M^-1 * D
    for (; i < from.size(); i++) {    log.dbg("i = ", i);
      Vec D = new Vec(i);
      Mtrx M = new Mtrx(i, i);
      for (int j = 0; j < i; j++) {
        D.set(j, -BB.get(i, j));
        for (int J = 0; J < i; J++) {
          M.set(j, J, BB.get(j, J));
        }
      }        log.dbg("M=\n", M);  log.dbg("D = ", D);
      M = M.inverse();          log.dbg("M.inverse=\n", M);
//      Vec C = new Vec(i);
//      C.mult(M, D);   log.dbg("C=M*D=", C);
      Vec C = M.mult(D);     log.dbg("C=M*D=", C);
      CC[i] = new Vec(i + 1);
      CC[i].set(i, 1);
      for (int j = 0; j < i; j++) {
        CC[i].set(j, C.get(j));
      }
    }
    FuncArr res = makeOrth(from, CC);
    norm(res, w);
    from.setArr(res);
  }
  public static FuncArr makeOrth(FuncArr from, Vec[] C) {
    Vec x = from.getX();
    FuncArr res = new FuncArr(x, from.size());
    for (int ix = 0; ix < x.size(); ix++) {
      for (int i = 0; i < from.size(); i++) {
        Vec Cj = C[i];
        double sum = 0;
        int N = Cj.size();
        for (int j = 0; j < N; j++) {
          sum += Cj.get(j) * from.get(j).get(ix);
        }
        res.get(i).set(ix, sum);
      }
    }
    return res;
  }
//http://en.wikipedia.org/wiki/Gram-Schmidt_process
public static FuncArr makeOrth_DEV(FuncArr from, Quadr w) {
  FuncArr res = from.copyDeepY();
  norm(res, w);
  for (int k = 1; k < res.size(); k++) { // NOTE: k=1
    FuncVec vk = res.get(k);
    FuncVec uk = vk.copyY();
    for (int j = 0; j < k; j++) {
      FuncVec uj = res.get(j);
      double vu = w.calcInt(vk, uj);
      double uu = w.calcInt(uj, uj);
      uk.addMultSafe(-vu / uu, uj);
    }
    double norm = w.calcInt(uk, uk);
    uk.mult(1. / Math.sqrt(norm));
    res.set(k, uk);
  }
  return res;
}
  public static void norm(FuncArr arr, Quadr w) {
    for (int r = 0; r < arr.size(); r++) {
      FuncVec f = arr.get(r);
      double s = w.calcInt(f, f);
      s = 1. / Math.sqrt(s);
      f.mult(s);
    }
  }

}
