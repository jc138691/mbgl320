package scatt.jm_2008.jm.theory;

import math.Mathx;
import flanagan.complex.Cmplx;
import math.complex.Cmplx2F1;
import math.complex.CmplxGamma;
import math.func.FactLn;

import javax.utilx.log.Log;

import static java.lang.Math.abs;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 17/02/2010, 10:20:19 AM
 */
public class JmTools {
  public static Log log = Log.getLog(JmTools.class);
  private static FactLn fLog = FactLn.getInstance();

/*
c-------------------------------------------------------------------------
      subroutine sc_n (s_n, c_n, n, atomZ, L, q, alp, eps, fl, nfl)
c
c     C_n, S_n -coefficients, J.Phys.B9, 1491 (1976)     (I.2.13)
c
      complex*16 onei, Wgamma, gm, c1, c2, c3, c4, c5, q, t, a, b,
     >   x, y, th, th1, th2
      integer n, L, L2, nfl
      real*8 alp, atomZ, eps, c_n, s_n, fl(0:nfl), pi
c
c     z.ne.0 case needs debugging,
      if(abs(z) .gt. eps) stop ' atomZ=0 isnot implemented in sc_n(tools.jm)'
c
      L2 = 2 * (L + 1)
      onei = dcmplx(0d0, 1d0)
      pi = dacos(-1d0)
      a = q / alp
      b = a**2
      t = atomZ / q
c
      y = b + 0.25d0
      if (abs(y) .gt. eps) then
         x = (b - 0.25d0) / y
         y = a / y
         th = x + onei*y
         c4 = th**(-2)
         th1 = (1d0, 0d0) /
     >      (2d0**L  * exp(- dacos(-1d0) * 0.5d0 * t) * th**(onei*t) * y**L)
         th2 = th**(-n-1)
      else
         th = y / ((b - 0.25d0) + onei * a)
         y  = y / a
         c4 = th**2
         th1 = (1d0, 0d0) /
     >      (2d0**L  * exp(- dacos(-1d0) * 0.5d0 * t) ) * y**L
         if (abs(t) .gt. eps) then
            th1 = th1 * th**(onei*t)
         end if
         th2 = th**(n+1)
      end if
c
c     y = sin(theta)
c     where sin(theta) = a / (b + 0.25)
c
      c2 = dcmplx(L + 1, 0) - onei * t
      gm = WGamma(c2)
      c1 = dcmplx(-L, 0) - onei * t
      c2 = dcmplx(n+1, 0)
      c3 = dcmplx(n+L+2, 0) - onei * t
c
      call F_21_jm (c1, c2, c3, c4, c5, eps)
c
      c5 = -dexp(fl(n)) * gm / (zabs(gm) * WGamma(c3)) * th1 * th2 * c5
c
      s_n = Imag(c5)
      c_n = dble(c5)
c
      return
      end
 */
public static Cmplx sc_n(int n, int L, Cmplx q, double lambda, double eps) { log.dbg("calc(n=", n);
  Cmplx gm, c1, c2, c3, c4, c5; //  complex*16 onei, Wgamma, gm, c1, c2, c3, c4, c5, q, t, a, b,
  Cmplx th, th1, th2; // >   x, y, th, th1, th2
  Cmplx a = q.div(lambda);  log.dbg("a=", a);      //  a = q / alp
  Cmplx b = a.mult(a);       log.dbg("b=", b);      //  b = a**2
  Cmplx y = b.add(0.25);   log.dbg("y=", y);      //  y = b + 0.25d0
  if (y.abs() > eps) {//  if (abs(y) .gt. eps) then
    Cmplx x = b.add(- 0.25).div(y);   log.dbg("x=", x);      //     x = (b - 0.25d0) / y
    y = a.div(y);            log.dbg("y=", y);      //     y = a / y
    th = x.add(y.mult(Cmplx.i)); log.dbg("th=", th);    //     th = x + onei*y
    c4 = th.pow(-2);      log.dbg("c4=", c4);    //     c4 = th**(-2)
    th1 = Cmplx.ONE.div(y.times(2.).pow(L)); log.dbg("th1=", th1); //     th1 = (1d0, 0d0) /
    // >      (2d0**L  * exp(- dacos(-1d0) * 0.5d0 * t) * th**(onei*t) * y**L)
    th2 = th.pow(-n-1);   log.dbg("th2=", th2);  //     th2 = th**(-n-1)
  } else {//  else
    th = y.div(b.add(-0.25).add(a.mult(Cmplx.i))); log.dbg("th=", th);//     th = y / ((b - 0.25d0) + onei * a)
    y  = y.div(a);           log.dbg("y=", y);      //     y  = y / a
    c4 = th.pow(2);       log.dbg("c4=", c4);    //     c4 = th**2
    th1 = y.times(0.5).pow(L); log.dbg("th1=", th1);//     th1 = (1d0, 0d0) /
    // >      (2d0**L  * exp(- dacos(-1d0) * 0.5d0 * t) ) * y**L
    th2 = th.pow(n+1);    log.dbg("th2=", th2);  //     th2 = th**(n+1)
  }//  end if
//c     y = sin(theta)
//c     where sin(theta) = a / (b + 0.25)
  c2 = new Cmplx(L + 1);     log.dbg("Cmplx(L + 1)=", c2);     //  c2 = dcmplx(L + 1, 0) - onei * t
  gm = CmplxGamma.calc(c2);  log.dbg("WGamma(c2)=", gm);       //  gm = WGamma(c2)
  gm = gm.div(gm.abs());     log.dbg("gm / (zabs(gm)=", gm);   //  calc: gm / (zabs(gm)
  c1 = new Cmplx(-L);        log.dbg("Cmplx(-L)=", c1);        //  c1 = dcmplx(-L, 0) - onei * t
  c2 = new Cmplx(n+1);       log.dbg("Cmplx(n+1)=", c2);       //  c2 = dcmplx(n+1, 0)
  c3 = new Cmplx(n+L+2);     log.dbg("Cmplx(n+L+2)=", c3);     //  c3 = dcmplx(n+L+2, 0) - onei * t
  c5 = Cmplx2F1.calc(c1, c2, c3, c4, eps); log.dbg("2F1=", c5);//  call F_21_jm (c1, c2, c3, c4, c5, eps)

  // calc: -dexp(fl(n)) / WGamma(c3)
  c3 = CmplxGamma.calc(c3).log();   log.dbg("ln(WGamma(c3))=", c3); // NOTE log!!! ln(Gamma(c3))
  c3 = new Cmplx(fLog.calc(n)).minus(c3);  log.dbg("fl(n))-ln(WGamma(c3))=", c3); // ln(n!) - ln(Gamma(c3))
  c3 = c3.exp().times(-1);    log.dbg("-dexp(fl(n)) / WGamma(c3)=", c3); // calc: -dexp(fl(n)) / WGamma(c3)

  c5 = c3.mult(gm).mult(th1).mult(th2).mult(c5); log.dbg("res=", c5);//  c5 = -dexp(fl(n)) * gm / (zabs(gm) * WGamma(c3)) * th1 * th2 * c5
//  s_n = Imag(c5)
//  c_n = dble(c5)
  return c5;//  return
}//  end

//  subroutine sc_n (s_n, c_n, n, atomZ, L, q, alp, eps, fl, nfl)
//c     C_n, S_n -coefficients, J.Phys.B9, 1491 (1976)     (I.2.13)
//  complex*16 onei, Wgamma, gm, c1, c2, c3, c4, c5, q, t, a, b,
// >   x, y, th, th1, th2
//  integer n, L, L2, nfl
//  real*8 alp, atomZ, eps, c_n, s_n, fl(0:nfl), pi
  public static Cmplx sc_n(int n, int L, Cmplx q, double lambda, int jmZ, double eps) { log.dbg("calc(n=", n);
    if (jmZ == 0) {
      return sc_n(n, L, q, lambda, eps);
    }
    Cmplx t = new Cmplx(jmZ).div(q);     log.dbg("t=atomZ/q=", t); //t = atomZ / q
    Cmplx mit = (t.times(Cmplx.i)).times(-1); // -i*t
    Cmplx gm, c1, c2, c3, c4, c5; //  complex*16 onei, Wgamma, gm, c1, c2, c3, c4, c5, q, t, a, b,
    Cmplx th, th1, th2; // >   x, y, th, th1, th2
    Cmplx a = q.div(lambda);  log.dbg("a=q / alp=", a);      //  a = q / alp
    Cmplx b = a.mult(a);       log.dbg("b=a**2=", b);      //  b = a**2
//c     y = sin(theta)
//c     where sin(theta) = a / (b + 0.25)
    Cmplx y = b.add(0.25);   log.dbg("y=", y);      //  y = b + 0.25d0
    if (y.abs() > eps) {//  if (abs(y) .gt. eps) then
      Cmplx x = b.add(- 0.25).div(y);   log.dbg("x=", x);      //     x = (b - 0.25d0) / y
      y = a.div(y);            log.dbg("y=a / y=", y);      //     y = a / y
      th = x.add(y.mult(Cmplx.i)); log.dbg("th=x + onei*y=", th);    //     th = x + onei*y
      c4 = th.pow(-2);      log.dbg("c4=th**(-2)=", c4);    //     c4 = th**(-2)
      if (c4.abs2() == 1) {
        int dgb = 1;
      }

      th1 = Cmplx.ONE.div((y.times(2.)).pow(L)); log.dbg("th1=", th1); //     th1 = (1d0, 0d0) /
      // >      (2d0**L  * exp(- dacos(-1d0) * 0.5d0 * t) * th**(onei*t) * y**L)

      //[29Jun2011] added jmZ
      Cmplx c6 = (t.times(Math.PI * 0.5)).exp();   // exp(PI/2 * t)
      c6 = c6.times(th.pow(t.times(Cmplx.i)));     // exp(-THETA * t) = [exp(i THETA) ]^{i t}
      th1 = th1.times(c6);

      th2 = th.pow(-n-1);   log.dbg("th2=", th2);  //     th2 = th**(-n-1)
    } else {//  else
      throw new IllegalArgumentException(log.error("NOT TESTED !(y.abs() > eps)"));
//      th = y.div(b.add(-0.25).add(a.multFirst(Cmplx.i))); log.dbg("th=", th);//     th = y / ((b - 0.25d0) + onei * a)
//      y  = y.div(a);           log.dbg("y=", y);      //     y  = y / a
//      c4 = th.pow(2);       log.dbg("c4=", c4);    //     c4 = th**2
//      th1 = (y.timesSelf(0.5)).pow(L); log.dbg("th1=", th1);//     th1 = (1d0, 0d0) /
//      // >      (2d0**L  * exp(- dacos(-1d0) * 0.5d0 * t) ) * y**L
//      th2 = th.pow(n+1);    log.dbg("th2=", th2);  //     th2 = th**(n+1)
    }//  end if
    c2 = mit.add(L + 1);       log.dbg("Cmplx(L + 1-it)=", c2);     //  c2 = dcmplx(L + 1, 0) - onei * t
    gm = CmplxGamma.calc(c2);  log.dbg("WGamma(c2)=", gm);       //  gm = WGamma(c2)
    gm = gm.div(gm.abs());     log.dbg("gm / (zabs(gm)=", gm);   //  calc: gm / (zabs(gm)
    c1 = mit.add(-L);          log.dbg("c1=Cmplx(-L-it)=", c1);        //  c1 = dcmplx(-L, 0) - onei * t
    c2 = new Cmplx(n+1);       log.dbg("c2=Cmplx(n+1)=", c2);       //  c2 = dcmplx(n+1, 0)
    c3 = mit.add(n+L+2);       log.dbg("c3=Cmplx(n+L+2-it)=", c3);     //  c3 = dcmplx(n+L+2, 0) - onei * t
    log.dbg("Cmplx2F1.calc(c1, c2, c3, c4=", c4);
    c5 = Cmplx2F1.calc(c1, c2, c3, c4, eps); log.dbg("2F1=", c5);//  call F_21_jm (c1, c2, c3, c4, c5, eps)

    // calc: -dexp(fl(n)) / WGamma(c3)
    c3 = CmplxGamma.calc(c3).log();   log.dbg("ln(WGamma(c3))=", c3); // NOTE log!!! ln(Gamma(c3))
    c3 = new Cmplx(fLog.calc(n)).minus(c3);  log.dbg("fl(n))-ln(WGamma(c3))=", c3); // ln(n!) - ln(Gamma(c3))
    c3 = c3.exp().times(-1);    log.dbg("-dexp(fl(n)) / WGamma(c3)=", c3); // calc: -dexp(fl(n)) / WGamma(c3)

    c5 = c3.mult(gm).mult(th1).mult(th2).mult(c5); log.dbg("res=", c5);//  c5 = -dexp(fl(n)) * gm / (zabs(gm) * WGamma(c3)) * th1 * th2 * c5
//  s_n = Imag(c5)
//  c_n = dble(c5)
    return c5;//  return
  }//  end

/*
c-----------------------------------------------------------------------------
     subroutine jmtrx (r1, r2, n, m, atomZ, L, q, alp, fl, nfl)
c     J-matrix, J.Phys.B9, 1491 (1976)
     integer n, m, L, nfl
     real*8 alp, atomZ, ddl, r1, r2, fl(0:nfl)
     complex*16 q, a, b, c, y, yx
c
     a = q / alp
     b = a**2
c$$$      x = (b - 0.25d0) / (b + 0.25d0)
c     y = a/sin(theta) * alp/2
c     where sin(theta) = a / (b + 0.25)
     y  = (b + 0.25d0) * alp * 0.5d0
     yx = (b - 0.25d0) * alp * 0.5d0
c
     c = dcmplx(dexp(fl(n + 2 * L + 1) - fl(n)), 0d0) * (
    >   dcmplx(atomZ * ddl(n, m), 0d0) -
    >   (yx * dcmplx(2d0 * dble(n + L + 1) * ddl(n, m), 0d0) -
    >   y * dcmplx(dble(n) * ddl(m, n-1) + dble(n + 2 * L + 2)
    >   * ddl(m, n+1),0d0)))
     r1 = dble(c)
     r2 = imag(c)
     return
     end
*/
//  subroutine jmtrx (r1, r2, n, m, atomZ, L, q, alp, fl, nfl)
//c     J-matrix, J.Phys.B9, 1491 (1976)
//  integer n, m, L, nfl
//  real*8 alp, atomZ, ddl, r1, r2, fl(0:nfl)
//  complex*16 q, a, b, c, y, yx
  public static Cmplx jmtrx(int n, int m, int L, Cmplx q, double lambda, int jmZ) {
    Cmplx a = q.div(lambda); // a = q / alp
    Cmplx b = a.mult(a); // b = a**2
//c$$$      x = (b - 0.25d0) / (b + 0.25d0)
//c     y = a/sin(theta) * alp/2
//c     where sin(theta) = a / (b + 0.25)
    Cmplx y  = b.add(0.25).times(lambda * 0.5);//y  = (b + 0.25d0) * alp * 0.5d0
    Cmplx yx = b.add(-0.25).times(lambda * 0.5); //yx = (b - 0.25d0) * alp * 0.5d0
//c
    double d2 = Math.exp(fLog.calc(n + 2 * L + 1) - fLog.calc(n)); //c = dcmplx(dexp(fl(n + 2 * L + 1) - fl(n)), 0d0) * (
    //   >   dcmplx(atomZ * ddl(n, m), 0d0) -
    double d3 = -2. * (n + L + 1) * Mathx.dlt(n, m);//>   (yx * dcmplx(2d0 * dble(n + L + 1) * ddl(n, m), 0d0) -
    double d4 = (double)n * Mathx.dlt(m, n-1) + (n + 2 * L + 2) * Mathx.dlt(m, n+1);//   >   y * dcmplx(dble(n) * ddl(m, n-1) + dble(n + 2 * L + 2)
    //   >   * ddl(m, n+1),0d0)))
    Cmplx c = yx.times(d3).add(y.times(d4));
    double zz = jmZ * Mathx.dlt(n, m);
    c = c.add(zz);
    return c.times(d2);    // return;
  }

}
