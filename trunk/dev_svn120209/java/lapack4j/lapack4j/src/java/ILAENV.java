package lapack4j.src.java;
import static lapack4j.utils.IntrFuncs.ICHAR;
import static lapack4j.utils.IntrFuncs.MIN;
import static lapack4j.src.java.IEEECK.IEEECK;
import static lapack4j.src.java.IPARMQ.IPARMQ;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 27/10/11, 9:45 AM
 */
public class ILAENV { //INTEGER FUNCTION ILAENV( ISPEC, NAME, OPTS, N1, N2, N3, N4 )
public static int ILAENV(
  final int ISPEC   //ISPEC   (input) INTEGER
  , final String NAME  //NAME    (input) CHARACTER*(*)
  , final String OPTS //OPTS    (input) CHARACTER*(*)
  , final int N1   //N1      (input) INTEGER
  , final int N2   //N2      (input) INTEGER
  , final int N3   //N3      (input) INTEGER
  , final int N4   // N4      (input) INTEGER
) { //INTEGER FUNCTION ILAENV( ISPEC, NAME, OPTS, N1, N2, N3, N4 )
//*
//*  -- LAPACK auxiliary routine (version 3.2.1)                        --
//*
//*  -- April 2009                                                      --
//*
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*
//*     .. Scalar Arguments ..
//  char *[]    NAME, OPTS; //CHARACTER*( * )    NAME, OPTS
//  int ISPEC, N1, N2, N3, N4; //INTEGER            ISPEC, N1, N2, N3, N4
  int ILAENV = 0;
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  ILAENV is called from the LAPACK routines to choose problem-dependent
//*  parameters for the local environment.  See ISPEC for a description of
//*  the parameters.
//*
//*  ILAENV returns an INTEGER
//*  if ILAENV >= 0: ILAENV returns the value of the parameter specified by ISPEC
//*  if ILAENV < 0:  if ILAENV = -k, the k-th argument had an illegal value.
//*
//*  This version provides a set of parameters which should give good,
//*  but not optimal, performance on many of the currently available
//*  computers.  Users are encouraged to modify this subroutine to set
//*  the tuning parameters for their particular machine using the option
//*  and problem size information in the arguments.
//*
//*  This routine will not function correctly if it is converted to all
//*  lower case.  Converting it to all upper case is allowed.
//*
//*  Arguments
//*  =========
//*
//*  ISPEC   (input) INTEGER
//*          Specifies the parameter to be returned as the value of
//*          ILAENV.
//*          = 1: the optimal blocksize; if this value is 1, an unblocked
//*               algorithm will give the best performance.
//*          = 2: the minimum block size for which the block routine
//*               should be used; if the usable block size is less than
//*               this value, an unblocked routine should be used.
//*          = 3: the crossover point (in a block routine, for N less
//*               than this value, an unblocked routine should be used)
//*          = 4: the number of shifts, used in the nonsymmetric
//*               eigenvalue routines (DEPRECATED)
//*          = 5: the minimum column dimension for blocking to be used;
//*               rectangular blocks must have dimension at least k by m,
//*               where k is given by ILAENV(2,...) and m by ILAENV(5,...)
//*          = 6: the crossover point for the SVD (when reducing an m by n
//*               matrix to bidiagonal form, if max(m,n)/min(m,n) exceeds
//*               this value, a QR factorization is used first to reduce
//*               the matrix to a triangular form.)
//*          = 7: the number of processors
//*          = 8: the crossover point for the multishift QR method
//*               for nonsymmetric eigenvalue problems (DEPRECATED)
//*          = 9: maximum size of the subproblems at the bottom of the
//*               computation tree in the divide-and-conquer algorithm
//*               (used by xGELSD and xGESDD)
//*          =10: ieee NaN arithmetic can be trusted not to trap
//*          =11: infinity arithmetic can be trusted not to trap
//*          12 <= ISPEC <= 16:
//*               xHSEQR or one of its subroutines,
//*               see IPARMQ for detailed explanation
//*
//*  NAME    (input) CHARACTER*(*)
//*          The name of the calling subroutine, in either upper case or
//*          lower case.
//*
//*  OPTS    (input) CHARACTER*(*)
//*          The character options to the subroutine NAME, concatenated
//*          into a single character string.  For example, UPLO = 'U',
//*          TRANS = 'T', and DIAG = 'N' for a triangular routine would
//*          be specified as OPTS = 'UTN'.
//*
//*  N1      (input) INTEGER
//*  N2      (input) INTEGER
//*  N3      (input) INTEGER
//*  N4      (input) INTEGER
//*          Problem dimensions for the subroutine NAME; these may not all
//*          be required.
//*
//*  Further Details
//*  ===============
//*
//*  The following conventions have been used when calling ILAENV from the
//*  LAPACK routines:
//*  1)  OPTS is a concatenation of all of the character options to
//*      subroutine NAME, in the same order that they appear in the
//*      argument list for NAME, even if they are not used in determining
//*      the value of the parameter specified by ISPEC.
//*  2)  The problem dimensions N1, N2, N3, N4 are specified in the order
//*      that they appear in the argument list for NAME.  N1 is used
//*      first, N2 second, and so on, and unused problem dimensions are
//*      passed a value of -1.
//*  3)  The parameter value returned by ILAENV is checked for validity in
//*      the calling subroutine.  For example, ILAENV is used to retrieve
//*      the optimal blocksize for STRTRI as follows:
//*
//*      NB = ILAENV( 1, 'STRTRI', UPLO // DIAG, N, -1, -1, -1 )
//*      IF( NB.LE.1 ) NB = MAX( 1, N )
//*
//*  =====================================================================
//*
//*     .. Local Scalars ..
  int I, IC, IZ, NB, NBMIN, NX; //INTEGER            I, IC, IZ, NB, NBMIN, NX
  boolean CNAME, SNAME; //LOGICAL            CNAME, SNAME
  char C1; //CHARACTER          C1*1, C2*2, C4*2, C3*3, SUBNAM*6
  String C2, C4, C3; //CHARACTER          C1*1, C2*2, C4*2, C3*3, SUBNAM*6
  char[] SUBNAM; //CHARACTER          C1*1, C2*2, C4*2, C3*3, SUBNAM*6
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC          CHAR, ICHAR, INT, MIN, REAL
//*     ..
//*     .. External Functions ..
//  int IEEECK, IPARMQ; //INTEGER            IEEECK, IPARMQ
//  EXTERNAL           IEEECK, IPARMQ; //EXTERNAL           IEEECK, IPARMQ
//*     ..
//*     .. Executable Statements ..
//*
//GO TO ( 10, 10, 10, 80, 90, 100, 110, 120,130, 140, 150, 160, 160, 160, 160, 160 )ISPEC
  switch (ISPEC) {
    default://*     Invalid value for ISPEC
      ILAENV = -1; //ILAENV = -1
      return ILAENV; // RETURN
    case 1:    //10 CONTINUE
    case 2:    //10 CONTINUE
    case 3:    //10 CONTINUE
//*     Convert NAME to upper case if the first character is lower case.
      ILAENV = 1; //ILAENV = 1
      SUBNAM = NAME.toCharArray(); //SUBNAM = NAME
      IC = ICHAR( SUBNAM[0] ); //IC = ICHAR( SUBNAM( 1: 1 ) )
      IZ = ICHAR( 'Z' ); //IZ = ICHAR( 'Z' )
      if ( IZ == 90  ||  IZ == 122 ) { //IF( IZ.EQ.90 .OR. IZ.EQ.122 ) THEN
//*        ASCII character set
        if ( IC >= 97  &&  IC <= 122 ) { //IF( IC.GE.97 .AND. IC.LE.122 ) THEN
          SUBNAM[0] = (char)( IC-32 ); //SUBNAM( 1: 1 ) = CHAR( IC-32 )
          for (I = 2; I <= 6; I++) { //DO 20 I = 2, 6
            IC = ICHAR( SUBNAM[I -1] ); //IC = ICHAR( SUBNAM( I: I ) )
            if ( IC >= 97  &&  IC <= 122 )
              SUBNAM[ I -1] = (char)( IC-32 ); //IF( IC.GE.97 .AND. IC.LE.122 )SUBNAM( I: I ) = CHAR( IC-32 )
          } //20       CONTINUE
        } // END IF
      } else if ( IZ == 233  ||  IZ == 169 ) { //ELSE IF( IZ.EQ.233 .OR. IZ.EQ.169 ) THEN
//*        EBCDIC character set
        if ( ( IC >= 129  &&  IC <= 137 )  || ( IC >= 145  &&  IC <= 153 )  || ( IC >= 162  &&  IC <= 169 ) ) { //IF( ( IC.GE.129 .AND. IC.LE.137 ) .OR.( IC.GE.145 .AND. IC.LE.153 ) .OR.( IC.GE.162 .AND. IC.LE.169 ) ) THEN
          SUBNAM[0] = (char)( IC+64 ); //SUBNAM( 1: 1 ) = CHAR( IC+64 )
          for (I = 2; I <= 6; I++) { //DO 30 I = 2, 6
            IC = ICHAR( SUBNAM[I -1] ); //IC = ICHAR( SUBNAM( I: I ) )
            if ( ( IC >= 129  &&  IC <= 137 )  || ( IC >= 145  &&  IC <= 153 )
              || ( IC >= 162  &&  IC <= 169 ) )
              SUBNAM[I -1] = (char)( IC+64 ); //IF( ( IC.GE.129 .AND. IC.LE.137 ) .OR.( IC.GE.145 .AND. IC.LE.153 ) .OR.( IC.GE.162 .AND. IC.LE.169 ) )SUBNAM( I:I ) = CHAR( IC+64 )
          } //30       CONTINUE
        } // END IF
      } else if ( IZ == 218  ||  IZ == 250 ) { //ELSE IF( IZ.EQ.218 .OR. IZ.EQ.250 ) THEN
//*        Prime machines:  ASCII+128
        if ( IC >= 225  &&  IC <= 250 ) { //IF( IC.GE.225 .AND. IC.LE.250 ) THEN
          SUBNAM[0] = (char)( IC-32 ); //SUBNAM( 1: 1 ) = CHAR( IC-32 )
          for (I = 2; I <= 6; I++) { //DO 40 I = 2, 6
            IC = ICHAR( SUBNAM[ I -1] ); //IC = ICHAR( SUBNAM( I: I ) )
            if ( IC >= 225  &&  IC <= 250 )
              SUBNAM[ I -1] = (char)( IC-32 ); //IF( IC.GE.225 .AND. IC.LE.250 )SUBNAM( I: I ) = CHAR( IC-32 )
          } //40       CONTINUE
        } // END IF
      } // END IF
      C1 = SUBNAM[0]; //C1 = SUBNAM( 1: 1 )
      SNAME = C1 == 'S'  ||  C1 == 'D'; //SNAME = C1.EQ.'S' .OR. C1.EQ.'D'
      CNAME = C1 == 'C'  ||  C1 == 'Z'; //CNAME = C1.EQ.'C' .OR. C1.EQ.'Z'
      if (  !( CNAME  ||  SNAME ) )
        return ILAENV; //IF( .NOT.( CNAME .OR. SNAME ) )RETURN
      String stSUBNAM = new String(SUBNAM);
      C2 = stSUBNAM.substring( 2 -1, 3 -1); //C2 = SUBNAM( 2: 3 )
      C3 = stSUBNAM.substring( 4 -1, 6 -1); //C3 = SUBNAM( 4: 6 )
      C4 = C3.substring( 2 -1, 3 -1); //C4 = C3( 2: 3 )
//*
//  GO TO ( 50, 60, 70 ) ISPEC; //GO TO ( 50, 60, 70 )ISPEC
      if (ISPEC == 1) {//50 CONTINUE
//*     ISPEC = 1:  block size
//*     In these examples, separate code is provided for setting NB for
//*     real and complex.  We assume that NB will take the same value in
//*     single or double precision.
        NB = 1; //NB = 1
        if ( C2.equals("GE") ) { //IF( C2.EQ.'GE' ) THEN
          if ( C3.equals("TRF") ) { //IF( C3.EQ.'TRF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 64; //NB = 64
            } else { // ELSE
              NB = 64; //NB = 64
            } // END IF
          } else if ( C3.equals("QRF")
            ||  C3.equals("RQF")  ||  C3.equals("LQF")  || C3.equals("QLF") ) { //ELSE IF( C3.EQ.'QRF' .OR. C3.EQ.'RQF' .OR. C3.EQ.'LQF' .OR.C3.EQ.'QLF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 32; //NB = 32
            } else { // ELSE
              NB = 32; //NB = 32
            } // END IF
          } else if ( C3.equals("HRD") ) { //ELSE IF( C3.EQ.'HRD' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 32; //NB = 32
            } else { // ELSE
              NB = 32; //NB = 32
            } // END IF
          } else if ( C3.equals("BRD") ) { //ELSE IF( C3.EQ.'BRD' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 32; //NB = 32
            } else { // ELSE
              NB = 32; //NB = 32
            } // END IF
          } else if ( C3.equals("TRI") ) { //ELSE IF( C3.EQ.'TRI' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 64; //NB = 64
            } else { // ELSE
              NB = 64; //NB = 64
            } // END IF
          } // END IF
        } else if ( C2.equals("PO") ) { //ELSE IF( C2.EQ.'PO' ) THEN
          if ( C3.equals("TRF") ) { //IF( C3.EQ.'TRF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 64; //NB = 64
            } else { // ELSE
              NB = 64; //NB = 64
            } // END IF
          } // END IF
        } else if ( C2.equals("SY") ) { //ELSE IF( C2.EQ.'SY' ) THEN
          if ( C3.equals("TRF") ) { //IF( C3.EQ.'TRF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 64; //NB = 64
            } else { // ELSE
              NB = 64; //NB = 64
            } // END IF
          } else if ( SNAME  &&  C3.equals("TRD") ) { //ELSE IF( SNAME .AND. C3.EQ.'TRD' ) THEN
            NB = 32; //NB = 32
          } else if ( SNAME  &&  C3.equals("GST") ) { //ELSE IF( SNAME .AND. C3.EQ.'GST' ) THEN
            NB = 64; //NB = 64
          } // END IF
        } else if ( CNAME  &&  C2.equals("HE") ) { //ELSE IF( CNAME .AND. C2.EQ.'HE' ) THEN
          if ( C3.equals("TRF") ) { //IF( C3.EQ.'TRF' ) THEN
            NB = 64; //NB = 64
          } else if ( C3.equals("TRD") ) { //ELSE IF( C3.EQ.'TRD' ) THEN
            NB = 32; //NB = 32
          } else if ( C3.equals("GST") ) { //ELSE IF( C3.EQ.'GST' ) THEN
            NB = 64; //NB = 64
          } // END IF
        } else if ( SNAME  &&  C2.equals("OR") ) { //ELSE IF( SNAME .AND. C2.EQ.'OR' ) THEN
          if ( C3.charAt(0) == 'G' ) { //IF( C3( 1: 1 ).EQ.'G' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NB = 32; //NB = 32
            } // END IF
          } else if ( C3.charAt(0) == 'M' ) { //ELSE IF( C3( 1: 1 ).EQ.'M' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NB = 32; //NB = 32
            } // END IF
          } // END IF
        } else if ( CNAME  &&  C2.equals("UN") ) { //ELSE IF( CNAME .AND. C2.EQ.'UN' ) THEN
          if ( C3.charAt(0) == 'G' ) { //IF( C3( 1: 1 ).EQ.'G' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NB = 32; //NB = 32
            } // END IF
          } else if ( C3.charAt(0) == 'M' ) { //ELSE IF( C3( 1: 1 ).EQ.'M' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NB = 32; //NB = 32
            } // END IF
          } // END IF
        } else if ( C2.equals("GB") ) { //ELSE IF( C2.EQ.'GB' ) THEN
          if ( C3.equals("TRF") ) { //IF( C3.EQ.'TRF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              if ( N4 <= 64 ) { //IF( N4.LE.64 ) THEN
                NB = 1; //NB = 1
              } else { // ELSE
                NB = 32; //NB = 32
              } // END IF
            } else { // ELSE
              if ( N4 <= 64 ) { //IF( N4.LE.64 ) THEN
                NB = 1; //NB = 1
              } else { // ELSE
                NB = 32; //NB = 32
              } // END IF
            } // END IF
          } // END IF
        } else if ( C2.equals("PB") ) { //ELSE IF( C2.EQ.'PB' ) THEN
          if ( C3.equals("TRF") ) { //IF( C3.EQ.'TRF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              if ( N2 <= 64 ) { //IF( N2.LE.64 ) THEN
                NB = 1; //NB = 1
              } else { // ELSE
                NB = 32; //NB = 32
              } // END IF
            } else { // ELSE
              if ( N2 <= 64 ) { //IF( N2.LE.64 ) THEN
                NB = 1; //NB = 1
              } else { // ELSE
                NB = 32; //NB = 32
              } // END IF
            } // END IF
          } // END IF
        } else if ( C2.equals("TR") ) { //ELSE IF( C2.EQ.'TR' ) THEN
          if ( C3.equals("TRI") ) { //IF( C3.EQ.'TRI' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 64; //NB = 64
            } else { // ELSE
              NB = 64; //NB = 64
            } // END IF
          } // END IF
        } else if ( C2.equals("LA") ) { //ELSE IF( C2.EQ.'LA' ) THEN
          if ( C3.equals("UUM") ) { //IF( C3.EQ.'UUM' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NB = 64; //NB = 64
            } else { // ELSE
              NB = 64; //NB = 64
            } // END IF
          } // END IF
        } else if ( SNAME  &&  C2.equals("ST") ) { //ELSE IF( SNAME .AND. C2.EQ.'ST' ) THEN
          if ( C3.equals("EBZ") ) { //IF( C3.EQ.'EBZ' ) THEN
            NB = 1; //NB = 1
          } // END IF
        } // END IF
        ILAENV = NB; //ILAENV = NB
        return ILAENV; // RETURN
      } else if (ISPEC == 2) { //60 CONTINUE
//*     ISPEC = 2:  minimum block size
        NBMIN = 2; //NBMIN = 2
        if ( C2.equals("GE") ) { //IF( C2.EQ.'GE' ) THEN
          if ( C3.equals("QRF")  ||  C3.equals("RQF")
            ||  C3.equals("LQF")  ||  C3.equals("QLF") ) { //IF( C3.EQ.'QRF' .OR. C3.EQ.'RQF' .OR. C3.EQ.'LQF' .OR. C3.EQ.'QLF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NBMIN = 2; //NBMIN = 2
            } else { // ELSE
              NBMIN = 2; //NBMIN = 2
            } // END IF
          } else if ( C3.equals("HRD") ) { //ELSE IF( C3.EQ.'HRD' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NBMIN = 2; //NBMIN = 2
            } else { // ELSE
              NBMIN = 2; //NBMIN = 2
            } // END IF
          } else if ( C3.equals("BRD") ) { //ELSE IF( C3.EQ.'BRD' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NBMIN = 2; //NBMIN = 2
            } else { // ELSE
              NBMIN = 2; //NBMIN = 2
            } // END IF
          } else if ( C3.equals("TRI") ) { //ELSE IF( C3.EQ.'TRI' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NBMIN = 2; //NBMIN = 2
            } else { // ELSE
              NBMIN = 2; //NBMIN = 2
            } // END IF
          } // END IF
        } else if ( C2.equals("SY") ) { //ELSE IF( C2.EQ.'SY' ) THEN
          if ( C3.equals("TRF") ) { //IF( C3.EQ.'TRF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NBMIN = 8; //NBMIN = 8
            } else { // ELSE
              NBMIN = 8; //NBMIN = 8
            } // END IF
          } else if ( SNAME  &&  C3.equals("TRD") ) { //ELSE IF( SNAME .AND. C3.EQ.'TRD' ) THEN
            NBMIN = 2; //NBMIN = 2
          } // END IF
        } else if ( CNAME  &&  C2.equals("HE") ) { //ELSE IF( CNAME .AND. C2.EQ.'HE' ) THEN
          if ( C3.equals("TRD") ) { //IF( C3.EQ.'TRD' ) THEN
            NBMIN = 2; //NBMIN = 2
          } // END IF
        } else if ( SNAME  &&  C2.equals("OR") ) { //ELSE IF( SNAME .AND. C2.EQ.'OR' ) THEN
          if ( C3.charAt(0) == 'G' ) { //IF( C3( 1: 1 ).EQ.'G' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NBMIN = 2; //NBMIN = 2
            } // END IF
          } else if ( C3.charAt(0) == 'M' ) { //ELSE IF( C3( 1: 1 ).EQ.'M' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NBMIN = 2; //NBMIN = 2
            } // END IF
          } // END IF
        } else if ( CNAME  &&  C2.equals("UN") ) { //ELSE IF( CNAME .AND. C2.EQ.'UN' ) THEN
          if ( C3.charAt(0) == 'G' ) { //IF( C3( 1: 1 ).EQ.'G' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NBMIN = 2; //NBMIN = 2
            } // END IF
          } else if ( C3.charAt(0) == 'M' ) { //ELSE IF( C3( 1: 1 ).EQ.'M' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NBMIN = 2; //NBMIN = 2
            } // END IF
          } // END IF
        } // END IF
        ILAENV = NBMIN; //ILAENV = NBMIN
        return ILAENV; // RETURN
      } else if (ISPEC == 3) { //70 CONTINUE
//*     ISPEC = 3:  crossover point
        NX = 0; //NX = 0
        if ( C2.equals("GE") ) { //IF( C2.EQ.'GE' ) THEN
          if ( C3.equals("QRF")  ||  C3.equals("RQF")
            ||  C3.equals("LQF")  ||  C3.equals("QLF") ) { //IF( C3.EQ.'QRF' .OR. C3.EQ.'RQF' .OR. C3.EQ.'LQF' .OR. C3.EQ.'QLF' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NX = 128; //NX = 128
            } else { // ELSE
              NX = 128; //NX = 128
            } // END IF
          } else if ( C3.equals("HRD") ) { //ELSE IF( C3.EQ.'HRD' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NX = 128; //NX = 128
            } else { // ELSE
              NX = 128; //NX = 128
            } // END IF
          } else if ( C3.equals("BRD") ) { //ELSE IF( C3.EQ.'BRD' ) THEN
            if ( SNAME ) { //IF( SNAME ) THEN
              NX = 128; //NX = 128
            } else { // ELSE
              NX = 128; //NX = 128
            } // END IF
          } // END IF
        } else if ( C2.equals("SY") ) { //ELSE IF( C2.EQ.'SY' ) THEN
          if ( SNAME  &&  C3.equals("TRD") ) { //IF( SNAME .AND. C3.EQ.'TRD' ) THEN
            NX = 32; //NX = 32
          } // END IF
        } else if ( CNAME  &&  C2.equals("HE") ) { //ELSE IF( CNAME .AND. C2.EQ.'HE' ) THEN
          if ( C3.equals("TRD") ) { //IF( C3.EQ.'TRD' ) THEN
            NX = 32; //NX = 32
          } // END IF
        } else if ( SNAME  &&  C2.equals("OR") ) { //ELSE IF( SNAME .AND. C2.EQ.'OR' ) THEN
          if ( C3.charAt(0) == 'G' ) { //IF( C3( 1: 1 ).EQ.'G' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NX = 128; //NX = 128
            } // END IF
          } // END IF
        } else if ( CNAME  &&  C2.equals("UN") ) { //ELSE IF( CNAME .AND. C2.EQ.'UN' ) THEN
          if ( C3.charAt(0) == 'G' ) { //IF( C3( 1: 1 ).EQ.'G' ) THEN
            if ( C4.equals("QR")  ||  C4.equals("RQ")  ||  C4.equals("LQ")
              ||  C4.equals("QL")  ||  C4.equals("HR")  ||  C4.equals("TR")
              ||  C4.equals("BR") ) { //IF( C4.EQ.'QR' .OR. C4.EQ.'RQ' .OR. C4.EQ.'LQ' .OR. C4.EQ.'QL' .OR. C4.EQ.'HR' .OR. C4.EQ.'TR' .OR. C4.EQ.'BR' )THEN
              NX = 128; //NX = 128
            } // END IF
          } // END IF
        } // END IF
        ILAENV = NX; //ILAENV = NX
        return ILAENV; // RETURN
      }
    case 4:  //80 CONTINUE
//*     ISPEC = 4:  number of shifts (used by xHSEQR)
      ILAENV = 6; //ILAENV = 6
      return ILAENV; // RETURN
    case 5://90 CONTINUE
//*     ISPEC = 5:  minimum column dimension (not used)
      ILAENV = 2; //ILAENV = 2
      return ILAENV; // RETURN
    case 6://100 CONTINUE
//*     ISPEC = 6:  crossover point for SVD (used by xGELSS and xGESVD)
      ILAENV = (int)( (float)( MIN( N1, N2 ) )*1.6E0 ); //ILAENV = INT( REAL( MIN( N1, N2 ) )*1.6E0 )
      return ILAENV; // RETURN
    case 7://110 CONTINUE
//*     ISPEC = 7:  number of processors (not used)
      ILAENV = 1; //ILAENV = 1
      return ILAENV; // RETURN
    case 8://120 CONTINUE
//*     ISPEC = 8:  crossover point for multishift (used by xHSEQR)
      ILAENV = 50; //ILAENV = 50
      return ILAENV; // RETURN
    case 9://130 CONTINUE
//*     ISPEC = 9:  maximum size of the subproblems at the bottom of the
//*                 computation tree in the divide-and-conquer algorithm
//*                 (used by xGELSD and xGESDD)
      ILAENV = 25; //ILAENV = 25
      return ILAENV; // RETURN
    case 10://140 CONTINUE
//*     ISPEC = 10: ieee NaN arithmetic can be trusted not to trap
//*     ILAENV = 0
      ILAENV = 1; //ILAENV = 1
      if ( ILAENV == 1 ) { //IF( ILAENV.EQ.1 ) THEN
        ILAENV = IEEECK( 1, 0.0f, 1.0f ); //ILAENV = IEEECK( 1, 0.0, 1.0 )
      } // END IF
      return ILAENV; // RETURN
    case 11://150 CONTINUE
//*     ISPEC = 11: infinity arithmetic can be trusted not to trap
//*     ILAENV = 0
      ILAENV = 1; //ILAENV = 1
      if ( ILAENV == 1 ) { //IF( ILAENV.EQ.1 ) THEN
        ILAENV = IEEECK( 0, 0.0f, 1.0f ); //ILAENV = IEEECK( 0, 0.0, 1.0 )
      } // END IF
      return ILAENV; // RETURN
    case 12: //160 CONTINUE
//*     12 <= ISPEC <= 16: xHSEQR or one of its subroutines.
      ILAENV = IPARMQ( ISPEC, NAME, OPTS, N1, N2, N3, N4 ); //ILAENV = IPARMQ( ISPEC, NAME, OPTS, N1, N2, N3, N4 )
      return ILAENV; // RETURN
//*     End of ILAENV
  } // end of switch (ISPEC) {
}
} // END