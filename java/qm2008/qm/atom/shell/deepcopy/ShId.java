package atom.shell.deepcopy;

import atom.shell.Ls;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 03/06/2010, 10:14:56 AM
 */
public class ShId implements Comparable<ShId> { // "fast" shell is
  public static Log log = Log.getLog(ShId.class);
  private int idx;   // ABSOLUTE shell index, i.e. 0,1,...,n
  private int l;     // radial wavefunction L

  public ShId(ShId from) {
    this.idx = from.idx;
    this.l = from.l;
  }
  public ShId(int idx, int L) {
    this.idx = idx;
    this.l = L;
  }
  public int getIdx() {
    return idx;
  }

  public void setIdx(int idx) {
    this.idx = idx;
  }

  public int getL() {
    return l;
  }

  public void setL(int l) {
    this.l = l;
  }

  public String toString() {
    return Integer.toString(idx) + Ls.toString(l);
  }

  // THIS IS VERY VERY IMPORTANT!!!!!!!!
  // This is some fixed ordering
  // 1s, 2s, 2p, 3s, 3p, 3d, ...
  final public int compareTo(ShId id) {
    if (id == this)
      return 0;
    int res = this.idx - id.idx;
    if (res != 0)
      return res;
    return res = l - id.l;
  }

  @Override
  final public boolean equals(Object from) { // STOP overwriting
    if (!(from instanceof ShId)) {
      throw new IllegalArgumentException(log.error("!(from instanceof ShId)"));
    }
    return compareTo((ShId)from) == 0;
  }
}
