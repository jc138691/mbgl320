package atom.angular;

import atom.shell.Ls;
;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,04/06/2010,9:27:51 AM
 */
import static java.lang.Math.*;
public class AngMomRules {
  public static boolean isValid(Ls s, Ls s2, Ls s3) {
    return isValid(s.getL(), s2.getL(), s3.getL())  && isValid(s.getS21(), s2.getS21(), s3.getS21());
  }
  public static boolean isValid(int a, int b, int c) {
    int ia = abs(a);
    int ib = abs(b);
    int ic = abs(c);
    if (abs(ia - ib) > ic)
      return false;
    if (abs(ia - ic) > ib)
      return false;
    if (abs(ib - ic) > ia)
      return false;
    return true;
  }
}
