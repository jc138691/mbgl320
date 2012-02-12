package func.bspline.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 21/07/2008, Time: 10:35:29 */
import junit.framework.*;
import func.bspline.BSplBasisFactory;

import javax.utilx.log.Log;

import math.integral.Quadr;
import math.vec.Vec;
import atom.wf.bspline.BSplBoundBasis;
public class BSplBasisFactoryTest extends TestCase {
  public static Log log = Log.getLog(BSplBasisFactoryTest.class);
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  public static Test suite() {
    TestSuite suite = new TestSuite();
//    suite.addTest(StepGridTest.suite());
//    suite.addTest(ZetaHyLCR.suite());
    suite.addTest(new TestSuite(BSplBasisFactoryTest.class));
    return suite;
  }
  public void testMakeDefaultLogCR() throws Exception {
    log.setDbg();
    double FIRST = 0;
    double LAST = 1;
    int BASIS_SIZE = 3;
    int GRID_SIZE = 17;
    int N_BLOCKS_PER_KNOT = 1;
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultLogCR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    assertEquals("BASIS_SIZE=", BASIS_SIZE, basis.size());
    Quadr w = basis.getNormQuadr();
    Vec grid = w.getX();
    log.assertZero("FIRST=", FIRST - grid.getFirst(), 1.e-18);
    log.assertZero("LAST=", LAST - grid.getLast(), 1.e-18);
    assertEquals("GRID_SIZE=", GRID_SIZE, grid.size());
  }
  public void testMakeDefaultLogCR2() throws Exception {
    log.setDbg();
    double FIRST = -2;
    double LAST = 2;
    int BASIS_SIZE = 5;
    int GRID_SIZE = 65;
    int N_BLOCKS_PER_KNOT = 2;
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultLogCR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    assertEquals("BASIS_SIZE=", BASIS_SIZE, basis.size());
    Quadr w = basis.getNormQuadr();
    Vec grid = w.getX();
    log.assertZero("FIRST=", FIRST - grid.getFirst(), 1.e-18);
    log.assertZero("LAST=", LAST - grid.getLast(), 1.e-18);
    assertEquals("GRID_SIZE=", GRID_SIZE, grid.size());
  }
  public void testMakeDefaultLogCR3() throws Exception {
    log.setDbg();
    double FIRST = -2;
    double LAST = 2;
    int BASIS_SIZE = 10;
    int GRID_SIZE = 145;
    int N_BLOCKS_PER_KNOT = 2;
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultLogCR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    assertEquals("BASIS_SIZE=", BASIS_SIZE, basis.size());
    Quadr w = basis.getNormQuadr();
    Vec grid = w.getX();
    log.assertZero("FIRST=", FIRST - grid.getFirst(), 1.e-18);
    log.assertZero("LAST=", LAST - grid.getLast(), 1.e-18);
    assertEquals("GRID_SIZE=", GRID_SIZE, grid.size());
  }
  public void testMakeDefaultLogCR4() throws Exception {
    log.setDbg();
    double FIRST = -2;
    double LAST = 3;
    int BASIS_SIZE = 20;
    int GRID_SIZE = 305;
    int N_BLOCKS_PER_KNOT = 2;
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultLogCR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    assertEquals("BASIS_SIZE=", BASIS_SIZE, basis.size());
    Quadr w = basis.getNormQuadr();
    Vec grid = w.getX();
    log.assertZero("FIRST=", FIRST - grid.getFirst(), 1.e-18);
    log.assertZero("LAST=", LAST - grid.getLast(), 1.e-18);
    assertEquals("GRID_SIZE=", GRID_SIZE, grid.size());
  }

}