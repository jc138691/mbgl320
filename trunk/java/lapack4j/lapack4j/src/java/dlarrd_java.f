public class DLARRD { //SUBROUTINE DLARRD( RANGE, ORDER, N, VL, VU, IL, IU, GERS,RELTOL, D, E, E2, PIVMIN, NSPLIT, ISPLIT,M, W, WERR, WL, WU, IBLOCK, INDEXW,WORK, IWORK, INFO )
public static void DLARRD( RANGE, ORDER, N, VL, VU, IL, IU, GERS,RELTOL, D, E, E2, PIVMIN, NSPLIT, ISPLIT,M, W, WERR, WL, WU, IBLOCK, INDEXW,WORK, IWORK, INFO ) { //SUBROUTINE DLARRD( RANGE, ORDER, N, VL, VU, IL, IU, GERS,RELTOL, D, E, E2, PIVMIN, NSPLIT, ISPLIT,M, W, WERR, WL, WU, IBLOCK, INDEXW,WORK, IWORK, INFO )
//*
//*  -- LAPACK auxiliary routine (version 3.3.0)                        --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*     November 2010
//*
//*     .. Scalar Arguments ..
char ORDER, RANGE; //CHARACTER          ORDER, RANGE
int IL, INFO, IU, M, N, NSPLIT; //INTEGER            IL, INFO, IU, M, N, NSPLIT
double PIVMIN, RELTOL, VL, VU, WL, WU; //DOUBLE PRECISION    PIVMIN, RELTOL, VL, VU, WL, WU
//*     ..
//*     .. Array Arguments ..
int IBLOCK[], INDEXW[],ISPLIT[], IWORK[]; //INTEGER            IBLOCK( * ), INDEXW( * ),ISPLIT( * ), IWORK( * )
double D[], E[], E2[],GERS[], W[], WERR[], WORK[]; //DOUBLE PRECISION   D( * ), E( * ), E2( * ),GERS( * ), W( * ), WERR( * ), WORK( * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  DLARRD computes the eigenvalues of a symmetric tridiagonal
//*  matrix T to suitable accuracy. This is an auxiliary code to be
//*  called from DSTEMR.
//*  The user may ask for all eigenvalues, all eigenvalues
//*  in the half-open interval (VL, VU], or the IL-th through IU-th
//*  eigenvalues.
//*
//*  To avoid overflow, the matrix must be scaled so that its
//*  largest element is no greater than overflow**(1/2) *
//*  underflow**(1/4) in absolute value, and for greatest
//*  accuracy, it should not be much smaller than that.
//*
//*  See W. Kahan "Accurate Eigenvalues of a Symmetric Tridiagonal
//*  Matrix", Report CS41, Computer Science Dept., Stanford
//*  University, July 21, 1966.
//*
//*  Arguments
//*  =========
//*
//*  RANGE   (input) CHARACTER*1
//*          = 'A': ("All")   all eigenvalues will be found.
//*          = 'V': ("Value") all eigenvalues in the half-open interval
//*                           (VL, VU] will be found.
//*          = 'I': ("Index") the IL-th through IU-th eigenvalues (of the
//*                           entire matrix) will be found.
//*
//*  ORDER   (input) CHARACTER*1
//*          = 'B': ("By Block") the eigenvalues will be grouped by
//*                              split-off block (see IBLOCK, ISPLIT) and
//*                              ordered from smallest to largest within
//*                              the block.
//*          = 'E': ("Entire matrix")
//*                              the eigenvalues for the entire matrix
//*                              will be ordered from smallest to
//*                              largest.
//*
//*  N       (input) INTEGER
//*          The order of the tridiagonal matrix T.  N >= 0.
//*
//*  VL      (input) DOUBLE PRECISION
//*  VU      (input) DOUBLE PRECISION
//*          If RANGE='V', the lower and upper bounds of the interval to
//*          be searched for eigenvalues.  Eigenvalues less than or equal
//*          to VL, or greater than VU, will not be returned.  VL < VU.
//*          Not referenced if RANGE = 'A' or 'I'.
//*
//*  IL      (input) INTEGER
//*  IU      (input) INTEGER
//*          If RANGE='I', the indices (in ascending order) of the
//*          smallest and largest eigenvalues to be returned.
//*          1 <= IL <= IU <= N, if N > 0; IL = 1 and IU = 0 if N = 0.
//*          Not referenced if RANGE = 'A' or 'V'.
//*
//*  GERS    (input) DOUBLE PRECISION array, dimension (2*N)
//*          The N Gerschgorin intervals (the i-th Gerschgorin interval
//*          is (GERS(2*i-1), GERS(2*i)).
//*
//*  RELTOL  (input) DOUBLE PRECISION
//*          The minimum relative width of an interval.  When an interval
//*          is narrower than RELTOL times the larger (in
//*          magnitude) endpoint, then it is considered to be
//*          sufficiently small, i.e., converged.  Note: this should
//*          always be at least radix*machine epsilon.
//*
//*  D       (input) DOUBLE PRECISION array, dimension (N)
//*          The n diagonal elements of the tridiagonal matrix T.
//*
//*  E       (input) DOUBLE PRECISION array, dimension (N-1)
//*          The (n-1) off-diagonal elements of the tridiagonal matrix T.
//*
//*  E2      (input) DOUBLE PRECISION array, dimension (N-1)
//*          The (n-1) squared off-diagonal elements of the tridiagonal matrix T.
//*
//*  PIVMIN  (input) DOUBLE PRECISION
//*          The minimum pivot allowed in the Sturm sequence for T.
//*
//*  NSPLIT  (input) INTEGER
//*          The number of diagonal blocks in the matrix T.
//*          1 <= NSPLIT <= N.
//*
//*  ISPLIT  (input) INTEGER array, dimension (N)
//*          The splitting points, at which T breaks up into submatrices.
//*          The first submatrix consists of rows/columns 1 to ISPLIT(1),
//*          the second of rows/columns ISPLIT(1)+1 through ISPLIT(2),
//*          etc., and the NSPLIT-th consists of rows/columns
//*          ISPLIT(NSPLIT-1)+1 through ISPLIT(NSPLIT)=N.
//*          (Only the first NSPLIT elements will actually be used, but
//*          since the user cannot know a priori what value NSPLIT will
//*          have, N words must be reserved for ISPLIT.)
//*
//*  M       (output) INTEGER
//*          The actual number of eigenvalues found. 0 <= M <= N.
//*          (See also the description of INFO=2,3.)
//*
//*  W       (output) DOUBLE PRECISION array, dimension (N)
//*          On exit, the first M elements of W will contain the
//*          eigenvalue approximations. DLARRD computes an interval
//*          I_j = (a_j, b_j] that includes eigenvalue j. The eigenvalue
//*          approximation is given as the interval midpoint
//*          W(j)= ( a_j + b_j)/2. The corresponding error is bounded by
//*          WERR(j) = abs( a_j - b_j)/2
//*
//*  WERR    (output) DOUBLE PRECISION array, dimension (N)
//*          The error bound on the corresponding eigenvalue approximation
//*          in W.
//*
//*  WL      (output) DOUBLE PRECISION
//*  WU      (output) DOUBLE PRECISION
//*          The interval (WL, WU] contains all the wanted eigenvalues.
//*          If RANGE='V', then WL=VL and WU=VU.
//*          If RANGE='A', then WL and WU are the global Gerschgorin bounds
//*                        on the spectrum.
//*          If RANGE='I', then WL and WU are computed by DLAEBZ from the
//*                        index range specified.
//*
//*  IBLOCK  (output) INTEGER array, dimension (N)
//*          At each row/column j where E(j) is zero or small, the
//*          matrix T is considered to split into a block diagonal
//*          matrix.  On exit, if INFO = 0, IBLOCK(i) specifies to which
//*          block (from 1 to the number of blocks) the eigenvalue W(i)
//*          belongs.  (DLARRD may use the remaining N-M elements as
//*          workspace.)
//*
//*  INDEXW  (output) INTEGER array, dimension (N)
//*          The indices of the eigenvalues within each block (submatrix);
//*          for example, INDEXW(i)= j and IBLOCK(i)=k imply that the
//*          i-th eigenvalue W(i) is the j-th eigenvalue in block k.
//*
//*  WORK    (workspace) DOUBLE PRECISION array, dimension (4*N)
//*
//*  IWORK   (workspace) INTEGER array, dimension (3*N)
//*
//*  INFO    (output) INTEGER
//*          = 0:  successful exit
//*          < 0:  if INFO = -i, the i-th argument had an illegal value
//*          > 0:  some or all of the eigenvalues failed to converge or
//*                were not computed:
//*                =1 or 3: Bisection failed to converge for some
//*                        eigenvalues; these eigenvalues are flagged by a
//*                        negative block number.  The effect is that the
//*                        eigenvalues may not be as accurate as the
//*                        absolute and relative tolerances.  This is
//*                        generally caused by unexpectedly inaccurate
//*                        arithmetic.
//*                =2 or 3: RANGE='I' only: Not all of the eigenvalues
//*                        IL:IU were found.
//*                        Effect: M < IU+1-IL
//*                        Cause:  non-monotonic arithmetic, causing the
//*                                Sturm sequence to be non-monotonic.
//*                        Cure:   recalculate, using RANGE='A', and pick
//*                                out eigenvalues IL:IU.  In some cases,
//*                                increasing the PARAMETER "FUDGE" may
//*                                make things work.
//*                = 4:    RANGE='I', and the Gershgorin interval
//*                        initially used was too small.  No eigenvalues
//*                        were computed.
//*                        Probable cause: your machine has sloppy
//*                                        floating-point arithmetic.
//*                        Cure: Increase the PARAMETER "FUDGE",
//*                              recompile, and try again.
//*
//*  Internal Parameters
//*  ===================
//*
//*  FUDGE   DOUBLE PRECISION, default = 2
//*          A "fudge factor" to widen the Gershgorin intervals.  Ideally,
//*          a value of 1 should work, but on machines with sloppy
//*          arithmetic, this needs to be larger.  The default for
//*          publicly released versions should be large enough to handle
//*          the worst machine around.  Note that this has no effect
//*          on accuracy of the solution.
//*
//*  Based on contributions by
//*     W. Kahan, University of California, Berkeley, USA
//*     Beresford Parlett, University of California, Berkeley, USA
//*     Jim Demmel, University of California, Berkeley, USA
//*     Inderjit Dhillon, University of Texas, Austin, USA
//*     Osni Marques, LBNL/NERSC, USA
//*     Christof Voemel, University of California, Berkeley, USA
//*
//*  =====================================================================
//*
//*     .. Parameters ..
double ZERO, ONE, TWO, HALF, FUDGE; //DOUBLE PRECISION   ZERO, ONE, TWO, HALF, FUDGE
{  ZERO = 0.0D0, ONE = 1.0D0,TWO = 2.0D0, HALF = ONE/TWO,FUDGE = TWO ; } //PARAMETER          ( ZERO = 0.0D0, ONE = 1.0D0,TWO = 2.0D0, HALF = ONE/TWO,FUDGE = TWO )
int ALLRNG, VALRNG, INDRNG; //INTEGER   ALLRNG, VALRNG, INDRNG
{  ALLRNG = 1, VALRNG = 2, INDRNG = 3 ; } //PARAMETER ( ALLRNG = 1, VALRNG = 2, INDRNG = 3 )
//*     ..
//*     .. Local Scalars ..
boolean NCNVRG, TOOFEW; //LOGICAL            NCNVRG, TOOFEW
int I, IB, IBEGIN, IDISCL, IDISCU, IE, IEND, IINFO,IM, IN, IOFF, IOUT, IRANGE, ITMAX, ITMP1,ITMP2, IW, IWOFF, J, JBLK, JDISC, JE, JEE, NB,NWL, NWU; //INTEGER            I, IB, IBEGIN, IDISCL, IDISCU, IE, IEND, IINFO,IM, IN, IOFF, IOUT, IRANGE, ITMAX, ITMP1,ITMP2, IW, IWOFF, J, JBLK, JDISC, JE, JEE, NB,NWL, NWU
double ATOLI, EPS, GL, GU, RTOLI, TMP1, TMP2,TNORM, UFLOW, WKILL, WLU, WUL; //DOUBLE PRECISION   ATOLI, EPS, GL, GU, RTOLI, TMP1, TMP2,TNORM, UFLOW, WKILL, WLU, WUL
//*     ..
//*     .. Local Arrays ..
int IDUMMA( 1 ); //INTEGER            IDUMMA( 1 )
//*     ..
//*     .. External Functions ..
boolean LSAME; //LOGICAL            LSAME
int ILAENV; //INTEGER            ILAENV
double DLAMCH; //DOUBLE PRECISION   DLAMCH
EXTERNAL           LSAME, ILAENV, DLAMCH; //EXTERNAL           LSAME, ILAENV, DLAMCH
//*     ..
//*     .. External Subroutines ..
EXTERNAL           DLAEBZ; //EXTERNAL           DLAEBZ
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC          ABS, INT, LOG, MAX, MIN
//*     ..
//*     .. Executable Statements ..
//*
INFO = 0; //INFO = 0
//*
//*     Decode RANGE
//*
if ( LSAME( RANGE, 'A' ) ) { //IF( LSAME( RANGE, 'A' ) ) THEN
IRANGE = ALLRNG; //IRANGE = ALLRNG
} else if ( LSAME( RANGE, 'V' ) ) { //ELSE IF( LSAME( RANGE, 'V' ) ) THEN
IRANGE = VALRNG; //IRANGE = VALRNG
} else if ( LSAME( RANGE, 'I' ) ) { //ELSE IF( LSAME( RANGE, 'I' ) ) THEN
IRANGE = INDRNG; //IRANGE = INDRNG
} else { // ELSE
IRANGE = 0; //IRANGE = 0
} // END IF
//*
//*     Check for Errors
//*
if ( IRANGE <= 0 ) { //IF( IRANGE.LE.0 ) THEN
INFO = -1; //INFO = -1
} else if (  !(LSAME(ORDER,'B') || LSAME(ORDER,'E')) ) { //ELSE IF( .NOT.(LSAME(ORDER,'B').OR.LSAME(ORDER,'E')) ) THEN
INFO = -2; //INFO = -2
} else if ( N < 0 ) { //ELSE IF( N.LT.0 ) THEN
INFO = -3; //INFO = -3
} else if ( IRANGE == VALRNG ) { //ELSE IF( IRANGE.EQ.VALRNG ) THEN
if ( VL >= VU )INFO = -5; //IF( VL.GE.VU )INFO = -5
} else if ( IRANGE == INDRNG  && ( IL < 1  ||  IL > MAX( 1, N ) ) ) { //ELSE IF( IRANGE.EQ.INDRNG .AND.( IL.LT.1 .OR. IL.GT.MAX( 1, N ) ) ) THEN
INFO = -6; //INFO = -6
} else if ( IRANGE == INDRNG  && ( IU < MIN( N, IL )  ||  IU > N ) ) { //ELSE IF( IRANGE.EQ.INDRNG .AND.( IU.LT.MIN( N, IL ) .OR. IU.GT.N ) ) THEN
INFO = -7; //INFO = -7
} // END IF
//*
if ( INFO != 0 ) { //IF( INFO.NE.0 ) THEN
} // RETURN
} // END IF
//*     Initialize error flags
INFO = 0; //INFO = 0
NCNVRG =  false; //NCNVRG = .FALSE.
TOOFEW =  false; //TOOFEW = .FALSE.
//*     Quick return if possible
M = 0; //M = 0
if ( N == 0 ) return; //IF( N.EQ.0 ) RETURN
//*     Simplification:
if ( IRANGE == INDRNG  &&  IL == 1  &&  IU == N ) IRANGE = 1; //IF( IRANGE.EQ.INDRNG .AND. IL.EQ.1 .AND. IU.EQ.N ) IRANGE = 1
//*     Get machine constants
EPS = DLAMCH( 'P' ); //EPS = DLAMCH( 'P' )
UFLOW = DLAMCH( 'U' ); //UFLOW = DLAMCH( 'U' )
//*     Special Case when N=1
//*     Treat case of 1x1 matrix for quick return
if ( N == 1 ) { //IF( N.EQ.1 ) THEN
if ( (IRANGE == ALLRNG) || ((IRANGE == VALRNG) && (D(1) > VL) && (D(1) <= VU)) || ((IRANGE == INDRNG) && (IL == 1) && (IU == 1)) ) { //IF( (IRANGE.EQ.ALLRNG).OR.((IRANGE.EQ.VALRNG).AND.(D(1).GT.VL).AND.(D(1).LE.VU)).OR.((IRANGE.EQ.INDRNG).AND.(IL.EQ.1).AND.(IU.EQ.1)) ) THEN
M = 1; //M = 1
W(1) = D(1); //W(1) = D(1)
//*           The computation error of the eigenvalue is zero
WERR(1) = ZERO; //WERR(1) = ZERO
IBLOCK( 1 ) = 1; //IBLOCK( 1 ) = 1
INDEXW( 1 ) = 1; //INDEXW( 1 ) = 1
} // ENDIF
} // RETURN
} // END IF
//*     NB is the minimum vector length for vector bisection, or 0
//*     if only scalar is to be done.
NB = ILAENV( 1, 'DSTEBZ', ' ', N, -1, -1, -1 ); //NB = ILAENV( 1, 'DSTEBZ', ' ', N, -1, -1, -1 )
if ( NB <= 1 ) NB = 0; //IF( NB.LE.1 ) NB = 0
//*     Find global spectral radius
GL = D(1); //GL = D(1)
GU = D(1); //GU = D(1)
for ( 5 I = 1;N) { //DO 5 I = 1,N
GL =  MIN( GL, GERS( 2*I - 1)); //GL =  MIN( GL, GERS( 2*I - 1))
GU = MAX( GU, GERS(2*I) ); //GU = MAX( GU, GERS(2*I) )
5    CONTINUE; //5    CONTINUE
//*     Compute global Gerschgorin bounds and spectral diameter
TNORM = MAX( ABS( GL ), ABS( GU ) ); //TNORM = MAX( ABS( GL ), ABS( GU ) )
GL = GL - FUDGE*TNORM*EPS*N - FUDGE*TWO*PIVMIN; //GL = GL - FUDGE*TNORM*EPS*N - FUDGE*TWO*PIVMIN
GU = GU + FUDGE*TNORM*EPS*N + FUDGE*TWO*PIVMIN; //GU = GU + FUDGE*TNORM*EPS*N + FUDGE*TWO*PIVMIN
//*     [JAN/28/2009] remove the line below since SPDIAM variable not use
//*     SPDIAM = GU - GL
//*     Input arguments for DLAEBZ:
//*     The relative tolerance.  An interval (a,b] lies within
//*     "relative tolerance" if  b-a < RELTOL*max(|a|,|b|),
RTOLI = RELTOL; //RTOLI = RELTOL
//*     Set the absolute tolerance for interval convergence to zero to force
//*     interval convergence based on relative size of the interval.
//*     This is dangerous because intervals might not converge when RELTOL is
//*     small. But at least a very small number should be selected so that for
//*     strongly graded matrices, the code can get relatively accurate
//*     eigenvalues.
ATOLI = FUDGE*TWO*UFLOW + FUDGE*TWO*PIVMIN; //ATOLI = FUDGE*TWO*UFLOW + FUDGE*TWO*PIVMIN
if ( IRANGE == INDRNG ) { //IF( IRANGE.EQ.INDRNG ) THEN
//*        RANGE='I': Compute an interval containing eigenvalues
//*        IL through IU. The initial interval [GL,GU] from the global
//*        Gerschgorin bounds GL and GU is refined by DLAEBZ.
ITMAX = INT( ( LOG( TNORM+PIVMIN )-LOG( PIVMIN ) ) /LOG( TWO ) ) + 2; //ITMAX = INT( ( LOG( TNORM+PIVMIN )-LOG( PIVMIN ) ) /LOG( TWO ) ) + 2
WORK( N+1 ) = GL; //WORK( N+1 ) = GL
WORK( N+2 ) = GL; //WORK( N+2 ) = GL
WORK( N+3 ) = GU; //WORK( N+3 ) = GU
WORK( N+4 ) = GU; //WORK( N+4 ) = GU
WORK( N+5 ) = GL; //WORK( N+5 ) = GL
WORK( N+6 ) = GU; //WORK( N+6 ) = GU
IWORK( 1 ) = -1; //IWORK( 1 ) = -1
IWORK( 2 ) = -1; //IWORK( 2 ) = -1
IWORK( 3 ) = N + 1; //IWORK( 3 ) = N + 1
IWORK( 4 ) = N + 1; //IWORK( 4 ) = N + 1
IWORK( 5 ) = IL - 1; //IWORK( 5 ) = IL - 1
IWORK( 6 ) = IU; //IWORK( 6 ) = IU
//*
CALL DLAEBZ( 3, ITMAX, N, 2, 2, NB, ATOLI, RTOLI, PIVMIN,D, E, E2, IWORK( 5 ), WORK( N+1 ), WORK( N+5 ), IOUT,IWORK, W, IBLOCK, IINFO ); //CALL DLAEBZ( 3, ITMAX, N, 2, 2, NB, ATOLI, RTOLI, PIVMIN,D, E, E2, IWORK( 5 ), WORK( N+1 ), WORK( N+5 ), IOUT,IWORK, W, IBLOCK, IINFO )
if ( IINFO  !=  0 ) { //IF( IINFO .NE. 0 ) THEN
INFO = IINFO; //INFO = IINFO
} // RETURN
} // END IF
//*        On exit, output intervals may not be ordered by ascending negcount
if ( IWORK( 6 ) == IU ) { //IF( IWORK( 6 ).EQ.IU ) THEN
WL = WORK( N+1 ); //WL = WORK( N+1 )
WLU = WORK( N+3 ); //WLU = WORK( N+3 )
NWL = IWORK( 1 ); //NWL = IWORK( 1 )
WU = WORK( N+4 ); //WU = WORK( N+4 )
WUL = WORK( N+2 ); //WUL = WORK( N+2 )
NWU = IWORK( 4 ); //NWU = IWORK( 4 )
} else { // ELSE
WL = WORK( N+2 ); //WL = WORK( N+2 )
WLU = WORK( N+4 ); //WLU = WORK( N+4 )
NWL = IWORK( 2 ); //NWL = IWORK( 2 )
WU = WORK( N+3 ); //WU = WORK( N+3 )
WUL = WORK( N+1 ); //WUL = WORK( N+1 )
NWU = IWORK( 3 ); //NWU = IWORK( 3 )
} // END IF
//*        On exit, the interval [WL, WLU] contains a value with negcount NWL,
//*        and [WUL, WU] contains a value with negcount NWU.
if ( NWL < 0  ||  NWL >= N  ||  NWU < 1  ||  NWU > N ) { //IF( NWL.LT.0 .OR. NWL.GE.N .OR. NWU.LT.1 .OR. NWU.GT.N ) THEN
INFO = 4; //INFO = 4
} // RETURN
} // END IF
ELSEIF( IRANGE == VALRNG ) THEN; //ELSEIF( IRANGE.EQ.VALRNG ) THEN
WL = VL; //WL = VL
WU = VU; //WU = VU
ELSEIF( IRANGE == ALLRNG ) THEN; //ELSEIF( IRANGE.EQ.ALLRNG ) THEN
WL = GL; //WL = GL
WU = GU; //WU = GU
} // ENDIF
//*     Find Eigenvalues -- Loop Over blocks and recompute NWL and NWU.
//*     NWL accumulates the number of eigenvalues .le. WL,
//*     NWU accumulates the number of eigenvalues .le. WU
M = 0; //M = 0
IEND = 0; //IEND = 0
INFO = 0; //INFO = 0
NWL = 0; //NWL = 0
NWU = 0; //NWU = 0
//*
for ( 70 JBLK = 1; NSPLIT) { //DO 70 JBLK = 1, NSPLIT
IOFF = IEND; //IOFF = IEND
IBEGIN = IOFF + 1; //IBEGIN = IOFF + 1
IEND = ISPLIT( JBLK ); //IEND = ISPLIT( JBLK )
IN = IEND - IOFF; //IN = IEND - IOFF
//*
if ( IN == 1 ) { //IF( IN.EQ.1 ) THEN
//*           1x1 block
if ( WL >= D( IBEGIN )-PIVMIN )NWL = NWL + 1; //IF( WL.GE.D( IBEGIN )-PIVMIN )NWL = NWL + 1
if ( WU >= D( IBEGIN )-PIVMIN )NWU = NWU + 1; //IF( WU.GE.D( IBEGIN )-PIVMIN )NWU = NWU + 1
if ( IRANGE == ALLRNG  || ( WL < D( IBEGIN )-PIVMIN &&  WU >=  D( IBEGIN )-PIVMIN ) ) { //IF( IRANGE.EQ.ALLRNG .OR.( WL.LT.D( IBEGIN )-PIVMIN.AND. WU.GE. D( IBEGIN )-PIVMIN ) ) THEN
M = M + 1; //M = M + 1
W( M ) = D( IBEGIN ); //W( M ) = D( IBEGIN )
WERR(M) = ZERO; //WERR(M) = ZERO
//*              The gap for a single block doesn't matter for the later
//*              algorithm and is assigned an arbitrary large value
IBLOCK( M ) = JBLK; //IBLOCK( M ) = JBLK
INDEXW( M ) = 1; //INDEXW( M ) = 1
} // END IF
//*        Disabled 2x2 case because of a failure on the following matrix
//*        RANGE = 'I', IL = IU = 4
//*          Original Tridiagonal, d = [
//*           -0.150102010615740E+00
//*           -0.849897989384260E+00
//*           -0.128208148052635E-15
//*            0.128257718286320E-15
//*          ];
//*          e = [
//*           -0.357171383266986E+00
//*           -0.180411241501588E-15
//*           -0.175152352710251E-15
//*          ];
//*
//*         ELSE IF( IN.EQ.2 ) THEN
//**           2x2 block
//*            DISC = SQRT( (HALF*(D(IBEGIN)-D(IEND)))**2 + E(IBEGIN)**2 )
//*            TMP1 = HALF*(D(IBEGIN)+D(IEND))
//*            L1 = TMP1 - DISC
//*            IF( WL.GE. L1-PIVMIN )
//*     $         NWL = NWL + 1
//*            IF( WU.GE. L1-PIVMIN )
//*     $         NWU = NWU + 1
//*            IF( IRANGE.EQ.ALLRNG .OR. ( WL.LT.L1-PIVMIN .AND. WU.GE.
//*     $          L1-PIVMIN ) ) THEN
//*               M = M + 1
//*               W( M ) = L1
//**              The uncertainty of eigenvalues of a 2x2 matrix is very small
//*               WERR( M ) = EPS * ABS( W( M ) ) * TWO
//*               IBLOCK( M ) = JBLK
//*               INDEXW( M ) = 1
//*            ENDIF
//*            L2 = TMP1 + DISC
//*            IF( WL.GE. L2-PIVMIN )
//*     $         NWL = NWL + 1
//*            IF( WU.GE. L2-PIVMIN )
//*     $         NWU = NWU + 1
//*            IF( IRANGE.EQ.ALLRNG .OR. ( WL.LT.L2-PIVMIN .AND. WU.GE.
//*     $          L2-PIVMIN ) ) THEN
//*               M = M + 1
//*               W( M ) = L2
//**              The uncertainty of eigenvalues of a 2x2 matrix is very small
//*               WERR( M ) = EPS * ABS( W( M ) ) * TWO
//*               IBLOCK( M ) = JBLK
//*               INDEXW( M ) = 2
//*            ENDIF
} else { // ELSE
//*           General Case - block of size IN >= 2
//*           Compute local Gerschgorin interval and use it as the initial
//*           interval for DLAEBZ
GU = D( IBEGIN ); //GU = D( IBEGIN )
GL = D( IBEGIN ); //GL = D( IBEGIN )
TMP1 = ZERO; //TMP1 = ZERO
for ( 40 J = IBEGIN; IEND) { //DO 40 J = IBEGIN, IEND
GL =  MIN( GL, GERS( 2*J - 1)); //GL =  MIN( GL, GERS( 2*J - 1))
GU = MAX( GU, GERS(2*J) ); //GU = MAX( GU, GERS(2*J) )
40       CONTINUE; //40       CONTINUE
//*           [JAN/28/2009]
//*           change SPDIAM by TNORM in lines 2 and 3 thereafter
//*           line 1: remove computation of SPDIAM (not useful anymore)
//*           SPDIAM = GU - GL
//*           GL = GL - FUDGE*SPDIAM*EPS*IN - FUDGE*PIVMIN
//*           GU = GU + FUDGE*SPDIAM*EPS*IN + FUDGE*PIVMIN
GL = GL - FUDGE*TNORM*EPS*IN - FUDGE*PIVMIN; //GL = GL - FUDGE*TNORM*EPS*IN - FUDGE*PIVMIN
GU = GU + FUDGE*TNORM*EPS*IN + FUDGE*PIVMIN; //GU = GU + FUDGE*TNORM*EPS*IN + FUDGE*PIVMIN
//*
if ( IRANGE > 1 ) { //IF( IRANGE.GT.1 ) THEN
if ( GU < WL ) { //IF( GU.LT.WL ) THEN
//*                 the local block contains none of the wanted eigenvalues
NWL = NWL + IN; //NWL = NWL + IN
NWU = NWU + IN; //NWU = NWU + IN
GO TO 70; //GO TO 70
} // END IF
//*              refine search interval if possible, only range (WL,WU] matters
GL = MAX( GL, WL ); //GL = MAX( GL, WL )
GU = MIN( GU, WU ); //GU = MIN( GU, WU )
if ( GL >= GU )GO TO 70; //IF( GL.GE.GU )GO TO 70
} // END IF
//*           Find negcount of initial interval boundaries GL and GU
WORK( N+1 ) = GL; //WORK( N+1 ) = GL
WORK( N+IN+1 ) = GU; //WORK( N+IN+1 ) = GU
CALL DLAEBZ( 1, 0, IN, IN, 1, NB, ATOLI, RTOLI, PIVMIN,D( IBEGIN ), E( IBEGIN ), E2( IBEGIN ),IDUMMA, WORK( N+1 ), WORK( N+2*IN+1 ), IM,IWORK, W( M+1 ), IBLOCK( M+1 ), IINFO ); //CALL DLAEBZ( 1, 0, IN, IN, 1, NB, ATOLI, RTOLI, PIVMIN,D( IBEGIN ), E( IBEGIN ), E2( IBEGIN ),IDUMMA, WORK( N+1 ), WORK( N+2*IN+1 ), IM,IWORK, W( M+1 ), IBLOCK( M+1 ), IINFO )
if ( IINFO  !=  0 ) { //IF( IINFO .NE. 0 ) THEN
INFO = IINFO; //INFO = IINFO
} // RETURN
} // END IF
//*
NWL = NWL + IWORK( 1 ); //NWL = NWL + IWORK( 1 )
NWU = NWU + IWORK( IN+1 ); //NWU = NWU + IWORK( IN+1 )
IWOFF = M - IWORK( 1 ); //IWOFF = M - IWORK( 1 )
//*           Compute Eigenvalues
ITMAX = INT( ( LOG( GU-GL+PIVMIN )-LOG( PIVMIN ) ) /LOG( TWO ) ) + 2; //ITMAX = INT( ( LOG( GU-GL+PIVMIN )-LOG( PIVMIN ) ) /LOG( TWO ) ) + 2
CALL DLAEBZ( 2, ITMAX, IN, IN, 1, NB, ATOLI, RTOLI, PIVMIN,D( IBEGIN ), E( IBEGIN ), E2( IBEGIN ),IDUMMA, WORK( N+1 ), WORK( N+2*IN+1 ), IOUT,IWORK, W( M+1 ), IBLOCK( M+1 ), IINFO ); //CALL DLAEBZ( 2, ITMAX, IN, IN, 1, NB, ATOLI, RTOLI, PIVMIN,D( IBEGIN ), E( IBEGIN ), E2( IBEGIN ),IDUMMA, WORK( N+1 ), WORK( N+2*IN+1 ), IOUT,IWORK, W( M+1 ), IBLOCK( M+1 ), IINFO )
if ( IINFO  !=  0 ) { //IF( IINFO .NE. 0 ) THEN
INFO = IINFO; //INFO = IINFO
} // RETURN
} // END IF
//*
//*           Copy eigenvalues into W and IBLOCK
//*           Use -JBLK for block number for unconverged eigenvalues.
//*           Loop over the number of output intervals from DLAEBZ
for ( 60 J = 1; IOUT) { //DO 60 J = 1, IOUT
//*              eigenvalue approximation is middle point of interval
TMP1 = HALF*( WORK( J+N )+WORK( J+IN+N ) ); //TMP1 = HALF*( WORK( J+N )+WORK( J+IN+N ) )
//*              semi length of error interval
TMP2 = HALF*ABS( WORK( J+N )-WORK( J+IN+N ) ); //TMP2 = HALF*ABS( WORK( J+N )-WORK( J+IN+N ) )
if ( J > IOUT-IINFO ) { //IF( J.GT.IOUT-IINFO ) THEN
//*                 Flag non-convergence.
NCNVRG =  true; //NCNVRG = .TRUE.
IB = -JBLK; //IB = -JBLK
} else { // ELSE
IB = JBLK; //IB = JBLK
} // END IF
for ( 50 JE = IWORK( J ) + 1 + IWOFF;IWORK( J+IN ) + IWOFF) { //DO 50 JE = IWORK( J ) + 1 + IWOFF,IWORK( J+IN ) + IWOFF
W( JE ) = TMP1; //W( JE ) = TMP1
WERR( JE ) = TMP2; //WERR( JE ) = TMP2
INDEXW( JE ) = JE - IWOFF; //INDEXW( JE ) = JE - IWOFF
IBLOCK( JE ) = IB; //IBLOCK( JE ) = IB
50          CONTINUE; //50          CONTINUE
60       CONTINUE; //60       CONTINUE
//*
M = M + IM; //M = M + IM
} // END IF
70 CONTINUE; //70 CONTINUE
//*     If RANGE='I', then (WL,WU) contains eigenvalues NWL+1,...,NWU
//*     If NWL+1 < IL or NWU > IU, discard extra eigenvalues.
if ( IRANGE == INDRNG ) { //IF( IRANGE.EQ.INDRNG ) THEN
IDISCL = IL - 1 - NWL; //IDISCL = IL - 1 - NWL
IDISCU = NWU - IU; //IDISCU = NWU - IU
//*
if ( IDISCL > 0 ) { //IF( IDISCL.GT.0 ) THEN
IM = 0; //IM = 0
for ( 80 JE = 1; M) { //DO 80 JE = 1, M
//*              Remove some of the smallest eigenvalues from the left so that
//*              at the end IDISCL =0. Move all eigenvalues up to the left.
if ( W( JE ) <= WLU  &&  IDISCL > 0 ) { //IF( W( JE ).LE.WLU .AND. IDISCL.GT.0 ) THEN
IDISCL = IDISCL - 1; //IDISCL = IDISCL - 1
} else { // ELSE
IM = IM + 1; //IM = IM + 1
W( IM ) = W( JE ); //W( IM ) = W( JE )
WERR( IM ) = WERR( JE ); //WERR( IM ) = WERR( JE )
INDEXW( IM ) = INDEXW( JE ); //INDEXW( IM ) = INDEXW( JE )
IBLOCK( IM ) = IBLOCK( JE ); //IBLOCK( IM ) = IBLOCK( JE )
} // END IF
80         CONTINUE; //80         CONTINUE
M = IM; //M = IM
} // END IF
if ( IDISCU > 0 ) { //IF( IDISCU.GT.0 ) THEN
//*           Remove some of the largest eigenvalues from the right so that
//*           at the end IDISCU =0. Move all eigenvalues up to the left.
IM=M+1; //IM=M+1
for ( 81 JE = M; 1; -1) { //DO 81 JE = M, 1, -1
if ( W( JE ) >= WUL  &&  IDISCU > 0 ) { //IF( W( JE ).GE.WUL .AND. IDISCU.GT.0 ) THEN
IDISCU = IDISCU - 1; //IDISCU = IDISCU - 1
} else { // ELSE
IM = IM - 1; //IM = IM - 1
W( IM ) = W( JE ); //W( IM ) = W( JE )
WERR( IM ) = WERR( JE ); //WERR( IM ) = WERR( JE )
INDEXW( IM ) = INDEXW( JE ); //INDEXW( IM ) = INDEXW( JE )
IBLOCK( IM ) = IBLOCK( JE ); //IBLOCK( IM ) = IBLOCK( JE )
} // END IF
81         CONTINUE; //81         CONTINUE
JEE = 0; //JEE = 0
for ( 82 JE = IM; M) { //DO 82 JE = IM, M
JEE = JEE + 1; //JEE = JEE + 1
W( JEE ) = W( JE ); //W( JEE ) = W( JE )
WERR( JEE ) = WERR( JE ); //WERR( JEE ) = WERR( JE )
INDEXW( JEE ) = INDEXW( JE ); //INDEXW( JEE ) = INDEXW( JE )
IBLOCK( JEE ) = IBLOCK( JE ); //IBLOCK( JEE ) = IBLOCK( JE )
82         CONTINUE; //82         CONTINUE
M = M-IM+1; //M = M-IM+1
} // END IF
if ( IDISCL > 0  ||  IDISCU > 0 ) { //IF( IDISCL.GT.0 .OR. IDISCU.GT.0 ) THEN
//*           Code to deal with effects of bad arithmetic. (If N(w) is
//*           monotone non-decreasing, this should never happen.)
//*           Some low eigenvalues to be discarded are not in (WL,WLU],
//*           or high eigenvalues to be discarded are not in (WUL,WU]
//*           so just kill off the smallest IDISCL/largest IDISCU
//*           eigenvalues, by marking the corresponding IBLOCK = 0
if ( IDISCL > 0 ) { //IF( IDISCL.GT.0 ) THEN
WKILL = WU; //WKILL = WU
for ( 100 JDISC = 1; IDISCL) { //DO 100 JDISC = 1, IDISCL
IW = 0; //IW = 0
for ( 90 JE = 1; M) { //DO 90 JE = 1, M
if ( IBLOCK( JE ) != 0  && ( W( JE ) < WKILL  ||  IW == 0 ) ) { //IF( IBLOCK( JE ).NE.0 .AND.( W( JE ).LT.WKILL .OR. IW.EQ.0 ) ) THEN
IW = JE; //IW = JE
WKILL = W( JE ); //WKILL = W( JE )
} // END IF
90               CONTINUE; //90               CONTINUE
IBLOCK( IW ) = 0; //IBLOCK( IW ) = 0
100           CONTINUE; //100           CONTINUE
} // END IF
if ( IDISCU > 0 ) { //IF( IDISCU.GT.0 ) THEN
WKILL = WL; //WKILL = WL
for ( 120 JDISC = 1; IDISCU) { //DO 120 JDISC = 1, IDISCU
IW = 0; //IW = 0
for ( 110 JE = 1; M) { //DO 110 JE = 1, M
if ( IBLOCK( JE ) != 0  && ( W( JE ) >= WKILL  ||  IW == 0 ) ) { //IF( IBLOCK( JE ).NE.0 .AND.( W( JE ).GE.WKILL .OR. IW.EQ.0 ) ) THEN
IW = JE; //IW = JE
WKILL = W( JE ); //WKILL = W( JE )
} // END IF
110              CONTINUE; //110              CONTINUE
IBLOCK( IW ) = 0; //IBLOCK( IW ) = 0
120           CONTINUE; //120           CONTINUE
} // END IF
//*           Now erase all eigenvalues with IBLOCK set to zero
IM = 0; //IM = 0
for ( 130 JE = 1; M) { //DO 130 JE = 1, M
if ( IBLOCK( JE ) != 0 ) { //IF( IBLOCK( JE ).NE.0 ) THEN
IM = IM + 1; //IM = IM + 1
W( IM ) = W( JE ); //W( IM ) = W( JE )
WERR( IM ) = WERR( JE ); //WERR( IM ) = WERR( JE )
INDEXW( IM ) = INDEXW( JE ); //INDEXW( IM ) = INDEXW( JE )
IBLOCK( IM ) = IBLOCK( JE ); //IBLOCK( IM ) = IBLOCK( JE )
} // END IF
130        CONTINUE; //130        CONTINUE
M = IM; //M = IM
} // END IF
if ( IDISCL < 0  ||  IDISCU < 0 ) { //IF( IDISCL.LT.0 .OR. IDISCU.LT.0 ) THEN
TOOFEW =  true; //TOOFEW = .TRUE.
} // END IF
} // END IF
//*
if (( IRANGE == ALLRNG  &&  M != N ) || ( IRANGE == INDRNG  &&  M != IU-IL+1 ) ) { //IF(( IRANGE.EQ.ALLRNG .AND. M.NE.N ).OR.( IRANGE.EQ.INDRNG .AND. M.NE.IU-IL+1 ) ) THEN
TOOFEW =  true; //TOOFEW = .TRUE.
} // END IF
//*     If ORDER='B', do nothing the eigenvalues are already sorted by
//*        block.
//*     If ORDER='E', sort the eigenvalues from smallest to largest
if ( LSAME(ORDER,'E')  &&  NSPLIT > 1 ) { //IF( LSAME(ORDER,'E') .AND. NSPLIT.GT.1 ) THEN
for ( 150 JE = 1; M - 1) { //DO 150 JE = 1, M - 1
IE = 0; //IE = 0
TMP1 = W( JE ); //TMP1 = W( JE )
for ( 140 J = JE + 1; M) { //DO 140 J = JE + 1, M
if ( W( J ) < TMP1 ) { //IF( W( J ).LT.TMP1 ) THEN
IE = J; //IE = J
TMP1 = W( J ); //TMP1 = W( J )
} // END IF
140       CONTINUE; //140       CONTINUE
if ( IE != 0 ) { //IF( IE.NE.0 ) THEN
TMP2 = WERR( IE ); //TMP2 = WERR( IE )
ITMP1 = IBLOCK( IE ); //ITMP1 = IBLOCK( IE )
ITMP2 = INDEXW( IE ); //ITMP2 = INDEXW( IE )
W( IE ) = W( JE ); //W( IE ) = W( JE )
WERR( IE ) = WERR( JE ); //WERR( IE ) = WERR( JE )
IBLOCK( IE ) = IBLOCK( JE ); //IBLOCK( IE ) = IBLOCK( JE )
INDEXW( IE ) = INDEXW( JE ); //INDEXW( IE ) = INDEXW( JE )
W( JE ) = TMP1; //W( JE ) = TMP1
WERR( JE ) = TMP2; //WERR( JE ) = TMP2
IBLOCK( JE ) = ITMP1; //IBLOCK( JE ) = ITMP1
INDEXW( JE ) = ITMP2; //INDEXW( JE ) = ITMP2
} // END IF
150    CONTINUE; //150    CONTINUE
} // END IF
//*
INFO = 0; //INFO = 0
if ( NCNVRG )INFO = INFO + 1; //IF( NCNVRG )INFO = INFO + 1
if ( TOOFEW )INFO = INFO + 2; //IF( TOOFEW )INFO = INFO + 2
} // RETURN
//*
//*     End of DLARRD
//*
} // END