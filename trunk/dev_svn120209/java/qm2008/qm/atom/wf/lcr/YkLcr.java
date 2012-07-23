package atom.wf.lcr;
import math.func.intrg.IntgInftyPts7;
import math.func.intrg.IntgPts7;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.func.FuncVec;
import math.Mathx;
import math.interpol.PolynIntrp;

import javax.utilx.log.Log;
// YkLogR(r)=r I^oo_0 r<^k/r>^(k+1) s^2 ds Rn(s)Rn'(s)
// R(r)=sqrt(y)/r * P(r); where y(r)=c+r
// YkLogR(r) = Zk(r) + I^oo_r (r/s)^(k+1) yds Pn(s) Pn'(s)
// Zk(r) = I^r_0 (s/r)^k yds Pn(s) Pn'(s)
//
// dZk/dr = -k/r Zk + PPy
// dYk/dr = (k+1)/r YkLogR - (2k+1)/r Zk
// Zk(0)=0; YkLogR(r->oo)->Zk(r)
// dZ/dr=dZ/dx 1/y; since x=ln(y)
//
// dZ/dx = -ky/r atomZ + PPy^2
// dY/dx = (k+1)y/r Y - (2k+1)y/r atomZ
//
// integrating factor: r^k    for atomZ
// d(r^k atomZ)/dx = k r^(k-1) dr/dx atomZ + r^k dZ/dx;  since dr/dx=y
// d(r^k atomZ)/dx = FF y^2 r^k
//
// Zim = (ri/rim)^k Zi + I^im_i FF y^2 * (r/rim)^k dx;  where ri=r_i, rim=r_(i+m)
// the same as in Froese-Fischer but replacing
//  exp(-mhk) with (ri/rim)^k;   and
//  r*exp(k(x-xim)) with  y^2*(r/rim)^k
//
// integrating factor: r^-(k+1)    for Y
// d(r^-(k+1) Y)/dx = -(k+1) r^(k+1) y/r atomZ + r^-(k+1) dZ/dx;  since dr/dx=y
// d(r^-(k+1) Y)/dx = -(2k+1)y/r r^-(k+1) atomZ
//
// Yim = (rim/ri)^(k+1) Yi + I^im_i atomZ y/r (rim/r)^(k+1) dx;  where ri=r_i, rim=r_(i+m)
// the same as in Froese-Fischer but replacing
//  exp(-mh(k+1)) with (ri/rim)^(k+1);   and
//  exp(-(k+1)*(x-xi)) with  y/r*(r/rim)^(k+1)
//
public class YkLcr {
public static Log log = Log.getLog(YkLcr.class);
private final static double[] yTmp = new double[10]; // tmp array for zk_OLD
private final Vec wf;
private final Vec wf2;
private final double[] cr2;//(c+r)^2
private final double[] cr;//(c+r)
private final double[] r;
private final Vec vR;
private final double[] divR;
private final double[] yDivR;
private final int k;
private final int MX;
private final double H;
private final double H2;
private final double H3;
private final TransLcrToR xToR;
private final WFQuadrLcr quadr;

// NOTE! THIS IS optimization. Stop re-calculating the same values
private static FuncVec zF;  // z-function inside int_0^r
private static FuncVec rK;    // 1/r^k
private static FuncVec yF;  // y-function inside int_0^r
private static FuncVec rK1;    // r^(k+1)
private static FuncVec last_f;
private static FuncVec last_f2;
private static Vec last_r;
private static int last_K;
private boolean useLast = false;

public YkLcr(final WFQuadrLcr quadr, final Vec wf, final Vec wf2, final int K) {
  this.k = K;
  this.wf = wf;
  this.wf2 = wf2;
  this.quadr = quadr;
  xToR = quadr.getLcrToR();
  this.r = xToR.getArr();
  this.vR = xToR;
  this.cr2 = xToR.getCR2().getArr();
  this.cr = xToR.getCR().getArr();
  this.divR = xToR.getDivR().getArr();
  this.yDivR = xToR.getCRDivR().getArr();
  if (!(xToR.getX() instanceof StepGrid)) {
    String mssg = "YkLogR can only work with StepGrid";
    throw new IllegalArgumentException(log.error(mssg));
  }
  this.H = ((StepGrid) xToR.getX()).getGridStep();
  this.H2 = H / 2.0;
  this.H3 = H / 3.0;
  this.MX = (Math.min(wf.size(), wf2.size()) / 2) * 2;//      MX = (MIN0(MAX(I),MAX(J))/2)*2                                    AATK4123
  // MX is used to trace simpson's rule errors
  //EH = DEXP(-H)   // from SUBROUTINE INIT

  useLast = checkUseLast();
}
private boolean checkUseLast() {
  if (last_f == wf && last_f2 == wf2 &&  last_K == k &&  last_r  == vR)
    return true;
  return false;
}
public FuncVec calcZk() {
//  return calcZk_NEW();
  return calcZk_OLD();
}
public FuncVec calcYk() {
//  return calcYk_NEW();
  return calcYk_OLD();
}
public FuncVec calcYk_NEW() {
  FuncVec zk = calcZk_NEW();
//  FuncVec zk = calcZk_OLD();
  return calcYk_NEW(zk);
}
public FuncVec calcYk_OLD() {
  FuncVec zk = calcZk_OLD();
//  FuncVec zk = calcZk_NEW();
  return calcYk_OLD(zk);
}
public FuncVec calcYk_NEW(FuncVec zk) {   log.setDbg();
  loadYFuncs(zk);
  // integrate backwards
  FuncVec res = new IntgInftyPts7(yF);    log.info("IntgInftyPts7(zF)=", new VecDbgView(res));

  // boundary Y_k(r-->oo) = Z_k(r)
  double corr = zk.getLast() / rK1.getLast();
  res.add(corr - res.getLast());

  res.multSelf(rK1);
  return res;
}
public FuncVec calcZk_NEW() {   log.setDbg();
  loadZFuncs();

  // IntgInftyPts7 is not good for ln(r)-type grid!!!!!!!!
  // delta_r is much smaller for small r's than for large r's,
  // so it is mor accurate to integrate from start
//  FuncVec res = new IntgInftyPts7(zF);    log.info("IntgInftyPts7(zF)=", new VecDbgView(res));
//  res.add(-res.getFirst());

  //NOTE!!  HERE  IntgPts7 is much better than calcFuncInt_DEV
  FuncVec res = new IntgPts7(zF);    log.info("IntgPts7(zF)=", new VecDbgView(res));
//  FuncVec res = quadr.calcFuncInt_DEV(zF);    log.info("IntgPts7(zF)=", new VecDbgView(res));

  res.multSelf(rK);
  return res;
}
private void loadZFuncs() {
  zF = new FuncVec(xToR.getX());
  double[] z = zF.getArr();
  rK = new FuncVec(vR);
  double[] rk = rK.getArr();
  z[0] = 0;
  rk[0] = 0;
  for (int i = 1; i < z.length; i++) {
    z[i] = cr2[i] * wf.get(i) * wf2.get(i);
    double rki = Mathx.pow(r[i], k);
    z[i] *= rki;
    rk[i] = 1. / rki;
  }
  log.info("zF=", new VecDbgView(zF));
  log.info("rK=", new VecDbgView(rK));
}
private void loadYFuncs(FuncVec zkFunc) {
  yF = new FuncVec(xToR.getX());
  double[] y = yF.getArr();
  rK1 = new FuncVec(vR);
  double[] rk = rK1.getArr();
  double[] zk = zkFunc.getArr();
  y[0] = 0;
  rk[0] = 0;
  double kk = -(2. * k + 1.);
  int K1 = k + 1;
  for (int i = 1; i < y.length; i++) {
    y[i] = kk * (cr[i] / r[i])* zk[i];
    double rki = Mathx.pow(r[i], K1);
    y[i] /= rki;
    rk[i] = rki;
  }
  log.info("yF=", new VecDbgView(yF));
  log.info("rK1=", new VecDbgView(rK1));
}
public FuncVec calcZk_OLD() {
  FuncVec res = new FuncVec(vR);
  double[] YK = res.getArr();
  //      DEN = L(I) + L(J) + 3+ K                                          AATK4107
  //      FACT = (D1/(L(I)+1) + D1/(L(J)+1))/(DEN + D1)                     AATK4108
  //      A = EH**K                                                         AATK4109
  //      A2 = A*A                                                          AATK4110
  double H90 = H / 90.0;//      H90 = H/90.D0                                                     AATK4111
  //      A3 = A2*A*H90                                                     AATK4112
  //      AI = H90/A                                                        AATK4113
  //      AN = 114.D0*A*H90                                                 AATK4114
  double A34 = 34.0 * H90;//      A34 = 34.D0*H90                                                   AATK4115
  int M = 0;
  double f1 = cr2[M] * wf.get(M) * wf2.get(M);
  M++;//      F1 = RR(1)*P(1,I)*P(1,J)                                          AATK4116
  double f2 = cr2[M] * wf.get(M) * wf2.get(M);
  M++;//      F2 = RR(2)*P(2,I)*P(2,J)                                          AATK4117
  double f3 = cr2[M] * wf.get(M) * wf2.get(M);
  M++;//      F3 = RR(3)*P(3,I)*P(3,J)                                          AATK4118
  double f4 = cr2[M] * wf.get(M) * wf2.get(M);
  M++;//      F4 = RR(4)*P(4,I)*P(4,J)                                          AATK4119
  //YK(1) = F1*(D1 + atomZ*R(1)*FACT)/DEN                                 AATK4120
  //YK(2) = F2*(D1 + atomZ*R(2)*FACT)/DEN                                 AATK4121
  //YK(3) = YK(1)*A2 + H3*(F3 + D4*A*F2 + A2*F1)                      AATK4122
  YK[0] = 0;
  YK[1] = approxFirstZ(1, f2, f3);
  YK[2] = approxFirstZ(2, f2, f3); // simpson does not work as good as this
//  YK[1] = 0;
//  YK[2] = 0;

  for (M = 5; M <= MX; M++) {//      DO 8 M = 5,MX                                                     AATK4124
    double f5 = cr2[M - 1] * wf.get(M - 1) * wf2.get(M - 1);//      F5 = (RR(M)*P(M,I))*P(M,J)                                        AATK4125

    // Zim = (ri/rim)^k Zi + I^im_i FF y^2 * (r/rim)^k dx;  where ri=r_i, rim=r_(i+m)
    // the same as in Froese-Fischer but replacing
    //  exp(-mhk) with (ri/rim)^k;   and
    //  r*exp(k(x-xim)) with  y^2*(r/rim)^k

    // Replace exp(-mhk) with (ri/rim)^k;  where m=2
    //      EH = DEXP(-H)   // from SUBROUTINE INIT
    //      A = EH**K                                                         AATK4109
    double ovR = divR[M - 2];
    double a3 = Mathx.pow(r[M - 5] * ovR, k) * H90;//      A3 = A2*A*H90                                                     AATK4112
    double a2 = Mathx.pow(r[M - 4] * ovR, k);//      A2 = A*A                                                          AATK4110
    double a1 = Mathx.pow(r[M - 3] * ovR, k) * 114.0 * H90;//      AN = 114.D0*A*H90                                                 AATK4114
    double ai = Mathx.pow(r[M - 1] * ovR, k) * H90;//      AI = H90/A                                                        AATK4113

    // From Froese Fischer etal Computational atomic structure 2000
    // DELTA^2 F = F2 - 2*F3 + F4
    // DELTA^4 F = F1 - 4*F2 + 6*F3 - 4*F4 + F5
    // INTL F(x)dx = h(F3 + DELTA^2 F/3 - DELTA^4 F/90) + O(h^7) =
    //  = h/90 [114*F3 + 34*(F2+F4) - F1 - F5]
    //
    //      YK(M-1) = YK(M-3)*A2 + ( AN*F3 + A34*(F4+A2*F2)-F5*AI-F1*A3)      AATK4126
    double YKM_3 = YK[M - 4];
    double YKM_1 = YKM_3 * a2 + (a1 * f3 + A34 * (f4 + a2 * f2) - f5 * ai - f1 * a3);
    YK[M - 2] = YKM_1;
    f1 = f2;//      F1 = F2                                                           AATK4127
    f2 = f3;//      F2 = F3                                                           AATK4128
    f3 = f4;//      F3 = F4                                                           AATK4129
    f4 = f5;//8     F4 = F5                                                           AATK4130
  }
  int M1 = MX - 1;//      M1 = MX - 1                                                       AATK4131
//  if (K == 0) {//      IF (IABS(I-J)  +  IABS(K) .NE. 0) GO TO 2                         AATK4132
//    //*  *****  FOR Y0(I,I) SET THE LIMIT TO 1 AND REMOVE OSCILLATIONS        AATK4134
//    //*  *****  INTRODUCED BY THE USE OF SIMPSON'S RULE                       AATK4135
//    int M2 = M1 - 1;//      M2 = M1 - 1                                                       AATK4137
//    double mid = 0.5 * (YK[M1 - 1] + YK[M2 - 1]);
//    double C1 = mid - YK[M1 - 1];//      C1 = D1 - YK(M1)                                                  AATK4138
//    double C2 = mid - YK[M2 - 1];//      C2 = D1 - YK(M2)                                                  AATK4139
//    for (M = 1; M <= M1; M += 2) {//      DO 3 M = 1,M1,2                                                   AATK4140
//      YK[M - 1] += C1;//      YK(M) = YK(M) + C1                                                AATK4141
//      YK[M] += C2;//3     YK(M+1) = YK(M+1) + C2                                            AATK4142
//    }
//  }
  int NO = r.length;
  for (M = M1 + 1; M <= NO; M++) {//2     DO 1 M = M1+1,NO                                                  AATK4143
    double A = Mathx.pow(r[M - 2] * divR[M - 1], k);//A = EH**K                                                         AATK4109
    YK[M - 1] = A * YK[M - 2];//         YK(M) = A*YK(M-1)                                              AATK4144
  }//1     CONTINUE                                                          AATK4145
  return res;
}
private double approxFirstZ(int idxDest, double F2, double F3) {
  FuncVec tmp = new FuncVec(vR, yTmp);
  int M = 0;
  tmp.set(M, 0);
  M++;
  double fx[] = {0, 0, 0};
  double rDest = r[idxDest];
  double A = Mathx.pow(r[M] / rDest, k);
  fx[M] = F2 * A / cr[M];
  tmp.set(M, fx[M]);
  M++;
  A = Mathx.pow(r[M] / rDest, k);
  fx[M] = F3 * A / cr[M];
  tmp.set(M, fx[M]);
  double b = PolynIntrp.calcPowerSLOW(tmp, 0);
  return rDest / (b + 1) * fx[idxDest];
}
public FuncVec calcYk_OLD(FuncVec res) {
  double[] YK = res.getArr();
//  FuncVec res = calcZk(); //CALL ZK(I,J,K)                                                    AATK4062
  //A =    EH**(K+1)                                                  AATK4063
  int K1 = k + 1;
  double C = 2 * k + 1;//      C = 2*K+1                                                         AATK4064
  //      A2 = A*A                                                          AATK4065
  double H90 = C * H3 / 30.;//      H90 = C*H3/D30                                                    AATK4066
  //      A3 = A2*A*H90                                                     AATK4067
  //      AI = H90/A                                                        AATK4068
  //      AN = 114.D0*A*H90                                                 AATK4069
  double A34 = 34. * H90;//      A34 = 34.D0*H90                                                   AATK4070
  //      MX = (MIN0(MAX(I),MAX(J))/2)*2                                    AATK4071
  int M = MX - 1;
  double F1 = yDivR[M] * res.get(M);
  M--;//      F1 = YK(MX)*EH**K                                                 AATK4072
  double F2 = yDivR[M] * res.get(M);
  M--;//      F2 = YK(MX)                                                       AATK4073
  double F3 = yDivR[M] * res.get(M);
  M--;//      F3 = YK(MX-1)                                                     AATK4074
  double F4 = yDivR[M] * res.get(M);
  M--;//      F4 = YK(MX-2)                                                     AATK4075
  // NOTE!!! MX-1, MX-2, and MX-3; the last three must be set to atomZ, otherwise the algorithm does not work?!
  for (M = MX - 3; M >= 2; M--) {//      DO 9 M = MX-2,2,-1                                                AATK4076
    double F5 = yDivR[M - 2] * res.get(M - 2);//      F5 = YK(M-1)                                                      AATK4077
    double rM1 = r[M - 1]; // NOTE K1 not K below
    double A3 = Mathx.pow(rM1 * divR[M + 2], K1) * H90;//      A3 = A2*A*H90                                                     AATK4112
    double A2 = Mathx.pow(rM1 * divR[M + 1], K1);//      A2 = A*A                                                          AATK4110
    double AN = Mathx.pow(rM1 * divR[M + 0], K1) * 114.0 * H90;//      AN = 114.D0*A*H90                                                 AATK4114
    double AI = Mathx.pow(rM1 * divR[M - 2], K1) * H90;//      AI = H90/A                                                        AATK4113

    //      YK(M) = YK(M+2)*A2 + ( AN*F3 + A34*(F4+A2*F2)-F5*AI-F1*A3)        AATK4078
    YK[M - 1] = YK[M + 1] * A2 + (AN * F3 + A34 * (F4 + A2 * F2) - F5 * AI - F1 * A3);
    F1 = F2;//      F1 = F2                                                           AATK4079
    F2 = F3;//      F2 = F3                                                           AATK4080
    F3 = F4;//      F3 = F4                                                           AATK4081
    F4 = F5;//9     F4 = F5                                                           AATK4082
  }
  //      YK(1) = YK(3)*A2+C*H3*(F4 + D4*A*F3 + A2*F2)                      AATK4083
//  YK[0] = 0; // always zero
  return res;
}
}
/*
*AATKHF86.  GENERAL HARTREE-FOCK PROGRAM.  C.F. FISCHER.                 AATK0000
*REF. IN COMP. PHYS. COMMUN. 43 (1987) 355                               AATK0000
*     ------------------------------------------------------------------AATK0001
*                                                                       AATK0002
*     A GENERAL HARTREE-FOCK PROGRAM                                    AATK0003
*                                                                       AATK0004
*     by C. Froese Fischer                                              AATK0005
*        Vanderbilt University                                          AATK0006
*        Nashville, TN 37235 USA                                        AATK0007
*                                                                       AATK0008
*     April, 1986                                                       AATK0009
*                                                                       AATK0010
*     ------------------------------------------------------------------AATK0011
*                                                                       AATK0012
*                                                                       AATK0013
*     All comments in the program listing assume the radial function P  AATK0014
*     is the solution of an equation of the form                        AATK0015
*                                                                       AATK0016
*      P" + ( 2Z/R - Y - L(L+1)/R**2 - E)P = X + T                      AATK0017
*                                                                       AATK0018
*     where Y is called a potential function                            AATK0019
*           X is called an exchange function, and                       AATK0020
*           T includes contributions from off-diagonal energy parameter AATK0021
*                                                                       AATK0022
*     The program uses LOG(atomZ*R) as independent variable and             AATK0023
*                      P/SQRT(R) as dependent variable.                 AATK0024
*     As a result all equations must be transformed as described in     AATK0025
*     Sec. 6-2 and 6-4 of the book - ``The Hartree-Fock Method for      AATK0026
*     Atoms'',Wiley Interscience, 1977, by Charlotte FROESE FISCHER.    AATK0027
*     (All references to equations and sections refer to this book.)    AATK0028
*                                                                       AATK0029
*     This program is written as a double precision FORTRAN77           AATK0030
*     program.  However, on computers with a word length of 48 bits or  AATK0031
*     more it should be run as a single precision program.  Such con-   AATK0032
*     version is facilitated through the use of IMPLICIT type declar-   AATK0033
*     ations and the initialization of many double precision            AATK0034
*     constants in the INIT program.                                    AATK0035
*                                                                       AATK0036
*                                                                       AATK0037
*                                                                       AATK0051
*     ------------------------------------------------------------------AATK4046
*                 Y K F                                                 AATK4047
*     ------------------------------------------------------------------AATK4048
*                                                                       AATK4049
*               k                                                       AATK4050
*       Stores Y (i, j; r) in the array YK                              AATK4051
*                                                                       AATK4052
*                                                                       AATK4053
SUBROUTINE YKF(I,J,K,REL)                                         AATK4054
IMPLICIT DOUBLE PRECISION(A-H,O-atomZ)                                AATK4055
PARAMETER (NOD=220,NWFD=20)                                       AATK4056
COMMON /PARAM/H,H1,H3,CH,EH,RHO,atomZ,TOL,NO,ND,NWF,NP,NCFG,IB,IC,ID  AATK4057
:   ,D0,D1,D2,D3,D4,D5,D6,D8,D10,D12,D16,D30,FINE,NSCF,NCLOSD      AATK4058
COMMON /RADIAL/R(NOD),RR(NOD),R2(NOD),P(NOD,NWFD),YK(NOD),        AATK4059
:   YR(NOD),X(NOD),AZ(NWFD),L(NWFD),MAX(NWFD),N(NWFD)              AATK4060
LOGICAL REL                                                       AATK4061
CALL ZK(I,J,K)                                                    AATK4062
A =    EH**(K+1)                                                  AATK4063
C = 2*K+1                                                         AATK4064
A2 = A*A                                                          AATK4065
H90 = C*H3/D30                                                    AATK4066
A3 = A2*A*H90                                                     AATK4067
AI = H90/A                                                        AATK4068
AN = 114.D0*A*H90                                                 AATK4069
A34 = 34.D0*H90                                                   AATK4070
MX = (MIN0(MAX(I),MAX(J))/2)*2                                    AATK4071
F1 = YK(MX)*EH**K                                                 AATK4072
F2 = YK(MX)                                                       AATK4073
F3 = YK(MX-1)                                                     AATK4074
F4 = YK(MX-2)                                                     AATK4075
DO 9 M = MX-2,2,-1                                                AATK4076
F5 = YK(M-1)                                                      AATK4077
YK(M) = YK(M+2)*A2 + ( AN*F3 + A34*(F4+A2*F2)-F5*AI-F1*A3)        AATK4078
F1 = F2                                                           AATK4079
F2 = F3                                                           AATK4080
F3 = F4                                                           AATK4081
9     F4 = F5                                                           AATK4082
YK(1) = YK(3)*A2+C*H3*(F4 + D4*A*F3 + A2*F2)                      AATK4083
IF (.NOT.REL) RETURN                                              AATK4084
C = C*FINE                                                        AATK4085
DO 10 M = 1,MX                                                    AATK4086
YK(M) = YK(M) + C*P(M,I)*P(M,J)                                   AATK4087
10    CONTINUE                                                          AATK4088
RETURN                                                            AATK4089
END                                                               AATK4090
*                                                                       AATK4091
*AATKHF86.  GENERAL HARTREE-FOCK PROGRAM.  C.F. FISCHER.                 AATK0000
*REF. IN COMP. PHYS. COMMUN. 43 (1987) 355                               AATK0000
*     ------------------------------------------------------------------AATK0001
*                                                                       AATK0002
*     A GENERAL HARTREE-FOCK PROGRAM                                    AATK0003
*                                                                       AATK0004
*     by C. Froese Fischer                                              AATK0005
*        Vanderbilt University                                          AATK0006
*        Nashville, TN 37235 USA                                        AATK0007
*                                                                       AATK0008
*     April, 1986                                                       AATK0009
*                                                                       AATK0010
*     ------------------------------------------------------------------AATK0011
*                                                                       AATK0012
*                                                                       AATK0013
*     All comments in the program listing assume the radial function P  AATK0014
*     is the solution of an equation of the form                        AATK0015
*                                                                       AATK0016
*      P" + ( 2Z/R - Y - L(L+1)/R**2 - E)P = X + T                      AATK0017
*                                                                       AATK0018
*     where Y is called a potential function                            AATK0019
*           X is called an exchange function, and                       AATK0020
*           T includes contributions from off-diagonal energy parameter AATK0021
*                                                                       AATK0022
*     The program uses LOG(atomZ*R) as independent variable and             AATK0023
*                      P/SQRT(R) as dependent variable.                 AATK0024
*     As a result all equations must be transformed as described in     AATK0025
*     Sec. 6-2 and 6-4 of the book - ``The Hartree-Fock Method for      AATK0026
*     Atoms'',Wiley Interscience, 1977, by Charlotte FROESE FISCHER.    AATK0027
*     (All references to equations and sections refer to this book.)    AATK0028
*                                                                       AATK0029
*     This program is written as a double precision FORTRAN77           AATK0030
*     program.  However, on computers with a word length of 48 bits or  AATK0031
*     more it should be run as a single precision program.  Such con-   AATK0032
*     version is facilitated through the use of IMPLICIT type declar-   AATK0033
*     ations and the initialization of many double precision            AATK0034
*     constants in the INIT program.                                    AATK0035
*                                                                       AATK0036
*                                                                       AATK0037
*     ------------------------------------------------------------------AATK4092
*                 atomZ K                                                   AATK4093
*     ------------------------------------------------------------------AATK4094
*                                                                       AATK4095
*               k                                                       AATK4096
*       Stores atomZ (i, j; r) in the array YK.                             AATK4097
*                                                                       AATK4098
*                                                                       AATK4099
SUBROUTINE ZK(I,J,K)                                              AATK4100
PARAMETER (NOD=220,NWFD=20)                                       AATK4101
IMPLICIT DOUBLE PRECISION(A-H,O-atomZ)                                AATK4102
COMMON /PARAM/H,H1,H3,CH,EH,RHO,atomZ,TOL,NO,ND,NWF,NP,NCFG,IB,IC,ID  AATK4103
:   ,D0,D1,D2,D3,D4,D5,D6,D8,D10,D12,D16,D30,FINE,NSCF,NCLOSD      AATK4104
COMMON /RADIAL/R(NOD),RR(NOD),R2(NOD),P(NOD,NWFD),YK(NOD),        AATK4105
:   YR(NOD),X(NOD),AZ(NWFD),L(NWFD),MAX(NWFD),N(NWFD)              AATK4106
DEN = L(I) + L(J) + 3+ K                                          AATK4107
FACT = (D1/(L(I)+1) + D1/(L(J)+1))/(DEN + D1)                     AATK4108
A = EH**K                                                         AATK4109
A2 = A*A                                                          AATK4110
H90 = H/90.D0                                                     AATK4111
A3 = A2*A*H90                                                     AATK4112
AI = H90/A                                                        AATK4113
AN = 114.D0*A*H90                                                 AATK4114
A34 = 34.D0*H90                                                   AATK4115
F1 = RR(1)*P(1,I)*P(1,J)                                          AATK4116
F2 = RR(2)*P(2,I)*P(2,J)                                          AATK4117
F3 = RR(3)*P(3,I)*P(3,J)                                          AATK4118
F4 = RR(4)*P(4,I)*P(4,J)                                          AATK4119
YK(1) = F1*(D1 + atomZ*R(1)*FACT)/DEN                                 AATK4120
YK(2) = F2*(D1 + atomZ*R(2)*FACT)/DEN                                 AATK4121
YK(3) = YK(1)*A2 + H3*(F3 + D4*A*F2 + A2*F1)                      AATK4122
MX = (MIN0(MAX(I),MAX(J))/2)*2                                    AATK4123
DO 8 M = 5,MX                                                     AATK4124
F5 = (RR(M)*P(M,I))*P(M,J)                                        AATK4125
YK(M-1) = YK(M-3)*A2 + ( AN*F3 + A34*(F4+A2*F2)-F5*AI-F1*A3)      AATK4126
F1 = F2                                                           AATK4127
F2 = F3                                                           AATK4128
F3 = F4                                                           AATK4129
8     F4 = F5                                                           AATK4130
M1 = MX - 1                                                       AATK4131
IF (IABS(I-J)  +  IABS(K) .NE. 0) GO TO 2                         AATK4132
*                                                                       AATK4133
*  *****  FOR Y0(I,I) SET THE LIMIT TO 1 AND REMOVE OSCILLATIONS        AATK4134
*  *****  INTRODUCED BY THE USE OF SIMPSON'S RULE                       AATK4135
*                                                                       AATK4136
M2 = M1 - 1                                                       AATK4137
C1 = D1 - YK(M1)                                                  AATK4138
C2 = D1 - YK(M2)                                                  AATK4139
DO 3 M = 1,M1,2                                                   AATK4140
YK(M) = YK(M) + C1                                                AATK4141
3     YK(M+1) = YK(M+1) + C2                                            AATK4142
2     DO 1 M = M1+1,NO                                                  AATK4143
YK(M) = A*YK(M-1)                                              AATK4144
1     CONTINUE                                                          AATK4145
END                                                               AATK4146
*/