package atom.wf.mm;
import atom.energy.Energy;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.intrg.IntgPts7;
import math.vec.Vec;
import math.vec.VecDbgView;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 10/05/12, 2:39 PM
 */
public class HkMm {
public static Log log = Log.getLog(HkMm.class);
// INPUT
private final WFQuadrLcr quadr;
private final FuncVec a;
private final FuncVec b;
private final FuncVec a2;
private final FuncVec b2;
private final int K;
// derived from INPUT
private final Vec vcr2;
private final Vec vcr2r;
private final Vec vx;
//
private FuncVec zFk;  // z-function inside int_0^r
private FuncVec zaa;   // result of int_0^r
private FuncVec zaa2;  // result of int_0^r
private FuncVec zaar;  // result of int_0^r
private FuncVec rK;    // 1/r^k
private FuncVec yF;  // y-function inside int_0^r
private FuncVec rK1;    // r^(k+1)
// RESULTS
private boolean hasOv = false;
private double ov;
private boolean hasKin = false;
private double kin;
private boolean hasPot1 = false;
private double pot1;
private boolean hasPot2 = false;
private double pot2;

public HkMm(final WFQuadrLcr quadr
            , FuncVec a, FuncVec b, FuncVec a2, FuncVec b2, int K) {
  this.quadr = quadr;
  this.K = K;
  this.a = a;
  this.b = b;
  this.a2 = a2;
  this.b2 = b2;

  vcr2 = quadr.getLcrToR().getCR2();
  vcr2r = quadr.getLcrToR().getCR2DivR();
  vx = quadr.getX();
}
public double calcOv() {   // overlap
  if (hasOv)
    return ov;
  calcZAA();  // \int_0^{r'} dr a(r) a2(r)
  double res = 2. * quadr.calcInt(b, b2, zaa);  // NOTE!!!! *2.
  hasOv = true;
  ov = res;
  return res;
}
public Energy calcH(double atomZ) {
  Energy res = new Energy();
  res.kin = calcKin();
  res.p1 = calcPot1(atomZ);
  res.p2 = calcPot2();
  res.pt = res.p1 + res.p2;
  return res;
}

public double calcTotE(double atomZ) {
  double ke = calcKin();
  double p1 = calcPot1(atomZ);
  double p2 = calcPot2();
  return ke + p1 + p2;
}
public double calcKin() {
  if (hasKin)
    return kin;
/*
\int_0^\infty dr_1 \int_0^\infty dr_2 \Phi_{ab}(r_1, r_2)
[\frac{1}{2}\frac{\partial^2}{\partial r_1^2} + \frac{1}{2} \frac{\partial^2}{\partial r_2^2}]\Phi_{a'b'}(r_1, r_2)
=\\
=\int_0^\infty dr_1  \int_0^{r_1} dr_2 P_a(r_2) P_b(r_1)
[\frac{\partial^2}{\partial r_1^2} + \frac{\partial^2}{\partial r_2^2} ]
P_{a'}(r_2) P_{b'}(r_1)=\\
=\int_0^\infty dr_1  P_b(r_1) P_{b'}(r_1) \int_0^{r_1} dr_2 P_a(r_2) P''_{a'}(r_2)
+\int_0^\infty dr_1  P_b(r_1) P''_{b'}(r_1) \int_0^{r_1} dr_2 P_a(r_2) P_{a'}(r_2)
 */
  calcZAA();  // \int_0^{r'} dr a(r) a2(r)

  // calc zaa2 without cr2
  calcZAA2();  // \int_0^{r'} dr a(r) a2''(r)

  // (-1/2)d^2/dr^2 P(r) = (-1/2) d^2/dx^2 P(r(x)) + (1/2)1/4 P(r(x))
  double aa2 = quadr.calcInt(b, b2, zaa2);

  double bb2 = quadr.calc(b, b2.getDrv2(), zaa);// NOT  calcInt!!!
  double bb = quadr.calc(b, b2, zaa);// NOT  calcInt!!!
  bb2 -= (0.25 * bb);

  double abab2 = 0;
  if (a2 != b2) {
    Vec ab2 = a2.copy();
    ab2.multSelf(b2.getDrv());
    Vec ba2 = b2.copy();
    ba2.multSelf(a2.getDrv());

    ab2.addMultSafe(-1, ba2);
    abab2 = quadr.calcInt(a, b, ab2);
  }
  double res = -aa2 - bb2 - abab2;
//  double res = -aa2 - bb2; // DEBUG
  hasKin = true;
  kin = res;
  return res;
}
public double calcPot1(double atomZ) { // atomZ=+1 for Hydrogen
  if (hasPot1)
    return atomZ * pot1;
  calcZAA();  // \int_0^{r'} dr a(r) a2(r)
  calcZAAR();  // \int_0^{r'} dr a(r) a2(r) 1/r
  double ar = -2. * quadr.calcInt(b, b2, zaar);    // NOTE!!!! *2.
  double br = -2. * quadr.getWithCR2DivR().calc(b, b2, zaa);    // NOTE!!!! *2.
  double res = ar + br;
  hasPot1 = true;
  pot1 = res;
  return atomZ * res;
}
public double calcPot2() {
  if (hasPot2)
    return pot2;
  calcZAA();  // \int_0^{r'} dr a(r) a2(r)
  double res = 2. * quadr.getWithCR2DivR().calc(b, b2, zaa);    // NOTE!!!! *2.
  hasPot2 = true;
  pot2 = res;
  return res;
}

private void calcZAAR() {
  if (zaar != null)
    return;
  zaar = calcZ1(a, a2);  log.info("zaar=", new VecDbgView(zaar));
}
private void calcZAA() {
  if (zaa != null)
    return;
  zaa = calcZ0(a, a2);  log.info("zaa=", new VecDbgView(zaa));
}
private void calcZAA2() {  // with second deriv of a2
  if (zaa2 != null)
    return;
  // converting to d^2/dx^2
  // \int dr a(r) d^2/dr^2 b(r) = \int dx Fa(x)[ d^2/dx^2 -1/4 ]Fb(x)
  // NOTE: \int dx DOE NOT have CR2!!!!!!
  zaa2 = calcZDrv2(a, a2);  log.info("zaa2=", new VecDbgView(zaa2));
}
private FuncVec calcZDrv2(Vec fa, FuncVec fb) {   log.setDbg();
  Vec v = fb.getDrv2().copy();      //[ d^2/dx^2 Fb(x)]
  v.addMultSafe(-0.25, fb);  // [ d^2/dx^2 -1/4 ]Fb(x)]
  v.multSelf(fa);            // Fa(x) [ d^2/dx^2 -1/4 ]Fb(x)]
  FuncVec z = new FuncVec(vx, v);
  FuncVec res = new IntgPts7(z);
  return res;
}
private FuncVec calcZ0(Vec vf, Vec vf2) {   log.setDbg();
  Vec v = vf.copy();
  v.multSelf(vcr2, vf2);
  FuncVec z = new FuncVec(vx, v);
  FuncVec res = new IntgPts7(z);
  return res;
}
private FuncVec calcZ1(Vec vf, Vec vf2) {
  Vec v = vf.copy();
  v.multSelf(vcr2r, vf2);
  FuncVec z = new FuncVec(vx, v);
  FuncVec res = new IntgPts7(z);
  return res;
}
//private void loadZFuncs(Vec vf, Vec vf2) {
//  double[] f = vf.getArr();
//  double[] f2 = vf2.getArr();
//  double[] CR2 = quadr.getLcrToR().getCR2();
//  Vec vr = quadr.getR();
//  zF0 = new FuncVec(vr);
//  double[] z = zF.getArr();
//  rK = new FuncVec(rVec);
//  double[] rk = rK.getArr();
//  z[0] = 0;
//  rk[0] = 0;
//  for (int i = 1; i < z.length; i++) {
//    z[i] = CR2[i] * f[i] * f2[i];
//    double rki = Mathx.pow(r[i], K);
//    z[i] *= rki;
//    rk[i] = 1. / rki;
//  }
//  log.info("zF=", new VecDbgView(zF));
//  log.info("rK=", new VecDbgView(rK));
//}
}
