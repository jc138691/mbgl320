package scatt.jm_1995;

import static scatt.jm_1995.par_jm.*;  //http://www.deitel.com/articles/java_tutorials/20060211/index.html


/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 14:56:37
 //        PROGRAM JM
 //  C     ------------------------------------------------------------------
 //  C     M A I N   J-matrix P R O G R A M
 //  C     ------------------------------------------------------------------
 */
public class jm_main {
  //  C     Execute by           main < input.jm
  public static void main(String[] args) {
    int Ncore_par = 2; //        integer Ncore_par //        parameter (Ncore_par = 2)
    // see import static ... //        include 'par.jm.f'
//        include 'formats.jm.f'
    char[] ch_e = {' ', ' ', ' ', ' '}; //        character*2 ch_e(nu_par)
    String u_e[] = {"au", "Ry", "eV", "  "}; //        data u_e /'au', 'Ry', 'eV', '  '/
//        character*8 ch_cs(nu_par), chtmp
    String ch_cs[] = {"a.u.", "\\pi*a.u.", "e-16cm^2", "4*\\pi*au"}; //        data ch_cs /'a.u.', '\pi*a.u.', 'e-16cm^2', '4*\pi*au'/
    int i, i2, i_test, i_printout, i_stop, n_interv, n_print_g, n_e0,
      units_cs, units_dcs, units_e, ndiff, i_E0, l, lwf,  nth;
    double  Egrnd, au, Ry, E_step, E0_min, E0_max,  pi, Zion, tmp, thi, thinc, h;
    double[]  E_0 = new double[ne_par];
    double[]  alagl = new double[Lmax_par];
//    double[]  u_e = new double[nu_par];
    double[]  u_cs = new double[nu_par];
    double[]  TH = new double[nth_par];
    double[]  td = new double[nth_par];
    double[]  wth = new double[nth_par];

//        namelist /list1/
//       >   i_atom, zatom, Zion, eps, nr, rmin, rmax,
//       >   Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
//       >   alagl, n_print_g, units_cs, units_dcs,
//       >   Egrnd, i_test, i_printout, i_stop
//  Ci
//  Ci    Inputs:
//  Ci    Zatom - atomZ of the target
//  Ci    Zion  - used to create a better start for
//  Ci            the ground states orbitals of the target
//  Ci    nr - # of points in r-grid
//  Ci    rmin, rmax - min, max of the r-grid
//  Ci    LLmax - max L of target (1) and  scattering (2) wf
//  Ci    LSmin, LSmax - min, max 2S+1 of target (1) and scattering (2) wf
//  Ci    Ncore - number of core shells
//  Ci    nc(k), lc(k) - n,l's of the core shells
//  Ci    Nnlexc, Lnlexc - n,l's of the shell to be excited
//  Ci
//  Ci    LWF - max L of the lgrrN size
//  Ci    NWFL - lgrrN size for each L above the core states
//  Ci    ns_exc - maximum number of core excitations for l- electrons
//  Ci    njml - maximum n in j-matrix calculation for each L, reserved for
//  Ci           future use
//  Ci    nexcl - only nexcl(L) states will be used after diagonalization
//  Ci
//  Ci    n_print_g - # of first \gamma's for which the corresponding cross
//  Ci                 sections are saved on disk.
//  Ci
//  Ci
//        READ (5, *)
//        READ (5, list1)
//  c
//        read(5, *) l, chtmp, (nwfl(i), i = 0, l),
//       >   chtmp, (ns_exc(i2), i2 = 0, l)
//        do i = l+1, Lmax_par
//           nwfl(i)   = nwfl(l)
//           ns_exc(i) = ns_exc(l)
//        end do
//        read(5, *) l, chtmp, (njml(i), i = 0, l),
//       >   chtmp, (nexcl(i2), i2 = 0, l)
//        do i = l+1, Lmax_par
//           njml(i)  = njml(l)
//           nexcl(i) = nexcl(l)
//        end do
//  c
//  c     Check that
//        call check_par (n_print_g, ig_par, 'n_print_g', 'ig_par')
//        call check_par (n_print_g, ig_par, 'n_print_g', 'ig_par')
//  c
//  c
//        lwf = max0(LLmax(1), LLmax(2))
//        do l = 0, lwf
//  c
//           if (njml(l) .le. nwfl(l))   then
//              njml(l) = nwfl(l) + 1
//              print*, 'njml(', l, ') is reset to its minimum = nwfl(l)+1'
//           end if
//  c
//           call check_par (nexcl(l), nwfl(l), 'nexcl(l)', 'nwfl(l)')
//           call check_par (max0(nwfl(l), njml(l)), n_par, 'njml(l)', 'n_par')
//  c
//        end do
//  c
//        call check_par (lwf, Lmax_par, 'lwf', 'Lmax_par')
//        call check_par (LLmax(2), lwf, 'Lmax(2)', 'lwf', )
//  c
//        print*, ' J-matrix calculation of:'
//        if (i_atom .eq. 1) then
//           if (LSmin(1) .ne. 2) then
//              print*, ' WARNING: ===> LSmin(1) is reset to 2 in _jm.main'
//              LSmin(1) = 2
//              if (LSmax(1) .lt. LSmin(1)) then
//                 print*, ' WARNING===> LSmax(1) is reset to LSmin(1) in _jm.main'
//                 LSmax(1) = LSmin(1)
//              end if
//           end if
//           if (Ncore .ne. 0 .or. Nnlexc .ne. 1 .or. Lnlexc .ne. 0) then
//              print*, ' WARNING: ===> reset Ncore=0, Nnlexc=1,',
//       >         ' Lnlexc=0  in _jm.main'
//              Ncore = 0
//              Nnlexc = 1
//              Lnlexc = 0
//           end if
//        end if
//  c     LS = 2S+1, S - Total "scattering" spin, from LSmin to LSmax
//  c     LL           - Total "scattering" angular momentum, from LLmin to LLmax
//  c     Check list1
//        call check_par (LLmax(2), Lmax_par, 'LLmax(2)', 'Lmax_par')
//        call check_par (LLmax(1), Lmax_par, 'LLmax(1)', 'Lmax_par')
//        call check_par (LSmax(1), LSmax_par, 'LSmax(1)', 'LSmax_par')
//        call check_par (LSmax(2), LSmax_par, 'LSmax(2)', 'LSmax_par')
//        if (abs(LSmin(2) - LSmin(1)) .ne. 1 .or. LSmin(1) .lt. 1
//       >   .or. LSmin(2) .lt. 1) then
//           print*, ' LSmin(1), LSmin(2)=', LSmin(1), LSmin(2)
//           stop ' Check: LSmin(1), LSmin(2) in input.jm'
//        end if
//        if (LLmax(2) .lt. LLmax(1))
//       >   print*, ' WARNING: ===> LLmax(2) .lt. LLmax(1), OK?'
//        if (abs(LSmax(2) - LSmax(1)) .ne. 1)
//       >   print*, ' WARNING: ===> LSmax(2) - LSmax(1) .ne. 1, OK?'
//  c
//        pi = dacos(-1d0)
//        au = 27.212
//        Ry = au / 2d0
//  c
//        u_e(1) = 1d0
//        u_e(2) = 2d0
//        u_e(3) = au
//  c
//        u_cs(1) = 1d0
//        u_cs(2) = 1d0 / pi
//        u_cs(4) = 1d0 / pi / 4d0
//        u_cs(3) = 0.52918**2
//  c
//  c
//  c     Incident Scattering energy in units_e
//        READ (5, *)
//        READ (5, *)
//        i2 = 0
//        READ (5, *)  units_e, n_interv
//        READ (5, *)
//        do i = 1, n_interv
//           READ (5, *) E0_min, E0_max, n_E0
//  c     Convert into a.u.
//           E0_min = E0_min / u_e(units_e)
//           E0_max = E0_max / u_e(units_e)
//           E_step = 0d0
//           if (n_E0 .ne. 1)  E_step = (E0_max - E0_min) / dble(n_E0 - 1)
//           do i_E0 = 1, n_E0
//              i2 = i2 + 1
//              E_0(i2) = E0_min + dble(i_E0 - 1) * E_step
//           end do
//        end do
//        n_e0 = i2
//  c
//  c
//        READ (5, *) ndiff
//  c
//  C     Read variable angle data,       NTH: Number of angles
//  C     THI: Initial angle (degrees),   THINC: Angular increment (degrees)
//  c     TD - in radians
//        READ(5, *) NTH, THI, THINC
//        call check_par (ndiff, 1, 'ndiff', 'not ready')
//        call check_par (n_E0, ne_par, 'n_E0', 'ne_par')
//        call check_par (nth, nth_par, 'nth', 'nth_par')
//        tmp = pi / 180.0d0
//        DO  I = 1, NTH
//           TH(I) = THI + (I-1) * THINC
//           TD(I)  = TH(I) * tmp
//        end do
//  c
//  c     define Simpson's weights to be used for angular integrations
//        if (mod(nth,2) .eq. 0) stop ' nth must be odd, stoped in jm.main'
//        h = td(2) - td(1)
//        tmp = 2d0 * h / 3d0
//        tmp = tmp * pi * 2d0
//        do I = 1, NTH
//           wth(i) = dble(mod(i+1, 2) + 1) * tmp * dsin(td(i))
//        end do
//        wth(1)   = wth(1) / 2d0
//        wth(nth) = wth(nth) / 2d0
//  c
//        if (i_printout .gt. 0) then
//           print*, 'i_printout>0:  jm.main'
//           write(6, list1)
//           print*, n_e0,
//       >      ' # of energies to be calculated. E_0(1) and E_0(n_e0) in a.u.'
//           write(6, 204) E_0(1), E_0(n_e0)
//        end if
//  c
//  c     Calculate Target and Scattering Hamiltonian matrixes
//  c
//        call jm (
//  c     In: list1 from jm.main.f is used
//       >   i_atom, zatom, Zion, eps, nr, rmin, rmax,
//       >   Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
//       >   alagl, Egrnd, E_0, n_E0, ndiff, lwf, nwfl, ns_exc,
//       >   nexcl, njml, i_test, i_printout,
//       >   i_stop, n_print_g,
//       >   u_e, u_cs, ch_e, ch_cs, units_cs, units_dcs, units_e,
//       >   nth, th, td, wth)
//  c
//  c
//        if (i_printout .gt. 0) then
//           print*, 'i_printout>0:'
//           print*, '            : phase shifts(i, 1) #1 are in fort.1100+i'
//  c$$$         print*, '            : phase shifts(i, 1) #2 are in fort.1200+i'
//           print*, '            : Cross sections (i, 1), are in fort.2000+i'
//  c$$$         print*, '            : Total Cross sections, are in fort.4000'
//  c$$$         print*, '            : T(i, 1)-matrix, are in fort.5000+i'
//        end if
//  c
//        if (i_printout .gt. 2)  then
//           print*, 'i_printout>2: jm'
//           print*, '            :',
//       >      ' write(20,80) L, (2S+1), j1, j2, (|)=matrix elemt. from h_matrix'
//           print*, '            : j_matrix'
//           print*, '            :',
//       >      ' write(1200, 225) (S(igm1, igm), igm = 1, igmaxLS)'
//        else if (i_printout .gt. 3)  then
//           print*, 'i_printout>3: jm'
//           print*, '            : write(100+i, 200) rn(j), PP(j,i+mm) * r5(j)'
//           print*, '            :',
//       >      ' write(3000+100*k+10*i+i2, 200)  rn(j), yk2(j, i, i2, k)'
//        end if
//  c
//        if (i_test .gt. 1) then
//           print*, 'i_test>1: j_matrix'
//           print*, '        : Phase shifts --> 1100+, 1201+igm1'
//           print*, '        : Partial dcr.sections --> 3000'
//           print*, '        : ',
//       >      'fort.10 <--Target (k=1) and Scattering (k=2) Quantum numbers'
//        else if (i_test .gt. 3) then
//           print*, 'i_test>3: jm'
//           print*, '        : fort.40 <- <- Y0(1s,1s,r), alpha=1'
//           print*, '        : fort.42 <- <- Y0(2p,2p,r), alpha=1'
//        end if
//  c
//        end
//
  }

}
// original jm.main.f
/*
C
C     Execute by           main < input.jm
C
      PROGRAM JM
C     ------------------------------------------------------------------
C     M A I N   J-matrix P R O G R A M
C     ------------------------------------------------------------------
      integer Ncore_par
      parameter (Ncore_par = 2)
c
      include 'par.jm.f'
      include 'formats.jm.f'
c
      character*2 ch_e(nu_par)
      data u_e /'au', 'Ry', 'eV', '  '/
      character*8 ch_cs(nu_par), chtmp
      data ch_cs /'a.u.', '\pi*a.u.', 'e-16cm^2', '4*\pi*au'/
      integer
     >   i, i2, i_test, i_printout, i_stop, n_interv, n_print_g, n_e0,
     >   units_cs, units_dcs, units_e, ndiff, i_E0, l,
     >   i_atom, nr, Nnlexc, Lnlexc, lwf,
     >   Ncore, Lc(Ncore_par), Nc(Ncore_par),
     >   LLmax(2), LSmin(2), LSmax(2), nwfl(0:Lmax_par), nexcl(0:Lmax_par),
     >   njml(0:Lmax_par), ns_exc(0:Lmax_par), nth
c
      real*8
     >   Egrnd, au, Ry, E_step, E0_min, E0_max,
     >   E_0(ne_par),  pi,
     >   zatom, Zion, eps, rmin, rmax, alagl(0:Lmax_par), tmp,
     >   u_e(nu_par), u_cs(nu_par),
     >   TH(nth_par), td(nth_par), wth(nth_par), thi, thinc, h
c
      namelist /list1/
     >   i_atom, zatom, Zion, eps, nr, rmin, rmax,
     >   Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
     >   alagl, n_print_g, units_cs, units_dcs,
     >   Egrnd, i_test, i_printout, i_stop
Ci
Ci    Inputs:
Ci    Zatom - atomZ of the target
Ci    Zion  - used to create a better start for
Ci            the ground states orbitals of the target
Ci    nr - # of points in r-grid
Ci    rmin, rmax - min, max of the r-grid
Ci    LLmax - max L of target (1) and  scattering (2) wf
Ci    LSmin, LSmax - min, max 2S+1 of target (1) and scattering (2) wf
Ci    Ncore - number of core shells
Ci    nc(k), lc(k) - n,l's of the core shells
Ci    Nnlexc, Lnlexc - n,l's of the shell to be excited
Ci
Ci    LWF - max L of the lgrrN size
Ci    NWFL - lgrrN size for each L above the core states
Ci    ns_exc - maximum number of core excitations for l- electrons
Ci    njml - maximum n in j-matrix calculation for each L, reserved for
Ci           future use
Ci    nexcl - only nexcl(L) states will be used after diagonalization
Ci
Ci    n_print_g - # of first \gamma's for which the corresponding cross
Ci                 sections are saved on disk.
Ci
Ci
      READ (5, *)
      READ (5, list1)
c
      read(5, *) l, chtmp, (nwfl(i), i = 0, l),
     >   chtmp, (ns_exc(i2), i2 = 0, l)
      do i = l+1, Lmax_par
         nwfl(i)   = nwfl(l)
         ns_exc(i) = ns_exc(l)
      end do
      read(5, *) l, chtmp, (njml(i), i = 0, l),
     >   chtmp, (nexcl(i2), i2 = 0, l)
      do i = l+1, Lmax_par
         njml(i)  = njml(l)
         nexcl(i) = nexcl(l)
      end do
c
c     Check that
      call check_par (n_print_g, ig_par, 'n_print_g', 'ig_par')
      call check_par (n_print_g, ig_par, 'n_print_g', 'ig_par')
c
c
      lwf = max0(LLmax(1), LLmax(2))
      do l = 0, lwf
c
         if (njml(l) .le. nwfl(l))   then
            njml(l) = nwfl(l) + 1
            print*, 'njml(', l, ') is reset to its minimum = nwfl(l)+1'
         end if
c
         call check_par (nexcl(l), nwfl(l), 'nexcl(l)', 'nwfl(l)')
         call check_par (max0(nwfl(l), njml(l)), n_par, 'njml(l)', 'n_par')
c
      end do
c
      call check_par (lwf, Lmax_par, 'lwf', 'Lmax_par')
      call check_par (LLmax(2), lwf, 'Lmax(2)', 'lwf', )
c
      print*, ' J-matrix calculation of:'
      if (i_atom .eq. 1) then
         if (LSmin(1) .ne. 2) then
            print*, ' WARNING: ===> LSmin(1) is reset to 2 in _jm.main'
            LSmin(1) = 2
            if (LSmax(1) .lt. LSmin(1)) then
               print*, ' WARNING===> LSmax(1) is reset to LSmin(1) in _jm.main'
               LSmax(1) = LSmin(1)
            end if
         end if
         if (Ncore .ne. 0 .or. Nnlexc .ne. 1 .or. Lnlexc .ne. 0) then
            print*, ' WARNING: ===> reset Ncore=0, Nnlexc=1,',
     >         ' Lnlexc=0  in _jm.main'
            Ncore = 0
            Nnlexc = 1
            Lnlexc = 0
         end if
      end if
c     LS = 2S+1, S - Total "scattering" spin, from LSmin to LSmax
c     LL           - Total "scattering" angular momentum, from LLmin to LLmax
c     Check list1
      call check_par (LLmax(2), Lmax_par, 'LLmax(2)', 'Lmax_par')
      call check_par (LLmax(1), Lmax_par, 'LLmax(1)', 'Lmax_par')
      call check_par (LSmax(1), LSmax_par, 'LSmax(1)', 'LSmax_par')
      call check_par (LSmax(2), LSmax_par, 'LSmax(2)', 'LSmax_par')
      if (abs(LSmin(2) - LSmin(1)) .ne. 1 .or. LSmin(1) .lt. 1
     >   .or. LSmin(2) .lt. 1) then
         print*, ' LSmin(1), LSmin(2)=', LSmin(1), LSmin(2)
         stop ' Check: LSmin(1), LSmin(2) in input.jm'
      end if
      if (LLmax(2) .lt. LLmax(1))
     >   print*, ' WARNING: ===> LLmax(2) .lt. LLmax(1), OK?'
      if (abs(LSmax(2) - LSmax(1)) .ne. 1)
     >   print*, ' WARNING: ===> LSmax(2) - LSmax(1) .ne. 1, OK?'
c
      pi = dacos(-1d0)
      au = 27.212
      Ry = au / 2d0
c
      u_e(1) = 1d0
      u_e(2) = 2d0
      u_e(3) = au
c
      u_cs(1) = 1d0
      u_cs(2) = 1d0 / pi
      u_cs(4) = 1d0 / pi / 4d0
      u_cs(3) = 0.52918**2
c
c
c     Incident Scattering energy in units_e
      READ (5, *)
      READ (5, *)
      i2 = 0
      READ (5, *)  units_e, n_interv
      READ (5, *)
      do i = 1, n_interv
         READ (5, *) E0_min, E0_max, n_E0
c     Convert into a.u.
         E0_min = E0_min / u_e(units_e)
         E0_max = E0_max / u_e(units_e)
         E_step = 0d0
         if (n_E0 .ne. 1)  E_step = (E0_max - E0_min) / dble(n_E0 - 1)
         do i_E0 = 1, n_E0
            i2 = i2 + 1
            E_0(i2) = E0_min + dble(i_E0 - 1) * E_step
         end do
      end do
      n_e0 = i2
c
c
      READ (5, *) ndiff
c
C     Read variable angle data,       NTH: Number of angles
C     THI: Initial angle (degrees),   THINC: Angular increment (degrees)
c     TD - in radians
      READ(5, *) NTH, THI, THINC
      call check_par (ndiff, 1, 'ndiff', 'not ready')
      call check_par (n_E0, ne_par, 'n_E0', 'ne_par')
      call check_par (nth, nth_par, 'nth', 'nth_par')
      tmp = pi / 180.0d0
      DO  I = 1, NTH
         TH(I) = THI + (I-1) * THINC
         TD(I)  = TH(I) * tmp
      end do
c
c     define Simpson's weights to be used for angular integrations
      if (mod(nth,2) .eq. 0) stop ' nth must be odd, stoped in jm.main'
      h = td(2) - td(1)
      tmp = 2d0 * h / 3d0
      tmp = tmp * pi * 2d0
      do I = 1, NTH
         wth(i) = dble(mod(i+1, 2) + 1) * tmp * dsin(td(i))
      end do
      wth(1)   = wth(1) / 2d0
      wth(nth) = wth(nth) / 2d0
c
      if (i_printout .gt. 0) then
         print*, 'i_printout>0:  jm.main'
         write(6, list1)
         print*, n_e0,
     >      ' # of energies to be calculated. E_0(1) and E_0(n_e0) in a.u.'
         write(6, 204) E_0(1), E_0(n_e0)
      end if
c
c     Calculate Target and Scattering Hamiltonian matrixes
c
      call jm (
c     In: list1 from jm.main.f is used
     >   i_atom, zatom, Zion, eps, nr, rmin, rmax,
     >   Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
     >   alagl, Egrnd, E_0, n_E0, ndiff, lwf, nwfl, ns_exc,
     >   nexcl, njml, i_test, i_printout,
     >   i_stop, n_print_g,
     >   u_e, u_cs, ch_e, ch_cs, units_cs, units_dcs, units_e,
     >   nth, th, td, wth)
c
c
      if (i_printout .gt. 0) then
         print*, 'i_printout>0:'
         print*, '            : phase shifts(i, 1) #1 are in fort.1100+i'
c$$$         print*, '            : phase shifts(i, 1) #2 are in fort.1200+i'
         print*, '            : Cross sections (i, 1), are in fort.2000+i'
c$$$         print*, '            : Total Cross sections, are in fort.4000'
c$$$         print*, '            : T(i, 1)-matrix, are in fort.5000+i'
      end if
c
      if (i_printout .gt. 2)  then
         print*, 'i_printout>2: jm'
         print*, '            :',
     >      ' write(20,80) L, (2S+1), j1, j2, (|)=matrix elemt. from h_matrix'
         print*, '            : j_matrix'
         print*, '            :',
     >      ' write(1200, 225) (S(igm1, igm), igm = 1, igmaxLS)'
      else if (i_printout .gt. 3)  then
         print*, 'i_printout>3: jm'
         print*, '            : write(100+i, 200) rn(j), PP(j,i+mm) * r5(j)'
         print*, '            :',
     >      ' write(3000+100*k+10*i+i2, 200)  rn(j), yk2(j, i, i2, k)'
      end if
c
      if (i_test .gt. 1) then
         print*, 'i_test>1: j_matrix'
         print*, '        : Phase shifts --> 1100+, 1201+igm1'
         print*, '        : Partial dcr.sections --> 3000'
         print*, '        : ',
     >      'fort.10 <--Target (k=1) and Scattering (k=2) Quantum numbers'
      else if (i_test .gt. 3) then
         print*, 'i_test>3: jm'
         print*, '        : fort.40 <- <- Y0(1s,1s,r), alpha=1'
         print*, '        : fort.42 <- <- Y0(2p,2p,r), alpha=1'
      end if
c
      end


 */
