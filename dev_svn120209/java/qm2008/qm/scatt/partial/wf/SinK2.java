package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 13/03/12, 1:53 PM
 */
// sin(kr) * sqrt(2/k)
public class SinK2 extends SinPWaveLcr {
public SinK2(WFQuadrLcr w, final double p, final int L) {
  super(w, p, L);
  mult(Math.sqrt(2. / p));
}
}
