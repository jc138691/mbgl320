package scatt.jm_2008.jm.laguerre;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/04/2010, 9:14:40 AM
 */
public class JmLgrrLabelMaker extends LgrrOpt {
  private int targetN;

  public JmLgrrLabelMaker(LgrrOpt from, int nt) {
    super(from);
    this.targetN = nt;
  }

  public String toString() {
    return "JmLgrrLabelMaker(L=" + L + ", N=" + N
      + ", Nt=" + targetN + ", lambda=" + lambda + ")";
  }

  public String makeLabel() {
    return super.makeLabel() + "_Nt" + targetN;
  }
}
