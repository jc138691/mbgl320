public class DLARRV { //SUBROUTINE DLARRV( N, VL, VU, D, L, PIVMIN,ISPLIT, M, DOL, DOU, MINRGP,RTOL1, RTOL2, W, WERR, WGAP,IBLOCK, INDEXW, GERS, Z, LDZ, ISUPPZ,WORK, IWORK, INFO )
public static void DLARRV( N, VL, VU, D, L, PIVMIN,ISPLIT, M, DOL, DOU, MINRGP,RTOL1, RTOL2, W, WERR, WGAP,IBLOCK, INDEXW, GERS, Z, LDZ, ISUPPZ,WORK, IWORK, INFO ) { //SUBROUTINE DLARRV( N, VL, VU, D, L, PIVMIN,ISPLIT, M, DOL, DOU, MINRGP,RTOL1, RTOL2, W, WERR, WGAP,IBLOCK, INDEXW, GERS, Z, LDZ, ISUPPZ,WORK, IWORK, INFO )
//*
//*  -- LAPACK auxiliary routine (version 3.3.1) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*  -- April 2011                                                      --
//*
//*     .. Scalar Arguments ..
int DOL, DOU, INFO, LDZ, M, N; //INTEGER            DOL, DOU, INFO, LDZ, M, N
double MINRGP, PIVMIN, RTOL1, RTOL2, VL, VU; //DOUBLE PRECISION   MINRGP, PIVMIN, RTOL1, RTOL2, VL, VU
//*     ..
//*     .. Array Arguments ..
int IBLOCK[], INDEXW[], ISPLIT[],ISUPPZ[], IWORK[]; //INTEGER            IBLOCK( * ), INDEXW( * ), ISPLIT( * ),ISUPPZ( * ), IWORK( * )
double D[], GERS[], L[], W[], WERR[],WGAP[], WORK[]; //DOUBLE PRECISION   D( * ), GERS( * ), L( * ), W( * ), WERR( * ),WGAP( * ), WORK( * )
double Z( LDZ, * ); //DOUBLE PRECISION  Z( LDZ, * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  DLARRV computes the eigenvectors of the tridiagonal matrix
//*  T = L D L**T given L, D and APPROXIMATIONS to the eigenvalues of L D L**T.
//*  The input eigenvalues should have been computed by DLARRE.
//*
//*  Arguments
//*  =========
//*
//*  N       (input) INTEGER
//*          The order of the matrix.  N >= 0.
//*
//*  VL      (input) DOUBLE PRECISION
//*  VU      (input) DOUBLE PRECISION
//*          Lower and upper bounds of the interval that contains the desired
//*          eigenvalues. VL < VU. Needed to compute gaps on the left or right
//*          end of the extremal eigenvalues in the desired RANGE.
//*
//*  D       (input/output) DOUBLE PRECISION array, dimension (N)
//*          On entry, the N diagonal elements of the diagonal matrix D.
//*          On exit, D may be overwritten.
//*
//*  L       (input/output) DOUBLE PRECISION array, dimension (N)
//*          On entry, the (N-1) subdiagonal elements of the unit
//*          bidiagonal matrix L are in elements 1 to N-1 of L
//*          (if the matrix is not splitted.) At the end of each block
//*          is stored the corresponding shift as given by DLARRE.
//*          On exit, L is overwritten.
//*
//*  PIVMIN  (input) DOUBLE PRECISION
//*          The minimum pivot allowed in the Sturm sequence.
//*
//*  ISPLIT  (input) INTEGER array, dimension (N)
//*          The splitting points, at which T breaks up into blocks.
//*          The first block consists of rows/columns 1 to
//*          ISPLIT( 1 ), the second of rows/columns ISPLIT( 1 )+1
//*          through ISPLIT( 2 ), etc.
//*
//*  M       (input) INTEGER
//*          The total number of input eigenvalues.  0 <= M <= N.
//*
//*  DOL     (input) INTEGER
//*  DOU     (input) INTEGER
//*          If the user wants to compute only selected eigenvectors from all
//*          the eigenvalues supplied, he can specify an index range DOL:DOU.
//*          Or else the setting DOL=1, DOU=M should be applied.
//*          Note that DOL and DOU refer to the order in which the eigenvalues
//*          are stored in W.
//*          If the user wants to compute only selected eigenpairs, then
//*          the columns DOL-1 to DOU+1 of the eigenvector space Z contain the
//*          computed eigenvectors. All other columns of Z are set to zero.
//*
//*  MINRGP  (input) DOUBLE PRECISION
//*
//*  RTOL1   (input) DOUBLE PRECISION
//*  RTOL2   (input) DOUBLE PRECISION
//*           Parameters for bisection.
//*           An interval [LEFT,RIGHT] has converged if
//*           RIGHT-LEFT.LT.MAX( RTOL1*GAP, RTOL2*MAX(|LEFT|,|RIGHT|) )
//*
//*  W       (input/output) DOUBLE PRECISION array, dimension (N)
//*          The first M elements of W contain the APPROXIMATE eigenvalues for
//*          which eigenvectors are to be computed.  The eigenvalues
//*          should be grouped by split-off block and ordered from
//*          smallest to largest within the block ( The output array
//*          W from DLARRE is expected here ). Furthermore, they are with
//*          respect to the shift of the corresponding root representation
//*          for their block. On exit, W holds the eigenvalues of the
//*          UNshifted matrix.
//*
//*  WERR    (input/output) DOUBLE PRECISION array, dimension (N)
//*          The first M elements contain the semiwidth of the uncertainty
//*          interval of the corresponding eigenvalue in W
//*
//*  WGAP    (input/output) DOUBLE PRECISION array, dimension (N)
//*          The separation from the right neighbor eigenvalue in W.
//*
//*  IBLOCK  (input) INTEGER array, dimension (N)
//*          The indices of the blocks (submatrices) associated with the
//*          corresponding eigenvalues in W; IBLOCK(i)=1 if eigenvalue
//*          W(i) belongs to the first block from the top, =2 if W(i)
//*          belongs to the second block, etc.
//*
//*  INDEXW  (input) INTEGER array, dimension (N)
//*          The indices of the eigenvalues within each block (submatrix);
//*          for example, INDEXW(i)= 10 and IBLOCK(i)=2 imply that the
//*          i-th eigenvalue W(i) is the 10-th eigenvalue in the second block.
//*
//*  GERS    (input) DOUBLE PRECISION array, dimension (2*N)
//*          The N Gerschgorin intervals (the i-th Gerschgorin interval
//*          is (GERS(2*i-1), GERS(2*i)). The Gerschgorin intervals should
//*          be computed from the original UNshifted matrix.
//*
//*  Z       (output) DOUBLE PRECISION array, dimension (LDZ, max(1,M) )
//*          If INFO = 0, the first M columns of Z contain the
//*          orthonormal eigenvectors of the matrix T
//*          corresponding to the input eigenvalues, with the i-th
//*          column of Z holding the eigenvector associated with W(i).
//*          Note: the user must ensure that at least max(1,M) columns are
//*          supplied in the array Z.
//*
//*  LDZ     (input) INTEGER
//*          The leading dimension of the array Z.  LDZ >= 1, and if
//*          JOBZ = 'V', LDZ >= max(1,N).
//*
//*  ISUPPZ  (output) INTEGER array, dimension ( 2*max(1,M) )
//*          The support of the eigenvectors in Z, i.e., the indices
//*          indicating the nonzero elements in Z. The I-th eigenvector
//*          is nonzero only in elements ISUPPZ( 2*I-1 ) through
//*          ISUPPZ( 2*I ).
//*
//*  WORK    (workspace) DOUBLE PRECISION array, dimension (12*N)
//*
//*  IWORK   (workspace) INTEGER array, dimension (7*N)
//*
//*  INFO    (output) INTEGER
//*          = 0:  successful exit
//*
//*          > 0:  A problem occured in DLARRV.
//*          < 0:  One of the called subroutines signaled an internal problem.
//*                Needs inspection of the corresponding parameter IINFO
//*                for further information.
//*
//*          =-1:  Problem in DLARRB when refining a child's eigenvalues.
//*          =-2:  Problem in DLARRF when computing the RRR of a child.
//*                When a child is inside a tight cluster, it can be difficult
//*                to find an RRR. A partial remedy from the user's point of
//*                view is to make the parameter MINRGP smaller and recompile.
//*                However, as the orthogonality of the computed vectors is
//*                proportional to 1/MINRGP, the user should be aware that
//*                he might be trading in precision when he decreases MINRGP.
//*          =-3:  Problem in DLARRB when refining a single eigenvalue
//*                after the Rayleigh correction was rejected.
//*          = 5:  The Rayleigh Quotient Iteration failed to converge to
//*                full accuracy in MAXITR steps.
//*
//*  Further Details
//*  ===============
//*
//*  Based on contributions by
//*     Beresford Parlett, University of California, Berkeley, USA
//*     Jim Demmel, University of California, Berkeley, USA
//*     Inderjit Dhillon, University of Texas, Austin, USA
//*     Osni Marques, LBNL/NERSC, USA
//*     Christof Voemel, University of California, Berkeley, USA
//*
//*  =====================================================================
//*
//*     .. Parameters ..
int MAXITR; //INTEGER            MAXITR
{  MAXITR = 10 ; } //PARAMETER          ( MAXITR = 10 )
double ZERO, ONE, TWO, THREE, FOUR, HALF; //DOUBLE PRECISION   ZERO, ONE, TWO, THREE, FOUR, HALF
{  ZERO = 0.0D0, ONE = 1.0D0,TWO = 2.0D0, THREE = 3.0D0,FOUR = 4.0D0, HALF = 0.5D0; } //PARAMETER          ( ZERO = 0.0D0, ONE = 1.0D0,TWO = 2.0D0, THREE = 3.0D0,FOUR = 4.0D0, HALF = 0.5D0)
//*     ..
//*     .. Local Scalars ..
boolean ESKIP, NEEDBS, STP2II, TRYRQC, USEDBS, USEDRQ; //LOGICAL            ESKIP, NEEDBS, STP2II, TRYRQC, USEDBS, USEDRQ
int DONE, I, IBEGIN, IDONE, IEND, II, IINDC1,IINDC2, IINDR, IINDWK, IINFO, IM, IN, INDEIG,INDLD, INDLLD, INDWRK, ISUPMN, ISUPMX, ITER,ITMP1, J, JBLK, K, MINIWSIZE, MINWSIZE, NCLUS,NDEPTH, NEGCNT, NEWCLS, NEWFST, NEWFTT, NEWLST,NEWSIZ, OFFSET, OLDCLS, OLDFST, OLDIEN, OLDLST,OLDNCL, P, PARITY, Q, WBEGIN, WEND, WINDEX,WINDMN, WINDPL, ZFROM, ZTO, ZUSEDL, ZUSEDU,ZUSEDW; //INTEGER            DONE, I, IBEGIN, IDONE, IEND, II, IINDC1,IINDC2, IINDR, IINDWK, IINFO, IM, IN, INDEIG,INDLD, INDLLD, INDWRK, ISUPMN, ISUPMX, ITER,ITMP1, J, JBLK, K, MINIWSIZE, MINWSIZE, NCLUS,NDEPTH, NEGCNT, NEWCLS, NEWFST, NEWFTT, NEWLST,NEWSIZ, OFFSET, OLDCLS, OLDFST, OLDIEN, OLDLST,OLDNCL, P, PARITY, Q, WBEGIN, WEND, WINDEX,WINDMN, WINDPL, ZFROM, ZTO, ZUSEDL, ZUSEDU,ZUSEDW
double BSTRES, BSTW, EPS, FUDGE, GAP, GAPTOL, GL, GU,LAMBDA, LEFT, LGAP, MINGMA, NRMINV, RESID,RGAP, RIGHT, RQCORR, RQTOL, SAVGAP, SGNDEF,SIGMA, SPDIAM, SSIGMA, TAU, TMP, TOL, ZTZ; //DOUBLE PRECISION   BSTRES, BSTW, EPS, FUDGE, GAP, GAPTOL, GL, GU,LAMBDA, LEFT, LGAP, MINGMA, NRMINV, RESID,RGAP, RIGHT, RQCORR, RQTOL, SAVGAP, SGNDEF,SIGMA, SPDIAM, SSIGMA, TAU, TMP, TOL, ZTZ
//*     ..
//*     .. External Functions ..
double DLAMCH; //DOUBLE PRECISION   DLAMCH
EXTERNAL           DLAMCH; //EXTERNAL           DLAMCH
//*     ..
//*     .. External Subroutines ..
EXTERNAL           DCOPY, DLAR1V, DLARRB, DLARRF, DLASET,DSCAL; //EXTERNAL           DCOPY, DLAR1V, DLARRB, DLARRF, DLASET,DSCAL
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC ABS, DBLE, MAX, MIN
//*     ..
//*     .. Executable Statements ..
//*     ..
//*     The first N entries of WORK are reserved for the eigenvalues
INDLD = N+1; //INDLD = N+1
INDLLD= 2*N+1; //INDLLD= 2*N+1
INDWRK= 3*N+1; //INDWRK= 3*N+1
MINWSIZE = 12 * N; //MINWSIZE = 12 * N
for ( 5 I= 1;MINWSIZE) { //DO 5 I= 1,MINWSIZE
WORK( I ) = ZERO; //WORK( I ) = ZERO
5    CONTINUE; //5    CONTINUE
//*     IWORK(IINDR+1:IINDR+N) hold the twist indices R for the
//*     factorization used to compute the FP vector
IINDR = 0; //IINDR = 0
//*     IWORK(IINDC1+1:IINC2+N) are used to store the clusters of the current
//*     layer and the one above.
IINDC1 = N; //IINDC1 = N
IINDC2 = 2*N; //IINDC2 = 2*N
IINDWK = 3*N + 1; //IINDWK = 3*N + 1
MINIWSIZE = 7 * N; //MINIWSIZE = 7 * N
for ( 10 I= 1;MINIWSIZE) { //DO 10 I= 1,MINIWSIZE
IWORK( I ) = 0; //IWORK( I ) = 0
10   CONTINUE; //10   CONTINUE
ZUSEDL = 1; //ZUSEDL = 1
if (DOL > 1) { //IF(DOL.GT.1) THEN
//*        Set lower bound for use of Z
ZUSEDL = DOL-1; //ZUSEDL = DOL-1
} // ENDIF
ZUSEDU = M; //ZUSEDU = M
if (DOU < M) { //IF(DOU.LT.M) THEN
//*        Set lower bound for use of Z
ZUSEDU = DOU+1; //ZUSEDU = DOU+1
} // ENDIF
//*     The width of the part of Z that is used
ZUSEDW = ZUSEDU - ZUSEDL + 1; //ZUSEDW = ZUSEDU - ZUSEDL + 1
CALL DLASET( 'Full', N, ZUSEDW, ZERO, ZERO,Z(1,ZUSEDL), LDZ ); //CALL DLASET( 'Full', N, ZUSEDW, ZERO, ZERO,Z(1,ZUSEDL), LDZ )
EPS = DLAMCH( 'Precision' ); //EPS = DLAMCH( 'Precision' )
RQTOL = TWO * EPS; //RQTOL = TWO * EPS
//*
//*     Set expert flags for standard code.
TRYRQC =  true; //TRYRQC = .TRUE.
if ((DOL == 1) && (DOU == M)) { //IF((DOL.EQ.1).AND.(DOU.EQ.M)) THEN
} else { // ELSE
//*        Only selected eigenpairs are computed. Since the other evalues
//*        are not refined by RQ iteration, bisection has to compute to full
//*        accuracy.
RTOL1 = FOUR * EPS; //RTOL1 = FOUR * EPS
RTOL2 = FOUR * EPS; //RTOL2 = FOUR * EPS
} // ENDIF
//*     The entries WBEGIN:WEND in W, WERR, WGAP correspond to the
//*     desired eigenvalues. The support of the nonzero eigenvector
//*     entries is contained in the interval IBEGIN:IEND.
//*     Remark that if k eigenpairs are desired, then the eigenvectors
//*     are stored in k contiguous columns of Z.
//*     DONE is the number of eigenvectors already computed
DONE = 0; //DONE = 0
IBEGIN = 1; //IBEGIN = 1
WBEGIN = 1; //WBEGIN = 1
for ( 170 JBLK = 1; IBLOCK( M )) { //DO 170 JBLK = 1, IBLOCK( M )
IEND = ISPLIT( JBLK ); //IEND = ISPLIT( JBLK )
SIGMA = L( IEND ); //SIGMA = L( IEND )
//*        Find the eigenvectors of the submatrix indexed IBEGIN
//*        through IEND.
WEND = WBEGIN - 1; //WEND = WBEGIN - 1
15      CONTINUE; //15      CONTINUE
if ( WEND < M ) { //IF( WEND.LT.M ) THEN
if ( IBLOCK( WEND+1 ) == JBLK ) { //IF( IBLOCK( WEND+1 ).EQ.JBLK ) THEN
WEND = WEND + 1; //WEND = WEND + 1
GO TO 15; //GO TO 15
} // END IF
} // END IF
if ( WEND < WBEGIN ) { //IF( WEND.LT.WBEGIN ) THEN
IBEGIN = IEND + 1; //IBEGIN = IEND + 1
GO TO 170; //GO TO 170
ELSEIF( (WEND < DOL) || (WBEGIN > DOU) ) THEN; //ELSEIF( (WEND.LT.DOL).OR.(WBEGIN.GT.DOU) ) THEN
IBEGIN = IEND + 1; //IBEGIN = IEND + 1
WBEGIN = WEND + 1; //WBEGIN = WEND + 1
GO TO 170; //GO TO 170
} // END IF
//*        Find local spectral diameter of the block
GL = GERS( 2*IBEGIN-1 ); //GL = GERS( 2*IBEGIN-1 )
GU = GERS( 2*IBEGIN ); //GU = GERS( 2*IBEGIN )
for ( 20 I = IBEGIN+1 ; IEND) { //DO 20 I = IBEGIN+1 , IEND
GL = MIN( GERS( 2*I-1 ), GL ); //GL = MIN( GERS( 2*I-1 ), GL )
GU = MAX( GERS( 2*I ), GU ); //GU = MAX( GERS( 2*I ), GU )
20      CONTINUE; //20      CONTINUE
SPDIAM = GU - GL; //SPDIAM = GU - GL
//*        OLDIEN is the last index of the previous block
OLDIEN = IBEGIN - 1; //OLDIEN = IBEGIN - 1
//*        Calculate the size of the current block
IN = IEND - IBEGIN + 1; //IN = IEND - IBEGIN + 1
//*        The number of eigenvalues in the current block
IM = WEND - WBEGIN + 1; //IM = WEND - WBEGIN + 1
//*        This is for a 1x1 block
if ( IBEGIN == IEND ) { //IF( IBEGIN.EQ.IEND ) THEN
DONE = DONE+1; //DONE = DONE+1
Z( IBEGIN, WBEGIN ) = ONE; //Z( IBEGIN, WBEGIN ) = ONE
ISUPPZ( 2*WBEGIN-1 ) = IBEGIN; //ISUPPZ( 2*WBEGIN-1 ) = IBEGIN
ISUPPZ( 2*WBEGIN ) = IBEGIN; //ISUPPZ( 2*WBEGIN ) = IBEGIN
W( WBEGIN ) = W( WBEGIN ) + SIGMA; //W( WBEGIN ) = W( WBEGIN ) + SIGMA
WORK( WBEGIN ) = W( WBEGIN ); //WORK( WBEGIN ) = W( WBEGIN )
IBEGIN = IEND + 1; //IBEGIN = IEND + 1
WBEGIN = WBEGIN + 1; //WBEGIN = WBEGIN + 1
GO TO 170; //GO TO 170
} // END IF
//*        The desired (shifted) eigenvalues are stored in W(WBEGIN:WEND)
//*        Note that these can be approximations, in this case, the corresp.
//*        entries of WERR give the size of the uncertainty interval.
//*        The eigenvalue approximations will be refined when necessary as
//*        high relative accuracy is required for the computation of the
//*        corresponding eigenvectors.
CALL DCOPY( IM, W( WBEGIN ), 1,WORK( WBEGIN ), 1 ); //CALL DCOPY( IM, W( WBEGIN ), 1,WORK( WBEGIN ), 1 )
//*        We store in W the eigenvalue approximations w.r.t. the original
//*        matrix T.
for ( 30 I=1;IM) { //DO 30 I=1,IM
W(WBEGIN+I-1) = W(WBEGIN+I-1)+SIGMA; //W(WBEGIN+I-1) = W(WBEGIN+I-1)+SIGMA
30      CONTINUE; //30      CONTINUE
//*        NDEPTH is the current depth of the representation tree
NDEPTH = 0; //NDEPTH = 0
//*        PARITY is either 1 or 0
PARITY = 1; //PARITY = 1
//*        NCLUS is the number of clusters for the next level of the
//*        representation tree, we start with NCLUS = 1 for the root
NCLUS = 1; //NCLUS = 1
IWORK( IINDC1+1 ) = 1; //IWORK( IINDC1+1 ) = 1
IWORK( IINDC1+2 ) = IM; //IWORK( IINDC1+2 ) = IM
//*        IDONE is the number of eigenvectors already computed in the current
//*        block
IDONE = 0; //IDONE = 0
//*        loop while( IDONE.LT.IM )
//*        generate the representation tree for the current block and
//*        compute the eigenvectors
40    CONTINUE; //40    CONTINUE
if ( IDONE < IM ) { //IF( IDONE.LT.IM ) THEN
//*           This is a crude protection against infinitely deep trees
if ( NDEPTH > M ) { //IF( NDEPTH.GT.M ) THEN
INFO = -2; //INFO = -2
} // RETURN
} // ENDIF
//*           breadth first processing of the current level of the representation
//*           tree: OLDNCL = number of clusters on current level
OLDNCL = NCLUS; //OLDNCL = NCLUS
//*           reset NCLUS to count the number of child clusters
NCLUS = 0; //NCLUS = 0
//*
PARITY = 1 - PARITY; //PARITY = 1 - PARITY
if ( PARITY == 0 ) { //IF( PARITY.EQ.0 ) THEN
OLDCLS = IINDC1; //OLDCLS = IINDC1
NEWCLS = IINDC2; //NEWCLS = IINDC2
} else { // ELSE
OLDCLS = IINDC2; //OLDCLS = IINDC2
NEWCLS = IINDC1; //NEWCLS = IINDC1
} // END IF
//*           Process the clusters on the current level
for ( 150 I = 1; OLDNCL) { //DO 150 I = 1, OLDNCL
J = OLDCLS + 2*I; //J = OLDCLS + 2*I
//*              OLDFST, OLDLST = first, last index of current cluster.
//*                               cluster indices start with 1 and are relative
//*                               to WBEGIN when accessing W, WGAP, WERR, Z
OLDFST = IWORK( J-1 ); //OLDFST = IWORK( J-1 )
OLDLST = IWORK( J ); //OLDLST = IWORK( J )
if ( NDEPTH > 0 ) { //IF( NDEPTH.GT.0 ) THEN
//*                 Retrieve relatively robust representation (RRR) of cluster
//*                 that has been computed at the previous level
//*                 The RRR is stored in Z and overwritten once the eigenvectors
//*                 have been computed or when the cluster is refined
if ((DOL == 1) && (DOU == M)) { //IF((DOL.EQ.1).AND.(DOU.EQ.M)) THEN
//*                    Get representation from location of the leftmost evalue
//*                    of the cluster
J = WBEGIN + OLDFST - 1; //J = WBEGIN + OLDFST - 1
} else { // ELSE
if (WBEGIN+OLDFST-1 < DOL) { //IF(WBEGIN+OLDFST-1.LT.DOL) THEN
//*                       Get representation from the left end of Z array
J = DOL - 1; //J = DOL - 1
ELSEIF(WBEGIN+OLDFST-1 > DOU) THEN; //ELSEIF(WBEGIN+OLDFST-1.GT.DOU) THEN
//*                       Get representation from the right end of Z array
J = DOU; //J = DOU
} else { // ELSE
J = WBEGIN + OLDFST - 1; //J = WBEGIN + OLDFST - 1
} // ENDIF
} // ENDIF
CALL DCOPY( IN, Z( IBEGIN, J ), 1, D( IBEGIN ), 1 ); //CALL DCOPY( IN, Z( IBEGIN, J ), 1, D( IBEGIN ), 1 )
CALL DCOPY( IN-1, Z( IBEGIN, J+1 ), 1, L( IBEGIN ),1 ); //CALL DCOPY( IN-1, Z( IBEGIN, J+1 ), 1, L( IBEGIN ),1 )
SIGMA = Z( IEND, J+1 ); //SIGMA = Z( IEND, J+1 )
//*                 Set the corresponding entries in Z to zero
CALL DLASET( 'Full', IN, 2, ZERO, ZERO,Z( IBEGIN, J), LDZ ); //CALL DLASET( 'Full', IN, 2, ZERO, ZERO,Z( IBEGIN, J), LDZ )
} // END IF
//*              Compute DL and DLL of current RRR
for ( 50 J = IBEGIN; IEND-1) { //DO 50 J = IBEGIN, IEND-1
TMP = D( J )*L( J ); //TMP = D( J )*L( J )
WORK( INDLD-1+J ) = TMP; //WORK( INDLD-1+J ) = TMP
WORK( INDLLD-1+J ) = TMP*L( J ); //WORK( INDLLD-1+J ) = TMP*L( J )
50          CONTINUE; //50          CONTINUE
if ( NDEPTH > 0 ) { //IF( NDEPTH.GT.0 ) THEN
//*                 P and Q are index of the first and last eigenvalue to compute
//*                 within the current block
P = INDEXW( WBEGIN-1+OLDFST ); //P = INDEXW( WBEGIN-1+OLDFST )
Q = INDEXW( WBEGIN-1+OLDLST ); //Q = INDEXW( WBEGIN-1+OLDLST )
//*                 Offset for the arrays WORK, WGAP and WERR, i.e., the P-OFFSET
//*                 through the Q-OFFSET elements of these arrays are to be used.
//*                  OFFSET = P-OLDFST
OFFSET = INDEXW( WBEGIN ) - 1; //OFFSET = INDEXW( WBEGIN ) - 1
//*                 perform limited bisection (if necessary) to get approximate
//*                 eigenvalues to the precision needed.
CALL DLARRB( IN, D( IBEGIN ),WORK(INDLLD+IBEGIN-1),P, Q, RTOL1, RTOL2, OFFSET,WORK(WBEGIN),WGAP(WBEGIN),WERR(WBEGIN),WORK( INDWRK ), IWORK( IINDWK ),PIVMIN, SPDIAM, IN, IINFO ); //CALL DLARRB( IN, D( IBEGIN ),WORK(INDLLD+IBEGIN-1),P, Q, RTOL1, RTOL2, OFFSET,WORK(WBEGIN),WGAP(WBEGIN),WERR(WBEGIN),WORK( INDWRK ), IWORK( IINDWK ),PIVMIN, SPDIAM, IN, IINFO )
if ( IINFO != 0 ) { //IF( IINFO.NE.0 ) THEN
INFO = -1; //INFO = -1
} // RETURN
} // ENDIF
//*                 We also recompute the extremal gaps. W holds all eigenvalues
//*                 of the unshifted matrix and must be used for computation
//*                 of WGAP, the entries of WORK might stem from RRRs with
//*                 different shifts. The gaps from WBEGIN-1+OLDFST to
//*                 WBEGIN-1+OLDLST are correctly computed in DLARRB.
//*                 However, we only allow the gaps to become greater since
//*                 this is what should happen when we decrease WERR
if ( OLDFST > 1) { //IF( OLDFST.GT.1) THEN
WGAP( WBEGIN+OLDFST-2 ) =MAX(WGAP(WBEGIN+OLDFST-2),W(WBEGIN+OLDFST-1)-WERR(WBEGIN+OLDFST-1)- W(WBEGIN+OLDFST-2)-WERR(WBEGIN+OLDFST-2) ); //WGAP( WBEGIN+OLDFST-2 ) =MAX(WGAP(WBEGIN+OLDFST-2),W(WBEGIN+OLDFST-1)-WERR(WBEGIN+OLDFST-1)- W(WBEGIN+OLDFST-2)-WERR(WBEGIN+OLDFST-2) )
} // ENDIF
if ( WBEGIN + OLDLST -1  <  WEND ) { //IF( WBEGIN + OLDLST -1 .LT. WEND ) THEN
WGAP( WBEGIN+OLDLST-1 ) =MAX(WGAP(WBEGIN+OLDLST-1),W(WBEGIN+OLDLST)-WERR(WBEGIN+OLDLST)- W(WBEGIN+OLDLST-1)-WERR(WBEGIN+OLDLST-1) ); //WGAP( WBEGIN+OLDLST-1 ) =MAX(WGAP(WBEGIN+OLDLST-1),W(WBEGIN+OLDLST)-WERR(WBEGIN+OLDLST)- W(WBEGIN+OLDLST-1)-WERR(WBEGIN+OLDLST-1) )
} // ENDIF
//*                 Each time the eigenvalues in WORK get refined, we store
//*                 the newly found approximation with all shifts applied in W
for ( 53 J=OLDFST;OLDLST) { //DO 53 J=OLDFST,OLDLST
W(WBEGIN+J-1) = WORK(WBEGIN+J-1)+SIGMA; //W(WBEGIN+J-1) = WORK(WBEGIN+J-1)+SIGMA
53               CONTINUE; //53               CONTINUE
} // END IF
//*              Process the current node.
NEWFST = OLDFST; //NEWFST = OLDFST
for ( 140 J = OLDFST; OLDLST) { //DO 140 J = OLDFST, OLDLST
if ( J == OLDLST ) { //IF( J.EQ.OLDLST ) THEN
//*                    we are at the right end of the cluster, this is also the
//*                    boundary of the child cluster
NEWLST = J; //NEWLST = J
} else if  ( WGAP( WBEGIN + J -1) >= MINRGP* ABS( WORK(WBEGIN + J -1) ) ) { //ELSE IF ( WGAP( WBEGIN + J -1).GE.MINRGP* ABS( WORK(WBEGIN + J -1) ) ) THEN
//*                    the right relative gap is big enough, the child cluster
//*                    (NEWFST,..,NEWLST) is well separated from the following
NEWLST = J; //NEWLST = J
} else { // ELSE
//*                    inside a child cluster, the relative gap is not
//*                    big enough.
GOTO 140; //GOTO 140
} // END IF
//*                 Compute size of child cluster found
NEWSIZ = NEWLST - NEWFST + 1; //NEWSIZ = NEWLST - NEWFST + 1
//*                 NEWFTT is the place in Z where the new RRR or the computed
//*                 eigenvector is to be stored
if ((DOL == 1) && (DOU == M)) { //IF((DOL.EQ.1).AND.(DOU.EQ.M)) THEN
//*                    Store representation at location of the leftmost evalue
//*                    of the cluster
NEWFTT = WBEGIN + NEWFST - 1; //NEWFTT = WBEGIN + NEWFST - 1
} else { // ELSE
if (WBEGIN+NEWFST-1 < DOL) { //IF(WBEGIN+NEWFST-1.LT.DOL) THEN
//*                       Store representation at the left end of Z array
NEWFTT = DOL - 1; //NEWFTT = DOL - 1
ELSEIF(WBEGIN+NEWFST-1 > DOU) THEN; //ELSEIF(WBEGIN+NEWFST-1.GT.DOU) THEN
//*                       Store representation at the right end of Z array
NEWFTT = DOU; //NEWFTT = DOU
} else { // ELSE
NEWFTT = WBEGIN + NEWFST - 1; //NEWFTT = WBEGIN + NEWFST - 1
} // ENDIF
} // ENDIF
if ( NEWSIZ > 1) { //IF( NEWSIZ.GT.1) THEN
//*
//*                    Current child is not a singleton but a cluster.
//*                    Compute and store new representation of child.
//*
//*
//*                    Compute left and right cluster gap.
//*
//*                    LGAP and RGAP are not computed from WORK because
//*                    the eigenvalue approximations may stem from RRRs
//*                    different shifts. However, W hold all eigenvalues
//*                    of the unshifted matrix. Still, the entries in WGAP
//*                    have to be computed from WORK since the entries
//*                    in W might be of the same order so that gaps are not
//*                    exhibited correctly for very close eigenvalues.
if ( NEWFST == 1 ) { //IF( NEWFST.EQ.1 ) THEN
LGAP = MAX( ZERO,W(WBEGIN)-WERR(WBEGIN) - VL ); //LGAP = MAX( ZERO,W(WBEGIN)-WERR(WBEGIN) - VL )
} else { // ELSE
LGAP = WGAP( WBEGIN+NEWFST-2 ); //LGAP = WGAP( WBEGIN+NEWFST-2 )
} // ENDIF
RGAP = WGAP( WBEGIN+NEWLST-1 ); //RGAP = WGAP( WBEGIN+NEWLST-1 )
//*
//*                    Compute left- and rightmost eigenvalue of child
//*                    to high precision in order to shift as close
//*                    as possible and obtain as large relative gaps
//*                    as possible
//*
for ( 55 K =1;2) { //DO 55 K =1,2
if (K == 1) { //IF(K.EQ.1) THEN
P = INDEXW( WBEGIN-1+NEWFST ); //P = INDEXW( WBEGIN-1+NEWFST )
} else { // ELSE
P = INDEXW( WBEGIN-1+NEWLST ); //P = INDEXW( WBEGIN-1+NEWLST )
} // ENDIF
OFFSET = INDEXW( WBEGIN ) - 1; //OFFSET = INDEXW( WBEGIN ) - 1
CALL DLARRB( IN, D(IBEGIN),WORK( INDLLD+IBEGIN-1 ),P,P,RQTOL, RQTOL, OFFSET,WORK(WBEGIN),WGAP(WBEGIN),WERR(WBEGIN),WORK( INDWRK ),IWORK( IINDWK ), PIVMIN, SPDIAM,IN, IINFO ); //CALL DLARRB( IN, D(IBEGIN),WORK( INDLLD+IBEGIN-1 ),P,P,RQTOL, RQTOL, OFFSET,WORK(WBEGIN),WGAP(WBEGIN),WERR(WBEGIN),WORK( INDWRK ),IWORK( IINDWK ), PIVMIN, SPDIAM,IN, IINFO )
55                  CONTINUE; //55                  CONTINUE
//*
if ((WBEGIN+NEWLST-1 < DOL) || (WBEGIN+NEWFST-1 > DOU)) { //IF((WBEGIN+NEWLST-1.LT.DOL).OR.(WBEGIN+NEWFST-1.GT.DOU)) THEN
//*                       if the cluster contains no desired eigenvalues
//*                       skip the computation of that branch of the rep. tree
//*
//*                       We could skip before the refinement of the extremal
//*                       eigenvalues of the child, but then the representation
//*                       tree could be different from the one when nothing is
//*                       skipped. For this reason we skip at this place.
IDONE = IDONE + NEWLST - NEWFST + 1; //IDONE = IDONE + NEWLST - NEWFST + 1
GOTO 139; //GOTO 139
} // ENDIF
//*
//*                    Compute RRR of child cluster.
//*                    Note that the new RRR is stored in Z
//*
//*                    DLARRF needs LWORK = 2*N
CALL DLARRF( IN, D( IBEGIN ), L( IBEGIN ),WORK(INDLD+IBEGIN-1),NEWFST, NEWLST, WORK(WBEGIN),WGAP(WBEGIN), WERR(WBEGIN),SPDIAM, LGAP, RGAP, PIVMIN, TAU,Z(IBEGIN, NEWFTT),Z(IBEGIN, NEWFTT+1),WORK( INDWRK ), IINFO ); //CALL DLARRF( IN, D( IBEGIN ), L( IBEGIN ),WORK(INDLD+IBEGIN-1),NEWFST, NEWLST, WORK(WBEGIN),WGAP(WBEGIN), WERR(WBEGIN),SPDIAM, LGAP, RGAP, PIVMIN, TAU,Z(IBEGIN, NEWFTT),Z(IBEGIN, NEWFTT+1),WORK( INDWRK ), IINFO )
if ( IINFO == 0 ) { //IF( IINFO.EQ.0 ) THEN
//*                       a new RRR for the cluster was found by DLARRF
//*                       update shift and store it
SSIGMA = SIGMA + TAU; //SSIGMA = SIGMA + TAU
Z( IEND, NEWFTT+1 ) = SSIGMA; //Z( IEND, NEWFTT+1 ) = SSIGMA
//*                       WORK() are the midpoints and WERR() the semi-width
//*                       Note that the entries in W are unchanged.
for ( 116 K = NEWFST; NEWLST) { //DO 116 K = NEWFST, NEWLST
FUDGE =THREE*EPS*ABS(WORK(WBEGIN+K-1)); //FUDGE =THREE*EPS*ABS(WORK(WBEGIN+K-1))
WORK( WBEGIN + K - 1 ) =WORK( WBEGIN + K - 1) - TAU; //WORK( WBEGIN + K - 1 ) =WORK( WBEGIN + K - 1) - TAU
FUDGE = FUDGE +FOUR*EPS*ABS(WORK(WBEGIN+K-1)); //FUDGE = FUDGE +FOUR*EPS*ABS(WORK(WBEGIN+K-1))
//*                          Fudge errors
WERR( WBEGIN + K - 1 ) =WERR( WBEGIN + K - 1 ) + FUDGE; //WERR( WBEGIN + K - 1 ) =WERR( WBEGIN + K - 1 ) + FUDGE
//*                          Gaps are not fudged. Provided that WERR is small
//*                          when eigenvalues are close, a zero gap indicates
//*                          that a new representation is needed for resolving
//*                          the cluster. A fudge could lead to a wrong decision
//*                          of judging eigenvalues 'separated' which in
//*                          reality are not. This could have a negative impact
//*                          on the orthogonality of the computed eigenvectors.
116                    CONTINUE; //116                    CONTINUE
NCLUS = NCLUS + 1; //NCLUS = NCLUS + 1
K = NEWCLS + 2*NCLUS; //K = NEWCLS + 2*NCLUS
IWORK( K-1 ) = NEWFST; //IWORK( K-1 ) = NEWFST
IWORK( K ) = NEWLST; //IWORK( K ) = NEWLST
} else { // ELSE
INFO = -2; //INFO = -2
} // RETURN
} // ENDIF
} else { // ELSE
//*
//*                    Compute eigenvector of singleton
//*
ITER = 0; //ITER = 0
//*
TOL = FOUR * LOG(DBLE(IN)) * EPS; //TOL = FOUR * LOG(DBLE(IN)) * EPS
//*
K = NEWFST; //K = NEWFST
WINDEX = WBEGIN + K - 1; //WINDEX = WBEGIN + K - 1
WINDMN = MAX(WINDEX - 1,1); //WINDMN = MAX(WINDEX - 1,1)
WINDPL = MIN(WINDEX + 1,M); //WINDPL = MIN(WINDEX + 1,M)
LAMBDA = WORK( WINDEX ); //LAMBDA = WORK( WINDEX )
DONE = DONE + 1; //DONE = DONE + 1
//*                    Check if eigenvector computation is to be skipped
if ((WINDEX < DOL) || (WINDEX > DOU)) { //IF((WINDEX.LT.DOL).OR.(WINDEX.GT.DOU)) THEN
ESKIP =  true; //ESKIP = .TRUE.
GOTO 125; //GOTO 125
} else { // ELSE
ESKIP =  false; //ESKIP = .FALSE.
} // ENDIF
LEFT = WORK( WINDEX ) - WERR( WINDEX ); //LEFT = WORK( WINDEX ) - WERR( WINDEX )
RIGHT = WORK( WINDEX ) + WERR( WINDEX ); //RIGHT = WORK( WINDEX ) + WERR( WINDEX )
INDEIG = INDEXW( WINDEX ); //INDEIG = INDEXW( WINDEX )
//*                    Note that since we compute the eigenpairs for a child,
//*                    all eigenvalue approximations are w.r.t the same shift.
//*                    In this case, the entries in WORK should be used for
//*                    computing the gaps since they exhibit even very small
//*                    differences in the eigenvalues, as opposed to the
//*                    entries in W which might "look" the same.
if ( K  ==  1) { //IF( K .EQ. 1) THEN
//*                       In the case RANGE='I' and with not much initial
//*                       accuracy in LAMBDA and VL, the formula
//*                       LGAP = MAX( ZERO, (SIGMA - VL) + LAMBDA )
//*                       can lead to an overestimation of the left gap and
//*                       thus to inadequately early RQI 'convergence'.
//*                       Prevent this by forcing a small left gap.
LGAP = EPS*MAX(ABS(LEFT),ABS(RIGHT)); //LGAP = EPS*MAX(ABS(LEFT),ABS(RIGHT))
} else { // ELSE
LGAP = WGAP(WINDMN); //LGAP = WGAP(WINDMN)
} // ENDIF
if ( K  ==  IM) { //IF( K .EQ. IM) THEN
//*                       In the case RANGE='I' and with not much initial
//*                       accuracy in LAMBDA and VU, the formula
//*                       can lead to an overestimation of the right gap and
//*                       thus to inadequately early RQI 'convergence'.
//*                       Prevent this by forcing a small right gap.
RGAP = EPS*MAX(ABS(LEFT),ABS(RIGHT)); //RGAP = EPS*MAX(ABS(LEFT),ABS(RIGHT))
} else { // ELSE
RGAP = WGAP(WINDEX); //RGAP = WGAP(WINDEX)
} // ENDIF
GAP = MIN( LGAP, RGAP ); //GAP = MIN( LGAP, RGAP )
if (( K  ==  1) || (K  ==  IM)) { //IF(( K .EQ. 1).OR.(K .EQ. IM)) THEN
//*                       The eigenvector support can become wrong
//*                       because significant entries could be cut off due to a
//*                       large GAPTOL parameter in LAR1V. Prevent this.
GAPTOL = ZERO; //GAPTOL = ZERO
} else { // ELSE
GAPTOL = GAP * EPS; //GAPTOL = GAP * EPS
} // ENDIF
ISUPMN = IN; //ISUPMN = IN
ISUPMX = 1; //ISUPMX = 1
//*                    Update WGAP so that it holds the minimum gap
//*                    to the left or the right. This is crucial in the
//*                    case where bisection is used to ensure that the
//*                    eigenvalue is refined up to the required precision.
//*                    The correct value is restored afterwards.
SAVGAP = WGAP(WINDEX); //SAVGAP = WGAP(WINDEX)
WGAP(WINDEX) = GAP; //WGAP(WINDEX) = GAP
//*                    We want to use the Rayleigh Quotient Correction
//*                    as often as possible since it converges quadratically
//*                    when we are close enough to the desired eigenvalue.
//*                    However, the Rayleigh Quotient can have the wrong sign
//*                    and lead us away from the desired eigenvalue. In this
//*                    case, the best we can do is to use bisection.
USEDBS =  false; //USEDBS = .FALSE.
USEDRQ =  false; //USEDRQ = .FALSE.
//*                    Bisection is initially turned off unless it is forced
NEEDBS =   !TRYRQC; //NEEDBS =  .NOT.TRYRQC
120                 CONTINUE; //120                 CONTINUE
//*                    Check if bisection should be used to refine eigenvalue
if (NEEDBS) { //IF(NEEDBS) THEN
//*                       Take the bisection as new iterate
USEDBS =  true; //USEDBS = .TRUE.
ITMP1 = IWORK( IINDR+WINDEX ); //ITMP1 = IWORK( IINDR+WINDEX )
OFFSET = INDEXW( WBEGIN ) - 1; //OFFSET = INDEXW( WBEGIN ) - 1
CALL DLARRB( IN, D(IBEGIN),WORK(INDLLD+IBEGIN-1),INDEIG,INDEIG,ZERO, TWO*EPS, OFFSET,WORK(WBEGIN),WGAP(WBEGIN),WERR(WBEGIN),WORK( INDWRK ),IWORK( IINDWK ), PIVMIN, SPDIAM,ITMP1, IINFO ); //CALL DLARRB( IN, D(IBEGIN),WORK(INDLLD+IBEGIN-1),INDEIG,INDEIG,ZERO, TWO*EPS, OFFSET,WORK(WBEGIN),WGAP(WBEGIN),WERR(WBEGIN),WORK( INDWRK ),IWORK( IINDWK ), PIVMIN, SPDIAM,ITMP1, IINFO )
if ( IINFO != 0 ) { //IF( IINFO.NE.0 ) THEN
INFO = -3; //INFO = -3
} // RETURN
} // ENDIF
LAMBDA = WORK( WINDEX ); //LAMBDA = WORK( WINDEX )
//*                       Reset twist index from inaccurate LAMBDA to
//*                       force computation of true MINGMA
IWORK( IINDR+WINDEX ) = 0; //IWORK( IINDR+WINDEX ) = 0
} // ENDIF
//*                    Given LAMBDA, compute the eigenvector.
CALL DLAR1V( IN, 1, IN, LAMBDA, D( IBEGIN ),L( IBEGIN ), WORK(INDLD+IBEGIN-1),WORK(INDLLD+IBEGIN-1),PIVMIN, GAPTOL, Z( IBEGIN, WINDEX ), !USEDBS, NEGCNT, ZTZ, MINGMA,IWORK( IINDR+WINDEX ), ISUPPZ( 2*WINDEX-1 ),NRMINV, RESID, RQCORR, WORK( INDWRK ) ); //CALL DLAR1V( IN, 1, IN, LAMBDA, D( IBEGIN ),L( IBEGIN ), WORK(INDLD+IBEGIN-1),WORK(INDLLD+IBEGIN-1),PIVMIN, GAPTOL, Z( IBEGIN, WINDEX ),.NOT.USEDBS, NEGCNT, ZTZ, MINGMA,IWORK( IINDR+WINDEX ), ISUPPZ( 2*WINDEX-1 ),NRMINV, RESID, RQCORR, WORK( INDWRK ) )
if (ITER  ==  0) { //IF(ITER .EQ. 0) THEN
BSTRES = RESID; //BSTRES = RESID
BSTW = LAMBDA; //BSTW = LAMBDA
ELSEIF(RESID < BSTRES) THEN; //ELSEIF(RESID.LT.BSTRES) THEN
BSTRES = RESID; //BSTRES = RESID
BSTW = LAMBDA; //BSTW = LAMBDA
} // ENDIF
ISUPMN = MIN(ISUPMN,ISUPPZ( 2*WINDEX-1 )); //ISUPMN = MIN(ISUPMN,ISUPPZ( 2*WINDEX-1 ))
ISUPMX = MAX(ISUPMX,ISUPPZ( 2*WINDEX )); //ISUPMX = MAX(ISUPMX,ISUPPZ( 2*WINDEX ))
ITER = ITER + 1; //ITER = ITER + 1
//*                    sin alpha <= |resid|/gap
//*                    Note that both the residual and the gap are
//*                    proportional to the matrix, so ||T|| doesn't play
//*                    a role in the quotient
//*
//*                    Convergence test for Rayleigh-Quotient iteration
//*                    (omitted when Bisection has been used)
//*
if ( RESID > TOL*GAP  &&  ABS( RQCORR ) > RQTOL*ABS( LAMBDA )  &&   ! USEDBS)THEN; //IF( RESID.GT.TOL*GAP .AND. ABS( RQCORR ).GT.RQTOL*ABS( LAMBDA ) .AND. .NOT. USEDBS)THEN
//*                       We need to check that the RQCORR update doesn't
//*                       move the eigenvalue away from the desired one and
//*                       towards a neighbor. -> protection with bisection
if (INDEIG <= NEGCNT) { //IF(INDEIG.LE.NEGCNT) THEN
//*                          The wanted eigenvalue lies to the left
SGNDEF = -ONE; //SGNDEF = -ONE
} else { // ELSE
//*                          The wanted eigenvalue lies to the right
SGNDEF = ONE; //SGNDEF = ONE
} // ENDIF
//*                       We only use the RQCORR if it improves the
//*                       the iterate reasonably.
if ( ( RQCORR*SGNDEF >= ZERO ) && ( LAMBDA + RQCORR <=  RIGHT) && ( LAMBDA + RQCORR >=  LEFT)) { //IF( ( RQCORR*SGNDEF.GE.ZERO ).AND.( LAMBDA + RQCORR.LE. RIGHT).AND.( LAMBDA + RQCORR.GE. LEFT)) THEN
USEDRQ =  true; //USEDRQ = .TRUE.
//*                          Store new midpoint of bisection interval in WORK
if (SGNDEF == ONE) { //IF(SGNDEF.EQ.ONE) THEN
//*                             The current LAMBDA is on the left of the true
//*                             eigenvalue
LEFT = LAMBDA; //LEFT = LAMBDA
//*                             We prefer to assume that the error estimate
//*                             is correct. We could make the interval not
//*                             as a bracket but to be modified if the RQCORR
//*                             chooses to. In this case, the RIGHT side should
//*                             be modified as follows:
//*                              RIGHT = MAX(RIGHT, LAMBDA + RQCORR)
} else { // ELSE
//*                             The current LAMBDA is on the right of the true
//*                             eigenvalue
RIGHT = LAMBDA; //RIGHT = LAMBDA
//*                             See comment about assuming the error estimate is
//*                             correct above.
//*                              LEFT = MIN(LEFT, LAMBDA + RQCORR)
} // ENDIF
WORK( WINDEX ) =HALF * (RIGHT + LEFT); //WORK( WINDEX ) =HALF * (RIGHT + LEFT)
//*                          Take RQCORR since it has the correct sign and
//*                          improves the iterate reasonably
LAMBDA = LAMBDA + RQCORR; //LAMBDA = LAMBDA + RQCORR
//*                          Update width of error interval
WERR( WINDEX ) =HALF * (RIGHT-LEFT); //WERR( WINDEX ) =HALF * (RIGHT-LEFT)
} else { // ELSE
NEEDBS =  true; //NEEDBS = .TRUE.
} // ENDIF
if (RIGHT-LEFT < RQTOL*ABS(LAMBDA)) { //IF(RIGHT-LEFT.LT.RQTOL*ABS(LAMBDA)) THEN
//*                             The eigenvalue is computed to bisection accuracy
//*                             compute eigenvector and stop
USEDBS =  true; //USEDBS = .TRUE.
GOTO 120; //GOTO 120
ELSEIF( ITER < MAXITR ) THEN; //ELSEIF( ITER.LT.MAXITR ) THEN
GOTO 120; //GOTO 120
ELSEIF( ITER == MAXITR ) THEN; //ELSEIF( ITER.EQ.MAXITR ) THEN
NEEDBS =  true; //NEEDBS = .TRUE.
GOTO 120; //GOTO 120
} else { // ELSE
INFO = 5; //INFO = 5
} // RETURN
} // END IF
} else { // ELSE
STP2II =  false; //STP2II = .FALSE.
if (USEDRQ  &&  USEDBS  && BSTRES <= RESID) { //IF(USEDRQ .AND. USEDBS .AND.BSTRES.LE.RESID) THEN
LAMBDA = BSTW; //LAMBDA = BSTW
STP2II =  true; //STP2II = .TRUE.
} // ENDIF
if  (STP2II) { //IF (STP2II) THEN
//*                          improve error angle by second step
CALL DLAR1V( IN, 1, IN, LAMBDA,D( IBEGIN ), L( IBEGIN ),WORK(INDLD+IBEGIN-1),WORK(INDLLD+IBEGIN-1),PIVMIN, GAPTOL, Z( IBEGIN, WINDEX ), !USEDBS, NEGCNT, ZTZ, MINGMA,IWORK( IINDR+WINDEX ),ISUPPZ( 2*WINDEX-1 ),NRMINV, RESID, RQCORR, WORK( INDWRK ) ); //CALL DLAR1V( IN, 1, IN, LAMBDA,D( IBEGIN ), L( IBEGIN ),WORK(INDLD+IBEGIN-1),WORK(INDLLD+IBEGIN-1),PIVMIN, GAPTOL, Z( IBEGIN, WINDEX ),.NOT.USEDBS, NEGCNT, ZTZ, MINGMA,IWORK( IINDR+WINDEX ),ISUPPZ( 2*WINDEX-1 ),NRMINV, RESID, RQCORR, WORK( INDWRK ) )
} // ENDIF
WORK( WINDEX ) = LAMBDA; //WORK( WINDEX ) = LAMBDA
} // END IF
//*
//*                    Compute FP-vector support w.r.t. whole matrix
//*
ISUPPZ( 2*WINDEX-1 ) = ISUPPZ( 2*WINDEX-1 )+OLDIEN; //ISUPPZ( 2*WINDEX-1 ) = ISUPPZ( 2*WINDEX-1 )+OLDIEN
ISUPPZ( 2*WINDEX ) = ISUPPZ( 2*WINDEX )+OLDIEN; //ISUPPZ( 2*WINDEX ) = ISUPPZ( 2*WINDEX )+OLDIEN
ZFROM = ISUPPZ( 2*WINDEX-1 ); //ZFROM = ISUPPZ( 2*WINDEX-1 )
ZTO = ISUPPZ( 2*WINDEX ); //ZTO = ISUPPZ( 2*WINDEX )
ISUPMN = ISUPMN + OLDIEN; //ISUPMN = ISUPMN + OLDIEN
ISUPMX = ISUPMX + OLDIEN; //ISUPMX = ISUPMX + OLDIEN
//*                    Ensure vector is ok if support in the RQI has changed
if (ISUPMN < ZFROM) { //IF(ISUPMN.LT.ZFROM) THEN
for ( 122 II = ISUPMN;ZFROM-1) { //DO 122 II = ISUPMN,ZFROM-1
Z( II, WINDEX ) = ZERO; //Z( II, WINDEX ) = ZERO
122                    CONTINUE; //122                    CONTINUE
} // ENDIF
if (ISUPMX > ZTO) { //IF(ISUPMX.GT.ZTO) THEN
for ( 123 II = ZTO+1;ISUPMX) { //DO 123 II = ZTO+1,ISUPMX
Z( II, WINDEX ) = ZERO; //Z( II, WINDEX ) = ZERO
123                    CONTINUE; //123                    CONTINUE
} // ENDIF
CALL DSCAL( ZTO-ZFROM+1, NRMINV,Z( ZFROM, WINDEX ), 1 ); //CALL DSCAL( ZTO-ZFROM+1, NRMINV,Z( ZFROM, WINDEX ), 1 )
125                 CONTINUE; //125                 CONTINUE
//*                    Update W
W( WINDEX ) = LAMBDA+SIGMA; //W( WINDEX ) = LAMBDA+SIGMA
//*                    Recompute the gaps on the left and right
//*                    But only allow them to become larger and not
//*                    smaller (which can only happen through "bad"
//*                    cancellation and doesn't reflect the theory
//*                    where the initial gaps are underestimated due
//*                    to WERR being too crude.)
if ( !ESKIP) { //IF(.NOT.ESKIP) THEN
if ( K > 1) { //IF( K.GT.1) THEN
WGAP( WINDMN ) = MAX( WGAP(WINDMN),W(WINDEX)-WERR(WINDEX)- W(WINDMN)-WERR(WINDMN) ); //WGAP( WINDMN ) = MAX( WGAP(WINDMN),W(WINDEX)-WERR(WINDEX)- W(WINDMN)-WERR(WINDMN) )
} // ENDIF
if ( WINDEX < WEND ) { //IF( WINDEX.LT.WEND ) THEN
WGAP( WINDEX ) = MAX( SAVGAP,W( WINDPL )-WERR( WINDPL )- W( WINDEX )-WERR( WINDEX) ); //WGAP( WINDEX ) = MAX( SAVGAP,W( WINDPL )-WERR( WINDPL )- W( WINDEX )-WERR( WINDEX) )
} // ENDIF
} // ENDIF
IDONE = IDONE + 1; //IDONE = IDONE + 1
} // ENDIF
//*                 here ends the code for the current child
//*
139              CONTINUE; //139              CONTINUE
//*                 Proceed to any remaining child nodes
NEWFST = J + 1; //NEWFST = J + 1
140           CONTINUE; //140           CONTINUE
150        CONTINUE; //150        CONTINUE
NDEPTH = NDEPTH + 1; //NDEPTH = NDEPTH + 1
GO TO 40; //GO TO 40
} // END IF
IBEGIN = IEND + 1; //IBEGIN = IEND + 1
WBEGIN = WEND + 1; //WBEGIN = WEND + 1
170  CONTINUE; //170  CONTINUE
//*
} // RETURN
//*
//*     End of DLARRV
//*
} // END