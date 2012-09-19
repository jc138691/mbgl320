package atom.wf.dcr;
import math.func.Func;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 17/09/12, 9:10 AM
 */
public abstract class FuncDcr implements Func {
  // x = - 1/CR, CR=c+r; c > 0
  // CR = -1/x; r = -[1/x + c];
  protected final double c;
  public FuncDcr(final double c) {
    this.c = c;
  }

  public FuncDcr(FuncDcr from) {
    this.c = from.c;
  }

  public final double getC() {
    return c;
  }
}