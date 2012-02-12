package math.func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 09:50:42
 */
public class Factorial {
  private FactLn fLn;
  public Factorial(int last) {
    fLn = FactLn.makeInstance(last);
  }
  public double calc(int n) {
    return Math.exp(fLn.calc(n));
  }
}
