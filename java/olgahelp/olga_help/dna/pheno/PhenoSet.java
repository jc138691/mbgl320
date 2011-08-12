package dna.pheno;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/08/2009, 4:27:45 PM
 */
public class PhenoSet {
  private TreeMap<Pheno, Integer> mapPhenoToIdx;
  private ArrayList<Pheno> set;

  public PhenoSet() {
    mapPhenoToIdx = new TreeMap<Pheno, Integer> ();
    set = new ArrayList<Pheno>();
  }

  public Integer add(Pheno pheno) {
    Integer idx = mapPhenoToIdx.get(pheno);
    if (idx == null) {
      idx = set.size();
      mapPhenoToIdx.put(pheno, idx);
      set.add(pheno);
    }
    return idx;
  }

  public int size() {
    return set.size();
  }
  public String[] toStrArr() {
    String[] res = new String[set.size()];
    for (int i = 0; i < res.length; i++) {
      res[i] = set.get(i).getId();
    }
    return res;
  }

  public String toString() {
    StringBuffer buff = new StringBuffer();
    for (Iterator<Pheno> it = set.iterator(); it.hasNext(); ) {
      Pheno tag = it.next();
      buff.append(tag.getId());
      if (it.hasNext()) {
        buff.append(", ");
      }
    }
    return buff.toString();
  }

  public Pheno get(int idx) {
    return set.get(idx);
  }

  public int getIdx(Pheno pheno) {
    return mapPhenoToIdx.get(pheno);
  }
}
