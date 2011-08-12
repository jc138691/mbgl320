package scatt.jm_1995;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 15:15:04
 */
public class tools_hm {
//  C=====August,1990===========================================================
//  C            Analytic Independent-Particle Model for Atoms.
//  C            Alex E. S. at al, Phys.Rev., v.184, 1969, p.1
//  C
//  C            -Z0/r is removed from this potential!
//  C             -----------------------------------
//  C
//  C=====By D.A.Konovalov======================MADE IN AUSTRALIA===============
//        subroutine ipmhf (Z, Z0, v, grid, nr, expcut)
//        integer i, jump, nr, NZ, natom, n
//        parameter (natom = 19)
//        implicit real*8 (a-h, o-z)
//        dimension v(nr), grid(nr)
//  c
//  c INPUT:
//  c -----
//  c  Z     - charge of the atom
//  c  grid  - r-grid, nr - number of points in this grid
//  C  expcut- the smollest value of exp-functions in "IPMDHF.F"
//  C  Z0    - Coulomb potential removed from the potential V.
//  C          V(r) = Vatom(r)  -  Z0 / r     mult-ed by r if  Z.NE.Z0
//  c
//  c OUTPUT:
//  c ------
//  c  V - Array of the values of the potential multiplyed by r  if  Z.NE.Z0
//  c
//        dimension dn(natom)
//        data (dn(n), n = 1, natom) /1.0d-30, 0.215d0, 0.563d0, 0.858d0
//       >,0.979d0, 0.880d0, 0.776d0, 0.708d0, 0.575d0, 0.500d0, 0.561d0
//       >,0.621d0, 0.729d0, 0.817d0, 0.868d0, 0.885d0, 0.881d0, 0.862d0
//       >,1.006d0/
//        NZ = nint(Z)
//  c
//  c     Do same check
//  c
//        if ((NZ .lt. 1) .or. (NZ .gt. Natom)) then
//           print*, 'This Z is not available yet,  Z=', Z
//           print*, ' Max Z=', Natom
//           stop    'Stop in "ipmdhf.f"'
//        end if
//  c
//  c     Some constants
//  c
//        dd = dn(NZ)
//        hh = dd * (Z - 1d0)**0.4d0
//  c
//  c     Loop by r-grid
//  c
//        jump = 0
//        do i = 1, nr
//           r = grid(i)
//           r1 = 1d0 / r
//           v(i) = Z0 - 1d0
//           if (jump .eq. 0) then
//              dexpr = dexp(- r / dd)
//              if (dexpr .lt. expcut)  then
//                 jump = i
//              else
//                 teta = dexpr / (hh * (1d0 - dexpr) + dexpr)
//                 v(i) = - ((Z - 1d0)* teta + 1d0) + Z0
//              end if
//           end if
//        end do
//  c
//  c     Put back 1/r if Z.EQ.Z0
//  c
//        if (nint(Z) .eq. nint(Z0))  then
//           do i = 1, nr
//              v(i) = v(i) / grid(i)
//           end do
//        end if
//        return
//        end
//
//  c-------------------------------------------------------------------------
//  c
//  c     Define such matrix element is possible
//  c
//        function irrss (Qnl, Nnl, Lnl, j1, j2, nsh, ii)
//        include 'par.jm.f'
//        integer irrss, itmp1, itmp2, idnl,
//       >   Qnl(j_par, Nshells_par),
//       >   Nnl(j_par, Nshells_par),
//       >   Lnl(j_par, Nshells_par),
//       >   ib, ib2, nsh, idl, j1, j2, ii(4), n1, n2, l1, l2, i
//  c
//        irrss = 0
//  c
//        do ib = 1, nsh
//           itmp1 = Qnl(j1, ib) - idl(ib, ii(1)) - idl(ib, ii(2))
//           itmp2 = Qnl(j2, ib) - idl(ib, ii(3)) - idl(ib, ii(4))
//  c
//  c     Check if there are enough electrons for \sigma's and \rho's
//           if (itmp1 .lt. 0 .or. itmp2 .lt. 0) return
//  c
//  c     Check if two configurations have the same spectator electrons, p.A69_Fano
//  c
//           if (ib .eq. 1 .and. itmp1 .ne. itmp2) return
//  c
//           if (itmp1 .gt. 0 .and. ib .gt. 1) then
//              n1 = Nnl(j1, ib)
//              l1 = Lnl(j1, ib)
//              i = 0
//              do ib2 = 2, nsh
//                 n2 = Nnl(j2, ib2)
//                 l2 = Lnl(j2, ib2)
//                 itmp2 = Qnl(j2, ib2) - idl(ib2, ii(3)) - idl(ib2, ii(4))
//                 if (idnl(n1, l1, n2, l2) .eq. 1 .and. itmp1 .eq. itmp2)   i = 1
//              end do
//              if (i .eq. 0) return
//           end if
//        end do
//  c
//        irrss = 1
//  c
//        return
//        end
//  c-------------------------------------------------------------------------
//  c
//  c     Copy shells
//  c
//        subroutine copysh(Qnl, Nnl, Lnl, LLnl, LSnl, j1, j2, i1, i2, k1, k2)
//        include 'par.jm.f'
//        integer j1, j2, i1, i2, k1, k2
//        integer
//       >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
//       >   Qnl(j_par, Nshells_par, 2),
//       >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2)
//  c
//        Qnl(j2, i2, k2)  = Qnl(j1, i1, k1)
//        Nnl(j2, i2, k2)  = Nnl(j1, i1, k1)
//        Lnl(j2, i2, k2)  = Lnl(j1, i1, k1)
//        LLnl(j2, i2, k2) = LLnl(j1, i1, k1)
//        LSnl(j2, i2, k2) = LSnl(j1, i1, k1)
//  c
//        return
//        end
//  c
//  c
//  c     Create empty shell
//  c
//        subroutine emptysh(Qnl, Nnl, Lnl, LLnl, LSnl, j, i, k)
//        include 'par.jm.f'
//        integer j, i, k
//        integer
//       >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
//       >   Qnl(j_par, Nshells_par, 2),
//       >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2)
//
//        Qnl(j, i, k) = 0
//        Nnl(j, i, k) = 0
//        Lnl(j, i, k) = 0
//        LLnl(j, i, k) = 0
//        LSnl(j, i, k) = 1
//
//        return
//        end
//  c
//  c     Check if j's configuration exists already
//  c
//  c     newcon = 1, if this configuration is new
//        function newcon (Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, j, k, nsh)
//        include 'par.jm.f'
//        integer j, m, i, ii, k, nsh, newcon
//        integer
//       >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
//       >   Qnl(j_par, Nshells_par, 2),
//       >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2),
//       >   LL12(j_par, 2), LS12(j_par, 2)
//        newcon = 1
//  c
//        do i = 1, j - 1
//           if (LL12(i, k) .eq. LL12(j, k) .and. LS12(i, k) .eq. LS12(j, k)) then
//  c
//  c     Check all shells
//              ii = 0
//              do m = 1, nsh
//                 if(Qnl(i, m, k) .eq. Qnl(j, m, k) .and.
//       >            Nnl(i, m, k) .eq. Nnl(j, m, k) .and.
//       >            Lnl(i, m, k) .eq. Lnl(j, m, k) .and.
//       >            LLnl(i, m, k) .eq. LLnl(j, m, k) .and.
//       >            LSnl(i, m, k) .eq. LSnl(j, m, k) ) then
//                    ii = ii + 1
//                 end if
//              end do
//  c
//  c     all nsh-shells are the same
//              if (ii .eq. nsh) then
//                 newcon = 0
//                 return
//              end if
//  c
//           end if
//        end do
//        return
//        end
//  c
//  c     Triangle rule
//  c
//        function itri(i1, i2, i3)
//        integer itri, i1, i2, i3
//        itri = 1
//        if(iabs(i1 - i2) .gt. i3 .or.  i1 + i2 .lt. i3 .or.
//       >   iabs(i1 - i3) .gt. i2 .or.  i1 + i3 .lt. i2 .or.
//       >   iabs(i2 - i3) .gt. i1 .or.  i2 + i3 .lt. i1 )  itri = 0
//        return
//        end
//  c---------------------------------------------------------------------
//  c
//  c     Memory position for a symmetric array
//  c
//        function msym (i1, i2, n)
//        integer msym, i, j, n, i1, i2
//        i = max0(i1, i2)
//        j = min0(i1, i2)
//        msym = ((2 * n + 2 - j) * (j - 1)) / 2 + (i - j + 1)
//        return
//        end
//  c---------------------------------------------------------------------
//  c
//  c     Generate arrays for r (rn) and sqrt(r) (r5) with
//  c     a constant mesh size in the log(z*r) variable
//  c
//        subroutine rn_hm (
//  c     In
//       >   z, nr, rmin, rmax, IY,
//  c     Out
//       >   rn, r5, wn, h)
//  c     IY = 1, Weights are Simpson's otherwise they are Bode's
//  c
//        integer nr, i, IY
//        real*8 z, rmin, rmax, rn(nr), r5(nr), wn(nr), h, rho,
//       >   a(0:3), tmp
//  c
//  c  *****  set the starting point and the step size
//  c
//        rho = log(z * rmin)
//        h   = (log(z * rmax) - rho) / dble(nr - 1)
//        do i = 1, nr
//           rn(i) = dexp(rho) / z
//           r5(i) = dsqrt(rn(i))
//           rho = rho + h
//        end do
//        rho = rho - dble(nr) * h
//  c
//        if (IY .eq. 1) then
//  c     Simpson's weights
//           if (mod(nr,2) .eq. 0) stop ' nr must be odd, stoped in rn_hm'
//           tmp = 2d0 * h / 3d0
//           do i = 1, nr
//              wn(i) = dble(mod(i+1, 2) + 1) * tmp * rn(i)
//           end do
//           wn(1)  = wn(1) / 2d0
//           wn(nr) = wn(nr) / 2d0
//        else
//  c     Bode's weights
//           tmp = 2d0 * h / 45d0
//           a(1) = 14d0 * tmp
//           a(2) = 32d0 * tmp
//           a(3) = 12d0 * tmp
//           a(0) = 32d0 * tmp
//           do i = 1, nr
//              wn(i) = a(mod(i, 4))  * rn(i)
//           end do
//           wn(1)  = wn(1) / 2d0
//           wn(nr) = wn(nr) / 2d0
//        end if
//        return
//        end
//  C     ------------------------------------------------------------------
//  C
//  C     Stores Y^k (p1, p2; r) in the array Y(r)
//  C
//        SUBROUTINE yk_hm (
//  c     In
//       >   i1, i2, p1, p2, l1, l2, K, nr, rn, h, Zatom,
//  c     Out
//       >   y)
//        IMPLICIT REAL*8(A-H,O-Z)
//        real*8 p1(nr), p2(nr), rn(nr), y(nr)
//        integer L1, L2, nr, k, m, m1, m2, mm, i1, i2
//  c
//        EH = exp(-H)
//        h3 = h / 3d0
//  c
//  C     Calculates and stores Z^k (p1, p2; r) in the array Y(r)
//  c
//        DEN = dble(L1 + L2 + 3 + K)
//        FACT = (1d0 / dble(L1 + 1) + 1d0 / dble(L2 + 1)) / (DEN + 1d0)
//        A = EH**K
//        A2 = A * A
//        H90 = H / 90.D0
//        A3 = A2 * A * H90
//        AI = H90 / A
//        AN = 114d0 * A * H90
//        A34 = 34d0 * H90
//        F1 = rn(1)**2 * P1(1) * P2(1)
//        F2 = rn(2)**2 * P1(2) * P2(2)
//        F3 = rn(3)**2 * P1(3) * P2(3)
//        F4 = rn(4)**2 * P1(4) * P2(4)
//        y(1) = F1 * (1d0 + Zatom * rn(1) * FACT)/DEN
//        y(2) = F2 * (1d0 + Zatom * rn(2) * FACT)/DEN
//        y(3) = y(1) * A2 + H3 * (F3 + 4d0 * A * F2 + A2 * F1)
//        DO M = 5, nr
//           F5 = rn(M)**2 * P1(M) * P2(M)
//           y(M-1) = y(M-3) * A2 + (AN * F3 + A34 * (F4+A2 * F2)-F5 * AI-F1 * A3)
//           F1 = F2
//           F2 = F3
//           F3 = F4
//           F4 = F5
//        end do
//        y(nr) = A * y(nr-1)
//        if (i1 .eq. i2 .and. iabs(k) .eq. 0) then
//  C
//  C  *****  FOR Y0(I,I) SET THE LIMIT TO 1 AND REMOVE OSCILLATIONS
//  C  *****  INTRODUCED BY THE USE OF SIMPSON'S RULE
//  C
//           M2 = (nr / 2) * 2 - 1
//           M1 = M2 - 1
//           C1 = 1d0 - y(M1)
//           C2 = 1d0 - y(M2)
//           DO M = 1, M1, 2
//              y(M) = y(M) + C1
//              y(M+1) = y(M+1) + C2
//           end do
//           y(nr) = 1d0
//           y(nr-1) = 1d0
//        end if
//  C
//  C     calculate Y^k (p1, p2; r)
//  c
//        A = EH**(K+1)
//        C = 2 * K+1
//        A2 = A * A
//        H90 = C * H3 / 30d0
//        A3 = A2 * A * H90
//        AI = H90 / A
//        AN = 114.D0 * A * H90
//        A34 = 34.D0 * H90
//        F1 = y(nr) * EH**K
//        F2 = y(nr)
//        F3 = y(nr-1)
//        F4 = y(nr-2)
//        DO MM = 2, nr - 2
//           M = nr - MM
//           F5 = y(M-1)
//           y(M) = y(M+2) * A2
//       >      + (AN * F3 + A34 * (F4 + A2 * F2) - F5 * AI - F1 * A3)
//           F1 = F2
//           F2 = F3
//           F3 = F4
//           F4 = F5
//        end do
//        y(1) = y(3) * A2 + C * H3 * (F4 + D4 * A * F3 + A2 * F2)
//        RETURN
//        END
//  c--------------------------------------------------------------------
//  c
//  c     Scalar product,
//        real*8 function Sc_k (l1, l2, j1, j2, k, L, clkl, eps)
//        include 'par.jm.f'
//        real wign6j
//        real*8 clkl(0:Lmax_par, 0:k_par, 0:Lmax_par), eps
//        integer l1, l2, j1, j2, k, L
//  c$$$      call iot6 (L, l2, l1, k, j1, j2, sc_k)
//        sc_k = wign6j (real(L), real(l2), real(l1), real(k), real(j1), real(j2))
//  c
//        if (abs(sc_k) .lt. eps)            return
//        sc_k = dble((-1)**(j1 + l2 + L)) *
//       >   sc_k * clkl(l1, k, j1) * clkl(l2, k, j2)
//        return
//        end
//  c----------------------------------------------------------------------
//  c     itype =
//  c     0  -  (l1 l2) L12, l3 L | (l1, l2) L23, l3 L
//  c     1  -  (l1 l2) L12, l3 L | l1 (l2, l3) L23, L
//  c     10 -  (l1 l2) L12, l3 L | l1 (l3, l2) L23, L
//  c     2  -  (l1 l2) L12, l3 L | (l1, l3) L23, l2 L
//  c     20 -  (l1 l2) L12, l3 L | (l3, l1) L23, l2 L
//  c     3  -  (l1 l2) L12, l3 L | (l3, l2) L23, l1 L
//  c     30 -  (l1 l2) L12, l3 L | (l2, l3) L23, l1 L
//  c     4  -  (l1 l2) L12, l3 L | l1 (l3, l2) L23, L
//  c     5  -  (l1 l2) L12, l3 L | l3 (l2, l1) L23, L
//  c     50 -  (l1 l2) L12, l3 L | l3 (l1, l2) L23, L
//  c     6  -  l1 (l2 l3) L12, L | l3 (l2, l1) L23, L
//  c     60 -  l1 (l2 l3) L12, L | l3 (l1, l2) L23, L
//  c     61 -  l1 (l2 l3) L12, L | l1 (l3, l2) L23, L
//  c     62 -  l1 (l2 l3) L12, L | l2 (l3, l1) L23, L
//  c     620-  l1 (l2 l3) L12, L | l2 (l1, l3) L23, L
//  c     7  -  (l1 l2) L12, l3 L | l2 (l3, l1) L23, L
//  c     70 -  (l1 l2) L12, l3 L | l2 (l1, l3) L23, L
//  c     8  -  (l1 l2) L12, l3 L | (l2, l1) L23, l3 L
//  c
//  c     Recoupling
//        real*8 function U_6j (itype, L12, L23, l1, l2, l3, L, eps, IY)
//        real*8 eps, ddl
//        integer l1, l2, l3, L12, L23, L, IY, itype, j1, j2, j3
//        real  f1, f2, f3, F12, F23, F, phase, JAHNUF
//        U_6j = 0d0
//        if (itype .eq. 0) then
//           U_6j = ddl(L12, L23)
//           return
//        end if
//        if (IY .eq. 1) then
//  c
//           if(itype .eq. 1) then
//              j1 = l1
//              j2 = l2
//              j3 = l3
//              phase = 1.0
//           else if (itype .eq. 2) then
//              j1 = l2
//              j2 = l1
//              j3 = l3
//              phase = (-1.0)**(l1 + l2 - L12 + l2 + L23 - L)
//           else
//              stop ' not ready, U_6j for l1,l2,l3'
//           end if
//
//           call iot6 (j1, j2, L12, j3, L, L23, U_6j)
//  c
//           if (abs(U_6j) .lt. eps)   return
//           U_6j = U_6j * dble((-1)**(j1 + j2 + j3 + L))
//       >      * dsqrt(dble((2 * L12 + 1) * (2 * L23 + 1))) * phase
//        else
//           f1 = real(l1 - 1) / 2.0
//           f2 = real(l2 - 1) / 2.0
//           f3 = real(l3 - 1) / 2.0
//           F12 = real(L12 - 1) / 2.0
//           F23 = real(L23 - 1) / 2.0
//           F = real(L - 1) / 2.0
//           U_6j = JAHNUF (f1, f2, F, f3, F12, F23)
//  c
//           if (itype .eq. 1) then
//              return
//           else if (itype .eq. 10) then
//              U_6j = U_6j * (-1.0)**nint(1.0 - F23)
//           else if (itype .eq. 2) then
//              U_6j = U_6j * (-1.0)**nint(1.0 - F12 + 0.5 + F23 - F)
//           else if (itype .eq. 20) then
//              U_6j = U_6j * (-1.0)**nint(1.0 - F12 + 0.5 + F23 - F + 1.0 - F23)
//           else if (itype .eq. 3) then
//              U_6j = U_6j * (-1.0)**nint(F23 + 0.5 - F + 1.0 - F23)
//           else if (itype .eq. 30) then
//              U_6j = U_6j * (-1.0)**nint(F23 + 0.5 - F)
//           else if (itype .eq. 4) then
//              U_6j = U_6j * (-1.0)**nint(1.0 - F23)
//           else if (itype .eq. 5) then
//              U_6j =  ddl(L12, L23) * (-1.0)**nint(1.0 - F23 + 0.5 + F23 - F)
//           else if (itype .eq. 50) then
//              U_6j =  ddl(L12, L23) * (-1.0)**nint(0.5 + F23 - F)
//           else if (itype .eq. 6) then
//              U_6j = U_6j * (-1.0)**nint(0.5 + F12 - F + 1.0 - F12)
//           else if (itype .eq. 60) then
//              U_6j = U_6j * (-1.0)**nint(0.5 + F12 - F + 1.0 - F12 + 1.0 - F23)
//           else if (itype .eq. 61) then
//              U_6j =  ddl(L12, L23) * (-1.0)**nint(1.0 - F23)
//           else if (itype .eq. 62) then
//              U_6j =  U_6j * (-1.0)**nint(0.5 + F12 - F)
//           else if (itype .eq. 620) then
//              U_6j =  U_6j * (-1.0)**nint(0.5 + F12 - F + 1.0 - F23)
//           else if (itype .eq. 7) then
//              U_6j = U_6j * (-1.0)**nint(1.0 - F12 + 1.0 - F23)
//           else if (itype .eq. 70) then
//              U_6j = U_6j * (-1.0)**nint(1.0 - F12)
//           else if (itype .eq. 8) then
//              U_6j = ddl(L12, L23) * (-1.0)**nint(1.0 - F23)
//           else
//              stop ' not ready, in U_6j'
//           end if
//        end if
//        return
//        end
//  c
//  c
//        real*8 function c1_hm (ii, j1, j2, Qnl, dir, exc, idp, eps, n, l)
//        include 'par.jm.f'
//        real*8 dir, exc, eps
//        integer i1, i2, i3, i4, idp, j1, j2, Qnl(j_par, Nshells_par),
//       >   ii(4), n(4), l(4), itmp1, itmp2
//        i1 = ii(1)
//        i2 = ii(2)
//        i3 = ii(3)
//        i4 = ii(4)
//        itmp1 = 0
//        if (n(1) .eq. n(2) .and. l(1) .eq. l(2))  itmp1 = 1
//        itmp2 = 0
//        if (n(3) .eq. n(4) .and. l(3) .eq. l(4))  itmp2 = 1
//  c
//        if (abs(dir) + abs(exc) .gt. eps) then
//           c1_hm = (-1d0)**idp * 0.5d0 *
//       >      sqrt(dble(Qnl(j1, i1) * (Qnl(j1, i2) - itmp1) *
//       >      Qnl(j2, i3) * (Qnl(j2, i4) - itmp2) ) )
//       >      * ( dble(1 + (1 - itmp1) * (1 - itmp2)) * dir
//       >      - dble(2 - itmp1 - itmp2) * exc )
//        else
//           c1_hm = 0d0
//        end if
//        return
//        end
//  c
//  c
//        real*8 function c2_hm (l, n, h1, h2, IY, nel)
//  c
//  Cr    nel - total number of electrons in the system
//  Cr    IY - switch for direct (IY=1)
//  c
//        real*8 h1, h2, ddl
//        integer l(4), n(4), IY, nel
//  c
//        if (IY .eq. 1) then
//           c2_hm = ddl(l(1), l(3)) * ddl(l(2),l(4)) / dble(nel - 1)
//       >      * (ddl(n(1), n(3)) * h1 + ddl(n(2), n(4)) * h2 )
//        else
//           c2_hm = ddl(l(1), l(4)) * ddl(l(2),l(3)) / dble(nel - 1)
//       >      * (ddl(n(1), n(4)) * h1 + ddl(n(2), n(3)) * h2 )
//        end if
//        return
//        end
//  c-------------------------------------------------------------------------
//  c
//  c     Define i1 = \rho  and  i2 = \sigma
//  c
//        subroutine r_s_hm (
//  c     IN:
//       >   Qnl, j1, nsh, ib,
//  c     OUT:
//       >   ii)
//        include 'par.jm.f'
//        integer itmpv(nwf_par), Qnl(j_par, Nshells_par),
//       >   irho, isigma, ib, ib2, nsh, idl, j1, ii(2)
//  c
//        irho = 0
//        isigma = 0
//        do ib2 = 1, nsh
//           itmpv(ib2) = Qnl(j1, ib2) - idl(ib, ib2)
//           if (itmpv(ib2) .gt. 0 .and. irho .eq. 0) then
//              ii(1) = ib2
//              itmpv(ib2) = itmpv(ib2) - 1
//              irho = 1
//           end if
//        end do
//        do ib2 = 1, nsh
//           if (itmpv(ib2) .gt. 0 .and. isigma .eq. 0) then
//              ii(2) = ib2
//              isigma = 1
//           end if
//        end do
//        return
//        end
//  c--------------------------------------------------------------------------
//  c
//  c     nel - number of electrons in the configuration
//  c
//        subroutine d_e_hm (
//  c     IN:
//       >   LL, HL0, n, l, ip, ipp, pp, eps, clkl,
//       >   nwf, nr, nel, rn, wn, h, Zatom,
//  c     OUT:
//       >   dir, exc)
//        include 'par.jm.f'
//        integer kk, ip(4), n(4), l(4),
//       >   ipp(nwf_par, 2), nwf, nr, LL, nel, i1, i2
//        real*8 pp(nr_par, nwf_par), HL0(nwf_par, nwf_par), rn(nr), wn(nr),
//       >   h, Zatom, YK2(nr_par), clkl(0:Lmax_par, 0:k_par, 0:Lmax_par)
//        real*8 tmp1, tmp2, eps, dir, exc, sc_k, c2_hm
//  c
//  c     direct
//        dir = 0d0
//        do kk = max0(abs(l(1) - l(3)), abs(l(2) - l(4))),
//       >   min0(abs(l(1) + l(3)), abs(l(2) + l(4)), k_par), 2
//           tmp1 = sc_k (l(1), l(2), l(3), l(4), kk, LL, clkl, eps)
//           if (abs(tmp1) .gt. eps) then
//
//              i1 = ip(2)
//              i2 = ip(4)
//              call yk_hm (i1, i2, pp(1,i1), pp(1,i2), l(2), l(4),
//       >         kk, nr, rn, h, Zatom, yk2)
//
//              call int3_8 (PP(1,ip(1)), ipp(ip(1),1), ipp(ip(1),2),
//       >         PP(1,ip(3)), ipp(ip(3),1), ipp(ip(3),2),
//       >         yk2, 1, nr, tmp2, wn, nr)
//              tmp1 = tmp1 * tmp2
//           end if
//           dir = dir + tmp1
//        end do
//        dir = dir + c2_hm (l, n, HL0(ip(2), ip(4)), HL0(ip(1), ip(3)), 1, nel)
//  c
//  c     Exchange  3<-->4
//        exc = 0d0
//        do kk = max0(abs(l(1) - l(4)), abs(l(2) - l(3))),
//       >   min0(abs(l(1) + l(4)), abs(l(2) + l(3)), k_par), 2
//           tmp1 = sc_k (l(1), l(2), l(4), l(3), kk, LL, clkl, eps)
//       >      * (-1.0)**(l(3) + l(4) - LL)
//           if (abs(tmp1) .gt. eps) then
//
//              i1 = ip(2)
//              i2 = ip(3)
//              call yk_hm (i1, i2, pp(1,i1), pp(1,i2), l(2), l(3),
//       >         kk, nr, rn, h, Zatom, yk2)
//
//              call int3_8 (PP(1,ip(1)), ipp(ip(1),1), ipp(ip(1),2),
//       >         PP(1,ip(4)), ipp(ip(4),1), ipp(ip(4),2),
//       >         yk2, 1, nr, tmp2, wn, nr)
//              tmp1 = tmp1 * tmp2
//           end if
//           exc = exc + tmp1
//        end do
//        exc = exc + c2_hm (l, n, HL0(ip(2), ip(3)), HL0(ip(1), ip(4)), 0, nel)
//  c
//        return
//        end
//  c-------------------------------------------------------------------------
//  c$$$      subroutine (
//  c$$$c     IN:
//  c$$$     >
//  c$$$c     OUT:
//  c$$$     >   )
//  c$$$      return
//  c$$$      end
//

}
/*
C=====August,1990===========================================================
C            Analytic Independent-Particle Model for Atoms.
C            Alex E. S. at al, Phys.Rev., v.184, 1969, p.1
C
C            -Z0/r is removed from this potential!
C             -----------------------------------
C
C=====By D.A.Konovalov======================MADE IN AUSTRALIA===============
      subroutine ipmhf (Z, Z0, v, grid, nr, expcut)
      integer i, jump, nr, NZ, natom, n
      parameter (natom = 19)
      implicit real*8 (a-h, o-z)
      dimension v(nr), grid(nr)
c
c INPUT:
c -----
c  Z     - charge of the atom
c  grid  - r-grid, nr - number of points in this grid
C  expcut- the smollest value of exp-functions in "IPMDHF.F"
C  Z0    - Coulomb potential removed from the potential V.
C          V(r) = Vatom(r)  -  Z0 / r     mult-ed by r if  Z.NE.Z0
c
c OUTPUT:
c ------
c  V - Array of the values of the potential multiplyed by r  if  Z.NE.Z0
c
      dimension dn(natom)
      data (dn(n), n = 1, natom) /1.0d-30, 0.215d0, 0.563d0, 0.858d0
     >,0.979d0, 0.880d0, 0.776d0, 0.708d0, 0.575d0, 0.500d0, 0.561d0
     >,0.621d0, 0.729d0, 0.817d0, 0.868d0, 0.885d0, 0.881d0, 0.862d0
     >,1.006d0/
      NZ = nint(Z)
c
c     Do same check
c
      if ((NZ .lt. 1) .or. (NZ .gt. Natom)) then
         print*, 'This Z is not available yet,  Z=', Z
         print*, ' Max Z=', Natom
         stop    'Stop in "ipmdhf.f"'
      end if
c
c     Some constants
c
      dd = dn(NZ)
      hh = dd * (Z - 1d0)**0.4d0
c
c     Loop by r-grid
c
      jump = 0
      do i = 1, nr
         r = grid(i)
         r1 = 1d0 / r
         v(i) = Z0 - 1d0
         if (jump .eq. 0) then
            dexpr = dexp(- r / dd)
            if (dexpr .lt. expcut)  then
               jump = i
            else
               teta = dexpr / (hh * (1d0 - dexpr) + dexpr)
               v(i) = - ((Z - 1d0)* teta + 1d0) + Z0
            end if
         end if
      end do
c
c     Put back 1/r if Z.EQ.Z0
c
      if (nint(Z) .eq. nint(Z0))  then
         do i = 1, nr
            v(i) = v(i) / grid(i)
         end do
      end if
      return
      end

c-------------------------------------------------------------------------
c
c     Define such matrix element is possible
c
      function irrss (Qnl, Nnl, Lnl, j1, j2, nsh, ii)
      include 'par.jm.f'
      integer irrss, itmp1, itmp2, idnl,
     >   Qnl(j_par, Nshells_par),
     >   Nnl(j_par, Nshells_par),
     >   Lnl(j_par, Nshells_par),
     >   ib, ib2, nsh, idl, j1, j2, ii(4), n1, n2, l1, l2, i
c
      irrss = 0
c
      do ib = 1, nsh
         itmp1 = Qnl(j1, ib) - idl(ib, ii(1)) - idl(ib, ii(2))
         itmp2 = Qnl(j2, ib) - idl(ib, ii(3)) - idl(ib, ii(4))
c
c     Check if there are enough electrons for \sigma's and \rho's
         if (itmp1 .lt. 0 .or. itmp2 .lt. 0) return
c
c     Check if two configurations have the same spectator electrons, p.A69_Fano
c
         if (ib .eq. 1 .and. itmp1 .ne. itmp2) return
c
         if (itmp1 .gt. 0 .and. ib .gt. 1) then
            n1 = Nnl(j1, ib)
            l1 = Lnl(j1, ib)
            i = 0
            do ib2 = 2, nsh
               n2 = Nnl(j2, ib2)
               l2 = Lnl(j2, ib2)
               itmp2 = Qnl(j2, ib2) - idl(ib2, ii(3)) - idl(ib2, ii(4))
               if (idnl(n1, l1, n2, l2) .eq. 1 .and. itmp1 .eq. itmp2)   i = 1
            end do
            if (i .eq. 0) return
         end if
      end do
c
      irrss = 1
c
      return
      end
c-------------------------------------------------------------------------
c
c     Copy shells
c
      subroutine copysh(Qnl, Nnl, Lnl, LLnl, LSnl, j1, j2, i1, i2, k1, k2)
      include 'par.jm.f'
      integer j1, j2, i1, i2, k1, k2
      integer
     >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
     >   Qnl(j_par, Nshells_par, 2),
     >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2)
c
      Qnl(j2, i2, k2)  = Qnl(j1, i1, k1)
      Nnl(j2, i2, k2)  = Nnl(j1, i1, k1)
      Lnl(j2, i2, k2)  = Lnl(j1, i1, k1)
      LLnl(j2, i2, k2) = LLnl(j1, i1, k1)
      LSnl(j2, i2, k2) = LSnl(j1, i1, k1)
c
      return
      end
c
c
c     Create empty shell
c
      subroutine emptysh(Qnl, Nnl, Lnl, LLnl, LSnl, j, i, k)
      include 'par.jm.f'
      integer j, i, k
      integer
     >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
     >   Qnl(j_par, Nshells_par, 2),
     >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2)

      Qnl(j, i, k) = 0
      Nnl(j, i, k) = 0
      Lnl(j, i, k) = 0
      LLnl(j, i, k) = 0
      LSnl(j, i, k) = 1

      return
      end
c
c     Check if j's configuration exists already
c
c     newcon = 1, if this configuration is new
      function newcon (Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, j, k, nsh)
      include 'par.jm.f'
      integer j, m, i, ii, k, nsh, newcon
      integer
     >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
     >   Qnl(j_par, Nshells_par, 2),
     >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2),
     >   LL12(j_par, 2), LS12(j_par, 2)
      newcon = 1
c
      do i = 1, j - 1
         if (LL12(i, k) .eq. LL12(j, k) .and. LS12(i, k) .eq. LS12(j, k)) then
c
c     Check all shells
            ii = 0
            do m = 1, nsh
               if(Qnl(i, m, k) .eq. Qnl(j, m, k) .and.
     >            Nnl(i, m, k) .eq. Nnl(j, m, k) .and.
     >            Lnl(i, m, k) .eq. Lnl(j, m, k) .and.
     >            LLnl(i, m, k) .eq. LLnl(j, m, k) .and.
     >            LSnl(i, m, k) .eq. LSnl(j, m, k) ) then
                  ii = ii + 1
               end if
            end do
c
c     all nsh-shells are the same
            if (ii .eq. nsh) then
               newcon = 0
               return
            end if
c
         end if
      end do
      return
      end
c
c     Triangle rule
c
      function itri(i1, i2, i3)
      integer itri, i1, i2, i3
      itri = 1
      if(iabs(i1 - i2) .gt. i3 .or.  i1 + i2 .lt. i3 .or.
     >   iabs(i1 - i3) .gt. i2 .or.  i1 + i3 .lt. i2 .or.
     >   iabs(i2 - i3) .gt. i1 .or.  i2 + i3 .lt. i1 )  itri = 0
      return
      end
c---------------------------------------------------------------------
c
c     Memory position for a symmetric array
c
      function msym (i1, i2, n)
      integer msym, i, j, n, i1, i2
      i = max0(i1, i2)
      j = min0(i1, i2)
      msym = ((2 * n + 2 - j) * (j - 1)) / 2 + (i - j + 1)
      return
      end
c---------------------------------------------------------------------
c
c     Generate arrays for r (rn) and sqrt(r) (r5) with
c     a constant mesh size in the log(z*r) variable
c
      subroutine rn_hm (
c     In
     >   z, nr, rmin, rmax, IY,
c     Out
     >   rn, r5, wn, h)
c     IY = 1, Weights are Simpson's otherwise they are Bode's
c
      integer nr, i, IY
      real*8 z, rmin, rmax, rn(nr), r5(nr), wn(nr), h, rho,
     >   a(0:3), tmp
c
c  *****  set the starting point and the step size
c
      rho = log(z * rmin)
      h   = (log(z * rmax) - rho) / dble(nr - 1)
      do i = 1, nr
         rn(i) = dexp(rho) / z
         r5(i) = dsqrt(rn(i))
         rho = rho + h
      end do
      rho = rho - dble(nr) * h
c
      if (IY .eq. 1) then
c     Simpson's weights
         if (mod(nr,2) .eq. 0) stop ' nr must be odd, stoped in rn_hm'
         tmp = 2d0 * h / 3d0
         do i = 1, nr
            wn(i) = dble(mod(i+1, 2) + 1) * tmp * rn(i)
         end do
         wn(1)  = wn(1) / 2d0
         wn(nr) = wn(nr) / 2d0
      else
c     Bode's weights
         tmp = 2d0 * h / 45d0
         a(1) = 14d0 * tmp
         a(2) = 32d0 * tmp
         a(3) = 12d0 * tmp
         a(0) = 32d0 * tmp
         do i = 1, nr
            wn(i) = a(mod(i, 4))  * rn(i)
         end do
         wn(1)  = wn(1) / 2d0
         wn(nr) = wn(nr) / 2d0
      end if
      return
      end
C     ------------------------------------------------------------------
C
C     Stores Y^k (p1, p2; r) in the array Y(r)
C
      SUBROUTINE yk_hm (
c     In
     >   i1, i2, p1, p2, l1, l2, K, nr, rn, h, Zatom,
c     Out
     >   y)
      IMPLICIT REAL*8(A-H,O-Z)
      real*8 p1(nr), p2(nr), rn(nr), y(nr)
      integer L1, L2, nr, k, m, m1, m2, mm, i1, i2
c
      EH = exp(-H)
      h3 = h / 3d0
c
C     Calculates and stores Z^k (p1, p2; r) in the array Y(r)
c
      DEN = dble(L1 + L2 + 3 + K)
      FACT = (1d0 / dble(L1 + 1) + 1d0 / dble(L2 + 1)) / (DEN + 1d0)
      A = EH**K
      A2 = A * A
      H90 = H / 90.D0
      A3 = A2 * A * H90
      AI = H90 / A
      AN = 114d0 * A * H90
      A34 = 34d0 * H90
      F1 = rn(1)**2 * P1(1) * P2(1)
      F2 = rn(2)**2 * P1(2) * P2(2)
      F3 = rn(3)**2 * P1(3) * P2(3)
      F4 = rn(4)**2 * P1(4) * P2(4)
      y(1) = F1 * (1d0 + Zatom * rn(1) * FACT)/DEN
      y(2) = F2 * (1d0 + Zatom * rn(2) * FACT)/DEN
      y(3) = y(1) * A2 + H3 * (F3 + 4d0 * A * F2 + A2 * F1)
      DO M = 5, nr
         F5 = rn(M)**2 * P1(M) * P2(M)
         y(M-1) = y(M-3) * A2 + (AN * F3 + A34 * (F4+A2 * F2)-F5 * AI-F1 * A3)
         F1 = F2
         F2 = F3
         F3 = F4
         F4 = F5
      end do
      y(nr) = A * y(nr-1)
      if (i1 .eq. i2 .and. iabs(k) .eq. 0) then
C
C  *****  FOR Y0(I,I) SET THE LIMIT TO 1 AND REMOVE OSCILLATIONS
C  *****  INTRODUCED BY THE USE OF SIMPSON'S RULE
C
         M2 = (nr / 2) * 2 - 1
         M1 = M2 - 1
         C1 = 1d0 - y(M1)
         C2 = 1d0 - y(M2)
         DO M = 1, M1, 2
            y(M) = y(M) + C1
            y(M+1) = y(M+1) + C2
         end do
         y(nr) = 1d0
         y(nr-1) = 1d0
      end if
C
C     calculate Y^k (p1, p2; r)
c
      A = EH**(K+1)
      C = 2 * K+1
      A2 = A * A
      H90 = C * H3 / 30d0
      A3 = A2 * A * H90
      AI = H90 / A
      AN = 114.D0 * A * H90
      A34 = 34.D0 * H90
      F1 = y(nr) * EH**K
      F2 = y(nr)
      F3 = y(nr-1)
      F4 = y(nr-2)
      DO MM = 2, nr - 2
         M = nr - MM
         F5 = y(M-1)
         y(M) = y(M+2) * A2
     >      + (AN * F3 + A34 * (F4 + A2 * F2) - F5 * AI - F1 * A3)
         F1 = F2
         F2 = F3
         F3 = F4
         F4 = F5
      end do
      y(1) = y(3) * A2 + C * H3 * (F4 + D4 * A * F3 + A2 * F2)
      RETURN
      END
c--------------------------------------------------------------------
c
c     Scalar product,
      real*8 function Sc_k (l1, l2, j1, j2, k, L, clkl, eps)
      include 'par.jm.f'
      real wign6j
      real*8 clkl(0:Lmax_par, 0:k_par, 0:Lmax_par), eps
      integer l1, l2, j1, j2, k, L
c$$$      call iot6 (L, l2, l1, k, j1, j2, sc_k)
      sc_k = wign6j (real(L), real(l2), real(l1), real(k), real(j1), real(j2))
c
      if (abs(sc_k) .lt. eps)            return
      sc_k = dble((-1)**(j1 + l2 + L)) *
     >   sc_k * clkl(l1, k, j1) * clkl(l2, k, j2)
      return
      end
c----------------------------------------------------------------------
c     itype =
c     0  -  (l1 l2) L12, l3 L | (l1, l2) L23, l3 L
c     1  -  (l1 l2) L12, l3 L | l1 (l2, l3) L23, L
c     10 -  (l1 l2) L12, l3 L | l1 (l3, l2) L23, L
c     2  -  (l1 l2) L12, l3 L | (l1, l3) L23, l2 L
c     20 -  (l1 l2) L12, l3 L | (l3, l1) L23, l2 L
c     3  -  (l1 l2) L12, l3 L | (l3, l2) L23, l1 L
c     30 -  (l1 l2) L12, l3 L | (l2, l3) L23, l1 L
c     4  -  (l1 l2) L12, l3 L | l1 (l3, l2) L23, L
c     5  -  (l1 l2) L12, l3 L | l3 (l2, l1) L23, L
c     50 -  (l1 l2) L12, l3 L | l3 (l1, l2) L23, L
c     6  -  l1 (l2 l3) L12, L | l3 (l2, l1) L23, L
c     60 -  l1 (l2 l3) L12, L | l3 (l1, l2) L23, L
c     61 -  l1 (l2 l3) L12, L | l1 (l3, l2) L23, L
c     62 -  l1 (l2 l3) L12, L | l2 (l3, l1) L23, L
c     620-  l1 (l2 l3) L12, L | l2 (l1, l3) L23, L
c     7  -  (l1 l2) L12, l3 L | l2 (l3, l1) L23, L
c     70 -  (l1 l2) L12, l3 L | l2 (l1, l3) L23, L
c     8  -  (l1 l2) L12, l3 L | (l2, l1) L23, l3 L
c
c     Recoupling
      real*8 function U_6j (itype, L12, L23, l1, l2, l3, L, eps, IY)
      real*8 eps, ddl
      integer l1, l2, l3, L12, L23, L, IY, itype, j1, j2, j3
      real  f1, f2, f3, F12, F23, F, phase, JAHNUF
      U_6j = 0d0
      if (itype .eq. 0) then
         U_6j = ddl(L12, L23)
         return
      end if
      if (IY .eq. 1) then
c
         if(itype .eq. 1) then
            j1 = l1
            j2 = l2
            j3 = l3
            phase = 1.0
         else if (itype .eq. 2) then
            j1 = l2
            j2 = l1
            j3 = l3
            phase = (-1.0)**(l1 + l2 - L12 + l2 + L23 - L)
         else
            stop ' not ready, U_6j for l1,l2,l3'
         end if

         call iot6 (j1, j2, L12, j3, L, L23, U_6j)
c
         if (abs(U_6j) .lt. eps)   return
         U_6j = U_6j * dble((-1)**(j1 + j2 + j3 + L))
     >      * dsqrt(dble((2 * L12 + 1) * (2 * L23 + 1))) * phase
      else
         f1 = real(l1 - 1) / 2.0
         f2 = real(l2 - 1) / 2.0
         f3 = real(l3 - 1) / 2.0
         F12 = real(L12 - 1) / 2.0
         F23 = real(L23 - 1) / 2.0
         F = real(L - 1) / 2.0
         U_6j = JAHNUF (f1, f2, F, f3, F12, F23)
c
         if (itype .eq. 1) then
            return
         else if (itype .eq. 10) then
            U_6j = U_6j * (-1.0)**nint(1.0 - F23)
         else if (itype .eq. 2) then
            U_6j = U_6j * (-1.0)**nint(1.0 - F12 + 0.5 + F23 - F)
         else if (itype .eq. 20) then
            U_6j = U_6j * (-1.0)**nint(1.0 - F12 + 0.5 + F23 - F + 1.0 - F23)
         else if (itype .eq. 3) then
            U_6j = U_6j * (-1.0)**nint(F23 + 0.5 - F + 1.0 - F23)
         else if (itype .eq. 30) then
            U_6j = U_6j * (-1.0)**nint(F23 + 0.5 - F)
         else if (itype .eq. 4) then
            U_6j = U_6j * (-1.0)**nint(1.0 - F23)
         else if (itype .eq. 5) then
            U_6j =  ddl(L12, L23) * (-1.0)**nint(1.0 - F23 + 0.5 + F23 - F)
         else if (itype .eq. 50) then
            U_6j =  ddl(L12, L23) * (-1.0)**nint(0.5 + F23 - F)
         else if (itype .eq. 6) then
            U_6j = U_6j * (-1.0)**nint(0.5 + F12 - F + 1.0 - F12)
         else if (itype .eq. 60) then
            U_6j = U_6j * (-1.0)**nint(0.5 + F12 - F + 1.0 - F12 + 1.0 - F23)
         else if (itype .eq. 61) then
            U_6j =  ddl(L12, L23) * (-1.0)**nint(1.0 - F23)
         else if (itype .eq. 62) then
            U_6j =  U_6j * (-1.0)**nint(0.5 + F12 - F)
         else if (itype .eq. 620) then
            U_6j =  U_6j * (-1.0)**nint(0.5 + F12 - F + 1.0 - F23)
         else if (itype .eq. 7) then
            U_6j = U_6j * (-1.0)**nint(1.0 - F12 + 1.0 - F23)
         else if (itype .eq. 70) then
            U_6j = U_6j * (-1.0)**nint(1.0 - F12)
         else if (itype .eq. 8) then
            U_6j = ddl(L12, L23) * (-1.0)**nint(1.0 - F23)
         else
            stop ' not ready, in U_6j'
         end if
      end if
      return
      end
c
c
      real*8 function c1_hm (ii, j1, j2, Qnl, dir, exc, idp, eps, n, l)
      include 'par.jm.f'
      real*8 dir, exc, eps
      integer i1, i2, i3, i4, idp, j1, j2, Qnl(j_par, Nshells_par),
     >   ii(4), n(4), l(4), itmp1, itmp2
      i1 = ii(1)
      i2 = ii(2)
      i3 = ii(3)
      i4 = ii(4)
      itmp1 = 0
      if (n(1) .eq. n(2) .and. l(1) .eq. l(2))  itmp1 = 1
      itmp2 = 0
      if (n(3) .eq. n(4) .and. l(3) .eq. l(4))  itmp2 = 1
c
      if (abs(dir) + abs(exc) .gt. eps) then
         c1_hm = (-1d0)**idp * 0.5d0 *
     >      sqrt(dble(Qnl(j1, i1) * (Qnl(j1, i2) - itmp1) *
     >      Qnl(j2, i3) * (Qnl(j2, i4) - itmp2) ) )
     >      * ( dble(1 + (1 - itmp1) * (1 - itmp2)) * dir
     >      - dble(2 - itmp1 - itmp2) * exc )
      else
         c1_hm = 0d0
      end if
      return
      end
c
c
      real*8 function c2_hm (l, n, h1, h2, IY, nel)
c
Cr    nel - total number of electrons in the system
Cr    IY - switch for direct (IY=1)
c
      real*8 h1, h2, ddl
      integer l(4), n(4), IY, nel
c
      if (IY .eq. 1) then
         c2_hm = ddl(l(1), l(3)) * ddl(l(2),l(4)) / dble(nel - 1)
     >      * (ddl(n(1), n(3)) * h1 + ddl(n(2), n(4)) * h2 )
      else
         c2_hm = ddl(l(1), l(4)) * ddl(l(2),l(3)) / dble(nel - 1)
     >      * (ddl(n(1), n(4)) * h1 + ddl(n(2), n(3)) * h2 )
      end if
      return
      end
c-------------------------------------------------------------------------
c
c     Define i1 = \rho  and  i2 = \sigma
c
      subroutine r_s_hm (
c     IN:
     >   Qnl, j1, nsh, ib,
c     OUT:
     >   ii)
      include 'par.jm.f'
      integer itmpv(nwf_par), Qnl(j_par, Nshells_par),
     >   irho, isigma, ib, ib2, nsh, idl, j1, ii(2)
c
      irho = 0
      isigma = 0
      do ib2 = 1, nsh
         itmpv(ib2) = Qnl(j1, ib2) - idl(ib, ib2)
         if (itmpv(ib2) .gt. 0 .and. irho .eq. 0) then
            ii(1) = ib2
            itmpv(ib2) = itmpv(ib2) - 1
            irho = 1
         end if
      end do
      do ib2 = 1, nsh
         if (itmpv(ib2) .gt. 0 .and. isigma .eq. 0) then
            ii(2) = ib2
            isigma = 1
         end if
      end do
      return
      end
c--------------------------------------------------------------------------
c
c     nel - number of electrons in the configuration
c
      subroutine d_e_hm (
c     IN:
     >   LL, HL0, n, l, ip, ipp, pp, eps, clkl,
     >   nwf, nr, nel, rn, wn, h, Zatom,
c     OUT:
     >   dir, exc)
      include 'par.jm.f'
      integer kk, ip(4), n(4), l(4),
     >   ipp(nwf_par, 2), nwf, nr, LL, nel, i1, i2
      real*8 pp(nr_par, nwf_par), HL0(nwf_par, nwf_par), rn(nr), wn(nr),
     >   h, Zatom, YK2(nr_par), clkl(0:Lmax_par, 0:k_par, 0:Lmax_par)
      real*8 tmp1, tmp2, eps, dir, exc, sc_k, c2_hm
c
c     direct
      dir = 0d0
      do kk = max0(abs(l(1) - l(3)), abs(l(2) - l(4))),
     >   min0(abs(l(1) + l(3)), abs(l(2) + l(4)), k_par), 2
         tmp1 = sc_k (l(1), l(2), l(3), l(4), kk, LL, clkl, eps)
         if (abs(tmp1) .gt. eps) then

            i1 = ip(2)
            i2 = ip(4)
            call yk_hm (i1, i2, pp(1,i1), pp(1,i2), l(2), l(4),
     >         kk, nr, rn, h, Zatom, yk2)

            call int3_8 (PP(1,ip(1)), ipp(ip(1),1), ipp(ip(1),2),
     >         PP(1,ip(3)), ipp(ip(3),1), ipp(ip(3),2),
     >         yk2, 1, nr, tmp2, wn, nr)
            tmp1 = tmp1 * tmp2
         end if
         dir = dir + tmp1
      end do
      dir = dir + c2_hm (l, n, HL0(ip(2), ip(4)), HL0(ip(1), ip(3)), 1, nel)
c
c     Exchange  3<-->4
      exc = 0d0
      do kk = max0(abs(l(1) - l(4)), abs(l(2) - l(3))),
     >   min0(abs(l(1) + l(4)), abs(l(2) + l(3)), k_par), 2
         tmp1 = sc_k (l(1), l(2), l(4), l(3), kk, LL, clkl, eps)
     >      * (-1.0)**(l(3) + l(4) - LL)
         if (abs(tmp1) .gt. eps) then

            i1 = ip(2)
            i2 = ip(3)
            call yk_hm (i1, i2, pp(1,i1), pp(1,i2), l(2), l(3),
     >         kk, nr, rn, h, Zatom, yk2)

            call int3_8 (PP(1,ip(1)), ipp(ip(1),1), ipp(ip(1),2),
     >         PP(1,ip(4)), ipp(ip(4),1), ipp(ip(4),2),
     >         yk2, 1, nr, tmp2, wn, nr)
            tmp1 = tmp1 * tmp2
         end if
         exc = exc + tmp1
      end do
      exc = exc + c2_hm (l, n, HL0(ip(2), ip(3)), HL0(ip(1), ip(4)), 0, nel)
c
      return
      end
c-------------------------------------------------------------------------
c$$$      subroutine (
c$$$c     IN:
c$$$     >
c$$$c     OUT:
c$$$     >   )
c$$$      return
c$$$      end


 */
