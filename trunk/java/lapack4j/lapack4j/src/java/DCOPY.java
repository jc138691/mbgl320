package lapack4j.src.java;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,18/10/11,1:16 PM
 */
public class DCOPY { //SUBROUTINE DCOPY(N,DX,INCX,DY,INCY)
  public static void DCOPY(int N, double[] DX, int INCX, double[] DY, int INCY) { //SUBROUTINE DCOPY(N,DX,INCX,DY,INCY)
    //public class DCOPY { //SUBROUTINE DCOPY(N,DX,INCX,DY,INCY)
    //public static void DCOPY(N,DX,INCX,DY,INCY) { //SUBROUTINE DCOPY(N,DX,INCX,DY,INCY)
    //*     .. Scalar Arguments ..
    //int INCX,INCY,N; //INTEGER INCX,INCY,N
    //*     ..
    //*     .. Array Arguments ..
    //double DX[],DY[]; //DOUBLE PRECISION DX(*),DY(*)
    //*     ..
    //*
    //*  Purpose
    //*  =======
    //*
    //*     DCOPY copies a vector, x, to a vector, y.
    //*     uses unrolled loops for increments equal to one.
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
    int I,IX,IY,M,MP1; //INTEGER I,IX,IY,M,MP1
    //*     ..
    //*     .. Intrinsic Functions ..
    //INTRINSIC MOD
    //*     ..
    if  (N <= 0) return; //IF (N.LE.0) RETURN
    if  (INCX == 1  &&  INCY == 1) { //IF (INCX.EQ.1 .AND. INCY.EQ.1) THEN
      //*
      //*        code for both increments equal to 1
      //*
      //*
      //*        clean-up loop
      //*
      M = N%7; //M = MOD(N,7)
      if  (M != 0) { //IF (M.NE.0) THEN
        for ( I = 0; I < M; I++) { //DO I = 1,M
          DY[I] = DX[I]; //DY(I) = DX(I)
        } // END DO
        if  (N < 7) return; //IF (N.LT.7) RETURN
      } // END IF
      MP1 = M + 1; //MP1 = M + 1
      for ( I = MP1 - 1; I < N ; I += 7) { //DO I = MP1,N,7
        DY[I] = DX[I]; //DY(I) = DX(I)
        DY[I+1] = DX[I+1]; //DY(I+1) = DX(I+1)
        DY[I+2] = DX[I+2]; //DY(I+2) = DX(I+2)
        DY[I+3] = DX[I+3]; //DY(I+3) = DX(I+3)
        DY[I+4] = DX[I+4]; //DY(I+4) = DX(I+4)
        DY[I+5] = DX[I+5]; //DY(I+5) = DX(I+5)
        DY[I+6] = DX[I+6]; //DY(I+6) = DX(I+6)
      } // END DO
    } else { // ELSE
      //*
      //*        code for unequal increments or equal increments
      //*          not equal to 1
      //*
      IX = 1; //IX = 1
      IY = 1; //IY = 1
      for ( I = 0; I < N; I++) { //DO I = 1,N
        DY[IY] = DX[IX]; //DY(IY) = DX(IX)
        IX = IX + INCX; //IX = IX + INCX
        IY = IY + INCY; //IY = IY + INCY
      } // END DO
    } // END IF
  } // RETURN
} // END