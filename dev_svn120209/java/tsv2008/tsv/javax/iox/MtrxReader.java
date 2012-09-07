package javax.iox;
import math.vec.Vec;

import javax.utilx.log.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
/**
* Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 7/09/12, 11:47 AM
*/
public class MtrxReader {
public static Log log = Log.getLog(MtrxReader.class);
private String delim;
public MtrxReader(String delim) {
  this.delim = delim;
}

public double[][] readMtrx(int nRows, int nCols, TextFile from) {
  return readMtrx(nRows, nCols, from, 0);
}

public double[][] readMtrx(int nRows, int nCols, String fileName) {
  return readMtrx(nRows, nCols, 0, fileName);
}

public double[][] readMtrx(int nRows, int nCols, int ignoreCols, String fileName) {
  File file = new File(fileName);
  TextFile from = new TextFile();
  from.read(file, null);
  from.trim();
  log.info("\nfrom file = \n" + from);
  return readMtrx(nRows, nCols, from, ignoreCols);
}

public double[][] readMtrx(int nRows, int nCols, TextFile from, int ignoreCols)
{
  if (nRows > from.size()) {
    log.error("nRows > from.size()");
    return null;
  }
  if (nRows == -1) {
    nRows = from.size();
  }
  double[][] arr = new double[nRows][0];
  for (int r = 0; r < nRows; r++) {
    String line = from.getLine(r).trim();
    if (line == null  || line.length() == 0)
      continue;
    double[] rArr = readArr(ignoreCols, nCols, line);
    arr[r] = rArr;
  }
  return arr;
}

private double[] readArr(int ignoreCols, int maxCols, String line)  {
//    log.info("line=" + line);
  StringTokenizer tokens = new StringTokenizer(line, delim, false);
//    if (maxCols > -1  &&  maxCols > tokens.countTokens()) {
//      String error = "Error in readDoubleArr: maxCols > tokens.countTokens()";
//      log.error(error);
//      return null;
//    }

  int nCols = maxCols;
  if (maxCols == -1) {
    nCols = tokens.countTokens();
  }
  else {
    nCols = Math.min(tokens.countTokens(), maxCols);
  }

  String token;
  ArrayList<Double> tmpArr = new ArrayList<Double>();
  int countCols = 1;
  while (tokens.hasMoreTokens()  &&  (tmpArr.size() < nCols  ||  maxCols == -1)) {
    token = tokens.nextToken().trim();
    if (countCols++ <= ignoreCols)
      continue;
    try {
      Double val = new Double(token);
      tmpArr.add(val);
    }
    catch (NumberFormatException e) {
      tmpArr.add(new Double(0));
//        String error = "Unable to import double from text=[" + token.trim() + "]";
//        log.severe(error);
//        JOptionPane.showMessageDialog(null, error);
//        return null;
    }
  }
//    return Vec.asArray(tmpArr, maxCols);
  return Vec.convert(tmpArr);
}
}
