package lapack4j.src.java;
import lapack4j.utils.DblRef;
import lapack4j.utils.IntRef;
import lapack4j.utils.goto_labels.GO_TO_30;
import lapack4j.utils.goto_labels.GO_TO_40;
import lapack4j.utils.goto_labels.GO_TO_80;

import static lapack4j.utils.IntrFuncs.SQRT;
import static lapack4j.utils.IntrFuncs.MIN;
import static lapack4j.utils.IntrFuncs.MAX;
import static lapack4j.utils.IntrFuncs.ABS;
import static lapack4j.src.java.DLASQ4.DLASQ4;
import static lapack4j.src.java.DLASQ5.DLASQ5;
import static lapack4j.src.java.DLASQ6.DLASQ6;
import static lapack4j.src.java.DLAMCH.DLAMCH;
import static lapack4j.src.java.DISNAN.DISNAN;
import static lapack4j.utils.MathConsts.*;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,20/10/11,4:01 PM
 */
public class DLASQ3 { //SUBROUTINE DLASQ3( I0, N0, Z, PP, DMIN, SIGMA, DESIG, QMAX, NFAIL,ITER, NDIV, IEEE, TTYPE, DMIN1, DMIN2, DN, DN1,DN2, G, TAU )
public static void DLASQ3(
  final int I0   //I0     (input) INTEGER
  , final int[] N0     //N0     (input/output) INTEGER
  , final double[] Z   //Z      (input) DOUBLE PRECISION array, dimension ( 4*N )
  , final int[] PP     //PP     (input/output) INTEGER
  , final double[] DMIN   //DMIN   (output) DOUBLE PRECISION
  , final double[] SIGMA  //SIGMA  (output) DOUBLE PRECISION
  , final double[] DESIG  //DESIG  (input/output) DOUBLE PRECISION
  , final double[] QMAX   //QMAX   (input) DOUBLE PRECISION//todo: bug? QMAX value is changed here
  , final int[] NFAIL  //NFAIL  (output) INTEGER
  , final int[] ITER   //ITER   (output) INTEGER
  , final int[] NDIV   //NDIV   (output) INTEGER
  , final boolean IEEE   //IEEE   (input) LOGICAL
  , final int[] TTYPE   //TTYPE  (input/output) INTEGER
  , final double[] DMIN1   //DMIN1  (input/output) DOUBLE PRECISION
  , final double[] DMIN2   //DMIN2  (input/output) DOUBLE PRECISION
  , final double[] DN      //DN     (input/output) DOUBLE PRECISION
  , final double[] DN1     //DN1    (input/output) DOUBLE PRECISION
  , final double[] DN2     //DN2    (input/output) DOUBLE PRECISION
  , final double[] G       //G      (input/output) DOUBLE PRECISION
  , final double[] TAU     //TAU    (input/output) DOUBLE PRECISION
) { //SUBROUTINE DLASQ3( I0, N0, Z, PP, DMIN, SIGMA, DESIG, QMAX, NFAIL,ITER, NDIV, IEEE, TTYPE, DMIN1, DMIN2, DN, DN1,DN2, G, TAU )
  //*
  //*  -- LAPACK routine (version 3.2.2)                                    --
  //*
  //*  -- Contributed by Osni Marques of the Lawrence Berkeley National   --
  //*  -- Laboratory and Beresford Parlett of the Univ. of California at  --
  //*  -- Berkeley                                                        --
  //*  -- June 2010                                                       --
  //*
  //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
  //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
  //*
  //*     .. Scalar Arguments ..
  //    boolean IEEE; //IEEE   (input) LOGICAL//LOGICAL            IEEE
  ITER[0] = 0;  //ITER   (output) INTEGER
  NDIV[0] = 0;    //NDIV   (output) INTEGER
  NFAIL[0] = 0;  //NFAIL  (output) INTEGER
  //INTEGER            I0, ITER, N0, NDIV, NFAIL, PP
  DMIN[0] = 0; //DMIN   (output) DOUBLE PRECISION
  SIGMA[0] = 0;      //SIGMA  (output) DOUBLE PRECISION
  //DOUBLE PRECISION   DESIG, DMIN, DMIN1, DMIN2, DN, DN1, DN2, G,QMAX, SIGMA, TAU
  //*     ..
  //*     .. Array Arguments ..
  //    double Z[]; //DOUBLE PRECISION   Z( * )
  //*     ..
  //*
  //*  Purpose
  //*  =======
  //*
  //*  DLASQ3 checks for deflation, computes a shift (TAU) and calls dqds.
  //*  In case of failure it changes shifts, and tries again until output
  //*  is positive.
  //*
  //*  Arguments
  //*  =========
  //*
  //*  I0     (input) INTEGER
  //*         First index.
  //*
  //*  N0     (input/output) INTEGER
  //*         Last index.
  //*
  //*  Z      (input) DOUBLE PRECISION array, dimension ( 4*N )
  //*         Z holds the qd array.
  //*
  //*  PP     (input/output) INTEGER
  //*         PP=0 for ping, PP=1 for pong.
  //*         PP=2 indicates that flipping was applied to the Z array
  //*         and that the initial tests for deflation should not be
  //*         performed.
  //*
  //*  DMIN   (output) DOUBLE PRECISION
  //*         Minimum value of d.
  //*
  //*  SIGMA  (output) DOUBLE PRECISION
  //*         Sum of shifts used in current segment.
  //*
  //*  DESIG  (input/output) DOUBLE PRECISION
  //*         Lower order part of SIGMA
  //*
  //*  QMAX   (input) DOUBLE PRECISION
  //*         Maximum value of q.
  ////todo: bug? QMAX value is changed here
  //*
  //*  NFAIL  (output) INTEGER
  //*         Number of times shift was too big.
  //*
  //*  ITER   (output) INTEGER
  //*         Number of iterations.
  //*
  //*  NDIV   (output) INTEGER
  //*         Number of divisions.
  //*
  //*  IEEE   (input) LOGICAL
  //*         Flag for IEEE or non IEEE arithmetic (passed to DLASQ5).
  //*
  //*  TTYPE  (input/output) INTEGER
  //*         Shift type.
  //*
  //*  DMIN1  (input/output) DOUBLE PRECISION
  //*
  //*  DMIN2  (input/output) DOUBLE PRECISION
  //*
  //*  DN     (input/output) DOUBLE PRECISION
  //*
  //*  DN1    (input/output) DOUBLE PRECISION
  //*
  //*  DN2    (input/output) DOUBLE PRECISION
  //*
  //*  G      (input/output) DOUBLE PRECISION
  //*
  //*  TAU    (input/output) DOUBLE PRECISION
  //*
  //*         These are passed as arguments in order to save their values
  //*         between calls to DLASQ3.
  //*
  //*  =====================================================================
  //*
  //*     .. Parameters ..
  double CBIAS; //DOUBLE PRECISION   CBIAS
  {  CBIAS = 1.50D; } //PARAMETER          ( CBIAS = 1.50D0 )
//  double QURTR, HUNDRD; //DOUBLE PRECISION   ZERO, QURTR, HALF, ONE, TWO, HUNDRD
//  {  QURTR = 0.250D; HUNDRD = 100.0D; } //PARAMETER          ( ZERO = 0.0D0, QURTR = 0.250D0, HALF = 0.5D0,ONE = 1.0D0, TWO = 2.0D0, HUNDRD = 100.0D0 )
  //*     ..
  //*     .. Local Scalars ..
  int IPN4, J4, N0IN, NN;
  //, TTYPE; //INTEGER            IPN4, J4, N0IN, NN, TTYPE
  double EPS, S, T, TEMP, TOL, TOL2; //DOUBLE PRECISION   EPS, S, T, TEMP, TOL, TOL2
  //*     ..
  //*     .. External Subroutines ..
  //    EXTERNAL           DLASQ4, DLASQ5, DLASQ6; //EXTERNAL           DLASQ4, DLASQ5, DLASQ6
  //*     ..
  //*     .. External Function ..
  //    double DLAMCH; //DOUBLE PRECISION   DLAMCH
  //    boolean DISNAN; //LOGICAL            DISNAN
  //    EXTERNAL           DISNAN, DLAMCH; //EXTERNAL           DISNAN, DLAMCH
  //*     ..
  //*     .. Intrinsic Functions ..
  //INTRINSIC          ABS, MAX, MIN, SQRT
  //*     ..
  //*     .. Executable Statements ..
  //*
  N0IN = N0[0]; //N0IN = N0
  EPS = DLAMCH( 'P' ); //EPS = DLAMCH( 'Precision' )
  TOL = EPS*HUNDRD; //TOL = EPS*HUNDRD
  TOL2 = TOL*TOL; //TOL2 = TOL**2
  //*     Check for deflation.
  for (;;) { //10 CONTINUE
    if ( N0[0] < I0 )   {
      return; //IF( N0.LT.I0 )RETURN
    }
    NN = 4*N0[0] + PP[0]; //TODO: //Bug fix? duplicated to compile in java ;
    try {
      if ( N0[0] != I0 ) { //IF( N0.EQ.I0 )GO TO 20
        NN = 4*N0[0] + PP[0]; //NN = 4*N0 + PP
        if ( N0[0] == ( I0+1 ) )
          throw new GO_TO_40(); //IF( N0.EQ.( I0+1 ) )GO TO 40
        //*     Check whether E(N0-1) is negligible, 1 eigenvalue.
        if ( Z[ NN-5 -1] > TOL2*( SIGMA[0]+Z[ NN-3 -1] )  && Z[ NN-2*PP[0]-4 -1] > TOL2*Z[ NN-7 -1] )
          throw new GO_TO_30(); //IF( Z( NN-5 ).GT.TOL2*( SIGMA+Z( NN-3 ) ) .AND.Z( NN-2*PP-4 ).GT.TOL2*Z( NN-7 ) )GO TO 30
      } //20 CONTINUE
      Z[ 4*N0[0]-3 -1] = Z[ 4*N0[0]+PP[0]-3 -1] + SIGMA[0]; //Z( 4*N0-3 ) = Z( 4*N0+PP-3 ) + SIGMA
      N0[0] = N0[0] - 1; //N0 = N0 - 1
      continue; //GO TO 10
      //*     Check  whether E(N0-2) is negligible, 2 eigenvalues.
    } catch (GO_TO_30 e) { //30 CONTINUE
      if ( Z[ NN-9 -1] > TOL2*SIGMA[0]  && Z[ NN-2*PP[0]-8 -1] > TOL2*Z[ NN-11 -1] )
        break; //IF( Z( NN-9 ).GT.TOL2*SIGMA .AND.Z( NN-2*PP-8 ).GT.TOL2*Z( NN-11 ) )GO TO 50
    } catch (GO_TO_40 e) {} //40 CONTINUE
    if ( Z[ NN-3 -1] > Z[ NN-7 -1] ) { //IF( Z( NN-3 ).GT.Z( NN-7 ) ) THEN
      S = Z[ NN-3 -1]; //S = Z( NN-3 )
      Z[ NN-3 -1] = Z[ NN-7 -1]; //Z( NN-3 ) = Z( NN-7 )
      Z[ NN-7 -1] = S; //Z( NN-7 ) = S
    } // END IF
    if ( Z[ NN-5 -1] > Z[ NN-3 -1]*TOL2 ) { //IF( Z( NN-5 ).GT.Z( NN-3 )*TOL2 ) THEN
      T = HALF*( ( Z[ NN-7 -1]-Z[ NN-3 -1] )+Z[ NN-5 -1] ); //T = HALF*( ( Z( NN-7 )-Z( NN-3 ) )+Z( NN-5 ) )
      S = Z[ NN-3 -1]*( Z[ NN-5 -1] / T ); //S = Z( NN-3 )*( Z( NN-5 ) / T )
      if ( S <= T ) { //IF( S.LE.T ) THEN
        S = Z[ NN-3 -1]*( Z[ NN-5 -1] /( T*( ONE+SQRT( ONE+S / T ) ) ) );
        //S = Z( NN-3 )*( Z( NN-5 ) /( T*( ONE+SQRT( ONE+S / T ) ) ) )
      } else { // ELSE
        S = Z[ NN-3 -1]*( Z[ NN-5 -1] / ( T+SQRT( T )*SQRT( T+S ) ) );
        //S = Z( NN-3 )*( Z( NN-5 ) / ( T+SQRT( T )*SQRT( T+S ) ) )
      } // END IF
      T = Z[ NN-7 -1] + ( S+Z[ NN-5 -1] ); //T = Z( NN-7 ) + ( S+Z( NN-5 ) )
      Z[ NN-3 -1] = Z[ NN-3 -1]*( Z[ NN-7 -1] / T ); //Z( NN-3 ) = Z( NN-3 )*( Z( NN-7 ) / T )
      Z[ NN-7 -1] = T; //Z( NN-7 ) = T
    } // END IF
    Z[ 4*N0[0]-7 -1] = Z[ NN-7 -1] + SIGMA[0]; //Z( 4*N0-7 ) = Z( NN-7 ) + SIGMA
    Z[ 4*N0[0]-3 -1] = Z[ NN-3 -1] + SIGMA[0]; //Z( 4*N0-3 ) = Z( NN-3 ) + SIGMA
    N0[0] = N0[0] - 2;//N0 = N0 - 2
  }//GO TO 10
  //50 CONTINUE
  if ( PP[0] == 2 )  {
    PP[0] = 0; //IF( PP.EQ.2 ) PP = 0
  }
  //*     Reverse the qd-array, if warranted.
  if ( DMIN[0] <= ZERO  ||  N0[0] < N0IN ) { //IF( DMIN.LE.ZERO .OR. N0.LT.N0IN ) THEN
    if ( CBIAS*Z[ 4*I0+PP[0]-3 -1] < Z[ 4*N0[0]+PP[0]-3 -1] ) { //IF( CBIAS*Z( 4*I0+PP-3 ).LT.Z( 4*N0+PP-3 ) ) THEN
      IPN4 = 4*( I0+N0[0] ) - 1; //IPN4 = 4*( I0+N0 )
      for (J4 = 4*I0; J4 <= 2*( I0+N0[0]-1 ); J4 += 4) { //DO 60 J4 = 4*I0, 2*( I0+N0-1 ), 4
        TEMP = Z[ J4-3 -1]; //TEMP = Z( J4-3 )
        Z[ J4-3 -1] = Z[ IPN4-J4-3 -1]; //Z( J4-3 ) = Z( IPN4-J4-3 )
        Z[ IPN4-J4-3 -1] = TEMP; //Z( IPN4-J4-3 ) = TEMP
        TEMP = Z[ J4-2 -1]; //TEMP = Z( J4-2 )
        Z[ J4-2 -1] = Z[ IPN4-J4-2 -1]; //Z( J4-2 ) = Z( IPN4-J4-2 )
        Z[ IPN4-J4-2 -1] = TEMP; //Z( IPN4-J4-2 ) = TEMP
        TEMP = Z[ J4-1 ]; //TEMP = Z( J4-1 )
        Z[ J4-1 -1] = Z[ IPN4-J4-5 -1]; //Z( J4-1 ) = Z( IPN4-J4-5 )
        Z[ IPN4-J4-5 -1] = TEMP; //Z( IPN4-J4-5 ) = TEMP
        TEMP = Z[ J4 -1]; //TEMP = Z( J4 )
        Z[ J4 -1] = Z[ IPN4-J4-4 -1]; //Z( J4 ) = Z( IPN4-J4-4 )
        Z[ IPN4-J4-4 -1] = TEMP; //Z( IPN4-J4-4 ) = TEMP
      } //60       CONTINUE
      if ( N0[0]-I0 <= 4 ) { //IF( N0-I0.LE.4 ) THEN
        Z[ 4*N0[0]+PP[0]-1 -1] = Z[ 4*I0+PP[0]-1 -1]; //Z( 4*N0+PP-1 ) = Z( 4*I0+PP-1 )
        Z[ 4*N0[0]-PP[0] -1] = Z[ 4*I0-PP[0] -1]; //Z( 4*N0-PP ) = Z( 4*I0-PP )
      } // END IF
      DMIN2[0] = MIN( DMIN2[0], Z[ 4*N0[0]+PP[0]-1 -1] ); //DMIN2 = MIN( DMIN2, Z( 4*N0+PP-1 ) )
      Z[ 4*N0[0]+PP[0]-1 -1] = MIN( Z[ 4*N0[0]+PP[0]-1 -1]
        , Z[ 4*I0+PP[0]-1 -1]
        , Z[ 4*I0+PP[0]+3 -1] ); //Z( 4*N0+PP-1 ) = MIN( Z( 4*N0+PP-1 ), Z( 4*I0+PP-1 ),Z( 4*I0+PP+3 ) )
      Z[ 4*N0[0]-PP[0] -1] = MIN( Z[ 4*N0[0]-PP[0] -1]
        , Z[ 4*I0-PP[0] -1]
        , Z[ 4*I0-PP[0]+4 -1] ); //Z( 4*N0-PP ) = MIN( Z( 4*N0-PP ), Z( 4*I0-PP ),Z( 4*I0-PP+4 ) )
      QMAX[0] = MAX( QMAX[0]
        , Z[ 4*I0+PP[0]-3 -1]
        , Z[ 4*I0+PP[0]+1 -1] ); //QMAX = MAX( QMAX, Z( 4*I0+PP-3 ), Z( 4*I0+PP+1 ) )
      DMIN[0] = -ZERO; //DMIN = -ZERO
    } // END IF
  } // END IF
  //*     Choose a shift.
  DLASQ4(I0, N0, Z, PP, N0IN, DMIN, DMIN1, DMIN2, DN, DN1, DN2
    , TAU, TTYPE, G); //CALL DLASQ4( I0, N0, Z, PP, N0IN, DMIN, DMIN1, DMIN2, DN, DN1,DN2, TAU, TTYPE, G )
  //*     Call dqds until DMIN > 0.
  for (;;) { //70 CONTINUE
    DLASQ5(I0, N0, Z, PP, TAU, DMIN, DMIN1, DMIN2, DN, DN1, DN2
      , IEEE); //CALL DLASQ5( I0, N0, Z, PP, TAU, DMIN, DMIN1, DMIN2, DN,DN1, DN2, IEEE )
    NDIV[0] = NDIV[0] + ( N0[0]-I0+2 );  //NDIV = NDIV + ( N0-I0+2 )
    ITER[0] = ITER[0] + 1;   //ITER = ITER + 1
    //*     Check status.
    try {   // for GO_TO_80
      if ( DMIN[0] >= ZERO  &&  DMIN1[0] > ZERO ) { //IF( DMIN.GE.ZERO .AND. DMIN1.GT.ZERO ) THEN
        //*        Success.
        break; //GO TO 90
      } else if ( DMIN[0] < ZERO  &&  DMIN1[0] > ZERO
        &&  Z[ 4*( N0[0]-1 )-PP[0] - 1] < TOL*( SIGMA[0]+DN1[0] )
        && ABS( DN[0] ) < TOL*SIGMA[0] ) {
        //ELSE IF( DMIN.LT.ZERO .AND. DMIN1.GT.ZERO .AND. Z( 4*( N0-1 )-PP ).LT.TOL*( SIGMA+DN1 ) .AND.ABS( DN ).LT.TOL*SIGMA ) THEN
        //*        Convergence hidden by negative DN.
        Z[ 4*( N0[0]-1 )-PP[0]+2 -1] = ZERO; //Z( 4*( N0-1 )-PP+2 ) = ZERO
        DMIN[0] = ZERO; //DMIN = ZERO
        break; //GO TO 90
      } else if ( DMIN[0] < ZERO ) { //ELSE IF( DMIN.LT.ZERO ) THEN
        //*        TAU too big. Select new TAU and try again.
        NFAIL[0] = NFAIL[0] + 1; //NFAIL = NFAIL + 1
        if ( TTYPE[0] < -22 ) { //IF( TTYPE.LT.-22 ) THEN
          //*           Failed twice. Play it safe.
          TAU[0] = ZERO; //TAU = ZERO
        } else if ( DMIN1[0] > ZERO ) { //ELSE IF( DMIN1.GT.ZERO ) THEN
          //*           Late failure. Gives excellent shift.
          TAU[0] = ( TAU[0]+DMIN[0] )*( ONE-TWO*EPS ); //TAU = ( TAU+DMIN )*( ONE-TWO*EPS )
          TTYPE[0] = TTYPE[0] - 11; //TTYPE = TTYPE - 11
        } else { // ELSE
          //*           Early failure. Divide by 4.
          TAU[0] = QURTR*TAU[0]; //TAU = QURTR*TAU
          TTYPE[0] = TTYPE[0] - 12; //TTYPE = TTYPE - 12
        } // END IF
        continue; //GO TO 70
      } else if ( DISNAN( DMIN[0] ) ) { //ELSE IF( DISNAN( DMIN ) ) THEN
        //*        NaN.
        if ( TAU[0] == ZERO ) { //IF( TAU.EQ.ZERO ) THEN
          throw new GO_TO_80(); //GO TO 80
        } else { // ELSE
          TAU[0] = ZERO; //TAU = ZERO
          continue; //GO TO 70
        } // END IF
      } else { // ELSE
        //*        Possible underflow. Play it safe.
        throw new GO_TO_80(); //GO TO 80
      } // END IF
      //*     Risk of underflow.
    } catch (GO_TO_80 e) {} //80 CONTINUE
    DLASQ6( I0, N0, Z, PP
      , DMIN, DMIN1, DMIN2, DN, DN1, DN2 ); //CALL DLASQ6( I0, N0, Z, PP, DMIN, DMIN1, DMIN2, DN, DN1, DN2 )
    NDIV[0] = NDIV[0] + ( N0[0]-I0+2 ); //NDIV = NDIV + ( N0-I0+2 )
    ITER[0] = ITER[0] + 1; //ITER = ITER + 1
    TAU[0] = ZERO; //TAU = ZERO
  } //90 CONTINUE
  if ( TAU[0] < SIGMA[0] ) { //IF( TAU.LT.SIGMA ) THEN
    DESIG[0] = DESIG[0] + TAU[0]; //DESIG = DESIG + TAU
    T = SIGMA[0] + DESIG[0]; //T = SIGMA + DESIG
    DESIG[0] = DESIG[0] - ( T-SIGMA[0] ); //DESIG = DESIG - ( T-SIGMA )
  } else { // ELSE
    T = SIGMA[0] + TAU[0]; //T = SIGMA + TAU
    DESIG[0] = SIGMA[0] - ( T-TAU[0] ) + DESIG[0]; //DESIG = SIGMA - ( T-TAU ) + DESIG
  } // END IF
  SIGMA[0] = T; //SIGMA = T
} // RETURN
//*     End of DLASQ3
} // END