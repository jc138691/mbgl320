package dna.snp.flank;

import dna.snp.Snp;
import dna.snp.SnpFilterFlankOpt;

import java.util.ArrayList;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 18/05/2009, 4:20:08 PM
 */
public class FlankArr extends ArrayList<Flank> {
  static StringBuffer buff = new StringBuffer();
  public String toString(SnpFilterFlankOpt opt) {
    buff.setLength(0);
    for (int i = 0; i < size(); i++) {
      Flank f = get(i);
      buff.append(toString(f, opt));
    }
    String res = buff.toString();
    buff.setLength(0);
    return res;
  }
  public String  toString(Snp f, SnpFilterFlankOpt opt) {
    if (f.getMajorCount() == 0)
      return "";   // ignore empty inserts
    if (f.hasPadding()) {
      if (opt.getMaxInsertsPcnt().on() &&  (opt.getMaxInsertsPcnt().val() >= f.getInsertsPcnt())) {
        return "";   // ignore inserts
      }
      if (opt.getMaxDeletesPcnt().on() &&  (opt.getMaxDeletesPcnt().val() < f.getPaddingPcnt())) {
        return "X";  // problem! padding is too large
      }
    }
    if (opt.getIupacMinMinorFreqPcnt().on() &&  (opt.getIupacMinMinorFreqPcnt().val() <= f.getMinorFreqPcnt())) {
      return Character.toString(f.getIUPAC());
    }
    return Character.toString(f.getMajorBase());
  }
}
