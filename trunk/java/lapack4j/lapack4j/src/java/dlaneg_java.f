public class DLANEG { //INTEGER FUNCTION DLANEG( N, D, LLD, SIGMA, PIVMIN, R )
public static int DLANEG( N, D, LLD, SIGMA, PIVMIN, R ) { //INTEGER FUNCTION DLANEG( N, D, LLD, SIGMA, PIVMIN, R )
//IMPLICIT NONE
//*
//*  -- LAPACK auxiliary routine (version 3.2.2) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*     June 2010
//*
//*     .. Scalar Arguments ..
int N, R; //INTEGER            N, R
double PIVMIN, SIGMA; //DOUBLE PRECISION   PIVMIN, SIGMA
//*     ..
//*     .. Array Arguments ..
double D[], LLD[]; //DOUBLE PRECISION   D( * ), LLD( * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  DLANEG computes the Sturm count, the number of negative pivots
//*  encountered while factoring tridiagonal T - sigma I = L D L^T.
//*  This implementation works directly on the factors without forming
//*  the tridiagonal matrix T.  The Sturm count is also the number of
//*  eigenvalues of T less than sigma.
//*
//*  This routine is called from DLARRB.
//*
//*  The current routine does not use the PIVMIN parameter but rather
//*  requires IEEE-754 propagation of Infinities and NaNs.  This
//*  routine also has no input range restrictions but does require
//*  default exception handling such that x/0 produces Inf when x is
//*  non-zero, and Inf/Inf produces NaN.  For more information, see:
//*
//*    Marques, Riedy, and Voemel, "Benefits of IEEE-754 Features in
//*    Modern Symmetric Tridiagonal Eigensolvers," SIAM Journal on
//*    Scientific Computing, v28, n5, 2006.  DOI 10.1137/050641624
//*    (Tech report version in LAWN 172 with the same title.)
//*
//*  Arguments
//*  =========
//*
//*  N       (input) INTEGER
//*          The order of the matrix.
//*
//*  D       (input) DOUBLE PRECISION array, dimension (N)
//*          The N diagonal elements of the diagonal matrix D.
//*
//*  LLD     (input) DOUBLE PRECISION array, dimension (N-1)
//*          The (N-1) elements L(i)*L(i)*D(i).
//*
//*  SIGMA   (input) DOUBLE PRECISION
//*          Shift amount in T - sigma I = L D L^T.
//*
//*  PIVMIN  (input) DOUBLE PRECISION
//*          The minimum pivot in the Sturm sequence.  May be used
//*          when zero pivots are encountered on non-IEEE-754
//*          architectures.
//*
//*  R       (input) INTEGER
//*          The twist index for the twisted factorization that is used
//*          for the negcount.
//*
//*  Further Details
//*  ===============
//*
//*  Based on contributions by
//*     Osni Marques, LBNL/NERSC, USA
//*     Christof Voemel, University of California, Berkeley, USA
//*     Jason Riedy, University of California, Berkeley, USA
//*
//*  =====================================================================
//*
//*     .. Parameters ..
double ZERO, ONE; //DOUBLE PRECISION   ZERO, ONE
{  ZERO = 0.0D0, ONE = 1.0D0 ; } //PARAMETER        ( ZERO = 0.0D0, ONE = 1.0D0 )
//*     Some architectures propagate Infinities and NaNs very slowly, so
//*     the code computes counts in BLKLEN chunks.  Then a NaN can
//*     propagate at most BLKLEN columns before being detected.  This is
//*     not a general tuning parameter; it needs only to be just large
//*     enough that the overhead is tiny in common cases.
int BLKLEN; //INTEGER BLKLEN
{  BLKLEN = 128 ; } //PARAMETER ( BLKLEN = 128 )
//*     ..
//*     .. Local Scalars ..
int BJ, J, NEG1, NEG2, NEGCNT; //INTEGER            BJ, J, NEG1, NEG2, NEGCNT
double BSAV, DMINUS, DPLUS, GAMMA, P, T, TMP; //DOUBLE PRECISION   BSAV, DMINUS, DPLUS, GAMMA, P, T, TMP
boolean SAWNAN; //LOGICAL SAWNAN
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC MIN, MAX
//*     ..
//*     .. External Functions ..
boolean DISNAN; //LOGICAL DISNAN
EXTERNAL DISNAN; //EXTERNAL DISNAN
//*     ..
//*     .. Executable Statements ..
NEGCNT = 0; //NEGCNT = 0
//*     I) upper part: L D L^T - SIGMA I = L+ D+ L+^T
T = -SIGMA; //T = -SIGMA
for ( 210 BJ = 1; R-1; BLKLEN) { //DO 210 BJ = 1, R-1, BLKLEN
NEG1 = 0; //NEG1 = 0
BSAV = T; //BSAV = T
for ( 21 J = BJ; MIN(BJ+BLKLEN-1; R-1)) { //DO 21 J = BJ, MIN(BJ+BLKLEN-1, R-1)
DPLUS = D( J ) + T; //DPLUS = D( J ) + T
if ( DPLUS < ZERO ) NEG1 = NEG1 + 1; //IF( DPLUS.LT.ZERO ) NEG1 = NEG1 + 1
TMP = T / DPLUS; //TMP = T / DPLUS
T = TMP * LLD( J ) - SIGMA; //T = TMP * LLD( J ) - SIGMA
21      CONTINUE; //21      CONTINUE
SAWNAN = DISNAN( T ); //SAWNAN = DISNAN( T )
//*     Run a slower version of the above loop if a NaN is detected.
//*     A NaN should occur only with a zero pivot after an infinite
//*     pivot.  In that case, substituting 1 for T/DPLUS is the
//*     correct limit.
if ( SAWNAN ) { //IF( SAWNAN ) THEN
NEG1 = 0; //NEG1 = 0
T = BSAV; //T = BSAV
for ( 22 J = BJ; MIN(BJ+BLKLEN-1; R-1)) { //DO 22 J = BJ, MIN(BJ+BLKLEN-1, R-1)
DPLUS = D( J ) + T; //DPLUS = D( J ) + T
if ( DPLUS < ZERO ) NEG1 = NEG1 + 1; //IF( DPLUS.LT.ZERO ) NEG1 = NEG1 + 1
TMP = T / DPLUS; //TMP = T / DPLUS
if  (DISNAN(TMP)) TMP = ONE; //IF (DISNAN(TMP)) TMP = ONE
T = TMP * LLD(J) - SIGMA; //T = TMP * LLD(J) - SIGMA
22         CONTINUE; //22         CONTINUE
} // END IF
NEGCNT = NEGCNT + NEG1; //NEGCNT = NEGCNT + NEG1
210  CONTINUE; //210  CONTINUE
//*
//*     II) lower part: L D L^T - SIGMA I = U- D- U-^T
P = D( N ) - SIGMA; //P = D( N ) - SIGMA
for ( 230 BJ = N-1; R; -BLKLEN) { //DO 230 BJ = N-1, R, -BLKLEN
NEG2 = 0; //NEG2 = 0
BSAV = P; //BSAV = P
for ( 23 J = BJ; MAX(BJ-BLKLEN+1; R); -1) { //DO 23 J = BJ, MAX(BJ-BLKLEN+1, R), -1
DMINUS = LLD( J ) + P; //DMINUS = LLD( J ) + P
if ( DMINUS < ZERO ) NEG2 = NEG2 + 1; //IF( DMINUS.LT.ZERO ) NEG2 = NEG2 + 1
TMP = P / DMINUS; //TMP = P / DMINUS
P = TMP * D( J ) - SIGMA; //P = TMP * D( J ) - SIGMA
23      CONTINUE; //23      CONTINUE
SAWNAN = DISNAN( P ); //SAWNAN = DISNAN( P )
//*     As above, run a slower version that substitutes 1 for Inf/Inf.
//*
if ( SAWNAN ) { //IF( SAWNAN ) THEN
NEG2 = 0; //NEG2 = 0
P = BSAV; //P = BSAV
for ( 24 J = BJ; MAX(BJ-BLKLEN+1; R); -1) { //DO 24 J = BJ, MAX(BJ-BLKLEN+1, R), -1
DMINUS = LLD( J ) + P; //DMINUS = LLD( J ) + P
if ( DMINUS < ZERO ) NEG2 = NEG2 + 1; //IF( DMINUS.LT.ZERO ) NEG2 = NEG2 + 1
TMP = P / DMINUS; //TMP = P / DMINUS
if  (DISNAN(TMP)) TMP = ONE; //IF (DISNAN(TMP)) TMP = ONE
P = TMP * D(J) - SIGMA; //P = TMP * D(J) - SIGMA
24         CONTINUE; //24         CONTINUE
} // END IF
NEGCNT = NEGCNT + NEG2; //NEGCNT = NEGCNT + NEG2
230  CONTINUE; //230  CONTINUE
//*
//*     III) Twist index
//*       T was shifted by SIGMA initially.
GAMMA = (T + SIGMA) + P; //GAMMA = (T + SIGMA) + P
if ( GAMMA < ZERO ) NEGCNT = NEGCNT+1; //IF( GAMMA.LT.ZERO ) NEGCNT = NEGCNT+1
DLANEG = NEGCNT; //DLANEG = NEGCNT
} // END