public class DLASRT { //SUBROUTINE DLASRT( ID, N, D, INFO )
public static void DLASRT( ID, N, D, INFO ) { //SUBROUTINE DLASRT( ID, N, D, INFO )
//*
//*  -- LAPACK routine (version 3.2) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*     November 2006
//*
//*     .. Scalar Arguments ..
char ID; //CHARACTER          ID
int INFO, N; //INTEGER            INFO, N
//*     ..
//*     .. Array Arguments ..
double D[]; //DOUBLE PRECISION   D( * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  Sort the numbers in D in increasing order (if ID = 'I') or
//*  in decreasing order (if ID = 'D' ).
//*
//*  Use Quick Sort, reverting to Insertion sort on arrays of
//*  size <= 20. Dimension of STACK limits N to about 2**32.
//*
//*  Arguments
//*  =========
//*
//*  ID      (input) CHARACTER*1
//*          = 'I': sort D in increasing order;
//*          = 'D': sort D in decreasing order.
//*
//*  N       (input) INTEGER
//*          The length of the array D.
//*
//*  D       (input/output) DOUBLE PRECISION array, dimension (N)
//*          On entry, the array to be sorted.
//*          On exit, D has been sorted into increasing order
//*          (D(1) <= ... <= D(N) ) or into decreasing order
//*          (D(1) >= ... >= D(N) ), depending on ID.
//*
//*  INFO    (output) INTEGER
//*          = 0:  successful exit
//*          < 0:  if INFO = -i, the i-th argument had an illegal value
//*
//*  =====================================================================
//*
//*     .. Parameters ..
int SELECT; //INTEGER            SELECT
{  SELECT = 20 ; } //PARAMETER          ( SELECT = 20 )
//*     ..
//*     .. Local Scalars ..
int DIR, ENDD, I, J, START, STKPNT; //INTEGER            DIR, ENDD, I, J, START, STKPNT
double D1, D2, D3, DMNMX, TMP; //DOUBLE PRECISION   D1, D2, D3, DMNMX, TMP
//*     ..
//*     .. Local Arrays ..
int STACK( 2, 32 ); //INTEGER            STACK( 2, 32 )
//*     ..
//*     .. External Functions ..
boolean LSAME; //LOGICAL            LSAME
EXTERNAL           LSAME; //EXTERNAL           LSAME
//*     ..
//*     .. External Subroutines ..
EXTERNAL           XERBLA; //EXTERNAL           XERBLA
//*     ..
//*     .. Executable Statements ..
//*
//*     Test the input paramters.
//*
INFO = 0; //INFO = 0
DIR = -1; //DIR = -1
if ( LSAME( ID, 'D' ) ) { //IF( LSAME( ID, 'D' ) ) THEN
DIR = 0; //DIR = 0
} else if ( LSAME( ID, 'I' ) ) { //ELSE IF( LSAME( ID, 'I' ) ) THEN
DIR = 1; //DIR = 1
} // END IF
if ( DIR == -1 ) { //IF( DIR.EQ.-1 ) THEN
INFO = -1; //INFO = -1
} else if ( N < 0 ) { //ELSE IF( N.LT.0 ) THEN
INFO = -2; //INFO = -2
} // END IF
if ( INFO != 0 ) { //IF( INFO.NE.0 ) THEN
CALL XERBLA( 'DLASRT', -INFO ); //CALL XERBLA( 'DLASRT', -INFO )
} // RETURN
} // END IF
//*
//*     Quick return if possible
//*
if ( N <= 1 )RETURN; //IF( N.LE.1 )RETURN
//*
STKPNT = 1; //STKPNT = 1
STACK( 1, 1 ) = 1; //STACK( 1, 1 ) = 1
STACK( 2, 1 ) = N; //STACK( 2, 1 ) = N
10 CONTINUE; //10 CONTINUE
START = STACK( 1, STKPNT ); //START = STACK( 1, STKPNT )
ENDD = STACK( 2, STKPNT ); //ENDD = STACK( 2, STKPNT )
STKPNT = STKPNT - 1; //STKPNT = STKPNT - 1
if ( ENDD-START <= SELECT  &&  ENDD-START > 0 ) { //IF( ENDD-START.LE.SELECT .AND. ENDD-START.GT.0 ) THEN
//*
//*        Do Insertion sort on D( START:ENDD )
//*
if ( DIR == 0 ) { //IF( DIR.EQ.0 ) THEN
//*
//*           Sort into decreasing order
//*
for ( 30 I = START + 1; ENDD) { //DO 30 I = START + 1, ENDD
for ( 20 J = I; START + 1; -1) { //DO 20 J = I, START + 1, -1
if ( D( J ) > D( J-1 ) ) { //IF( D( J ).GT.D( J-1 ) ) THEN
DMNMX = D( J ); //DMNMX = D( J )
D( J ) = D( J-1 ); //D( J ) = D( J-1 )
D( J-1 ) = DMNMX; //D( J-1 ) = DMNMX
} else { // ELSE
GO TO 30; //GO TO 30
} // END IF
20          CONTINUE; //20          CONTINUE
30       CONTINUE; //30       CONTINUE
//*
} else { // ELSE
//*
//*           Sort into increasing order
//*
for ( 50 I = START + 1; ENDD) { //DO 50 I = START + 1, ENDD
for ( 40 J = I; START + 1; -1) { //DO 40 J = I, START + 1, -1
if ( D( J ) < D( J-1 ) ) { //IF( D( J ).LT.D( J-1 ) ) THEN
DMNMX = D( J ); //DMNMX = D( J )
D( J ) = D( J-1 ); //D( J ) = D( J-1 )
D( J-1 ) = DMNMX; //D( J-1 ) = DMNMX
} else { // ELSE
GO TO 50; //GO TO 50
} // END IF
40          CONTINUE; //40          CONTINUE
50       CONTINUE; //50       CONTINUE
//*
} // END IF
//*
} else if ( ENDD-START > SELECT ) { //ELSE IF( ENDD-START.GT.SELECT ) THEN
//*
//*        Partition D( START:ENDD ) and stack parts, largest one first
//*
//*        Choose partition entry as median of 3
//*
D1 = D( START ); //D1 = D( START )
D2 = D( ENDD ); //D2 = D( ENDD )
I = ( START+ENDD ) / 2; //I = ( START+ENDD ) / 2
D3 = D( I ); //D3 = D( I )
if ( D1 < D2 ) { //IF( D1.LT.D2 ) THEN
if ( D3 < D1 ) { //IF( D3.LT.D1 ) THEN
DMNMX = D1; //DMNMX = D1
} else if ( D3 < D2 ) { //ELSE IF( D3.LT.D2 ) THEN
DMNMX = D3; //DMNMX = D3
} else { // ELSE
DMNMX = D2; //DMNMX = D2
} // END IF
} else { // ELSE
if ( D3 < D2 ) { //IF( D3.LT.D2 ) THEN
DMNMX = D2; //DMNMX = D2
} else if ( D3 < D1 ) { //ELSE IF( D3.LT.D1 ) THEN
DMNMX = D3; //DMNMX = D3
} else { // ELSE
DMNMX = D1; //DMNMX = D1
} // END IF
} // END IF
//*
if ( DIR == 0 ) { //IF( DIR.EQ.0 ) THEN
//*
//*           Sort into decreasing order
//*
I = START - 1; //I = START - 1
J = ENDD + 1; //J = ENDD + 1
60       CONTINUE; //60       CONTINUE
70       CONTINUE; //70       CONTINUE
J = J - 1; //J = J - 1
if ( D( J ) < DMNMX )GO TO 70; //IF( D( J ).LT.DMNMX )GO TO 70
80       CONTINUE; //80       CONTINUE
I = I + 1; //I = I + 1
if ( D( I ) > DMNMX )GO TO 80; //IF( D( I ).GT.DMNMX )GO TO 80
if ( I < J ) { //IF( I.LT.J ) THEN
TMP = D( I ); //TMP = D( I )
D( I ) = D( J ); //D( I ) = D( J )
D( J ) = TMP; //D( J ) = TMP
GO TO 60; //GO TO 60
} // END IF
if ( J-START > ENDD-J-1 ) { //IF( J-START.GT.ENDD-J-1 ) THEN
STKPNT = STKPNT + 1; //STKPNT = STKPNT + 1
STACK( 1, STKPNT ) = START; //STACK( 1, STKPNT ) = START
STACK( 2, STKPNT ) = J; //STACK( 2, STKPNT ) = J
STKPNT = STKPNT + 1; //STKPNT = STKPNT + 1
STACK( 1, STKPNT ) = J + 1; //STACK( 1, STKPNT ) = J + 1
STACK( 2, STKPNT ) = ENDD; //STACK( 2, STKPNT ) = ENDD
} else { // ELSE
STKPNT = STKPNT + 1; //STKPNT = STKPNT + 1
STACK( 1, STKPNT ) = J + 1; //STACK( 1, STKPNT ) = J + 1
STACK( 2, STKPNT ) = ENDD; //STACK( 2, STKPNT ) = ENDD
STKPNT = STKPNT + 1; //STKPNT = STKPNT + 1
STACK( 1, STKPNT ) = START; //STACK( 1, STKPNT ) = START
STACK( 2, STKPNT ) = J; //STACK( 2, STKPNT ) = J
} // END IF
} else { // ELSE
//*
//*           Sort into increasing order
//*
I = START - 1; //I = START - 1
J = ENDD + 1; //J = ENDD + 1
90       CONTINUE; //90       CONTINUE
100       CONTINUE; //100       CONTINUE
J = J - 1; //J = J - 1
if ( D( J ) > DMNMX )GO TO 100; //IF( D( J ).GT.DMNMX )GO TO 100
110       CONTINUE; //110       CONTINUE
I = I + 1; //I = I + 1
if ( D( I ) < DMNMX )GO TO 110; //IF( D( I ).LT.DMNMX )GO TO 110
if ( I < J ) { //IF( I.LT.J ) THEN
TMP = D( I ); //TMP = D( I )
D( I ) = D( J ); //D( I ) = D( J )
D( J ) = TMP; //D( J ) = TMP
GO TO 90; //GO TO 90
} // END IF
if ( J-START > ENDD-J-1 ) { //IF( J-START.GT.ENDD-J-1 ) THEN
STKPNT = STKPNT + 1; //STKPNT = STKPNT + 1
STACK( 1, STKPNT ) = START; //STACK( 1, STKPNT ) = START
STACK( 2, STKPNT ) = J; //STACK( 2, STKPNT ) = J
STKPNT = STKPNT + 1; //STKPNT = STKPNT + 1
STACK( 1, STKPNT ) = J + 1; //STACK( 1, STKPNT ) = J + 1
STACK( 2, STKPNT ) = ENDD; //STACK( 2, STKPNT ) = ENDD
} else { // ELSE
STKPNT = STKPNT + 1; //STKPNT = STKPNT + 1
STACK( 1, STKPNT ) = J + 1; //STACK( 1, STKPNT ) = J + 1
STACK( 2, STKPNT ) = ENDD; //STACK( 2, STKPNT ) = ENDD
STKPNT = STKPNT + 1; //STKPNT = STKPNT + 1
STACK( 1, STKPNT ) = START; //STACK( 1, STKPNT ) = START
STACK( 2, STKPNT ) = J; //STACK( 2, STKPNT ) = J
} // END IF
} // END IF
} // END IF
if ( STKPNT > 0 )GO TO 10; //IF( STKPNT.GT.0 )GO TO 10
} // RETURN
//*
//*     End of DLASRT
//*
} // END