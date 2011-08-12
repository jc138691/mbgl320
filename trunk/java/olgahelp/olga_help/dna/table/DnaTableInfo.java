package dna.table;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 20/03/2009, 2:26:05 PM
 */
public class DnaTableInfo {
  private int numRows;
  private int minLen;
  private int maxLen;
  private int minQlty;
  private int maxQlty;

  public int getMinLen() {
    return minLen;
  }

  public void setMinLen(int minLen) {
    this.minLen = minLen;
  }

  public int getNumRows() {
    return numRows;
  }

  public void setNumRows(int numRows) {
    this.numRows = numRows;
  }

  public int getMaxLen() {
    return maxLen;
  }

  public void setMaxLen(int maxLen) {
    this.maxLen = maxLen;
  }

  public String toString() {
    return "minLen=" + minLen + ", maxLen=" + maxLen + ", numRows=" + numRows;
  }

  public int getMinQlty() {
    return minQlty;
  }

  public void setMinQlty(int minQlty) {
    this.minQlty = minQlty;
  }

  public int getMaxQlty() {
    return maxQlty;
  }

  public void setMaxQlty(int maxQlty) {
    this.maxQlty = maxQlty;
  }
}
