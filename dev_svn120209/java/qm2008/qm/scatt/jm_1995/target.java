package scatt.jm_1995;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 15:16:46
 */
public class target {
//  C     target.f  Defines Target Quantum Numbers
//  c
//  Cr    Remarks:
//  Cr    i - shell number
//  Cr    j - configuration number
//  Cr    jmint(LL,LS) and jmaxt(LL,LS)-memory position of j's for each LL and LS
//  Cr    nsht - number of shells
//  Cr    Qnl(j,i,k) - number of electrons in the i-shell in the j-configuration
//  Cr    Nnl(j,i,k) - N quantum number of the i-shell in the j-configuration
//  Cr    Lnl(j,i,k) - L quantum number of the i-shell in the j-configuration
//  Cr    LLnl(j,i,k) - total L of the i-shell in the j-configuration
//  Cr    LSnl(j,i,k) - total 2S+1 of the i-shell in the j-configuration
//  Cr    LL12(j,k) = LLnl(j,1,k) + LLnl(j,2,k) Intermediate L-coupling
//  Cr    LS12(j,k) = LSnl(j,1,k) + LSnl(j,2,k) Intermediate S-coupling
//  Cr    LLmax(k), LSmin(k), LSmax(k) - (LLmin=0 is assumed) min, max total L,2S+1
//  Cr
//  Cr    Ncore - number of core shells
//  Cr    nc(i), lc(i) - n,l's of the core shells
//  Cr
//  Cr    Nnlexc, Lnlexc - n,l's of the shell to be excited
//  Cr
//  Cr    For i_atom=2, k=2
//  Cr    Nnl and Lnl are used to define \rho's and \sigma's in h_matrix.f
//  c
//        subroutine target (
//  c     In: list1 from jm.main.f is used, see jm_2008.main.f or jm_2008.tex for help
//       >   i_atom, Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
//       >   lwf, nwfl, ns_exc, i_test, i_printout, chl, chll, m_orb,
//  c     Out:
//       >   jmint, jmaxt, Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, nsht, ncft)
//        include 'par.jm.f'
//        character*6 atom
//        character*1 chl(0:nchl_par), chll(0:nchl_par)
//        integer Nnlexc, Lnlexc, i_atom,  i_printout, ns1, ns2,
//       >   LL, LS, i, m_orb(0:Lmax_par), nlm1, nlm2, n2_max, n2_min
//        include 'formats.jm.f'
//  c
//        integer i_atom, Nnlexc, Lnlexc, lwf, i_test, i_printout,
//       >   Ncore, Lc(Ncore), Nc(Ncore),
//       >   LLmax(2), LSmin(2), LSmax(2), nwfl(0:lwf), ns_exc(0:Lmax_par)
//  c
//        integer k, k1, k2, j, n1, n2, l1, l2, ltmp,
//       >   jmint(0:Lmax_par, LSmax_par), jmaxt(0:Lmax_par, LSmax_par),
//       >   nsht, ncft, itri, idnl
//  c
//        integer
//       >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
//       >   Qnl(j_par, Nshells_par, 2),
//       >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2),
//       >   LL12(j_par, 2), LS12(j_par, 2)
//  c
//        k1 = 1
//        k2 = 2
//  c=========================================================================
//  c
//  c     H Y D R O G E N
//  c
//  c=========================================================================
//        if (i_atom .eq. 1) then
//           k = k1
//           nsht = 1
//           atom = ' Hydr.'
//           print*, ' H Y D R O G E N  is the target'
//  c
//  c        Define configurations
//  c
//           j = 1
//           do LL = 0, LLmax(k)
//              do LS = LSmin(k), LSmax(k), 2
//                 jmint(LL, LS) = j
//  c
//                 do N1 = 1 + LL, nwfl(LL) + LL
//                    nnl(j, 1, k) = N1
//                    lnl(j, 1, k) = LL
//                    qnl(j, 1, k) = 1
//                    LLnl(j, 1, k) = LL
//                    LSnl(j, 1, k) = 2
//                    j = j + 1
//                 end do
//                 jmaxt(LL, LS) = j - 1
//              end do
//           end do
//           ncft = j - 1
//        end if
//  c=========================================================================
//  c
//  c     L I T H I U M
//  c
//  c=========================================================================
//        if (i_atom .eq. 3) then
//           k = k1
//  c     3 shells are used to have Li target H_matrix calculated
//  c     using He+e scattering H_matrix
//           nsht = 3
//           atom = ' Li   '
//           print*, ' L I T H I U M  is the target'
//  c
//  c        Define configurations
//  c
//           ns1 = 1
//           ns2 = 2
//           j = 1
//           do LL = 0, LLmax(k)
//              do LS = LSmin(k), LSmax(k), 2
//                 jmint(LL, LS) = j
//  c
//  c     Only ns_exc-number of (N2s)^2 shells are used
//                 L1 = 0
//                 do N1 = 1 + L1, ns_exc(L1) + L1
//
//                    n2_min = 1 + LL
//  c$$$                  if (LL .eq. 0)  n2_min = N1 + 1 + LL
//
//                    do N2 = n2_min, nwfl(LL) + LL
//                       if (idnl(N1, 0, N2, LL) .eq. 0) then
//  c
//                          nnl(j, ns1, k) = N1
//                          lnl(j, ns1, k) = L1
//                          qnl(j, ns1, k) = 2
//                          LLnl(j, ns1, k) = 0
//                          LSnl(j, ns1, k) = 1
//  c
//                          nnl(j, ns2, k) = N2
//                          lnl(j, ns2, k) = LL
//                          qnl(j, ns2, k) = 1
//                          LLnl(j, ns2, k) = LL
//                          LSnl(j, ns2, k) = 2
//  c     Create new empty shell
//                          call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, 3, k)
//  c     LL12 and LS12
//                          LL12(j, k) = LL
//                          LS12(j, k) = 2
//  c
//                          j = j + 1
//                       end if
//                    end do
//                 end do
//                 jmaxt(LL, LS) = j - 1
//              end do
//           end do
//           ncft = j - 1
//        end if
//        if (i_atom .eq. 1 .or. i_atom .eq. 3 .and. i_printout .gt. 0) then
//           print*, 'i_printout>0: target'
//           print*, '            : k=', k, ' i_atom=', i_atom
//           print*, '            : WARNING: L=0 of the ground state is assumed'
//        end if
//  C=======================================================================
//  c
//  C     Atoms with closed shells, only one shell gets excited.
//  c
//  c     H E L I U M
//  c
//  C=======================================================================
//        if (i_atom .eq. 2 ) then
//           k = k1
//           atom = 'Closed'
//           print*, ' LsFermiSysH with CLOSED shells is the target'
//           nsht = 2
//           j = 1
//  c
//  c     Define configurations (scatt.f is used)
//  c
//           do LL = 0, LLmax(k)
//              do LS = LSmin(k), LSmax(k), 2
//                 jmint(LL, LS) = j
//  c
//  c     only nS states are allowed for the first electron
//                 L1 = 0
//                 Ltmp = 4 * L1 + 2
//  c
//  c     Only ns_exc-number of N1-states are excited for s-electrons
//  c$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//                 do N1 = 1 + L1, ns_exc(L1) + L1
//                    nlm1 = N1 - L1 + m_orb(L1)
//  c
//                    do L2 = abs(L1 - LL), min0(L1 + LL, LLmax(k))
//  c
//                       n2_max = ns_exc(L2) + L2
//                       if (N1 .eq. 1)  n2_max = nwfl(L2) + L2
//  c
//                       do N2 = L2 + 1, n2_max
//  c$$$c   this is used for 1config. matrix elements
//  c$$$                     do N2 = L2 + 1, 1
//                          nlm2 = N2 - L2 + m_orb(L2)
//  c
//                          if (N2 .eq. N1 .and. L2 .eq. L1) then
//  c     Create new empty shell
//                             call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, 2, k)
//  c     'Add one electron to already occupied shell'
//                             Qnl(j, 1, k)  = 2
//                             Nnl(j, 1, k)  = N1
//                             Lnl(j, 1, k)  = L1
//  c     Check if the created shell is not a closed shell
//                             if (Qnl(j, 1, k) .lt. Ltmp .or. LS .eq. 1) then
//                                LLnl(j, 1, k) = LL
//                                LSnl(j, 1, k) = LS
//                                j = j + 1
//                             end if
//  c
//                          else if   (nlm2 .gt. nlm1 .or. (N2 .gt. nwfl(L2) + L2
//       >                        .and. nlm2 .lt. nlm1)  ) then
//  c
//  c     k1-shell is used to creat k2-shell
//  c
//                             Qnl(j, 1, k)  = 1
//                             Nnl(j, 1, k)  = N1
//                             Lnl(j, 1, k)  = L1
//                             LLnl(j, 1, k) = L1
//                             LSnl(j, 1, k) = 2
//  c     New shell with one electron is created
//                             Qnl(j, 2, k) = 1
//                             Nnl(j, 2, k) = N2
//                             Lnl(j, 2, k) = L2
//                             LLnl(j, 2, k) = L2
//                             LSnl(j, 2, k) = 2
//                             if (itri(LS, LSnl(j, 1, k), LSnl(j, 2, k)).eq.1)then
//                                j = j + 1
//                             end if
//                          end if
//  c
//                       end do
//  c
//                    end do
//                 end do
//                 jmaxt(LL, LS) = j - 1
//              end do
//           end do
//           ncft = j - 1
//  c
//        end if
//  c
//  c----------------------------------------------------------------------------
//  c
//  c
//        if (i_printout .gt. 1) then
//           print*, 'i_printout>1: target'
//           print*, '            : # of Configurations used', ncft
//           print*, '            : # of core orbitals, Ncore=', Ncore
//  c
//           if (ncore .gt. 0) then
//              print*, '            : #, N, L of the core shells'
//              do i = 1, ncore
//                 write(6, 214) i, nc(i), lc(i)
//              end do
//           end if
//  c
//           if (i_test .gt. 1)  then
//              write(10, 80) ' Target (k=1) Quantum numbers'
//              k = 1
//              write(10, 10) ' k=', k
//              write(10, 80)
//       >         'j, L, 2S+1, i, Qnl(j,i,k), Nnl(j,i,k), Lnl(j,i,k),',
//       >         ' LLnl(j,i,k), LSnl(j,i,k)'
//              do LL = 0, LLmax(k)
//                 do LS = LSmin(k), LSmax(k), 2
//                    do j = jmint(LL, LS), jmaxt(LL, LS)
//                       write(10, 207) j, LL, LS,
//       >                  (i, Qnl(j,i,k), Nnl(j,i,k), chl(Lnl(j,i,k)),
//       >                  chll(LLnl(j,i,k)), LSnl(j,i,k),
//       >                  i = 1, min0(1,nsht))
//                       do i = 2, nsht
//                          write(10, 307)  i, Qnl(j,i,k), Nnl(j,i,k),
//       >                     chl(Lnl(j,i,k)), chll(LLnl(j,i,k)), LSnl(j,i,k)
//                       end do
//                       if (nsht .gt. 2)  write(10, 213)
//       >                  chll(LL12(j,k)), LS12(j,k)
//                       if (nsht .le. 2)  write(10, 213)
//  c
//                    end do
//                 end do
//              end do
//           end if
//  c
//        end if
//  c
//        return
//        end

}
/*
C     target.f  Defines Target Quantum Numbers
c
Cr    Remarks:
Cr    i - shell number
Cr    j - configuration number
Cr    jmint(LL,LS) and jmaxt(LL,LS)-memory position of j's for each LL and LS
Cr    nsht - number of shells
Cr    Qnl(j,i,k) - number of electrons in the i-shell in the j-configuration
Cr    Nnl(j,i,k) - N quantum number of the i-shell in the j-configuration
Cr    Lnl(j,i,k) - L quantum number of the i-shell in the j-configuration
Cr    LLnl(j,i,k) - total L of the i-shell in the j-configuration
Cr    LSnl(j,i,k) - total 2S+1 of the i-shell in the j-configuration
Cr    LL12(j,k) = LLnl(j,1,k) + LLnl(j,2,k) Intermediate L-coupling
Cr    LS12(j,k) = LSnl(j,1,k) + LSnl(j,2,k) Intermediate S-coupling
Cr    LLmax(k), LSmin(k), LSmax(k) - (LLmin=0 is assumed) min, max total L,2S+1
Cr
Cr    Ncore - number of core shells
Cr    nc(i), lc(i) - n,l's of the core shells
Cr
Cr    Nnlexc, Lnlexc - n,l's of the shell to be excited
Cr
Cr    For i_atom=2, k=2
Cr    Nnl and Lnl are used to define \rho's and \sigma's in h_matrix.f
c
      subroutine target (
c     In: list1 from jm.main.f is used, see jm_2008.main.f or jm_2008.tex for help
     >   i_atom, Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
     >   lwf, nwfl, ns_exc, i_test, i_printout, chl, chll, m_orb,
c     Out:
     >   jmint, jmaxt, Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, nsht, ncft)
      include 'par.jm.f'
      character*6 atom
      character*1 chl(0:nchl_par), chll(0:nchl_par)
      integer Nnlexc, Lnlexc, i_atom,  i_printout, ns1, ns2,
     >   LL, LS, i, m_orb(0:Lmax_par), nlm1, nlm2, n2_max, n2_min
      include 'formats.jm.f'
c
      integer i_atom, Nnlexc, Lnlexc, lwf, i_test, i_printout,
     >   Ncore, Lc(Ncore), Nc(Ncore),
     >   LLmax(2), LSmin(2), LSmax(2), nwfl(0:lwf), ns_exc(0:Lmax_par)
c
      integer k, k1, k2, j, n1, n2, l1, l2, ltmp,
     >   jmint(0:Lmax_par, LSmax_par), jmaxt(0:Lmax_par, LSmax_par),
     >   nsht, ncft, itri, idnl
c
      integer
     >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
     >   Qnl(j_par, Nshells_par, 2),
     >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2),
     >   LL12(j_par, 2), LS12(j_par, 2)
c
      k1 = 1
      k2 = 2
c=========================================================================
c
c     H Y D R O G E N
c
c=========================================================================
      if (i_atom .eq. 1) then
         k = k1
         nsht = 1
         atom = ' Hydr.'
         print*, ' H Y D R O G E N  is the target'
c
c        Define configurations
c
         j = 1
         do LL = 0, LLmax(k)
            do LS = LSmin(k), LSmax(k), 2
               jmint(LL, LS) = j
c
               do N1 = 1 + LL, nwfl(LL) + LL
                  nnl(j, 1, k) = N1
                  lnl(j, 1, k) = LL
                  qnl(j, 1, k) = 1
                  LLnl(j, 1, k) = LL
                  LSnl(j, 1, k) = 2
                  j = j + 1
               end do
               jmaxt(LL, LS) = j - 1
            end do
         end do
         ncft = j - 1
      end if
c=========================================================================
c
c     L I T H I U M
c
c=========================================================================
      if (i_atom .eq. 3) then
         k = k1
c     3 shells are used to have Li target H_matrix calculated
c     using He+e scattering H_matrix
         nsht = 3
         atom = ' Li   '
         print*, ' L I T H I U M  is the target'
c
c        Define configurations
c
         ns1 = 1
         ns2 = 2
         j = 1
         do LL = 0, LLmax(k)
            do LS = LSmin(k), LSmax(k), 2
               jmint(LL, LS) = j
c
c     Only ns_exc-number of (N2s)^2 shells are used
               L1 = 0
               do N1 = 1 + L1, ns_exc(L1) + L1

                  n2_min = 1 + LL
c$$$                  if (LL .eq. 0)  n2_min = N1 + 1 + LL

                  do N2 = n2_min, nwfl(LL) + LL
                     if (idnl(N1, 0, N2, LL) .eq. 0) then
c
                        nnl(j, ns1, k) = N1
                        lnl(j, ns1, k) = L1
                        qnl(j, ns1, k) = 2
                        LLnl(j, ns1, k) = 0
                        LSnl(j, ns1, k) = 1
c
                        nnl(j, ns2, k) = N2
                        lnl(j, ns2, k) = LL
                        qnl(j, ns2, k) = 1
                        LLnl(j, ns2, k) = LL
                        LSnl(j, ns2, k) = 2
c     Create new empty shell
                        call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, 3, k)
c     LL12 and LS12
                        LL12(j, k) = LL
                        LS12(j, k) = 2
c
                        j = j + 1
                     end if
                  end do
               end do
               jmaxt(LL, LS) = j - 1
            end do
         end do
         ncft = j - 1
      end if
      if (i_atom .eq. 1 .or. i_atom .eq. 3 .and. i_printout .gt. 0) then
         print*, 'i_printout>0: target'
         print*, '            : k=', k, ' i_atom=', i_atom
         print*, '            : WARNING: L=0 of the ground state is assumed'
      end if
C=======================================================================
c
C     Atoms with closed shells, only one shell gets excited.
c
c     H E L I U M
c
C=======================================================================
      if (i_atom .eq. 2 ) then
         k = k1
         atom = 'Closed'
         print*, ' LsFermiSysH with CLOSED shells is the target'
         nsht = 2
         j = 1
c
c     Define configurations (scatt.f is used)
c
         do LL = 0, LLmax(k)
            do LS = LSmin(k), LSmax(k), 2
               jmint(LL, LS) = j
c
c     only nS states are allowed for the first electron
               L1 = 0
               Ltmp = 4 * L1 + 2
c
c     Only ns_exc-number of N1-states are excited for s-electrons
c$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
               do N1 = 1 + L1, ns_exc(L1) + L1
                  nlm1 = N1 - L1 + m_orb(L1)
c
                  do L2 = abs(L1 - LL), min0(L1 + LL, LLmax(k))
c
                     n2_max = ns_exc(L2) + L2
                     if (N1 .eq. 1)  n2_max = nwfl(L2) + L2
c
                     do N2 = L2 + 1, n2_max
c$$$c   this is used for 1config. matrix elements
c$$$                     do N2 = L2 + 1, 1
                        nlm2 = N2 - L2 + m_orb(L2)
c
                        if (N2 .eq. N1 .and. L2 .eq. L1) then
c     Create new empty shell
                           call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, 2, k)
c     'Add one electron to already occupied shell'
                           Qnl(j, 1, k)  = 2
                           Nnl(j, 1, k)  = N1
                           Lnl(j, 1, k)  = L1
c     Check if the created shell is not a closed shell
                           if (Qnl(j, 1, k) .lt. Ltmp .or. LS .eq. 1) then
                              LLnl(j, 1, k) = LL
                              LSnl(j, 1, k) = LS
                              j = j + 1
                           end if
c
                        else if   (nlm2 .gt. nlm1 .or. (N2 .gt. nwfl(L2) + L2
     >                        .and. nlm2 .lt. nlm1)  ) then
c
c     k1-shell is used to creat k2-shell
c
                           Qnl(j, 1, k)  = 1
                           Nnl(j, 1, k)  = N1
                           Lnl(j, 1, k)  = L1
                           LLnl(j, 1, k) = L1
                           LSnl(j, 1, k) = 2
c     New shell with one electron is created
                           Qnl(j, 2, k) = 1
                           Nnl(j, 2, k) = N2
                           Lnl(j, 2, k) = L2
                           LLnl(j, 2, k) = L2
                           LSnl(j, 2, k) = 2
                           if (itri(LS, LSnl(j, 1, k), LSnl(j, 2, k)).eq.1)then
                              j = j + 1
                           end if
                        end if
c
                     end do
c
                  end do
               end do
               jmaxt(LL, LS) = j - 1
            end do
         end do
         ncft = j - 1
c
      end if
c
c----------------------------------------------------------------------------
c
c
      if (i_printout .gt. 1) then
         print*, 'i_printout>1: target'
         print*, '            : # of Configurations used', ncft
         print*, '            : # of core orbitals, Ncore=', Ncore
c
         if (ncore .gt. 0) then
            print*, '            : #, N, L of the core shells'
            do i = 1, ncore
               write(6, 214) i, nc(i), lc(i)
            end do
         end if
c
         if (i_test .gt. 1)  then
            write(10, 80) ' Target (k=1) Quantum numbers'
            k = 1
            write(10, 10) ' k=', k
            write(10, 80)
     >         'j, L, 2S+1, i, Qnl(j,i,k), Nnl(j,i,k), Lnl(j,i,k),',
     >         ' LLnl(j,i,k), LSnl(j,i,k)'
            do LL = 0, LLmax(k)
               do LS = LSmin(k), LSmax(k), 2
                  do j = jmint(LL, LS), jmaxt(LL, LS)
                     write(10, 207) j, LL, LS,
     >                  (i, Qnl(j,i,k), Nnl(j,i,k), chl(Lnl(j,i,k)),
     >                  chll(LLnl(j,i,k)), LSnl(j,i,k),
     >                  i = 1, min0(1,nsht))
                     do i = 2, nsht
                        write(10, 307)  i, Qnl(j,i,k), Nnl(j,i,k),
     >                     chl(Lnl(j,i,k)), chll(LLnl(j,i,k)), LSnl(j,i,k)
                     end do
                     if (nsht .gt. 2)  write(10, 213)
     >                  chll(LL12(j,k)), LS12(j,k)
                     if (nsht .le. 2)  write(10, 213)
c
                  end do
               end do
            end do
         end if
c
      end if
c
      return
      end

 */
