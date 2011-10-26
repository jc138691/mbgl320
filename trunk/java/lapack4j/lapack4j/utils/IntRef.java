package lapack4j.utils;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,20/10/11,11:31 AM
 */
public class IntRef {
  private int val;
  public int get() {
    return val;
  }
  public void set(int val) {
    this.val = val;
  }
  public static void set(IntRef pV, int v, IntRef pV2, int v2) {
    pV.set(v);
    pV2.set(v2);
  }
  public static void set(IntRef pV, int v, IntRef pV2, int v2, IntRef pV3, int v3) {
    set(pV, v, pV2, v2);
    pV3.set(v3);
  }
}
