package scatt.jm_2008.jm;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 7/10/2008, Time: 16:44:49
 */
public class JmTestModel {
  private float maxIntgrlErr;
  public void loadDefault() {
    loadDefault(this);
  }
  public static void loadDefault(JmTestModel model) {
    model.setMaxIntgrlErr(0.000001f);    
  }
  public float getMaxIntgrlErr() {
    return maxIntgrlErr;
  }
  public void setMaxIntgrlErr(float maxIntgrlErr) {
    this.maxIntgrlErr = maxIntgrlErr;
  }
}
