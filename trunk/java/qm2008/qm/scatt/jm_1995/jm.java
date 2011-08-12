package scatt.jm_1995;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 14:57:43
 */
public class jm {
}
// original jm.f
/*
c
C     jm.f
c
      subroutine jm (
c     In: list1 from jm.main.f is used, see jm_2008.main.f or jm_2008.tex for help
     >   i_atom, zatom, Zion, eps, nr, rmin, rmax,
     >   Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
     >   alagl, Egrnd, E_0, n_E0, ndiff, lwf, nwfl, ns_exc,
     >   nexcl, njml, i_test, i_printout,
     >   i_stop, n_print_g,
     >   u_e, u_cs, ch_e, ch_cs, units_cs, units_dcs,units_e, nth, th, td, wth)
c
      include 'par.jm.f'
c$$$      integer mema, malloc
      integer ii, n1, nth,
     >   i_atom, nr, Nnlexc, Lnlexc, lwf, i_test, i_printout, i_stop,
     >   n_print_g, Ncore, Lc(Ncore), Nc(Ncore),
     >   LLmax(2), LSmin(2), LSmax(2), nwfl(0:lwf), nexcl(0:lwf),
     >   ns_exc(0:Lmax_par), njml(0:lwf),
     >   ig_igm(igm_par), igm_jm(igm_par), l_igm(igm_par),
     >   LL_ig(igm_par), LS_ig(igm_par),
c
     >   jmint(0:Lmax_par, LSmax_par), jmaxt(0:Lmax_par, LSmax_par),
     >   jmax, igmax, imaxLS,
     >   igm, ig, igm1, i, ig1, i1, ig2, i2, n_e0, ndiff, i_e0,
     >   nsh, nsht, ncf, ncft
c
      integer  j_last, j_first, j_ig_l(igm_par, 0:Lmax_par)
c
      integer
     >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
     >   Qnl(j_par, Nshells_par, 2),
     >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2),
     >   LL12(j_par, 2), LS12(j_par, 2)
c
      real*8 zatom, Zion, eps, eps_1, eps_2, rmin, rmax, E0, Egrnd,
     >   alagl(0:Lmax_par), th(nth), td(nth), wth(nth_par),
     >   E_0(n_E0), pi, pi2, pi4, pi05, pi_4, dgm(igm_par), g,
c$$$     >   alpha, rho, Z0, expcut,
     >   Y_lm(nth_par, 0:Lmax_par, 0:Lmax_par), plm
c
c     Temporary: tmp-variables, tmpv-vectors, tmpa-arrays
      integer m1, m2
      real*8
     >   tmp, tmp1, tmp2, tmp3, tmp4, tmp5, ztmp,
     >   tmpv1(i_par), tmpv2(i_par), tmpv3(nr_par),
     >   tmpa(nr_par, nwf_par), tmpa2(i_par, i_par)
      complex*16 ctmp, ctmp1, ctmp2
c
      complex*16 qgm
      real*8 cjm, s_n, s_n1, c_n, c_n1
c
      complex*16 tg(igm_par, 0:Lmax_par, ne_par),
     >   tmm(ig_par, -Lt_par:Lt_par, -Lt_par:Lt_par, LSmax_par, nth_par)

      real*8  cs(igm_par, 0:Lmax_par, ne_par), dcs(nth_par, ig_par)
      real*8 v(nr_par),
     >   eps10, eps8, eps100, hrho, u_e(nu_par), u_cs(nu_par),
     >   eng(i_par), engt(igm_par), tcs(igm_par, 1, ne_par), eng_neg,
     >   vec(i_par, i_par),  vect(igm_par, igm_par),
     >   U(igm_par, i_par), rj,
     >   HL0(nwf_par, nwf_par),
     >   clkl(0:Lmax_par, 0:k_par, 0:Lmax_par)
c
      integer Lgr, m, m1, m2,
     >   l1, l2, l3, k1, k2, j, j1, j2, j0, jj, ierr,
     >   LLt, LSt, LL, LS, k, i1, i2, units_e, units_cs, units_dcs,
     >   nwf, mm, IY, l, nexc, i, nfl
      real wign3j, clebsg
      real*8 fl(0:nfl_par)
c
c     rn_hm variables
      integer nr, Np(nwf_par), Lp(nwf_par),
     >   m_orb(0:Lmax_par), ipp(nwf_par, 2)
      real*8 rn(nr_par), r5(nr_par), wn(nr_par), pn(nwf_par),pp(nr_par,nwf_par)
c
c     External calls: ddl, target, h_matrix, rn_hm, lagpol8
      real*8 ddl
c
      character*2 ch_e(nu_par)
      character*8 ch_cs(nu_par), filen
c
      character*1 chl(0:nchl_par), chll(0:nchl_par)
      data chl  /'s', 'p', 'd', 'f', 'g', 'h', 'i', '7', '8', '9', '0'/
      data chll /'S', 'P', 'D', 'F', 'G', 'H', 'I', '7', '8', '9', '0'/
c
      integer i_atom
      include 'formats.jm.f'
      if (i_printout .gt. 1)  print*, 'i_printout>1: jm, +++ entry +++'
c
c     Pointers: Used to create dynamic memory allocation for big arrays
c
c$$$      pointer (ptg, tg)
c$$$      mema = mema * 2
c$$$      ptg = malloc(mema)
c
      eps8  = 1d-7
      eps10 = 10.0d0 * eps
      eps100 = 100.0d0 * eps
      eps_1 = 0.1d0 * eps
      eps_2 = 0.01d0 * eps
      k1 = 1
      k2 = 2
      pi   = dacos(-1d0)
      pi2  = pi * 2d0
      pi4  = pi * 4d0
      pi05 = pi * 0.5d0
      pi_4 = 1d0 / dsqrt(pi4)
c
c     ALAGL - \lambda/2- parameter of the jmBasisN for each L
c     nexc - total number of excited states
      nexc = 0
      do L = 0, lwf
c
c     The same r-region is covered in each L
c
c$$$         ALAGL(L) = alagl(0) + dble(njml(l) + l - njml(0)) * dlog(Rmax) / Rmax
c$$$         ALAGL(L) = alagl(0)
         ALAGL(L) = alagl(0) * dble(NJML(L) + L + 1) / dble(NJML(0) + 1)
c$$$         ALAGL(L) = alagl(0) * dble(nwfl(L) + L + 1) / dble(nwfl(0) + 1)
c$$$         print*, ALAGL(L)
c
         nexc = nexc + nexcl(L)
      end do
c
c     Set up rn(n) together with the integration weights wn(n)
      IY = 2
c     IY.ne.1, Bode's weights. Simpson's weights calculated if IY=1
      call rn_hm (Zatom, nr, rmin, rmax, IY, rn, r5, wn, hrho)
c
c     Calculates factorials and stores them for future use in fl(0:nfl_par)
      nfl = nfl_par
      call factor(fl, nfl)
c
      if (i_test .gt. 1) then
         print*, 'i_test>1: jm'
         print*, '        : J_nm, S_m, C_m to satisfy Eq.2.6 and Eq.2.12 of I'
c
c+++  dak 7.94 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
c
c     Test J_nm, S_m, C_m to satisfy Eq.2.6 and Eq.2.12 of I.
c
         E0 = E_0(1)
         do L = 0, LLmax(2)
            do m1 = njml(L), njml(L)
               tmp1 = 0d0
               tmp2 = 0d0
               ctmp1 = (0d0, 0d0)
               do m2 = 1, njml(L)
c
                  Ztmp = 0d0
                  ctmp = dcmplx(dsqrt(2d0 * E0), 0d0)
                  call jmtrx
     >               (tmp5, tmp4, m1-1, m2-1,Ztmp,L,ctmp,2d0*alagl(L),fl,nfl)
                  call JmTheory
     >               (tmp3, tmp4, m2-1, Ztmp,L,ctmp,2d0 *alagl(L),eps_2,fl,nfl)
                  tmp1 = tmp1 + tmp5 * tmp3
                  tmp2 = tmp2 + tmp5 * tmp4
c
                  ctmp = dcmplx(0d0, dsqrt(2d0 * E0))
                  call jmtrx
     >               (tmp3, tmp4, m1-1,m2-1,Ztmp,L,ctmp,2d0*alagl(L),fl,nfl)
                  ctmp2 = dcmplx(tmp3, tmp4)
                  call JmTheory
     >               (tmp3, tmp4, m2-1, Ztmp,L,ctmp,2d0*alagl(L),eps_2,fl,nfl)
                  ctmp1 = ctmp1 + ctmp2 * dcmplx(tmp4, tmp3)
c
               end do
               if (m1 .gt. 1 .and. m1 .lt. njml(L) .and.
     >            abs(ctmp1) + abs(tmp1) + abs(tmp2) .gt. eps8) then
                  print*, 'm1, L = ', m1, L
                  print*, 'Sum_m Jnm Sm,Cm (k-real) =', real(tmp1), real(tmp2)
                  print*, 'Sum_m Jnm Sm,Cm (k-imag) =', ctmp1
                  if (i_stop .gt. 0) stop ' jm.f'
               end if
            end do
         end do
      end if
c
c     Define Basis Quantum Numbers and
C     Compute Initial Radial Functions (Initial Basis)
c
c     Check if there is enough memory for nwf
c
      nwf = 0
      do L = 0, Lwf
         do i = 1, njml(L)
            nwf = nwf + 1
         end do
      end do
      if (nwf .gt. nwf_par) then
         print*, ' increase nwf_par to ', nwf, ' from ', nwf_par
         stop    ' increase nwf_par'
      end if
c
c     Define NWF, total jmBasisN size
      nwf = 0
      do L = 0, Lwf
         do i = 1, njml(L)
            nwf = nwf + 1
            Np(nwf) = i + L
            Lp(nwf) = L
         end do
Cr       m_orb(L) is a memory location pointer for Np(i) and Lp(i) for diff. L
         if(L .eq. 0) m_orb(L) = 0
         if(L .ne. 0) m_orb(L) = m_orb(L-1) + njml(L-1)
c        Define Laguer Polinomials for each L
         call lagpol8 (2*L+2, 2d0*alagl(L), tmpa, nr_par, njml(L),
     >      rn, nr, tmpv1)
         mm = m_orb(L)
         do i = 1, njml(L)
c           PN is a normalization constant
            PN(i+mm) = dsqrt(2d0 * alagl(L)) * dexp(0.5d0 *
     >         (FL(i - 1)  -  FL(2 * L + 1 + i)))
c
c           Define initial jmBasisN
            ipp(i+mm, 1) = 1
            ipp(i+mm, 2) = nr
            do j = 1, nr
               PP(j,i+mm) = PN(i+mm) * (2d0 * alagl(L) * rn(j))**(L + 1)
     >            * exp(-alagl(L) * rn(j)) * tmpa(j, i) / r5(J)
               if (i_printout.gt.2) write(100+i, 200) rn(j), PP(j,i+mm) * r5(j)
            end do
         end do
      end do
c
c     Check if there's enough memory
c
      if (nwf .gt. nwf_par) stop ' Larger nwf_par is needed, nwf > nwf_par'
c
c     Test orthonornality of PP(i,j) w.f.'s
      if (i_test .gt. 0) then
         print*, 'i_test>0: jm'
         print*, '        : Orthogonality test for initial jmBasisN set, done'
         do i1 = 1, nwf
            do i2 = i1, nwf
               if (Lp(i1) .eq. Lp(i2)) then
                  call int3_8 (PP(1,i1), ipp(i1,1), ipp(i1,2),
     >               PP(1,i2), ipp(i2,1), ipp(i2,2), rn, 1, nr,
     >               tmp2, wn, nr)
                  tmp1 = abs(tmp2 - ddl(i1, i2))
                  if (tmp1 .gt. eps8 .or. i_printout .gt. 2)
     >               print*, 'L,I,J,<i|j>', Lp(i1), i1, i2, real(tmp2)
               end if
            end do
         end do
      end if
c
      if (i_test .gt. 3) then
         do j = 1, nr
            write(40, 200)  rn(j), real(1.0 - exp(-2.0*rn(j)) * (1.0 + rn(j)))
            write(42, 200)  rn(j), real(1.0 - exp(-rn(j)) *
     >         (1.0 + 3.0/4.0 * rn(j) +1.0/4.0*rn(j)**2 +1.0/24.0 * rn(j)**3))
         end do
      end if
c
c     Diagonalize H0 in nwfl for each L=0 only
c     This should reduce number of functions needed
c
      do l = 0, lwf
         if (l .gt. 0)   Zion = 1d0
         do i = 1, nr
            V(i) = - Zion
         end do
c
         if (i_atom .eq. 3) then
c$$$c     Calculate HF potential using Indep.Part.Model
c$$$            expcut = 1d-10
c$$$            Z0 = 0d0
c$$$            call ipmhf (Zatom, Z0, v, rn, nr, expcut)
         end if
c
         n1 = nwfl(L)
         do i = 1, n1
            i1 = m_orb(L) + i
            call deriv_2 (pp(1,i1), nr, tmpv3, hrho, 0)
            tmp1 = (dble(L) + 0.5d0)**2
            do j = 1, nr
               rj = rn(j)
               tmpv3(j) = (tmpv3(j) - (2d0*rj * v(j) + tmp1) *PP(j,i1)) /rj**2
            end do
c
            do ii = 1, i
               i2 = m_orb(L) + ii
c
               U(i, ii) = 0d0
               call int3_8 (tmpv3, 1, nr, PP(1,i2), ipp(i2,1), ipp(i2,2),
     >            rn, 1, nr, tmp2, wn, nr)
               U(i, ii) = - 0.5d0 * tmp2
               U(ii, i) = U(i, ii)
            end do
         end do
c
c     call real matrix diagonilization routine
         call rs (igm_par, n1, U, engt, 1, vect, tmpv1, tmpv2, ierr)
c
c     Test VECT,   U = V' * V = must = I
         call dc_axb (vect, vect, U, 1, igm_par, igm_par, igm_par, n1, n1, n1)
         do i1 = 1, n1
            do i2 = i1, n1
               if(abs(U(i1, i2) - ddl(i1, i2)) .gt. eps8)
     >            print*, ' Please check VECT', ierr, i1,i2,tmp1
            end do
         end do
c
         if (i_printout .gt. 0) then
            print*, ' Exact H0 energies:'
            write(6, 205) (real( -Zion**2/2d0/dble(i+L)**2), i = 1, n1)
            print*, ' Basis H0 energies:'
            write(6, 205) (real(engt(i)), i = 1, n1)
            call flush (6)
            if (i_printout .gt. 2) then
c     Write coeff. under corresp. energies
               write(6, 205)
               do jj = 1, n1
                  write(6, 205)  (vect(jj, j), j = 1, n1)
               end do
            end if
         end if
c
c     Recalculate Target functions (Eq.3.1),  VEC = PP * VECT
         if (i_par .lt. nr_par) then
            print*, 'increase i_par to at least nr_par=', nr_par
            stop    'increase i_par to at least nr_par for dc_axb'
         end if
         mm = m_orb(L)
         call dc_axb (pp(1,1+mm), vect, vec, 0, nr_par, igm_par, i_par,
     >      nr, n1, n1)
c
c     Redefine P(j,i)-jmBasisN functions , P = VEC
         call da_bxv (pp(1,1+mm), vec, 1d0, 0, nr_par,i_par,nr,n1,n1)
      end do
c
c     Define boundaries for each PP
c     -----------------------------
      do i = 1, nwf
         do j = 1, nr
            if (abs(PP(j, i)) .gt. eps_1) then
               ipp(i, 1) = j
c     exit from this loop
               goto 11
            end if
         end do
 11      continue
c
         do j = nr, 1, -1
            if (abs(PP(j, i)) .gt. eps_1) then
               ipp(i,2) = j
c     exit from this loop
               goto 12
            end if
         end do
 12      continue
      end do
c
c     Calculate some useful arrays to be used later on
c
      do i = 1, nr
         v(i) = - Zatom
      end do
c$$$c
c$$$c     Use polarization potential to improve one-electron energies
c$$$c
c$$$      if (i_atom .eq. 3) then
c$$$         alpha = 0.19d0
c$$$         rho   = 1.4d0
c$$$         do j = 1, nr
c$$$            rj = rn(j)
c$$$            v(j) = - Zatom
c$$$     >         - alpha / (2d0 * rj**3) * (1d0 - dexp(-(rj / rho)**6))
c$$$         end do
c$$$      end if
c
      do i1 = 1, nwf
         if (i_printout .gt. 3 .or. (i_printout .eq. 1 .and. i1 .eq. 1)) then
            do j = 1, nr
               write(5000+i1, 200)  rn(j), pp(j, i1)
            end do
         end if
c
         call deriv_2 (pp(1,i1), nr, tmpv3, hrho, 0)
         tmp1 = (dble(Lp(i1)) + 0.5d0)**2
         do i = 1, nr
            tmpv3(i) = (tmpv3(i) - (2d0 * rn(i) * v(i) + tmp1) * PP(i,i1))
     >         / rn(i)**2
         end do
c$$$         do i2 = i1, nwf
         do i2 = 1, i1
c
c     Calculate HL0-one-electron-array to be used in H_matrix
            HL0(i1, i2) = 0d0
            if (Lp(i1) .eq. Lp(i2)) then
               call int3_8 (tmpv3, 1, nr, PP(1,i2), ipp(i2,1), ipp(i2,2),
     >            rn, 1, nr, tmp2, wn, nr)
               HL0(i1, i2) = - 0.5d0 * tmp2
               if ((i_printout .gt. 2 .and. i1 .eq. i2) .or. i_printout .gt. 3)
     >            print*, 'L=', Lp(i1),' <HL0>=',Np(i1),Np(i2),real(HL0(i1,i2))
            else
               HL0(i1, i2) = 0d0
            end if
            HL0(i2, i1) = HL0(i1, i2)
         end do
      end do
c
c
      if (i_printout .gt. 1) then
         print*, 'i_printout>1: jm'
         Print*, '            : Check definition of orbitals'
         print*, '            : Orbital numbering,N,L, m_orb(L)-memory pointer'
         write(6, 209) (i, i = 1, nwf)
         write(6, 210) (Np(i), i = 1, nwf)
         write(6, 211) (Lp(i), i = 1, nwf)
         write(6, 212) (m_orb(L), L = 0, lwf)
      end if
c
c     Set up quantum numbers of target states
c
      call target (
c     In: list1 from jm.main.f is used, see jm_2008.main.f or jm_2008.tex for help
     >   i_atom, Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
     >   lwf, nwfl, ns_exc, i_test, i_printout, chl, chll, m_orb,
c     Out:
     >   jmint, jmaxt, Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, nsht, ncft)
c
c     define clkl-array
      do l3 = 0, Lmax_par
         tmp3 = dsqrt(dble(2 * l3 + 1))
         do l2 = 0, k_par
            do l1 = 0, Lmax_par
               tmp1 = (-1d0)**l1 * dsqrt(dble(2 * l1 + 1))
c$$$               call iot3 (l1, l2, l3, 0, 0, 0, tmp2)
               tmp2 = wign3j (real(l1), real(l2), real(l3), 0.0, 0.0, 0.0)
               clkl(l1, l2, l3) = tmp2 * tmp1 * tmp3
            end do
         end do
      end do
c
c     Initialize tcs-cross section array
c
      do i_E0 = 1, n_E0
         ig = 1
         do ig1 = 1, jmaxt(LLmax(1), LSmax(1))
            tcs(ig1, ig, i_E0) = 0d0
c
c      differential cross sections
            LLt = LL_ig(ig1)
c$$$            i1 = ig1 - jmint(LLt, LSt) + 1 + LLt
            do j = 1, nth
               do m1 = -LLt, LLt
                  do m = -LLt, LLt
                     do LS = LSmin(2), LSmax(2), 2
                        tmm(ig1, m1, m, LS, j) = (0d0, 0d0)
                     end do
                  end do
               end do
            end do
         end do

      end do
c
c     Calculate Y_{lm} for the diff.crss.sections
c
      do l = 0, LLmax(2)
         tmp1 = dsqrt(dble(2*l+1))
         do m = 0, l
            tmp2 = tmp1 * dble((-1)**m) * pi_4 * dexp((fl(l-m)-fl(l+m))*0.5d0)
            do j = 1, nth
               Y_lm(j, l, m) = tmp2 * PLM(l, m, dcos(TD(j)))
            end do
         end do
      end do
c
c     test Y_lm * Y_l'm
      do l = 0, LLmax(2)
         do l1 = 0, l
            do m = 0, l1
               call int2_8 (Y_lm(1, l, m), 1, nth,
     >            Y_lm(1, l1, m), 1, nth, tmp2, wth, nth)
               tmp2 = tmp2 - ddl(l, l1)
               if(abs(tmp2) .gt. 1d-4)
     >            print*, 'l l1 m', l, l1, m, real(tmp2)
            end do
         end do
      end do

c
C     Define Target  and scattering
c     w.f's and eng's by diagonalizing the Target Hamiltonian.
      do k = 1, 2
         if (i_printout .gt. 2)
     >      write(20,80)' L, (2S+1), j1, j2, (|)=matrix elements from h_matrix'
c
         do LL = 0, LLmax(k)
            do LS = LSmin(k), LSmax(k), 2
c
               if (k .eq. 1) then
                  j_first = jmint(LL, LS)
                  j_last = jmaxt(LL, LS)
               else
c
c     Set up quantum numbers of scattering states
c
                  call scatt (
c     IN:
     >               LL, LS,
     >               i_atom, Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax,Nc,Lc,
     >               lwf, nwfl, nexcl, njml, i_test, i_printout,chl,chll,m_orb,
     >               jmint, jmaxt, nsht,
c     Out:
     >               jmax, igmax,
     >               Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, nsh, ncf,
     >               ig_igm, igm_jm, l_igm, j_ig_l, LL_ig, LS_ig)
c
c     Check if there's enough memory for U-array
c
                  print*, 'Memory check: igmax, igm_par = ', igmax, igm_par,
     >               ' and jmax, i_par = ', jmax, i_par
                  if (igmax .gt. igm_par .or. jmax .gt. i_par)
     >               stop ' increase igm_par or i_par'
c
                  j_first = 1
                  j_last = jmax
               end if
c
               i1 = 0
               do j2 = j_first, j_last
c$$$               do j1 = j_first, j_last
                  i1 = i1 + 1
                  i2 = 0
c$$$                  do j2 = j_first, j1
                  do j1 = j_first, j2
                     i2 = i2 + 1
                     call h_matrix (
c     In:
     >                  j1, j2, k, LL, LS,
     >                  i_atom, zatom, eps, nr, rn, wn, hrho,
     >                  Nnlexc, Lnlexc, Ncore, Nc, Lc,
     >                  i_printout,
     >                  Qnl(1,1,k), Nnl(1,1,k), Lnl(1,1,k), LLnl(1,1,k),
     >                  LSnl(1,1,k), LL12(1,k), LS12(1,k), nsh, nsht,
     >                  m_orb, pp, ipp, nwf, HL0, clkl, chl, chll,
c     Out:
     >                  tmpa2(i1, i2))
c
                     if (i_printout .gt. 2 .and. i1 .eq. i2) then
                        if (i1 .eq. 1 .and. i2 .eq. 1)
     >                     print*, 'k=', k, ' L=', LL, ' (2S+1)=', LS
                        print*, '<H>=', j1, j2, real(tmpa2(i1,i2))
                     end if
                     tmpa2(i2, i1) = tmpa2(i1, i2)
                  end do
               end do
               imaxLS = i1
               if (imaxLS .gt. i_par) then
                  print*, 'increase i_par from ', i_par, ' to ', imaxLS
                  stop
               end if
c
c     Print H\gamma'\gamma for debugging
c
               if (i_printout .gt. 2) then
                  print*, ' H_gamma'',n''_gamma,n:'
                  do jj = 1, imaxLS
                     write(6, 226)  (tmpa2(jj, j), j = 1, imaxLS)
                  end do
               end if
c
c     call real matrix diagonilization routine
c
               call rs(i_par, imaxLS, tmpa2, eng, 1, vec, tmpv1, tmpv2, ierr)
               if (LL .eq. 0 .and. LS .eq. LSmin(k) .and. k .eq. 2)
     >            eng_neg = eng(1)
c
c     save vec into vect(k=1)
c
               if (k .eq. 1) then
                  j0 = jmint(LL, LS) - 1
                  do jj = 1, imaxLS
                     engt(jj + j0) = eng(jj)
                     do j = 1, imaxLS
                        vect(jj + j0, j + j0) = vec(jj, j)
                     end do
                  end do
c
                  if (Egrnd .lt. 0.0 .and. j0 .eq. 0) then
                     print*, ' Calculated E ground , =', real(engt(1))
                     engt(1) = Egrnd
                     print*, ' EXPERIMENTAL E ground is used, =', real(engt(1))
                  end if
               end if
c
c     Print
c
               if ((i_printout .gt. 0 .and. k .eq. 2) .or.
     >            ( i_printout .gt. 0 .and. k .eq. 1) ) then
                  print*, 'Eng and Vec of k=', k, ' L=',LL,' (2S+1)=',LS
                  if (k .eq. 1)  then
                     write(6,205) ((engt(j+jmint(LL,LS)-1)-engt(1)) * u_e(3),
     >                  j = 1, min0(imaxLS, 20))
                     write(6,205) (engt(j+jmint(LL, LS)-1),j=1,min0(imaxLS,20))
                  end if
c
                  if (k .eq. 2) write(6, 205)
c$$$     >               (eng(j),
     >               ((eng(j) - engt(1)) * u_e(units_e),
c$$$     >               ((eng(j) - eng_neg),
     >               j = 1, min0(imaxLS, 30))
                  if (i_printout .gt. 2) then
c     Write coeff. under corresp. energies
                     write(6, 205)
                     do jj = 1, imaxLS
                        write(6, 205)  (vec(jj, j), j = 1, imaxLS)
                     end do
c
                  end if
               end if
               call flush (6)
c
               if (k .eq. 2) then
c
Cr    Calculate U that correspondes to (I.3.16, 3.16.3) from vec and vect
Cr    igm's - Gamma's for the Scattering
Cr    ig's - gamma's for the Target
Cr    j's - ordering for each LL, LS,  1...jmax
                  do igm1 = 1, igmax
                     ig1 = ig_igm(igm1)
                     LLt  = LL_ig(ig1)
                     LSt = LS_ig(ig1)
                     l1  = l_igm(igm1)
c
c     Define dgm (Eq.3.4) for (Eq.3.20) of I and (3.20.2)
c     Note: dgm=delta_n,N_l-1 * PN(n,l)
                     dgm(igm1) = PN(njml(l1) + m_orb(L1))
c
                     do i   = 1, jmax
                        tmp = 0d0
                        do ig2 = jmint(LLt, LSt), jmaxt(LLt, LSt)
                           j2 = j_ig_l(ig2, l1)
                           if (j2 .ne. 0)
     >                        tmp = tmp + vect(ig2, ig_igm(igm1)) * vec(j2, i)
                        end do
c     Redefine U (3.16.4)
                        U(igm1, i) = tmp * dgm(igm1)
                     end do
                  end do
c
c     Calculate Positions and Widths os resonances
c
                  do i2 = 1, imaxLS
                     ctmp1 = (0d0, 0d0)
                     do igm1 = 1, igmax
                        L1 = l_igm(igm1)
                        tmp1 = eng(i2) - engt(ig_igm(igm1))
c     Define k_\gamma - momentum of a \Gamma channel
                        if (tmp1 .gt. 0d0) then
                           qgm = dcmplx(dsqrt(2d0 * tmp1), 0d0)
                           call jmtrx(cjm, tmp4, njml(L1) - 1, njml(L1), 0d0,
     >                        L1,  qgm, 2d0 * alagl(L1), fl, nfl)
                           call JmTheory (s_n, c_n, njml(L1) - 1, 0d0, L1,
     >                        qgm, 2d0 * alagl(L1), eps, fl, nfl)
                           call JmTheory (s_n1, c_n1, njml(L1), 0d0, L1,
     >                        qgm, 2d0 * alagl(L1), eps, fl, nfl)
                           Ctmp1 = Ctmp1 + dcmplx(c_n1, s_n1) / dcmplx(c_n,s_n)
     >                        * cjm * U(igm1, i2)**2
                        end if
                     end do
                     if (i2 .lt. 50) then
                        print 1,
     >                     real(eng(i2) - engt(1) + real(ctmp1)) *u_e(units_e),
     >                     real(real(ctmp1)) *u_e(units_e),
     >                     -2.0 * real(imag(ctmp1)) * u_e(units_e)
c$$$  1                format (I4, $)
 1                      format (3F15.6)
                     end if
c
                  end do
c
c
c     calculate T-matrix for each LL and LS using the J-matrix method
c
                  call J_matrix (
c     In:
     >               E_0, n_E0, LL, LS, eng, engt, U, zatom, eps_2,
     >               alagl, lwf, njml, nexcl, i_test, i_printout, n_print_g,
     >               igmax, imaxLS, m_orb, fl, nfl, ig_igm, igm_jm, l_igm,
c     Out:
     >               tg, cs)
                  write(*, *)
c
c     Calculate various cross sections.
c     Sum by all possible L and S (I.4.1)
c
                  do i_E0 = 1, n_E0
                     E0 = E_0(i_E0)
c
                     write(3000, *) ' E0=', real(E0 * u_e(units_e)),
     >                  ' in ', ch_e(units_e),
     >                  ', cr.sec. in ', ch_cs(units_cs)
c
c     Calculate \sigma_{\gamma',\gamma} cross sections
c
                     do igm = 1, igmax
                        L  = l_igm(igm)
                        ig = ig_igm(igm)
                        Lgr = LL_ig(ig)
c
c     Scattering from the ground state only,
c     Proceed only if ig = 1 (ground state)
                        if (ig .eq. 1) then
                           g = 1d0 / dble(2 * (2 * Lgr + 1) * LS_ig(ig))
c
                           do igm1 = 1, igmax
                              L1 = l_igm(igm1)
                              ig1 = ig_igm(igm1)
                              LLt = LL_ig(ig1)
                              LSt = LS_ig(ig1)
                              i1 = ig1 - jmint(LLt, LSt) + 1 + LLt
c
                              if (igm_jm(igm1) * igm_jm(igm)  .eq. 1) then
                                 if (i_test .gt. 1) write(3000,*)
     >                              chll(LL), LS, i1,
     >                              chl(LLt), LSt, ', ', chl(L), chl(L1),
     >                              real(cs(igm1, L, i_E0) *
     >                              u_cs(units_cs)), ', ', igm1, igm
c
                                 tmp1 = cs(igm1, L, i_E0) * g
                                 tcs(ig1, ig, i_E0) = tcs(ig1, ig, i_E0) + tmp1
                                 if (i_E0 .le. ndiff)   print*, i1, chl(LLt),
     >                              L, L1, LL, LS,  real(tmp1)
                                 cs(igm1, L, i_E0) = 0d0
c
c     Calculate differential cross sections
      if (i_E0 .le. ndiff .and. ig1 .le. ig_par .and. LLt .le. Lt_par) then
         ctmp1 = tg(igm1, L, i_E0) * pi_4 * dsqrt(g * dble(2 * L + 1) )
         tg(igm1, L, i_E0) = (0d0, 0d0)
         do m = -min0(Lgr, LL), min0(Lgr, LL)
            do m1 = -LLt, LLt
               m2 = m - m1
               if (iabs(m2) .le. L1) then
                  ctmp2 = ctmp1 * dble(
     >   clebsg(real(LLt), real(L1), real(LL), real(m1), real(m2), real(-m))*
     >   clebsg(real(Lgr), real(L),  real(LL), real(m),  real(0),  real(-m)))
                  if (m2 .lt. 0)  ctmp2 = ctmp2 * dble((-1)**m2)
                  do j = 1, nth
                     tmm(ig1, m1, m, LS, j) = tmm(ig1, m1, m, LS, j) + ctmp2 *
     >                  Y_lm(j, L1, iabs(m2))
                  end do
               end if
            end do
         end do
      end if
c
                              end if
                           end do
                        end if
                     end do
c     The end of E-loop
                  end do
c
               end if
c
c     End of LL and LS loops
            end do
         end do
c     End k-loop
      end do
c
c     Open cross section files
c
      do LLt = 0, LLmax(1)
         do LSt = LSmin(1), LSmax(1), 2
            do ig1 = jmint(LLt, LSt), jmaxt(LLt, LSt)
               i1 = ig1 - jmint(LLt, LSt) + 1 + LLt
c
               if(i1 - LLt .le. n_print_g) then
                  write(filen, '(I1,A1,I1)') i1, chl(LLt), LSt
                  open(2000 + ig1, file=filen)
                  write(filen, '(I1,A1,I1,I1)') i1, chl(LLt), LSt, LSt
                  open(2100 + ig1, file=filen)
c
               end if
            end do
         end do
      end do
      open(4000, file='total')
c
c     Save Cross Sections
c
      do i_E0 = 1, n_E0
         E0 = E_0(i_E0)
c
         write(2000, *) ' E0=', real(E0 * u_e(units_e)),
     >      ' in ', ch_e(units_e),
     >      ', cr.sec. in ', ch_cs(units_cs)
c
         ig = 1
         Lgr = LL_ig(ig)
         tmp1 = 0d0
c
         do LLt = 0, LLmax(1)
            do LSt = LSmin(1), LSmax(1), 2
               do ig1 = jmint(LLt, LSt), jmaxt(LLt, LSt)
                  i1 = ig1 - jmint(LLt, LSt) + 1 + LLt
c
c     Find the grand total cross section
                  tmp1 = tmp1 + tcs(ig1, ig, i_e0)
c
                  if(i1 - LLt .le. n_print_g) then
c
                     write(2000+ig1, 200) E0 * u_e(units_e),
     >                  tcs(ig1, ig, i_e0) * u_cs(units_cs)
                     write(2000, *)  i1, chl(LLt),
     >                  real(tcs(ig1, ig, i_e0) * u_cs(units_cs))
                     call flush (2000+ig1)
c
c     Calculate Diff. cross sections
      if (i_E0 .le. ndiff .and. LLt .le. Lt_par) then
         do j = 1, nth
            dcs(j, ig1) = 0d0
            do LS = LSmin(2), LSmax(2), 2
               do m1 = -LLt, LLt
                  do m = -Lgr, Lgr
                     dcs(j, ig1) = dcs(j, ig1) + zabs(tmm(ig1, m1, m,LS,j))**2
                  end do
               end do
            end do
         end do
c
c     Save Diff. cross sections
         do j = 1, nth
            write(2100+ig1, 340) real(th(j)), dcs(j, ig1) * u_cs(units_dcs)
         end do
c
c     Integrate the Diff.c.s. and compare with the excitation c.s.'s
         call int1_8 (dcs(1, ig1), 1, nth, tmp2, wth, nth)
         tmp3 = tcs(ig1, ig, i_E0)
         if(abs(tmp3-tmp2) .gt. 100.*eps8)   print*, i1, chl(LLt),
     >      ' WARNING: integr.diff. ', real(tmp3), ' not eq. ', real(tmp2)
      end if
c
                  end if
               end do
            end do
         end do
c
c     Save the grand total cross section
         write(4000, 200) real(E0*u_e(units_e)), real(tmp1*u_cs(units_cs))
         call flush (4000)
c
c     This is the end of E0-loop
      end do
c
c     Free allocated dynamic memory
c$$$      call free (ptg)
c
      return
      end
*/