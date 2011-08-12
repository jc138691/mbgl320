package dna.seq.arr;

import java.util.ArrayList;
import java.util.HashMap;

import dna.seq.Seq;
import dna.table.DnaTableInfo;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 16/02/2009 at 14:01:52.
 */
public class SeqArr {
  private String id;
  private ArrayList<Seq> arr;
  private HashMap<Integer, Seq> mapIdToRead;

  public SeqArr() {
//    info = new DnaTableInfo();
    arr = new ArrayList<Seq>();
    mapIdToRead = new HashMap<Integer, Seq>();
  }

  public SeqArr(SeqArr from) {
//    this.info = from.info;
    this.arr = from.arr;
    this.mapIdToRead = from.mapIdToRead;
  }

  public void add(Seq s) {
    mapIdToRead.put(s.getIdKey(), s);
    arr.add(s);
//    updateInfo(info, s);
  }

  public static void updateInfo(DnaTableInfo info, String seq) {
    if (info == null)
      return;
    info.setNumRows(info.getNumRows() + 1);
    int minLen = info.getMinLen();
    if (minLen > seq.length())
      info.setMinLen(seq.length());
    int maxLen = info.getMaxLen();
    if (maxLen < seq.length())
      info.setMaxLen(seq.length());
  }

  public static void updateInfo(DnaTableInfo prop, Seq dna) {
    updateQltyInfo(prop, dna);
    prop.setNumRows(prop.getNumRows() + 1);
    int minLen = prop.getMinLen();
    if (minLen > dna.size())
      prop.setMinLen(dna.size());
    int maxLen = prop.getMaxLen();
    if (maxLen < dna.size())
      prop.setMaxLen(dna.size());
  }

  public static void updateQltyInfo(DnaTableInfo info, Seq dna) {
    if (info == null)
      return;
    int minQlty = dna.calcMinQlty();
    int saved = info.getMinQlty();
    if (saved > minQlty)
      info.setMinQlty(minQlty);

    int maxQlty = dna.calcMaxQlty();
    saved = info.getMaxQlty();
    if (saved < maxQlty)
      info.setMaxQlty(maxQlty);
  }



  public Seq getByIdx(int idx) {
    return arr.get(idx);
  }
  public Seq getByKey(int key) {
    return mapIdToRead.get(key);
  }
  final public int calcMaxSeqLen() {
    int maxSeqLen = 0;
    for (int i = 0; i < size(); i++) { //   This will work with PageOpt
      Seq dna = getByIdx(i);
      if (dna.size() == 0)
        continue;
      int len = dna.size();
      if (maxSeqLen < len)
        maxSeqLen = len;
    }
    return maxSeqLen;
  }

  final public int calcMaxQlty() {
    int res = 0;
    for (int i = 0; i < size(); i++) { //   This will work with PageOpt
      Seq dna = getByIdx(i);
      int seqMax = dna.calcMaxQlty();
      if (res < seqMax)
        res = seqMax;
    }
    return res;
  }

  final public int calcMinQlty() {
    int res = 0;
    for (int i = 0; i < size(); i++) { //   This will work with PageOpt
      Seq dna = getByIdx(i);
      int seqMin = dna.calcMinQlty();
      if (res > seqMin)
        res = seqMin;
    }
    return res;
  }

  public int size() {
    return arr.size();
  }

  public DnaTableInfo calcInfo() {
    DnaTableInfo res = new DnaTableInfo();
    res.setMaxLen(calcMaxSeqLen());
    res.setMinQlty(calcMinQlty());
    res.setMaxQlty(calcMaxQlty());
    return res;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  public Seq getLast() {
    return arr.get(arr.size()-1);
  }

}
