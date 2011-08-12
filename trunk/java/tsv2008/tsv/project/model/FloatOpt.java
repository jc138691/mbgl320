package project.model;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 02/10/2009, 1:02:13 PM
 */
public class FloatOpt extends OnOpt{
  private float val;

  public FloatOpt(boolean b, float i) {
    super(b);
    val = i;
  }

  public FloatOpt(float i) {
    super(true);
    val = i;
  }

  final public float getVal() {   return val;  }
  final public float val() {    return val;  }

  public void setVal(float val) {
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

  public void set(boolean b, float i) {
    setOn(b);
    setVal(i);
  }
}
