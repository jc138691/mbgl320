public class DLARRA { //SUBROUTINE DLARRA( N, D, E, E2, SPLTOL, TNRM,NSPLIT, ISPLIT, INFO )
public static void DLARRA( N, D, E, E2, SPLTOL, TNRM,NSPLIT, ISPLIT, INFO ) { //SUBROUTINE DLARRA( N, D, E, E2, SPLTOL, TNRM,NSPLIT, ISPLIT, INFO )
//IMPLICIT NONE
//*
//*  -- LAPACK auxiliary routine (version 3.2.2) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*     June 2010
//*
//*     .. Scalar Arguments ..
int INFO, N, NSPLIT; //INTEGER            INFO, N, NSPLIT
double SPLTOL, TNRM; //DOUBLE PRECISION    SPLTOL, TNRM
//*     ..
//*     .. Array Arguments ..
int ISPLIT[]; //INTEGER            ISPLIT( * )
double D[], E[], E2[]; //DOUBLE PRECISION   D( * ), E( * ), E2( * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  Compute the splitting points with threshold SPLTOL.
//*  DLARRA sets any "small" off-diagonal elements to zero.
//*
//*  Arguments
//*  =========
//*
//*  N       (input) INTEGER
//*          The order of the matrix. N > 0.
//*
//*  D       (input) DOUBLE PRECISION array, dimension (N)
//*          On entry, the N diagonal elements of the tridiagonal
//*          matrix T.
//*
//*  E       (input/output) DOUBLE PRECISION array, dimension (N)
//*          On entry, the first (N-1) entries contain the subdiagonal
//*          elements of the tridiagonal matrix T; E(N) need not be set.
//*          On exit, the entries E( ISPLIT( I ) ), 1 <= I <= NSPLIT,
//*          are set to zero, the other entries of E are untouched.
//*
//*  E2      (input/output) DOUBLE PRECISION array, dimension (N)
//*          On entry, the first (N-1) entries contain the SQUARES of the
//*          subdiagonal elements of the tridiagonal matrix T;
//*          E2(N) need not be set.
//*          On exit, the entries E2( ISPLIT( I ) ),
//*          1 <= I <= NSPLIT, have been set to zero
//*
//*  SPLTOL  (input) DOUBLE PRECISION
//*          The threshold for splitting. Two criteria can be used:
//*          SPLTOL<0 : criterion based on absolute off-diagonal value
//*          SPLTOL>0 : criterion that preserves relative accuracy
//*
//*  TNRM    (input) DOUBLE PRECISION
//*          The norm of the matrix.
//*
//*  NSPLIT  (output) INTEGER
//*          The number of blocks T splits into. 1 <= NSPLIT <= N.
//*
//*  ISPLIT  (output) INTEGER array, dimension (N)
//*          The splitting points, at which T breaks up into blocks.
//*          The first block consists of rows/columns 1 to ISPLIT(1),
//*          the second of rows/columns ISPLIT(1)+1 through ISPLIT(2),
//*          etc., and the NSPLIT-th consists of rows/columns
//*          ISPLIT(NSPLIT-1)+1 through ISPLIT(NSPLIT)=N.
//*
//*
//*  INFO    (output) INTEGER
//*          = 0:  successful exit
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
double ZERO; //DOUBLE PRECISION   ZERO
{  ZERO = 0.0D0 ; } //PARAMETER          ( ZERO = 0.0D0 )
//*     ..
//*     .. Local Scalars ..
int I; //INTEGER            I
double EABS, TMP1; //DOUBLE PRECISION   EABS, TMP1
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC          ABS
//*     ..
//*     .. Executable Statements ..
//*
INFO = 0; //INFO = 0
//*     Compute splitting points
NSPLIT = 1; //NSPLIT = 1
if (SPLTOL < ZERO) { //IF(SPLTOL.LT.ZERO) THEN
//*        Criterion based on absolute off-diagonal value
TMP1 = ABS(SPLTOL)* TNRM; //TMP1 = ABS(SPLTOL)* TNRM
for ( 9 I = 1; N-1) { //DO 9 I = 1, N-1
EABS = ABS( E(I) ); //EABS = ABS( E(I) )
if ( EABS  <=  TMP1) { //IF( EABS .LE. TMP1) THEN
E(I) = ZERO; //E(I) = ZERO
E2(I) = ZERO; //E2(I) = ZERO
ISPLIT( NSPLIT ) = I; //ISPLIT( NSPLIT ) = I
NSPLIT = NSPLIT + 1; //NSPLIT = NSPLIT + 1
} // END IF
9       CONTINUE; //9       CONTINUE
} else { // ELSE
//*        Criterion that guarantees relative accuracy
for ( 10 I = 1; N-1) { //DO 10 I = 1, N-1
EABS = ABS( E(I) ); //EABS = ABS( E(I) )
if ( EABS  <=  SPLTOL * SQRT(ABS(D(I)))*SQRT(ABS(D(I+1))) )THEN; //IF( EABS .LE. SPLTOL * SQRT(ABS(D(I)))*SQRT(ABS(D(I+1))) )THEN
E(I) = ZERO; //E(I) = ZERO
E2(I) = ZERO; //E2(I) = ZERO
ISPLIT( NSPLIT ) = I; //ISPLIT( NSPLIT ) = I
NSPLIT = NSPLIT + 1; //NSPLIT = NSPLIT + 1
} // END IF
10      CONTINUE; //10      CONTINUE
} // ENDIF
ISPLIT( NSPLIT ) = N; //ISPLIT( NSPLIT ) = N
} // RETURN
//*
//*     End of DLARRA
//*
} // END