package atom.shell;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:12:56
 */
public class ShPair extends Conf {
  public Shell a;
  public Shell b;
  public ShPair(final Shell sh, final Shell sh2, Ls totLs) {
    super(sh, sh2, totLs);
    setShellPair();
  }
  public ShPair(Shell sh) {
    super(sh);
    setShellPair();
  }
  public void setShellPair() {
    a = getSh(0);
    b = getSh(0);
    if (b.getQ() == 1) {
      b = getSh(1);
    }
  }
}
