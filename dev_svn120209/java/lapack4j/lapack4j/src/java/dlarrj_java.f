public class DLARRJ { //SUBROUTINE DLARRJ( N, D, E2, IFIRST, ILAST,RTOL, OFFSET, W, WERR, WORK, IWORK,PIVMIN, SPDIAM, INFO )
public static void DLARRJ( N, D, E2, IFIRST, ILAST,RTOL, OFFSET, W, WERR, WORK, IWORK,PIVMIN, SPDIAM, INFO ) { //SUBROUTINE DLARRJ( N, D, E2, IFIRST, ILAST,RTOL, OFFSET, W, WERR, WORK, IWORK,PIVMIN, SPDIAM, INFO )
//*
//*  -- LAPACK auxiliary routine (version 3.2.2) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*     June 2010
//*
//*     .. Scalar Arguments ..
int IFIRST, ILAST, INFO, N, OFFSET; //INTEGER            IFIRST, ILAST, INFO, N, OFFSET
double PIVMIN, RTOL, SPDIAM; //DOUBLE PRECISION   PIVMIN, RTOL, SPDIAM
//*     ..
//*     .. Array Arguments ..
int IWORK[]; //INTEGER            IWORK( * )
double D[], E2[], W[],WERR[], WORK[]; //DOUBLE PRECISION   D( * ), E2( * ), W( * ),WERR( * ), WORK( * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  Given the initial eigenvalue approximations of T, DLARRJ
//*  does  bisection to refine the eigenvalues of T,
//*  W( IFIRST-OFFSET ) through W( ILAST-OFFSET ), to more accuracy. Initial
//*  guesses for these eigenvalues are input in W, the corresponding estimate
//*  of the error in these guesses in WERR. During bisection, intervals
//*  [left, right] are maintained by storing their mid-points and
//*  semi-widths in the arrays W and WERR respectively.
//*
//*  Arguments
//*  =========
//*
//*  N       (input) INTEGER
//*          The order of the matrix.
//*
//*  D       (input) DOUBLE PRECISION array, dimension (N)
//*          The N diagonal elements of T.
//*
//*  E2      (input) DOUBLE PRECISION array, dimension (N-1)
//*          The Squares of the (N-1) subdiagonal elements of T.
//*
//*  IFIRST  (input) INTEGER
//*          The index of the first eigenvalue to be computed.
//*
//*  ILAST   (input) INTEGER
//*          The index of the last eigenvalue to be computed.
//*
//*  RTOL    (input) DOUBLE PRECISION
//*          Tolerance for the convergence of the bisection intervals.
//*          An interval [LEFT,RIGHT] has converged if
//*          RIGHT-LEFT.LT.RTOL*MAX(|LEFT|,|RIGHT|).
//*
//*  OFFSET  (input) INTEGER
//*          Offset for the arrays W and WERR, i.e., the IFIRST-OFFSET
//*          through ILAST-OFFSET elements of these arrays are to be used.
//*
//*  W       (input/output) DOUBLE PRECISION array, dimension (N)
//*          On input, W( IFIRST-OFFSET ) through W( ILAST-OFFSET ) are
//*          estimates of the eigenvalues of L D L^T indexed IFIRST through
//*          ILAST.
//*          On output, these estimates are refined.
//*
//*  WERR    (input/output) DOUBLE PRECISION array, dimension (N)
//*          On input, WERR( IFIRST-OFFSET ) through WERR( ILAST-OFFSET ) are
//*          the errors in the estimates of the corresponding elements in W.
//*          On output, these errors are refined.
//*
//*  WORK    (workspace) DOUBLE PRECISION array, dimension (2*N)
//*          Workspace.
//*
//*  IWORK   (workspace) INTEGER array, dimension (2*N)
//*          Workspace.
//*
//*  PIVMIN  (input) DOUBLE PRECISION
//*          The minimum pivot in the Sturm sequence for T.
//*
//*  SPDIAM  (input) DOUBLE PRECISION
//*          The spectral diameter of T.
//*
//*  INFO    (output) INTEGER
//*          Error flag.
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
double ZERO, ONE, TWO, HALF; //DOUBLE PRECISION   ZERO, ONE, TWO, HALF
{  ZERO = 0.0D0, ONE = 1.0D0, TWO = 2.0D0,HALF = 0.5D0 ; } //PARAMETER        ( ZERO = 0.0D0, ONE = 1.0D0, TWO = 2.0D0,HALF = 0.5D0 )
int MAXITR; //INTEGER   MAXITR
//*     ..
//*     .. Local Scalars ..
int CNT, I, I1, I2, II, ITER, J, K, NEXT, NINT,OLNINT, P, PREV, SAVI1; //INTEGER            CNT, I, I1, I2, II, ITER, J, K, NEXT, NINT,OLNINT, P, PREV, SAVI1
double DPLUS, FAC, LEFT, MID, RIGHT, S, TMP, WIDTH; //DOUBLE PRECISION   DPLUS, FAC, LEFT, MID, RIGHT, S, TMP, WIDTH
//*
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC          ABS, MAX
//*     ..
//*     .. Executable Statements ..
//*
INFO = 0; //INFO = 0
//*
MAXITR = INT( ( LOG( SPDIAM+PIVMIN )-LOG( PIVMIN ) ) /LOG( TWO ) ) + 2; //MAXITR = INT( ( LOG( SPDIAM+PIVMIN )-LOG( PIVMIN ) ) /LOG( TWO ) ) + 2
//*
//*     Initialize unconverged intervals in [ WORK(2*I-1), WORK(2*I) ].
//*     The Sturm Count, Count( WORK(2*I-1) ) is arranged to be I-1, while
//*     Count( WORK(2*I) ) is stored in IWORK( 2*I ). The integer IWORK( 2*I-1 )
//*     for an unconverged interval is set to the index of the next unconverged
//*     interval, and is -1 or 0 for a converged interval. Thus a linked
//*     list of unconverged intervals is set up.
//*
I1 = IFIRST; //I1 = IFIRST
I2 = ILAST; //I2 = ILAST
//*     The number of unconverged intervals
NINT = 0; //NINT = 0
//*     The last unconverged interval found
PREV = 0; //PREV = 0
for ( 75 I = I1; I2) { //DO 75 I = I1, I2
K = 2*I; //K = 2*I
II = I - OFFSET; //II = I - OFFSET
LEFT = W( II ) - WERR( II ); //LEFT = W( II ) - WERR( II )
MID = W(II); //MID = W(II)
RIGHT = W( II ) + WERR( II ); //RIGHT = W( II ) + WERR( II )
WIDTH = RIGHT - MID; //WIDTH = RIGHT - MID
TMP = MAX( ABS( LEFT ), ABS( RIGHT ) ); //TMP = MAX( ABS( LEFT ), ABS( RIGHT ) )
//*        The following test prevents the test of converged intervals
if ( WIDTH < RTOL*TMP ) { //IF( WIDTH.LT.RTOL*TMP ) THEN
//*           This interval has already converged and does not need refinement.
//*           (Note that the gaps might change through refining the
//*            eigenvalues, however, they can only get bigger.)
//*           Remove it from the list.
IWORK( K-1 ) = -1; //IWORK( K-1 ) = -1
//*           Make sure that I1 always points to the first unconverged interval
if ((I == I1) && (I < I2)) I1 = I + 1; //IF((I.EQ.I1).AND.(I.LT.I2)) I1 = I + 1
if ((PREV >= I1) && (I <= I2)) IWORK( 2*PREV-1 ) = I + 1; //IF((PREV.GE.I1).AND.(I.LE.I2)) IWORK( 2*PREV-1 ) = I + 1
} else { // ELSE
//*           unconverged interval found
PREV = I; //PREV = I
//*           Make sure that [LEFT,RIGHT] contains the desired eigenvalue
//*
//*           Do while( CNT(LEFT).GT.I-1 )
//*
FAC = ONE; //FAC = ONE
20         CONTINUE; //20         CONTINUE
CNT = 0; //CNT = 0
S = LEFT; //S = LEFT
DPLUS = D( 1 ) - S; //DPLUS = D( 1 ) - S
if ( DPLUS < ZERO ) CNT = CNT + 1; //IF( DPLUS.LT.ZERO ) CNT = CNT + 1
for ( 30 J = 2; N) { //DO 30 J = 2, N
DPLUS = D( J ) - S - E2( J-1 )/DPLUS; //DPLUS = D( J ) - S - E2( J-1 )/DPLUS
if ( DPLUS < ZERO ) CNT = CNT + 1; //IF( DPLUS.LT.ZERO ) CNT = CNT + 1
30         CONTINUE; //30         CONTINUE
if ( CNT > I-1 ) { //IF( CNT.GT.I-1 ) THEN
LEFT = LEFT - WERR( II )*FAC; //LEFT = LEFT - WERR( II )*FAC
FAC = TWO*FAC; //FAC = TWO*FAC
GO TO 20; //GO TO 20
} // END IF
//*
//*           Do while( CNT(RIGHT).LT.I )
//*
FAC = ONE; //FAC = ONE
50         CONTINUE; //50         CONTINUE
CNT = 0; //CNT = 0
S = RIGHT; //S = RIGHT
DPLUS = D( 1 ) - S; //DPLUS = D( 1 ) - S
if ( DPLUS < ZERO ) CNT = CNT + 1; //IF( DPLUS.LT.ZERO ) CNT = CNT + 1
for ( 60 J = 2; N) { //DO 60 J = 2, N
DPLUS = D( J ) - S - E2( J-1 )/DPLUS; //DPLUS = D( J ) - S - E2( J-1 )/DPLUS
if ( DPLUS < ZERO ) CNT = CNT + 1; //IF( DPLUS.LT.ZERO ) CNT = CNT + 1
60         CONTINUE; //60         CONTINUE
if ( CNT < I ) { //IF( CNT.LT.I ) THEN
RIGHT = RIGHT + WERR( II )*FAC; //RIGHT = RIGHT + WERR( II )*FAC
FAC = TWO*FAC; //FAC = TWO*FAC
GO TO 50; //GO TO 50
} // END IF
NINT = NINT + 1; //NINT = NINT + 1
IWORK( K-1 ) = I + 1; //IWORK( K-1 ) = I + 1
IWORK( K ) = CNT; //IWORK( K ) = CNT
} // END IF
WORK( K-1 ) = LEFT; //WORK( K-1 ) = LEFT
WORK( K ) = RIGHT; //WORK( K ) = RIGHT
75   CONTINUE; //75   CONTINUE
SAVI1 = I1; //SAVI1 = I1
//*
//*     Do while( NINT.GT.0 ), i.e. there are still unconverged intervals
//*     and while (ITER.LT.MAXITR)
//*
ITER = 0; //ITER = 0
80   CONTINUE; //80   CONTINUE
PREV = I1 - 1; //PREV = I1 - 1
I = I1; //I = I1
OLNINT = NINT; //OLNINT = NINT
for ( 100 P = 1; OLNINT) { //DO 100 P = 1, OLNINT
K = 2*I; //K = 2*I
II = I - OFFSET; //II = I - OFFSET
NEXT = IWORK( K-1 ); //NEXT = IWORK( K-1 )
LEFT = WORK( K-1 ); //LEFT = WORK( K-1 )
RIGHT = WORK( K ); //RIGHT = WORK( K )
MID = HALF*( LEFT + RIGHT ); //MID = HALF*( LEFT + RIGHT )
//*        semiwidth of interval
WIDTH = RIGHT - MID; //WIDTH = RIGHT - MID
TMP = MAX( ABS( LEFT ), ABS( RIGHT ) ); //TMP = MAX( ABS( LEFT ), ABS( RIGHT ) )
if ( ( WIDTH < RTOL*TMP )  || (ITER == MAXITR) )THEN; //IF( ( WIDTH.LT.RTOL*TMP ) .OR.(ITER.EQ.MAXITR) )THEN
//*           reduce number of unconverged intervals
NINT = NINT - 1; //NINT = NINT - 1
//*           Mark interval as converged.
IWORK( K-1 ) = 0; //IWORK( K-1 ) = 0
if ( I1 == I ) { //IF( I1.EQ.I ) THEN
I1 = NEXT; //I1 = NEXT
} else { // ELSE
//*              Prev holds the last unconverged interval previously examined
if (PREV >= I1) IWORK( 2*PREV-1 ) = NEXT; //IF(PREV.GE.I1) IWORK( 2*PREV-1 ) = NEXT
} // END IF
I = NEXT; //I = NEXT
GO TO 100; //GO TO 100
} // END IF
PREV = I; //PREV = I
//*
//*        Perform one bisection step
//*
CNT = 0; //CNT = 0
S = MID; //S = MID
DPLUS = D( 1 ) - S; //DPLUS = D( 1 ) - S
if ( DPLUS < ZERO ) CNT = CNT + 1; //IF( DPLUS.LT.ZERO ) CNT = CNT + 1
for ( 90 J = 2; N) { //DO 90 J = 2, N
DPLUS = D( J ) - S - E2( J-1 )/DPLUS; //DPLUS = D( J ) - S - E2( J-1 )/DPLUS
if ( DPLUS < ZERO ) CNT = CNT + 1; //IF( DPLUS.LT.ZERO ) CNT = CNT + 1
90      CONTINUE; //90      CONTINUE
if ( CNT <= I-1 ) { //IF( CNT.LE.I-1 ) THEN
WORK( K-1 ) = MID; //WORK( K-1 ) = MID
} else { // ELSE
WORK( K ) = MID; //WORK( K ) = MID
} // END IF
I = NEXT; //I = NEXT
100  CONTINUE; //100  CONTINUE
ITER = ITER + 1; //ITER = ITER + 1
//*     do another loop if there are still unconverged intervals
//*     However, in the last iteration, all intervals are accepted
//*     since this is the best we can do.
if ( ( NINT > 0 ) && (ITER <= MAXITR) ) GO TO 80; //IF( ( NINT.GT.0 ).AND.(ITER.LE.MAXITR) ) GO TO 80
//*
//*
//*     At this point, all the intervals have converged
for ( 110 I = SAVI1; ILAST) { //DO 110 I = SAVI1, ILAST
K = 2*I; //K = 2*I
II = I - OFFSET; //II = I - OFFSET
//*        All intervals marked by '0' have been refined.
if ( IWORK( K-1 ) == 0 ) { //IF( IWORK( K-1 ).EQ.0 ) THEN
W( II ) = HALF*( WORK( K-1 )+WORK( K ) ); //W( II ) = HALF*( WORK( K-1 )+WORK( K ) )
WERR( II ) = WORK( K ) - W( II ); //WERR( II ) = WORK( K ) - W( II )
} // END IF
110  CONTINUE; //110  CONTINUE
//*
} // RETURN
//*
//*     End of DLARRJ
//*
} // END