package papers.project_setup;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 7/10/2008, Time: 16:44:49
 */
public class ProjTestOpt {
  private double maxIntgrlErr;
  public void loadDefault() {
    loadDefault(this);
  }
  public static void loadDefault(ProjTestOpt model) {
    model.setMaxIntgrlErr(0.000001f);    
  }
  public double getMaxIntgrlErr() {
    return maxIntgrlErr;
  }
  public void setMaxIntgrlErr(double maxIntgrlErr) {
    this.maxIntgrlErr = maxIntgrlErr;
  }
}
