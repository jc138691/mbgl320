package atom.e_2;
import atom.energy.slater.SlaterLcr;
import d1.IConf;
import math.Mathx;
import atom.shell.*;
import atom.energy.Energy;

import javax.utilx.log.Log;

/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:09:14
*
* THIS uses equations from the 1965 Fano's paper
*/
public class SysE2OldOk extends AtomE2 {
public static Log log = Log.getLog(SysE2OldOk.class);
public SysE2OldOk(double z, SlaterLcr si) {
  super(z, si);
}
public Energy calcLsH(LsConf fc, LsConf fc2) {
  assertLS(fc, fc2);
  // He, H+e, any two electron atomic system
  ShPair sp = (ShPair) fc;
  Shell a = sp.a;
  Shell b = sp.b;
  // <a(1) b(2) |...|a2(1) b2(2)>
  ShPair sp2 = (ShPair) fc2;
  Shell a2 = sp2.a;
  Shell b2 = sp2.b;
  Energy dir = calcShPairEng(fc.getTotLS().getL(), a, b, a2, b2); // potential only
  Energy exc = null;
  if (a2.sameId(b2))
    exc = new Energy(dir);
  else
    exc = calcShPairEng(fc.getTotLS().getL(), a, b, b2, a2);
  int prty = 0; // see (21); no spectator electrons for He
  //ex *= MathX.pow(-1, (int)((CL)fc.s() + sp2.a()->nl().L() + sp2.b()->nl().L() + CL(1)) - (int)fc.L() );
  double excMult = Mathx.pow(-1, (fc.getTotLS().getS() + a2.getWfL() + b2.getWfL() + 1) - fc.getTotLS().getL());
  exc.timesSelf(excMult);
  DiEx norm = normQ(a, b, a2, b2, prty);
  dir.timesSelf(norm.di);
  exc.timesSelf(norm.ex);
  dir.add(exc); // result
  return dir;
}

public double calcLsOver(LsConf fc, LsConf fc2) {
  assertLS(fc, fc2);
  // He, H+e, any two electron atomic system
  ShPair sp = (ShPair) fc;
  Shell a = sp.a;
  Shell b = sp.b;
  // <a(1) b(2) |...|a2(1) b2(2)>
  ShPair sp2 = (ShPair) fc2;
  Shell a2 = sp2.a;
  Shell b2 = sp2.b;
  double dir = calcOverlap(a, b, a2, b2); // potential only
  double exc = 0;
  if (a2.equals(b2))
    exc = dir;
  else
    exc = calcOverlap(a, b, b2, a2);
  int prty = 0; // see (21); no spectator electrons for He
  //ex *= MathX.pow(-1, (int)((CL)fc.s() + sp2.a()->nl().L() + sp2.b()->nl().L() + CL(1)) - (int)fc.L() );
  double excMult = Mathx.pow(-1, (fc.getTotLS().getS() + a2.getWfL() + b2.getWfL() + 1) - fc.getTotLS().getL());
  exc *= excMult;
  DiEx norm = normQ(a, b, a2, b2, prty);
  double res = norm.di * dir + norm.ex * exc;
  return res;
}
}
