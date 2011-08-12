package scatt.jm_2008.e3;
import scatt.jm_2008.jm.JmTestModel;
import scatt.jm_2008.jm.laguerre.lcr.JmLagrrBiLcr;
import scatt.jm_2008.jm.laguerre.lcr.JmLgrrOrthLcr;
import scatt.jm_2008.jm.theory.JmD;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/04/11,1:20 PM
 */
// THIS IS ONLY for JmLgrrOrthLcr!!!!!!
public class JmDe3 extends JmD {
  // chi_n = Sum_m D_{nm} * phi_m(r)
  // D_{nm} = < bi_m | chi_n >
  // D_{n, N-1} = < bi_{N-1} | chi_n >
  public JmDe3(JmLagrrBiLcr bi, JmLgrrOrthLcr basisArr, JmTestModel jmOpt) {
    super(bi, basisArr);
    if (!validate(basisArr, jmOpt)) {
      throw new IllegalArgumentException(log.error("!validate(basisArr)"));
    }
  }
  private boolean validate(JmLgrrOrthLcr basisArr, JmTestModel jmOpt) {
    if (size() <= 2)   // something is wrong!
      return false;
    double maxErr = jmOpt.getMaxIntgrlErr();
    double[] correct = basisArr.getFromNonOrth();
    // THIS IS ONLY for JmLgrrOrthLcr!!!!!!
    for (int i = 0; i < size(); i++) {
      double d_i = get(i);
      double c_i = correct[i];
      if (i < (size() - 1)) { // NOT LAST
        if (Math.abs(d_i) > maxErr) { // must be zero
          return false;
        }
        set(i, 0); // set to exact zero
      } else { // LAST
        if (Math.abs(d_i) < maxErr) { // [28Apr2011] LAST must NOT be zero
          return false;
        }
        if (Math.abs(d_i - c_i) > maxErr) {
          return false;
        }
        set(i, c_i); // set to exact
      }
    }
    return true;
  }
}