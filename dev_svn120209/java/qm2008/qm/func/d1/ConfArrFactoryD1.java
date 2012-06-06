package func.d1;
import atom.e_3.AtomShModelE3;
import atom.shell.ConfArr;
import math.func.arr.FuncArr;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.IWFuncArr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 11:43 AM
 */
public class ConfArrFactoryD1 extends FlowTest {
public static Log log = Log.getLog(ConfArrFactoryD1.class);
public ConfArrFactoryD1() {
  super(ConfArrFactoryD1.class);
}
public void testMakeConfArr() throws Exception {
}
public static ConfArrD1 makeBosonConf_TODO(ConfArrOptD1 opt, IWFuncArr basis) {
  ConfArrD1 res = new ConfArrD1(basis);
  return res;
}
public static ConfArrD1 makeFermiConf_TODO(ConfArrOptD1 opt, IWFuncArr basis) {
  ConfArrD1 res = new ConfArrD1(basis);
  return res;
}
public static ConfArrD1 makeBosonConfN2(IWFuncArr basis) {
  // just TWO particles
  ConfArrD1 res = new ConfArrD1(basis);
  int n = basis.size();
  for (int i = 0; i < n; i++) {
    res.add(new ConfD1N2(i, true));
    for (int i2 = i+1; i2 < n; i2++) {
      res.add(new ConfD1N2(i, i2));
    }
  }
  return res;
}
}
