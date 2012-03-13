package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 13/03/12, 1:53 PM
 */
// sin(kr) / sqrt(k)
public class SinKLcr extends SinPWaveLcr {
public SinKLcr(WFQuadrLcr w, final double p, final int L) {
  super(w, p, L);
  mult(1. / Math.sqrt(p));
}
}
