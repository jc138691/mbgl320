package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,4/10/11,1:38 PM
 */
public class pythag  {
  public static double pythag(double a, double b) {
    //      double precision function pythag(a,b)
    //      double precision a,b
    //c
    //c     finds dsqrt(a**2+b**2) without overflow or destructive underflow
    //c
    //      double precision p,r,s,t,u
    double p,r,s,t,u;
    //      p = dmax1(dabs(a),dabs(b))
    double aa = Math.abs(a);
    double ab = Math.abs(b);
    p = Math.max(aa, ab);
    //  if (p .eq. 0.0d0) go to 20
    if (p == 0) {
      return 0;
    }
    // r = (dmin1(dabs(a),dabs(b))/p)**2
    r = Math.min(aa, ab) / p;
    r = r * r;
    // 10 continue
    for ( ; ; ) {
      // t = 4.0d0 + r
      t = 4.0 + r;
      // if (t .eq. 4.0d0) go to 20
      if (t == 4.0) {
        return p;
      }
      // s = r/t
      s = r / t;
      // u = 1.0d0 + 2.0d0*s
      u = 1.0 + 2.0 * s;
      // p = u*p
      p = u * p;
      //         r = (s/u)**2 * r
      double su = s / u;
      r = su * su * r;
      //      go to 10
    }
    //   20 pythag = p
    //      return
    //      end
  }
}
