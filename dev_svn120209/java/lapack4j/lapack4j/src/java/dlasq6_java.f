public class DLASQ6 { //SUBROUTINE DLASQ6( I0, N0, Z, PP, DMIN, DMIN1, DMIN2, DN,DNM1, DNM2 )
public static void DLASQ6( I0, N0, Z, PP, DMIN, DMIN1, DMIN2, DN,DNM1, DNM2 ) { //SUBROUTINE DLASQ6( I0, N0, Z, PP, DMIN, DMIN1, DMIN2, DN,DNM1, DNM2 )
//*
//*  -- LAPACK routine (version 3.2)                                    --
//*
//*  -- Contributed by Osni Marques of the Lawrence Berkeley National   --
//*  -- Laboratory and Beresford Parlett of the Univ. of California at  --
//*  -- Berkeley                                                        --
//*  -- November 2008                                                   --
//*
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*
//*     .. Scalar Arguments ..
int I0, N0, PP; //INTEGER            I0, N0, PP
double DMIN, DMIN1, DMIN2, DN, DNM1, DNM2; //DOUBLE PRECISION   DMIN, DMIN1, DMIN2, DN, DNM1, DNM2
//*     ..
//*     .. Array Arguments ..
double Z[]; //DOUBLE PRECISION   Z( * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  DLASQ6 computes one dqd (shift equal to zero) transform in
//*  ping-pong form, with protection against underflow and overflow.
//*
//*  Arguments
//*  =========
//*
//*  I0    (input) INTEGER
//*        First index.
//*
//*  N0    (input) INTEGER
//*        Last index.
//*
//*  Z     (input) DOUBLE PRECISION array, dimension ( 4*N )
//*        Z holds the qd array. EMIN is stored in Z(4*N0) to avoid
//*        an extra argument.
//*
//*  PP    (input) INTEGER
//*        PP=0 for ping, PP=1 for pong.
//*
//*  DMIN  (output) DOUBLE PRECISION
//*        Minimum value of d.
//*
//*  DMIN1 (output) DOUBLE PRECISION
//*        Minimum value of d, excluding D( N0 ).
//*
//*  DMIN2 (output) DOUBLE PRECISION
//*        Minimum value of d, excluding D( N0 ) and D( N0-1 ).
//*
//*  DN    (output) DOUBLE PRECISION
//*        d(N0), the last value of d.
//*
//*  DNM1  (output) DOUBLE PRECISION
//*        d(N0-1).
//*
//*  DNM2  (output) DOUBLE PRECISION
//*        d(N0-2).
//*
//*  =====================================================================
//*
//*     .. Parameter ..
double ZERO; //DOUBLE PRECISION   ZERO
{  ZERO = 0.0D0 ; } //PARAMETER          ( ZERO = 0.0D0 )
//*     ..
//*     .. Local Scalars ..
int J4, J4P2; //INTEGER            J4, J4P2
double D, EMIN, SAFMIN, TEMP; //DOUBLE PRECISION   D, EMIN, SAFMIN, TEMP
//*     ..
//*     .. External Function ..
double DLAMCH; //DOUBLE PRECISION   DLAMCH
EXTERNAL           DLAMCH; //EXTERNAL           DLAMCH
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC          MIN
//*     ..
//*     .. Executable Statements ..
//*
if ( ( N0-I0-1 ) <= 0 )RETURN; //IF( ( N0-I0-1 ).LE.0 )RETURN
//*
SAFMIN = DLAMCH( 'Safe minimum' ); //SAFMIN = DLAMCH( 'Safe minimum' )
J4 = 4*I0 + PP - 3; //J4 = 4*I0 + PP - 3
EMIN = Z( J4+4 ); //EMIN = Z( J4+4 )
D = Z( J4 ); //D = Z( J4 )
DMIN = D; //DMIN = D
//*
if ( PP == 0 ) { //IF( PP.EQ.0 ) THEN
for ( 10 J4 = 4*I0; 4*( N0-3 ); 4) { //DO 10 J4 = 4*I0, 4*( N0-3 ), 4
Z( J4-2 ) = D + Z( J4-1 ); //Z( J4-2 ) = D + Z( J4-1 )
if ( Z( J4-2 ) == ZERO ) { //IF( Z( J4-2 ).EQ.ZERO ) THEN
Z( J4 ) = ZERO; //Z( J4 ) = ZERO
D = Z( J4+1 ); //D = Z( J4+1 )
DMIN = D; //DMIN = D
EMIN = ZERO; //EMIN = ZERO
} else if ( SAFMIN*Z( J4+1 ) < Z( J4-2 )  && SAFMIN*Z( J4-2 ) < Z( J4+1 ) ) { //ELSE IF( SAFMIN*Z( J4+1 ).LT.Z( J4-2 ) .AND.SAFMIN*Z( J4-2 ).LT.Z( J4+1 ) ) THEN
TEMP = Z( J4+1 ) / Z( J4-2 ); //TEMP = Z( J4+1 ) / Z( J4-2 )
Z( J4 ) = Z( J4-1 )*TEMP; //Z( J4 ) = Z( J4-1 )*TEMP
D = D*TEMP; //D = D*TEMP
} else { // ELSE
Z( J4 ) = Z( J4+1 )*( Z( J4-1 ) / Z( J4-2 ) ); //Z( J4 ) = Z( J4+1 )*( Z( J4-1 ) / Z( J4-2 ) )
D = Z( J4+1 )*( D / Z( J4-2 ) ); //D = Z( J4+1 )*( D / Z( J4-2 ) )
} // END IF
DMIN = MIN( DMIN, D ); //DMIN = MIN( DMIN, D )
EMIN = MIN( EMIN, Z( J4 ) ); //EMIN = MIN( EMIN, Z( J4 ) )
10    CONTINUE; //10    CONTINUE
} else { // ELSE
for ( 20 J4 = 4*I0; 4*( N0-3 ); 4) { //DO 20 J4 = 4*I0, 4*( N0-3 ), 4
Z( J4-3 ) = D + Z( J4 ); //Z( J4-3 ) = D + Z( J4 )
if ( Z( J4-3 ) == ZERO ) { //IF( Z( J4-3 ).EQ.ZERO ) THEN
Z( J4-1 ) = ZERO; //Z( J4-1 ) = ZERO
D = Z( J4+2 ); //D = Z( J4+2 )
DMIN = D; //DMIN = D
EMIN = ZERO; //EMIN = ZERO
} else if ( SAFMIN*Z( J4+2 ) < Z( J4-3 )  && SAFMIN*Z( J4-3 ) < Z( J4+2 ) ) { //ELSE IF( SAFMIN*Z( J4+2 ).LT.Z( J4-3 ) .AND.SAFMIN*Z( J4-3 ).LT.Z( J4+2 ) ) THEN
TEMP = Z( J4+2 ) / Z( J4-3 ); //TEMP = Z( J4+2 ) / Z( J4-3 )
Z( J4-1 ) = Z( J4 )*TEMP; //Z( J4-1 ) = Z( J4 )*TEMP
D = D*TEMP; //D = D*TEMP
} else { // ELSE
Z( J4-1 ) = Z( J4+2 )*( Z( J4 ) / Z( J4-3 ) ); //Z( J4-1 ) = Z( J4+2 )*( Z( J4 ) / Z( J4-3 ) )
D = Z( J4+2 )*( D / Z( J4-3 ) ); //D = Z( J4+2 )*( D / Z( J4-3 ) )
} // END IF
DMIN = MIN( DMIN, D ); //DMIN = MIN( DMIN, D )
EMIN = MIN( EMIN, Z( J4-1 ) ); //EMIN = MIN( EMIN, Z( J4-1 ) )
20    CONTINUE; //20    CONTINUE
} // END IF
//*
//*     Unroll last two steps. 
//*
DNM2 = D; //DNM2 = D
DMIN2 = DMIN; //DMIN2 = DMIN
J4 = 4*( N0-2 ) - PP; //J4 = 4*( N0-2 ) - PP
J4P2 = J4 + 2*PP - 1; //J4P2 = J4 + 2*PP - 1
Z( J4-2 ) = DNM2 + Z( J4P2 ); //Z( J4-2 ) = DNM2 + Z( J4P2 )
if ( Z( J4-2 ) == ZERO ) { //IF( Z( J4-2 ).EQ.ZERO ) THEN
Z( J4 ) = ZERO; //Z( J4 ) = ZERO
DNM1 = Z( J4P2+2 ); //DNM1 = Z( J4P2+2 )
DMIN = DNM1; //DMIN = DNM1
EMIN = ZERO; //EMIN = ZERO
} else if ( SAFMIN*Z( J4P2+2 ) < Z( J4-2 )  && SAFMIN*Z( J4-2 ) < Z( J4P2+2 ) ) { //ELSE IF( SAFMIN*Z( J4P2+2 ).LT.Z( J4-2 ) .AND.SAFMIN*Z( J4-2 ).LT.Z( J4P2+2 ) ) THEN
TEMP = Z( J4P2+2 ) / Z( J4-2 ); //TEMP = Z( J4P2+2 ) / Z( J4-2 )
Z( J4 ) = Z( J4P2 )*TEMP; //Z( J4 ) = Z( J4P2 )*TEMP
DNM1 = DNM2*TEMP; //DNM1 = DNM2*TEMP
} else { // ELSE
Z( J4 ) = Z( J4P2+2 )*( Z( J4P2 ) / Z( J4-2 ) ); //Z( J4 ) = Z( J4P2+2 )*( Z( J4P2 ) / Z( J4-2 ) )
DNM1 = Z( J4P2+2 )*( DNM2 / Z( J4-2 ) ); //DNM1 = Z( J4P2+2 )*( DNM2 / Z( J4-2 ) )
} // END IF
DMIN = MIN( DMIN, DNM1 ); //DMIN = MIN( DMIN, DNM1 )
//*
DMIN1 = DMIN; //DMIN1 = DMIN
J4 = J4 + 4; //J4 = J4 + 4
J4P2 = J4 + 2*PP - 1; //J4P2 = J4 + 2*PP - 1
Z( J4-2 ) = DNM1 + Z( J4P2 ); //Z( J4-2 ) = DNM1 + Z( J4P2 )
if ( Z( J4-2 ) == ZERO ) { //IF( Z( J4-2 ).EQ.ZERO ) THEN
Z( J4 ) = ZERO; //Z( J4 ) = ZERO
DN = Z( J4P2+2 ); //DN = Z( J4P2+2 )
DMIN = DN; //DMIN = DN
EMIN = ZERO; //EMIN = ZERO
} else if ( SAFMIN*Z( J4P2+2 ) < Z( J4-2 )  && SAFMIN*Z( J4-2 ) < Z( J4P2+2 ) ) { //ELSE IF( SAFMIN*Z( J4P2+2 ).LT.Z( J4-2 ) .AND.SAFMIN*Z( J4-2 ).LT.Z( J4P2+2 ) ) THEN
TEMP = Z( J4P2+2 ) / Z( J4-2 ); //TEMP = Z( J4P2+2 ) / Z( J4-2 )
Z( J4 ) = Z( J4P2 )*TEMP; //Z( J4 ) = Z( J4P2 )*TEMP
DN = DNM1*TEMP; //DN = DNM1*TEMP
} else { // ELSE
Z( J4 ) = Z( J4P2+2 )*( Z( J4P2 ) / Z( J4-2 ) ); //Z( J4 ) = Z( J4P2+2 )*( Z( J4P2 ) / Z( J4-2 ) )
DN = Z( J4P2+2 )*( DNM1 / Z( J4-2 ) ); //DN = Z( J4P2+2 )*( DNM1 / Z( J4-2 ) )
} // END IF
DMIN = MIN( DMIN, DN ); //DMIN = MIN( DMIN, DN )
//*
Z( J4+2 ) = DN; //Z( J4+2 ) = DN
Z( 4*N0-PP ) = EMIN; //Z( 4*N0-PP ) = EMIN
} // RETURN
//*
//*     End of DLASQ6
//*
} // END