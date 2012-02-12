package atom.e_3;

import atom.shell.Ls;

import javax.langx.SysProp;
/**
 * Created by Dmitry.A.Konovalov@gmail.com, 03/06/2010, 11:51:13 AM
 */
public class AtomShModelE3 {
  private Ls ls;
  private int size;
  private int size2;
  private int size3;

  public AtomShModelE3(int n, int n2, int n3, Ls ls) {
    size = n;
    size2 = n2;
    size3 = n3;
    this.ls = ls;
  }
  public String toString() {
    return "(size1,2,3)=(" + size +", "+size2 +", "+size3 +"); LS="+ls;
  }


  public int getSize3() {
    return size3;
  }

  public void setSize3(int size3) {
    this.size3 = size3;
  }

  public int getSize2() {
    return size2;
  }

  public void setSize2(int size2) {
    this.size2 = size2;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Ls getLs() {
    return ls;
  }

  public void setLs(Ls ls) {
    this.ls = ls;
  }
}
