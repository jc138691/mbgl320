public class DSTEMR { //SUBROUTINE DSTEMR( JOBZ, RANGE, N, D, E, VL, VU, IL, IU,M, W, Z, LDZ, NZC, ISUPPZ, TRYRAC, WORK, LWORK,IWORK, LIWORK, INFO )
public static void DSTEMR( JOBZ, RANGE, N, D, E, VL, VU, IL, IU,M, W, Z, LDZ, NZC, ISUPPZ, TRYRAC, WORK, LWORK,IWORK, LIWORK, INFO ) { //SUBROUTINE DSTEMR( JOBZ, RANGE, N, D, E, VL, VU, IL, IU,M, W, Z, LDZ, NZC, ISUPPZ, TRYRAC, WORK, LWORK,IWORK, LIWORK, INFO )
//IMPLICIT NONE
//*
//*  -- LAPACK computational routine (version 3.2.2)                                  --
//*  -- LAPACK is a software package provided by Univ. of Tennessee,    --
//*  -- Univ. of California Berkeley, Univ. of Colorado Denver and NAG Ltd..--
//*  -- June 2010                                                       --
//*
//*     .. Scalar Arguments ..
char JOBZ, RANGE; //CHARACTER          JOBZ, RANGE
boolean TRYRAC; //LOGICAL            TRYRAC
int IL, INFO, IU, LDZ, NZC, LIWORK, LWORK, M, N; //INTEGER            IL, INFO, IU, LDZ, NZC, LIWORK, LWORK, M, N
double VL, VU; //DOUBLE PRECISION VL, VU
//*     ..
//*     .. Array Arguments ..
int ISUPPZ[], IWORK[]; //INTEGER            ISUPPZ( * ), IWORK( * )
double D[], E[], W[], WORK[]; //DOUBLE PRECISION   D( * ), E( * ), W( * ), WORK( * )
double Z( LDZ, * ); //DOUBLE PRECISION   Z( LDZ, * )
//*     ..
//*
//*  Purpose
//*  =======
//*
//*  DSTEMR computes selected eigenvalues and, optionally, eigenvectors
//*  of a real symmetric tridiagonal matrix T. Any such unreduced matrix has
//*  a well defined set of pairwise different real eigenvalues, the corresponding
//*  real eigenvectors are pairwise orthogonal.
//*
//*  The spectrum may be computed either completely or partially by specifying
//*  either an interval (VL,VU] or a range of indices IL:IU for the desired
//*  eigenvalues.
//*
//*  Depending on the number of desired eigenvalues, these are computed either
//*  by bisection or the dqds algorithm. Numerically orthogonal eigenvectors are
//*  computed by the use of various suitable L D L^T factorizations near clusters
//*  of close eigenvalues (referred to as RRRs, Relatively Robust
//*  Representations). An informal sketch of the algorithm follows.
//*
//*  For each unreduced block (submatrix) of T,
//*     (a) Compute T - sigma I  = L D L^T, so that L and D
//*         define all the wanted eigenvalues to high relative accuracy.
//*         This means that small relative changes in the entries of D and L
//*         cause only small relative changes in the eigenvalues and
//*         eigenvectors. The standard (unfactored) representation of the
//*         tridiagonal matrix T does not have this property in general.
//*     (b) Compute the eigenvalues to suitable accuracy.
//*         If the eigenvectors are desired, the algorithm attains full
//*         accuracy of the computed eigenvalues only right before
//*         the corresponding vectors have to be computed, see steps c) and d).
//*     (c) For each cluster of close eigenvalues, select a new
//*         shift close to the cluster, find a new factorization, and refine
//*         the shifted eigenvalues to suitable accuracy.
//*     (d) For each eigenvalue with a large enough relative separation compute
//*         the corresponding eigenvector by forming a rank revealing twisted
//*         factorization. Go back to (c) for any clusters that remain.
//*
//*  For more details, see:
//*  - Inderjit S. Dhillon and Beresford N. Parlett: "Multiple representations
//*    to compute orthogonal eigenvectors of symmetric tridiagonal matrices,"
//*    Linear Algebra and its Applications, 387(1), pp. 1-28, August 2004.
//*  - Inderjit Dhillon and Beresford Parlett: "Orthogonal Eigenvectors and
//*    Relative Gaps," SIAM Journal on Matrix Analysis and Applications, Vol. 25,
//*    2004.  Also LAPACK Working Note 154.
//*  - Inderjit Dhillon: "A new O(n^2) algorithm for the symmetric
//*    tridiagonal eigenvalue/eigenvector problem",
//*    Computer Science Division Technical Report No. UCB/CSD-97-971,
//*    UC Berkeley, May 1997.
//*
//*  Further Details
//*  1.DSTEMR works only on machines which follow IEEE-754
//*  floating-point standard in their handling of infinities and NaNs.
//*  This permits the use of efficient inner loops avoiding a check for
//*  zero divisors.
//*
//*  Arguments
//*  =========
//*
//*  JOBZ    (input) CHARACTER*1
//*          = 'N':  Compute eigenvalues only;
//*          = 'V':  Compute eigenvalues and eigenvectors.
//*
//*  RANGE   (input) CHARACTER*1
//*          = 'A': all eigenvalues will be found.
//*          = 'V': all eigenvalues in the half-open interval (VL,VU]
//*                 will be found.
//*          = 'I': the IL-th through IU-th eigenvalues will be found.
//*
//*  N       (input) INTEGER
//*          The order of the matrix.  N >= 0.
//*
//*  D       (input/output) DOUBLE PRECISION array, dimension (N)
//*          On entry, the N diagonal elements of the tridiagonal matrix
//*          T. On exit, D is overwritten.
//*
//*  E       (input/output) DOUBLE PRECISION array, dimension (N)
//*          On entry, the (N-1) subdiagonal elements of the tridiagonal
//*          matrix T in elements 1 to N-1 of E. E(N) need not be set on
//*          input, but is used internally as workspace.
//*          On exit, E is overwritten.
//*
//*  VL      (input) DOUBLE PRECISION
//*  VU      (input) DOUBLE PRECISION
//*          If RANGE='V', the lower and upper bounds of the interval to
//*          be searched for eigenvalues. VL < VU.
//*          Not referenced if RANGE = 'A' or 'I'.
//*
//*  IL      (input) INTEGER
//*  IU      (input) INTEGER
//*          If RANGE='I', the indices (in ascending order) of the
//*          smallest and largest eigenvalues to be returned.
//*          1 <= IL <= IU <= N, if N > 0.
//*          Not referenced if RANGE = 'A' or 'V'.
//*
//*  M       (output) INTEGER
//*          The total number of eigenvalues found.  0 <= M <= N.
//*          If RANGE = 'A', M = N, and if RANGE = 'I', M = IU-IL+1.
//*
//*  W       (output) DOUBLE PRECISION array, dimension (N)
//*          The first M elements contain the selected eigenvalues in
//*          ascending order.
//*
//*  Z       (output) DOUBLE PRECISION array, dimension (LDZ, max(1,M) )
//*          If JOBZ = 'V', and if INFO = 0, then the first M columns of Z
//*          contain the orthonormal eigenvectors of the matrix T
//*          corresponding to the selected eigenvalues, with the i-th
//*          column of Z holding the eigenvector associated with W(i).
//*          If JOBZ = 'N', then Z is not referenced.
//*          Note: the user must ensure that at least max(1,M) columns are
//*          supplied in the array Z; if RANGE = 'V', the exact value of M
//*          is not known in advance and can be computed with a workspace
//*          query by setting NZC = -1, see below.
//*
//*  LDZ     (input) INTEGER
//*          The leading dimension of the array Z.  LDZ >= 1, and if
//*          JOBZ = 'V', then LDZ >= max(1,N).
//*
//*  NZC     (input) INTEGER
//*          The number of eigenvectors to be held in the array Z.
//*          If RANGE = 'A', then NZC >= max(1,N).
//*          If RANGE = 'V', then NZC >= the number of eigenvalues in (VL,VU].
//*          If RANGE = 'I', then NZC >= IU-IL+1.
//*          If NZC = -1, then a workspace query is assumed; the
//*          routine calculates the number of columns of the array Z that
//*          are needed to hold the eigenvectors.
//*          This value is returned as the first entry of the Z array, and
//*          no error message related to NZC is issued by XERBLA.
//*
//*  ISUPPZ  (output) INTEGER ARRAY, dimension ( 2*max(1,M) )
//*          The support of the eigenvectors in Z, i.e., the indices
//*          indicating the nonzero elements in Z. The i-th computed eigenvector
//*          is nonzero only in elements ISUPPZ( 2*i-1 ) through
//*          ISUPPZ( 2*i ). This is relevant in the case when the matrix
//*          is split. ISUPPZ is only accessed when JOBZ is 'V' and N > 0.
//*
//*  TRYRAC  (input/output) LOGICAL
//*          If TRYRAC.EQ..TRUE., indicates that the code should check whether
//*          the tridiagonal matrix defines its eigenvalues to high relative
//*          accuracy.  If so, the code uses relative-accuracy preserving
//*          algorithms that might be (a bit) slower depending on the matrix.
//*          If the matrix does not define its eigenvalues to high relative
//*          accuracy, the code can uses possibly faster algorithms.
//*          If TRYRAC.EQ..FALSE., the code is not required to guarantee
//*          relatively accurate eigenvalues and can use the fastest possible
//*          techniques.
//*          On exit, a .TRUE. TRYRAC will be set to .FALSE. if the matrix
//*          does not define its eigenvalues to high relative accuracy.
//*
//*  WORK    (workspace/output) DOUBLE PRECISION array, dimension (LWORK)
//*          On exit, if INFO = 0, WORK(1) returns the optimal
//*          (and minimal) LWORK.
//*
//*  LWORK   (input) INTEGER
//*          The dimension of the array WORK. LWORK >= max(1,18*N)
//*          if JOBZ = 'V', and LWORK >= max(1,12*N) if JOBZ = 'N'.
//*          If LWORK = -1, then a workspace query is assumed; the routine
//*          only calculates the optimal size of the WORK array, returns
//*          this value as the first entry of the WORK array, and no error
//*          message related to LWORK is issued by XERBLA.
//*
//*  IWORK   (workspace/output) INTEGER array, dimension (LIWORK)
//*          On exit, if INFO = 0, IWORK(1) returns the optimal LIWORK.
//*
//*  LIWORK  (input) INTEGER
//*          The dimension of the array IWORK.  LIWORK >= max(1,10*N)
//*          if the eigenvectors are desired, and LIWORK >= max(1,8*N)
//*          if only the eigenvalues are to be computed.
//*          If LIWORK = -1, then a workspace query is assumed; the
//*          routine only calculates the optimal size of the IWORK array,
//*          returns this value as the first entry of the IWORK array, and
//*          no error message related to LIWORK is issued by XERBLA.
//*
//*  INFO    (output) INTEGER
//*          On exit, INFO
//*          = 0:  successful exit
//*          < 0:  if INFO = -i, the i-th argument had an illegal value
//*          > 0:  if INFO = 1X, internal error in DLARRE,
//*                if INFO = 2X, internal error in DLARRV.
//*                Here, the digit X = ABS( IINFO ) < 10, where IINFO is
//*                the nonzero error code returned by DLARRE or
//*                DLARRV, respectively.
//*
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
double ZERO, ONE, FOUR, MINRGP; //DOUBLE PRECISION   ZERO, ONE, FOUR, MINRGP
{  ZERO = 0.0D0, ONE = 1.0D0,FOUR = 4.0D0,MINRGP = 1.0D-3 ; } //PARAMETER          ( ZERO = 0.0D0, ONE = 1.0D0,FOUR = 4.0D0,MINRGP = 1.0D-3 )
//*     ..
//*     .. Local Scalars ..
boolean ALLEIG, INDEIG, LQUERY, VALEIG, WANTZ, ZQUERY; //LOGICAL            ALLEIG, INDEIG, LQUERY, VALEIG, WANTZ, ZQUERY
int I, IBEGIN, IEND, IFIRST, IIL, IINDBL, IINDW,IINDWK, IINFO, IINSPL, IIU, ILAST, IN, INDD,INDE2, INDERR, INDGP, INDGRS, INDWRK, ITMP,ITMP2, J, JBLK, JJ, LIWMIN, LWMIN, NSPLIT,NZCMIN, OFFSET, WBEGIN, WEND; //INTEGER            I, IBEGIN, IEND, IFIRST, IIL, IINDBL, IINDW,IINDWK, IINFO, IINSPL, IIU, ILAST, IN, INDD,INDE2, INDERR, INDGP, INDGRS, INDWRK, ITMP,ITMP2, J, JBLK, JJ, LIWMIN, LWMIN, NSPLIT,NZCMIN, OFFSET, WBEGIN, WEND
double BIGNUM, CS, EPS, PIVMIN, R1, R2, RMAX, RMIN,RTOL1, RTOL2, SAFMIN, SCALE, SMLNUM, SN,THRESH, TMP, TNRM, WL, WU; //DOUBLE PRECISION   BIGNUM, CS, EPS, PIVMIN, R1, R2, RMAX, RMIN,RTOL1, RTOL2, SAFMIN, SCALE, SMLNUM, SN,THRESH, TMP, TNRM, WL, WU
//*     ..
//*     ..
//*     .. External Functions ..
boolean LSAME; //LOGICAL            LSAME
double DLAMCH, DLANST; //DOUBLE PRECISION   DLAMCH, DLANST
EXTERNAL           LSAME, DLAMCH, DLANST; //EXTERNAL           LSAME, DLAMCH, DLANST
//*     ..
//*     .. External Subroutines ..
EXTERNAL           DCOPY, DLAE2, DLAEV2, DLARRC, DLARRE, DLARRJ,DLARRR, DLARRV, DLASRT, DSCAL, DSWAP, XERBLA; //EXTERNAL           DCOPY, DLAE2, DLAEV2, DLARRC, DLARRE, DLARRJ,DLARRR, DLARRV, DLASRT, DSCAL, DSWAP, XERBLA
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC          MAX, MIN, SQRT
//*     ..
//*     .. Executable Statements ..
//*
//*     Test the input parameters.
//*
WANTZ = LSAME( JOBZ, 'V' ); //WANTZ = LSAME( JOBZ, 'V' )
ALLEIG = LSAME( RANGE, 'A' ); //ALLEIG = LSAME( RANGE, 'A' )
VALEIG = LSAME( RANGE, 'V' ); //VALEIG = LSAME( RANGE, 'V' )
INDEIG = LSAME( RANGE, 'I' ); //INDEIG = LSAME( RANGE, 'I' )
//*
LQUERY = ( ( LWORK == -1 ) || ( LIWORK == -1 ) ); //LQUERY = ( ( LWORK.EQ.-1 ).OR.( LIWORK.EQ.-1 ) )
ZQUERY = ( NZC == -1 ); //ZQUERY = ( NZC.EQ.-1 )
//*     DSTEMR needs WORK of size 6*N, IWORK of size 3*N.
//*     In addition, DLARRE needs WORK of size 6*N, IWORK of size 5*N.
//*     Furthermore, DLARRV needs WORK of size 12*N, IWORK of size 7*N.
if ( WANTZ ) { //IF( WANTZ ) THEN
LWMIN = 18*N; //LWMIN = 18*N
LIWMIN = 10*N; //LIWMIN = 10*N
} else { // ELSE
//*        need less workspace if only the eigenvalues are wanted
LWMIN = 12*N; //LWMIN = 12*N
LIWMIN = 8*N; //LIWMIN = 8*N
} // ENDIF
WL = ZERO; //WL = ZERO
WU = ZERO; //WU = ZERO
IIL = 0; //IIL = 0
IIU = 0; //IIU = 0
if ( VALEIG ) { //IF( VALEIG ) THEN
//*        We do not reference VL, VU in the cases RANGE = 'I','A'
//*        The interval (WL, WU] contains all the wanted eigenvalues.
//*        It is either given by the user or computed in DLARRE.
WL = VL; //WL = VL
WU = VU; //WU = VU
ELSEIF( INDEIG ) THEN; //ELSEIF( INDEIG ) THEN
//*        We do not reference IL, IU in the cases RANGE = 'V','A'
IIL = IL; //IIL = IL
IIU = IU; //IIU = IU
} // ENDIF
//*
INFO = 0; //INFO = 0
if (  !( WANTZ  ||  LSAME( JOBZ, 'N' ) ) ) { //IF( .NOT.( WANTZ .OR. LSAME( JOBZ, 'N' ) ) ) THEN
INFO = -1; //INFO = -1
} else if (  !( ALLEIG  ||  VALEIG  ||  INDEIG ) ) { //ELSE IF( .NOT.( ALLEIG .OR. VALEIG .OR. INDEIG ) ) THEN
INFO = -2; //INFO = -2
} else if ( N < 0 ) { //ELSE IF( N.LT.0 ) THEN
INFO = -3; //INFO = -3
} else if ( VALEIG  &&  N > 0  &&  WU <= WL ) { //ELSE IF( VALEIG .AND. N.GT.0 .AND. WU.LE.WL ) THEN
INFO = -7; //INFO = -7
} else if ( INDEIG  &&  ( IIL < 1  ||  IIL > N ) ) { //ELSE IF( INDEIG .AND. ( IIL.LT.1 .OR. IIL.GT.N ) ) THEN
INFO = -8; //INFO = -8
} else if ( INDEIG  &&  ( IIU < IIL  ||  IIU > N ) ) { //ELSE IF( INDEIG .AND. ( IIU.LT.IIL .OR. IIU.GT.N ) ) THEN
INFO = -9; //INFO = -9
} else if ( LDZ < 1  ||  ( WANTZ  &&  LDZ < N ) ) { //ELSE IF( LDZ.LT.1 .OR. ( WANTZ .AND. LDZ.LT.N ) ) THEN
INFO = -13; //INFO = -13
} else if ( LWORK < LWMIN  &&   !LQUERY ) { //ELSE IF( LWORK.LT.LWMIN .AND. .NOT.LQUERY ) THEN
INFO = -17; //INFO = -17
} else if ( LIWORK < LIWMIN  &&   !LQUERY ) { //ELSE IF( LIWORK.LT.LIWMIN .AND. .NOT.LQUERY ) THEN
INFO = -19; //INFO = -19
} // END IF
//*
//*     Get machine constants.
//*
SAFMIN = DLAMCH( 'Safe minimum' ); //SAFMIN = DLAMCH( 'Safe minimum' )
EPS = DLAMCH( 'Precision' ); //EPS = DLAMCH( 'Precision' )
SMLNUM = SAFMIN / EPS; //SMLNUM = SAFMIN / EPS
BIGNUM = ONE / SMLNUM; //BIGNUM = ONE / SMLNUM
RMIN = SQRT( SMLNUM ); //RMIN = SQRT( SMLNUM )
RMAX = MIN( SQRT( BIGNUM ), ONE / SQRT( SQRT( SAFMIN ) ) ); //RMAX = MIN( SQRT( BIGNUM ), ONE / SQRT( SQRT( SAFMIN ) ) )
//*
if ( INFO == 0 ) { //IF( INFO.EQ.0 ) THEN
WORK( 1 ) = LWMIN; //WORK( 1 ) = LWMIN
IWORK( 1 ) = LIWMIN; //IWORK( 1 ) = LIWMIN
//*
if ( WANTZ  &&  ALLEIG ) { //IF( WANTZ .AND. ALLEIG ) THEN
NZCMIN = N; //NZCMIN = N
} else if ( WANTZ  &&  VALEIG ) { //ELSE IF( WANTZ .AND. VALEIG ) THEN
CALL DLARRC( 'T', N, VL, VU, D, E, SAFMIN,NZCMIN, ITMP, ITMP2, INFO ); //CALL DLARRC( 'T', N, VL, VU, D, E, SAFMIN,NZCMIN, ITMP, ITMP2, INFO )
} else if ( WANTZ  &&  INDEIG ) { //ELSE IF( WANTZ .AND. INDEIG ) THEN
NZCMIN = IIU-IIL+1; //NZCMIN = IIU-IIL+1
} else { // ELSE
//*           WANTZ .EQ. FALSE.
NZCMIN = 0; //NZCMIN = 0
} // ENDIF
if ( ZQUERY  &&  INFO == 0 ) { //IF( ZQUERY .AND. INFO.EQ.0 ) THEN
Z( 1,1 ) = NZCMIN; //Z( 1,1 ) = NZCMIN
} else if ( NZC < NZCMIN  &&   !ZQUERY ) { //ELSE IF( NZC.LT.NZCMIN .AND. .NOT.ZQUERY ) THEN
INFO = -14; //INFO = -14
} // END IF
} // END IF
if ( INFO != 0 ) { //IF( INFO.NE.0 ) THEN
//*
CALL XERBLA( 'DSTEMR', -INFO ); //CALL XERBLA( 'DSTEMR', -INFO )
//*
} // RETURN
} else if ( LQUERY  ||  ZQUERY ) { //ELSE IF( LQUERY .OR. ZQUERY ) THEN
} // RETURN
} // END IF
//*
//*     Handle N = 0, 1, and 2 cases immediately
//*
M = 0; //M = 0
if ( N == 0 )RETURN; //IF( N.EQ.0 )RETURN
//*
if ( N == 1 ) { //IF( N.EQ.1 ) THEN
if ( ALLEIG  ||  INDEIG ) { //IF( ALLEIG .OR. INDEIG ) THEN
M = 1; //M = 1
W( 1 ) = D( 1 ); //W( 1 ) = D( 1 )
} else { // ELSE
if ( WL < D( 1 )  &&  WU >= D( 1 ) ) { //IF( WL.LT.D( 1 ) .AND. WU.GE.D( 1 ) ) THEN
M = 1; //M = 1
W( 1 ) = D( 1 ); //W( 1 ) = D( 1 )
} // END IF
} // END IF
if ( WANTZ && ( !ZQUERY) ) { //IF( WANTZ.AND.(.NOT.ZQUERY) ) THEN
Z( 1, 1 ) = ONE; //Z( 1, 1 ) = ONE
ISUPPZ(1) = 1; //ISUPPZ(1) = 1
ISUPPZ(2) = 1; //ISUPPZ(2) = 1
} // END IF
} // RETURN
} // END IF
//*
if ( N == 2 ) { //IF( N.EQ.2 ) THEN
if (  !WANTZ ) { //IF( .NOT.WANTZ ) THEN
CALL DLAE2( D(1), E(1), D(2), R1, R2 ); //CALL DLAE2( D(1), E(1), D(2), R1, R2 )
} else if ( WANTZ && ( !ZQUERY) ) { //ELSE IF( WANTZ.AND.(.NOT.ZQUERY) ) THEN
CALL DLAEV2( D(1), E(1), D(2), R1, R2, CS, SN ); //CALL DLAEV2( D(1), E(1), D(2), R1, R2, CS, SN )
} // END IF
if ( ALLEIG || (VALEIG && (R2 > WL) && (R2 <= WU)) || (INDEIG && (IIL == 1)) ) { //IF( ALLEIG.OR.(VALEIG.AND.(R2.GT.WL).AND.(R2.LE.WU)).OR.(INDEIG.AND.(IIL.EQ.1)) ) THEN
M = M+1; //M = M+1
W( M ) = R2; //W( M ) = R2
if ( WANTZ && ( !ZQUERY) ) { //IF( WANTZ.AND.(.NOT.ZQUERY) ) THEN
Z( 1, M ) = -SN; //Z( 1, M ) = -SN
Z( 2, M ) = CS; //Z( 2, M ) = CS
//*              Note: At most one of SN and CS can be zero.
if  (SN != ZERO) { //IF (SN.NE.ZERO) THEN
if  (CS != ZERO) { //IF (CS.NE.ZERO) THEN
ISUPPZ(2*M-1) = 1; //ISUPPZ(2*M-1) = 1
ISUPPZ(2*M) = 2; //ISUPPZ(2*M) = 2
} else { // ELSE
ISUPPZ(2*M-1) = 1; //ISUPPZ(2*M-1) = 1
ISUPPZ(2*M) = 1; //ISUPPZ(2*M) = 1
} // END IF
} else { // ELSE
ISUPPZ(2*M-1) = 2; //ISUPPZ(2*M-1) = 2
ISUPPZ(2*M) = 2; //ISUPPZ(2*M) = 2
} // END IF
} // ENDIF
} // ENDIF
if ( ALLEIG || (VALEIG && (R1 > WL) && (R1 <= WU)) || (INDEIG && (IIU == 2)) ) { //IF( ALLEIG.OR.(VALEIG.AND.(R1.GT.WL).AND.(R1.LE.WU)).OR.(INDEIG.AND.(IIU.EQ.2)) ) THEN
M = M+1; //M = M+1
W( M ) = R1; //W( M ) = R1
if ( WANTZ && ( !ZQUERY) ) { //IF( WANTZ.AND.(.NOT.ZQUERY) ) THEN
Z( 1, M ) = CS; //Z( 1, M ) = CS
Z( 2, M ) = SN; //Z( 2, M ) = SN
//*              Note: At most one of SN and CS can be zero.
if  (SN != ZERO) { //IF (SN.NE.ZERO) THEN
if  (CS != ZERO) { //IF (CS.NE.ZERO) THEN
ISUPPZ(2*M-1) = 1; //ISUPPZ(2*M-1) = 1
ISUPPZ(2*M) = 2; //ISUPPZ(2*M) = 2
} else { // ELSE
ISUPPZ(2*M-1) = 1; //ISUPPZ(2*M-1) = 1
ISUPPZ(2*M) = 1; //ISUPPZ(2*M) = 1
} // END IF
} else { // ELSE
ISUPPZ(2*M-1) = 2; //ISUPPZ(2*M-1) = 2
ISUPPZ(2*M) = 2; //ISUPPZ(2*M) = 2
} // END IF
} // ENDIF
} // ENDIF
} // RETURN
} // END IF
//*     Continue with general N
INDGRS = 1; //INDGRS = 1
INDERR = 2*N + 1; //INDERR = 2*N + 1
INDGP = 3*N + 1; //INDGP = 3*N + 1
INDD = 4*N + 1; //INDD = 4*N + 1
INDE2 = 5*N + 1; //INDE2 = 5*N + 1
INDWRK = 6*N + 1; //INDWRK = 6*N + 1
//*
IINSPL = 1; //IINSPL = 1
IINDBL = N + 1; //IINDBL = N + 1
IINDW = 2*N + 1; //IINDW = 2*N + 1
IINDWK = 3*N + 1; //IINDWK = 3*N + 1
//*
//*     Scale matrix to allowable range, if necessary.
//*     The allowable range is related to the PIVMIN parameter; see the
//*     comments in DLARRD.  The preference for scaling small values
//*     up is heuristic; we expect users' matrices not to be close to the
//*     RMAX threshold.
//*
SCALE = ONE; //SCALE = ONE
TNRM = DLANST( 'M', N, D, E ); //TNRM = DLANST( 'M', N, D, E )
if ( TNRM > ZERO  &&  TNRM < RMIN ) { //IF( TNRM.GT.ZERO .AND. TNRM.LT.RMIN ) THEN
SCALE = RMIN / TNRM; //SCALE = RMIN / TNRM
} else if ( TNRM > RMAX ) { //ELSE IF( TNRM.GT.RMAX ) THEN
SCALE = RMAX / TNRM; //SCALE = RMAX / TNRM
} // END IF
if ( SCALE != ONE ) { //IF( SCALE.NE.ONE ) THEN
CALL DSCAL( N, SCALE, D, 1 ); //CALL DSCAL( N, SCALE, D, 1 )
CALL DSCAL( N-1, SCALE, E, 1 ); //CALL DSCAL( N-1, SCALE, E, 1 )
TNRM = TNRM*SCALE; //TNRM = TNRM*SCALE
if ( VALEIG ) { //IF( VALEIG ) THEN
//*           If eigenvalues in interval have to be found,
//*           scale (WL, WU] accordingly
WL = WL*SCALE; //WL = WL*SCALE
WU = WU*SCALE; //WU = WU*SCALE
} // ENDIF
} // END IF
//*
//*     Compute the desired eigenvalues of the tridiagonal after splitting
//*     into smaller subblocks if the corresponding off-diagonal elements
//*     are small
//*     THRESH is the splitting parameter for DLARRE
//*     A negative THRESH forces the old splitting criterion based on the
//*     size of the off-diagonal. A positive THRESH switches to splitting
//*     which preserves relative accuracy.
//*
if ( TRYRAC ) { //IF( TRYRAC ) THEN
//*        Test whether the matrix warrants the more expensive relative approach.
CALL DLARRR( N, D, E, IINFO ); //CALL DLARRR( N, D, E, IINFO )
} else { // ELSE
//*        The user does not care about relative accurately eigenvalues
IINFO = -1; //IINFO = -1
} // ENDIF
//*     Set the splitting criterion
if  (IINFO == 0) { //IF (IINFO.EQ.0) THEN
THRESH = EPS; //THRESH = EPS
} else { // ELSE
THRESH = -EPS; //THRESH = -EPS
//*        relative accuracy is desired but T does not guarantee it
TRYRAC =  false; //TRYRAC = .FALSE.
} // ENDIF
//*
if ( TRYRAC ) { //IF( TRYRAC ) THEN
//*        Copy original diagonal, needed to guarantee relative accuracy
CALL DCOPY(N,D,1,WORK(INDD),1); //CALL DCOPY(N,D,1,WORK(INDD),1)
} // ENDIF
//*     Store the squares of the offdiagonal values of T
for ( 5 J = 1; N-1) { //DO 5 J = 1, N-1
WORK( INDE2+J-1 ) = E(J)**2; //WORK( INDE2+J-1 ) = E(J)**2
5    CONTINUE; //5    CONTINUE
//*     Set the tolerance parameters for bisection
if (  !WANTZ ) { //IF( .NOT.WANTZ ) THEN
//*        DLARRE computes the eigenvalues to full precision.
RTOL1 = FOUR * EPS; //RTOL1 = FOUR * EPS
RTOL2 = FOUR * EPS; //RTOL2 = FOUR * EPS
} else { // ELSE
//*        DLARRE computes the eigenvalues to less than full precision.
//*        DLARRV will refine the eigenvalue approximations, and we can
//*        need less accurate initial bisection in DLARRE.
//*        Note: these settings do only affect the subset case and DLARRE
RTOL1 = SQRT(EPS); //RTOL1 = SQRT(EPS)
RTOL2 = MAX( SQRT(EPS)*5.0D-3, FOUR * EPS ); //RTOL2 = MAX( SQRT(EPS)*5.0D-3, FOUR * EPS )
} // ENDIF
CALL DLARRE( RANGE, N, WL, WU, IIL, IIU, D, E,WORK(INDE2), RTOL1, RTOL2, THRESH, NSPLIT,IWORK( IINSPL ), M, W, WORK( INDERR ),WORK( INDGP ), IWORK( IINDBL ),IWORK( IINDW ), WORK( INDGRS ), PIVMIN,WORK( INDWRK ), IWORK( IINDWK ), IINFO ); //CALL DLARRE( RANGE, N, WL, WU, IIL, IIU, D, E,WORK(INDE2), RTOL1, RTOL2, THRESH, NSPLIT,IWORK( IINSPL ), M, W, WORK( INDERR ),WORK( INDGP ), IWORK( IINDBL ),IWORK( IINDW ), WORK( INDGRS ), PIVMIN,WORK( INDWRK ), IWORK( IINDWK ), IINFO )
if ( IINFO != 0 ) { //IF( IINFO.NE.0 ) THEN
INFO = 10 + ABS( IINFO ); //INFO = 10 + ABS( IINFO )
} // RETURN
} // END IF
//*     Note that if RANGE .NE. 'V', DLARRE computes bounds on the desired
//*     part of the spectrum. All desired eigenvalues are contained in
//*     (WL,WU]
if ( WANTZ ) { //IF( WANTZ ) THEN
//*
//*        Compute the desired eigenvectors corresponding to the computed
//*        eigenvalues
//*
CALL DLARRV( N, WL, WU, D, E,PIVMIN, IWORK( IINSPL ), M,1, M, MINRGP, RTOL1, RTOL2,W, WORK( INDERR ), WORK( INDGP ), IWORK( IINDBL ),IWORK( IINDW ), WORK( INDGRS ), Z, LDZ,ISUPPZ, WORK( INDWRK ), IWORK( IINDWK ), IINFO ); //CALL DLARRV( N, WL, WU, D, E,PIVMIN, IWORK( IINSPL ), M,1, M, MINRGP, RTOL1, RTOL2,W, WORK( INDERR ), WORK( INDGP ), IWORK( IINDBL ),IWORK( IINDW ), WORK( INDGRS ), Z, LDZ,ISUPPZ, WORK( INDWRK ), IWORK( IINDWK ), IINFO )
if ( IINFO != 0 ) { //IF( IINFO.NE.0 ) THEN
INFO = 20 + ABS( IINFO ); //INFO = 20 + ABS( IINFO )
} // RETURN
} // END IF
} else { // ELSE
//*        DLARRE computes eigenvalues of the (shifted) root representation
//*        DLARRV returns the eigenvalues of the unshifted matrix.
//*        However, if the eigenvectors are not desired by the user, we need
//*        to apply the corresponding shifts from DLARRE to obtain the
//*        eigenvalues of the original matrix.
for ( 20 J = 1; M) { //DO 20 J = 1, M
ITMP = IWORK( IINDBL+J-1 ); //ITMP = IWORK( IINDBL+J-1 )
W( J ) = W( J ) + E( IWORK( IINSPL+ITMP-1 ) ); //W( J ) = W( J ) + E( IWORK( IINSPL+ITMP-1 ) )
20      CONTINUE; //20      CONTINUE
} // END IF
//*
if  ( TRYRAC ) { //IF ( TRYRAC ) THEN
//*        Refine computed eigenvalues so that they are relatively accurate
//*        with respect to the original matrix T.
IBEGIN = 1; //IBEGIN = 1
WBEGIN = 1; //WBEGIN = 1
for ( 39  JBLK = 1; IWORK( IINDBL+M-1 )) { //DO 39  JBLK = 1, IWORK( IINDBL+M-1 )
IEND = IWORK( IINSPL+JBLK-1 ); //IEND = IWORK( IINSPL+JBLK-1 )
IN = IEND - IBEGIN + 1; //IN = IEND - IBEGIN + 1
WEND = WBEGIN - 1; //WEND = WBEGIN - 1
//*           check if any eigenvalues have to be refined in this block
36         CONTINUE; //36         CONTINUE
if ( WEND < M ) { //IF( WEND.LT.M ) THEN
if ( IWORK( IINDBL+WEND ) == JBLK ) { //IF( IWORK( IINDBL+WEND ).EQ.JBLK ) THEN
WEND = WEND + 1; //WEND = WEND + 1
GO TO 36; //GO TO 36
} // END IF
} // END IF
if ( WEND < WBEGIN ) { //IF( WEND.LT.WBEGIN ) THEN
IBEGIN = IEND + 1; //IBEGIN = IEND + 1
GO TO 39; //GO TO 39
} // END IF
OFFSET = IWORK(IINDW+WBEGIN-1)-1; //OFFSET = IWORK(IINDW+WBEGIN-1)-1
IFIRST = IWORK(IINDW+WBEGIN-1); //IFIRST = IWORK(IINDW+WBEGIN-1)
ILAST = IWORK(IINDW+WEND-1); //ILAST = IWORK(IINDW+WEND-1)
RTOL2 = FOUR * EPS; //RTOL2 = FOUR * EPS
CALL DLARRJ( IN,WORK(INDD+IBEGIN-1), WORK(INDE2+IBEGIN-1),IFIRST, ILAST, RTOL2, OFFSET, W(WBEGIN),WORK( INDERR+WBEGIN-1 ),WORK( INDWRK ), IWORK( IINDWK ), PIVMIN,TNRM, IINFO ); //CALL DLARRJ( IN,WORK(INDD+IBEGIN-1), WORK(INDE2+IBEGIN-1),IFIRST, ILAST, RTOL2, OFFSET, W(WBEGIN),WORK( INDERR+WBEGIN-1 ),WORK( INDWRK ), IWORK( IINDWK ), PIVMIN,TNRM, IINFO )
IBEGIN = IEND + 1; //IBEGIN = IEND + 1
WBEGIN = WEND + 1; //WBEGIN = WEND + 1
39      CONTINUE; //39      CONTINUE
} // ENDIF
//*
//*     If matrix was scaled, then rescale eigenvalues appropriately.
//*
if ( SCALE != ONE ) { //IF( SCALE.NE.ONE ) THEN
CALL DSCAL( M, ONE / SCALE, W, 1 ); //CALL DSCAL( M, ONE / SCALE, W, 1 )
} // END IF
//*
//*     If eigenvalues are not in increasing order, then sort them,
//*     possibly along with eigenvectors.
//*
if ( NSPLIT > 1 ) { //IF( NSPLIT.GT.1 ) THEN
if (  ! WANTZ ) { //IF( .NOT. WANTZ ) THEN
CALL DLASRT( 'I', M, W, IINFO ); //CALL DLASRT( 'I', M, W, IINFO )
if ( IINFO != 0 ) { //IF( IINFO.NE.0 ) THEN
INFO = 3; //INFO = 3
} // RETURN
} // END IF
} else { // ELSE
for ( 60 J = 1; M - 1) { //DO 60 J = 1, M - 1
I = 0; //I = 0
TMP = W( J ); //TMP = W( J )
for ( 50 JJ = J + 1; M) { //DO 50 JJ = J + 1, M
if ( W( JJ ) < TMP ) { //IF( W( JJ ).LT.TMP ) THEN
I = JJ; //I = JJ
TMP = W( JJ ); //TMP = W( JJ )
} // END IF
50            CONTINUE; //50            CONTINUE
if ( I != 0 ) { //IF( I.NE.0 ) THEN
W( I ) = W( J ); //W( I ) = W( J )
W( J ) = TMP; //W( J ) = TMP
if ( WANTZ ) { //IF( WANTZ ) THEN
CALL DSWAP( N, Z( 1, I ), 1, Z( 1, J ), 1 ); //CALL DSWAP( N, Z( 1, I ), 1, Z( 1, J ), 1 )
ITMP = ISUPPZ( 2*I-1 ); //ITMP = ISUPPZ( 2*I-1 )
ISUPPZ( 2*I-1 ) = ISUPPZ( 2*J-1 ); //ISUPPZ( 2*I-1 ) = ISUPPZ( 2*J-1 )
ISUPPZ( 2*J-1 ) = ITMP; //ISUPPZ( 2*J-1 ) = ITMP
ITMP = ISUPPZ( 2*I ); //ITMP = ISUPPZ( 2*I )
ISUPPZ( 2*I ) = ISUPPZ( 2*J ); //ISUPPZ( 2*I ) = ISUPPZ( 2*J )
ISUPPZ( 2*J ) = ITMP; //ISUPPZ( 2*J ) = ITMP
} // END IF
} // END IF
60         CONTINUE; //60         CONTINUE
} // END IF
} // ENDIF
//*
//*
WORK( 1 ) = LWMIN; //WORK( 1 ) = LWMIN
IWORK( 1 ) = LIWMIN; //IWORK( 1 ) = LIWMIN
} // RETURN
//*
//*     End of DSTEMR
//*
} // END