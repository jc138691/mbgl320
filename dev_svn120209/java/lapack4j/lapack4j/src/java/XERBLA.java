package lapack4j.src.java;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 31/10/11, 12:18 PM
 */
public class XERBLA { //SUBROUTINE XERBLA( SRNAME, INFO )
public static void XERBLA(
  String SRNAME  //SRNAME  (input) CHARACTER*(*)
  , int INFO     //INFO    (input) INTEGER
) { //SUBROUTINE XERBLA( SRNAME, INFO )
//*
//*  -- LAPACK auxiliary routine (preliminary version) --
//*     Univ. of Tennessee, Univ. of California Berkeley and NAG Ltd..
//*     November 2006
//*
//*     .. Scalar Arguments ..
//  char *[]      SRNAME; //CHARACTER*(*)      SRNAME
//  int INFO; //INTEGER            INFO
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  XERBLA  is an error handler for the LAPACK routines.
//*  It is called by an LAPACK routine if an input parameter has an
//*  invalid value.  A message is printed and execution stops.
//*
//*  Installers may consider modifying the STOP statement in order to
//*  call system-specific exception-handling facilities.
//*
//*  Arguments
//*  =========
//*
//*  SRNAME  (input) CHARACTER*(*)
//*          The name of the routine which called XERBLA.
//*
//*  INFO    (input) INTEGER
//*          The position of the invalid parameter in the parameter list
//*          of the calling routine.
//*
//* =====================================================================
//*
//*     .. Intrinsic Functions ..
//INTRINSIC          LEN_TRIM
//*     ..
//*     .. Executable Statements ..
//*
  System.out.println(" ** On entry to " + SRNAME
    + " parameter number " + INFO + " had ','an illegal value" );
  //WRITE( *, FMT = 9999 )SRNAME( 1:LEN_TRIM( SRNAME ) ), INFO
  System.exit(INFO); //STOP
//9999 FORMAT( ' ** On entry to ', A, ' parameter number ', I2, ' had ','an illegal value' )
//*     End of XERBLA
} // END
}