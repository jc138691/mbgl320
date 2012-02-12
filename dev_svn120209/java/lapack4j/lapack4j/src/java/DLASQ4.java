package lapack4j.src.java;
import lapack4j.utils.DblRef;
import lapack4j.utils.IntRef;
import static lapack4j.utils.IntrFuncs.MIN;
import static lapack4j.utils.IntrFuncs.MAX;
import static lapack4j.utils.IntrFuncs.SQRT;

/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,20/10/11,3:19 PM
 */
public class DLASQ4 { //SUBROUTINE DLASQ4( I0, N0, Z, PP, N0IN, DMIN, DMIN1, DMIN2, DN,DN1, DN2, TAU, TTYPE, G )
  public static void DLASQ4(final int  I0, final int N0, final double[] Z
    , final int PP, final int N0IN
    , final double DMIN, final double DMIN1, final double DMIN2
    , final double DN, final double DN1, final double DN2
    , DblRef pTAU, IntRef pTTYPE, DblRef pG ) { //SUBROUTINE DLASQ4( I0, N0, Z, PP, N0IN, DMIN, DMIN1, DMIN2, DN,DN1, DN2, TAU, TTYPE, G )
    //*
    //*  -- LAPACK routine (version 3.3.1)                                    --
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
    int TTYPE = pTTYPE.get(); //todo: BUG? //INTEGER            I0, N0, N0IN, PP, TTYPE
    double G = pG.get(), TAU = 0; //DOUBLE PRECISION   DMIN, DMIN1, DMIN2, DN, DN1, DN2, G, TAU
    //*     ..
    //*     .. Array Arguments ..
    //    double Z[]; //DOUBLE PRECISION   Z( * )
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  DLASQ4 computes an approximation TAU to the smallest eigenvalue
    //*  using values of d from the previous transform.
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
    //*        Z holds the qd array.
    //*
    //*  PP    (input) INTEGER
    //*        PP=0 for ping, PP=1 for pong.
    //*
    //*  NOIN  (input) INTEGER
    //*        The value of N0 at start of EIGTEST.
    //*
    //*  DMIN  (input) DOUBLE PRECISION
    //*        Minimum value of d.
    //*
    //*  DMIN1 (input) DOUBLE PRECISION
    //*        Minimum value of d, excluding D( N0 ).
    //*
    //*  DMIN2 (input) DOUBLE PRECISION
    //*        Minimum value of d, excluding D( N0 ) and D( N0-1 ).
    //*
    //*  DN    (input) DOUBLE PRECISION
    //*        d(N)
    //*
    //*  DN1   (input) DOUBLE PRECISION
    //*        d(N-1)
    //*
    //*  DN2   (input) DOUBLE PRECISION
    //*        d(N-2)
    //*
    //*  TAU   (output) DOUBLE PRECISION
    //*        This is the shift.
    //*
    //*  TTYPE (output) INTEGER
    //*        Shift type.
    //*
    //*  G     (input/output) REAL
    //*        G is passed as an argument in order to save its value between
    //*        calls to DLASQ4.
    //*
    //*  Further Details
    //*  ===============
    //*  CNST1 = 9/16
    //*
    //*  =====================================================================
    //*
    //*     .. Parameters ..
    double CNST1, CNST2, CNST3; //DOUBLE PRECISION   CNST1, CNST2, CNST3
    {  CNST1 = 0.5630D; CNST2 = 1.010D; CNST3 = 1.050D; } //PARAMETER          ( CNST1 = 0.5630D0, CNST2 = 1.010D0,CNST3 = 1.050D0 )
    double QURTR, THIRD, HALF, ZERO, ONE, TWO, HUNDRD; //DOUBLE PRECISION   QURTR, THIRD, HALF, ZERO, ONE, TWO, HUNDRD
    {  QURTR = 0.250D; THIRD = 0.3330D; HALF = 0.50D; ZERO = 0.0D; ONE = 1.0D; TWO = 2.0D; HUNDRD = 100.0D; } //PARAMETER          ( QURTR = 0.250D0, THIRD = 0.3330D0,HALF = 0.50D0, ZERO = 0.0D0, ONE = 1.0D0,TWO = 2.0D0, HUNDRD = 100.0D0 )
    //*     ..
    //*     .. Local Scalars ..
    int I4, NN, NP; //INTEGER            I4, NN, NP
    double A2, B1, B2, GAM, GAP1, GAP2, S = 0; //DOUBLE PRECISION   A2, B1, B2, GAM, GAP1, GAP2, S
    //*     ..
    //*     .. Intrinsic Functions ..
    //INTRINSIC          MAX, MIN, SQRT
    //*     ..
    //*     .. Executable Statements ..
    //*
    //*     A negative DMIN forces the shift to take that absolute value
    //*     TTYPE records the type of shift.
    //*
    if ( DMIN <= ZERO ) { //IF( DMIN.LE.ZERO ) THEN
      TAU = -DMIN; //TAU = -DMIN
      TTYPE = -1; //TTYPE = -1
      pTAU.set(TAU);
      pTTYPE.set(TTYPE);
      return; // RETURN
    } // END IF
    //*
    NN = 4*N0 + PP - 1; //todo: note -1; //NN = 4*N0 + PP
    if ( N0IN == N0 ) { //IF( N0IN.EQ.N0 ) THEN
      //*
      //*        No eigenvalues deflated.
      //*
      if ( DMIN == DN  ||  DMIN == DN1 ) { //IF( DMIN.EQ.DN .OR. DMIN.EQ.DN1 ) THEN
        //*
        B1 = SQRT( Z[ NN-3 ] )*SQRT( Z[ NN-5 ] ); //B1 = SQRT( Z( NN-3 ) )*SQRT( Z( NN-5 ) )
        B2 = SQRT( Z[ NN-7 ] )*SQRT( Z[ NN-9 ] ); //B2 = SQRT( Z( NN-7 ) )*SQRT( Z( NN-9 ) )
        A2 = Z[ NN-7 ] + Z[ NN-5 ]; //A2 = Z( NN-7 ) + Z( NN-5 )
        //*
        //*           Cases 2 and 3.
        //*
        if ( DMIN == DN  &&  DMIN1 == DN1 ) { //IF( DMIN.EQ.DN .AND. DMIN1.EQ.DN1 ) THEN
          GAP2 = DMIN2 - A2 - DMIN2*QURTR; //GAP2 = DMIN2 - A2 - DMIN2*QURTR
          if ( GAP2 > ZERO  &&  GAP2 > B2 ) { //IF( GAP2.GT.ZERO .AND. GAP2.GT.B2 ) THEN
            GAP1 = A2 - DN - ( B2 / GAP2 )*B2; //GAP1 = A2 - DN - ( B2 / GAP2 )*B2
          } else { // ELSE
            GAP1 = A2 - DN - ( B1+B2 ); //GAP1 = A2 - DN - ( B1+B2 )
          } // END IF
          if ( GAP1 > ZERO  &&  GAP1 > B1 ) { //IF( GAP1.GT.ZERO .AND. GAP1.GT.B1 ) THEN
            S = MAX( DN-( B1 / GAP1 )*B1, HALF*DMIN ); //S = MAX( DN-( B1 / GAP1 )*B1, HALF*DMIN )
            TTYPE = -2; //TTYPE = -2
          } else { // ELSE
            S = ZERO; //S = ZERO
            if ( DN > B1 )S = DN - B1; //IF( DN.GT.B1 )S = DN - B1
            if ( A2 > ( B1+B2 ) )S = MIN( S, A2-( B1+B2 ) ); //IF( A2.GT.( B1+B2 ) )S = MIN( S, A2-( B1+B2 ) )
            S = MAX( S, THIRD*DMIN ); //S = MAX( S, THIRD*DMIN )
            TTYPE = -3; //TTYPE = -3
          } // END IF
        } else { // ELSE
          //*
          //*              Case 4.
          //*
          TTYPE = -4; //TTYPE = -4
          S = QURTR*DMIN; //S = QURTR*DMIN
          if ( DMIN == DN ) { //IF( DMIN.EQ.DN ) THEN
            GAM = DN; //GAM = DN
            A2 = ZERO; //A2 = ZERO
            if ( Z[ NN-5 ]  >  Z[ NN-7 ] ) {
              pTAU.set(TAU);
              pTTYPE.set(TTYPE);
              return;
            } //IF( Z( NN-5 ) .GT. Z( NN-7 ) )RETURN
            B2 = Z[ NN-5 ] / Z[ NN-7 ]; //B2 = Z( NN-5 ) / Z( NN-7 )
            NP = NN - 9; //NP = NN - 9
          } else { // ELSE
            NP = NN - 2*PP; //NP = NN - 2*PP
            B2 = Z[ NP-2 ]; //B2 = Z( NP-2 )
            GAM = DN1; //GAM = DN1
            if ( Z[ NP-4 ]  >  Z[ NP-2 ] ) {
              pTAU.set(TAU);
              pTTYPE.set(TTYPE);
              return;
            } //IF( Z( NP-4 ) .GT. Z( NP-2 ) )RETURN
            A2 = Z[ NP-4 ] / Z[ NP-2 ]; //A2 = Z( NP-4 ) / Z( NP-2 )
            if ( Z[ NN-9 ]  >  Z[ NN-11 ] ) {
              pTAU.set(TAU);
              pTTYPE.set(TTYPE);
              return;
            }//IF( Z( NN-9 ) .GT. Z( NN-11 ) )RETURN
            B2 = Z[ NN-9 ] / Z[ NN-11 ]; //B2 = Z( NN-9 ) / Z( NN-11 )
            NP = NN - 13; //NP = NN - 13
          } // END IF
          //*
          //*              Approximate contribution to norm squared from I < NN-1.
          //*
          A2 = A2 + B2; //A2 = A2 + B2
          for (I4 = NP; I4 > 4*I0 - 1 + PP; I4 -= 4) { //DO 10 I4 = NP, 4*I0 - 1 + PP, -4
            if ( B2 == ZERO )
              break; //IF( B2.EQ.ZERO )GO TO 20
            B1 = B2; //B1 = B2
            if ( Z[ I4 ]  >  Z[ I4-2 ] ) {
              pTAU.set(TAU);
              pTTYPE.set(TTYPE);
              return;
            } //IF( Z( I4 ) .GT. Z( I4-2 ) )RETURN
            B2 = B2*( Z[ I4 ] / Z[ I4-2 ] ); //B2 = B2*( Z( I4 ) / Z( I4-2 ) )
            A2 = A2 + B2; //A2 = A2 + B2
            if ( HUNDRD*MAX( B2, B1 ) < A2  ||  CNST1 < A2 )
              break; //IF( HUNDRD*MAX( B2, B1 ).LT.A2 .OR. CNST1.LT.A2 ) GO TO 20
          } //10          CONTINUE
          //20          CONTINUE
          A2 = CNST3*A2; //A2 = CNST3*A2
          //*
          //*              Rayleigh quotient residual bound.
          //*
          if ( A2 < CNST1 )S = GAM*( ONE-SQRT( A2 ) ) / ( ONE+A2 ); //IF( A2.LT.CNST1 )S = GAM*( ONE-SQRT( A2 ) ) / ( ONE+A2 )
        } // END IF
      } else if ( DMIN == DN2 ) { //ELSE IF( DMIN.EQ.DN2 ) THEN
        //*
        //*           Case 5.
        //*
        TTYPE = -5; //TTYPE = -5
        S = QURTR*DMIN; //S = QURTR*DMIN
        //*
        //*           Compute contribution to norm squared from I > NN-2.
        //*
        NP = NN - 2*PP; //NP = NN - 2*PP
        B1 = Z[ NP-2 ]; //B1 = Z( NP-2 )
        B2 = Z[ NP-6 ]; //B2 = Z( NP-6 )
        GAM = DN2; //GAM = DN2
        if ( Z[ NP-8 ] > B2  ||  Z[ NP-4 ] > B1 ) {
          pTAU.set(TAU);
          pTTYPE.set(TTYPE);
          return; //IF( Z( NP-8 ).GT.B2 .OR. Z( NP-4 ).GT.B1 )RETURN
        }
        A2 = ( Z[ NP-8 ] / B2 )*( ONE+Z[ NP-4 ] / B1 ); //A2 = ( Z( NP-8 ) / B2 )*( ONE+Z( NP-4 ) / B1 )
        //*
        //*           Approximate contribution to norm squared from I < NN-2.
        //*
        if ( N0-I0 > 2 ) { //IF( N0-I0.GT.2 ) THEN
          B2 = Z[ NN-13 ] / Z[ NN-15 ]; //B2 = Z( NN-13 ) / Z( NN-15 )
          A2 = A2 + B2; //A2 = A2 + B2
          for (I4 = NN - 17; I4 > 4*I0 - 1 + PP; I4 -= 4) { //DO 30 I4 = NN - 17, 4*I0 - 1 + PP, -4
            if ( B2 == ZERO )
              break; //IF( B2.EQ.ZERO )GO TO 40
            B1 = B2; //B1 = B2
            if ( Z[ I4 ]  >  Z[ I4-2 ] ) {
              pTAU.set(TAU);
              pTTYPE.set(TTYPE);
              return; //IF( Z( I4 ) .GT. Z( I4-2 ) )RETURN
            }
            B2 = B2*( Z[ I4 ] / Z[ I4-2 ] ); //B2 = B2*( Z( I4 ) / Z( I4-2 ) )
            A2 = A2 + B2; //A2 = A2 + B2
            if ( HUNDRD*MAX( B2, B1 ) < A2  ||  CNST1 < A2 )
              break; //IF( HUNDRD*MAX( B2, B1 ).LT.A2 .OR. CNST1.LT.A2 ) GO TO 40
          }//30          CONTINUE
          //40          CONTINUE
          A2 = CNST3*A2; //A2 = CNST3*A2
        } // END IF
        //*
        if ( A2 < CNST1 )S = GAM*( ONE-SQRT( A2 ) ) / ( ONE+A2 ); //IF( A2.LT.CNST1 )S = GAM*( ONE-SQRT( A2 ) ) / ( ONE+A2 )
      } else { // ELSE
        //*
        //*           Case 6, no information to guide us.
        //*
        // todo: BUG? TTYPE may not be set in the original code
        if ( TTYPE == -6 ) { //IF( TTYPE.EQ.-6 ) THEN
          G = G + THIRD*( ONE-G ); //G = G + THIRD*( ONE-G )
        } else if ( TTYPE == -18 ) { //ELSE IF( TTYPE.EQ.-18 ) THEN
          G = QURTR*THIRD; //G = QURTR*THIRD
        } else { // ELSE
          G = QURTR; //G = QURTR
        } // END IF
        S = G*DMIN; //S = G*DMIN
        TTYPE = -6; //TTYPE = -6
      } // END IF
      //*
    } else if ( N0IN == ( N0+1 ) ) { //ELSE IF( N0IN.EQ.( N0+1 ) ) THEN
      //*
      //*        One eigenvalue just deflated. Use DMIN1, DN1 for DMIN and DN.
      //*
      if ( DMIN1 == DN1  &&  DMIN2 == DN2 ) { //IF( DMIN1.EQ.DN1 .AND. DMIN2.EQ.DN2 ) THEN
        //*
        //*           Cases 7 and 8.
        //*
        TTYPE = -7; //TTYPE = -7
        S = THIRD*DMIN1; //S = THIRD*DMIN1
        if ( Z[ NN-5 ] > Z[ NN-7 ] ) {
          DblRef.set(pTAU, TAU, pG, G);
          pTTYPE.set(TTYPE);
          return; //IF( Z( NN-5 ).GT.Z( NN-7 ) )RETURN
        }
        B1 = Z[ NN-5 ] / Z[ NN-7 ]; //B1 = Z( NN-5 ) / Z( NN-7 )
        B2 = B1; //B2 = B1
        if ( B2 != ZERO ) { //IF( B2.EQ.ZERO )GO TO 60
          for (I4 = 4*N0 - 9 + PP - 1; I4 > 4*I0 - 1 + PP; I4 -= 4) { //todo: note -1; //DO 50 I4 = 4*N0 - 9 + PP, 4*I0 - 1 + PP, -4
            A2 = B1; //A2 = B1
            if ( Z[ I4 ] > Z[ I4-2 ] ) {
              DblRef.set(pTAU, TAU, pG, G);
              pTTYPE.set(TTYPE);
              return; //IF( Z( I4 ).GT.Z( I4-2 ) )RETURN
            }
            B1 = B1*( Z[ I4 ] / Z[ I4-2 ] ); //B1 = B1*( Z( I4 ) / Z( I4-2 ) )
            B2 = B2 + B1; //B2 = B2 + B1
            if ( HUNDRD*MAX( B1, A2 ) < B2 )
              break; //IF( HUNDRD*MAX( B1, A2 ).LT.B2 ) GO TO 60
          } //50       CONTINUE
        }//60       CONTINUE
        B2 = SQRT( CNST3*B2 ); //B2 = SQRT( CNST3*B2 )
        A2 = DMIN1 / ( ONE+B2*B2 ); //A2 = DMIN1 / ( ONE+B2**2 )
        GAP2 = HALF*DMIN2 - A2; //GAP2 = HALF*DMIN2 - A2
        if ( GAP2 > ZERO  &&  GAP2 > B2*A2 ) { //IF( GAP2.GT.ZERO .AND. GAP2.GT.B2*A2 ) THEN
          S = MAX( S, A2*( ONE-CNST2*A2*( B2 / GAP2 )*B2 ) ); //S = MAX( S, A2*( ONE-CNST2*A2*( B2 / GAP2 )*B2 ) )
        } else { // ELSE
          S = MAX( S, A2*( ONE-CNST2*B2 ) ); //S = MAX( S, A2*( ONE-CNST2*B2 ) )
          TTYPE = -8; //TTYPE = -8
        } // END IF
      } else { // ELSE
        //*
        //*           Case 9.
        //*
        S = QURTR*DMIN1; //S = QURTR*DMIN1
        if ( DMIN1 == DN1 )
          S = HALF*DMIN1; //IF( DMIN1.EQ.DN1 )S = HALF*DMIN1
        TTYPE = -9; //TTYPE = -9
      } // END IF
      //*
    } else if ( N0IN == ( N0+2 ) ) { //ELSE IF( N0IN.EQ.( N0+2 ) ) THEN
      //*
      //*        Two eigenvalues deflated. Use DMIN2, DN2 for DMIN and DN.
      //*
      //*        Cases 10 and 11.
      //*
      if ( DMIN2 == DN2  &&  TWO*Z[ NN-5 ] < Z[ NN-7 ] ) { //IF( DMIN2.EQ.DN2 .AND. TWO*Z( NN-5 ).LT.Z( NN-7 ) ) THEN
        TTYPE = -10; //TTYPE = -10
        S = THIRD*DMIN2; //S = THIRD*DMIN2
        if ( Z[ NN-5 ] > Z[ NN-7 ] ) {
          DblRef.set(pTAU, TAU, pG, G);
          pTTYPE.set(TTYPE);
          return; //IF( Z( NN-5 ).GT.Z( NN-7 ) )RETURN
        }
        B1 = Z[ NN-5 ] / Z[ NN-7 ]; //B1 = Z( NN-5 ) / Z( NN-7 )
        B2 = B1; //B2 = B1
        if ( B2 != ZERO ) { //IF( B2.EQ.ZERO )GO TO 80
          for (I4 = 4*N0 - 9 + PP - 1; I4 > 4*I0 - 1 + PP; I4 -= 4) { //todo: note -1; //DO 70 I4 = 4*N0 - 9 + PP, 4*I0 - 1 + PP, -4
            if ( Z[ I4 ] > Z[ I4-2 ] ) {
              DblRef.set(pTAU, TAU, pG, G);
              pTTYPE.set(TTYPE);
              return; //IF( Z( I4 ).GT.Z( I4-2 ) )RETURN
            }
            B1 = B1*( Z[ I4 ] / Z[ I4-2 ] ); //B1 = B1*( Z( I4 ) / Z( I4-2 ) )
            B2 = B2 + B1; //B2 = B2 + B1
            if ( HUNDRD*B1 < B2 )
              break; //IF( HUNDRD*B1.LT.B2 )GO TO 80
          } //70       CONTINUE
        }//80       CONTINUE
        B2 = SQRT( CNST3*B2 ); //B2 = SQRT( CNST3*B2 )
        A2 = DMIN2 / ( ONE+B2*B2 ); //A2 = DMIN2 / ( ONE+B2**2 )
        GAP2 = Z[ NN-7 ] + Z[ NN-9 ] -SQRT( Z[ NN-11 ] )*SQRT( Z[ NN-9 ] ) - A2; //GAP2 = Z( NN-7 ) + Z( NN-9 ) -SQRT( Z( NN-11 ) )*SQRT( Z( NN-9 ) ) - A2
        if ( GAP2 > ZERO  &&  GAP2 > B2*A2 ) { //IF( GAP2.GT.ZERO .AND. GAP2.GT.B2*A2 ) THEN
          S = MAX( S, A2*( ONE-CNST2*A2*( B2 / GAP2 )*B2 ) ); //S = MAX( S, A2*( ONE-CNST2*A2*( B2 / GAP2 )*B2 ) )
        } else { // ELSE
          S = MAX( S, A2*( ONE-CNST2*B2 ) ); //S = MAX( S, A2*( ONE-CNST2*B2 ) )
        } // END IF
      } else { // ELSE
        S = QURTR*DMIN2; //S = QURTR*DMIN2
        TTYPE = -11; //TTYPE = -11
      } // END IF
    } else if ( N0IN > ( N0+2 ) ) { //ELSE IF( N0IN.GT.( N0+2 ) ) THEN
      //*
      //*        Case 12, more than two eigenvalues deflated. No information.
      //*
      S = ZERO; //S = ZERO
      TTYPE = -12; //TTYPE = -12
    } // END IF
    //*
    TAU = S; //TAU = S
    DblRef.set(pTAU, TAU, pG, G);
    pTTYPE.set(TTYPE);
  } // RETURN
  //*
  //*     End of DLASQ4
  //*
} // END