package lapack4j.src.java;
import static lapack4j.src.java.DLAISNAN.DLAISNAN;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,20/10/11,4:19 PM
 */
public class DISNAN { //LOGICAL FUNCTION DISNAN( DIN )
  public static boolean DISNAN(double DIN ) { //LOGICAL FUNCTION DISNAN( DIN )
    //*
    //*  -- LAPACK auxiliary routine (version 3.2.2) --
    //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
    //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
    //*     June 2010
    //*
    //*     .. Scalar Arguments ..
    //    double DIN; //DOUBLE PRECISION   DIN
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
    //    boolean DLAISNAN; //LOGICAL DLAISNAN
    //    EXTERNAL DLAISNAN; //EXTERNAL DLAISNAN
    //*  ..
    //*  .. Executable Statements ..
    boolean DISNAN = DLAISNAN(DIN,DIN); //DISNAN = DLAISNAN(DIN,DIN)
    return   DISNAN;
  } // RETURN
} // END