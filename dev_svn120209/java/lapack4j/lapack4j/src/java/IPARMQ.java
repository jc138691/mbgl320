package lapack4j.src.java;
import com.sun.xml.internal.ws.message.saaj.SAAJHeader;

import static lapack4j.utils.IntrFuncs.LOG;
import static lapack4j.utils.IntrFuncs.MOD;
import static lapack4j.utils.IntrFuncs.MAX;
import static lapack4j.utils.IntrFuncs.NINT;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 28/10/11, 11:20 AM
 */
public class IPARMQ { //INTEGER FUNCTION IPARMQ( ISPEC, NAME, OPTS, N, ILO, IHI, LWORK )
public static int IPARMQ(
  final int ISPEC    //ISPEC  (input) integer scalar
  , final String NAME  //NAME    (input) character string
  , final String OPTS  //OPTS    (input) character string
  , final int N   //N       (input) integer scalar
  , final int ILO  //ILO     (input) INTEGER
  , final int IHI  //IHI     (input) INTEGER
  , final int LWORK  //LWORK   (input) integer scalar
) { //INTEGER FUNCTION IPARMQ( ISPEC, NAME, OPTS, N, ILO, IHI, LWORK )
//*
//*  -- LAPACK auxiliary routine (version 3.2) --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*     November 2006
//*
//*     .. Scalar Arguments ..
  int IPARMQ = 0; //INTEGER            IHI, ILO, ISPEC, LWORK, N
//  char NAME*[], OPTS*[]; //CHARACTER          NAME*( * ), OPTS*( * )
//*
//*  Purpose
//*  =======
//*
//*       This program sets problem and machine dependent parameters
//*       useful for xHSEQR and its subroutines. It is called whenever
//*       ILAENV is called with 12 <= ISPEC <= 16
//*
//*  Arguments
//*  =========
//*
//*       ISPEC  (input) integer scalar
//*              ISPEC specifies which tunable parameter IPARMQ should
//*              return.
//*
//*              ISPEC=12: (INMIN)  Matrices of order nmin or less
//*                        are sent directly to xLAHQR, the implicit
//*                        double shift QR algorithm.  NMIN must be
//*                        at least 11.
//*
//*              ISPEC=13: (INWIN)  Size of the deflation window.
//*                        This is best set greater than or equal to
//*                        the number of simultaneous shifts NS.
//*                        Larger matrices benefit from larger deflation
//*                        windows.
//*
//*              ISPEC=14: (INIBL) Determines when to stop nibbling and
//*                        invest in an (expensive) multi-shift QR sweep.
//*                        If the aggressive early deflation subroutine
//*                        finds LD converged eigenvalues from an order
//*                        NW deflation window and LD.GT.(NW*NIBBLE)/100,
//*                        then the next QR sweep is skipped and early
//*                        deflation is applied immediately to the
//*                        remaining active diagonal block.  Setting
//*                        IPARMQ(ISPEC=14) = 0 causes TTQRE to skip a
//*                        multi-shift QR sweep whenever early deflation
//*                        finds a converged eigenvalue.  Setting
//*                        IPARMQ(ISPEC=14) greater than or equal to 100
//*                        prevents TTQRE from skipping a multi-shift
//*                        QR sweep.
//*
//*              ISPEC=15: (NSHFTS) The number of simultaneous shifts in
//*                        a multi-shift QR iteration.
//*
//*              ISPEC=16: (IACC22) IPARMQ is set to 0, 1 or 2 with the
//*                        following meanings.
//*                        0:  During the multi-shift QR sweep,
//*                            xLAQR5 does not accumulate reflections and
//*                            does not use matrix-matrix multiply to
//*                            update the far-from-diagonal matrix
//*                            entries.
//*                        1:  During the multi-shift QR sweep,
//*                            xLAQR5 and/or xLAQRaccumulates reflections and uses
//*                            matrix-matrix multiply to update the
//*                            far-from-diagonal matrix entries.
//*                        2:  During the multi-shift QR sweep.
//*                            xLAQR5 accumulates reflections and takes
//*                            advantage of 2-by-2 block structure during
//*                            matrix-matrix multiplies.
//*                        (If xTRMM is slower than xGEMM, then
//*                        IPARMQ(ISPEC=16)=1 may be more efficient than
//*                        IPARMQ(ISPEC=16)=2 despite the greater level of
//*                        arithmetic work implied by the latter choice.)
//*
//*       NAME    (input) character string
//*               Name of the calling subroutine
//*
//*       OPTS    (input) character string
//*               This is a concatenation of the string arguments to
//*               TTQRE.
//*
//*       N       (input) integer scalar
//*               N is the order of the Hessenberg matrix H.
//*
//*       ILO     (input) INTEGER
//*       IHI     (input) INTEGER
//*               It is assumed that H is already upper triangular
//*               in rows and columns 1:ILO-1 and IHI+1:N.
//*
//*       LWORK   (input) integer scalar
//*               The amount of workspace available.
//*
//*  Further Details
//*  ===============
//*
//*       Little is known about how best to choose these parameters.
//*       It is possible to use different values of the parameters
//*       for each of CHSEQR, DHSEQR, SHSEQR and ZHSEQR.
//*
//*       It is probably best to choose different parameters for
//*       different matrices and different parameters at different
//*       times during the iteration, but this has not been
//*       implemented --- yet.
//*
//*
//*       The best choices of most of the parameters depend
//*       in an ill-understood way on the relative execution
//*       rate of xLAQR3 and xLAQR5 and on the nature of each
//*       particular eigenvalue problem.  Experiment may be the
//*       only practical way to determine which choices are most
//*       effective.
//*
//*       Following is a list of default values supplied by IPARMQ.
//*       These defaults may be adjusted in order to attain better
//*       performance in any particular computational environment.
//*
//*       IPARMQ(ISPEC=12) The xLAHQR vs xLAQR0 crossover point.
//*                        Default: 75. (Must be at least 11.)
//*
//*       IPARMQ(ISPEC=13) Recommended deflation window size.
//*                        This depends on ILO, IHI and NS, the
//*                        number of simultaneous shifts returned
//*                        by IPARMQ(ISPEC=15).  The default for
//*                        (IHI-ILO+1).LE.500 is NS.  The default
//*                        for (IHI-ILO+1).GT.500 is 3*NS/2.
//*
//*       IPARMQ(ISPEC=14) Nibble crossover point.  Default: 14.
//*
//*       IPARMQ(ISPEC=15) Number of simultaneous shifts, NS.
//*                        a multi-shift QR iteration.
//*
//*                        If IHI-ILO+1 is ...
//*
//*                        greater than      ...but less    ... the
//*                        or equal to ...      than        default is
//*
//*                                0               30       NS =   2+
//*                               30               60       NS =   4+
//*                               60              150       NS =  10
//*                              150              590       NS =  **
//*                              590             3000       NS =  64
//*                             3000             6000       NS = 128
//*                             6000             infinity   NS = 256
//*
//*                    (+)  By default matrices of this order are
//*                         passed to the implicit double shift routine
//*                         xLAHQR.  See IPARMQ(ISPEC=12) above.   These
//*                         values of NS are used only in case of a rare
//*                         xLAHQR failure.
//*
//*                    (**) The asterisks (**) indicate an ad-hoc
//*                         function increasing from 10 to 64.
//*
//*       IPARMQ(ISPEC=16) Select structured matrix multiply.
//*                        (See ISPEC=16 above for details.)
//*                        Default: 3.
//*
//*     ================================================================
//*     .. Parameters ..
  int INMIN, INWIN, INIBL, ISHFTS, IACC22; //INTEGER            INMIN, INWIN, INIBL, ISHFTS, IACC22
  {  INMIN = 12; INWIN = 13; INIBL = 14; ISHFTS = 15; IACC22 = 16; } //PARAMETER          ( INMIN = 12, INWIN = 13, INIBL = 14,ISHFTS = 15, IACC22 = 16 )
  int NMIN, K22MIN, KACMIN, NIBBLE, KNWSWP; //INTEGER            NMIN, K22MIN, KACMIN, NIBBLE, KNWSWP
  {  NMIN = 75; K22MIN = 14; KACMIN = 14; NIBBLE = 14; KNWSWP = 500; } //PARAMETER          ( NMIN = 75, K22MIN = 14, KACMIN = 14,NIBBLE = 14, KNWSWP = 500 )
  float               TWO; //REAL               TWO
  {  TWO = 2.0f; } //PARAMETER          ( TWO = 2.0 )
//*     ..
//*     .. Local Scalars ..
  int NH = 0, NS = 0; //INTEGER            NH, NS
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC          LOG, MAX, MOD, NINT, REAL
//*     ..
//*     .. Executable Statements ..
  if ( ( ISPEC == ISHFTS )
    ||  ( ISPEC == INWIN )
    || ( ISPEC == IACC22 ) ) { //IF( ( ISPEC.EQ.ISHFTS ) .OR. ( ISPEC.EQ.INWIN ) .OR.( ISPEC.EQ.IACC22 ) ) THEN
//*
//*        ==== Set the number simultaneous shifts ====
//*
    NH = IHI - ILO + 1; //NH = IHI - ILO + 1
    NS = 2; //NS = 2
    if ( NH >= 30 )
      NS = 4; //IF( NH.GE.30 )NS = 4
    if ( NH >= 60 )
      NS = 10; //IF( NH.GE.60 )NS = 10
    if ( NH >= 150 )
      NS = MAX( 10, NH / NINT( LOG( (float)( NH ) ) / LOG( TWO ) ) ); //IF( NH.GE.150 )NS = MAX( 10, NH / NINT( LOG( REAL( NH ) ) / LOG( TWO ) ) )
    if ( NH >= 590 )
      NS = 64; //IF( NH.GE.590 )NS = 64
    if ( NH >= 3000 )
      NS = 128; //IF( NH.GE.3000 )NS = 128
    if ( NH >= 6000 )
      NS = 256; //IF( NH.GE.6000 )NS = 256
    NS = MAX( 2, NS-MOD( NS, 2 ) ); //NS = MAX( 2, NS-MOD( NS, 2 ) )
  } // END IF
//*
  if ( ISPEC == INMIN ) { //IF( ISPEC.EQ.INMIN ) THEN
//*
//*
//*        ===== Matrices of order smaller than NMIN get sent
//*        .     to xLAHQR, the classic double shift algorithm.
//*        .     This must be at least 11. ====
//*
    IPARMQ = NMIN; //IPARMQ = NMIN
//*
  } else if ( ISPEC == INIBL ) { //ELSE IF( ISPEC.EQ.INIBL ) THEN
//*
//*        ==== INIBL: skip a multi-shift qr iteration and
//*        .    whenever aggressive early deflation finds
//*        .    at least (NIBBLE*(window size)/100) deflations. ====
//*
    IPARMQ = NIBBLE; //IPARMQ = NIBBLE
//*
  } else if ( ISPEC == ISHFTS ) { //ELSE IF( ISPEC.EQ.ISHFTS ) THEN
//*
//*        ==== NSHFTS: The number of simultaneous shifts =====
//*
    IPARMQ = NS; //IPARMQ = NS
//*
  } else if ( ISPEC == INWIN ) { //ELSE IF( ISPEC.EQ.INWIN ) THEN
//*
//*        ==== NW: deflation window size.  ====
//*
    if ( NH <= KNWSWP ) { //IF( NH.LE.KNWSWP ) THEN
      IPARMQ = NS; //IPARMQ = NS
    } else { // ELSE
      IPARMQ = 3*NS / 2; //IPARMQ = 3*NS / 2
    } // END IF
//*
  } else if ( ISPEC == IACC22 ) { //ELSE IF( ISPEC.EQ.IACC22 ) THEN
//*
//*        ==== IACC22: Whether to accumulate reflections
//*        .     before updating the far-from-diagonal elements
//*        .     and whether to use 2-by-2 block structure while
//*        .     doing it.  A small amount of work could be saved
//*        .     by making this choice dependent also upon the
//*        .     NH=IHI-ILO+1.
//*
    IPARMQ = 0; //IPARMQ = 0
    if ( NS >= KACMIN )IPARMQ = 1; //IF( NS.GE.KACMIN )IPARMQ = 1
    if ( NS >= K22MIN )IPARMQ = 2; //IF( NS.GE.K22MIN )IPARMQ = 2
//*
  } else { // ELSE
//*        ===== invalid value of ispec =====
    IPARMQ = -1; //IPARMQ = -1
//*
  } // END IF
//*
//*     ==== End of IPARMQ ====
//*
  return IPARMQ;
}
} // END