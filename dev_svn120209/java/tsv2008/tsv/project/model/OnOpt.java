package project.model;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 03/09/2009, 9:14:46 AM
 */
public class OnOpt {
  private boolean on;

  public OnOpt(boolean b) {
    on = b;
  }

  final public boolean getOn() {    return on;  }
  final public boolean on() {   return on;  }

  public void setOn(boolean on) {
    this.on = on;
  }

}
