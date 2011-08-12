package scatt.partial.wf;
import math.func.FuncVec;
import math.func.Func;
import math.vec.Vec;

import javax.utilx.log.Log;

import scatt.eng.EngModel;
import scatt.Scatt;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 13:18:04
 */
public abstract class PartWaveR extends EngFuncArr {
  public static Log log = Log.getLog(PartWaveR.class);
  protected int L;

  public PartWaveR(Vec r, EngModel model, int L) {
    super(r, model);
    this.L = L;
    load();
  }
  protected void load() {
    if (size() == 0)
      return;
    Vec r = getX();
    for (int i = 0; i < getEng().size(); i++) {
      double E = getEng().get(i);               log.dbg("E = ", E);
      double p = Scatt.calcMomFromE(E);        log.dbg("p = ", p);
      FuncVec f = new FuncVec(r, makeFunc(p, L));
      set(i, f);
    }
  }
  protected Func makeFunc(double p, int L) { return null; }
}
