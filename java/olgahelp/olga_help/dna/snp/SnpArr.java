package dna.snp;

import javax.langx.SysProp;
import java.util.ArrayList;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/05/2009, 11:54:52 AM
 */
public class SnpArr extends ArrayList<Snp> {
//  static StringBuffer buff = new StringBuffer();
  private static final int MAX_N_PREVIEW = 10;

  public String toString(String rowHeader, SnpOpt opt, boolean withHelp) {
    return toString(rowHeader, -1, opt, withHelp);
  }
  public String toString(String rowHeader, int maxCount, SnpOpt opt, boolean withHelp) {
    StringBuffer buff = new StringBuffer();
//    buff.setLength(0);
    int count = 0;
    for (int i = 0; i < size(); i++) {
      Snp snp = get(i);
      if (!snp.isSnp() || !snp.isValidSnp())
        continue;

      buff.append(rowHeader).append(" \t");
//      if (maxCount != -1)
      buff.append(count+1).append(". \t");

      if (withHelp) {
        buff.append(snp.getHelpMssg(opt)).append(" \t");
      }
      buff.append(snp.toString(opt)).append(SysProp.EOL);

      count++;
      if (maxCount != -1  &&  count >= maxCount)
        break;
    }
    String res = buff.toString();
    buff.setLength(0);
    return res;
  }


  public String makeExportPreview(SnpOpt opt) {
    StringBuffer buff = new StringBuffer();
    int count = countSnps();
    if (count == 0) {
      return " has no SNPs.";
    }
    else {
      if (count > MAX_N_PREVIEW) {
        buff.append(" contains the following 10 (of "+count+") SNPs:").append(SysProp.EOL);
      }
      else {
        buff.append(" contains the following "+count+" SNP(s):").append(SysProp.EOL);
      }
    }
    buff.append(toString("This contig:", MAX_N_PREVIEW, opt, true));
    return buff.toString();
  }

  private int countSnps() {
    int count = 0;
    for (int i = 0; i < size(); i++) {
      Snp snp = get(i);
      if (!snp.isSnp() || !snp.isValidSnp())
        continue;
      count++;
    }
    return count;
  }
}
