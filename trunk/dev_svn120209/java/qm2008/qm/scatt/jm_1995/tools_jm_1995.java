package scatt.jm_1995;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 15:14:09
 See JmTools for java version
 */
public class tools_jm_1995 {
}
/*
      subroutine check_par (n, n_par, ch, ch_par)
      integer n, n_par
      character*10 ch, ch_par
      if (n .gt. n_par) then
         print*,  '    Increase ', ch_par, ' from ', n_par, ' to ', n
         print*,  ' or Decrease ', ch, ' from ', n, ' to ', n_par
         stop ' memory is not allocated for some arrays'
      end if
      return
      end
c--------------------------------------------
      function const_1 (pi05, q)
      complex*16 const_1, q
      real*8 pi05
c$$$      const_1 = (dcmplx(pi05, 0d0) * q)**(-0.5d0)
      const_1 = dcmplx( (pi05 * abs(q))**(-0.5d0), 0d0 )
      return
      end
c
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
c---------------------------------------------------------------------
c     delta-function
      real*8 FUNCTION dnl (n1, l1, n2, l2)
      integer l1, l2, n1, n2
      dnl = 0d0
      if (n1 .eq. n2 .and. l1 .eq. l2)  dnl = 1d0
      return
      end
c---------------------------------------------------------------------
c     delta-function
      FUNCTION idnl (n1, l1, n2, l2)
      integer idnl, l1, l2, n1, n2
      idnl = 0
      if (n1 .eq. n2 .and. l1 .eq. l2)  idnl = 1
      return
      end
c---------------------------------------------------------------------
c     delta-function
      real*8 FUNCTION ddl (i, j)
      integer i, j
      ddl = 0d0
      if (i .eq. j)  ddl = 1d0
      return
      end
c-----------------------------------------------------------------
c     Integer delta-function
      integer FUNCTION idl (i, j)
      integer i, j
      idl = 0
      if (i .eq. j)  idl = 1
      return
      end
c-----------------------------------------------------------------
c     Theta-function
      real*8 FUNCTION dth (i)
      integer i
      dth = 0d0
      if (i .ge. 0)  dth = 1d0
      return
      end
c-------------------------------------------------------------------------
      subroutine F_21_jm (a, b, c, z, res, eps)
      real*8  eps
      complex*16 a, b, c, z, t1, a1, b1, c1, f, one, res
      one = dcmplx(1d0, 0d0)
      res = one
c
c     If  z.gt.1, there is another formular with (1/z)^n, but it diverges!!
      f = one
      t1 = a * b * z / c
      a1 = a
      b1 = b
      c1 = c
      do while (real(abs(t1)) .gt. eps)
         res = res + t1
         a1 = a1 + one
         b1 = b1 + one
         c1 = c1 + one
         f  = f  + one
         t1 = t1 * a1 * b1 * z / (c1 * f)
      end do
      return
      end
c------------------------------------------------------------

 */
