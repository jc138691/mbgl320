package atom;
import atom.energy.Energy;
import atom.energy.slater.Slater;
import atom.fano.FanoTermE2;
import atom.shell.*;
import atom.angular.Wign6j;
import atom.angular.Reduced3j;
import math.func.FuncVec;
import math.Mathx;

import javax.utilx.log.Log;

import static math.Mathx.dlt;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 11:51:44
 */
// this uses equations from Fano 1965 PhysRev 140, pA65
abstract public class AtomFano1965 {
  public static Log log = Log.getLog(AtomFano1965.class);
  final public double Z;
  final protected Slater si;
  public AtomFano1965(double z, Slater si) {
    Z = z;
    this.si = si;
  }
  final public double calcTot(Conf fc, Conf fc2) {
    Energy res = calcH(fc, fc2);
    return res.kin + res.pot;
  }
  final public double calcKin(Conf fc, Conf fc2) {
    Energy res = calcH(fc, fc2);
    return res.kin;
  }
  final public double calcPot(Conf fc, Conf fc2) {
    Energy res = calcH(fc, fc2);
    return res.pot;
  }
  abstract public Energy calcH(Conf fc, Conf fc2);
  public FuncVec calcDensity(Conf fc, Conf fc2) {return null;}
  abstract public Energy calcOverlap(Conf fc, Conf fc2);

  public void assertLS(Conf fc, Conf fc2) {
    if (!fc.getTotLS().equals(fc2.getTotLS())) {// this is not possible, and must be a bug
      String mssg = "!fc.Ls().equals(fc2.Ls()); " + fc.getTotLS() + "!=" + fc2.getTotLS();
      throw new IllegalArgumentException(log.error(mssg));
    }
  }
  abstract public int getNumElec();
  public double calcRk(Shell a, Shell b, Shell a2, Shell b2, int kk) {
    String mssg = "must overwrite before use";
    throw new IllegalArgumentException(log.error(mssg));
  }
  public Energy calcShPairEng(int LL, Shell a, Shell b, Shell a2, Shell b2) {
    Energy res = calcOneH(a, b, a2, b2);
    res.pot += calcTwoPot(LL, a, b, a2, b2);
    return res;
  }
  public Energy calcOverlap(Shell a, Shell b, Shell a2, Shell b2) {
    Energy res = new Energy();
    if (a.getWfL() != a2.getWfL() || b.getWfL() != b2.getWfL())
      return res;
    // <aa||aa> // note that <aa||cc> = 0 if a!=c
    if (a.getId().equals(b.getId())  // <aa|
      && a2.getId().equals(b2.getId()) //    |cc>
      && a.getId().equals(a2.getId())) { // <aa||aa> no need to calculate the same z_pot twice
      res.pot = calcOverlap(a, a2);
      res.pot *= 2;
    } else {
      if (a.getId().equals(a2.getId()))  // <ab||ab'>
        res.pot = calcOverlap(b, b2);  // b, b2 could be different
      if (b.getId().equals(b2.getId())) {  // <ab||a'b>
        double ov2 = calcOverlap(a, a2);  // a, a2 could be different
        res.pot += ov2;
      }
    }
    res.pot /= (getNumElec() - 1);
    return res;
  }
  public Energy calcOneH(Shell a, Shell b, Shell a2, Shell b2) {
    Energy res = new Energy();
    if (a.getWfL() != a2.getWfL() || b.getWfL() != b2.getWfL())
      return res;

    // <aa||aa> // note that <aa||cc> = 0 if a!=c
    if (a.getId().equals(b.getId())  // <aa|
      && a2.getId().equals(b2.getId()) //    |cc>
      && a.getId().equals(a2.getId())) { // <aa||aa> no need to calculate the same z_pot twice
      res = calcOneH(a, a2);
      res.kin *= 2;
      res.pot *= 2;
    } else {
      if (a.getId().equals(a2.getId()))  // <ab||ab'>
        res = calcOneH(b, b2);  // b, b2 could be different
      if (b.getId().equals(b2.getId())) {  // <ab||a'b>
        Energy res2 = calcOneH(a, a2);  // a, a2 could be different
        res.kin += res2.kin;
        res.pot += res2.pot;
      }
    }
    res.kin /= (getNumElec() - 1);
    res.pot /= (getNumElec() - 1);
    return res;
  }
  public FuncVec calcOneDensity(Shell a, Shell b, Shell a2, Shell b2) {
    FuncVec res = null;
    if (a.getWfL() != a2.getWfL() || b.getWfL() != b2.getWfL())
      return null;

    // <aa||aa> // note that <aa||cc> = 0 if a!=c
    if (a.getId().equals(b.getId())  // <aa|
      && a2.getId().equals(b2.getId()) //    |cc>
      && a.getId().equals(a2.getId())) { // <aa||aa> no need to calculate the same twice
      res = calcOneDensity(a, a2);
      res.mult(2);
    } else {
      if (a.getId().equals(a2.getId()))  // <ab||ab'>
        res = calcOneDensity(b, b2);  // b, b2 could be different
      if (b.getId().equals(b2.getId())) {  // <ab||a'b>
        FuncVec res2 = calcOneDensity(a, a2);  // a, a2 could be different
        if (res == null)
          res = res2;
        else if (res2 != null)
          res.addSafe(res2);
      }
    }
    if (res != null)
      res.mult(1. / (getNumElec() - 1));
    return res;
  }
  public double calcTwoPot(int LL, Shell a, Shell b, Shell a2, Shell b2) {
    double res = 0;
    //      CL k_min(std::max(
    //           abs((int)sp.a()->nl().L() - (int)sp2.a()->nl().L())
    //         , abs((int)sp.b()->nl().L() - (int)sp2.b()->nl().L())));
    int k_min = Math.max(Math.abs(a.getWfL() - a2.getWfL()), Math.abs(b.getWfL() - b2.getWfL()));
    //      CL k_max(std::min(
    //           abs(sp.a()->nl().L() + sp2.a()->nl().L())
    //         , abs(sp.b()->nl().L() + sp2.b()->nl().L())));
    int k_max = Math.min(Math.abs(a.getWfL() + a2.getWfL()), Math.abs(b.getWfL() + b2.getWfL()));
    //      for (CL kk = k_min; kk <= k_max; kk = (int)kk + 2) {
    for (int kk = k_min; kk <= k_max; kk += 2) {
      //         double ang = calcTwoElecCoupling(
      //              sp.a()->nl().L(), sp.b()->nl().L()
      //            , sp2.a()->nl().L(), sp2.b()->nl().L()
      //            , kk, LL); // angular momentum coefficient
      double ang = calcTwoElecCoupling(a, b, a2, b2, kk, LL);
      if (AtomUtil.isZero(ang))
        continue;
//         res += ang * TRk<FUNC>(jmBasisN().w(), jmBasisN().r()
//            , *sp.a(), *sp.b(), *sp2.a(), *sp2.b(), kk);
      res += ang * calcRk(a, b, a2, b2, kk);
    }
    return res;
  }
  private double calcTwoElecCoupling(Shell a, Shell b, Shell a2, Shell b2, int k, int L) {
//      double two_el_LL_coupling( const CL& a,  const CL& b
//                 , const CL& a2, const CL& b2
//                 , const CL& k,  const CL& L ) const {
//         double res = (double)CWign6j(L, b, a, k, a2, b2);
//         if (fabs(res) < EPS18)
//            return 0.;
//         return res * pow(-(int)1, (int)(a2 + b + L))
//            * CReduced(a, k, a2) //TBD: need to store all values as in the CFactorial
//            * CReduced(b, k, b2);
    double res = (double) Wign6j.calc(L, b.getWfL(), a.getWfL(), k, a2.getWfL(), b2.getWfL());
    if (AtomUtil.isZero(res))
      return 0.;
    return res * Mathx.pow(-1, (int) (a2.getWfL() + b.getWfL() + L))
      * Reduced3j.calc(a.getWfL(), k, a2.getWfL()) //TBD: need to store all values as in the CFactorial
      * Reduced3j.calc(b.getWfL(), k, b2.getWfL());
  }
  public DiEx normQ(Shell a, Shell b, Shell a2, Shell b2, int prty) {
    //=============================
    // formula (22) from pA70
    // Normalize by number of getSh electrons
    int d = Mathx.delta(a.getId(), b.getId());
    int d2 = Mathx.delta(a2.getId(), b2.getId()); // delta prime
    double P = Mathx.pow(-1, prty) * 0.5;
    double nn = a.getQ() * (b.getQ() - d)
      * a2.getQ() * (b2.getQ() - d2);
    // sum              (1 - eta * delta_ab) * (1 - eta'* delta_a'b') (-1)**(eta-eta') =...
    //    eta,ets'=0,1
    // ...= dir * (1 + (1 - delta_ab) * (1 - delta_a'b'))
    //    - exc * (2 - delta_ab - delta_a'b')
    double S = Math.sqrt(nn);
    double D = S * (1 + (1 - d) * (1 - d2));
    double E = S * (2 - d - d2);
    return new DiEx(P * D, -P * E);
  }
//   public Energy normQ(Shell a, Shell b, Shell a2, Shell b2
//                             , Energy dir, Energy exc, int prty) {
//      Energy res = null;
//      //=============================
//      // formular (22) from pA70
//      // Normalize by number of getSh electrons
//      if (JMatrix.ignore(dir) && JMatrix.ignore(exc))
//         return res;
//      int d  = MathX.delta(a.idx, b.idx);
//      int d2 = MathX.delta(a2.idx, b2.idx); // delta prime
//      double P = MathX.pow(-1, prty) * 0.5;
//      res = new Energy(P, P);
//      double nn =  a.q *  (b.q - d)
//                * a2.q * (b2.q - d2);
//      // sum              (1 - eta * delta_ab) * (1 - eta'* delta_a'b') (-1)**(eta-eta') =...
//      //    eta,ets'=0,1
//      // ...= dir * (1 + (1 - delta_ab) * (1 - delta_a'b'))
//      //    - exc * (2 - delta_ab - delta_a'b')
//      double S = Math.sqrt(nn);
//      double D = S * (1 + (1 - d) * (1 - d2));
//      double E = S * (2 - d - d2);
//      res.kin *= (dir.kin * D - exc.kin * E);
//      res.pot *= (dir.pot * D - exc.pot * E);
////      double dir_exc = dir * (1 + (1 - d) * (1 - d2))  - exc * (2 - d - d2);
////      res *= (Math.sqrt(nn) * dir_exc);
//      return res;
//   }

//   public FuncVec normQDensity(Shell a, Shell b, Shell a2, Shell b2
//                             , FuncVec dir, FuncVec exc, int prty) {
//      FuncVec res = new FuncVec(dir);
//      // formular (22) from pA70
//      // Normalize by number of getSh electrons
//      int d  = MathX.delta(a.idx, b.idx);
//      int d2 = MathX.delta(a2.idx, b2.idx); // delta prime
//      double P = MathX.pow(-1, prty) * 0.5;
//      double nn =  a.q *  (b.q - d)
//                * a2.q * (b2.q - d2);
//      // sum              (1 - eta * delta_ab) * (1 - eta'* delta_a'b') (-1)**(eta-eta') =...
//      //    eta,ets'=0,1
//      // ...= dir * (1 + (1 - delta_ab) * (1 - delta_a'b'))
//      //    - exc * (2 - delta_ab - delta_a'b')
//      double S = Math.sqrt(nn);
//      double D = S * (1 + (1 - d) * (1 - d2));
//      double E = S * (2 - d - d2);
//      dir.scale(P * D);
//      exc.scale(-P * E);
//      dir.addLine(exc);
//      return dir;
//   }
  public Energy calcOneH(Shell sh, Shell sh2) {
    Energy res = new Energy();
    if (sh.getWfL() != sh2.getWfL())
      return res;
    res.pot = si.calcOneZPot(Z, sh, sh2);
    res.kin = si.calcOneKin(sh, sh2);
    return res;
  }
  public FuncVec calcOneDensity(Shell sh, Shell sh2) {
    if (sh.getWfL() != sh2.getWfL())
      return null;
    return si.calcOneDensity(sh, sh2);
  }
  public double calcOnePotZ(Shell sh, Shell sh2) {
    if (sh.getWfL() != sh2.getWfL())
      return 0.;
    return si.calcOneZPot(Z, sh, sh2);
  }
  public double calcOverlap(Shell sh, Shell sh2) {
    if (sh.getWfL() != sh2.getWfL())
      return 0.;
    return si.calcOverlap(sh, sh2);
  }
  public double calcOneKin(Shell sh, Shell sh2) {
    return si.calcOneKin(sh, sh2);
  }
  protected Energy calcFanoE2(DiEx spinTerm, Ls ls, FanoTermE2 t, FanoTermE2 t2) {
    if (spinTerm.isZero())  {
      return null;
    }
    ShInfo r = t.r;
    ShInfo s = t.s;
    ShInfo r2 = t2.r;
    ShInfo s2 = t2.s;          log.dbg("t=", t);        log.dbg("t2=", t2);
    Energy dir = calcShPairEng(ls.getL(), r.sh, s.sh, r2.sh, s2.sh);         log.dbg("dir=", dir);
    dir.times(spinTerm.di);

    Energy exc = null;
    if (t.hasExc(t2)) {
      exc = calcShPairEng(ls.getL(), r.sh, s.sh, s2.sh, r2.sh);                     log.dbg("exc=", exc);
      exc.times(spinTerm.ex);
      exc.times(-1); // since it is (dir - exc)
    }

    double norm2 = 1. + dlt(r.idx, s.idx) * dlt(r2.idx, s2.idx);               log.dbg("norm2=", norm2);
    double norm = t.norm * t2.norm;                                            log.dbg("norm=", norm);
    int sign = t.sign * t2.sign;                                               log.dbg("sign=", sign);

    int fano = t.signFano * t2.signFano;        log.dbg("fano=", fano);
    if (sign != fano) {
//      FanoTermE3 tmp = new FanoTermE3(b, r, s, conf);
//      FanoTermE3 tmp2 = new FanoTermE3(b2, r2, s2, conf2);
      log.info("t=", t);             log.info("t2=", t2);
      log.info("sign=", sign);       log.info("fano=", fano);
      throw new IllegalArgumentException(log.error("Fano's sign is different"));
    }
    sign = fano;

    double normTot = norm * sign / norm2;                                      log.dbg("normTot=", normTot);
    dir.add(exc);
    dir.times(normTot);                                                         log.dbg("res=", dir);
    return dir;
  }

  protected FuncVec calcDensity(DiEx spinTerm, Ls ls, FanoTermE2 t, FanoTermE2 t2) {
    ShInfo r = t.r;
    ShInfo s = t.s;
    ShInfo r2 = t2.r;
    ShInfo s2 = t2.s;                          log.dbg("t=", t);    log.dbg("t2=", t2);
    FuncVec dir = calcOneDensity(r.sh, s.sh, r2.sh, s2.sh);         log.dbg("dir=", dir);
    if (dir != null) {
      dir.mult(spinTerm.di);
    }

    FuncVec exc = null;
    if (t.hasExc(t2)) {
      exc = calcOneDensity(r.sh, s.sh, s2.sh, r2.sh);            log.dbg("exc=", exc);
      if (exc != null) {
        exc.mult(spinTerm.ex);
        exc.mult(-1); // since it is (dir - exc)
      }
    }
    if (exc == null && dir == null) {
      return null;
    }

    double norm2 = 1. + dlt(r.idx, s.idx) * dlt(r2.idx, s2.idx);     log.dbg("norm2=", norm2);
    double norm = t.norm * t2.norm;                                  log.dbg("norm=", norm);
    int sign = t.sign * t2.sign;             log.dbg("sign=", sign);
    double normTot = norm * sign / norm2;    log.dbg("normTot=", normTot);

    if (dir != null && exc != null)  {
      dir.addSafe(exc);
    } else  if (dir == null) {
      dir = exc;
    }
    dir.mult(normTot);      log.dbg("res=", dir);
    return dir;
  }

}