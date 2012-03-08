package atom.e_2;
import atom.energy.slater.SlaterLcr;
import math.Mathx;
import atom.shell.*;
import atom.energy.Energy;

import javax.utilx.log.Log;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:09:14
 *
 * THIS uses equations from the 1965 Fano's paper
 */
public class SysE2_OLD extends AtomE2 {
  public static Log log = Log.getLog(SysE2_OLD.class);
  public SysE2_OLD(double z, SlaterLcr si) {
    super(z, si);
  }
  @Override
  public Energy calcH(Conf fc, Conf fc2) {
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
    if (a2.getId().equals(b2.getId()))
      exc = new Energy(dir);
    else
      exc = calcShPairEng(fc.getTotLS().getL(), a, b, b2, a2);
    int prty = 0; // see (21); no spectator electrons for He
    //exc *= MathX.pow(-1, (int)((CL)fc.s() + sp2.a()->nl().L() + sp2.b()->nl().L() + CL(1)) - (int)fc.L() );
    double excMult = Mathx.pow(-1, (fc.getTotLS().getS() + a2.getWfL() + b2.getWfL() + 1) - fc.getTotLS().getL());
    exc.kin *= excMult;
    exc.pot *= excMult;
    DiEx norm = normQ(a, b, a2, b2, prty);
    Energy res = new Energy(norm.di * dir.kin + norm.ex * exc.kin
      , norm.di * dir.pot + norm.ex * exc.pot);
    return res;
  }

  public double calcOverlap(Conf fc, Conf fc2) {
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
    //exc *= MathX.pow(-1, (int)((CL)fc.s() + sp2.a()->nl().L() + sp2.b()->nl().L() + CL(1)) - (int)fc.L() );
    double excMult = Mathx.pow(-1, (fc.getTotLS().getS() + a2.getWfL() + b2.getWfL() + 1) - fc.getTotLS().getL());
    exc *= excMult;
    DiEx norm = normQ(a, b, a2, b2, prty);
    double res = norm.di * dir + norm.ex * exc;
    return res;
  }
}
