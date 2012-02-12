package lapack4j.src.java;
import lapack4j.utils.DblRef;
import static lapack4j.utils.IntrFuncs.MIN;

/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,20/10/11,2:52 PM
 */
public class DLASQ5 { //SUBROUTINE DLASQ5( I0, N0, Z, PP, TAU, DMIN, DMIN1, DMIN2, DN,DNM1, DNM2, IEEE )
  public static void DLASQ5(final int I0, final int N0, final double[] Z, final int PP, final double TAU
    , DblRef pDMIN, DblRef pDMIN1, DblRef pDMIN2
    , DblRef pDN, DblRef pDNM1, DblRef pDNM2
    , final boolean IEEE ) { //SUBROUTINE DLASQ5( I0, N0, Z, PP, TAU, DMIN, DMIN1, DMIN2, DN,DNM1, DNM2, IEEE )
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
    //    boolean IEEE; //LOGICAL            IEEE
    //    int I0, N0, PP; //INTEGER            I0, N0, PP
    double DMIN = 0, DMIN1 = 0, DMIN2 = 0
      , DN = 0, DNM1 = 0, DNM2 = 0; //DOUBLE PRECISION   DMIN, DMIN1, DMIN2, DN, DNM1, DNM2, TAU
    //*     ..
    //*     .. Array Arguments ..
    //    double Z[]; //DOUBLE PRECISION   Z( * )
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  DLASQ5 computes one dqds transform in ping-pong form, one
    //*  version for IEEE machines another for non IEEE machines.
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
    //*  TAU   (input) DOUBLE PRECISION
    //*        This is the shift.
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
    //*  IEEE  (input) LOGICAL
    //*        Flag for IEEE or non IEEE arithmetic.
    //*
    //*  =====================================================================
    //*
    //*     .. Parameter ..
    double ZERO; //DOUBLE PRECISION   ZERO
    {  ZERO = 0.0D; } //PARAMETER          ( ZERO = 0.0D0 )
    //*     ..
    //*     .. Local Scalars ..
    int J4, J4P2; //INTEGER            J4, J4P2
    double D, EMIN, TEMP; //DOUBLE PRECISION   D, EMIN, TEMP
    //*     ..
    //*     .. Intrinsic Functions ..
    //INTRINSIC          MIN
    //*     ..
    //*     .. Executable Statements ..
    //*
    if ( ( N0-I0-1 ) <= 0 )   {
      DblRef.set(pDMIN, DMIN, pDMIN1, DMIN1, pDMIN2, DMIN2);
      DblRef.set(pDN, DN, pDNM1, DNM1, pDNM2, DNM2);
      return; //IF( ( N0-I0-1 ).LE.0 )RETURN
    }
    //*
    J4 = 4*I0 + PP - 3 - 1; //todo: note -1; //J4 = 4*I0 + PP - 3
    EMIN = Z[ J4+4 ]; //EMIN = Z( J4+4 )
    D = Z[ J4 ] - TAU; //D = Z( J4 ) - TAU
    DMIN = D; //DMIN = D
    DMIN1 = -Z[ J4 ]; //DMIN1 = -Z( J4 )
    //*
    if ( IEEE ) { //IF( IEEE ) THEN
      //*
      //*        Code for IEEE arithmetic.
      //*
      if ( PP == 0 ) { //IF( PP.EQ.0 ) THEN
        for (J4 = 4*I0 - 1; J4 < 4*( N0-3 ); J4 += 4) { //todo: note -1; //DO 10 J4 = 4*I0, 4*( N0-3 ), 4
          Z[ J4-2 ] = D + Z[ J4-1 ]; //Z( J4-2 ) = D + Z( J4-1 )
          TEMP = Z[ J4+1 ] / Z[ J4-2 ]; //TEMP = Z( J4+1 ) / Z( J4-2 )
          D = D*TEMP - TAU; //D = D*TEMP - TAU
          DMIN = MIN( DMIN, D ); //DMIN = MIN( DMIN, D )
          Z[ J4 ] = Z[ J4-1 ]*TEMP; //Z( J4 ) = Z( J4-1 )*TEMP
          EMIN = MIN( Z[ J4 ], EMIN ); //EMIN = MIN( Z( J4 ), EMIN )
        } //10       CONTINUE
      } else { // ELSE
        for (J4 = 4*I0 - 1; J4 < 4*( N0-3 ); J4 += 4) { //todo: note -1; //DO 20 J4 = 4*I0, 4*( N0-3 ), 4
          Z[ J4-3 ] = D + Z[ J4 ]; //Z( J4-3 ) = D + Z( J4 )
          TEMP = Z[ J4+2 ] / Z[ J4-3 ]; //TEMP = Z( J4+2 ) / Z( J4-3 )
          D = D*TEMP - TAU; //D = D*TEMP - TAU
          DMIN = MIN( DMIN, D ); //DMIN = MIN( DMIN, D )
          Z[ J4-1 ] = Z[ J4 ]*TEMP; //Z( J4-1 ) = Z( J4 )*TEMP
          EMIN = MIN( Z[ J4-1 ], EMIN ); //EMIN = MIN( Z( J4-1 ), EMIN )
        } //20       CONTINUE
      } // END IF
      //*
      //*        Unroll last two steps.
      //*
      DNM2 = D; //DNM2 = D
      DMIN2 = DMIN; //DMIN2 = DMIN
      J4 = 4*( N0-2 ) - PP - 1; //todo: note -1; //J4 = 4*( N0-2 ) - PP
      J4P2 = J4 + 2*PP - 1; //J4P2 = J4 + 2*PP - 1
      Z[ J4-2 ] = DNM2 + Z[ J4P2 ]; //Z( J4-2 ) = DNM2 + Z( J4P2 )
      Z[ J4 ] = Z[ J4P2+2 ]*( Z[ J4P2 ] / Z[ J4-2 ] ); //Z( J4 ) = Z( J4P2+2 )*( Z( J4P2 ) / Z( J4-2 ) )
      DNM1 = Z[ J4P2+2 ]*( DNM2 / Z[ J4-2 ] ) - TAU; //DNM1 = Z( J4P2+2 )*( DNM2 / Z( J4-2 ) ) - TAU
      DMIN = MIN( DMIN, DNM1 ); //DMIN = MIN( DMIN, DNM1 )
      //*
      DMIN1 = DMIN; //DMIN1 = DMIN
      J4 = J4 + 3; //J4 = J4 + 4
      J4P2 = J4 + 2*PP - 1; //J4P2 = J4 + 2*PP - 1
      Z[ J4-2 ] = DNM1 + Z[ J4P2 ]; //Z( J4-2 ) = DNM1 + Z( J4P2 )
      Z[ J4 ] = Z[ J4P2+2 ]*( Z[ J4P2 ] / Z[ J4-2 ] ); //Z( J4 ) = Z( J4P2+2 )*( Z( J4P2 ) / Z( J4-2 ) )
      DN = Z[ J4P2+2 ]*( DNM1 / Z[ J4-2 ] ) - TAU; //DN = Z( J4P2+2 )*( DNM1 / Z( J4-2 ) ) - TAU
      DMIN = MIN( DMIN, DN ); //DMIN = MIN( DMIN, DN )
      //*
    } else { // ELSE
      //*
      //*        Code for non IEEE arithmetic.
      //*
      if ( PP == 0 ) { //IF( PP.EQ.0 ) THEN
        for (J4 = 4*I0 - 1; J4 < 4*( N0-3 ); J4 += 4) { //todo: note -1; //DO 30 J4 = 4*I0, 4*( N0-3 ), 4
          Z[ J4-2 ] = D + Z[ J4-1 ]; //Z( J4-2 ) = D + Z( J4-1 )
          if ( D < ZERO ) { //IF( D.LT.ZERO ) THEN
            DblRef.set(pDMIN, DMIN, pDMIN1, DMIN1, pDMIN2, DMIN2);
            DblRef.set(pDN, DN, pDNM1, DNM1, pDNM2, DNM2);
            return; // RETURN
          } else { // ELSE
            Z[ J4 ] = Z[ J4+1 ]*( Z[ J4-1 ] / Z[ J4-2 ] ); //Z( J4 ) = Z( J4+1 )*( Z( J4-1 ) / Z( J4-2 ) )
            D = Z[ J4+1 ]*( D / Z[ J4-2 ] ) - TAU; //D = Z( J4+1 )*( D / Z( J4-2 ) ) - TAU
          } // END IF
          DMIN = MIN( DMIN, D ); //DMIN = MIN( DMIN, D )
          EMIN = MIN( EMIN, Z[ J4 ] ); //EMIN = MIN( EMIN, Z( J4 ) )
        }//30       CONTINUE
      } else { // ELSE
        for (J4 = 4*I0 - 1; J4 < 4*( N0-3 ); J4 += 4) { //todo: note -1; //DO 40 J4 = 4*I0, 4*( N0-3 ), 4
          Z[ J4-3 ] = D + Z[ J4 ]; //Z( J4-3 ) = D + Z( J4 )
          if ( D < ZERO ) { //IF( D.LT.ZERO ) THEN
            DblRef.set(pDMIN, DMIN, pDMIN1, DMIN1, pDMIN2, DMIN2);
            DblRef.set(pDN, DN, pDNM1, DNM1, pDNM2, DNM2);
            return; // RETURN
          } else { // ELSE
            Z[ J4-1 ] = Z[ J4+2 ]*( Z[ J4 ] / Z[ J4-3 ] ); //Z( J4-1 ) = Z( J4+2 )*( Z( J4 ) / Z( J4-3 ) )
            D = Z[ J4+2 ]*( D / Z[ J4-3 ] ) - TAU; //D = Z( J4+2 )*( D / Z( J4-3 ) ) - TAU
          } // END IF
          DMIN = MIN( DMIN, D ); //DMIN = MIN( DMIN, D )
          EMIN = MIN( EMIN, Z[ J4-1 ] ); //EMIN = MIN( EMIN, Z( J4-1 ) )
        } //40       CONTINUE
      } // END IF
      //*
      //*        Unroll last two steps.
      //*
      DNM2 = D; //DNM2 = D
      DMIN2 = DMIN; //DMIN2 = DMIN
      J4 = 4*( N0-2 ) - PP - 1; //todo: note -1; //J4 = 4*( N0-2 ) - PP
      J4P2 = J4 + 2*PP - 1; //J4P2 = J4 + 2*PP - 1
      Z[ J4-2 ] = DNM2 + Z[ J4P2 ]; //Z( J4-2 ) = DNM2 + Z( J4P2 )
      if ( DNM2 < ZERO ) { //IF( DNM2.LT.ZERO ) THEN
        DblRef.set(pDMIN, DMIN, pDMIN1, DMIN1, pDMIN2, DMIN2);
        DblRef.set(pDN, DN, pDNM1, DNM1, pDNM2, DNM2);
        return; // RETURN
      } else { // ELSE
        Z[ J4 ] = Z[ J4P2+2 ]*( Z[ J4P2 ] / Z[ J4-2 ] ); //Z( J4 ) = Z( J4P2+2 )*( Z( J4P2 ) / Z( J4-2 ) )
        DNM1 = Z[ J4P2+2 ]*( DNM2 / Z[ J4-2 ] ) - TAU; //DNM1 = Z( J4P2+2 )*( DNM2 / Z( J4-2 ) ) - TAU
      } // END IF
      DMIN = MIN( DMIN, DNM1 ); //DMIN = MIN( DMIN, DNM1 )
      //*
      DMIN1 = DMIN; //DMIN1 = DMIN
      J4 = J4 + 3; //J4 = J4 + 4
      J4P2 = J4 + 2*PP - 1; //J4P2 = J4 + 2*PP - 1
      Z[ J4-2 ] = DNM1 + Z[ J4P2 ]; //Z( J4-2 ) = DNM1 + Z( J4P2 )
      if ( DNM1 < ZERO ) { //IF( DNM1.LT.ZERO ) THEN
        DblRef.set(pDMIN, DMIN, pDMIN1, DMIN1, pDMIN2, DMIN2);
        DblRef.set(pDN, DN, pDNM1, DNM1, pDNM2, DNM2);
        return; // RETURN
      } else { // ELSE
        Z[ J4 ] = Z[ J4P2+2 ]*( Z[ J4P2 ] / Z[ J4-2 ] ); //Z( J4 ) = Z( J4P2+2 )*( Z( J4P2 ) / Z( J4-2 ) )
        DN = Z[ J4P2+2 ]*( DNM1 / Z[ J4-2 ] ) - TAU; //DN = Z( J4P2+2 )*( DNM1 / Z( J4-2 ) ) - TAU
      } // END IF
      DMIN = MIN( DMIN, DN ); //DMIN = MIN( DMIN, DN )
      //*
    } // END IF
    //*
    Z[ J4+1 ] = DN; //Z( J4+2 ) = DN
    Z[ 4*N0-PP - 1 ] = EMIN; //Z( 4*N0-PP ) = EMIN
    DblRef.set(pDMIN, DMIN, pDMIN1, DMIN1, pDMIN2, DMIN2);
    DblRef.set(pDN, DN, pDNM1, DNM1, pDNM2, DNM2);
  } // RETURN
  //*
  //*     End of DLASQ5
  //*
} // END