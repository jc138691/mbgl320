package atom.e_3;

import atom.shell.Ls;
/**
 * Created by Dmitry.A.Konovalov@gmail.com, 03/06/2010, 11:51:13 AM
 */
public class AtomShModelE3 {
  private Ls ls;
  private int n1;
  private int n2;
  private int n3;

  public AtomShModelE3(int n, int n2, int n3, Ls ls) {
    n1 = n;
    this.n2 = n2;
    this.n3 = n3;
    this.ls = ls;
  }
  public String toString() {
    return "(size1,2,3)=(" + n1 +", "+ n2 +", "+ n3 +"); LS="+ls;
  }


  public int getN3() {
    return n3;
  }

  public void setN3(int n3) {
    this.n3 = n3;
  }

  public int getN2() {
    return n2;
  }

  public void setN2(int n2) {
    this.n2 = n2;
  }

  public int getN1() {
    return n1;
  }

  public void setN1(int n1) {
    this.n1 = n1;
  }

  public Ls getLs() {
    return ls;
  }

  public void setLs(Ls ls) {
    this.ls = ls;
  }
}
