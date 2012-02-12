package lapack4j.src.java;
import static lapack4j.utils.IntrFuncs.EPSILON;
import static lapack4j.utils.IntrFuncs.TINY;
import static lapack4j.utils.IntrFuncs.HUGE;
import static lapack4j.utils.IntrFuncs.RADIX;
import static lapack4j.utils.IntrFuncs.DIGITS;
import static lapack4j.utils.IntrFuncs.MINEXPONENT;
import static lapack4j.utils.IntrFuncs.MAXEXPONENT;
import static lapack4j.src.java.LSAME.LSAME;
import static lapack4j.utils.MathConsts.*;

/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,18/10/11,2:12 PM
 */
public class DLAMCH { //DOUBLE PRECISION FUNCTION DLAMCH( CMACH )
  public static double DLAMCH( final char CMACH ) { //DOUBLE PRECISION FUNCTION DLAMCH( CMACH )
    //public class DLAMCH { //DOUBLE PRECISION FUNCTION DLAMCH( CMACH )
    //public static double DLAMCH( CMACH ) { //DOUBLE PRECISION FUNCTION DLAMCH( CMACH )
    //*
    //*  -- LAPACK auxiliary routine (version 3.3.0) --
    //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
    //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
    //*     Based on LAPACK DLAMCH but with Fortran 95 query functions
    //*     See: http://www.cs.utk.edu/~luszczek/lapack/lamch.html
    //*     and  http://www.netlib.org/lapack-dev/lapack-coding/program-style.html#id2537289
    //*     July 2010
    //*
    //*     .. Scalar Arguments ..
    //char CMACH; //CHARACTER          CMACH
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  DLAMCH determines double precision machine parameters.
    //*
    //*  Arguments
    //*  =========
    //*
    //*  CMACH   (input) CHARACTER*1
    //*          Specifies the value to be returned by DLAMCH:
    //*          = 'E' or 'e',   DLAMCH := eps
    //*          = 'S' or 's ,   DLAMCH := sfmin
    //*          = 'B' or 'b',   DLAMCH := base
    //*          = 'P' or 'p',   DLAMCH := eps*base
    //*          = 'N' or 'n',   DLAMCH := t
    //*          = 'R' or 'r',   DLAMCH := rnd
    //*          = 'M' or 'm',   DLAMCH := emin
    //*          = 'U' or 'u',   DLAMCH := rmin
    //*          = 'L' or 'l',   DLAMCH := emax
    //*          = 'O' or 'o',   DLAMCH := rmax
    //*
    //*          where
    //*
    //*          eps   = relative machine precision
    //*          sfmin = safe minimum, such that 1/sfmin does not overflow
    //*          base  = base of the machine
    //*          prec  = eps*base
    //*          t     = number of (base) digits in the mantissa
    //*          rnd   = 1.0 when rounding occurs in addition, 0.0 otherwise
    //*          emin  = minimum exponent before (gradual) underflow
    //*          rmin  = underflow threshold - base**(emin-1)
    //*          emax  = largest exponent before overflow
    //*          rmax  = overflow threshold  - (base**emax)*(1-eps)
    //*
    //* =====================================================================
    //*
    //*     .. Parameters ..
//    double ONE, ZERO; //DOUBLE PRECISION   ONE, ZERO
//    {  ONE = 1.0D; ZERO = 0.0D ; } //PARAMETER          ( ONE = 1.0D+0, ZERO = 0.0D+0 )
    //*     ..
    //*     .. Local Scalars ..
    double RND, EPS, SFMIN, SMALL, RMACH; //DOUBLE PRECISION   RND, EPS, SFMIN, SMALL, RMACH
    //*     ..
    //*     .. External Functions ..
    boolean LSAME; //LOGICAL            LSAME
    //    EXTERNAL           LSAME; //EXTERNAL           LSAME
    //*     ..
    //*     .. Intrinsic Functions ..
    //INTRINSIC          DIGITS, EPSILON, HUGE, MAXEXPONENT,MINEXPONENT, RADIX, TINY
    //*     ..
    //*     .. Executable Statements ..
    //*
    //*
    //*     Assume rounding, not chopping. Always.
    //*
    RND = ONE; //RND = ONE
    //*
    if ( ONE == RND ) { //IF( ONE.EQ.RND ) THEN
      EPS = EPSILON(ZERO) * 0.5; //EPS = EPSILON(ZERO) * 0.5
    } else { // ELSE
      EPS = EPSILON(ZERO); //EPS = EPSILON(ZERO)
    } // END IF
    //*
    if ( LSAME( CMACH, 'E' ) ) { //IF( LSAME( CMACH, 'E' ) ) THEN
      RMACH = EPS; //RMACH = EPS
    } else if ( LSAME( CMACH, 'S' ) ) { //ELSE IF( LSAME( CMACH, 'S' ) ) THEN
      SFMIN = TINY(ZERO); //SFMIN = TINY(ZERO)
      SMALL = ONE / HUGE(ZERO); //SMALL = ONE / HUGE(ZERO)
      if ( SMALL >= SFMIN ) { //IF( SMALL.GE.SFMIN ) THEN
        //*
        //*           Use SMALL plus a bit, to avoid the possibility of rounding
        //*           causing overflow when computing  1/sfmin.
        //*
        SFMIN = SMALL*( ONE+EPS ); //SFMIN = SMALL*( ONE+EPS )
      } // END IF
      RMACH = SFMIN; //RMACH = SFMIN
    } else if ( LSAME( CMACH, 'B' ) ) { //ELSE IF( LSAME( CMACH, 'B' ) ) THEN
      RMACH = RADIX(ZERO); //RMACH = RADIX(ZERO)
    } else if ( LSAME( CMACH, 'P' ) ) { //ELSE IF( LSAME( CMACH, 'P' ) ) THEN
      RMACH = EPS * RADIX(ZERO); //RMACH = EPS * RADIX(ZERO)
    } else if ( LSAME( CMACH, 'N' ) ) { //ELSE IF( LSAME( CMACH, 'N' ) ) THEN
      RMACH = DIGITS(ZERO); //RMACH = DIGITS(ZERO)
    } else if ( LSAME( CMACH, 'R' ) ) { //ELSE IF( LSAME( CMACH, 'R' ) ) THEN
      RMACH = RND; //RMACH = RND
    } else if ( LSAME( CMACH, 'M' ) ) { //ELSE IF( LSAME( CMACH, 'M' ) ) THEN
      RMACH = MINEXPONENT(ZERO); //RMACH = MINEXPONENT(ZERO)
    } else if ( LSAME( CMACH, 'U' ) ) { //ELSE IF( LSAME( CMACH, 'U' ) ) THEN
      RMACH = TINY(ZERO); //RMACH = tiny(zero)
    } else if ( LSAME( CMACH, 'L' ) ) { //ELSE IF( LSAME( CMACH, 'L' ) ) THEN
      RMACH = MAXEXPONENT(ZERO); //RMACH = MAXEXPONENT(ZERO)
    } else if ( LSAME( CMACH, 'O' ) ) { //ELSE IF( LSAME( CMACH, 'O' ) ) THEN
      RMACH = HUGE(ZERO); //RMACH = HUGE(ZERO)
    } else { // ELSE
      RMACH = ZERO; //RMACH = ZERO
    } // END IF
    //*
    double DLAMCH = RMACH; //DLAMCH = RMACH
    return DLAMCH;
  } // RETURN
  //*
  //*     End of DLAMCH
  //*
  //} // END
  //************************************************************************
  //*
  //public class DLAMC3 { //DOUBLE PRECISION FUNCTION DLAMC3( A, B )
  public static double DLAMC3(double A, double B ) { //DOUBLE PRECISION FUNCTION DLAMC3( A, B )
    //*
    //*  -- LAPACK auxiliary routine (version 3.3.0) --
    //*     Univ. of Tennessee, Univ. of California Berkeley and NAG Ltd..
    //*     November 2010
    //*
    //*     .. Scalar Arguments ..
    //    double A, B; //DOUBLE PRECISION   A, B
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  DLAMC3  is intended to force  A  and  B  to be stored prior to doing
    //*  the addition of  A  and  B ,  for use in situations where optimizers
    //*  might hold one of these in a register.
    //*
    //*  Arguments
    //*  =========
    //*
    //*  A       (input) DOUBLE PRECISION
    //*  B       (input) DOUBLE PRECISION
    //*          The values A and B.
    //*
    //* =====================================================================
    //*
    //*     .. Executable Statements ..
    //*
    double DLAMC3 = A + B; //DLAMC3 = A + B
    //*
    return DLAMC3;
  } // RETURN
  //*
  //*     End of DLAMC3
  //*
} // END
//*
//************************************************************************