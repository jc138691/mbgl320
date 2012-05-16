package atom.energy;
import atom.energy.part_wave.OvMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.shell.ConfArr;
import atom.wf.WFQuadr;
import math.func.FuncVec;
import math.func.arr.IFuncArr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 16/05/12, 1:17 PM
 */
public class ConfHOvMtrx extends HOvMtrx {
public static Log log = Log.getLog(ConfHOvMtrx.class);
public ConfHOvMtrx(ConfArr basis, final ISysH atom) {
  super(new ConfHMtrx(basis, atom));
  setOv(new ConfOvMtrx(basis, atom));
}
}