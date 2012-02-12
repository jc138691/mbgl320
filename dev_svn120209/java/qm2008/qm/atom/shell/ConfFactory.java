package atom.shell;

import atom.angular.Spin;
import math.func.FuncVec;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,08/12/2010,9:47:36 AM
 */
public class ConfFactory {

  public static Conf makeLi_1s2_2s_2S(FuncVec f_1s, FuncVec f_2s) {
    // Making Li(1s^2, 2s)
    int L = 0;
    Ls LS = new Ls(L, Spin.SINGLET);
    ShellQ2 sh = new ShellQ2(0, f_1s, L, LS);
    Shell sh2 = new Shell(1, f_2s, L);
    Conf res = new ShPair(sh, sh2, new Ls(L, Spin.ELECTRON));
    return res;
  }
  public static Conf makeLi_1s_2s2_2S(FuncVec f_1s, FuncVec f_2s) {
    // Making Li(1s^2, 2s)
    int L = 0;
    Ls LS = new Ls(L, Spin.SINGLET);
    Shell sh = new Shell(0, f_1s, L);
    ShellQ2 sh2 = new ShellQ2(1, f_2s, L, LS);
    Conf res = new ShPair(sh, sh2, new Ls(L, Spin.ELECTRON));
    return res;
  }

}
