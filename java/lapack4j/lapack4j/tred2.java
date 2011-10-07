package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,4/10/11,2:40 PM
 */
public class tred2 {
  //      subroutine tred2(nm,n,a,d,e,z)
  public static void tred2(int nm, int n, double[][] a, double[] d, double[] e, double[][] z) {
    // // Setting up the java's goto      // see http://stackoverflow.com/questions/1487124/translate-goto-statements-to-if-switch-while-break-etc
    int goTo = 0;
    while (true) {
      switch (goTo) {
        case 0:
          //c
          //      integer i,j,k,l,n,ii,nm,jp1
          int i, j, k, L, ii, jp1;
          //      double precision a(nm,n),d(n),e(n),z(nm,n)
          //      double precision f,g,h,hh,scale
          double f, g, h, hh, scale;
          //2011-Oct: Converted to java by dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com
          //c
          //c     this subroutine is a translation of the algol procedure tred2,
          //c     num. math. 11, 181-195(1968) by martin, reinsch, and wilkinson.
          //c     handbook for auto. comp., vol.ii-linear algebra, 212-226(1971).
          //c
          //c     this subroutine reduces a real symmetric matrix to a
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
          //c
          //         do 80 j = i, n
          //   80    z(j,i) = a(j,i)
          //c
          //         d(i) = a(n,i)
          //  100 continue
          Dbl2D.copyFast(a, z, n, n);
          DblArr.copy(a[n], d, n);
          //c
          //      if (n .eq. 1) go to 510
          if (n == 1) { goTo = 510;   continue;}
          //c     .......... for i=n step -1 until 2 do -- ..........
          //      do 300 ii = 2, n
          for (ii = 2; ii <= n; ii++) {
            //         i = n + 2 - ii
            i = n + 2 - ii;
            int idxI = i - 1;
            //         l = i - 1
            L = i - 1;
            int idxL = L - 1;
            //         h = 0.0d0
            h = 0.0;
            //         scale = 0.0d0
            scale = 0.0;
            //         if (l .lt. 2) go to 130
            int goTo2 = 0;  while (true) {  switch (goTo2) {   case 0:
            if (L < 2) { goTo2 = 130; continue;}
            //c     .......... scale row (algol tol then not needed) ..........
            //         do 120 k = 1, l
            //  120    scale = scale + dabs(d(k))
            scale = DblArr.sumAbs(scale, d, L);
            //c
            //         if (scale .ne. 0.0d0) go to 140
            if (scale != 0) { goTo2 = 140; continue;}
            //  130    e(i) = d(l)
            case 130: e[idxI] = d[idxL];
            //c
            //         do 135 j = 1, l
              for (j = 0; j < L; j++) {
            //            d(j) = z(l,j)
                d[j] = z[idxL][j];
            //            z(i,j) = 0.0d0
                z[idxI][j] = 0.0;
            //            z(j,i) = 0.0d0
                z[j][idxI] = 0.0;
            //  135    continue
              }
// todo: Is it better to use the following? Likely not.
//              DblArr.copy(z[L], d, L);
//              DblArr.set(z[i - 1], 0, L);
//              Dbl2D.setCol(z, i - 1, 0, L);
            //c
            //         go to 290
              goTo2 = 290; continue;
            //c
            case 140:
            //  140    do 150 k = 1, l
              for (k = 0; k < L; k++) {
            //            d(k) = d(k) / scale
                d[k] = d[k] / scale;
            //            h = h + d(k) * d(k)
                h = h + d[k] * d[k];
            //  150    continue
              }
            //c
            //         f = d(l)
              f = d[idxL];
            //         g = -dsign(dsqrt(h),f)
              g = -Fortran77.sign(Math.sqrt(h), f);    //Returns `ABS(A)*s', where s is +1 if `B.GE.0', -1 otherwise.
            //         e(i) = scale * g
            //         h = h - f * g
            //         d(l) = f - g
            //c     .......... form a*u ..........
            //         do 170 j = 1, l
            //  170    e(j) = 0.0d0
            //c
            //         do 240 j = 1, l
            //            f = d(j)
            //            z(j,i) = f
            //            g = e(j) + z(j,j) * f
            //            jp1 = j + 1
            //            if (l .lt. jp1) go to 220
            //c
            //            do 200 k = jp1, l
            //               g = g + z(k,j) * d(k)
            //               e(k) = e(k) + z(k,j) * f
            //  200       continue
            //c
            case 220:
            //  220       e(j) = g
            //  240    continue
            //c     .......... form p ..........
            //         f = 0.0d0
            //c
            //         do 245 j = 1, l
            //            e(j) = e(j) / h
            //            f = f + e(j) * d(j)
            //  245    continue
            //c
            //         hh = f / (h + h)
            //c     .......... form q ..........
            //         do 250 j = 1, l
            //  250    e(j) = e(j) - hh * d(j)
            //c     .......... form reduced a ..........
            //         do 280 j = 1, l
            //            f = d(j)
            //            g = e(j)
            //c
            //            do 260 k = j, l
            //  260       z(k,j) = z(k,j) - f * e(k) - g * d(k)
            //c
            //            d(j) = z(l,j)
            //            z(i,j) = 0.0d0
            //  280    continue
            //c
            //  290    d(i) = h
            case 290: d[i-1] = h;
            //  300 continue
          } }}
          //c     .......... accumulation of transformation matrices ..........
          //      do 500 i = 2, n
          for (i = 2; i <= n; i++) {
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
      }
    }
  } // the two }} are the end of the GOTOs
}