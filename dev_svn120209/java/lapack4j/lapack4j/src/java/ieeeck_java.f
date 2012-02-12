public class IEEECK { //INTEGER          FUNCTION IEEECK( ISPEC, ZERO, ONE )
public static int IEEECK( ISPEC, ZERO, ONE ) { //INTEGER          FUNCTION IEEECK( ISPEC, ZERO, ONE )
//*
//*  -- LAPACK auxiliary routine (version 3.3.1) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*  -- April 2011                                                      --
//*
//*     .. Scalar Arguments ..
int ISPEC; //INTEGER            ISPEC
REAL               ONE, ZERO; //REAL               ONE, ZERO
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  IEEECK is called from the ILAENV to verify that Infinity and
//*  possibly NaN arithmetic is safe (i.e. will not trap).
//*
//*  Arguments
//*  =========
//*
//*  ISPEC   (input) INTEGER
//*          Specifies whether to test just for inifinity arithmetic
//*          or whether to test for infinity and NaN arithmetic.
//*          = 0: Verify infinity arithmetic only.
//*          = 1: Verify infinity and NaN arithmetic.
//*
//*  ZERO    (input) REAL
//*          Must contain the value 0.0
//*          This is passed to prevent the compiler from optimizing
//*          away this code.
//*
//*  ONE     (input) REAL
//*          Must contain the value 1.0
//*          This is passed to prevent the compiler from optimizing
//*          away this code.
//*
//*  RETURN VALUE:  INTEGER
//*          = 0:  Arithmetic failed to produce the correct answers
//*          = 1:  Arithmetic produced the correct answers
//*
//*  =====================================================================
//*
//*     .. Local Scalars ..
REAL               NAN1, NAN2, NAN3, NAN4, NAN5, NAN6, NEGINF,NEGZRO, NEWZRO, POSINF; //REAL               NAN1, NAN2, NAN3, NAN4, NAN5, NAN6, NEGINF,NEGZRO, NEWZRO, POSINF
//*     ..
//*     .. Executable Statements ..
IEEECK = 1; //IEEECK = 1
//*
POSINF = ONE / ZERO; //POSINF = ONE / ZERO
if ( POSINF <= ONE ) { //IF( POSINF.LE.ONE ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
NEGINF = -ONE / ZERO; //NEGINF = -ONE / ZERO
if ( NEGINF >= ZERO ) { //IF( NEGINF.GE.ZERO ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
NEGZRO = ONE / ( NEGINF+ONE ); //NEGZRO = ONE / ( NEGINF+ONE )
if ( NEGZRO != ZERO ) { //IF( NEGZRO.NE.ZERO ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
NEGINF = ONE / NEGZRO; //NEGINF = ONE / NEGZRO
if ( NEGINF >= ZERO ) { //IF( NEGINF.GE.ZERO ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
NEWZRO = NEGZRO + ZERO; //NEWZRO = NEGZRO + ZERO
if ( NEWZRO != ZERO ) { //IF( NEWZRO.NE.ZERO ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
POSINF = ONE / NEWZRO; //POSINF = ONE / NEWZRO
if ( POSINF <= ONE ) { //IF( POSINF.LE.ONE ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
NEGINF = NEGINF*POSINF; //NEGINF = NEGINF*POSINF
if ( NEGINF >= ZERO ) { //IF( NEGINF.GE.ZERO ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
POSINF = POSINF*POSINF; //POSINF = POSINF*POSINF
if ( POSINF <= ONE ) { //IF( POSINF.LE.ONE ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
//*
//*
//*
//*     Return if we were only asked to check infinity arithmetic
//*
if ( ISPEC == 0 )RETURN; //IF( ISPEC.EQ.0 )RETURN
//*
NAN1 = POSINF + NEGINF; //NAN1 = POSINF + NEGINF
//*
NAN2 = POSINF / NEGINF; //NAN2 = POSINF / NEGINF
//*
NAN3 = POSINF / POSINF; //NAN3 = POSINF / POSINF
//*
NAN4 = POSINF*ZERO; //NAN4 = POSINF*ZERO
//*
NAN5 = NEGINF*NEGZRO; //NAN5 = NEGINF*NEGZRO
//*
NAN6 = NAN5*ZERO; //NAN6 = NAN5*ZERO
//*
if ( NAN1 == NAN1 ) { //IF( NAN1.EQ.NAN1 ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
if ( NAN2 == NAN2 ) { //IF( NAN2.EQ.NAN2 ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
if ( NAN3 == NAN3 ) { //IF( NAN3.EQ.NAN3 ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
if ( NAN4 == NAN4 ) { //IF( NAN4.EQ.NAN4 ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
if ( NAN5 == NAN5 ) { //IF( NAN5.EQ.NAN5 ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
if ( NAN6 == NAN6 ) { //IF( NAN6.EQ.NAN6 ) THEN
IEEECK = 0; //IEEECK = 0
} // RETURN
} // END IF
//*
} // RETURN
} // END