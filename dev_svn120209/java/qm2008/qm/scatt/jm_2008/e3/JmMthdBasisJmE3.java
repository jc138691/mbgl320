package scatt.jm_2008.e3;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,4/04/11,9:55 AM
 */
import atom.shell.LsConfs;
import math.mtrx.api.Mtrx;
import scatt.jm_2008.e1.JmCalcOptE1;

import javax.utilx.log.Log;
import java.util.HashMap;
/**
 * Created by Dmitry.A.Konovalov@gmail.com, 02/06/2010, 2:46:32 PM
 */
public class JmMthdBasisJmE3 extends JmMthdBasisAnyE3 {
  public static Log log = Log.getLog(JmMthdBasisJmE3.class);

  public JmMthdBasisJmE3(JmCalcOptE1 calcOpt) {
    super(calcOpt);
  }

  @Override
  protected Mtrx calcX() {
    double[] D = getOverD().getArr();
    LsConfs sysBasis = sysConfH.getConfs();
    int sN = getSysBasisSize();
    int cN = getChNum();
    int N = calcOpt.getN();  // big N
    int nt = trgtE2.getNt();

    // optimization
    equalSysTrgt = new HashMap<String, String>(nt); // needed for  equals
    mapTrgtToSysIdx = new HashMap<String, Integer>(sN); // needed for  calcC

    Mtrx res = new Mtrx(cN, sN);
    for (int g = 0; g < cN; g++) {  // gIdx, g - for "little" gamma; target channel/state
      log.dbg("calcX{gamma = ", g);
      for (int i = 0; i < sN; i++) {  // iIdx, system state
          //  Any basis
//        double sum = 0;
//        for (int m = nt; m < N; m++) {  // mIdx, system state
//          double c = calcC(i, g, m);                          //log.dbg("c = ", c);
//          double d = D[m];                                    //log.dbg("d = ", d);
//          sum += (c * d);                                     //log.dbg("sum = ", sum);
//        }

        // NEW-3May2011: Optimized for JM-orthonormal companion functions
        int m = N-1;
        double c = calcC(i, g, m);                          //log.dbg("c = ", c);
        double d = D[m];                                    //log.dbg("d = ", d);
        double sum = (c * d);                                     //log.dbg("sum = ", sum);

        res.set(g, i, sum);                           //log.dbg("X[" + g + ", " + i + "]=", sum);
      }
    }
    return res;
  }
}
