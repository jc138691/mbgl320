package atom.shell;

import atom.angular.AngMomRules;
import atom.angular.Spin;
import atom.shell.deepcopy.ShId;
import atom.shell.deepcopy.ShWf;
import math.func.FuncVec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 03/06/2010, 10:10:27 AM
 */
public class Shell {
  private final int q;    // number of electrons in the shell
  private final Ls ls; // total LS
  private final ShWf wf;

  public Shell(int idx, FuncVec rwf, int q, int wfL, Ls totLs) {
    this.q = q;
    this.ls = totLs;
    this.wf = new ShWf(idx, wfL, rwf);
  }

  public Shell(Shell from) {
    this.q = from.q;
    this.ls = from.ls;
    this.wf = from.wf;
  }
  public Shell(int q, ShWf wf, Ls totLs) {
    this.q = q;
    this.ls = totLs;
    this.wf = wf;
  }
  public Shell(int idx, FuncVec rwf, int L) {  // one electron
    this.q = 1;
    this.ls = new Ls(L, Spin.ELECTRON);  // NOTE!!! deepcopy
    this.wf = new ShWf(idx, L, rwf);
  }

  public Shell(ShWf wf) {  // one electron
    this.q = 1;
    this.ls = new Ls(wf.getL(), Spin.ELECTRON);  // NOTE!!! deepcopy
    this.wf = wf;
  }
  public String toString() {
    StringBuffer buff = new StringBuffer();
    buff.append("" + ls);
    if (q > 1) {
      buff.append("(").append(wf.getId()).append(")^" + q);
    } else {
      buff.append("(").append(wf.getId()).append(")");
    }
    return buff.toString();
  }

  public ShId getId() {
    return wf.getId();
  }
  public int getIdx() {
    return wf.getIdx();
  }
  public int getQ() {
    return q;
  }
  public Ls getLs() {
    return ls;
  }

  public ShWf getWf() {
    return wf;
  }
  public int getWfL() {
    return wf.getL();
  }

//  public void setWf(ShWf wf) {
//    this.wf = wf;
//  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    Shell from = (Shell) obj;
    if (!getId().equals(from.getId()))
      return false;
    if (q != from.q)
      return false;
    return ls.equals(from.ls);
  }

  public boolean isValid() {
    int wfL = wf.getL();
    int wfS2 = Spin.ELECTRON.getS21();
    if (q == 1 || q == calcMaxElec() - 1) {
      if (wfL != ls.getL())
        return false;
      if (ls.getS21() != Spin.ELECTRON.getS21())
        return false;
    }
    if (q == calcMaxElec()) {
      if (!ls.equals(Ls.CLOSED_SHELL))
        return false;
    }
    if (q == 2) {
      if (!AngMomRules.isValid(wfL, wfL, ls.getL()))
        return false;
      if (!AngMomRules.isValid(wfS2, wfS2, ls.getS21()))
        return false;
    }
    return true;
  }

  private int calcMaxElec() {
    return 2 * (2 * getWfL() + 1);
  }
  
}
