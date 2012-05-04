package math.func;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 4/05/12, 8:58 AM
 */
public class FuncMult extends FuncVec {
public FuncMult(final FuncVec f, final FuncVec f2) {
  super(f.copyY());
  mult(f2);
}
public FuncMult(final FuncVec f, final FuncVec f2, final FuncVec f3) {
  this(f, f2);
  mult(f3);
}
}
