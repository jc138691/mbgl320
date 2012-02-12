package lapack4j.src.java;
import lapack4j.utils.DblRef;
import static lapack4j.src.java.DLAMCH.DLAMCH;
import static lapack4j.utils.IntrFuncs.ABS;
import static lapack4j.utils.IntrFuncs.SQRT;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,24/10/11,1:56 PM
 */
public class DLARRR { //SUBROUTINE DLARRR( N, D, E, INFO )
public static void DLARRR(
  final int N //N       (input) INTEGER
  , final double[] D //D       (input) DOUBLE PRECISION array, dimension (N)
  , final double[] E //E       (input/output) DOUBLE PRECISION array, dimension (N)
  , final int[] INFO  //INFO    (output) INTEGER
) { //SUBROUTINE DLARRR( N, D, E, INFO )
  //*
  //*  -- LAPACK auxiliary routine (version 3.2) --
  //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
  //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
  //*     November 2006
  //*
  //*     .. Scalar Arguments ..
  INFO[0] = 0; //INTEGER            N, INFO
  //*     ..
  //*     .. Array Arguments ..
  //    double D[], E[]; //DOUBLE PRECISION   D( * ), E( * )
  //*     ..
  //*
  //*
  //*  Purpose
  //*  =======
  //*
  //*  Perform tests to decide whether the symmetric tridiagonal matrix T
  //*  warrants expensive computations which guarantee high relative accuracy
  //*  in the eigenvalues.
  //*
  //*  Arguments
  //*  =========
  //*
  //*  N       (input) INTEGER
  //*          The order of the matrix. N > 0.
  //*
  //*  D       (input) DOUBLE PRECISION array, dimension (N)
  //*          The N diagonal elements of the tridiagonal matrix T.
  //*
  //*  E       (input/output) DOUBLE PRECISION array, dimension (N)
  //*          On entry, the first (N-1) entries contain the subdiagonal
  //*          elements of the tridiagonal matrix T; E(N) is set to ZERO.
  //*
  //*  INFO    (output) INTEGER
  //*          INFO = 0(default) : the matrix warrants computations preserving
  //*                              relative accuracy.
  //*          INFO = 1          : the matrix warrants computations guaranteeing
  //*                              only absolute accuracy.
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
  double ZERO, RELCOND; //DOUBLE PRECISION   ZERO, RELCOND
  {  ZERO = 0.0D; RELCOND = 0.999D; } //PARAMETER          ( ZERO = 0.0D0,RELCOND = 0.999D0 )
  //*     ..
  //*     .. Local Scalars ..
  int I; //INTEGER            I
  boolean YESREL; //LOGICAL            YESREL
  double EPS, SAFMIN, SMLNUM, RMIN, TMP, TMP2,OFFDIG, OFFDIG2; //DOUBLE PRECISION   EPS, SAFMIN, SMLNUM, RMIN, TMP, TMP2,OFFDIG, OFFDIG2
  //*     ..
  //*     .. External Functions ..
  //    double DLAMCH; //DOUBLE PRECISION   DLAMCH
  //    EXTERNAL           DLAMCH; //EXTERNAL           DLAMCH
  //*     ..
  //*     .. Intrinsic Functions ..
  //INTRINSIC          ABS
  //*     ..
  //*     .. Executable Statements ..
  //*
  //*     As a default, do NOT go for relative-accuracy preserving computations.
  INFO[0] = 1; //INFO = 1
  SAFMIN = DLAMCH( 'S' ); //SAFMIN = DLAMCH( 'Safe minimum' )
  EPS = DLAMCH( 'P' ); //EPS = DLAMCH( 'Precision' )
  SMLNUM = SAFMIN / EPS; //SMLNUM = SAFMIN / EPS
  RMIN = SQRT( SMLNUM ); //RMIN = SQRT( SMLNUM )
  //*     Tests for relative accuracy
  //*
  //*     Test for scaled diagonal dominance
  //*     Scale the diagonal entries to one and check whether the sum of the
  //*     off-diagonals is less than one
  //*
  //*     The sdd relative error bounds have a 1/(1- 2*x) factor in them,
  //*     x = max(OFFDIG + OFFDIG2), so when x is close to 1/2, no relative
  //*     accuracy is promised.  In the notation of the code fragment below,
  //*     1/(1 - (OFFDIG + OFFDIG2)) is the condition number.
  //*     We don't think it is worth going into "sdd mode" unless the relative
  //*     condition number is reasonable, not 1/macheps.
  //*     The threshold should be compatible with other thresholds used in the
  //*     code. We set  OFFDIG + OFFDIG2 <= .999 =: RELCOND, it corresponds
  //*     to losing at most 3 decimal digits: 1 / (1 - (OFFDIG + OFFDIG2)) <= 1000
  //*     instead of the current OFFDIG + OFFDIG2 < 1
  //*
  YESREL = true; //YESREL = .TRUE.
  OFFDIG = ZERO; //OFFDIG = ZERO
  TMP = SQRT(ABS(D[1-1])); //TMP = SQRT(ABS(D(1)))
  if  (TMP < RMIN) YESREL = false; //IF (TMP.LT.RMIN) YESREL = .FALSE.
  if (YESREL) {; //IF(.NOT.YESREL) GOTO 11
    for (I = 2; I <= N; I++) { //DO 10 I = 2, N
      TMP2 = SQRT(ABS(D[I -1])); //TMP2 = SQRT(ABS(D(I)))
      if  (TMP2 < RMIN)
        YESREL = false; //IF (TMP2.LT.RMIN) YESREL = .FALSE.
      if (!YESREL)
        break; //IF(.NOT.YESREL) GOTO 11
      OFFDIG2 = ABS(E[I-1 -1])/(TMP*TMP2); //OFFDIG2 = ABS(E(I-1))/(TMP*TMP2)
      if (OFFDIG+OFFDIG2 >= RELCOND)
        YESREL = false; //IF(OFFDIG+OFFDIG2.GE.RELCOND) YESREL = .FALSE.
      if (!YESREL)
        break; //IF(.NOT.YESREL) GOTO 11
      TMP = TMP2; //TMP = TMP2
      OFFDIG = OFFDIG2; //OFFDIG = OFFDIG2
    } //10   CONTINUE
  }
  //11   CONTINUE
  if ( YESREL ) { //IF( YESREL ) THEN
    INFO[0] = 0;//INFO = 0
    return; // RETURN
  } else { // ELSE
  } // ENDIF
  //*     *** MORE TO BE IMPLEMENTED ***
  //*
  //*
  //*     Test if the lower bidiagonal matrix L from T = L D L^T
  //*     (zero shift facto) is well conditioned
  //*
  //*
  //*     Test if the upper bidiagonal matrix U from T = U D U^T
  //*     (zero shift facto) is well conditioned.
  //*     In this case, the matrix needs to be flipped and, at the end
  //*     of the eigenvector computation, the flip needs to be applied
  //*     to the computed eigenvectors (and the support)
} // RETURN
//*     END OF DLARRR
} // END