package scatt.jm_1995;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 15:17:58
 */
public class scatt {
//  C     scatt.f  Defines Scattering Quantum Numbers
//  c
//  Cr    Remarks:
//  Cr    jmax-memory position of j's for each LL and LS
//  Cr    igmax - memory position of igm's for each LL and LS
//  Cr    nsh - number of shells
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
//        subroutine scatt (
//  c     IN:
//       >   LL, LS,
//       >   i_atom, Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
//       >   lwf, nwfl, nexcl, njml, i_test, i_printout, chl, chll, m_orb,
//       >   jmint, jmaxt, nsht,
//  c     Out:
//       >   jmax, igmax,
//       >   Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, nsh, ncf,
//       >   ig_igm, igm_jm, l_igm, j_ig_l, LL_ig, LS_ig)
//        include 'par.jm.f'
//        character*1 chl(0:nchl_par), chll(0:nchl_par), igm_used(0:1)
//        data igm_used /'-', '+'/
//        integer Nnlexc, Lnlexc, i_atom,  i_printout,
//       >   LL, LS, LL2, i, m_orb(0:Lmax_par), m, itri, newcon
//        include 'formats.jm.f'
//  c
//        integer i_atom, Nnlexc, Lnlexc, lwf, i_test, i_printout,
//       >   Ncore, Lc(Ncore), Nc(Ncore),
//       >   LLmax(2), LSmin(2), LSmax(2), nwfl(0:lwf),
//       >   nexcl(0:lwf), njml(0:lwf), nlm2, nlm1, ns1, ns2, ns3, Ncl,
//       >   ig_igm(igm_par),  igm_jm(igm_par),  l_igm(igm_par),
//       >   igm, LL_ig(igm_par), LS_ig(igm_par)
//  c
//        integer k, k1, k2, j, ig, n1, n2, l1, l2, Ltmp, ls1, ls2,
//       >   jmax, Lt, LSt, igm_yes,
//       >   jmint(0:Lmax_par, LSmax_par), jmaxt(0:Lmax_par, LSmax_par),
//       >   igmax, nsh, nsht, ncf, j_ig_l(igm_par, 0:Lmax_par)
//  c
//        integer
//       >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
//       >   Qnl(j_par, Nshells_par, 2),
//       >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2),
//       >   LL12(j_par, 2), LS12(j_par, 2)
//  c
//        k1 = 1
//        k2 = 2
//        nsh = nsht + 1
//        j = 1
//  C==========================================================================
//  c
//  c     H Y D R O G E N,   L I T H I U M,
//  c
//  C     Define Scattering configurations by adding one electron to (k=1)
//  c
//        if ((i_atom .eq. 1 .and. nsht .eq. 1) .or.
//       >   i_atom .eq. 3) then
//           if (i_atom .eq. 1) then
//              Print*, ' TARGET: --> Hydrogen'
//              nsh = nsht + 1
//              ns1 = 0
//              ns2 = 1
//              ns3 = 2
//           else if (i_atom .eq. 3) then
//              Print*, ' TARGET: --> Lithium'
//              Print*, '       : LL12 and LS12 denotes coupling above clsd.shlls'
//              nsh = nsht
//              ns1 = 1
//              ns2 = 2
//              ns3 = 3
//           end if
//  c
//  c     Sums by the Target L and 2S+1
//           igm = 0
//           do Lt = 0, LLmax(k1)
//              L1 = Lt
//              Ltmp = 4 * Lt + 2
//              do LSt = LSmin(k1), LSmax(k1), 2
//                 do ig = jmint(Lt, LSt), jmaxt(Lt, LSt)
//                    LL_ig(ig)  = Lt
//                    LS_ig(ig)  = LSt
//  c
//                    Ncl = 0
//                    if (i_atom .eq. 3) Ncl = Nnl(ig, ns1, k1)
//  c     Assume that Lcl=0
//  c$$$                  nlm0 = Ncl
//                    N1 = Nnl(ig, ns2, k1)
//                    nlm1 = N1 - Lt + m_orb(Lt)
//  c
//  c     do L2 = Lmin, Lmax, 2 ; ,2-is due to the conservation of parity!!!!
//  c     Only w.f. of the same parity are coupled. In the case of hydrogen,
//  c     it will be (-)^LL
//                    do L2 = abs(Lt - LL), min0(Lt + LL, LLmax(k2)), 2
//                    if (itri(LSt, 2, LS) .eq. 1 .and. Ncl .ne. nlm1) then
//  c
//                       igm_yes = 0
//                       j_ig_l(ig, L2) = 0
//  c
//                       do N2 = L2 + 1, njml(L2) + L2
//                          nlm2 = N2 - L2 + m_orb(L2)
//  c
//                          if (nlm1 .eq. nlm2 .and. Ncl .lt. nlm2) then
//  c
//                             if (i_atom .eq. 3)  call copysh (Qnl, Nnl, Lnl,
//       >                        LLnl, LSnl, ig, j, ns1, ns1, k1, k2)
//  c
//  c     Create new empty shell
//                             call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, ns3, k2)
//  c     Add one electron to already occupied shell
//                             Qnl(j, ns2, k2)  = Qnl(ig, ns2, k1) + 1
//                             Nnl(j, ns2, k2)  = N1
//                             Lnl(j, ns2, k2)  = Lt
//  c
//  c     Check if the created shell is not a closed shell
//                             if (Qnl(j, ns2, k2) .lt. Ltmp .or. LS .eq. 1) then
//                                LLnl(j, ns2, k2) = LL
//                                LSnl(j, ns2, k2) = LS
//  c     LL12 and LS12
//                                LL12(j, k2) = LL
//                                LS12(j, k2) = LS
//  c     memory pointers between igm,N2 and j
//                                if (n2 .eq. njml(L2) + L2)  igm_yes = 1
//                                j = j + 1
//                             end if
//  c
//                          else if (Ncl .ne. nlm2 .and.
//       >                        (nlm2 .gt. nlm1 .or. (N2 .gt. nwfl(L2) + L2
//       >                        .and. nlm2 .lt. nlm1) ) ) then
//  c
//  c     k1-shells are used to creat k2-shells
//  c
//                             if (i_atom .eq. 3)  call copysh (Qnl, Nnl, Lnl,
//       >                        LLnl, LSnl, ig, j, ns1, ns1, k1, k2)
//  c
//                             call copysh (Qnl, Nnl, Lnl, LLnl, LSnl,
//       >                        ig, j, ns2, ns2, k1, k2)
//  c     New shell with one electron is created
//                             Qnl(j, ns3, k2) = 1
//                             Nnl(j, ns3, k2) = N2
//                             Lnl(j, ns3, k2) = L2
//                             LLnl(j, ns3, k2) = L2
//                             LSnl(j, ns3, k2) = 2
//  c     LL12 and LS12
//                             LL12(j, k2) = LL
//                             LS12(j, k2) = LS
//                             LS1 = LSnl(j, ns2, k2)
//                             LS2 = LSnl(j, ns3, k2)
//                             if (LS .le. min0(LS1 + LS2 - 1, LSmax(k2)) .and.
//       >                        LS .ge. abs(LS1 - LS2 + 1))   then
//                                if (n2 .eq. njml(L2) + L2)  igm_yes = 1
//                                j = j + 1
//                             end if
//                          end if
//  c
//                       end do
//  c     This Gamma exists
//                       if (igm_yes .eq. 1) then
//  c     Assume that njml - is the last possible N2
//                          j_ig_l(ig, L2) = j - 1
//                          igm = igm + 1
//                          l_igm(igm)  = L2
//                          ig_igm(igm) = ig
//                          igm_jm(igm) = 0
//                          if (N1 .le. nexcl(Lt) + Lt)  igm_jm(igm) = 1
//                          if (i_printout .gt. 1)  then
//                             if (igm .eq. 1)
//       >                        write(10, 80) 'igm = ig,  (Lt, l) L, (LSt, 2) LS'
//                             write(10, 300) igm, ig,  Lt, l2, LL, LSt, 2, LS
//                          end if
//                       end if
//  c
//                    end if
//                    end do
//                 end do
//              end do
//           end do
//           jmax = j - 1
//  c
//           ncf = j - 1
//        end if
//  C-------------------------------------------------------------------------
//  c
//  c     Two k1-shell case
//  c
//  C-------------------------------------------------------------------------
//        if (i_atom .eq. 2 .and. nsht .eq. 2) then
//           Print*, ' TARGET: --> Helium, (n1s) (n2l),'
//           k = k2
//  c
//  c     Sums by the Target L and 2S+1
//           igm = 0
//           do Lt = 0, LLmax(k1)
//              Ltmp = 4 * Lt + 2
//  c
//  c     one electron is always in S-state
//              L1 = Lt
//  c
//  c     LSt is not a quantum number when  (N1,L1) = (N2,L2).
//  c     In that case LSt will be used as LSnl(j, 2, k2)
//              do LSt = LSmin(k1), LSmax(k1), 2
//                 do ig = jmint(Lt, LSt), jmaxt(Lt, LSt)
//                    LL_ig(ig)  = Lt
//                    LS_ig(ig)  = LSt
//  c
//                    m = 2
//                    if (Qnl(ig, m, k1) .eq. 0)  m = 1
//                    N1 = Nnl(ig, m, k1)
//                    nlm1 = N1 - L1 + m_orb(L1)
//  c
//  c     do L2 = Lmin, Lmax, 2 ; ,2-is due to the conservation of parity!!!!
//  c     Only w.f. of the same parity are coupled. In the case of scattering
//  c     from 1^1S state of helium and with one electron being always in the
//  c     s-state, it will be (-)^LL.
//                    do L2 = abs(Lt - LL), min0(Lt + LL, LLmax(k2)), 2
//                    if (itri(LSt, 2, LS) .eq. 1) then
//  c
//                       igm_yes = 0
//                       j_ig_l(ig, L2) = 0
//  c
//                       do N2 = L2 + 1, njml(L2) + L2
//                          nlm2 = N2 - L2 + m_orb(L2)
//  c
//  c     Case of: One electron is above one shell
//                          if (Qnl(ig, 2, k1) .eq. 0 .and. nlm1 .lt. nlm2) then
//  c
//                             call copysh (Qnl, Nnl, Lnl, LLnl, LSnl,
//       >                        ig, j, 1, 1, k1, k)
//  c     New shell with one electron is created
//                             Nnl(j, 2, k)  = N2
//                             Lnl(j, 2, k)  = L2
//                             Qnl(j, 2, k)  = 1
//                             LLnl(j, 2, k) = L2
//                             LSnl(j, 2, k) = 2
//  c     Create new empty shell
//                             call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, 3, k)
//  c     LL12 and LS12
//                             LL12(j, k) = L2
//                             LS12(j, k) = 2
//  c
//                             if (newcon(Qnl,Nnl,Lnl,LLnl,LSnl,LL12,LS12,j,k,nsh)
//       >                        .eq. 1) then
//                                if (n2 .eq. njml(L2) + L2)  igm_yes = 1
//                                j = j + 1
//                             end if
//  c
//  c     Case of:
//  c     two electrons above one-hole shell
//  c
//                          else if (nlm1 .eq. nlm2 .and. Qnl(ig, 1, k1) .eq. 1
//       >                        .and. LSt .eq. 1) then
//  c     The reason for this correction is not understood!
//  c$$$     >                        .and. itri(LSt, 2, LS) .eq. 1) then
//                             do LL2 = abs(LL - Lnlexc),
//       >                        min0(2 * Lt, LL + Lnlexc, LLmax(k1))
//  c
//  c     LSt is used as LSnl(j,2,k2)
//  c
//                                call copysh (Qnl, Nnl, Lnl, LLnl, LSnl,
//       >                           ig, j, 1, 1, k1, k)
//  c     Create new empty shell
//                                call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, 3, k)
//  c     Add one electron to already occupied shell
//                                Qnl(j, 2, k)  = Qnl(ig, 2, k1) + 1
//                                Nnl(j, 2, k)  = N1
//                                Lnl(j, 2, k)  = L1
//  c     LL12 and LS12
//                                LL12(j, k) = LL
//                                LS12(j, k) = LS
//  c
//  c     Check if the created shell is not a closed shell
//                                if (Qnl(j, 2, k) .lt. Ltmp .or. LSt .eq. 1) then
//                                   LLnl(j, 2, k) = LL2
//                                   LSnl(j, 2, k) = LSt
//                                   if (newcon(Qnl,Nnl,Lnl,LLnl,LSnl,LL12,LS12,
//       >                              j,k,nsh) .eq. 1) then
//                                      if (n2 .eq. njml(L2) +L2) igm_yes = 1
//                                      j = j + 1
//                                   end if
//                                end if
//                             end do
//  c
//  c     Case of:
//  c     Second and third shells have one electron each above
//  c     one-hole shell
//                          else if (Qnl(ig, 2, k1) .eq. 1 .and.
//       >                        (nlm2 .gt. nlm1 .or. (N2 .gt. nwfl(L2) + L2
//       >                        .and. nlm2 .lt. nlm1))  .and.
//       >                        itri(Lt, L2, LL) .eq. 1 .and.
//       >                        itri(LSt, 2, LS) .eq. 1 ) then
//  c
//  c     k1-shells is used to creat k2-shell
//  c
//                             do i = 1, 2
//                                call copysh (Qnl, Nnl, Lnl, LLnl, LSnl,
//       >                           ig, j, i, i, k1, k)
//                             end do
//  c     New shell with one electron is created
//                             Qnl(j, 3, k) = 1
//                             Nnl(j, 3, k) = N2
//                             Lnl(j, 3, k) = L2
//                             LLnl(j,3, k) = L2
//                             LSnl(j,3, k) = 2
//  c     LL12 and LS12
//                             LL12(j, k) = Lt
//                             LS12(j, k) = LSt
//  c
//                             if (newcon(Qnl,Nnl,Lnl,LLnl,LSnl,LL12,LS12,j,k,nsh)
//       >                        .eq. 1) then
//                                if (n2 .eq. njml(L2) + L2)  igm_yes = 1
//                                j = j + 1
//                             end if
//                          end if
//  c
//                       end do
//  c
//  c     This Gamma exists
//                       if (igm_yes .eq. 1) then
//  c     Assume that njml - is the last possible N2
//                          j_ig_l(ig, L2) = j - 1
//                          igm = igm + 1
//                          l_igm(igm)  = L2
//                          ig_igm(igm) = ig
//                          igm_jm(igm) = 0
//                          if (N1 .le. nexcl(L1) + L1)  igm_jm(igm) = 1
//                          if (i_printout .gt. 1)  then
//                             if (igm .eq. 1)
//       >                        write(10, 80) 'igm = ig,  (Lt, l) L, (LSt, 2) LS'
//                             write(10, 300) igm, ig,  Lt, l2, LL, LSt, 2, LS
//                          end if
//                       end if
//  c
//                    end if
//                    end do
//                 end do
//              end do
//           end do
//           jmax = j - 1
//  c
//           ncf = j - 1
//        end if
//  c
//        if (i_printout .gt. 0)
//       >   print*, ' Warning: L=0 of the first scattering state is assumed '
//  c
//        igmax = igm
//        if (igm .gt. igm_par) then
//           print*, 'Increase igm_par from ', igm_par, ' to ', igm
//           stop    'Increase igm_par'
//        end if
//  c
//        if (i_printout .gt. 1) then
//           print*, 'i_printout>1: scatt'
//           print*, '            : # of Configurations used', ncf
//  c
//           if (ncore .gt. 0) then
//              print*, '            : #, N, L of the core shells'
//              do i = 1, ncore
//                 write(6, 214) i, nc(i), lc(i)
//              end do
//           end if
//  c
//  c     Test for quantum numbers for the calculation of U
//  c
//           k = 2
//           write(10, 80) ' Test of Gamma and support of U '
//           write(10, 80) 'j, L, 2S+1, i, Qnl(j,i,k), Nnl(j,i,k), Lnl(j,i,k),',
//       >      ' LLnl(j,i,k), LSnl(j,i,k)'
//  c
//           do igm = 1, igmax
//              l1  = l_igm(igm)
//  c
//              n1  = njml(L1) + L1
//  c
//              write(10, *) igm_used(igm_jm(igm)),
//       >         ' G,l,L,S,N =', igm, l1, LL, LS, n1
//  c
//              ig = ig_igm(igm)
//              j = j_ig_l(ig, l1)
//  c
//              write(10, 207) j, LL, LS,
//       >         (i, Qnl(j,i,k), Nnl(j,i,k), chl(Lnl(j,i,k)),
//       >         chll(LLnl(j,i,k)), LSnl(j,i,k),
//       >         i = 1, min0(1,nsh))
//              do i = 2, nsh
//                 write(10, 307)  i, Qnl(j,i,k), Nnl(j,i,k),
//       >            chl(Lnl(j,i,k)), chll(LLnl(j,i,k)), LSnl(j,i,k)
//              end do
//              if (nsh .gt. 2)  write(10, 213)
//       >         chll(LL12(j,k)), LS12(j,k)
//              if (nsh .le. 2)  write(10, 213)
//           end do
//  c
//           if (i_test .gt. 1)  then
//              write(10, 80) ' Scattering (k=2) Quantum numbers'
//              k = 2
//              write(10, 10) ' k=', k
//              write(10, 80)
//       >         'j, L, 2S+1, i, Qnl(j,i,k), Nnl(j,i,k), Lnl(j,i,k),',
//       >         ' LLnl(j,i,k), LSnl(j,i,k)'
//              do j = 1, jmax
//                 write(10, 207) j, LL, LS,
//       >            (i, Qnl(j,i,k), Nnl(j,i,k), chl(Lnl(j,i,k)),
//       >            chll(LLnl(j,i,k)), LSnl(j,i,k),
//       >            i = 1, min0(1,nsh))
//                 do i = 2, nsh
//                    write(10, 307)  i, Qnl(j,i,k), Nnl(j,i,k),
//       >               chl(Lnl(j,i,k)), chll(LLnl(j,i,k)), LSnl(j,i,k)
//                 end do
//                 if (nsh .gt. 2)  write(10, 213)
//       >            chll(LL12(j,k)), LS12(j,k)
//                 if (nsh .le. 2)  write(10, 213)
//  c
//              end do
//           end if
//  c
//        end if
//  c
//        return
//        end

}
/*
C     scatt.f  Defines Scattering Quantum Numbers
c
Cr    Remarks:
Cr    jmax-memory position of j's for each LL and LS
Cr    igmax - memory position of igm's for each LL and LS
Cr    nsh - number of shells
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
      subroutine scatt (
c     IN:
     >   LL, LS,
     >   i_atom, Nnlexc, Lnlexc, Ncore, LLmax,  LSmin, LSmax, Nc, Lc,
     >   lwf, nwfl, nexcl, njml, i_test, i_printout, chl, chll, m_orb,
     >   jmint, jmaxt, nsht,
c     Out:
     >   jmax, igmax,
     >   Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, nsh, ncf,
     >   ig_igm, igm_jm, l_igm, j_ig_l, LL_ig, LS_ig)
      include 'par.jm.f'
      character*1 chl(0:nchl_par), chll(0:nchl_par), igm_used(0:1)
      data igm_used /'-', '+'/
      integer Nnlexc, Lnlexc, i_atom,  i_printout,
     >   LL, LS, LL2, i, m_orb(0:Lmax_par), m, itri, newcon
      include 'formats.jm.f'
c
      integer i_atom, Nnlexc, Lnlexc, lwf, i_test, i_printout,
     >   Ncore, Lc(Ncore), Nc(Ncore),
     >   LLmax(2), LSmin(2), LSmax(2), nwfl(0:lwf),
     >   nexcl(0:lwf), njml(0:lwf), nlm2, nlm1, ns1, ns2, ns3, Ncl,
     >   ig_igm(igm_par),  igm_jm(igm_par),  l_igm(igm_par),
     >   igm, LL_ig(igm_par), LS_ig(igm_par)
c
      integer k, k1, k2, j, ig, n1, n2, l1, l2, Ltmp, ls1, ls2,
     >   jmax, Lt, LSt, igm_yes,
     >   jmint(0:Lmax_par, LSmax_par), jmaxt(0:Lmax_par, LSmax_par),
     >   igmax, nsh, nsht, ncf, j_ig_l(igm_par, 0:Lmax_par)
c
      integer
     >   Nnl(j_par, Nshells_par, 2),  Lnl(j_par, Nshells_par, 2),
     >   Qnl(j_par, Nshells_par, 2),
     >   LLnl(j_par, Nshells_par, 2), LSnl(j_par, Nshells_par, 2),
     >   LL12(j_par, 2), LS12(j_par, 2)
c
      k1 = 1
      k2 = 2
      nsh = nsht + 1
      j = 1
C==========================================================================
c
c     H Y D R O G E N,   L I T H I U M,
c
C     Define Scattering configurations by adding one electron to (k=1)
c
      if ((i_atom .eq. 1 .and. nsht .eq. 1) .or.
     >   i_atom .eq. 3) then
         if (i_atom .eq. 1) then
            Print*, ' TARGET: --> Hydrogen'
            nsh = nsht + 1
            ns1 = 0
            ns2 = 1
            ns3 = 2
         else if (i_atom .eq. 3) then
            Print*, ' TARGET: --> Lithium'
            Print*, '       : LL12 and LS12 denotes coupling above clsd.shlls'
            nsh = nsht
            ns1 = 1
            ns2 = 2
            ns3 = 3
         end if
c
c     Sums by the Target L and 2S+1
         igm = 0
         do Lt = 0, LLmax(k1)
            L1 = Lt
            Ltmp = 4 * Lt + 2
            do LSt = LSmin(k1), LSmax(k1), 2
               do ig = jmint(Lt, LSt), jmaxt(Lt, LSt)
                  LL_ig(ig)  = Lt
                  LS_ig(ig)  = LSt
c
                  Ncl = 0
                  if (i_atom .eq. 3) Ncl = Nnl(ig, ns1, k1)
c     Assume that Lcl=0
c$$$                  nlm0 = Ncl
                  N1 = Nnl(ig, ns2, k1)
                  nlm1 = N1 - Lt + m_orb(Lt)
c
c     do L2 = Lmin, Lmax, 2 ; ,2-is due to the conservation of parity!!!!
c     Only w.f. of the same parity are coupled. In the case of hydrogen,
c     it will be (-)^LL
                  do L2 = abs(Lt - LL), min0(Lt + LL, LLmax(k2)), 2
                  if (itri(LSt, 2, LS) .eq. 1 .and. Ncl .ne. nlm1) then
c
                     igm_yes = 0
                     j_ig_l(ig, L2) = 0
c
                     do N2 = L2 + 1, njml(L2) + L2
                        nlm2 = N2 - L2 + m_orb(L2)
c
                        if (nlm1 .eq. nlm2 .and. Ncl .lt. nlm2) then
c
                           if (i_atom .eq. 3)  call copysh (Qnl, Nnl, Lnl,
     >                        LLnl, LSnl, ig, j, ns1, ns1, k1, k2)
c
c     Create new empty shell
                           call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, ns3, k2)
c     Add one electron to already occupied shell
                           Qnl(j, ns2, k2)  = Qnl(ig, ns2, k1) + 1
                           Nnl(j, ns2, k2)  = N1
                           Lnl(j, ns2, k2)  = Lt
c
c     Check if the created shell is not a closed shell
                           if (Qnl(j, ns2, k2) .lt. Ltmp .or. LS .eq. 1) then
                              LLnl(j, ns2, k2) = LL
                              LSnl(j, ns2, k2) = LS
c     LL12 and LS12
                              LL12(j, k2) = LL
                              LS12(j, k2) = LS
c     memory pointers between igm,N2 and j
                              if (n2 .eq. njml(L2) + L2)  igm_yes = 1
                              j = j + 1
                           end if
c
                        else if (Ncl .ne. nlm2 .and.
     >                        (nlm2 .gt. nlm1 .or. (N2 .gt. nwfl(L2) + L2
     >                        .and. nlm2 .lt. nlm1) ) ) then
c
c     k1-shells are used to creat k2-shells
c
                           if (i_atom .eq. 3)  call copysh (Qnl, Nnl, Lnl,
     >                        LLnl, LSnl, ig, j, ns1, ns1, k1, k2)
c
                           call copysh (Qnl, Nnl, Lnl, LLnl, LSnl,
     >                        ig, j, ns2, ns2, k1, k2)
c     New shell with one electron is created
                           Qnl(j, ns3, k2) = 1
                           Nnl(j, ns3, k2) = N2
                           Lnl(j, ns3, k2) = L2
                           LLnl(j, ns3, k2) = L2
                           LSnl(j, ns3, k2) = 2
c     LL12 and LS12
                           LL12(j, k2) = LL
                           LS12(j, k2) = LS
                           LS1 = LSnl(j, ns2, k2)
                           LS2 = LSnl(j, ns3, k2)
                           if (LS .le. min0(LS1 + LS2 - 1, LSmax(k2)) .and.
     >                        LS .ge. abs(LS1 - LS2 + 1))   then
                              if (n2 .eq. njml(L2) + L2)  igm_yes = 1
                              j = j + 1
                           end if
                        end if
c
                     end do
c     This Gamma exists
                     if (igm_yes .eq. 1) then
c     Assume that njml - is the last possible N2
                        j_ig_l(ig, L2) = j - 1
                        igm = igm + 1
                        l_igm(igm)  = L2
                        ig_igm(igm) = ig
                        igm_jm(igm) = 0
                        if (N1 .le. nexcl(Lt) + Lt)  igm_jm(igm) = 1
                        if (i_printout .gt. 1)  then
                           if (igm .eq. 1)
     >                        write(10, 80) 'igm = ig,  (Lt, l) L, (LSt, 2) LS'
                           write(10, 300) igm, ig,  Lt, l2, LL, LSt, 2, LS
                        end if
                     end if
c
                  end if
                  end do
               end do
            end do
         end do
         jmax = j - 1
c
         ncf = j - 1
      end if
C-------------------------------------------------------------------------
c
c     Two k1-shell case
c
C-------------------------------------------------------------------------
      if (i_atom .eq. 2 .and. nsht .eq. 2) then
         Print*, ' TARGET: --> Helium, (n1s) (n2l),'
         k = k2
c
c     Sums by the Target L and 2S+1
         igm = 0
         do Lt = 0, LLmax(k1)
            Ltmp = 4 * Lt + 2
c
c     one electron is always in S-state
            L1 = Lt
c
c     LSt is not a quantum number when  (N1,L1) = (N2,L2).
c     In that case LSt will be used as LSnl(j, 2, k2)
            do LSt = LSmin(k1), LSmax(k1), 2
               do ig = jmint(Lt, LSt), jmaxt(Lt, LSt)
                  LL_ig(ig)  = Lt
                  LS_ig(ig)  = LSt
c
                  m = 2
                  if (Qnl(ig, m, k1) .eq. 0)  m = 1
                  N1 = Nnl(ig, m, k1)
                  nlm1 = N1 - L1 + m_orb(L1)
c
c     do L2 = Lmin, Lmax, 2 ; ,2-is due to the conservation of parity!!!!
c     Only w.f. of the same parity are coupled. In the case of scattering
c     from 1^1S state of helium and with one electron being always in the
c     s-state, it will be (-)^LL.
                  do L2 = abs(Lt - LL), min0(Lt + LL, LLmax(k2)), 2
                  if (itri(LSt, 2, LS) .eq. 1) then
c
                     igm_yes = 0
                     j_ig_l(ig, L2) = 0
c
                     do N2 = L2 + 1, njml(L2) + L2
                        nlm2 = N2 - L2 + m_orb(L2)
c
c     Case of: One electron is above one shell
                        if (Qnl(ig, 2, k1) .eq. 0 .and. nlm1 .lt. nlm2) then
c
                           call copysh (Qnl, Nnl, Lnl, LLnl, LSnl,
     >                        ig, j, 1, 1, k1, k)
c     New shell with one electron is created
                           Nnl(j, 2, k)  = N2
                           Lnl(j, 2, k)  = L2
                           Qnl(j, 2, k)  = 1
                           LLnl(j, 2, k) = L2
                           LSnl(j, 2, k) = 2
c     Create new empty shell
                           call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, 3, k)
c     LL12 and LS12
                           LL12(j, k) = L2
                           LS12(j, k) = 2
c
                           if (newcon(Qnl,Nnl,Lnl,LLnl,LSnl,LL12,LS12,j,k,nsh)
     >                        .eq. 1) then
                              if (n2 .eq. njml(L2) + L2)  igm_yes = 1
                              j = j + 1
                           end if
c
c     Case of:
c     two electrons above one-hole shell
c
                        else if (nlm1 .eq. nlm2 .and. Qnl(ig, 1, k1) .eq. 1
     >                        .and. LSt .eq. 1) then
c     The reason for this correction is not understood!
c$$$     >                        .and. itri(LSt, 2, LS) .eq. 1) then
                           do LL2 = abs(LL - Lnlexc),
     >                        min0(2 * Lt, LL + Lnlexc, LLmax(k1))
c
c     LSt is used as LSnl(j,2,k2)
c
                              call copysh (Qnl, Nnl, Lnl, LLnl, LSnl,
     >                           ig, j, 1, 1, k1, k)
c     Create new empty shell
                              call emptysh (Qnl, Nnl, Lnl, LLnl, LSnl, j, 3, k)
c     Add one electron to already occupied shell
                              Qnl(j, 2, k)  = Qnl(ig, 2, k1) + 1
                              Nnl(j, 2, k)  = N1
                              Lnl(j, 2, k)  = L1
c     LL12 and LS12
                              LL12(j, k) = LL
                              LS12(j, k) = LS
c
c     Check if the created shell is not a closed shell
                              if (Qnl(j, 2, k) .lt. Ltmp .or. LSt .eq. 1) then
                                 LLnl(j, 2, k) = LL2
                                 LSnl(j, 2, k) = LSt
                                 if (newcon(Qnl,Nnl,Lnl,LLnl,LSnl,LL12,LS12,
     >                              j,k,nsh) .eq. 1) then
                                    if (n2 .eq. njml(L2) +L2) igm_yes = 1
                                    j = j + 1
                                 end if
                              end if
                           end do
c
c     Case of:
c     Second and third shells have one electron each above
c     one-hole shell
                        else if (Qnl(ig, 2, k1) .eq. 1 .and.
     >                        (nlm2 .gt. nlm1 .or. (N2 .gt. nwfl(L2) + L2
     >                        .and. nlm2 .lt. nlm1))  .and.
     >                        itri(Lt, L2, LL) .eq. 1 .and.
     >                        itri(LSt, 2, LS) .eq. 1 ) then
c
c     k1-shells is used to creat k2-shell
c
                           do i = 1, 2
                              call copysh (Qnl, Nnl, Lnl, LLnl, LSnl,
     >                           ig, j, i, i, k1, k)
                           end do
c     New shell with one electron is created
                           Qnl(j, 3, k) = 1
                           Nnl(j, 3, k) = N2
                           Lnl(j, 3, k) = L2
                           LLnl(j,3, k) = L2
                           LSnl(j,3, k) = 2
c     LL12 and LS12
                           LL12(j, k) = Lt
                           LS12(j, k) = LSt
c
                           if (newcon(Qnl,Nnl,Lnl,LLnl,LSnl,LL12,LS12,j,k,nsh)
     >                        .eq. 1) then
                              if (n2 .eq. njml(L2) + L2)  igm_yes = 1
                              j = j + 1
                           end if
                        end if
c
                     end do
c
c     This Gamma exists
                     if (igm_yes .eq. 1) then
c     Assume that njml - is the last possible N2
                        j_ig_l(ig, L2) = j - 1
                        igm = igm + 1
                        l_igm(igm)  = L2
                        ig_igm(igm) = ig
                        igm_jm(igm) = 0
                        if (N1 .le. nexcl(L1) + L1)  igm_jm(igm) = 1
                        if (i_printout .gt. 1)  then
                           if (igm .eq. 1)
     >                        write(10, 80) 'igm = ig,  (Lt, l) L, (LSt, 2) LS'
                           write(10, 300) igm, ig,  Lt, l2, LL, LSt, 2, LS
                        end if
                     end if
c
                  end if
                  end do
               end do
            end do
         end do
         jmax = j - 1
c
         ncf = j - 1
      end if
c
      if (i_printout .gt. 0)
     >   print*, ' Warning: L=0 of the first scattering state is assumed '
c
      igmax = igm
      if (igm .gt. igm_par) then
         print*, 'Increase igm_par from ', igm_par, ' to ', igm
         stop    'Increase igm_par'
      end if
c
      if (i_printout .gt. 1) then
         print*, 'i_printout>1: scatt'
         print*, '            : # of Configurations used', ncf
c
         if (ncore .gt. 0) then
            print*, '            : #, N, L of the core shells'
            do i = 1, ncore
               write(6, 214) i, nc(i), lc(i)
            end do
         end if
c
c     Test for quantum numbers for the calculation of U
c
         k = 2
         write(10, 80) ' Test of Gamma and support of U '
         write(10, 80) 'j, L, 2S+1, i, Qnl(j,i,k), Nnl(j,i,k), Lnl(j,i,k),',
     >      ' LLnl(j,i,k), LSnl(j,i,k)'
c
         do igm = 1, igmax
            l1  = l_igm(igm)
c
            n1  = njml(L1) + L1
c
            write(10, *) igm_used(igm_jm(igm)),
     >         ' G,l,L,S,N =', igm, l1, LL, LS, n1
c
            ig = ig_igm(igm)
            j = j_ig_l(ig, l1)
c
            write(10, 207) j, LL, LS,
     >         (i, Qnl(j,i,k), Nnl(j,i,k), chl(Lnl(j,i,k)),
     >         chll(LLnl(j,i,k)), LSnl(j,i,k),
     >         i = 1, min0(1,nsh))
            do i = 2, nsh
               write(10, 307)  i, Qnl(j,i,k), Nnl(j,i,k),
     >            chl(Lnl(j,i,k)), chll(LLnl(j,i,k)), LSnl(j,i,k)
            end do
            if (nsh .gt. 2)  write(10, 213)
     >         chll(LL12(j,k)), LS12(j,k)
            if (nsh .le. 2)  write(10, 213)
         end do
c
         if (i_test .gt. 1)  then
            write(10, 80) ' Scattering (k=2) Quantum numbers'
            k = 2
            write(10, 10) ' k=', k
            write(10, 80)
     >         'j, L, 2S+1, i, Qnl(j,i,k), Nnl(j,i,k), Lnl(j,i,k),',
     >         ' LLnl(j,i,k), LSnl(j,i,k)'
            do j = 1, jmax
               write(10, 207) j, LL, LS,
     >            (i, Qnl(j,i,k), Nnl(j,i,k), chl(Lnl(j,i,k)),
     >            chll(LLnl(j,i,k)), LSnl(j,i,k),
     >            i = 1, min0(1,nsh))
               do i = 2, nsh
                  write(10, 307)  i, Qnl(j,i,k), Nnl(j,i,k),
     >               chl(Lnl(j,i,k)), chll(LLnl(j,i,k)), LSnl(j,i,k)
               end do
               if (nsh .gt. 2)  write(10, 213)
     >            chll(LL12(j,k)), LS12(j,k)
               if (nsh .le. 2)  write(10, 213)
c
            end do
         end if
c
      end if
c
      return
      end

 */
