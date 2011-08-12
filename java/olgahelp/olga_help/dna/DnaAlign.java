package dna;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 08/04/2009, 4:04:54 PM
 */
public class DnaAlign {
  private int s;
  private int s2;
  private int r;
  private int r2;

  protected DnaAlign() {
    s = -1;
    s2 = -1;
    r = -1;
    r2 = -1;
  };
  public DnaAlign(int s, int s2, int r, int r2) {
    this.s = s;
    this.s2 = s2;
    this.r = r;
    this.r2 = r2;
  }

  public DnaAlign(DnaAlign from) {
    this.s = from.s;
    this.s2 = from.s2;
    this.r = from.r;
    this.r2 = from.r2; 
  }

  public int getMaxS() {
    return Math.max(s, s2);
  }
  public int getMinS() {
    return Math.min(s, s2);
  }

  public int getMaxR() {
    return Math.max(r, r2);
  }
  public int getMinR() {
    return Math.min(r, r2);
  }

  public int getS() {
    return s;
  }

  public void setS(int s) {
    this.s = s;
  }

  public int getS2() {
    return s2;
  }

  public void setS2(int s2) {
    this.s2 = s2;
  }

  public int getR() {
    return r;
  }

  public void setR(int r) {
    this.r = r;
  }

  public int getR2() {
    return r2;
  }

  public void setR2(int r2) {
    this.r2 = r2;
  }
}
