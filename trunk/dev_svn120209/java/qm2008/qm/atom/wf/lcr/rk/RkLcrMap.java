package atom.wf.lcr.rk;
import atom.shell.deepcopy.ShWf;
import atom.wf.lcr.WFQuadrLcr;

import javax.utilx.log.Log;
import java.util.HashMap;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 7/08/12, 3:01 PM
 */
public class RkLcrMap {
public static Log log = Log.getLog(RkLcrMap.class);
private WFQuadrLcr quadr;
protected HashMap<String, Double> mapE2;
//protected HashMap<String, Integer> mapDbg;
private StringBuffer buff;

public RkLcrMap(WFQuadrLcr quadr) {   log.setDbg();
  this.quadr = quadr;
  mapE2 = new HashMap<String, Double>(100); //
//  mapDbg = new HashMap<String, Integer>(100); //
  buff = new StringBuffer();
}
public double calc(ShWf a, ShWf b, ShWf a2, ShWf b2, int k) {//  log.setDbg();
  String key = loadKey(a, b, a2, b2, k);
  Double val = mapE2.get(key);
  if (val == null) {
    val = RkLcr.calc(quadr, a, b, a2, b2, k);
    mapE2.put(key, val);
//    mapDbg.put(key, 1);
  } else {
//    int count = mapDbg.get(key);
//    mapDbg.put(key, count+1);
//    log.dbg("count=" + count +", key="+ key + ", val=" + val);
  }
  return val;
}
private String loadKey(ShWf a, ShWf b, ShWf a2, ShWf b2, int k) {
  buff.setLength(0);
  buff.append(a.getId()).append(",");
  buff.append(b.getId()).append(",");
  buff.append(a2.getId()).append(",");
  buff.append(b2.getId()).append(",");
  buff.append(k);
  return buff.toString();
}
}
