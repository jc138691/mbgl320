package javax.swingx.colorx;

import project.model.OnOpt;
import project.model.IntOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 15/05/2009, 12:30:21 PM
 */
public class ColorOpt extends IntOpt {
  public ColorOpt(int rgb) {
    super(rgb);
  }

  public int getRgb() {
    return getVal();
  }
  public void setRgb(int rgb) {
    setVal(rgb);
  }
}
