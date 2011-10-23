public class DSCAL { //SUBROUTINE DSCAL(N,DA,DX,INCX)
public static void DSCAL(N,DA,DX,INCX) { //SUBROUTINE DSCAL(N,DA,DX,INCX)
//*     .. Scalar Arguments ..
double DA; //DOUBLE PRECISION DA
int INCX,N; //INTEGER INCX,N
//*     ..
//*     .. Array Arguments ..
double DX[]; //DOUBLE PRECISION DX(*)
//*     ..
//*
//*  Purpose
//*  =======
//*
//*     DSCAL scales a vector by a constant.
//*     uses unrolled loops for increment equal to one.
//*
//*  Further Details
//*  ===============
//*
//*     jack dongarra, linpack, 3/11/78.
//*     modified 3/93 to return if incx .le. 0.
//*     modified 12/3/93, array(1) declarations changed to array(*)
//*
//*  =====================================================================
//*
//*     .. Local Scalars ..
int I,M,MP1,NINCX; //INTEGER I,M,MP1,NINCX
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC MOD
//*     ..
if  (N <= 0  ||  INCX <= 0) return; //IF (N.LE.0 .OR. INCX.LE.0) RETURN
if  (INCX == 1) { //IF (INCX.EQ.1) THEN
//*
//*        code for increment equal to 1
//*
//*
//*        clean-up loop
//*
M = MOD(N,5); //M = MOD(N,5)
if  (M != 0) { //IF (M.NE.0) THEN
for ( I = 1;M) { //DO I = 1,M
DX(I) = DA*DX(I); //DX(I) = DA*DX(I)
} // END DO
if  (N < 5) return; //IF (N.LT.5) RETURN
} // END IF
MP1 = M + 1; //MP1 = M + 1
for ( I = MP1;N;5) { //DO I = MP1,N,5
DX(I) = DA*DX(I); //DX(I) = DA*DX(I)
DX(I+1) = DA*DX(I+1); //DX(I+1) = DA*DX(I+1)
DX(I+2) = DA*DX(I+2); //DX(I+2) = DA*DX(I+2)
DX(I+3) = DA*DX(I+3); //DX(I+3) = DA*DX(I+3)
DX(I+4) = DA*DX(I+4); //DX(I+4) = DA*DX(I+4)
} // END DO
} else { // ELSE
//*
//*        code for increment not equal to 1
//*
NINCX = N*INCX; //NINCX = N*INCX
for ( I = 1;NINCX;INCX) { //DO I = 1,NINCX,INCX
DX(I) = DA*DX(I); //DX(I) = DA*DX(I)
} // END DO
} // END IF
} // RETURN
} // END