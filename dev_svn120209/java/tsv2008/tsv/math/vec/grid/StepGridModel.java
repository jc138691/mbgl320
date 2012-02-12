package math.vec.grid;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 12/09/2008, Time: 15:05:14
 */
public class StepGridModel {
  private double first;
  private double last;
  private int numPoints;

  public StepGridModel() {
  }
  public StepGridModel(double first, double last, int n) {
    this.first = first;
    this.last = last;
    this.numPoints = n;
  }


  public double getFirst() {
    return first;
  }
  public void setFirst(double first) {
    this.first = first;
  }
  public double getLast() {
    return last;
  }
  public void setLast(double last) {
    this.last = last;
  }
  public int getNumPoints() {
    return numPoints;
  }
  public void setNumPoints(int numPoints) {
    this.numPoints = numPoints;
  }
  public String toString() {
    return "StepGridModel(first=" + (float)first + ", last=" + (float)last + ", nPts=" + numPoints + ")";
  }
}
