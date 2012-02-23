package scatt.jm_2008.jm.laguerre;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/09/2008, Time: 15:53:19
 */
public class LgrrModel {
  protected double lambda;
  protected int N;
  protected int L;

  public LgrrModel() {

  }
  public LgrrModel(int L, int N, double lambda) {
    this.lambda = lambda;
    this.N = N;
    this.L = L;
  }
  public LgrrModel(LgrrModel from) {
    this.lambda = from.lambda;
    this.N = from.N;
    this.L = from.L;
  }

  public void loadDefault() {
    loadDefault(this);
  }
  public static void loadDefault(LgrrModel model) {
    model.setLambda(1f);
    model.setL(0);
    model.setN(10);
  }
  public double getLambda() {
    return lambda;
  }
  public void setLambda(double lambda) {
    this.lambda = lambda;
  }
  public int getN() {
    return N;
  }
  public void setN(int n) {
    this.N = n;
  }
  public int getL() {
    return L;
  }
  public void setL(int l) {
    L = l;
  }
  public String toString() {
    return "LgrrModel(L=" + L + ", n=" + N + ", lambda=" + lambda + ")";
  }

  public String makeLabel() {
    return "L" + L + "_LMBD" + lambda + "_N" + N;
  }
}
