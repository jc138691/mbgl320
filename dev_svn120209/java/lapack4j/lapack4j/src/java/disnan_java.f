public class DISNAN { //LOGICAL FUNCTION DISNAN( DIN )
public static boolean DISNAN( DIN ) { //LOGICAL FUNCTION DISNAN( DIN )
//*
//*  -- LAPACK auxiliary routine (version 3.2.2) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*     June 2010
//*
//*     .. Scalar Arguments ..
double DIN; //DOUBLE PRECISION   DIN
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  DISNAN returns .TRUE. if its argument is NaN, and .FALSE.
//*  otherwise.  To be replaced by the Fortran 2003 intrinsic in the
//*  future.
//*
//*  Arguments
//*  =========
//*
//*  DIN     (input) DOUBLE PRECISION
//*          Input to test for NaN.
//*
//*  =====================================================================
//*
//*  .. External Functions ..
boolean DLAISNAN; //LOGICAL DLAISNAN
EXTERNAL DLAISNAN; //EXTERNAL DLAISNAN
//*  ..
//*  .. Executable Statements ..
DISNAN = DLAISNAN(DIN,DIN); //DISNAN = DLAISNAN(DIN,DIN)
} // RETURN
} // END