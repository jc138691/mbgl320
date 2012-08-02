package math.func.polynom.laguerre;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import math.integral.OrthFactory;
import math.integral.QuadrPts5;
import math.vec.grid.StepGrid;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrTailSearch;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 14:18:14
 */
public class LagrrOrthTest extends TestCase {
  public static Log log = Log.getLog(LagrrOrthTest.class);
  public static Test suite() {
    return new TestSuite(LagrrOrthTest.class);
  }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  public void testLagrrOrthon() {
    FuncArrTailSearch.log.setDbg();

    double FIRST = 0;
    double LAST = 60;
    int GRID_SIZE = 501;
    StepGrid r = new StepGrid(FIRST, LAST, GRID_SIZE);
    QuadrPts5 w = new QuadrPts5(r);

    int L = 0;
    int N = 4;
    int alpha = 2 * L + 2;
    double lambda = 0.7;
    FuncArr arr = new LgrrOrth(r, N, alpha, lambda);
    FuncArrTailSearch search = new FuncArrTailSearch(arr);
    double eps = 2e-8;
    search.findLastGE(eps);
    log.info("search=", search);

    double err = OrthFactory.calcMaxOrthErr(arr, w);
    log.assertZero("orthonorm error=", err, 1e-20) ;

  }

}
