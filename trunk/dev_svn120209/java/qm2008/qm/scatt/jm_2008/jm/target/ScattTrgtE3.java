package scatt.jm_2008.jm.target;
import atom.energy.ConfHMtrx;
import math.vec.Vec;

import javax.utilx.log.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,6:00 PM
 */
public class ScattTrgtE3 extends ScattTrgtE2 {
  public static Log log = Log.getLog(ScattTrgtE3.class);
  private ArrayList<ConfHMtrx> arrH;
  private ChConf[] confArr;
  public ScattTrgtE3() {
    arrH = new ArrayList<ConfHMtrx>();
  }
  public void add(ConfHMtrx h) {
    confArr = null;
    setEngs(null);
    arrH.add(h);
  }
  public void makeReady() {
    int arrSize = 0;
    for (ConfHMtrx h : arrH) {    // count required size
      arrSize += h.getEigVal().size();
    }                                        log.dbg("arrSize=", arrSize);
    // get all channels
    confArr = new ChConf[arrSize];
    int idx = 0;
    for (ConfHMtrx h : arrH) {
      Vec engs = h.getEigVal();              log.dbg("engs=\n", engs);
      for (int i = 0; i < engs.size(); i++) {
        ChConf conf = new ChConf(i, engs.get(i), h);
        confArr[idx++] = conf;
      }
    }
    Arrays.sort(confArr, new Comparator<ChConf>() {  // Sort by energy
      public int compare(ChConf a, ChConf b) {
        return Double.compare(a.eng, b.eng);
      }
    });

    // load energies
    double[] sorted = new double[arrSize];
    for (int i = 0; i < confArr.length; i++) {
      sorted[i] = confArr[i].eng;
    }
    Vec sortedEngs = new Vec(sorted);
    setEngs(sortedEngs);                log.dbg("sorted engs=\n", sortedEngs);
  }
  public Vec getEngs() {
    if (confArr == null) {
      throw new IllegalArgumentException(log.error("call makeReady first!"));
    }
    return super.getEngs();
  }

  public ChConf getChConf(int idx) {
    return confArr[idx];
  }

  public ArrayList<ConfHMtrx> getArrH() {
    return arrH;
  }
  public void removeClosed(double maxScttE, int idxFromCh, int numExtra) {
    Vec engs = getEngs();
    double fromE = engs.get(idxFromCh);
    double maxSysE = maxScttE + fromE;
    int idxLastOp = 0;
    for (; idxLastOp < engs.size(); idxLastOp++) {
      double trgtE = engs.get(idxLastOp);
      if (maxSysE < trgtE)
        break;
    }

    idxLastOp += numExtra;
    int keepSize = idxLastOp + 1;
    if (keepSize >= engs.size())
      return; // nothing to do; keep all

    ChConf[] confArrNew = new ChConf[keepSize];
    double[] sortedNew = new double[keepSize];
    for (int i = 0; i < keepSize; i++) {
      confArrNew[i] = confArr[i];
      sortedNew[i] = engs.get(i);
    }
    confArr = confArrNew;
    setEngs(new Vec(sortedNew));                log.dbg("new sorted engs=\n", sortedNew);
  }
}
