package math.func;
import static java.lang.Math.*;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 12:07:12
 */
// see CmplxGamma
public class FuncGamma implements Func {
  public static Log log = Log.getLog(FuncGamma.class);
  //  private static double PI = Math.PI;
  private static double PI = 3.14159265358979324; //      DATA PI /3.14159 26535 89793 24D0/
  private static double C1 = 2.50662827463100050; //      DATA C1 /2.50662 82746 31000 50D0/
  private static double[] C = {//      DIMENSION C(0:15)
    41.624436916439068
    , -51.224241022374774
    , 11.338755813488977
    , -0.747732687772388
    , +0.008782877493061
    , -0.000001899030264
    , +0.000000001946335
    , -0.000000000199345
    , +0.000000000008433
    , +0.000000000001486
    , -0.000000000000806
    , +0.000000000000293
    , -0.000000000000102
    , +0.000000000000037
    , -0.000000000000014
    , +0.000000000000006};
  //        FUNCTION WGAMMA(Z)
  public double calc(double Z) {
//      IMPLICIT DOUBLE PRECISION (A-H,O-Z)
//      COMPLEX*16     WGAMMA, Z,U,V,F,H,S
    double HF = 1./2.; //      PARAMETER (Z1 = 1, HF = Z1/2)
//      DOUBLE PRECISION  GREAL,GIMAG,XARG,YARG
//      COMPLEX*16  ZARG,GCONJG,GCMPLX
//      GREAL( ZARG)=DREAL( ZARG)
//      GIMAG( ZARG)=DIMAG( ZARG)
//      GCONJG(ZARG)=DCONJG(ZARG)
//      GCMPLX(XARG,YARG)=DCMPLX(XARG,YARG)
    double one = 1.;
    double F = 0;
    double H = 0;
    double V = 0;
    double U = Z; //      U=Z
    double X = U;   //      X=U
    if (-Math.abs(X) == ((int)X)) { //      IF(GIMAG(U) .EQ. 0 .AND. -ABS(X) .EQ. INT(X)) THEN
      throw new IllegalArgumentException(log.error("ARGUMENT EQUALS NON-POSITIVE INTEGER = " + X)); //  101 FORMAT('ARGUMENT EQUALS NON-POSITIVE INTEGER = ',1P,E15.1)
      //       F=0
      //       H=0
      //       WRITE(ERRTXT,101) X
      //       CALL MTLPRT(NAME,'C305.1',ERRTXT)
    } else { //      ELSE
      if (X >= 1.) {      //       IF(X .GE. 1) THEN
        F = 1;            //        F=1
        V = U;            //        V=U
      } else if (X >= 0) {//       ELSEIF(X .GE. 0) THEN
        F = 1. / U;       //        F=1/U
        V = 1. + U;       //        V=1+U
      }else{              //       ELSE
        F = 1.;           //        F=1
        V = 1. - U;       //        V=1-U
      }                   //       END IF
      H = 1.;             //       H=1
      double S = C[0];    //       S=C(0)
      for (int K = 1; K < C.length; K++) {        //       DO 1 K = 1,15
        H = ((V-K) / (V+(K-1)))*H;//       H=((V-K)/(V+(K-1)))*H
        S = S + C[K] * H; //    1  S=S+C(K)*H
      }
      H = V + (4.+HF);    //       H=V+(4+HF)
      double V2 = (V - HF) * Math.log(H);           //(V-HF)*LOG(H)
      H = exp(V2 - H) * S * C1; //       H=C1*EXP((V-HF)*LOG(H)-H)*S
      if (X < 0) H = PI / (sin(PI * U) * H); //       IF(X .LT. 0) H=PI/(SIN(PI*U)*H)
    } //      ENDIF
    double WGAMMA = F * H; //      WGAMMA=F*H
    return WGAMMA; //      RETURN
  } //      END
}

