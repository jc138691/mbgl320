package dna.table;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 01/04/2009, 1:21:32 PM
 */
public class PageOpt {
  private int pageLineIdx;
  private int pageSize;
  private int pageOffsetIdx;

  public void moveFirstLine() {
    setPageLineIdx(0);
  }
  public void moveLastLine() {
    setPageLineIdx(pageSize - 1);
  }
  public void moveNextLine() {
    setPageLineIdx(pageLineIdx + 1);
  }
  public void movePrevLine() {
    setPageLineIdx(pageLineIdx - 1);
  }
  public void makeValidCurrPos(int maxVal) {
    setPageLineIdx(Math.min(maxVal, pageLineIdx));
    setPageLineIdx(Math.max(0, pageLineIdx));
  }
  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageOffsetIdx() {
    return pageOffsetIdx;
  }

  public void setPageOffsetIdx(int pageOffsetIdx) {
    this.pageOffsetIdx = pageOffsetIdx;
  }

  public int getPageLineIdx() {
    return pageLineIdx;
  }

  public void setPageLineIdx(int pageLineIdx) {
    this.pageLineIdx = pageLineIdx;
  }
}
