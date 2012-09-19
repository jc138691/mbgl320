package atom.wf.dcr.func;
import atom.wf.dcr.FuncDcr;
import atom.wf.lcr.func.FuncLcr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 17/09/12, 10:14 AM
 */
public class FuncRToDcr extends FuncDcr {
// x = - 1/CR, CR=c+r; c > 0
// c+r0 = -1/x0; c = -1/x0 - r0
public FuncRToDcr(double x0, double r0) {
  super(-1./x0 - r0);
}

public double calc(double r) {
  return -1./(c + r);
}
}