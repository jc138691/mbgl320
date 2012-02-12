package math.func.polynom.laguerre;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import math.vec.grid.StepGrid;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 10:27:16
 */
public class LagrrArrTest extends TestCase {
  private double EPS = 1e-16;
  public static Test suite() {
    return new TestSuite(LagrrArrTest.class);
  }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  public void testLagrrArr() {
    StepGrid grid = new StepGrid(0., 1., 5);
    LgrrArr a12 = new LgrrArr(grid, 13, 0, 1);
    LgrrArr arr = new LgrrArr(grid, 1, 0, 1);
    assertEquals(1., arr.get(0).get(0), EPS);
    assertEquals(1., a12.get(0).get(0), EPS);
    assertEquals(1., arr.get(0).get(4), EPS);
    assertEquals(1., a12.get(0).get(4), EPS);
    arr = new LgrrArr(grid, 2, 0, 1);
    assertEquals(0.5, arr.get(1).get(2), EPS);
    assertEquals(0.5, a12.get(1).get(2), EPS);
    assertEquals(0., arr.get(1).get(4), EPS);
    assertEquals(0., a12.get(1).get(4), EPS);
    arr = new LgrrArr(grid, 3, 0, 1);
    assertEquals(-0.5, arr.get(2).get(4), EPS);
    assertEquals(-0.5, a12.get(2).get(4), EPS);
    arr = new LgrrArr(grid, 4, 0, 1);
    assertEquals(-2.0 / 3, arr.get(3).get(4), EPS);
    assertEquals(-2.0 / 3, a12.get(3).get(4), EPS);
    assertEquals(0., Math.abs(0.4962122235 - a12.get(12).get(4)), 1e-11);
    grid = new StepGrid(0., 5., 5);
    a12 = new LgrrArr(grid, 13, 0, 1);
    assertEquals(0., Math.abs(-1.4486042948 - a12.get(12).get(4)), 5e-11);
  }
}
