package math.mtrx;
import math.mtrx.api.Mtrx;

import javax.utilx.arraysx.StrVec;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/09/11,9:30 AM
 */
public class MtrxInfo extends Mtrx {
  private StrVec colHrds;
  private StrVec rowHrds;
  public MtrxInfo(Mtrx from) {
    super(from);
  }
  @Override
  public String toGnuplot() {
    String res = "# ";
    if (colHrds != null) {
      res += colHrds.toString();
    }
    return super.toTab();
  }
  public StrVec getColHrds() {
    return colHrds;
  }
  public void setColHrds(StrVec colHrds) {
    this.colHrds = colHrds;
  }
  public StrVec getRowHrds() {
    return rowHrds;
  }
  public void setRowHrds(StrVec rowHrds) {
    this.rowHrds = rowHrds;
  }
}