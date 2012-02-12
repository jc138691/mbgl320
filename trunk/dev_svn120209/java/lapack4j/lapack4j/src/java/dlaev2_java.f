public class DLAEV2 { //SUBROUTINE DLAEV2( A, B, C, RT1, RT2, CS1, SN1 )
public static void DLAEV2( A, B, C, RT1, RT2, CS1, SN1 ) { //SUBROUTINE DLAEV2( A, B, C, RT1, RT2, CS1, SN1 )
//*
//*  -- LAPACK auxiliary routine (version 3.2) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*     November 2006
//*
//*     .. Scalar Arguments ..
double A, B, C, CS1, RT1, RT2, SN1; //DOUBLE PRECISION   A, B, C, CS1, RT1, RT2, SN1
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  DLAEV2 computes the eigendecomposition of a 2-by-2 symmetric matrix
//*     [  A   B  ]
//*     [  B   C  ].
//*  On return, RT1 is the eigenvalue of larger absolute value, RT2 is the
//*  eigenvalue of smaller absolute value, and (CS1,SN1) is the unit right
//*  eigenvector for RT1, giving the decomposition
//*
//*     [ CS1  SN1 ] [  A   B  ] [ CS1 -SN1 ]  =  [ RT1  0  ]
//*     [-SN1  CS1 ] [  B   C  ] [ SN1  CS1 ]     [  0  RT2 ].
//*
//*  Arguments
//*  =========
//*
//*  A       (input) DOUBLE PRECISION
//*          The (1,1) element of the 2-by-2 matrix.
//*
//*  B       (input) DOUBLE PRECISION
//*          The (1,2) element and the conjugate of the (2,1) element of
//*          the 2-by-2 matrix.
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
//*  CS1     (output) DOUBLE PRECISION
//*  SN1     (output) DOUBLE PRECISION
//*          The vector (CS1, SN1) is a unit right eigenvector for RT1.
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
//*  CS1 and SN1 are accurate to a few ulps barring over/underflow.
//*
//*  Overflow is possible only if RT1 is within a factor of 5 of overflow.
//*  Underflow is harmless if the input data is 0 or exceeds
//*     underflow_threshold / macheps.
//*
//* =====================================================================
//*
//*     .. Parameters ..
double ONE; //DOUBLE PRECISION   ONE
{  ONE = 1.0D0 ; } //PARAMETER          ( ONE = 1.0D0 )
double TWO; //DOUBLE PRECISION   TWO
{  TWO = 2.0D0 ; } //PARAMETER          ( TWO = 2.0D0 )
double ZERO; //DOUBLE PRECISION   ZERO
{  ZERO = 0.0D0 ; } //PARAMETER          ( ZERO = 0.0D0 )
double HALF; //DOUBLE PRECISION   HALF
{  HALF = 0.5D0 ; } //PARAMETER          ( HALF = 0.5D0 )
//*     ..
//*     .. Local Scalars ..
int SGN1, SGN2; //INTEGER            SGN1, SGN2
double AB, ACMN, ACMX, ACS, ADF, CS, CT, DF, RT, SM,TB, TN; //DOUBLE PRECISION   AB, ACMN, ACMX, ACS, ADF, CS, CT, DF, RT, SM,TB, TN
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
ADF = ABS( DF ); //ADF = ABS( DF )
TB = B + B; //TB = B + B
AB = ABS( TB ); //AB = ABS( TB )
if ( ABS( A ) > ABS( C ) ) { //IF( ABS( A ).GT.ABS( C ) ) THEN
ACMX = A; //ACMX = A
ACMN = C; //ACMN = C
} else { // ELSE
ACMX = C; //ACMX = C
ACMN = A; //ACMN = A
} // END IF
if ( ADF > AB ) { //IF( ADF.GT.AB ) THEN
RT = ADF*SQRT( ONE+( AB / ADF )**2 ); //RT = ADF*SQRT( ONE+( AB / ADF )**2 )
} else if ( ADF < AB ) { //ELSE IF( ADF.LT.AB ) THEN
RT = AB*SQRT( ONE+( ADF / AB )**2 ); //RT = AB*SQRT( ONE+( ADF / AB )**2 )
} else { // ELSE
//*
//*        Includes case AB=ADF=0
//*
RT = AB*SQRT( TWO ); //RT = AB*SQRT( TWO )
} // END IF
if ( SM < ZERO ) { //IF( SM.LT.ZERO ) THEN
RT1 = HALF*( SM-RT ); //RT1 = HALF*( SM-RT )
SGN1 = -1; //SGN1 = -1
//*
//*        Order of execution important.
//*        To get fully accurate smaller eigenvalue,
//*        next line needs to be executed in higher precision.
//*
RT2 = ( ACMX / RT1 )*ACMN - ( B / RT1 )*B; //RT2 = ( ACMX / RT1 )*ACMN - ( B / RT1 )*B
} else if ( SM > ZERO ) { //ELSE IF( SM.GT.ZERO ) THEN
RT1 = HALF*( SM+RT ); //RT1 = HALF*( SM+RT )
SGN1 = 1; //SGN1 = 1
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
SGN1 = 1; //SGN1 = 1
} // END IF
//*
//*     Compute the eigenvector
//*
if ( DF >= ZERO ) { //IF( DF.GE.ZERO ) THEN
CS = DF + RT; //CS = DF + RT
SGN2 = 1; //SGN2 = 1
} else { // ELSE
CS = DF - RT; //CS = DF - RT
SGN2 = -1; //SGN2 = -1
} // END IF
ACS = ABS( CS ); //ACS = ABS( CS )
if ( ACS > AB ) { //IF( ACS.GT.AB ) THEN
CT = -TB / CS; //CT = -TB / CS
SN1 = ONE / SQRT( ONE+CT*CT ); //SN1 = ONE / SQRT( ONE+CT*CT )
CS1 = CT*SN1; //CS1 = CT*SN1
} else { // ELSE
if ( AB == ZERO ) { //IF( AB.EQ.ZERO ) THEN
CS1 = ONE; //CS1 = ONE
SN1 = ZERO; //SN1 = ZERO
} else { // ELSE
TN = -CS / TB; //TN = -CS / TB
CS1 = ONE / SQRT( ONE+TN*TN ); //CS1 = ONE / SQRT( ONE+TN*TN )
SN1 = TN*CS1; //SN1 = TN*CS1
} // END IF
} // END IF
if ( SGN1 == SGN2 ) { //IF( SGN1.EQ.SGN2 ) THEN
TN = CS1; //TN = CS1
CS1 = -SN1; //CS1 = -SN1
SN1 = TN; //SN1 = TN
} // END IF
} // RETURN
//*
//*     End of DLAEV2
//*
} // END