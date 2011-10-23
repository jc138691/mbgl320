package lapack4j.src.java;
import lapack4j.utils.IntRef;
import static lapack4j.src.java.LSAME.LSAME;

/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,20/10/11,11:29 AM
 */
public class DLARRC { //SUBROUTINE DLARRC( JOBT, N, VL, VU, D, E, PIVMIN,EIGCNT, LCNT, RCNT, INFO )
  public static void DLARRC(char JOBT, int N, double VL, double VU
    , double[] D, double[] E, double PIVMIN
    , IntRef pEIGCNT, IntRef pLCNT, IntRef pRCNT, IntRef pINFO ) { //SUBROUTINE DLARRC( JOBT, N, VL, VU, D, E, PIVMIN,EIGCNT, LCNT, RCNT, INFO )
    //*
    //*  -- LAPACK auxiliary routine (version 3.2) --
    //*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
    //*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
    //*     November 2006
    //*
    //*     .. Scalar Arguments ..
    //    char JOBT; //CHARACTER          JOBT
    int EIGCNT, INFO, LCNT, RCNT; //INTEGER            EIGCNT, INFO, LCNT, N, RCNT
    //    double PIVMIN, VL, VU; //DOUBLE PRECISION   PIVMIN, VL, VU
    //*     ..
    //*     .. Array Arguments ..
    //    double D[], E[]; //DOUBLE PRECISION   D( * ), E( * )
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*  Find the number of eigenvalues of the symmetric tridiagonal matrix T
    //*  that are in the interval (VL,VU] if JOBT = 'T', and of L D L^T
    //*  if JOBT = 'L'.
    //*
    //*  Arguments
    //*  =========
    //*
    //*  JOBT    (input) CHARACTER*1
    //*          = 'T':  Compute Sturm count for matrix T.
    //*          = 'L':  Compute Sturm count for matrix L D L^T.
    //*
    //*  N       (input) INTEGER
    //*          The order of the matrix. N > 0.
    //*
    //*  VL      (input) DOUBLE PRECISION
    //*  VU      (input) DOUBLE PRECISION
    //*          The lower and upper bounds for the eigenvalues.
    //*
    //*  D       (input) DOUBLE PRECISION array, dimension (N)
    //*          JOBT = 'T': The N diagonal elements of the tridiagonal matrix T.
    //*          JOBT = 'L': The N diagonal elements of the diagonal matrix D.
    //*
    //*  E       (input) DOUBLE PRECISION array, dimension (N)
    //*          JOBT = 'T': The N-1 offdiagonal elements of the matrix T.
    //*          JOBT = 'L': The N-1 offdiagonal elements of the matrix L.
    //*
    //*  PIVMIN  (input) DOUBLE PRECISION
    //*          The minimum pivot in the Sturm sequence for T.
    //*
    //*  EIGCNT  (output) INTEGER
    //*          The number of eigenvalues of the symmetric tridiagonal matrix T
    //*          that are in the interval (VL,VU]
    //*
    //*  LCNT    (output) INTEGER
    //*  RCNT    (output) INTEGER
    //*          The left and right negcounts of the interval.
    //*
    //*  INFO    (output) INTEGER
    //*
    //*  Further Details
    //*  ===============
    //*
    //*  Based on contributions by
    //*     Beresford Parlett, University of California, Berkeley, USA
    //*     Jim Demmel, University of California, Berkeley, USA
    //*     Inderjit Dhillon, University of Texas, Austin, USA
    //*     Osni Marques, LBNL/NERSC, USA
    //*     Christof Voemel, University of California, Berkeley, USA
    //*
    //*  =====================================================================
    //*
    //*     .. Parameters ..
    double ZERO; //DOUBLE PRECISION   ZERO
    {  ZERO = 0.0D; } //PARAMETER          ( ZERO = 0.0D0 )
    //*     ..
    //*     .. Local Scalars ..
    int I; //INTEGER            I
    boolean MATT; //LOGICAL            MATT
    double LPIVOT, RPIVOT, SL, SU, TMP, TMP2; //DOUBLE PRECISION   LPIVOT, RPIVOT, SL, SU, TMP, TMP2
    //*     ..
    //*     .. External Functions ..
    //    boolean LSAME; //LOGICAL            LSAME
    //    EXTERNAL           LSAME; //EXTERNAL           LSAME
    //*     ..
    //*     .. Executable Statements ..
    //*
    INFO = 0; //INFO = 0
    LCNT = 0; //LCNT = 0
    RCNT = 0; //RCNT = 0
    EIGCNT = 0; //EIGCNT = 0
    MATT = LSAME( JOBT, 'T' ); //MATT = LSAME( JOBT, 'T' )
    if  (MATT) { //IF (MATT) THEN
      //*        Sturm sequence count on T
      LPIVOT = D[ 0 ] - VL; //LPIVOT = D( 1 ) - VL
      RPIVOT = D[ 0 ] - VU; //RPIVOT = D( 1 ) - VU
      if ( LPIVOT <= ZERO ) { //IF( LPIVOT.LE.ZERO ) THEN
        LCNT = LCNT + 1; //LCNT = LCNT + 1
      } // ENDIF
      if ( RPIVOT <= ZERO ) { //IF( RPIVOT.LE.ZERO ) THEN
        RCNT = RCNT + 1; //RCNT = RCNT + 1
      } // ENDIF
      for (I = 0; I < N-1; I++) { //DO 10 I = 1, N-1
        TMP = E[I]*E[I]; //TMP = E(I)**2
        LPIVOT = ( D[ I+1 ]-VL ) - TMP/LPIVOT; //LPIVOT = ( D( I+1 )-VL ) - TMP/LPIVOT
        RPIVOT = ( D[ I+1 ]-VU ) - TMP/RPIVOT; //RPIVOT = ( D( I+1 )-VU ) - TMP/RPIVOT
        if ( LPIVOT <= ZERO ) { //IF( LPIVOT.LE.ZERO ) THEN
          LCNT = LCNT + 1; //LCNT = LCNT + 1
        } // ENDIF
        if ( RPIVOT <= ZERO ) { //IF( RPIVOT.LE.ZERO ) THEN
          RCNT = RCNT + 1; //RCNT = RCNT + 1
        } // ENDIF
      }//10      CONTINUE
    } else { // ELSE
      //*        Sturm sequence count on L D L^T
      SL = -VL; //SL = -VL
      SU = -VU; //SU = -VU
      for (I = 0; I < N - 1; I++) { //DO 20 I = 1, N - 1
        LPIVOT = D[ I ] + SL; //LPIVOT = D( I ) + SL
        RPIVOT = D[ I ] + SU; //RPIVOT = D( I ) + SU
        if ( LPIVOT <= ZERO ) { //IF( LPIVOT.LE.ZERO ) THEN
          LCNT = LCNT + 1; //LCNT = LCNT + 1
        } // ENDIF
        if ( RPIVOT <= ZERO ) { //IF( RPIVOT.LE.ZERO ) THEN
          RCNT = RCNT + 1; //RCNT = RCNT + 1
        } // ENDIF
        TMP = E[I] * D[I] * E[I]; //TMP = E(I) * D(I) * E(I)
        //*
        TMP2 = TMP / LPIVOT; //TMP2 = TMP / LPIVOT
        if ( TMP2 == ZERO ) { //IF( TMP2.EQ.ZERO ) THEN
          SL =  TMP - VL; //SL =  TMP - VL
        } else { // ELSE
          SL = SL*TMP2 - VL; //SL = SL*TMP2 - VL
        } // END IF
        //*
        TMP2 = TMP / RPIVOT; //TMP2 = TMP / RPIVOT
        if ( TMP2 == ZERO ) { //IF( TMP2.EQ.ZERO ) THEN
          SU =  TMP - VU; //SU =  TMP - VU
        } else { // ELSE
          SU = SU*TMP2 - VU; //SU = SU*TMP2 - VU
        } // END IF
      } //20      CONTINUE
      LPIVOT = D[ N-1 ] + SL; //LPIVOT = D( N ) + SL
      RPIVOT = D[ N-1 ] + SU; //RPIVOT = D( N ) + SU
      if ( LPIVOT <= ZERO ) { //IF( LPIVOT.LE.ZERO ) THEN
        LCNT = LCNT + 1; //LCNT = LCNT + 1
      } // ENDIF
      if ( RPIVOT <= ZERO ) { //IF( RPIVOT.LE.ZERO ) THEN
        RCNT = RCNT + 1; //RCNT = RCNT + 1
      } // ENDIF
    } // ENDIF
    EIGCNT = RCNT - LCNT; //EIGCNT = RCNT - LCNT

    pEIGCNT.set(EIGCNT);
    pINFO.set(INFO);
    pLCNT.set(LCNT);
    pRCNT.set(RCNT);
  } // RETURN
  //*
  //*     end of DLARRC
  //*
} // END