package math.complex;
import flanagan.complex.Cmplx;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

import static java.lang.Math.PI;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 1/09/2008, Time: 14:03:22
 */
public class CmplxGamma extends FlowTest {
  public static Log log = Log.getLog(CmplxGamma.class);
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

  public CmplxGamma() {
    super(CmplxGamma.class);
  }
  //        FUNCTION WGAMMA(Z)
  public static Cmplx calc(Cmplx Z) {
//      IMPLICIT DOUBLE PRECISION (A-H,O-Z)
//      COMPLEX*16     WGAMMA, Z,U,V,F,H,S
    Cmplx HF = new Cmplx(0.5); //      PARAMETER (Z1 = 1, HF = Z1/2)
//      DOUBLE PRECISION  GREAL,GIMAG,XARG,YARG
//      COMPLEX*16  ZARG,GCONJG,GCMPLX
//      GREAL( ZARG)=DREAL( ZARG)
//      GIMAG( ZARG)=DIMAG( ZARG)
//      GCONJG(ZARG)=DCONJG(ZARG)
//      GCMPLX(XARG,YARG)=DCMPLX(XARG,YARG)
    Cmplx one = new Cmplx(1);
    Cmplx F = new Cmplx();
    Cmplx H = new Cmplx();
    Cmplx V = new Cmplx();
    Cmplx U = new Cmplx(Z); //      U=Z
    double X = U.getReal();   //      X=U
    if (U.getIm() == 0
      &&  -Math.abs(X) == ((int)X)) { //      IF(GIMAG(U) .EQ. 0 .AND. -ABS(X) .EQ. INT(X)) THEN
      throw new IllegalArgumentException(log.error("ARGUMENT EQUALS NON-POSITIVE INTEGER = " + X)); //  101 FORMAT('ARGUMENT EQUALS NON-POSITIVE INTEGER = ',1P,E15.1)
      //       F=0
      //       H=0
      //       WRITE(ERRTXT,101) X
      //       CALL MTLPRT(NAME,'C305.1',ERRTXT)
    } else { //      ELSE
      if (X >= 1.) {          //       IF(X .GE. 1) THEN
        F = new Cmplx(1);     //        F=1
        V = new Cmplx(U);     //        V=U
      } else if (X >= 0) {    //       ELSEIF(X .GE. 0) THEN
        F = one.div(U);       //        F=1/U
        V = one.add(U);       //        V=1+U
      }else{                  //       ELSE
        F.setRe(1);           //        F=1
        V = one.minus(U);     //        V=1-U
      }                       //       END IF
      H = new Cmplx(1);       //       H=1
      Cmplx S = new Cmplx(C[0]);       //       S=C(0)
      for (int K = 1; K < C.length; K++) {        //       DO 1 K = 1,15
        H = V.minus(K).div(V.add(K - 1)).mult(H); //       H=((V-K)/(V+(K-1)))*H
        S.addToSelf(H.times(C[K]));                //    1  S=S+C(K)*H
      }
      H = V.add(HF.add(4)); //       H=V+(4+HF)
      Cmplx V2 = V.minus(HF).mult(H.log());           //(V-HF)*LOG(H)
      H = V2.minus(H).exp().mult(S).times(C1);//       H=C1*EXP((V-HF)*LOG(H)-H)*S
      if (X < 0) H = new Cmplx(PI).div(U.times(PI).sin().mult(H)); //       IF(X .LT. 0) H=PI/(SIN(PI*U)*H)
    } //      ENDIF
    Cmplx WGAMMA = F.mult(H); //      WGAMMA=F*H
    return WGAMMA; //      RETURN
  } //      END

  public void testCalc() throws Exception {  //log.setDbg();
    Cmplx z;
    Cmplx z2;
    Cmplx g;
    Cmplx g2;

    // check Gamma(z+1)=z*Gamma(z)
    z = new Cmplx(0.5, 0.5);
    z2 = new Cmplx(z).add(1);
    g = CmplxGamma.calc(z).mult(z);
    g2 = CmplxGamma.calc(z2).minus(g);
    log.assertZero("Gamma(z+1) - z*Gamma(z)=Re=", g2.getRe(), 4e-17);
    log.assertZero("Gamma(z+1) - z*Gamma(z)=Im=", g2.getIm(), 2e-16);

    // check Gamma(z+1)=z*Gamma(z)
    z = new Cmplx(1.5, 0.5);
    z2 = new Cmplx(z).add(1);
    g = CmplxGamma.calc(z).mult(z);
    g2 = CmplxGamma.calc(z2).minus(g);
    log.assertZero("Gamma(z+1) - z*Gamma(z)=Re=", g2.getRe(), 2e-15);
    log.assertZero("Gamma(z+1) - z*Gamma(z)=Im=", g2.getIm(), 2e-15);

    // check Gamma(z+1)=z*Gamma(z)
    z = new Cmplx(-1.5, 0.5);
    z2 = new Cmplx(z).add(1);
    g = CmplxGamma.calc(z).mult(z);
    g2 = CmplxGamma.calc(z2).minus(g);
    log.assertZero("Gamma(z+1) - z*Gamma(z)=Re=", g2.getRe(), 9e-16);
    log.assertZero("Gamma(z+1) - z*Gamma(z)=Im=", g2.getIm(), 9e-16);

    // check Gamma(3/4)=1.22541 67024 // p81 of Abr&Stig
    g = CmplxGamma.calc(new Cmplx(3./4.));
    double corr = 1.2254167024;
    log.assertZero("Gamma(3./4.)=Re=", g.getRe() - corr, 7e-11);
    log.assertZero("Gamma(3./4.)=Im=", g.getIm(), 4e-17);

    // check Gamma(1/2)=sqrt(PI) // p81 of Abr&Stig
    g = CmplxGamma.calc(new Cmplx(1./2.));
    corr = Math.sqrt(Math.PI);
    log.assertZero("Gamma(1./2.)=Re=", g.getRe() - corr, 5e-16);
    log.assertZero("Gamma(1./2.)=Im=", g.getIm(), 4e-18);

    // check Gamma(3/2)=sqrt(PI)/2 // p81 of Abr&Stig
    g = CmplxGamma.calc(new Cmplx(3./2.));
    corr = Math.sqrt(Math.PI) / 2.;
    log.assertZero("Gamma(3./2.)=Re=", g.getRe() - corr, 3e-16);
    log.assertZero("Gamma(3./2.)=Im=", g.getIm(), 4e-18);

    // check Gamma(3/2)=sqrt(PI)/2 // p81 of Abr&Stig
    g = CmplxGamma.calc(new Cmplx(2));
    corr = 1.;
    log.assertZero("Gamma(2)=Re=", g.getRe() - corr, 2e-16);
    log.assertZero("Gamma(2)=Im=", g.getIm(), 4e-18);

    // check Gamma(1.2, 4.5) // p103 of Abr&Stig
    g = CmplxGamma.calc(new Cmplx(1.2, 0.1));
    log.assertZero("Gamma(1.2, 0.1)=Re=", g.log().getRe() + 0.091697512413, 4e-13);
    log.assertZero("Gamma(1.2, 0.1)=Im=", g.log().getIm() + 0.028658497321, 4e-13);


    // p101 of Russian edition of Abramowitz & Stegun
// NOTE!!! valid results are LOG values
//   Data(complex(1.0, 0.2), complex(-0.032476292318, -0.112302222644))
//,  Data(complex(1.2, 2.2), complex(-1.979067237432, 0.541922948431))
    g = CmplxGamma.calc(new Cmplx(1.0, 0.2));
    log.assertZero("Gamma(1.0, 0.2)=Re=", g.log().getRe() + 0.032476292318, 2e-13);
    log.assertZero("Gamma(1.0, 0.2)=Im=", g.log().getIm() + 0.112302222644, 2e-13);
    g = CmplxGamma.calc(new Cmplx(1.2, 2.2));
    log.assertZero("Gamma(1.2, 2.2)=Re=", g.log().getRe() + 1.979067237432, 4e-13);
    log.assertZero("Gamma(1.2, 2.2)=Im=", g.log().getIm() - 0.541922948431, 4e-13);


// http://functions.wolfram.com/GammaBetaErf/Gamma/03/02/
//,  Data(complex(-3.0 / 2.0, 0.0), complex(log(4.0 / 3.0 * sqrt(PI)), 0.0))
    g = CmplxGamma.calc(new Cmplx(-3.0 / 2.0));
    log.assertZero("Gamma(1.2, 2.2)=Re=", g.log().getRe() - log(4.0 / 3.0 * sqrt(PI)), 3e-16);
    log.assertZero("Gamma(1.2, 2.2)=Im=", g.log().getIm() - 0, 4e-18);

    // check Gamma(1.2, 4.5) // p103 of Abr&Stig
    g = CmplxGamma.calc(new Cmplx(1.2, 4.5));
    log.assertZero("Gamma(1.2, 4.5)=Re=", g.log().getRe() + 5.095406054836, 8e-14);
    corr = 3.322742556043;
    corr -= (2*PI);
    log.assertZero("Gamma(1.2, 4.5)=Im=", g.log().getIm() - corr, 3e-13);
//    log.assertZero("Gamma(1.2, 4.5)=Im=", g.abs() - 3.322742556043, 4e-18);

  }
}

// original from CERN Program Library GENERAL, gen.f
/*
      FUNCTION WGAMMA(Z)
      IMPLICIT DOUBLE PRECISION (A-H,O-Z)
      COMPLEX*16
     +  WGAMMA
      COMPLEX*16
     +       Z,U,V,F,H,S
      CHARACTER NAME*(*)
      CHARACTER*80 ERRTXT
      PARAMETER (NAME = 'CGAMMA/WGAMMA')
      DIMENSION C(0:15)

      PARAMETER (Z1 = 1, HF = Z1/2)


      DATA PI /3.14159 26535 89793 24D0/
      DATA C1 /2.50662 82746 31000 50D0/

      DATA C( 0) / 41.62443 69164 39068D0/
      DATA C( 1) /-51.22424 10223 74774D0/
      DATA C( 2) / 11.33875 58134 88977D0/
      DATA C( 3) / -0.74773 26877 72388D0/
      DATA C( 4) /  0.00878 28774 93061D0/
      DATA C( 5) / -0.00000 18990 30264D0/
      DATA C( 6) /  0.00000 00019 46335D0/
      DATA C( 7) / -0.00000 00001 99345D0/
      DATA C( 8) /  0.00000 00000 08433D0/
      DATA C( 9) /  0.00000 00000 01486D0/
      DATA C(10) / -0.00000 00000 00806D0/
      DATA C(11) /  0.00000 00000 00293D0/
      DATA C(12) / -0.00000 00000 00102D0/
      DATA C(13) /  0.00000 00000 00037D0/
      DATA C(14) / -0.00000 00000 00014D0/
      DATA C(15) /  0.00000 00000 00006D0/

      DOUBLE PRECISION
     +      GREAL,GIMAG,XARG,YARG
      COMPLEX*16
     +      ZARG,GCONJG,GCMPLX
      GREAL( ZARG)=DREAL( ZARG)
      GIMAG( ZARG)=DIMAG( ZARG)
      GCONJG(ZARG)=DCONJG(ZARG)
      GCMPLX(XARG,YARG)=DCMPLX(XARG,YARG)

      U=Z
      X=U
      IF(GIMAG(U) .EQ. 0 .AND. -ABS(X) .EQ. INT(X)) THEN
       F=0
       H=0
       WRITE(ERRTXT,101) X
       CALL MTLPRT(NAME,'C305.1',ERRTXT)
      ELSE
       IF(X .GE. 1) THEN
        F=1
        V=U
       ELSEIF(X .GE. 0) THEN
        F=1/U
        V=1+U
       ELSE
        F=1
        V=1-U
       END IF
       H=1
       S=C(0)
       DO 1 K = 1,15
       H=((V-K)/(V+(K-1)))*H
    1  S=S+C(K)*H
       H=V+(4+HF)
       H=C1*EXP((V-HF)*LOG(H)-H)*S
       IF(X .LT. 0) H=PI/(SIN(PI*U)*H)
      ENDIF
      WGAMMA=F*H
      RETURN
  101 FORMAT('ARGUMENT EQUALS NON-POSITIVE INTEGER = ',1P,E15.1)
      END
*/