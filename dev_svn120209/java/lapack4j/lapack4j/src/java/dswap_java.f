public class DSWAP { //SUBROUTINE DSWAP(N,DX,INCX,DY,INCY)
public static void DSWAP(N,DX,INCX,DY,INCY) { //SUBROUTINE DSWAP(N,DX,INCX,DY,INCY)
//*     .. Scalar Arguments ..
int INCX,INCY,N; //INTEGER INCX,INCY,N
//*     ..
//*     .. Array Arguments ..
double DX[],DY[]; //DOUBLE PRECISION DX(*),DY(*)
//*     ..
//*
//*  Purpose
//*  =======
//*
//*     interchanges two vectors.
//*     uses unrolled loops for increments equal one.
//*
//*  Further Details
//*  ===============
//*
//*     jack dongarra, linpack, 3/11/78.
//*     modified 12/3/93, array(1) declarations changed to array(*)
//*
//*  =====================================================================
//*
//*     .. Local Scalars ..
double DTEMP; //DOUBLE PRECISION DTEMP
int I,IX,IY,M,MP1; //INTEGER I,IX,IY,M,MP1
//*     ..
//*     .. Intrinsic Functions ..
//INTRINSIC MOD
//*     ..
if  (N <= 0) return; //IF (N.LE.0) RETURN
if  (INCX == 1  &&  INCY == 1) { //IF (INCX.EQ.1 .AND. INCY.EQ.1) THEN
//*
//*       code for both increments equal to 1
//*
//*
//*       clean-up loop
//*
M = MOD(N,3); //M = MOD(N,3)
if  (M != 0) { //IF (M.NE.0) THEN
for ( I = 1;M) { //DO I = 1,M
DTEMP = DX(I); //DTEMP = DX(I)
DX(I) = DY(I); //DX(I) = DY(I)
DY(I) = DTEMP; //DY(I) = DTEMP
} // END DO
if  (N < 3) return; //IF (N.LT.3) RETURN
} // END IF
MP1 = M + 1; //MP1 = M + 1
for ( I = MP1;N;3) { //DO I = MP1,N,3
DTEMP = DX(I); //DTEMP = DX(I)
DX(I) = DY(I); //DX(I) = DY(I)
DY(I) = DTEMP; //DY(I) = DTEMP
DTEMP = DX(I+1); //DTEMP = DX(I+1)
DX(I+1) = DY(I+1); //DX(I+1) = DY(I+1)
DY(I+1) = DTEMP; //DY(I+1) = DTEMP
DTEMP = DX(I+2); //DTEMP = DX(I+2)
DX(I+2) = DY(I+2); //DX(I+2) = DY(I+2)
DY(I+2) = DTEMP; //DY(I+2) = DTEMP
} // END DO
} else { // ELSE
//*
//*       code for unequal increments or equal increments not equal
//*         to 1
//*
IX = 1; //IX = 1
IY = 1; //IY = 1
if  (INCX < 0) IX = (-N+1)*INCX + 1; //IF (INCX.LT.0) IX = (-N+1)*INCX + 1
if  (INCY < 0) IY = (-N+1)*INCY + 1; //IF (INCY.LT.0) IY = (-N+1)*INCY + 1
for ( I = 1;N) { //DO I = 1,N
DTEMP = DX(IX); //DTEMP = DX(IX)
DX(IX) = DY(IY); //DX(IX) = DY(IY)
DY(IY) = DTEMP; //DY(IY) = DTEMP
IX = IX + INCX; //IX = IX + INCX
IY = IY + INCY; //IY = IY + INCY
} // END DO
} // END IF
} // RETURN
} // END