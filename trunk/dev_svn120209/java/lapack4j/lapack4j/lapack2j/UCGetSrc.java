package lapack4j.lapack2j;
import lapack4j.utils.Fortran77;

import java.util.ArrayList;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,17/10/11,12:27 PM
 */
public class UCGetSrc extends Lapack2J {
  public static void main(String[] args) {
    UCGetSrc runMe = new UCGetSrc();
    runMe.JOB_TAG = ".f";
    runMe.SRC_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\src\\lapack";
    runMe.DEST_DIR = "C:\\dev\\physics\\dev_svn_110812\\java\\lapack4j\\lapack4j\\src\\fortran_src";
    for (int i = 0; i < FILE_NAMES.length; i++) {
      String fileName = FILE_NAMES[i];
      runMe.convert(fileName);
    }
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
    if (Fortran77.comment(srcLine)) {
      dest.add(srcLine);
      return true;
    }
    return false;
//    return Fortran77.comment(srcLine);
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
