package atom.shell;

import atom.shell.deepcopy.ShId;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,15/10/2010,2:34:35 PM
 */
public class ShInfo {
  public final int idx;   // This is !!!RELATIVE!!! index of this shell in the configuration 
  public final Shell sh;
  public final int q;
  public final ShId id;

  public ShInfo(int idx, Shell sh) {
    this.idx = idx;
    this.sh = sh;
    this.q = sh.getQ();
    this.id = sh.getId();
  }
  public String toString() {
     return "ShInfo.idx=" + idx + ", q=" + q + ", id=" + id
       + ", sh=" + sh;
  }

}
