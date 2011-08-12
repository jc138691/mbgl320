package project.model;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 03/09/2009, 9:13:48 AM
 */
public class IntOpt extends OnOpt{
  private int val;

  public IntOpt(boolean b, int i) {
    super(b);
    val = i;
  }

  public IntOpt(int i) {
    super(true);
    val = i;
  }

  final public int getVal() {   return val;  }
  final public int val() {    return val;  }

  public void setVal(int val) {
    this.val = val;
  }
  public String printOpt(String res, String optName) {
    if (!getOn())
      return res;
    if (res.length() > 0)
      res += ", ";
    res += (optName + "=" + getVal());
    return res;
  }

  public void set(boolean b, int i) {
    setOn(b);
    setVal(i);
  }
}
