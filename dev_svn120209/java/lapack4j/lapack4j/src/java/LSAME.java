package lapack4j.src.java;
import static lapack4j.utils.IntrFuncs.ICHAR;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,18/10/11,2:35 PM
 */
public class LSAME { //LOGICAL FUNCTION LSAME(CA,CB)
  public static boolean LSAME(final char CA, final char CB) { //LOGICAL FUNCTION LSAME(CA,CB)
    //public class LSAME { //LOGICAL FUNCTION LSAME(CA,CB)
    //public static boolean LSAME(CA,CB) { //LOGICAL FUNCTION LSAME(CA,CB)
    //*
    //*  -- LAPACK auxiliary routine (version 3.1) --
    //*     Univ. of Tennessee, Univ. of California Berkeley and NAG Ltd..
    //*     November 2006
    //*
    //*     .. Scalar Arguments ..
    //char CA,CB; //CHARACTER CA,CB
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  LSAME returns .TRUE. if CA is the same letter as CB regardless of
    //*  case.
    //*
    //*  Arguments
    //*  =========
    //*
    //*  CA      (input) CHARACTER*1
    //*
    //*  CB      (input) CHARACTER*1
    //*          CA and CB specify the single characters to be compared.
    //*
    //* =====================================================================
    //*
    //*     .. Intrinsic Functions ..
    //INTRINSIC ICHAR
    //*     ..
    //*     .. Local Scalars ..
    int INTA,INTB,ZCODE; //INTEGER INTA,INTB,ZCODE
    //*     ..
    //*
    //*     Test if the characters are equal
    //*
    boolean LSAME = CA  ==  CB; //LSAME = CA .EQ. CB
    if  (LSAME) return LSAME; //IF (LSAME) RETURN
    //*
    //*     Now test for equivalence if both characters are alphabetic.
    //*
    ZCODE = ICHAR('Z'); //ZCODE = ICHAR('Z')
    //*
    //*     Use 'Z' rather than 'A' so that ASCII can be detected on Prime
    //*     machines, on which ICHAR returns a value with bit 8 set.
    //*     ICHAR('A') on Prime machines returns 193 which is the same as
    //*     ICHAR('A') on an EBCDIC machine.
    //*
    INTA = ICHAR(CA); //INTA = ICHAR(CA)
    INTB = ICHAR(CB); //INTB = ICHAR(CB)
    //*
    if  (ZCODE == 90  ||  ZCODE == 122) { //IF (ZCODE.EQ.90 .OR. ZCODE.EQ.122) THEN
      //*
      //*        ASCII is assumed - ZCODE is the ASCII code of either lower or
      //*        upper case 'Z'.
      //*
      if  (INTA >= 97  &&  INTA <= 122) INTA = INTA - 32; //IF (INTA.GE.97 .AND. INTA.LE.122) INTA = INTA - 32
      if  (INTB >= 97  &&  INTB <= 122) INTB = INTB - 32; //IF (INTB.GE.97 .AND. INTB.LE.122) INTB = INTB - 32
      //*
    } else if  (ZCODE == 233  ||  ZCODE == 169) { //ELSE IF (ZCODE.EQ.233 .OR. ZCODE.EQ.169) THEN
      //*
      //*        EBCDIC is assumed - ZCODE is the EBCDIC code of either lower or
      //*        upper case 'Z'.
      //*
      if  (INTA >= 129  &&  INTA <= 137  || INTA >= 145  &&  INTA <= 153  || INTA >= 162  &&  INTA <= 169) INTA = INTA + 64; //IF (INTA.GE.129 .AND. INTA.LE.137 .OR.INTA.GE.145 .AND. INTA.LE.153 .OR.INTA.GE.162 .AND. INTA.LE.169) INTA = INTA + 64
      if  (INTB >= 129  &&  INTB <= 137  || INTB >= 145  &&  INTB <= 153  || INTB >= 162  &&  INTB <= 169) INTB = INTB + 64; //IF (INTB.GE.129 .AND. INTB.LE.137 .OR.INTB.GE.145 .AND. INTB.LE.153 .OR.INTB.GE.162 .AND. INTB.LE.169) INTB = INTB + 64
      //*
    } else if  (ZCODE == 218  ||  ZCODE == 250) { //ELSE IF (ZCODE.EQ.218 .OR. ZCODE.EQ.250) THEN
      //*
      //*        ASCII is assumed, on Prime machines - ZCODE is the ASCII code
      //*        plus 128 of either lower or upper case 'Z'.
      //*
      if  (INTA >= 225  &&  INTA <= 250) INTA = INTA - 32; //IF (INTA.GE.225 .AND. INTA.LE.250) INTA = INTA - 32
      if  (INTB >= 225  &&  INTB <= 250) INTB = INTB - 32; //IF (INTB.GE.225 .AND. INTB.LE.250) INTB = INTB - 32
    } // END IF
    LSAME = INTA  ==  INTB; //LSAME = INTA .EQ. INTB
    //*
    return LSAME; //*     RETURN
    //*
    //*     End of LSAME
    //*
  } // END
}