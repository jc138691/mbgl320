package scatt.partial.wf;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;

import scatt.eng.EngGrid;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.theory.JmTheory;
import scatt.eng.EngModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 20/11/2008, Time: 14:07:40
 */
public class JnnEng extends FuncVec {
  public static String HELP = "J_{N,N-1}";
  public static Log log = Log.getLog(JnnEng.class);
  public JnnEng(LgrrModel jmModel, EngModel eng) {  // TODO L
    super(new EngGrid(eng));
    load(jmModel);
  }
  protected void load(LgrrModel model) {
    double lambda = model.getLambda();
    int N = model.getN();                  log.dbg("N = " + N);// remember JM's jmBasisN is n=0,...,N-1
    Vec eng = getX();
    for (int i = 0; i < eng.size(); i++) {
      double E = eng.get(i);                    log.dbg("E = ", E);
      double Jnn = JmTheory.calcJnnL0byE(N, E, lambda); log.dbg("Jnn(E) = ", Jnn);
      set(i, Jnn);
    }
  }
}
