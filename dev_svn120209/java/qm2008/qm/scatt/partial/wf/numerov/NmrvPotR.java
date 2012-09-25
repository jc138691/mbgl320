package scatt.partial.wf.numerov;
import math.func.FuncVec;
import math.func.simple.FuncConst;
import math.vec.Vec;
import scatt.eng.EngOpt;
import scatt.partial.wf.eng_arr_not_used.EngFuncArr;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 14:16:39
 */
public class NmrvPotR extends EngFuncArr {
  public static String HELP = "Partial scattering wave (via Numerov algorithm)";
  public static Log log = Log.getLog(NmrvPotR.class);
  protected int L;
  private FuncVec pot;

  public NmrvPotR(FuncVec pot, EngOpt model, int L) {
    super(pot.getX(), model);
    this.pot = pot;
    this.L = L;
    if (L > 0) {
      throw new IllegalArgumentException(log.error("todo L>0"));
    }
    load();
  }

  protected void load() {
    if (size() == 0)
      return;
    Vec r = pot.getX();
    for (int i = 0; i < getEng().size(); i++) {
      double E = getEng().get(i);                         log.dbg("E = ", E);
      FuncVec V = new FuncVec(r, new FuncConst(2. * E));  log.dbg("2*E = ", V);
      V.addMultSafe(-2, pot);                                log.dbg("2*E - 2*V= ", V);
      FuncVec f = new NmrvAlgR(V).calc();
      set(i, f);
    }
  }
}

