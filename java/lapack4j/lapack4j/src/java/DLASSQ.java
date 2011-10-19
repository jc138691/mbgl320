package lapack4j.src.java;
import static lapack4j.utils.IntrFuncs.ABS;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/10/11,4:33 PM
 */
public class DLASSQ { //SUBROUTINE DLASSQ( N, X, INCX, SCALE, SUMSQ )
  public static void DLASSQ(int N, double[] X, int INCX, double SCALE, double SUMSQ ) { //SUBROUTINE DLASSQ( N, X, INCX, SCALE, SUMSQ )
    //*
    //*  -- LAPACK auxiliary routine (version 3.2) --
    //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
    //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
    //*     November 2006
    //*
    //*     .. Scalar Arguments ..
    //    int INCX, N; //INTEGER            INCX, N
    //    double SCALE, SUMSQ; //DOUBLE PRECISION   SCALE, SUMSQ
    //*     ..
    //*     .. Array Arguments ..
    //    double X[]; //DOUBLE PRECISION   X( * )
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  DLASSQ  returns the values  scl  and  smsq  such that
    //*
    //*     ( scl**2 )*smsq = x( 1 )**2 +...+ x( n )**2 + ( scale**2 )*sumsq,
    //*
    //*  where  x( i ) = X( 1 + ( i - 1 )*INCX ). The value of  sumsq  is
    //*  assumed to be non-negative and  scl  returns the value
    //*
    //*     scl = max( scale, abs( x( i ) ) ).
    //*
    //*  scale and sumsq must be supplied in SCALE and SUMSQ and
    //*  scl and smsq are overwritten on SCALE and SUMSQ respectively.
    //*
    //*  The routine makes only one pass through the vector x.
    //*
    //*  Arguments
    //*  =========
    //*
    //*  N       (input) INTEGER
    //*          The number of elements to be used from the vector X.
    //*
    //*  X       (input) DOUBLE PRECISION array, dimension (N)
    //*          The vector for which a scaled sum of squares is computed.
    //*             x( i )  = X( 1 + ( i - 1 )*INCX ), 1 <= i <= n.
    //*
    //*  INCX    (input) INTEGER
    //*          The increment between successive values of the vector X.
    //*          INCX > 0.
    //*
    //*  SCALE   (input/output) DOUBLE PRECISION
    //*          On entry, the value  scale  in the equation above.
    //*          On exit, SCALE is overwritten with  scl , the scaling factor
    //*          for the sum of squares.
    //*
    //*  SUMSQ   (input/output) DOUBLE PRECISION
    //*          On entry, the value  sumsq  in the equation above.
    //*          On exit, SUMSQ is overwritten with  smsq , the basic sum of
    //*          squares from which  scl  has been factored out.
    //*
    //* =====================================================================
    //*
    //*     .. Parameters ..
    double ZERO; //DOUBLE PRECISION   ZERO
    {  ZERO = 0.0D ; } //PARAMETER          ( ZERO = 0.0D+0 )
    //*     ..
    //*     .. Local Scalars ..
    int IX; //INTEGER            IX
    double ABSXI; //DOUBLE PRECISION   ABSXI
    //*     ..
    //*     .. Intrinsic Functions ..
    //INTRINSIC          ABS
    //*     ..
    //*     .. Executable Statements ..
    //*
    if ( N > 0 ) { //IF( N.GT.0 ) THEN
      for (IX = 0; IX < 1 + ( N-1 )*INCX; IX += INCX) { //DO 10 IX = 1, 1 + ( N-1 )*INCX, INCX
        if ( X[ IX ] != ZERO ) { //IF( X( IX ).NE.ZERO ) THEN
          ABSXI = ABS( X[ IX ] ); //ABSXI = ABS( X( IX ) )
          if ( SCALE < ABSXI ) { //IF( SCALE.LT.ABSXI ) THEN
            double tmp = ( SCALE / ABSXI );
            SUMSQ = 1.d + SUMSQ*tmp*tmp; //SUMSQ = 1 + SUMSQ*( SCALE / ABSXI )**2
            SCALE = ABSXI; //SCALE = ABSXI
          } else { // ELSE
            double tmp = ( ABSXI / SCALE );
            SUMSQ = SUMSQ + tmp*tmp; //SUMSQ = SUMSQ + ( ABSXI / SCALE )**2
          } // END IF
        } // END IF
      }//10    CONTINUE; //10    CONTINUE
    } // END IF
  } // RETURN
  //*
  //*     End of DLASSQ
  //*
} // END