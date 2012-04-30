package scatt.jm_2008.jm.target;

import flanagan.complex.Cmplx;
import math.Calc;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.theory.JmTools;

import javax.utilx.log.Log;

/**
* Created by Dmitry.A.Konovalov@gmail.com, 17/02/2010, 9:26:08 AM
C     from J_matrix.f  Calculates scattering via j-matrix method for given states
c     j-matrix references to the papers:
c     I  - J.T.Broad and W.P.Reinhardt, J.Phys.B9, 1491 (1976).
c     II - H.A.Yamani and L.Fishman, J.Math.Phys.16, 410 (1975).
*/
public class JmCh //extends LgrrModel
{   // target channel
private final LgrrModel jmModel;
public static Log log = Log.getLog(JmCh.class);
private Cmplx jnn; // from cjm
private Cmplx scattMom; // scattering momentum; from q
private double sqrtAbsMom;
private double absMom;
private double scattEng; // scattering energy; could be negative
private boolean open;
private double eng; // channel energy
private double sdcsW = 1.0; // singly differential ionization cross section conversion/integraation weight
private double sdcsContW = 1.0; // SDCS integration weight via overlap with continuum
private Cmplx cnn1;     // c_N / c_{N-1}
private Cmplx cn1;      // N-1
private Cmplx cn;       // N
private double sn;      // N
private Cmplx csn;      // N
private double sn1;     // N-1
private double qn1;     // s_{N-1} / c_{N-1}
private double snn1;     // s_N / s_{N-1}
private Cmplx csn1;     // N-1
public static final double EPS = 1e-10;

/*
see J_matrix
 */
public JmCh(double sysEng, double chEng, LgrrModel jmModel, int jmZ) { // channel energy
  this.jmModel = jmModel;
  init();
  eng = chEng;
  scattEng = sysEng - chEng; // available channel energy
  if (scattEng > 0) {
    open = true;
    loadOpen(jmZ);
    snn1 = sn / sn1;
    qn1 = (new Cmplx(sn1).div(cn1)).getRe();
  } else {
    open = false;
    loadClosed(jmZ);
  }
  sqrtAbsMom = Math.sqrt(scattMom.abs());
  absMom = scattMom.abs();

  if (cn1.abs() == 0) {
    cnn1 = new Cmplx(0);
  } else {
    cnn1 = cn.div(cn1);
  }
  removeZeros();
}
private void removeZeros() {
  if (Calc.isZero(sn))
    sn = 0;
  if (Calc.isZero(sn1))
    sn1 = 0;
  if (Calc.isZero(qn1))
    qn1 = 0;
  if (Calc.isZero(snn1))
    snn1 = 0;
  removeZeros(cn);
  removeZeros(cn1);
  removeZeros(cnn1);
  removeZeros(csn);
  removeZeros(csn1);
  removeZeros(jnn);
}
private void removeZeros(Cmplx c) {
if (Calc.isZero(c.getRe()))
  c.setRe(0);
if (Calc.isZero(c.getIm()))
  c.setIm(0);
}
private void init() {
  jnn = Cmplx.ZERO;
  scattMom = Cmplx.ZERO;
  cn  = Cmplx.ZERO;
  cn1 = Cmplx.ZERO;
}

private void loadOpen(int jmZ) {
  int N = jmModel.getN();
  int L = jmModel.getL();
  double lambda = jmModel.getLambda();
//c     Define k_\gamma - momentum of a \Gamma channel
  scattMom = new Cmplx(Math.sqrt(2. * scattEng), 0); //q(i1) = dcmplx(dsqrt(2d0 * tmp1), 0d0)
//               call jmtrx(tmp3, tmp4, njml(L1) - 1, njml(L1), 0d0, L1,
//     >            q(i1), 2d0 * alagl(L1), fl, nfl)
//               cjm(i1) = dcmplx(tmp3, 0d0)
  jnn = JmTools.jmtrx(N-1, N, L, scattMom, lambda, jmZ);
//    call sc_n (s_n(i1), tmp2, njml(L1) - 1, 0d0, L1,
//>            q(i1), 2d0 * alagl(L1), eps, fl, nfl)
//    c_n(i1) = dcmplx(tmp2, D0)
  csn1 = JmTools.sc_n(N-1, L, scattMom, lambda, jmZ, EPS);
  sn1 = csn1.getIm();
  cn1 = new Cmplx(csn1.getRe(), 0);
//    call sc_n (s_n1(i1), tmp2, njml(L1), D0, L1,
//>            q(i1), 2d0 * alagl(L1), eps, fl, nfl)
//    c_n1(i1) = dcmplx(tmp2, D0)
  csn = JmTools.sc_n(N, L, scattMom, lambda, jmZ, EPS);
  sn = csn.getIm();
  cn = new Cmplx(csn.getRe(), 0);
}
private void loadClosed(int jmZ) {
  int N = jmModel.getN();
  int L = jmModel.getL();
  double lambda = jmModel.getLambda();
//c     For a closed channel \Gamma, C is replaced by C+iS and evaluated
//c     at q=i|k_\gamma|, see (Eq.4.15) of II
  scattMom = new Cmplx(0, Math.sqrt(Math.abs(2. * scattEng)));   //q(i1) = dcmplx(0d0, dsqrt(dabs(2d0 * tmp1)) )
//    call jmtrx(tmp3, tmp4, njml(L1)-1, njml(L1), D0, L1,
//>            q(i1), 2d0 * alagl(L1), fl, nfl)
//    cjm(i1) = dcmplx(tmp3, tmp4)
  jnn = JmTools.jmtrx(N-1, N, L, scattMom, lambda, jmZ);
//    call sc_n (tmp1, tmp2, njml(L1)-1, D0, L1,
//>            q(i1), 2d0 * alagl(L1), eps, fl, nfl)
//    s_n(i1) = D0
//    c_n(i1) = dcmplx(tmp2, tmp1)
  sn1 = 0;
  cn1 = JmTools.sc_n(N-1, L, scattMom, lambda, jmZ, EPS);
//  cn1 = new Cmplx(cn1.getIm(), cn1.getRe()); //DEBUG
//  cn1 = new Cmplx(); // DEBUG
  csn1 = cn1;
//    call sc_n (tmp1, tmp2, njml(L1), D0, L1,
//>            q(i1), 2d0 * alagl(L1), eps, fl, nfl)
//    s_n1(i1) = D0
//    c_n1(i1) = dcmplx(tmp2, tmp1)
  sn = 0;
  cn = JmTools.sc_n(N, L, scattMom, lambda, jmZ, EPS);
//  cn = new Cmplx(cn.getIm(), cn.getRe()); //DEBUG
//  cn = new Cmplx(); // DEBUG
  csn = cn;

//  cnn1 = new Cmplx(1); //DEBUG
}

public String toString() {
  return "scattMom=" + scattMom
    + ", jnn=" + jnn
    + ", sn1=" + sn1
    + ", sn=" + sn
    + ", cn1=" + cn1
    + ", cn=" + cn
    ;
}

public Cmplx getJnn() {
  return jnn;
}

public Cmplx getCn() {
  return cn;
}
public Cmplx getCnn1() {
  return cnn1;
}
public Cmplx getCsn() {
  return csn;
}

public Cmplx getCn1() {
  return cn1;
}
public Cmplx getCsn1() {
  return csn1;
}

public double getSn() {
  return sn;
}
public double getSn1() {
  return sn1;
}
public Cmplx getScattMom() {
  return scattMom;
}
public double getQn1() {
  return qn1;
}
public double getSnn1() {
  return snn1;
}
public double getScattEng() {
  return scattEng;
}
public void setScattEng(double e) {
  this.scattEng = e;
}

public boolean isOpen() {
  return open;
}

public double getSqrtAbsMom() {
  return sqrtAbsMom;
}
public double getAbsMom() {
  return absMom;
}

public double getEng() {
  return eng;
}

public double getSysEng() {
  return eng + scattEng;
}
public void setEng(double eng) {
  this.eng = eng;
}

public double getSdcsW() {
  return sdcsW;
}

public void setSdcsW(double sdcsW) {
  this.sdcsW = sdcsW;
}

public double getSdcsContW() {
  return sdcsContW;
}

public void setSdcsContW(double sdcsContW) {
  this.sdcsContW = sdcsContW;
}
}
