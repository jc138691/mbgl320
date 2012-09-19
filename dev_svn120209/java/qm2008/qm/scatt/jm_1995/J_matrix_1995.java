package scatt.jm_1995;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 15:20:41
 */
public class J_matrix_1995 {
}
/*
c
C     J_matrix.f  Calculates scattering via j-matrix method for given states
c     j-matrix references to the papers:
c     I  - J.T.Broad and W.P.Reinhardt, J.Phys.B9, 1491 (1976).
c     II - H.A.Yamani and L.Fishman, J.Math.Phys.16, 410 (1975).
c
Cr    Remarks: description of
Cr    j,
Cr    Ncore, nc, lc,
Cr    see target.f or jm.tex
c
      subroutine J_matrix (
c     In:
     >   E_0, n_E0, LL, LS, eng, engt, U, zatom, eps,
     >   alagl, lwf, njml, nexcl, i_test, i_printout, n_print_g,
     >   igmax, imaxLS, m_orb, fl, nfl, ig_igm, igm_jm, l_igm,
c     Out:
     >   tg, cs)
      include 'par.jm.f'
      integer i_printout, n_print_g, n_E0, imaxLS,
     >   LL, LS, i2, igm, igm1, ig1, ig, nfl, i_o(igm_par)
      include 'formats.jm.f'
c
      integer m_orb(0:Lmax_par), i_E0
      real*8 eng(i_par), engt(igm_par), E_0(n_E0), E0,
     >   U(igm_par, i_par),
     >   s_n(igm_par), s_n1(igm_par), fl(0:nfl), pi, pi2, pi4, pi05
c
      real*8 zatom, eps, alagl(0:Lmax_par), D0
      integer i_test, i_printout, L1, L, i1, i, i_test_jm,
     >   lwf, njml(0:lwf), nexcl(0:lwf),
     >   ig_igm(igm_par), igm_jm(igm_par),  igm_i(igm_par), l_igm(igm_par),
     >   igmax
c
c     Temporary: tmp-variables, tmpv-vectors, tmpa-arrays
      real*8 tmp1, tmp2,  tmp3,  tmp4
      complex*16 ctmpv(igm_par), ctmpa(igm_par, igm_par),
     <   ctmp, ctmp1
c
c     External calls: int2_8
      real*8 ddl
C
      integer ipvt(igm_par), imax
      real*8 eps100, eps10, cs(igm_par, 0:Lmax_par, n_E0)
c
      complex*16 tg(igm_par, 0:Lmax_par, n_E0),
     >   q(igm_par), zeroc, onec, onei,
     >   wsjs(igm_par, igm_par), wcjc(igm_par, igm_par),
     >   Rgg(igm_par, igm_par), c_n(igm_par), c_n1(igm_par), cjm(igm_par)
c     Functions
      complex*16 const_1
c
      i_test_jm = 2
c+++ dak 6.94 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      D0 = 0d0
      pi   = dacos(-1d0)
      pi2  = pi * 2d0
      pi4  = pi * 4d0
      pi05 = pi * 0.5d0
      zeroc = (0d0, 0d0)
      onec = (1d0, 0d0)
      onei = (0d0, 1d0)
      eps10  = 10d0  * eps
      eps100 = 100d0 * eps
c
c
c     Define what channels are used
c
      imax = 0
      do igm = 1, igmax
         if (igm_jm(igm) .eq. 1) then
            imax = imax + 1
            igm_i(imax) = igm
         end if
      end do
c
c+++ dak 7.94 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
C
C     Progectile-energy dependent part
C
      do i_E0 = 1, n_e0
         E0 = E_0(i_E0) + engt(1)
c
         if (i_printout .gt. 0) then
            if (i_E0 .eq. 1)  print*, 'LL, LS = ', LL, LS,
     >         '  Incident electron energies (a.u.)'
c$$$            print*, i_E0, real(E_0(i_E0))
            print 1, i_E0
 1          format (I4, $)
            call flush (6)
         end if
c
c     Define Gamma-quantum numbers also
c     Define J-matrixes together with S_n, C_n -coefficients for (Eq.3.19).
c     Note: cjm-doesn't depend on \Gamma
c     Sums by the total Target L and 2S+1
c
         do i1 = 1, imax
            igm1 = igm_i(i1)
            L1 = l_igm(igm1)
c
            cjm(i1) = 0d0
            tmp1 = E0 -  engt(ig_igm(igm1))
c
c     Identify open and closed continuum state
            if(tmp1 .gt. 0d0) then
               i_o(i1) = 1
c
c     Define k_\gamma - momentum of a \Gamma channel
               q(i1) = dcmplx(dsqrt(2d0 * tmp1), 0d0)
               call jmtrx(tmp3, tmp4, njml(L1) - 1, njml(L1), 0d0, L1,
     >            q(i1), 2d0 * alagl(L1), fl, nfl)
               cjm(i1) = dcmplx(tmp3, 0d0)
c
               call sc_n (s_n(i1), tmp2, njml(L1) - 1, 0d0, L1,
     >            q(i1), 2d0 * alagl(L1), eps, fl, nfl)
               c_n(i1) = dcmplx(tmp2, D0)
c
               call sc_n (s_n1(i1), tmp2, njml(L1), D0, L1,
     >            q(i1), 2d0 * alagl(L1), eps, fl, nfl)
               c_n1(i1) = dcmplx(tmp2, D0)
            else
               i_o(i1) = 0
c
c     For a closed channel \Gamma, C is replaced by C+iS and evaluated
c     at q=i|k_\gamma|, see (Eq.4.15) of II
               q(i1) = dcmplx(0d0, dsqrt(dabs(2d0 * tmp1)) )
               call jmtrx(tmp3, tmp4, njml(L1)-1, njml(L1), D0, L1,
     >            q(i1), 2d0 * alagl(L1), fl, nfl)
               cjm(i1) = dcmplx(tmp3, tmp4)
c
               call sc_n (tmp1, tmp2, njml(L1)-1, D0, L1,
     >            q(i1), 2d0 * alagl(L1), eps, fl, nfl)
               s_n(i1) = D0
               c_n(i1) = dcmplx(tmp2, tmp1)
c
               call sc_n (tmp1, tmp2, njml(L1), D0, L1,
     >            q(i1), 2d0 * alagl(L1), eps, fl, nfl)
               s_n1(i1) = D0
               c_n1(i1) = dcmplx(tmp2, tmp1)
            end if
         end do
c
c     Print s_n, s_n1, c_n, c_n1 for debugging
c
         if (i_printout .gt. i_test_jm) then
            write(*, *)    ' s_n(i1), s_n1(i1)'
            do i1 = 1, imax
               write(*, 225) s_n(i1), s_n1(i1)
            end do
            write(*, *)    ' c_n(i1), c_n1(i1)'
            do i1 = 1, imax
               write(*, 225) c_n(i1), c_n1(i1)
            end do
         end if
c
c     Calculate and store Wgg-matrix, (Eq.3.20) of I
c
         do i1 = 1, imax
            igm1 = igm_i(i1)
            do i = 1, imax
               igm = igm_i(i)
               Ctmpa(i1, i) = 0d0
c
c     Sum by H-total
               do i2 = imaxLS, 1, -1
                  if(abs(eng(i2) - E0) .lt. eps) then
                     print*, 'eng(i2)=', eng(i2), 'E0=', real(E0)
                     print*, 'Check Wgg-calculation'
                  end if
c
c     Sums by n and n' (I.3.20) disappear because of D=\delat_N,n * Pnl
c
                  Ctmpa(i1, i) = Ctmpa(i1, i) +
     >               U(igm1, i2) * U(igm, i2) / (eng(i2) - E0)
               end do
c
               Ctmpa(i, i1) = Ctmpa(i1, i)
c
            end do
         end do
c
c     Define WSJS and WCJC - complex matrixs, (Eq.3.18 and Eq.3.19) of I.
c
         do i1 = 1, imax
            do i = 1, imax
c
               wcjc(i1, i) = (ctmpa(i1, i) *
     >            cjm(i) * c_n1(i)  + ddl(i1, i) * c_n(i) )
     >            / (abs(c_n1(i)) + abs(c_n(i)))
c
c     Check if the channel is open
c
               if (i_o(i) .eq. 1) then
                  wsjs(i1, i) = (ctmpa(i1, i) *
     >               cjm(i) * s_n1(i) + ddl(i1, i) * s_n(i) )
     >               / (abs(s_n1(i)) + abs(s_n(i)))
               else
                  wsjs(i1, i) = zeroc
               end if
c
            end do
         end do
c
c
c     Print W (ctmpa), WSJS and WCJC -matrixes for debugging
c
         if (i_printout .gt. i_test_jm) then
c
            write(*, *)  'U:'
            do i1 = 1, imax
               igm1 = igm_i(i1)
               write(*, 225)    (U(igm1, i), i = 1, imaxLS)
            end do
c
            write(*, *)  'W:'
            do i1 = 1, imax
               write(*, 225)    (ctmpa(i1, i), i = 1, imax)
            end do
c
            write(*, *)  'WSJS:'
            do i1 = 1, imax
               write(*, 225)    (wsjs(i1, i), i = 1, imax)
            end do
c
            write(*, *)  'WCJC:'
            do i1 = 1, imax
               write(*, 225)    (wcjc(i1, i), i = 1, imax)
            end do
         end if
c
c     Invert WCJC
         call zgeco (wcjc, igm_par, imax, ipvt, tmp1, ctmpv)
         if (abs(tmp1) .lt. eps)   then
            print*,     'Check zgeco #1, input matrix is ill conditioned', tmp1
         end if
         call zgedi (wcjc, igm_par, imax, ipvt, ctmp, ctmpv, 1)
c
c     Calculate Rgg- matrix, (Eq.3.17) of I.
c
         do i1 = 1, imax
            ctmp1 = const_1 (pi05, q(i1))
            do i = 1, imax
c
               Rgg(i1, i) = zeroc
c
c     Check if the channel is open
c
               if (i_o(i) .eq. 1) then
c
                  do i2 = 1, imax
                     Rgg(i1, i) = Rgg(i1, i) - wcjc(i1, i2) * wsjs(i2, i)
                  end do
c
                  ctmp = const_1 (pi05, q(i))
                  Rgg(i1, i) = Rgg(i1, i) * (ctmp / ctmp1)
     >               * ((abs(s_n1(i)) + abs(s_n(i)))
     >               / (abs(c_n1(i1)) + abs(c_n(i1))) )
               end if
            end do
         end do
c
c     Phase-shifts for testing
c
         do i1 = 1, imax
            if (i_test .gt. i_test_jm .and. i1 .lt. 4) then
               tmp1 = atan(dble(Rgg(i1, 1)))
c$$$               if (tmp1 .lt. 0d0)   tmp1 = tmp1 + pi
c$$$               if (i_E0 .eq. 1)  write(1100+i1, *) LL, LS, ' LL, LS'
c$$$               write(1100+i1, 200) real(dble(q(1)**2)), real(tmp1)
               write(1100+i1, 200) real(dble(q(1))), real(tmp1)
            end if
         end do
c
c     Calculate Sgg-matrix (Eq.4.12a and Eq.4.12b) of II.
c
c     Using wcjc is a working array
         do i1 = 1, imax
            do i = 1, imax
               if (i_o(i) * i_o(i1) .eq. 1) then
                  wcjc(i1, i) = onec * ddl(i1, i) - onei*Rgg(i1,i)
               else
                  wcjc(i1, i) = onec * ddl(i1, i)
               end if
            end do
         end do
c
c     Print R-matrix(Rgg) for debugging
c
         if (i_printout .gt. i_test_jm) then
c
            write(*, *)  'R:'
            do i1 = 1, imax
               write(*, 225)    (Rgg(i1, i), i = 1, imax)
            end do
c
            write(*, *)  '1-i*R:'
            do i1 = 1, imax
               write(*, 225)    (wcjc(i1, i), i = 1, imax)
            end do
c
         end if
c
c     Invert wcjc
         call zgeco (wcjc, igm_par, imax, ipvt, tmp1, ctmpv)
         if (abs(tmp1) .lt. eps)   then
            print*,  'Check zgeco #2, input matrix is ill conditioned', tmp1
         end if
         call zgedi (wcjc, igm_par, imax, ipvt, ctmp, ctmpv, 1)
c
c     (1-S)= -2iR(1-iR)^(-1)     matrix is stored in ctmpa temporarily
         tmp1 = 0d0
         do i1 = 1, imax
            igm1 = igm_i(i1)
            l1 = l_igm(igm1)
            ig1 = ig_igm(igm1)
            do i = 1, imax
               igm = igm_i(i)
               l = l_igm(igm)
               ig  = ig_igm(igm)
c
               ctmpa(igm1, igm) = zeroc
c
c     ig controls excitation, 1 means excitaion from the ground state
               if (i_o(i1) * i_o(i) .eq. 1 .and. ig .eq. 1) then
                  tg(igm1, l, i_E0) = zeroc
                  do i2 = 1, imax
                     if (i_o(i2) .eq. 1) then
                        tg(igm1, l, i_E0) = tg(igm1, l, i_E0) -
     >                     onei * 2d0 * Rgg(i1, i2) * wcjc(i2,i)
                     end if
                  end do
c$$$c
c$$$C     Define cross sections from the S-matrix, (I.4.2)
c$$$c
c$$$                  cs(igm1, igm, i_E0) = pi / dble(q(i))**2 *
c$$$     >               dble((2 * LL + 1) * LS) * zabs(tg(igm1,igm,i_E0))**2
c
C     Define the T-matrix from the S-matrix and store it in tg, (4.2.2)
c
                  tg(igm1, l, i_E0) = tg(igm1, l, i_E0)
     >               / (onei * pi2 * dsqrt(zabs(q(i1) * q(i)) ) )
c
c     Tests
c$$$                  tg(igm1, igm, i_E0) = (1d0, 0d0)
c$$$                  if(LL .ne. 0)  tg(igm1, igm, i_E0) = (0d0, 0d0)
c     Grand total dcr.section is calculated via Optical Theorem
                  if (igm .eq. 1 .and. igm1 .eq. 1)  tmp2 = -pi2**2
     >               * Imag(Tg(1, L, i_E0)) / q(1) * dble((2*LL+1)*LS)
c
c     Save T-matrix
                  if (i_E0 .le. 1) then
                     write(700+ig1, 330) ig1, ig, l_igm(igm1), l_igm(igm),
     >                  LL, LS, tg(igm1, l, i_E0)
                  end if
c
c     store new T as (2*pi)^2 * K_igm'/K_igm * sqrt(2S+1) * T
                  tg(igm1, l, i_E0) = tg(igm1, l, i_E0) * pi2**2
     >               * dsqrt(dble(LS) * zabs(q(i1)) / zabs(q(i)) )
C     Define cross sections from the T-matrix
                  cs(igm1, l, i_E0) = dble(2*LL+1)
     >               * zabs(tg(igm1, l, i_E0))**2 / pi4
c
c     Find partial the grand total cross section
                  if (ig .eq. 1) tmp1 = tmp1 + cs(igm1, l, i_E0)
               end if
c
            end do
         end do
c
c     Test that Sum of all cross sections = Total C.S.
c     calculated by the Optical Theorem
         if (abs(tmp1/tmp2-1d0) .gt. eps)
     >      print*, ' WARNING: Sum_SDCS/Total=', tmp1/tmp2
c
c     This is the end of E0-loop
      end do
c
      return
      end

*/
