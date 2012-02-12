package lapack4j.src.java;
import lapack4j.utils.IntRef;
import static lapack4j.utils.IntrFuncs.*;
import static lapack4j.utils.MathConsts.*;
import static lapack4j.src.java.DLANEG.DLANEG;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,24/10/11,2:38 PM
 */
public class DLARRB { //SUBROUTINE DLARRB( N, D, LLD, IFIRST, ILAST, RTOL1,RTOL2, OFFSET, W, WGAP, WERR, WORK, IWORK,PIVMIN, SPDIAM, TWIST, INFO )
public static void DLARRB(
  final int N  //N       (input) INTEGER
  , final double[] D //D       (input) DOUBLE PRECISION array, dimension (N)
  , final double[] LLD  //LLD     (input) DOUBLE PRECISION array, dimension (N-1)
  , final int IFIRST  //IFIRST  (input) INTEGER
  , final int ILAST  //ILAST   (input) INTEGER
  , final double RTOL1  //RTOL1   (input) DOUBLE PRECISION
  , final double RTOL2  //RTOL2   (input) DOUBLE PRECISION
  , final int OFFSET  //OFFSET  (input) INTEGER
  , final double[] W  //W       (input/output) DOUBLE PRECISION array, dimension (N)
  , final double[] WGAP  //WGAP    (input/output) DOUBLE PRECISION array, dimension (N-1)
  , final double[] WERR  //WERR    (input/output) DOUBLE PRECISION array, dimension (N)
  , final double[] WORK  //WORK    (workspace) DOUBLE PRECISION array, dimension (2*N)
  , final int[] IWORK   //IWORK   (workspace) INTEGER array, dimension (2*N)
  , final double PIVMIN  //PIVMIN  (input) DOUBLE PRECISION
  , final double SPDIAM  //SPDIAM  (input) DOUBLE PRECISION
  , final int TWIST  //TWIST   (input) INTEGER
  , final int[] INFO  //INFO    (output) INTEGER
) { //SUBROUTINE DLARRB( N, D, LLD, IFIRST, ILAST, RTOL1,RTOL2, OFFSET, W, WGAP, WERR, WORK, IWORK,PIVMIN, SPDIAM, TWIST, INFO )
  //*
  //*  -- LAPACK auxiliary routine (version 3.2) --
  //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
  //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
  //*     November 2006
  //*
  //*     .. Scalar Arguments ..
  INFO[0] = 0; //INTEGER            IFIRST, ILAST, INFO, N, OFFSET, TWIST
//  double PIVMIN, RTOL1, RTOL2, SPDIAM; //DOUBLE PRECISION   PIVMIN, RTOL1, RTOL2, SPDIAM
  //*     ..
  //*     .. Array Arguments ..
//  int IWORK[]; //INTEGER            IWORK( * )
//  double D[], LLD[], W[],WERR[], WGAP[], WORK[]; //DOUBLE PRECISION   D( * ), LLD( * ), W( * ),WERR( * ), WGAP( * ), WORK( * )
  //*     ..
  //*
  //*  Purpose
  //*  =======
  //*
  //*  Given the relatively robust representation(RRR) L D L^T, DLARRB
  //*  does "limited" bisection to refine the eigenvalues of L D L^T,
  //*  W( IFIRST-OFFSET ) through W( ILAST-OFFSET ), to more accuracy. Initial
  //*  guesses for these eigenvalues are input in W, the corresponding estimate
  //*  of the error in these guesses and their gaps are input in WERR
  //*  and WGAP, respectively. During bisection, intervals
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
  //*          The N diagonal elements of the diagonal matrix D.
  //*
  //*  LLD     (input) DOUBLE PRECISION array, dimension (N-1)
  //*          The (N-1) elements L(i)*L(i)*D(i).
  //*
  //*  IFIRST  (input) INTEGER
  //*          The index of the first eigenvalue to be computed.
  //*
  //*  ILAST   (input) INTEGER
  //*          The index of the last eigenvalue to be computed.
  //*
  //*  RTOL1   (input) DOUBLE PRECISION
  //*  RTOL2   (input) DOUBLE PRECISION
  //*          Tolerance for the convergence of the bisection intervals.
  //*          An interval [LEFT,RIGHT] has converged if
  //*          RIGHT-LEFT.LT.MAX( RTOL1*GAP, RTOL2*MAX(|LEFT|,|RIGHT|) )
  //*          where GAP is the (estimated) distance to the nearest
  //*          eigenvalue.
  //*
  //*  OFFSET  (input) INTEGER
  //*          Offset for the arrays W, WGAP and WERR, i.e., the IFIRST-OFFSET
  //*          through ILAST-OFFSET elements of these arrays are to be used.
  //*
  //*  W       (input/output) DOUBLE PRECISION array, dimension (N)
  //*          On input, W( IFIRST-OFFSET ) through W( ILAST-OFFSET ) are
  //*          estimates of the eigenvalues of L D L^T indexed IFIRST throug
  //*          ILAST.
  //*          On output, these estimates are refined.
  //*
  //*  WGAP    (input/output) DOUBLE PRECISION array, dimension (N-1)
  //*          On input, the (estimated) gaps between consecutive
  //*          eigenvalues of L D L^T, i.e., WGAP(I-OFFSET) is the gap between
  //*          eigenvalues I and I+1. Note that if IFIRST.EQ.ILAST
  //*          then WGAP(IFIRST-OFFSET) must be set to ZERO.
  //*          On output, these gaps are refined.
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
  //*          The minimum pivot in the Sturm sequence.
  //*
  //*  SPDIAM  (input) DOUBLE PRECISION
  //*          The spectral diameter of the matrix.
  //*
  //*  TWIST   (input) INTEGER
  //*          The twist index for the twisted factorization that is used
  //*          for the negcount.
  //*          TWIST = N: Compute negcount from L D L^T - LAMBDA I = L+ D+ L+^T
  //*          TWIST = 1: Compute negcount from L D L^T - LAMBDA I = U- D- U-^T
  //*          TWIST = R: Compute negcount from L D L^T - LAMBDA I = N(r) D(r) N(r)
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
//  double ZERO, TWO, HALF; //DOUBLE PRECISION   ZERO, TWO, HALF
//  {  ZERO = 0.0D; TWO = 2.0D; HALF = 0.5D; } //PARAMETER        ( ZERO = 0.0D0, TWO = 2.0D0,HALF = 0.5D0 )
  int MAXITR; //INTEGER   MAXITR
  //*     ..
  //*     .. Local Scalars ..
  int I, I1, II, IP, ITER, K, NEGCNT, NEXT, NINT,OLNINT, PREV, R; //INTEGER            I, I1, II, IP, ITER, K, NEGCNT, NEXT, NINT,OLNINT, PREV, R
  double BACK, CVRGD, GAP, LEFT, LGAP, MID, MNWDTH,RGAP, RIGHT, TMP, WIDTH; //DOUBLE PRECISION   BACK, CVRGD, GAP, LEFT, LGAP, MID, MNWDTH,RGAP, RIGHT, TMP, WIDTH
  //*     ..
  //*     .. External Functions ..
  int DLANEG; //INTEGER            DLANEG
//    EXTERNAL           DLANEG; //EXTERNAL           DLANEG
  //*
  //*     ..
  //*     .. Intrinsic Functions ..
  //INTRINSIC          ABS, MAX, MIN
  //*     ..
  //*     .. Executable Statements ..
  //*
  INFO[0] = 0; //INFO = 0
  //*
  MAXITR = (int)( ( LOG( SPDIAM+PIVMIN )-LOG( PIVMIN ) ) /LOG( TWO ) ) + 2; //MAXITR = INT( ( LOG( SPDIAM+PIVMIN )-LOG( PIVMIN ) ) /LOG( TWO ) ) + 2
  MNWDTH = TWO * PIVMIN; //MNWDTH = TWO * PIVMIN
  //*
  R = TWIST; //R = TWIST
  if ((R < 1) || (R > N))
    R = N; //IF((R.LT.1).OR.(R.GT.N)) R = N
  //*
  //*     Initialize unconverged intervals in [ WORK(2*I-1), WORK(2*I) ].
  //*     The Sturm Count, Count( WORK(2*I-1) ) is arranged to be I-1, while
  //*     Count( WORK(2*I) ) is stored in IWORK( 2*I ). The integer IWORK( 2*I-1 )
  //*     for an unconverged interval is set to the index of the next unconverged
  //*     interval, and is -1 or 0 for a converged interval. Thus a linked
  //*     list of unconverged intervals is set up.
  //*
  I1 = IFIRST; //I1 = IFIRST
  //*     The number of unconverged intervals
  NINT = 0; //NINT = 0
  //*     The last unconverged interval found
  PREV = 0; //PREV = 0
  RGAP = WGAP[ I1-OFFSET -1]; //RGAP = WGAP( I1-OFFSET )
  for (I = I1; I <= ILAST; I++) { //DO 75 I = I1, ILAST
    K = 2*I; //K = 2*I
    II = I - OFFSET; //II = I - OFFSET
    LEFT = W[ II -1] - WERR[ II -1]; //LEFT = W( II ) - WERR( II )
    RIGHT = W[ II -1] + WERR[ II -1]; //RIGHT = W( II ) + WERR( II )
    LGAP = RGAP; //LGAP = RGAP
    RGAP = WGAP[ II -1]; //RGAP = WGAP( II )
    GAP = MIN( LGAP, RGAP ); //GAP = MIN( LGAP, RGAP )
    //*        Make sure that [LEFT,RIGHT] contains the desired eigenvalue
    //*        Compute negcount from dstqds facto L+D+L+^T = L D L^T - LEFT
    //*
    //*        Do while( NEGCNT(LEFT).GT.I-1 )
    //*
    BACK = WERR[ II -1]; //BACK = WERR( II )
    for (;;) { //20      CONTINUE
      NEGCNT = DLANEG( N, D, LLD, LEFT, PIVMIN, R ); //NEGCNT = DLANEG( N, D, LLD, LEFT, PIVMIN, R )
      if ( NEGCNT > I-1 ) { //IF( NEGCNT.GT.I-1 ) THEN
        LEFT = LEFT - BACK; //LEFT = LEFT - BACK
        BACK = TWO*BACK; //BACK = TWO*BACK
        continue; //GO TO 20
      } // END IF
      break;
    }
    //*
    //*        Do while( NEGCNT(RIGHT).LT.I )
    //*        Compute negcount from dstqds facto L+D+L+^T = L D L^T - RIGHT
    //*
    BACK = WERR[ II -1]; //BACK = WERR( II )
    for (;;) { //50      CONTINUE
      NEGCNT = DLANEG( N, D, LLD, RIGHT, PIVMIN, R ); //NEGCNT = DLANEG( N, D, LLD, RIGHT, PIVMIN, R )
      if ( NEGCNT < I ) { //IF( NEGCNT.LT.I ) THEN
        RIGHT = RIGHT + BACK; //RIGHT = RIGHT + BACK
        BACK = TWO*BACK; //BACK = TWO*BACK
        continue; //GO TO 50
      } // END IF
      break;
    }
    WIDTH = HALF*ABS( LEFT - RIGHT ); //WIDTH = HALF*ABS( LEFT - RIGHT )
    TMP = MAX( ABS( LEFT ), ABS( RIGHT ) ); //TMP = MAX( ABS( LEFT ), ABS( RIGHT ) )
    CVRGD = MAX(RTOL1*GAP,RTOL2*TMP); //CVRGD = MAX(RTOL1*GAP,RTOL2*TMP)
    if ( WIDTH <= CVRGD  ||  WIDTH <= MNWDTH ) { //IF( WIDTH.LE.CVRGD .OR. WIDTH.LE.MNWDTH ) THEN
      //*           This interval has already converged and does not need refinement.
      //*           (Note that the gaps might change through refining the
      //*            eigenvalues, however, they can only get bigger.)
      //*           Remove it from the list.
      IWORK[ K-1 -1] = -1; //IWORK( K-1 ) = -1
      //*           Make sure that I1 always points to the first unconverged interval
      if ((I == I1) && (I < ILAST))
        I1 = I + 1; //IF((I.EQ.I1).AND.(I.LT.ILAST)) I1 = I + 1
      if ((PREV >= I1) && (I <= ILAST))
        IWORK[ 2*PREV-1 -1] = I + 1; //IF((PREV.GE.I1).AND.(I.LE.ILAST)) IWORK( 2*PREV-1 ) = I + 1
    } else { // ELSE
      //*           unconverged interval found
      PREV = I; //PREV = I
      NINT = NINT + 1; //NINT = NINT + 1
      IWORK[ K-1 -1] = I + 1; //IWORK( K-1 ) = I + 1
      IWORK[ K -1] = NEGCNT; //IWORK( K ) = NEGCNT
    } // END IF
    WORK[ K-1 -1] = LEFT; //WORK( K-1 ) = LEFT
    WORK[ K -1] = RIGHT; //WORK( K ) = RIGHT
  } //75   CONTINUE
  //*
  //*     Do while( NINT.GT.0 ), i.e. there are still unconverged intervals
  //*     and while (ITER.LT.MAXITR)
  //*
  ITER = 0; //ITER = 0
  for (;;) { //80   CONTINUE
    PREV = I1 - 1; //PREV = I1 - 1
    I = I1; //I = I1
    OLNINT = NINT; //OLNINT = NINT
    for (IP = 1; IP <= OLNINT; IP++) { //DO 100 IP = 1, OLNINT
      K = 2*I; //K = 2*I
      II = I - OFFSET; //II = I - OFFSET
      RGAP = WGAP[ II -1]; //RGAP = WGAP( II )
      LGAP = RGAP; //LGAP = RGAP
      if (II > 1)
        LGAP = WGAP[ II-1 -1]; //IF(II.GT.1) LGAP = WGAP( II-1 )
      GAP = MIN( LGAP, RGAP ); //GAP = MIN( LGAP, RGAP )
      NEXT = IWORK[ K-1 -1]; //NEXT = IWORK( K-1 )
      LEFT = WORK[ K-1 -1]; //LEFT = WORK( K-1 )
      RIGHT = WORK[ K -1]; //RIGHT = WORK( K )
      MID = HALF*( LEFT + RIGHT ); //MID = HALF*( LEFT + RIGHT )
      //*        semiwidth of interval
      WIDTH = RIGHT - MID; //WIDTH = RIGHT - MID
      TMP = MAX( ABS( LEFT ), ABS( RIGHT ) ); //TMP = MAX( ABS( LEFT ), ABS( RIGHT ) )
      CVRGD = MAX(RTOL1*GAP,RTOL2*TMP); //CVRGD = MAX(RTOL1*GAP,RTOL2*TMP)
      if ( ( WIDTH <= CVRGD )
        ||  ( WIDTH <= MNWDTH ) || ( ITER == MAXITR ) ) { //IF( ( WIDTH.LE.CVRGD ) .OR. ( WIDTH.LE.MNWDTH ).OR.( ITER.EQ.MAXITR ) )THEN
        //*           reduce number of unconverged intervals
        NINT = NINT - 1; //NINT = NINT - 1
        //*           Mark interval as converged.
        IWORK[ K-1 -1] = 0; //IWORK( K-1 ) = 0
        if ( I1 == I ) { //IF( I1.EQ.I ) THEN
          I1 = NEXT; //I1 = NEXT
        } else { // ELSE
          //*              Prev holds the last unconverged interval previously examined
          if (PREV >= I1)
            IWORK[ 2*PREV-1 -1] = NEXT; //IF(PREV.GE.I1) IWORK( 2*PREV-1 ) = NEXT
        } // END IF
        I = NEXT; //I = NEXT
        continue; //GO TO 100
      } // END IF
      PREV = I; //PREV = I
      //*
      //*        Perform one bisection step
      //*
      NEGCNT = DLANEG( N, D, LLD, MID, PIVMIN, R ); //NEGCNT = DLANEG( N, D, LLD, MID, PIVMIN, R )
      if ( NEGCNT <= I-1 ) { //IF( NEGCNT.LE.I-1 ) THEN
        WORK[ K-1 -1] = MID; //WORK( K-1 ) = MID
      } else { // ELSE
        WORK[ K -1] = MID; //WORK( K ) = MID
      } // END IF
      I = NEXT; //I = NEXT
    } //100  CONTINUE
    ITER = ITER + 1; //ITER = ITER + 1
    //*     do another loop if there are still unconverged intervals
    //*     However, in the last iteration, all intervals are accepted
    //*     since this is the best we can do.
    if ( ( NINT > 0 ) && (ITER <= MAXITR) )
      continue; //IF( ( NINT.GT.0 ).AND.(ITER.LE.MAXITR) ) GO TO 80
    break;
  }
  //*
  //*
  //*     At this point, all the intervals have converged
  for (I = IFIRST; I <= ILAST; I++) { //DO 110 I = IFIRST, ILAST
    K = 2*I; //K = 2*I
    II = I - OFFSET; //II = I - OFFSET
    //*        All intervals marked by '0' have been refined.
    if ( IWORK[ K-1 -1] == 0 ) { //IF( IWORK( K-1 ).EQ.0 ) THEN
      W[ II -1] = HALF*( WORK[ K-1 -1]+WORK[ K -1] ); //W( II ) = HALF*( WORK( K-1 )+WORK( K ) )
      WERR[ II -1] = WORK[ K -1] - W[ II -1]; //WERR( II ) = WORK( K ) - W( II )
    } // END IF
  } //110  CONTINUE
  //*
  for (I = IFIRST+1; I <= ILAST; I++) { //DO 111 I = IFIRST+1, ILAST
    K = 2*I; //K = 2*I
    II = I - OFFSET; //II = I - OFFSET
    WGAP[ II-1 -1] = MAX( ZERO, W[II -1] - WERR [II -1] - W[ II-1 -1] - WERR[ II-1 -1]); //WGAP( II-1 ) = MAX( ZERO,W(II) - WERR (II) - W( II-1 ) - WERR( II-1 ))
  } //111  CONTINUE
} // RETURN
//*
//*     End of DLARRB
//*
} // END