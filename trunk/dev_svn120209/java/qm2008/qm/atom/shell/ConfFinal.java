package atom.shell;

import javax.utilx.log.Log;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,09/11/2010,12:21:28 PM
 */
public class ConfFinal extends Conf {  // this class is for using config, NOT building it.
  public static Log log = Log.getLog(ConfFinal.class);
  private int[] arrQ;

  public ConfFinal(final Conf from) {  // nov2010 - ConfFinal looks odd?! cleanup if not needed.
    super(from);
    if (from instanceof ConfFinal) {
      throw new IllegalArgumentException(log.error("calling ConfFinal(final ConfFinal from)."));
    }
    loadQ();
  }

  private void loadQ() {
    arrQ = new int[size()];
    for (int i = 0; i < size(); i++) {
      arrQ[i] = getSh(i).getQ();
    }
  }

  public int[] getArrQ() {
    return arrQ;
  }

  public void add(Shell sh, Ls currLs) {
    throw new IllegalArgumentException(log.error("calling ConfFinal.add(Shell sh, Ls currLs)."));
  }
}
