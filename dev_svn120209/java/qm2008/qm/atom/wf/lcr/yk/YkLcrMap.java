package atom.wf.lcr.yk;
import atom.shell.deepcopy.ShWf;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.lcr.rk.RkLcr;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;
import java.util.HashMap;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 9/08/12, 11:46 AM
 */
public class YkLcrMap {
public static Log log = Log.getLog(YkLcrMap.class);
private WFQuadrLcr quadr;
protected HashMap<String, FuncVec> mapE2;
//protected HashMap<String, Integer> mapDbg;
private StringBuffer buff;

public YkLcrMap(WFQuadrLcr quadr) {   log.setDbg();
  this.quadr = quadr;
  mapE2 = new HashMap<String, FuncVec>(100); //
//  mapDbg = new HashMap<String, Integer>(100); //
  buff = new StringBuffer();
}
public FuncVec calcYk(ShWf a, ShWf b, int k) {  log.setDbg();
  String key = loadKey(a, b, k);
  FuncVec val = mapE2.get(key);
  if (val == null) {
    YkLcr yk = new YkLcr(quadr, a, b, k);
    val = yk.calcYk();
    mapE2.put(key, val);
//    mapDbg.put(key, 1);
  } else {
//    int count = mapDbg.get(key);
//    mapDbg.put(key, count+1);
//    if (count % 10 == 0) {
//      log.dbg("count=" + count +", key="+ key);
//    }
  }
  return val;
}
private String loadKey(ShWf a, ShWf b, int k) {
  buff.setLength(0);
  buff.append(a.getId()).append(",");
  buff.append(b.getId()).append(",");
  buff.append(k);
  return buff.toString();
}
}
