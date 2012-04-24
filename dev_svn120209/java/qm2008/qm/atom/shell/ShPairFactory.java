package atom.shell;
import math.func.FuncVec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 24/04/12, 3:50 PM
 */
public class ShPairFactory {

public static ShPair makePair(Shell sh, FuncVec wf, int id, int L, Ls LS) {
  Shell sh2 = new Shell(id, wf, L);
  ShPair res = new ShPair(sh, sh2, LS);
  return res;
}
public static ShPair makePair(FuncVec wf, int id, int L
  , FuncVec wf2, int id2, int L2, Ls LS) {
  Shell sh = new Shell(id, wf, L);
  ShPair res = makePair(sh, wf2, id2, L2, LS);
  return res;
}
}
