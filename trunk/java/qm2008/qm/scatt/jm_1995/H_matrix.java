package scatt.jm_1995;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 15:22:38
 */
public class H_matrix {
//  C     H_matrix.f  Calculates matrix elements for given states
//  c
//  Cr    Remarks: description of
//  Cr    j, k, nsh, Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12,
//  Cr    Ncore, nc, lc, Nnlexc, Lnlexc
//  Cr    see target.f or jm.tex
//  c
//        subroutine H_matrix (
//  c     In:
//       >   j1, j2, k, LL, LS,
//       >   i_atom, zatom, eps, nr, rn, wn, h,
//       >   Nnlexc, Lnlexc, Ncore, Nc, Lc,
//       >   i_printout,
//       >   Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, nsh, nsht,
//       >   m_orb, pp, ipp, nwf, HL0, clkl, chl, chll,
//  c     Out:
//       >   res)
//        include 'par.jm.f'
//        character*1 chl(0:nchl_par), chll(0:nchl_par)
//        integer Nnlexc, Lnlexc, i_atom,  i_printout,
//       >   LL, LS, ii(4), i1, i2, i3, i4, k, ss1, ss2, i, L_L
//        integer ip(4), n(4), l(4), l0, n0, nel, ncl, ip0, i_H,
//       >   ib, ib3, nwf, q1(Nshells_par), q2(Nshells_par),
//       >   s1(Nshells_par), s2(Nshells_par)
//  c
//        integer m_orb(0:Lmax_par), ipp(nwf_par, 2), d_type, e_type, s_12, s_23
//        real*8 pp(nr_par, nwf_par), HL0(nwf_par, nwf_par),
//       >   clkl(0:Lmax_par, 0:k_par, 0:Lmax_par), rn(nr), wn(nr), h
//  c
//        real*8 zatom, eps, res, res_bar, tmp1, tmp2
//        integer i_atom, nr, Nnlexc, Lnlexc, i_printout,
//       >   Ncore, Lc(Ncore), Nc(Ncore)
//  c
//  c     Temporary: tmp-variables, tmpv-vectors, tmpa-arrays
//        real*8 dir, exc
//        integer idp
//  c
//        integer j1, j2, nsh, nsht, ns
//        integer
//       >   Nnl(j_par, Nshells_par),  Lnl(j_par, Nshells_par),
//       >   Qnl(j_par, Nshells_par),
//       >   LLnl(j_par, Nshells_par), LSnl(j_par, Nshells_par),
//       >   LL12(j_par), LS12(j_par)
//  c
//  c     External calls: int2_8
//        real*8 U_6j, c1_hm, ddl
//        integer idnl, irrss, idl
//        include 'formats.jm.f'
//  c
//        res = 0d0
//        ns = nsht
//        if (k .eq. 2)  ns = nsh
//        ss1 = LS12(j1)
//        ss2 = LS12(j2)
//        do i = 1, ns
//           q1(i) = Qnl(j1, i)
//           q2(i) = Qnl(j2, i)
//           s1(i) = LSnl(j1, i)
//           s2(i) = LSnl(j2, i)
//        end do
//  c
//  C************************************************************************
//  c
//  c     Target states of  H Y D R O G E N
//  c     (k=1, i_atom=1)
//  c
//        if (k .eq. 1 .and. i_atom .eq. 1) then
//  c
//           l(1) = Lnl(j1, 1)
//           l(2) = Lnl(j2, 1)
//           n(1) = Nnl(j1, 1)
//           n(2) = Nnl(j2, 1)
//           ip(1) = n(1) - l(1) + m_orb(l(1))
//           ip(2) = n(2) - l(2) + m_orb(l(2))
//  c
//           res = HL0(ip(1), ip(2))
//           return
//        end if
//  C************************************************************************
//  c
//  c     Scattering states of H+e and target states of He
//  c     (k=2, i_atom=1) or (k=1, i_atom=2)
//  c
//        if ((k .eq. 2 .and. i_atom .eq. 1)
//       >   .or. (k .eq. 1 .and. i_atom .eq. 2)) then
//           nel = 2
//  c
//  c     Define i1=\rho i3=\rho' i2=\sigma i4=\sigma'
//  c     ii(1), ii(2), ii(3), ii(4),  - shell numbers of \rho,\sigma,\rho',\sigma'
//  c     n(1-4) and l(1-4) - n,l shell quantum numbers of 'as above'
//  c     ip(1-4) -     memory addresses of radial w.f. of 'as above'
//  c
//           ii(1) = 1
//           ii(2) = 2
//           ii(3) = 1
//           ii(4) = 2
//           if (Qnl(j1, 1) .eq. 2)  ii(2) = 1
//           if (Qnl(j2, 1) .eq. 2)  ii(4) = 1
//  c
//           do i = 1, 4
//              if (i .le. 2) then
//                 n(i) = Nnl(j1, ii(i))
//                 l(i) = Lnl(j1, ii(i))
//              else
//                 n(i) = Nnl(j2, ii(i))
//                 l(i) = Lnl(j2, ii(i))
//              end if
//              ip(i) = n(i) - l(i) + m_orb(l(i))
//           end do
//           idp = 2
//  c$$$         if (ip(1) .gt. ip(2)) idp = idp + 1
//  c$$$         if (ip(3) .gt. ip(4)) idp = idp + 1
//  c
//           call d_e_hm (LL, HL0, n, l, ip, ipp, pp,
//       >      eps, clkl, nwf, nr, nel, rn, wn, h, Zatom, dir, exc)
//  c
//           exc = - exc * dble((-1)**((LS-1)/2))
//           res = c1_hm (ii, j1, j2, Qnl, dir, exc, idp, eps, n, l)
//  c
//           return
//        end if
//  C************************************************************************
//  c
//  c     Scattering states of Li+e (k=2, i_atom=3)
//  c
//        if(k .eq. 2 .and. i_atom .eq. 3) then
//           nel = 4
//  c
//  c     All possible \rho's and \sigma's must be calculated
//  c     i1=\rho i3=\rho' i2=\sigma i4=\sigma'
//           do i1 = 1, ns
//              do i2 = i1, ns
//                 do i3 = 1, ns
//                    do i4 = i3, ns
//                       ii(1) = i1
//                       ii(2) = i2
//                       ii(3) = i3
//                       ii(4) = i4
//  c
//  c     check if such shells are allowed
//                       if (irrss (Qnl, Nnl, Lnl, j1, j2, ns, ii) .eq. 1) then
//  c
//  c     Define closed shell n,l-numbers, for Li
//                          ncl = 1
//                          n0 = Nnl(j1, ncl)
//                          l0 = Lnl(j1, ncl)
//                          ip0 = n0 - l0 + m_orb(l0)
//  c
//                          i_H = 1
//                          do i = 1, 4
//                             if (i .le. 2) then
//                                n(i) = Nnl(j1, ii(i))
//                                l(i) = Lnl(j1, ii(i))
//                             else
//                                n(i) = Nnl(j2, ii(i))
//                                l(i) = Lnl(j2, ii(i))
//                             end if
//                             ip(i) = n(i) - l(i) + m_orb(l(i))
//                             if (ip(i) .eq. ip0)  i_H = 0
//                          end do
//  c
//                          L_L = LL
//                          idp = 2
//                          dir = 0d0
//                          exc = 0d0
//                          res_bar = 0d0
//  c
//  c     Only proceed if spectator electrons have the same parity
//  c
//                          if (mod(l(1) + l(2), 2) .eq. mod(l(3) + l(4), 2)) then
//  c
//  c     case: ii(i) .ne. closed shell, Hydrogen like matris elements
//                             if(i_H .eq. 1) then
//  c$$$                              write(20, *) 'i_H = ', i_H
//                                dir = 1.0
//                                exc = - dble((-1)**((LS-1)/2))
//  c
//  c     case: \rho = \sigma and \rho' = \sigma', Hydrogen like matris elements
//                             else if(i1 .eq. i2 .and. i3 .eq. i4) then
//                                dir = ddl(s1(i1), s2(i3))
//                                exc = 0d0
//                                L_L = LLnl(j1, i1)
//  c
//  c     case: \rho = \rho'= closed shells, and  \rho's.ne.\sigma's
//                             else if(i1 .eq. 1 .and. i3 .eq. 1 .and.
//       >                           i1 .ne. i2 .and. i3 .ne. i4) then
//                                do i = i1 + 1, i2
//                                   idp = idp + (q1(i) - idl(i2,i))
//                                end do
//                                do i = i3 + 1, i4
//                                   idp = idp + (q2(i) - idl(i4,i))
//                                end do
//  c$$$                              write(20, *) 'idp = ', idp
//                                tmp1 = - dble((-1)**((LS-1)/2))
//                                L_L = Lnl(j1, i2)
//                                dir = 1d0
//                                exc = 0.5d0
//                                if ((-1)**idp .ne. 1)  then
//                                   dir = dir * tmp1
//                                   exc = exc * tmp1
//                                end if
//                             else
//                                print*, j1, j2, ' -not ready !'
//                                write(20, 80) ' -not ready !'
//                             end if
//  c
//                             call d_e_hm (L_L, HL0, n, l, ip, ipp, pp, eps,
//       >                        clkl, nwf, nr, nel, rn, wn, h, Zatom, tmp1, tmp2)
//                             dir = dir * tmp1
//                             exc = exc * tmp2
//                             res_bar = c1_hm (ii, j1, j2,qnl,dir,exc,idp,eps,n,l)
//                             res = res + res_bar
//  c
//  c     This is the end of parity check
//                          end if
//                          if (i_printout .gt. 1)  write(20, 317)
//       >                     (n(i), chl(l(i)), i = 1,4), real(res_bar), real(res)
//  c
//  c     this the end of if (such matrix element is possible)
//                       end if
//  c     this the end of \rho's and \sigma's-loops and if's
//                    end do
//                 end do
//              end do
//           end do
//        end if
//  c
//  C************************************************************************
//  c
//  c     Scattering states of He+e and One-electron
//  c     above-Closed shell atoms with only one shell gets excited (k=2, i_atom=2)
//  c     and
//  c     target states of Lithuim (k=1, i_atom=3)
//  c
//        if((k .eq. 2 .and. i_atom .eq. 2) .or.
//       >   (k .eq. 1 .and. i_atom .eq. 3)) then
//           nel = 3
//  c
//  c     All possible \bar{N}_\lambda's must be calculated
//           do ib = 1, ns
//  c
//  c     check that such shell exists in j2
//              do ib3 = 1, ns
//                 if (idnl(Nnl(j1, ib), Lnl(j1, ib),
//       >            Nnl(j2, ib3), Lnl(j2, ib3) ) .eq. 1
//       >            .and. min0(Qnl(j1, ib), Qnl(j2, ib3)) .gt. 0) then
//  c
//  c     Define i1=\rho i3=\rho' i2=\sigma i4=\sigma'
//                    call r_s_hm (Qnl, j1, ns, ib, ii(1))
//                    call r_s_hm (Qnl, j2, ns, ib3, ii(3))
//  c
//                    l0 = Lnl(j1, ib)
//                    n0 = Nnl(j1, ib)
//  c
//                    do i = 1, 4
//                       if (i .le. 2) then
//                          n(i) = Nnl(j1, ii(i))
//                          l(i) = Lnl(j1, ii(i))
//                       else
//                          n(i) = Nnl(j2, ii(i))
//                          l(i) = Lnl(j2, ii(i))
//                       end if
//                       ip(i) = n(i) - l(i) + m_orb(l(i))
//                    end do
//  c
//                    idp = 2
//  c$$$  if (ip(1) .gt. ip(2)) idp = idp + 1
//  c$$$  if (ip(3) .gt. ip(4)) idp = idp + 1
//                    dir = 0d0
//                    exc = 0d0
//                    res_bar = 0d0
//  c     default
//                    s_12 = s1(1)
//                    s_23 = ss2
//                    e_type = 2
//                    d_type = 0
//  c
//  c     Only proceed if spectator electrons have the same parity
//  c
//                    if (mod(l(1) + l(2), 2) .eq. mod(l(3) + l(4), 2)) then
//  c
//  c     case: ( n1l1^2, nl | h | n1'l1'^2, n2l2 )
//                       if (q1(1) .eq. 2 .and. q2(1) .eq. 2) then
//                          s_23 = s2(1)
//                          if (ib .eq. 1) then
//                             if (ib3 .eq. 2)  e_type = 3
//                             if (ib3 .eq. 2)  d_type = 30
//                          else if (ib .eq. 2) then
//                             e_type = 3
//                             if (ib3 .eq. 1)  d_type = 20
//                             if (ib3 .eq. 2)  e_type = 8
//                          end if
//  c
//  c     case: ( n1l1^2, nl | H | n1'l1', n'l', n''l'' )
//                       else if (q1(1) .eq. 2 .and. q2(3) .eq. 1) then
//                          if (ib .eq. 1) then
//                             if (ib3 .eq. 2)  d_type = 8
//                             if (ib3 .eq. 2)  e_type = 20
//                             if (ib3 .eq. 2)  idp = idp + 1
//                             if (ib3 .eq. 3)  e_type = 3
//                             if (ib3 .eq. 3)  d_type = 30
//                          else if (ib .eq. 2) then
//                             e_type = 3
//                             if (ib3 .eq. 1)  d_type = 20
//                             if (ib3 .eq. 2)  d_type = 2
//                             if (ib3 .eq. 2)  e_type = 30
//                             if (ib3 .eq. 2)  idp = idp + 1
//                             if (ib3 .eq. 3)  e_type = 8
//                          end if
//  c
//  c     case: ( n1l1^2, nl | h | n1'l1', n'l'^2 )
//                       else if (q1(1) .eq. 2 .and. q2(2) .eq. 2) then
//                          s_23 = s2(2)
//                          d_type = 1
//                          if (ib .eq. 1) then
//                             e_type = 4
//                             if (ib3 .eq. 2)  d_type = 70
//                             if (ib3 .eq. 2)  e_type = 50
//                             if (ib3 .eq. 2)  idp = idp + 1
//                          else if (ib .eq. 2) then
//                             e_type = 5
//                             if (ib3 .eq. 1)  d_type = 50
//                             if (ib3 .eq. 2)  d_type = 10
//                             if (ib3 .eq. 2)  e_type = 7
//                             if (ib3 .eq. 2)  idp = idp + 1
//                          end if
//  c
//  c     case: ( n1l1, nl^2 | h | n1'l1'^2, n2l2 )
//  c     using ( n1'l1'^2, n2l2 | h | n1l1, nl^2 )
//                       else if (q1(2) .eq. 2 .and. q2(1) .eq. 2) then
//                          s_12 = s2(1)
//                          s_23 = s1(2)
//                          d_type = 1
//                          if (ib .eq. 1) then
//                             e_type = 4
//                             if (ib3 .eq. 2) d_type = 50
//                             if (ib3 .eq. 2) e_type = 8
//                          else if (ib .eq. 2) then
//                             e_type = 50
//                             if (ib3 .eq. 1) d_type = 70
//                             if (ib3 .eq. 1) idp = idp + 1
//                             if (ib3 .eq. 2) d_type = 10
//                             if (ib3 .eq. 2) e_type = 7
//                             if (ib3 .eq. 2) idp = idp + 1
//                          end if
//  c
//  c     case: ( n1l1, nl^2 | H | n1'l1', n'l'^2 )
//                       else if (q1(2) .eq. 2 .and. q2(2).eq.2) then
//                          s_12 = s1(2)
//                          s_23 = s2(2)
//                          if (ib .eq. 1) then
//                             e_type = 61
//                             if (ib3 .eq. 2)  d_type = 620
//                             if (ib3 .eq. 2)  e_type = 60
//                             if (ib3 .eq. 2)  idp = idp + 1
//                          else if (ib .eq. 2) then
//                             e_type = 62
//                             if (ib3 .eq. 1)  d_type = 620
//                             if (ib3 .eq. 1)  idp = idp + 1
//                             if (ib3 .eq. 2)  e_type = 6
//                          end if
//  c
//  c     case: ( n1l1, n'l', n''l'' | H | n1l1, nl^2 )
//                       else if (q1(3) .eq. 1 .and. q2(2) .eq. 2) then
//                          s_12 = ss1
//                          s_23 = s2(2)
//                          d_type = 1
//                          if (ib .eq. 1) then
//                             e_type = 4
//                             if (ib3 .eq. 2)  d_type = 70
//                             if (ib3 .eq. 2)  e_type = 50
//                             if (ib3 .eq. 2)  idp = idp + 1
//                          else if (ib .eq. 2) then
//                             e_type = 7
//                             if (ib3 .eq. 1)  d_type = 70
//                             if (ib3 .eq. 1)  idp = idp + 1
//                             if (ib3 .eq. 2)  e_type = 5
//                          else if (ib .eq. 3) then
//                             e_type = 5
//                             if (ib3 .eq. 1)  d_type = 50
//                             if (ib3 .eq. 2)  d_type = 10
//                             if (ib3 .eq. 2)  e_type = 7
//                             if (ib3 .eq. 2)  idp = idp + 1
//                          end if
//  c
//  c     case: ( n1l1, nl^2 | H | n1'l1', n'l', n''l'' )
//  c     is calculated from ( n1l1, n'l', n''l'' | H | n1l1, nl^2 )
//                       else if (q1(2) .eq. 2 .and. q2(3) .eq. 1) then
//                          s_12 = ss2
//                          s_23 = s1(2)
//                          d_type = 1
//                          if (ib .eq. 1) then
//                             e_type = 4
//                             if (ib3 .eq. 2)  d_type = 70
//                             if (ib3 .eq. 2)  e_type = 7
//                             if (ib3 .eq. 2)  idp = idp + 1
//                             if (ib3 .eq. 3)  d_type = 50
//                             if (ib3 .eq. 3)  e_type = 5
//                          else if (ib .eq. 2) then
//                             e_type = 50
//                             if (ib3 .eq. 1)  d_type = 70
//                             if (ib3 .eq. 1)  idp = idp + 1
//                             if (ib3 .eq. 2)  e_type = 5
//                             if (ib3 .eq. 3)  d_type = 10
//                             if (ib3 .eq. 3)  e_type = 7
//                             if (ib3 .eq. 3)  idp = idp + 1
//                          end if
//  c
//  c     case: ( nl, n1l1, n2l2 | h | n'l', n1'l1', n2'l2' )
//                       else if (q1(3) .eq. 1 .and. q2(3) .eq. 1) then
//                          s_12 = ss1
//                          if (ib .eq. 1) then
//                             if (ib3 .eq. 2)  d_type = 8
//                             if (ib3 .eq. 2)  e_type = 20
//                             if (ib3 .eq. 2)  idp = idp + 1
//                             if (ib3 .eq. 3)  d_type = 30
//                             if (ib3 .eq. 3)  e_type = 3
//                          else if (ib .eq. 2) then
//                             e_type = 30
//                             if (ib3 .eq. 1)  d_type = 8
//                             if (ib3 .eq. 1)  idp = idp + 1
//                             if (ib3 .eq. 2)  e_type = 3
//                             if (ib3 .eq. 3)  d_type = 2
//                             if (ib3 .eq. 3)  e_type = 20
//                             if (ib3 .eq. 3)  idp = idp + 1
//                          else if (ib .eq. 3) then
//                             e_type = 3
//                             if (ib3 .eq. 1)  d_type = 20
//                             if (ib3 .eq. 2)  d_type = 2
//                             if (ib3 .eq. 2)  e_type = 30
//                             if (ib3 .eq. 2)  idp = idp + 1
//                             if (ib3 .eq. 3)  e_type = 8
//                          end if
//  c
//  c     case: ( nl, n1l1, n2l2 | H | n3l3^2, nl )
//                       else if (q1(3) .eq. 1 .and. q2(1) .eq. 2) then
//                          s_12 = ss1
//                          s_23 = s2(1)
//                          if (ib .eq. 1) then
//                             e_type = 2
//                             if (ib3 .eq. 2)  d_type = 30
//                             if (ib3 .eq. 2)  e_type = 3
//                          else if (ib .eq. 2) then
//                             e_type = 30
//                             if (ib3 .eq. 1)  d_type = 8
//                             if (ib3 .eq. 1)  idp = idp + 1
//                             if (ib3 .eq. 2)  d_type = 2
//                             if (ib3 .eq. 2)  e_type = 20
//                             if (ib3 .eq. 2)  idp = idp + 1
//                          else if (ib .eq. 3) then
//                             e_type = 3
//                             if (ib3 .eq. 1)  d_type = 20
//                             if (ib3 .eq. 2)  e_type = 8
//                          end if
//                       else
//                          print*, j1, j2, ' -not ready !'
//                          write(20, 80) ' -not ready !'
//                       end if
//  c
//                       dir = u_6j(d_type, s_12, s_23, 2, 2, 2, ls, eps, 2)
//                       exc = u_6j(e_type, s_12, s_23, 2, 2, 2, ls, eps, 2)
//  c
//                       if (Lnl(j1, ib) .eq. 0)  L_L = LL
//                       if (Lnl(j1, ib) .ne. 0)  L_L = max0(l(1), l(2))
//                       call d_e_hm (L_L, HL0, n, l, ip, ipp, pp,
//       >                  eps, clkl, nwf, nr, nel, rn, wn, h, Zatom, tmp1, tmp2)
//                       dir = dir * tmp1
//                       exc = exc * tmp2
//                       res_bar = c1_hm (ii, j1, j2, qnl, dir, exc, idp, eps, n,l)
//                       res = res + res_bar
//  c
//  c     This is the end of parity check
//                    end if
//                    if (i_printout .gt. 1)  write(20, 315)
//       >               n0, chl(l0), n(1), chl(l(1)), n(2), chl(l(2)),
//       >               n(3), chl(l(3)), n(4), chl(l(4)),
//       >               real(res_bar), real(res)
//  c
//  c     this the end of ib-loops and if's
//                 end if
//              end do
//           end do
//        end if
//  c
//  c     Printouts for testing
//  c
//           if (i_printout .gt. 1) then
//              if (ns .gt. 2) then
//                 write(20, 320) ll12(j1), ss1, ll12(j2), ss2
//                 write(20, 325) ll, ls, j1, j2,
//       >            (q1(i), nnl(j1, i), chl(lnl(j1, i)),
//       >            chll(llnl(j1, i)), s1(i), i = 1,ns),
//       >            (q2(i), nnl(j2, i), chl(lnl(j2, i)),
//       >            chll(llnl(j2, i)), s2(i), i = 1,ns), real(res)
//              else
//                 write(20, 310) ll, ls, j1, j2,
//       >            (q1(i), nnl(j1, i), chl(lnl(j1, i)),
//       >            chll(llnl(j1, i)), s1(i), i = 1,ns),
//       >            (q2(i), nnl(j2, i), chl(lnl(j2, i)),
//       >            chll(llnl(j2, i)), s2(i), i = 1,ns), real(res)
//              end if
//           end if
//  c
//        return
//        end
//

}
/*
C     H_matrix.f  Calculates matrix elements for given states
c
Cr    Remarks: description of
Cr    j, k, nsh, Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12,
Cr    Ncore, nc, lc, Nnlexc, Lnlexc
Cr    see target.f or jm.tex
c
      subroutine H_matrix (
c     In:
     >   j1, j2, k, LL, LS,
     >   i_atom, zatom, eps, nr, rn, wn, h,
     >   Nnlexc, Lnlexc, Ncore, Nc, Lc,
     >   i_printout,
     >   Qnl, Nnl, Lnl, LLnl, LSnl, LL12, LS12, nsh, nsht,
     >   m_orb, pp, ipp, nwf, HL0, clkl, chl, chll,
c     Out:
     >   res)
      include 'par.jm.f'
      character*1 chl(0:nchl_par), chll(0:nchl_par)
      integer Nnlexc, Lnlexc, i_atom,  i_printout,
     >   LL, LS, ii(4), i1, i2, i3, i4, k, ss1, ss2, i, L_L
      integer ip(4), n(4), l(4), l0, n0, nel, ncl, ip0, i_H,
     >   ib, ib3, nwf, q1(Nshells_par), q2(Nshells_par),
     >   s1(Nshells_par), s2(Nshells_par)
c
      integer m_orb(0:Lmax_par), ipp(nwf_par, 2), d_type, e_type, s_12, s_23
      real*8 pp(nr_par, nwf_par), HL0(nwf_par, nwf_par),
     >   clkl(0:Lmax_par, 0:k_par, 0:Lmax_par), rn(nr), wn(nr), h
c
      real*8 zatom, eps, res, res_bar, tmp1, tmp2
      integer i_atom, nr, Nnlexc, Lnlexc, i_printout,
     >   Ncore, Lc(Ncore), Nc(Ncore)
c
c     Temporary: tmp-variables, tmpv-vectors, tmpa-arrays
      real*8 dir, exc
      integer idp
c
      integer j1, j2, nsh, nsht, ns
      integer
     >   Nnl(j_par, Nshells_par),  Lnl(j_par, Nshells_par),
     >   Qnl(j_par, Nshells_par),
     >   LLnl(j_par, Nshells_par), LSnl(j_par, Nshells_par),
     >   LL12(j_par), LS12(j_par)
c
c     External calls: int2_8
      real*8 U_6j, c1_hm, ddl
      integer idnl, irrss, idl
      include 'formats.jm.f'
c
      res = 0d0
      ns = nsht
      if (k .eq. 2)  ns = nsh
      ss1 = LS12(j1)
      ss2 = LS12(j2)
      do i = 1, ns
         q1(i) = Qnl(j1, i)
         q2(i) = Qnl(j2, i)
         s1(i) = LSnl(j1, i)
         s2(i) = LSnl(j2, i)
      end do
c
C************************************************************************
c
c     Target states of  H Y D R O G E N
c     (k=1, i_atom=1)
c
      if (k .eq. 1 .and. i_atom .eq. 1) then
c
         l(1) = Lnl(j1, 1)
         l(2) = Lnl(j2, 1)
         n(1) = Nnl(j1, 1)
         n(2) = Nnl(j2, 1)
         ip(1) = n(1) - l(1) + m_orb(l(1))
         ip(2) = n(2) - l(2) + m_orb(l(2))
c
         res = HL0(ip(1), ip(2))
         return
      end if
C************************************************************************
c
c     Scattering states of H+e and target states of He
c     (k=2, i_atom=1) or (k=1, i_atom=2)
c
      if ((k .eq. 2 .and. i_atom .eq. 1)
     >   .or. (k .eq. 1 .and. i_atom .eq. 2)) then
         nel = 2
c
c     Define i1=\rho i3=\rho' i2=\sigma i4=\sigma'
c     ii(1), ii(2), ii(3), ii(4),  - shell numbers of \rho,\sigma,\rho',\sigma'
c     n(1-4) and l(1-4) - n,l shell quantum numbers of 'as above'
c     ip(1-4) -     memory addresses of radial w.f. of 'as above'
c
         ii(1) = 1
         ii(2) = 2
         ii(3) = 1
         ii(4) = 2
         if (Qnl(j1, 1) .eq. 2)  ii(2) = 1
         if (Qnl(j2, 1) .eq. 2)  ii(4) = 1
c
         do i = 1, 4
            if (i .le. 2) then
               n(i) = Nnl(j1, ii(i))
               l(i) = Lnl(j1, ii(i))
            else
               n(i) = Nnl(j2, ii(i))
               l(i) = Lnl(j2, ii(i))
            end if
            ip(i) = n(i) - l(i) + m_orb(l(i))
         end do
         idp = 2
c$$$         if (ip(1) .gt. ip(2)) idp = idp + 1
c$$$         if (ip(3) .gt. ip(4)) idp = idp + 1
c
         call d_e_hm (LL, HL0, n, l, ip, ipp, pp,
     >      eps, clkl, nwf, nr, nel, rn, wn, h, Zatom, dir, exc)
c
         exc = - exc * dble((-1)**((LS-1)/2))
         res = c1_hm (ii, j1, j2, Qnl, dir, exc, idp, eps, n, l)
c
         return
      end if
C************************************************************************
c
c     Scattering states of Li+e (k=2, i_atom=3)
c
      if(k .eq. 2 .and. i_atom .eq. 3) then
         nel = 4
c
c     All possible \rho's and \sigma's must be calculated
c     i1=\rho i3=\rho' i2=\sigma i4=\sigma'
         do i1 = 1, ns
            do i2 = i1, ns
               do i3 = 1, ns
                  do i4 = i3, ns
                     ii(1) = i1
                     ii(2) = i2
                     ii(3) = i3
                     ii(4) = i4
c
c     check if such shells are allowed
                     if (irrss (Qnl, Nnl, Lnl, j1, j2, ns, ii) .eq. 1) then
c
c     Define closed shell n,l-numbers, for Li
                        ncl = 1
                        n0 = Nnl(j1, ncl)
                        l0 = Lnl(j1, ncl)
                        ip0 = n0 - l0 + m_orb(l0)
c
                        i_H = 1
                        do i = 1, 4
                           if (i .le. 2) then
                              n(i) = Nnl(j1, ii(i))
                              l(i) = Lnl(j1, ii(i))
                           else
                              n(i) = Nnl(j2, ii(i))
                              l(i) = Lnl(j2, ii(i))
                           end if
                           ip(i) = n(i) - l(i) + m_orb(l(i))
                           if (ip(i) .eq. ip0)  i_H = 0
                        end do
c
                        L_L = LL
                        idp = 2
                        dir = 0d0
                        exc = 0d0
                        res_bar = 0d0
c
c     Only proceed if spectator electrons have the same parity
c
                        if (mod(l(1) + l(2), 2) .eq. mod(l(3) + l(4), 2)) then
c
c     case: ii(i) .ne. closed shell, Hydrogen like matris elements
                           if(i_H .eq. 1) then
c$$$                              write(20, *) 'i_H = ', i_H
                              dir = 1.0
                              exc = - dble((-1)**((LS-1)/2))
c
c     case: \rho = \sigma and \rho' = \sigma', Hydrogen like matris elements
                           else if(i1 .eq. i2 .and. i3 .eq. i4) then
                              dir = ddl(s1(i1), s2(i3))
                              exc = 0d0
                              L_L = LLnl(j1, i1)
c
c     case: \rho = \rho'= closed shells, and  \rho's.ne.\sigma's
                           else if(i1 .eq. 1 .and. i3 .eq. 1 .and.
     >                           i1 .ne. i2 .and. i3 .ne. i4) then
                              do i = i1 + 1, i2
                                 idp = idp + (q1(i) - idl(i2,i))
                              end do
                              do i = i3 + 1, i4
                                 idp = idp + (q2(i) - idl(i4,i))
                              end do
c$$$                              write(20, *) 'idp = ', idp
                              tmp1 = - dble((-1)**((LS-1)/2))
                              L_L = Lnl(j1, i2)
                              dir = 1d0
                              exc = 0.5d0
                              if ((-1)**idp .ne. 1)  then
                                 dir = dir * tmp1
                                 exc = exc * tmp1
                              end if
                           else
                              print*, j1, j2, ' -not ready !'
                              write(20, 80) ' -not ready !'
                           end if
c
                           call d_e_hm (L_L, HL0, n, l, ip, ipp, pp, eps,
     >                        clkl, nwf, nr, nel, rn, wn, h, Zatom, tmp1, tmp2)
                           dir = dir * tmp1
                           exc = exc * tmp2
                           res_bar = c1_hm (ii, j1, j2,qnl,dir,exc,idp,eps,n,l)
                           res = res + res_bar
c
c     This is the end of parity check
                        end if
                        if (i_printout .gt. 1)  write(20, 317)
     >                     (n(i), chl(l(i)), i = 1,4), real(res_bar), real(res)
c
c     this the end of if (such matrix element is possible)
                     end if
c     this the end of \rho's and \sigma's-loops and if's
                  end do
               end do
            end do
         end do
      end if
c
C************************************************************************
c
c     Scattering states of He+e and One-electron
c     above-Closed shell atoms with only one shell gets excited (k=2, i_atom=2)
c     and
c     target states of Lithuim (k=1, i_atom=3)
c
      if((k .eq. 2 .and. i_atom .eq. 2) .or.
     >   (k .eq. 1 .and. i_atom .eq. 3)) then
         nel = 3
c
c     All possible \bar{N}_\lambda's must be calculated
         do ib = 1, ns
c
c     check that such shell exists in j2
            do ib3 = 1, ns
               if (idnl(Nnl(j1, ib), Lnl(j1, ib),
     >            Nnl(j2, ib3), Lnl(j2, ib3) ) .eq. 1
     >            .and. min0(Qnl(j1, ib), Qnl(j2, ib3)) .gt. 0) then
c
c     Define i1=\rho i3=\rho' i2=\sigma i4=\sigma'
                  call r_s_hm (Qnl, j1, ns, ib, ii(1))
                  call r_s_hm (Qnl, j2, ns, ib3, ii(3))
c
                  l0 = Lnl(j1, ib)
                  n0 = Nnl(j1, ib)
c
                  do i = 1, 4
                     if (i .le. 2) then
                        n(i) = Nnl(j1, ii(i))
                        l(i) = Lnl(j1, ii(i))
                     else
                        n(i) = Nnl(j2, ii(i))
                        l(i) = Lnl(j2, ii(i))
                     end if
                     ip(i) = n(i) - l(i) + m_orb(l(i))
                  end do
c
                  idp = 2
c$$$  if (ip(1) .gt. ip(2)) idp = idp + 1
c$$$  if (ip(3) .gt. ip(4)) idp = idp + 1
                  dir = 0d0
                  exc = 0d0
                  res_bar = 0d0
c     default
                  s_12 = s1(1)
                  s_23 = ss2
                  e_type = 2
                  d_type = 0
c
c     Only proceed if spectator electrons have the same parity
c
                  if (mod(l(1) + l(2), 2) .eq. mod(l(3) + l(4), 2)) then
c
c     case: ( n1l1^2, nl | h | n1'l1'^2, n2l2 )
                     if (q1(1) .eq. 2 .and. q2(1) .eq. 2) then
                        s_23 = s2(1)
                        if (ib .eq. 1) then
                           if (ib3 .eq. 2)  e_type = 3
                           if (ib3 .eq. 2)  d_type = 30
                        else if (ib .eq. 2) then
                           e_type = 3
                           if (ib3 .eq. 1)  d_type = 20
                           if (ib3 .eq. 2)  e_type = 8
                        end if
c
c     case: ( n1l1^2, nl | H | n1'l1', n'l', n''l'' )
                     else if (q1(1) .eq. 2 .and. q2(3) .eq. 1) then
                        if (ib .eq. 1) then
                           if (ib3 .eq. 2)  d_type = 8
                           if (ib3 .eq. 2)  e_type = 20
                           if (ib3 .eq. 2)  idp = idp + 1
                           if (ib3 .eq. 3)  e_type = 3
                           if (ib3 .eq. 3)  d_type = 30
                        else if (ib .eq. 2) then
                           e_type = 3
                           if (ib3 .eq. 1)  d_type = 20
                           if (ib3 .eq. 2)  d_type = 2
                           if (ib3 .eq. 2)  e_type = 30
                           if (ib3 .eq. 2)  idp = idp + 1
                           if (ib3 .eq. 3)  e_type = 8
                        end if
c
c     case: ( n1l1^2, nl | h | n1'l1', n'l'^2 )
                     else if (q1(1) .eq. 2 .and. q2(2) .eq. 2) then
                        s_23 = s2(2)
                        d_type = 1
                        if (ib .eq. 1) then
                           e_type = 4
                           if (ib3 .eq. 2)  d_type = 70
                           if (ib3 .eq. 2)  e_type = 50
                           if (ib3 .eq. 2)  idp = idp + 1
                        else if (ib .eq. 2) then
                           e_type = 5
                           if (ib3 .eq. 1)  d_type = 50
                           if (ib3 .eq. 2)  d_type = 10
                           if (ib3 .eq. 2)  e_type = 7
                           if (ib3 .eq. 2)  idp = idp + 1
                        end if
c
c     case: ( n1l1, nl^2 | h | n1'l1'^2, n2l2 )
c     using ( n1'l1'^2, n2l2 | h | n1l1, nl^2 )
                     else if (q1(2) .eq. 2 .and. q2(1) .eq. 2) then
                        s_12 = s2(1)
                        s_23 = s1(2)
                        d_type = 1
                        if (ib .eq. 1) then
                           e_type = 4
                           if (ib3 .eq. 2) d_type = 50
                           if (ib3 .eq. 2) e_type = 8
                        else if (ib .eq. 2) then
                           e_type = 50
                           if (ib3 .eq. 1) d_type = 70
                           if (ib3 .eq. 1) idp = idp + 1
                           if (ib3 .eq. 2) d_type = 10
                           if (ib3 .eq. 2) e_type = 7
                           if (ib3 .eq. 2) idp = idp + 1
                        end if
c
c     case: ( n1l1, nl^2 | H | n1'l1', n'l'^2 )
                     else if (q1(2) .eq. 2 .and. q2(2).eq.2) then
                        s_12 = s1(2)
                        s_23 = s2(2)
                        if (ib .eq. 1) then
                           e_type = 61
                           if (ib3 .eq. 2)  d_type = 620
                           if (ib3 .eq. 2)  e_type = 60
                           if (ib3 .eq. 2)  idp = idp + 1
                        else if (ib .eq. 2) then
                           e_type = 62
                           if (ib3 .eq. 1)  d_type = 620
                           if (ib3 .eq. 1)  idp = idp + 1
                           if (ib3 .eq. 2)  e_type = 6
                        end if
c
c     case: ( n1l1, n'l', n''l'' | H | n1l1, nl^2 )
                     else if (q1(3) .eq. 1 .and. q2(2) .eq. 2) then
                        s_12 = ss1
                        s_23 = s2(2)
                        d_type = 1
                        if (ib .eq. 1) then
                           e_type = 4
                           if (ib3 .eq. 2)  d_type = 70
                           if (ib3 .eq. 2)  e_type = 50
                           if (ib3 .eq. 2)  idp = idp + 1
                        else if (ib .eq. 2) then
                           e_type = 7
                           if (ib3 .eq. 1)  d_type = 70
                           if (ib3 .eq. 1)  idp = idp + 1
                           if (ib3 .eq. 2)  e_type = 5
                        else if (ib .eq. 3) then
                           e_type = 5
                           if (ib3 .eq. 1)  d_type = 50
                           if (ib3 .eq. 2)  d_type = 10
                           if (ib3 .eq. 2)  e_type = 7
                           if (ib3 .eq. 2)  idp = idp + 1
                        end if
c
c     case: ( n1l1, nl^2 | H | n1'l1', n'l', n''l'' )
c     is calculated from ( n1l1, n'l', n''l'' | H | n1l1, nl^2 )
                     else if (q1(2) .eq. 2 .and. q2(3) .eq. 1) then
                        s_12 = ss2
                        s_23 = s1(2)
                        d_type = 1
                        if (ib .eq. 1) then
                           e_type = 4
                           if (ib3 .eq. 2)  d_type = 70
                           if (ib3 .eq. 2)  e_type = 7
                           if (ib3 .eq. 2)  idp = idp + 1
                           if (ib3 .eq. 3)  d_type = 50
                           if (ib3 .eq. 3)  e_type = 5
                        else if (ib .eq. 2) then
                           e_type = 50
                           if (ib3 .eq. 1)  d_type = 70
                           if (ib3 .eq. 1)  idp = idp + 1
                           if (ib3 .eq. 2)  e_type = 5
                           if (ib3 .eq. 3)  d_type = 10
                           if (ib3 .eq. 3)  e_type = 7
                           if (ib3 .eq. 3)  idp = idp + 1
                        end if
c
c     case: ( nl, n1l1, n2l2 | h | n'l', n1'l1', n2'l2' )
                     else if (q1(3) .eq. 1 .and. q2(3) .eq. 1) then
                        s_12 = ss1
                        if (ib .eq. 1) then
                           if (ib3 .eq. 2)  d_type = 8
                           if (ib3 .eq. 2)  e_type = 20
                           if (ib3 .eq. 2)  idp = idp + 1
                           if (ib3 .eq. 3)  d_type = 30
                           if (ib3 .eq. 3)  e_type = 3
                        else if (ib .eq. 2) then
                           e_type = 30
                           if (ib3 .eq. 1)  d_type = 8
                           if (ib3 .eq. 1)  idp = idp + 1
                           if (ib3 .eq. 2)  e_type = 3
                           if (ib3 .eq. 3)  d_type = 2
                           if (ib3 .eq. 3)  e_type = 20
                           if (ib3 .eq. 3)  idp = idp + 1
                        else if (ib .eq. 3) then
                           e_type = 3
                           if (ib3 .eq. 1)  d_type = 20
                           if (ib3 .eq. 2)  d_type = 2
                           if (ib3 .eq. 2)  e_type = 30
                           if (ib3 .eq. 2)  idp = idp + 1
                           if (ib3 .eq. 3)  e_type = 8
                        end if
c
c     case: ( nl, n1l1, n2l2 | H | n3l3^2, nl )
                     else if (q1(3) .eq. 1 .and. q2(1) .eq. 2) then
                        s_12 = ss1
                        s_23 = s2(1)
                        if (ib .eq. 1) then
                           e_type = 2
                           if (ib3 .eq. 2)  d_type = 30
                           if (ib3 .eq. 2)  e_type = 3
                        else if (ib .eq. 2) then
                           e_type = 30
                           if (ib3 .eq. 1)  d_type = 8
                           if (ib3 .eq. 1)  idp = idp + 1
                           if (ib3 .eq. 2)  d_type = 2
                           if (ib3 .eq. 2)  e_type = 20
                           if (ib3 .eq. 2)  idp = idp + 1
                        else if (ib .eq. 3) then
                           e_type = 3
                           if (ib3 .eq. 1)  d_type = 20
                           if (ib3 .eq. 2)  e_type = 8
                        end if
                     else
                        print*, j1, j2, ' -not ready !'
                        write(20, 80) ' -not ready !'
                     end if
c
                     dir = u_6j(d_type, s_12, s_23, 2, 2, 2, ls, eps, 2)
                     exc = u_6j(e_type, s_12, s_23, 2, 2, 2, ls, eps, 2)
c
                     if (Lnl(j1, ib) .eq. 0)  L_L = LL
                     if (Lnl(j1, ib) .ne. 0)  L_L = max0(l(1), l(2))
                     call d_e_hm (L_L, HL0, n, l, ip, ipp, pp,
     >                  eps, clkl, nwf, nr, nel, rn, wn, h, Zatom, tmp1, tmp2)
                     dir = dir * tmp1
                     exc = exc * tmp2
                     res_bar = c1_hm (ii, j1, j2, qnl, dir, exc, idp, eps, n,l)
                     res = res + res_bar
c
c     This is the end of parity check
                  end if
                  if (i_printout .gt. 1)  write(20, 315)
     >               n0, chl(l0), n(1), chl(l(1)), n(2), chl(l(2)),
     >               n(3), chl(l(3)), n(4), chl(l(4)),
     >               real(res_bar), real(res)
c
c     this the end of ib-loops and if's
               end if
            end do
         end do
      end if
c
c     Printouts for testing
c
         if (i_printout .gt. 1) then
            if (ns .gt. 2) then
               write(20, 320) ll12(j1), ss1, ll12(j2), ss2
               write(20, 325) ll, ls, j1, j2,
     >            (q1(i), nnl(j1, i), chl(lnl(j1, i)),
     >            chll(llnl(j1, i)), s1(i), i = 1,ns),
     >            (q2(i), nnl(j2, i), chl(lnl(j2, i)),
     >            chll(llnl(j2, i)), s2(i), i = 1,ns), real(res)
            else
               write(20, 310) ll, ls, j1, j2,
     >            (q1(i), nnl(j1, i), chl(lnl(j1, i)),
     >            chll(llnl(j1, i)), s1(i), i = 1,ns),
     >            (q2(i), nnl(j2, i), chl(lnl(j2, i)),
     >            chll(llnl(j2, i)), s2(i), i = 1,ns), real(res)
            end if
         end if
c
      return
      end


 */
