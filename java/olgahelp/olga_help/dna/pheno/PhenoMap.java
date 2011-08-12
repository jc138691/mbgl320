package dna.pheno;

import javax.swing.*;
import javax.utilx.log.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/08/2009, 10:21:21 AM
 */
public class PhenoMap {
  public static Log log = Log.getLog(PhenoMap.class);
  private TreeMap<Integer, Integer> mapReadToPhenoIdx;
  private PhenoSet phenoSet;

  public PhenoMap() {
    mapReadToPhenoIdx = new TreeMap<Integer, Integer>();
    phenoSet = new PhenoSet();
  }

  public void add(PhenoRead item) {
    Integer idx = phenoSet.add(item.getPheno());
    mapReadToPhenoIdx.put(item.getReadIdKey(), idx);
  }

  public int size() {
    return mapReadToPhenoIdx.size();
  }

  public PhenoSet getPhenoSet() {
    return phenoSet;
  }

  public Pheno getPheno(int readIdkey) {
    Integer idx = mapReadToPhenoIdx.get(readIdkey);
    if (idx == null) {
      return null;
    }
    return phenoSet.get(idx);
  }
}
