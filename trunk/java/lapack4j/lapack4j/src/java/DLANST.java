package lapack4j.src.java;
import lapack4j.utils.DblRef;

import static lapack4j.utils.IntrFuncs.ABS;
import static lapack4j.utils.IntrFuncs.MAX;
import static lapack4j.utils.IntrFuncs.SQRT;
import static lapack4j.src.java.LSAME.LSAME;
import static lapack4j.src.java.DLASSQ.DLASSQ;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/10/11,4:31 PM
 */
//TODO:
public class DLANST { //DOUBLE PRECISION FUNCTION DLANST( NORM, N, D, E )
  public static double DLANST(char NORM, int N, double[] D, double[] E) { //DOUBLE PRECISION FUNCTION DLANST( NORM, N, D, E )
    //public class DLANST { //DOUBLE PRECISION FUNCTION DLANST( NORM, N, D, E )
    //public static double DLANST( NORM, N, D, E ) { //DOUBLE PRECISION FUNCTION DLANST( NORM, N, D, E )
    //*
    //*  -- LAPACK auxiliary routine (version 3.2) --
    //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
    //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
    //*     November 2006
    //*
    //*     .. Scalar Arguments ..
    //char NORM; //CHARACTER          NORM
    //int N; //INTEGER            N
    //*     ..
    //*     .. Array Arguments ..
    //double D[], E[]; //DOUBLE PRECISION   D( * ), E( * )
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  DLANST  returns the value of the one norm,  or the Frobenius norm, or
    //*  the  infinity norm,  or the  element of  largest absolute value  of a
    //*  real symmetric tridiagonal matrix A.
    //*
    //*  Description
    //*  ===========
    //*
    //*  DLANST returns the value
    //*
    //*     DLANST = ( max(abs(A(i,j))), NORM = 'M' or 'm'
    //*              (
    //*              ( norm1(A),         NORM = '1', 'O' or 'o'
    //*              (
    //*              ( normI(A),         NORM = 'I' or 'i'
    //*              (
    //*              ( normF(A),         NORM = 'F', 'f', 'E' or 'e'
    //*
    //*  where  norm1  denotes the  one norm of a matrix (maximum column sum),
    //*  normI  denotes the  infinity norm  of a matrix  (maximum row sum) and
    //*  normF  denotes the  Frobenius norm of a matrix (square root of sum of
    //*  squares).  Note that  max(abs(A(i,j)))  is not a consistent matrix norm.
    //*
    //*  Arguments
    //*  =========
    //*
    //*  NORM    (input) CHARACTER*1
    //*          Specifies the value to be returned in DLANST as described
    //*          above.
    //*
    //*  N       (input) INTEGER
    //*          The order of the matrix A.  N >= 0.  When N = 0, DLANST is
    //*          set to zero.
    //*
    //*  D       (input) DOUBLE PRECISION array, dimension (N)
    //*          The diagonal elements of A.
    //*
    //*  E       (input) DOUBLE PRECISION array, dimension (N-1)
    //*          The (n-1) sub-diagonal or super-diagonal elements of A.
    //*
    //*  =====================================================================
    //*
    //*     .. Parameters ..
    double ONE, ZERO; //DOUBLE PRECISION   ONE, ZERO
    {
      ONE = 1.0D;
      ZERO = 0.0D;
    } //PARAMETER          ( ONE = 1.0D+0, ZERO = 0.0D+0 )
    //*     ..
    //*     .. Local Scalars ..
    int I; //INTEGER            I
    double ANORM = 0d, SCALE, SUM; //DOUBLE PRECISION   ANORM, SCALE, SUM
    DblRef pSCALE = new DblRef();
    DblRef pSUM = new DblRef();
    //*     ..
    //*     .. External Functions ..
    //    boolean LSAME; //LOGICAL            LSAME
    //    EXTERNAL           LSAME; //EXTERNAL           LSAME
    //*     ..
    //*     .. External Subroutines ..
    //    EXTERNAL           DLASSQ; //EXTERNAL           DLASSQ
    //*     ..
    //*     .. Intrinsic Functions ..
    //INTRINSIC          ABS, MAX, SQRT
    //*     ..
    //*     .. Executable Statements ..
    //*
    if (N <= 0) { //IF( N.LE.0 ) THEN
      ANORM = ZERO; //ANORM = ZERO
    } else if (LSAME(NORM, 'M')) { //ELSE IF( LSAME( NORM, 'M' ) ) THEN
      //*
      //*        Find max(abs(A(i,j))).
      //*
      ANORM = ABS(D[N-1]); //ANORM = ABS( D( N ) )
      for (I = 0; I < N - 1; I++) { //DO 10 I = 1, N - 1
        ANORM = MAX(ANORM, ABS(D[I])); //ANORM = MAX( ANORM, ABS( D( I ) ) )
        ANORM = MAX(ANORM, ABS(E[I])); //ANORM = MAX( ANORM, ABS( E( I ) ) )
      } //10    CONTINUE
    } else if (LSAME(NORM, 'O') || NORM == '1' || LSAME(NORM, 'I')) { //ELSE IF( LSAME( NORM, 'O' ) .OR. NORM.EQ.'1' .OR.LSAME( NORM, 'I' ) ) THEN
      //*
      //*        Find norm1(A).
      //*
      if (N == 1) { //IF( N.EQ.1 ) THEN
        ANORM = ABS(D[0]); //ANORM = ABS( D( 1 ) )
      } else { // ELSE
        ANORM = MAX(ABS(D[0]) + ABS(E[0]), ABS(E[N - 2]) + ABS(D[N-1])); //ANORM = MAX( ABS( D( 1 ) )+ABS( E( 1 ) ),ABS( E( N-1 ) )+ABS( D( N ) ) )
        for (I = 1; I < N - 1; I++) { //DO 20 I = 2, N - 1
          ANORM = MAX(ANORM, ABS(D[I]) + ABS(E[I]) + ABS(E[I - 1])); //ANORM = MAX( ANORM, ABS( D( I ) )+ABS( E( I ) )+ABS( E( I-1 ) ) )
        } //20       CONTINUE
      } // END IF
    } else if ((LSAME(NORM, 'F')) || (LSAME(NORM, 'E'))) { //ELSE IF( ( LSAME( NORM, 'F' ) ) .OR. ( LSAME( NORM, 'E' ) ) ) THEN
      //*
      //*        Find normF(A).
      //*
      SCALE = ZERO; //SCALE = ZERO
      SUM = ONE; //SUM = ONE
      if (N > 1) { //IF( N.GT.1 ) THEN
        //CALL DLASSQ( N-1, E, 1, SCALE, SUM )
        pSCALE.set(SCALE);
        pSUM.set(SUM);
        DLASSQ(N - 1, E, 1, pSCALE, pSUM);
        SCALE = pSCALE.get();
        SUM = pSUM.get();
        // end of //CALL DLASSQ( N-1, E, 1, SCALE, SUM )
        SUM = 2 * SUM; //SUM = 2*SUM
      } // END IF
      //CALL DLASSQ( N, D, 1, SCALE, SUM )
      pSCALE.set(SCALE);
      pSUM.set(SUM);
      DLASSQ(N, D, 1, pSCALE, pSUM);
      SCALE = pSCALE.get();
      SUM = pSUM.get();
      // end of //CALL DLASSQ( N-1, E, 1, SCALE, SUM )
      ANORM = SCALE * SQRT(SUM); //ANORM = SCALE*SQRT( SUM )
    } // END IF
    //*
    double DLANST = ANORM; //DLANST = ANORM
    return DLANST;
  } // RETURN
  //*
  //*     End of DLANST
  //*
} // END