package scatt.jm_2008.jm.laguerre.lcr;
import atom.wf.log_cr.FuncRToLcr;
import atom.wf.log_cr.WFQuadrLcr;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.utilx.log.Log;

import math.func.arr.FuncArrTailSearch;
import math.func.arr.FuncArr;
import math.func.polynom.laguerre.LgrrOrth;
import math.integral.QuadrStep5;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.integral.OrthonFactory;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 16:44:53
 */
public class LagrrOrthLCRTest_tmp extends TestCase {
  public static Log log = Log.getLog(LagrrOrthLCRTest_tmp.class);
  public static Test suite() {
    return new TestSuite(LagrrOrthLCRTest_tmp.class);
  }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  public void testLagrrOrthon() {
//    Log.setup();
    FuncArrTailSearch.log.setDbg();
    WFQuadrLcr.log.setDbg();

    double FIRST = 0;
    double LAST = 60;
    int GRID_SIZE = 501;
    StepGrid r = new StepGrid(FIRST, LAST, GRID_SIZE);
    QuadrStep5 w = new QuadrStep5(r);

    int L = 0;
    int N = 4;
    int alpha = 2 * L + 2;
    double lambda = 0.7;
    FuncArr arr = new LgrrOrth(r, N, alpha, lambda);
    FuncArrTailSearch search = new FuncArrTailSearch(arr);
    double eps = 2e-8;
    search.findLastGE(eps);
    log.info("search=", search);

    double err = OrthonFactory.calcMaxOrthonErr(arr, w);
    log.assertZero("orthonorm error on r=", err, 4e-6) ;

    // THE SAME in LogCR grid
    FuncRToLcr rToLCR = new FuncRToLcr(-1, 0);
    GRID_SIZE = 101;
    StepGrid x = new StepGrid(FIRST, rToLCR.calc(LAST), GRID_SIZE);
    WFQuadrLcr wx = new WFQuadrLcr(x);
    Vec rx = wx.getR();
    arr = new LgrrOrth(rx, N, alpha, lambda);
    err = OrthonFactory.calcMaxOrthonErr(arr, wx.getWithCR());
    log.assertZero("orthonorm error on logCR=", err, 1e-20) ;
  }
}
