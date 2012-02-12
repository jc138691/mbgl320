package lapack4j;
import lapack4j.utils.Fortran77;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,4/10/11,2:40 PM
 */
public class tred2 {
  //      startsSubr tred2(nm,n,a,d,e,z)
  public static void tred2(int nm, int n, double[][] a, double[] d, double[] e, double[][] z) {
    int i, j, k, L, ii, jp1;//      integer i,j,k,l,n,ii,nm,jp1
    int ixI, ixJ, ixK, ixL;//NEW
    //      double precision a(nm,n),d(n),e(n),z(nm,n)
    double f, g, h, hh, scale;//      double precision f,g,h,hh,scale
    double scaleRev = 0, hRev = 0;
    //2011-Oct: Converted to java by dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com
    //c
    //c     this startsSubr is a translation of the algol procedure tred2,
    //c     num. math. 11, 181-195(1968) by martin, reinsch, and wilkinson.
    //c     handbook for auto. comp., vol.ii-linear algebra, 212-226(1971).
    //c
    //c     this startsSubr reduces a real symmetric matrix to a
    //c     symmetric tridiagonal matrix using and accumulating
    //c     orthogonal similarity transformations.
    //c
    //c     on input
    //c
    //c        nm must be set to the row dimension of two-dimensional
    //c          array parameters as declared in the calling program
    //c          dimension statement.
    //c
    //c        n is the order of the matrix.
    //c
    //c        a contains the real symmetric input matrix.  only the
    //c          lower triangle of the matrix need be supplied.
    //c
    //c     on output
    //c
    //c        d contains the diagonal elements of the tridiagonal matrix.
    //c
    //c        e contains the subdiagonal elements of the tridiagonal
    //c          matrix in its last n-1 positions.  e(1) is set to zero.
    //c
    //c        z contains the orthogonal transformation matrix
    //c          produced in the reduction.
    //c
    //c        a and z may coincide.  if distinct, a is unaltered.
    //c
    //c     questions and comments should be directed to burton s. garbow,
    //c     mathematics and computer science div, argonne national laboratory
    //c
    //c     this version dated august 1983.
    //c
    //c     ------------------------------------------------------------------
    //c
    //      do 100 i = 1, n
    //         do 80 j = i, n
    //   80    z(j,i) = a(j,i)
    //         d(i) = a(n,i)
    //  100 continue
    Dbl2D.copyFast(a, z, n, n);//NEW
    DblArr.copy(a[n], d, n);//NEW
    // // Setting up the java's goto      // see http://stackoverflow.com/questions/1487124/translate-goto-statements-to-if-switch-while-break-etc
    int goTo = 0;  while (true) { switch (goTo) {  case 0://NEW
    if (n == 1) { goTo = 510;   continue;}//      if (n .eq. 1) go to 510
    //c     .......... for i=n step -1 until 2 do -- ..........
    for (ii = 2; ii <= n; ii++) {//      do 300 ii = 2, n
      i = n + 2 - ii;//         i = n + 2 - ii
      ixI = i - 1;// NEW
      L = i - 1;//         l = i - 1
      ixL = L - 1;// NEW
      h = 0.0D;//         h = 0.0d0
      scale = 0.0D;//         scale = 0.0d0
      int goTo2 = 0;  while (true) {  switch (goTo2) {   case 0:   // NEED NEW GOTOs, WE ARE IN A NEW LOOP
      if (L < 2) { goTo2 = 130; continue;} //         if (l .lt. 2) go to 130
      //c     .......... scale row (algol tol then not needed) ..........
      //         do 120 k = 1, l
      //  120    scale = scale + dabs(d(k))
      scale = DblArr.sumAbs(scale, d, L);
      if (scale != 0) { goTo2 = 140; continue;}//         if (scale .ne. 0.0d0) go to 140
      case 130: e[ixI] = d[ixL];//  130    e(i) = d(l)
      for (ixJ = 0; ixJ < L; ixJ++) {//         do 135 j = 1, l
        d[ixJ] = z[ixL][ixJ];//            d(j) = z(l,j)
        z[ixI][ixJ] = 0.0D;//            z(i,j) = 0.0d0
        z[ixJ][ixI] = 0.0D;//            z(j,i) = 0.0d0
      }//  135    continue
//      DblArr.copy(z[L], d, L);
//      DblArr.set(z[i - 1], 0, L);
//      Dbl2D.setCol(z, i - 1, 0, L);
      goTo2 = 290; continue;//         go to 290
      case 140:
      scaleRev = 1.0D / scale;
      for (ixK = 0; ixK < L; ixK++) {//  140    do 150 k = 1, l
        d[ixK] = d[ixK] * scaleRev;//            d(k) = d(k) / scale
        h = h + d[ixK] * d[ixK];//            h = h + d(k) * d(k)
      }//  150    continue
      f = d[ixL];//         f = d(l)
      g = -Fortran77.sign(Math.sqrt(h), f);    //         g = -dsign(dsqrt(h),f) //Returns `ABS(A)*s', where s is +1 if `B.GE.0', -1 otherwise.
      e[ixI] = scale * g;//         e(i) = scale * g
      h = h - f * g;//         h = h - f * g
      d[ixL] = f - g;//         d(l) = f - g
      //c     .......... form a*u ..........
      //         do 170 j = 1, l
      //  170    e(j) = 0.0d0
      DblArr.set(e, 0.0D, L);
      for (j = 1; j <= L; j++) {//         do 240 j = 1, l
          ixJ = j - 1;
          f = d[ixJ];//            f = d(j)
          z[ixJ][ixI] = f;//            z(j,i) = f
          g = e[ixJ] + z[ixJ][ixJ] * f;//            g = e(j) + z(j,j) * f
          jp1 = j + 1;//            jp1 = j + 1
          int goTo3 = 0;  while (true) {  switch (goTo3) {   case 0:
          if (L < jp1) { goTo3 = 220; continue;}//            if (l .lt. jp1) go to 220
          for (ixK = jp1-1; ixK < L; ixK++) {//            do 200 k = jp1, l
            g = g + z[ixK][ixJ] * d[ixK];//               g = g + z(k,j) * d(k)
            e[ixK] = e[ixK] + z[ixK][ixJ] * f;//               e(k) = e(k) + z(k,j) * f
          }//  200       continue      
          case 220: e[ixJ] = g;//  220       e(j) = g
      }}}//  240    continue
      //c     .......... form p ..........
      f = 0.0D; //         f = 0.0d0
      hRev = 1.D / h;
      for (ixJ = 0; ixJ < L; ixJ++) {//         do 245 j = 1, l
        e[ixJ] = e[ixJ] * hRev; //            e(j) = e(j) / h
        f = f + e[ixJ] * d[ixJ]; //            f = f + e(j) * d(j)
      }//  245    continue
      hh = f / (h + h); //         hh = f / (h + h)
      //c     .......... form q ..........
      for (ixJ = 0; ixJ < L; ixJ++) {//         do 250 j = 1, l
        e[ixJ] = e[ixJ] - hh * d[ixJ]; }//  250    e(j) = e(j) - hh * d(j)
      //c     .......... form reduced a ..........
      for (ixJ = 0; ixJ < L; ixJ++) {//         do 280 j = 1, l
        f = d[ixJ]; //            f = d(j)
        g = e[ixJ]; //            g = e(j)
      //            do 260 k = j, l
      //  260       z(k,j) = z(k,j) - f * e(k) - g * d(k)
        d[ixJ] = z[ixL][ixJ]; //            d(j) = z(l,j)
        z[ixI][ixJ] = 0.0D; //            z(i,j) = 0.0d0
      }//  280    continue
      case 290: d[ixI] = h;//  290    d(i) = h
    } }}//  300 continue
    //c     .......... accumulation of transformation matrices ..........
    for (i = 2; i <= n; i++) {//      do 500 i = 2, n
      //         l = i - 1
      //         z(n,l) = z(l,l)
      //         z(l,l) = 1.0d0
      //         h = d(i)
      //         if (h .eq. 0.0d0) go to 380
      //c
      //         do 330 k = 1, l
      //  330    d(k) = z(k,i) / h
      //c
      //         do 360 j = 1, l
      //            g = 0.0d0
      //c
      //            do 340 k = 1, l
      //  340       g = g + z(k,i) * z(k,j)
      //c
      //            do 360 k = 1, l
      //               z(k,j) = z(k,j) - g * d(k)
      //  360    continue
      //c
      //  380    do 400 k = 1, l
      //  400    z(k,i) = 0.0d0
      //c
      //  500 continue
    }
    //c
  case 510:
    //  510 do 520 i = 1, n
    //         d(i) = z(n,i)
    //         z(n,i) = 0.0d0
    //  520 continue
    DblArr.copy(z[n], d, n);
    DblArr.set(z[n], 0, n);
    //c
    //      z(n,n) = 1.0d0
    z[n][n] = 1.0;
    //      e(1) = 0.0d0
    e[0] = 0.0;
    //      return
    return;
  //      end
  }}} // the two }} are the end of the GOTOs
}