package lapack4j.src.java;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,18/10/11,1:44 PM
 */
public class DLAE2 { //SUBROUTINE DLAE2( A, B, C, RT1, RT2 )
  public static void DLAE2(double A, double B, double C, double RT1, double RT2 ) { //SUBROUTINE DLAE2( A, B, C, RT1, RT2 )
    //public class DLAE2 { //SUBROUTINE DLAE2( A, B, C, RT1, RT2 )
    //public static void DLAE2( A, B, C, RT1, RT2 ) { //SUBROUTINE DLAE2( A, B, C, RT1, RT2 )
    //*
    //*  -- LAPACK auxiliary routine (version 3.2) --
    //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
    //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
    //*     November 2006
    //*
    //*     .. Scalar Arguments ..
    //double A, B, C, RT1, RT2; //DOUBLE PRECISION   A, B, C, RT1, RT2
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  DLAE2  computes the eigenvalues of a 2-by-2 symmetric matrix
    //*     [  A   B  ]
    //*     [  B   C  ].
    //*  On return, RT1 is the eigenvalue of larger absolute value, and RT2
    //*  is the eigenvalue of smaller absolute value.
    //*
    //*  Arguments
    //*  =========
    //*
    //*  A       (input) DOUBLE PRECISION
    //*          The (1,1) element of the 2-by-2 matrix.
    //*
    //*  B       (input) DOUBLE PRECISION
    //*          The (1,2) and (2,1) elements of the 2-by-2 matrix.
    //*
    //*  C       (input) DOUBLE PRECISION
    //*          The (2,2) element of the 2-by-2 matrix.
    //*
    //*  RT1     (output) DOUBLE PRECISION
    //*          The eigenvalue of larger absolute value.
    //*
    //*  RT2     (output) DOUBLE PRECISION
    //*          The eigenvalue of smaller absolute value.
    //*
    //*  Further Details
    //*  ===============
    //*
    //*  RT1 is accurate to a few ulps barring over/underflow.
    //*
    //*  RT2 may be inaccurate if there is massive cancellation in the
    //*  determinant A*C-B*B; higher precision or correctly rounded or
    //*  correctly truncated arithmetic would be needed to compute RT2
    //*  accurately in all cases.
    //*
    //*  Overflow is possible only if RT1 is within a factor of 5 of overflow.
    //*  Underflow is harmless if the input data is 0 or exceeds
    //*     underflow_threshold / macheps.
    //*
    //* =====================================================================
    //*
    //*     .. Parameters ..
    double ONE; //DOUBLE PRECISION   ONE
    {  ONE = 1.0D ; } //PARAMETER          ( ONE = 1.0D0 )
    double TWO; //DOUBLE PRECISION   TWO
    {  TWO = 2.0D ; } //PARAMETER          ( TWO = 2.0D0 )
    double ZERO; //DOUBLE PRECISION   ZERO
    {  ZERO = 0.0D ; } //PARAMETER          ( ZERO = 0.0D0 )
    double HALF; //DOUBLE PRECISION   HALF
    {  HALF = 0.5D ; } //PARAMETER          ( HALF = 0.5D0 )
    //*     ..
    //*     .. Local Scalars ..
    double AB, ACMN, ACMX, ADF, DF, RT, SM, TB; //DOUBLE PRECISION   AB, ACMN, ACMX, ADF, DF, RT, SM, TB
    //*     ..
    //*     .. Intrinsic Functions ..
    //INTRINSIC          ABS, SQRT
    //*     ..
    //*     .. Executable Statements ..
    //*
    //*     Compute the eigenvalues
    //*
    SM = A + C; //SM = A + C
    DF = A - C; //DF = A - C
    ADF = abs(DF); //ADF = ABS( DF )
    TB = B + B; //TB = B + B
    AB = abs(TB); //AB = ABS( TB )
    if ( abs(A) > abs( C ) ) { //IF( ABS( A ).GT.ABS( C ) ) THEN
      ACMX = A; //ACMX = A
      ACMN = C; //ACMN = C
    } else { // ELSE
      ACMX = C; //ACMX = C
      ACMN = A; //ACMN = A
    } // END IF
    if ( ADF > AB ) { //IF( ADF.GT.AB ) THEN
      double tmp = ( AB / ADF );
      RT = ADF*sqrt( ONE+tmp*tmp ); //RT = ADF*SQRT( ONE+( AB / ADF )**2 )
    } else if ( ADF < AB ) { //ELSE IF( ADF.LT.AB ) THEN
      double tmp = ( ADF / AB );
      RT = AB*sqrt( ONE+tmp*tmp ); //RT = AB*SQRT( ONE+( ADF / AB )**2 )
    } else { // ELSE
      //*
      //*        Includes case AB=ADF=0
      //*
      RT = AB*sqrt( TWO ); //RT = AB*SQRT( TWO )
    } // END IF
    if ( SM < ZERO ) { //IF( SM.LT.ZERO ) THEN
      RT1 = HALF*( SM-RT ); //RT1 = HALF*( SM-RT )
      //*
      //*        Order of execution important.
      //*        To get fully accurate smaller eigenvalue,
      //*        next line needs to be executed in higher precision.
      //*
      RT2 = ( ACMX / RT1 )*ACMN - ( B / RT1 )*B; //RT2 = ( ACMX / RT1 )*ACMN - ( B / RT1 )*B
    } else if ( SM > ZERO ) { //ELSE IF( SM.GT.ZERO ) THEN
      RT1 = HALF*( SM+RT ); //RT1 = HALF*( SM+RT )
      //*
      //*        Order of execution important.
      //*        To get fully accurate smaller eigenvalue,
      //*        next line needs to be executed in higher precision.
      //*
      RT2 = ( ACMX / RT1 )*ACMN - ( B / RT1 )*B; //RT2 = ( ACMX / RT1 )*ACMN - ( B / RT1 )*B
    } else { // ELSE
      //*
      //*        Includes case RT1 = RT2 = 0
      //*
      RT1 = HALF*RT; //RT1 = HALF*RT
      RT2 = -HALF*RT; //RT2 = -HALF*RT
    } // END IF
  } // RETURN
  //*
  //*     End of DLAE2
  //*
} // END