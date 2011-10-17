package lapack4j.lapack2j;
import lapack4j.utils.Fortran77;

import java.util.ArrayList;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,17/10/11,12:27 PM
 */
public class UCGetSrc extends Lapack2J {
  public static void main(String[] args) {
    UCGetSrc runMe = new UCGetSrc();
    JOB_TAG = "_src.f";
    DEST_DIR = SRC_DIR;
    runMe.convert(SRC_DIR, "lsame.f", DEST_DIR);
    runMe.convert(SRC_DIR, "dstemr.f", DEST_DIR);
    System.exit(0);
  }
  public ArrayList<String> convert2J(ArrayList<String> src) {
    ArrayList<String> res = new ArrayList<String>();
    for (int i = 0; i < src.size(); i++) {
      String line = src.get(i);
      if (line == null)
        break;
      debug(line);
      String lineTrim = line.trim();
      if (isEmpty(line, res))  continue;
      if (comment(line, res))  continue;
      if (splitLine(line, res))  continue;
      res.add(line);
    }
    return res;
  }

  public boolean isEmpty(String srcLine, ArrayList<String> dest) {
    return srcLine.isEmpty();
  }
  public boolean comment(String srcLine, ArrayList<String> dest) {
    return Fortran77.comment(srcLine);
  }
  public boolean splitLine(String srcLine, ArrayList<String> dest) {
    if (!Fortran77.splitLine(srcLine))
      return false;
    String str = Fortran77.removeSplit(srcLine);
    int idx = dest.size() - 1;
    String last = dest.get(idx);
    last = last + str;
    dest.set(idx, last);
    return true;
  }
}
