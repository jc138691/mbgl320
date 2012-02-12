package atom.fano;

import atom.shell.Conf;
import atom.shell.ShInfo;
import atom.shell.Shell;
import atom.shell.deepcopy.ShId;
import math.Calc;
import math.Mathx;

import javax.triplet.Int3;
import javax.utilx.intx.ArrInt4;
import javax.utilx.intx.Int4;
import javax.utilx.log.Log;
import javax.utilx.pair.Int2;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,08/06/2010,1:52:11 PM
 */
// this uses equation from the 2010 e-He paper 
public class Atom2011 {
  public static Log log = Log.getLog(Atom2011.class);

  public static TreeMap<ShId, Int3> loadIdMap(Conf conf, Conf C2) {
    TreeMap<ShId, Int3> res = new TreeMap<ShId, Int3>();
    for (int i = 0; i < conf.size(); i++) {
      Shell sh = conf.getSh(i);
      ShId id = sh.getId();
      int q = sh.getQ();
      Int2 count = res.get(id);
      if (count != null) {
        throw new IllegalArgumentException(log.error("count != null"));
      }
      res.put(id, new Int3(q));
    }
    for (int i = 0; i < C2.size(); i++) {
      Shell sh = C2.getSh(i);
      ShId id = sh.getId();
      int q = sh.getQ();
      Int3 count = res.get(id);
      if (count == null) {
        res.put(id, new Int3(0, q));
      } else {
        if (count.b != 0) {
          throw new IllegalArgumentException(log.error("count.b != 0"));
        }
        count.b = q;
        count.c = Math.min(count.a, q);     // store possible spectator electrons, \bar{N}_\lambda (14)
      }
    }
    return res;
  }

  // is a possible spectator shell

  private static boolean isPossible(ShId specId, Shell sh) {
    if (specId.equals(sh.getId()) && sh.getQ() < 2) {
      // specator electron(s) is in this shell but the shell has only one electron
      return false;
    }
    return true;
  }
  // is a possible spectator shell

  public static boolean isPossible(ShInfo spec, ShInfo act) { // active
    if (spec.id.equals(act.id) && act.q < 2) {
      // specator electron(s) is in this shell but the shell has only one electron
      return false;
    }
    return true;
  }
  // is a possible spectator shell, while selecting sh-rho and SH2-sigma

  private static boolean isPossible(ShId specId, Shell sh, Shell SH2) {
    if (!isPossible(specId, sh) || !isPossible(specId, SH2))
      return false;
    if (sh.getId().equals(SH2.getId()) && sh.getQ() < 2) {
      // rho&sigma electrons are in this shell but the shell has only one electron
      return false;
    }
    if (specId.equals(sh.getId())
      && specId.equals(SH2.getId())
      && sh.getQ() < 3) {
      // specator electron(s) is in this shell but the shell does not have enough electrons
      return false;
    }
    return true;
  }

  public static boolean isPossible(ShInfo spec, ShInfo sh, ShInfo SH2) {
    if (!isPossible(spec, sh) || !isPossible(spec, SH2))
      return false;
    if (sh.id.equals(SH2.id) && sh.q < 2) {
      // rho&sigma electrons are in this shell but the shell has only one electron
      return false;
    }
    if (spec.id.equals(sh.id)
      && spec.id.equals(SH2.id)
      && sh.q < 3) {
      // specator electron(s) is in this shell but the shell does not have enough electrons
      return false;
    }
    return true;
  }

  // scan one spectator shell
  // This is ok for 3-electron systems

  public static ArrInt4 loadOneSpecShell(Conf conf, Conf C2) {
    ArrInt4 res = new ArrInt4();
    TreeMap<ShId, Int3> idMap = Atom2011.loadIdMap(conf, C2);
    // check all possible spectator electrons, \bar{N}_\lambda (14)
    for (Iterator<ShId> it = idMap.keySet().iterator(); it.hasNext();) {
      ShId key = it.next();
      Int3 count = idMap.get(key);
      if (count.c == 0)
        continue; // no spectator electrons

      for (int r = 0; r < conf.size(); r++) { // rho    (14)
        Shell shr = conf.getSh(r);
        if (!isPossible(key, shr))
          continue; // not possible

        for (int s = r; s < conf.size(); s++) { // sigma (14)
          Shell shs = conf.getSh(s);
          if (!isPossible(key, shr, shs))
            continue; // not possible

          for (int R2 = 0; R2 < C2.size(); R2++) { // rho    (14')
            Shell shR2 = conf.getSh(R2);
            if (!isPossible(key, shR2))
              continue; // not possible

            for (int S2 = R2; S2 < C2.size(); S2++) { // sigma (14')
              Shell shS2 = conf.getSh(S2);
              if (!isPossible(key, shR2, shS2))
                continue; // not possible

              res.add(new Int4(r, s, R2, S2));
            }
          }
        }
      }
    }
    return res;
  }

  public static int calcSign(ShInfo r, ShInfo s, int[] qs) {
    int sum = 1 - Mathx.dlt(r.idx, s.idx); 
    for (int ib = r.idx+1; ib <= s.idx; ib++) { // count non empty shells
      sum += qs[ib];
    }
    int res = Calc.prty(sum);
    return res;
  }
  public static int calcSignFano(ShInfo r, ShInfo s, int[] qs) { // 
    int sum = 0;
    for (int ib = r.idx+1; ib <= s.idx; ib++) { // count non empty shells
      int barQ = qs[ib];
      if (ib == r.idx)
       barQ--;
      if (ib == s.idx)
       barQ--;
      if (barQ < 0) {
        throw new IllegalArgumentException(log.error("barQ < 0"));
      }
      sum += barQ;
    }
    return Calc.prty(sum);
  }

  public static double calcNorm(ShInfo r, ShInfo s, int[] qs) {
    double res = qs[r.idx] * (qs[s.idx] - Mathx.dlt(r.idx, s.idx));
    return Math.sqrt(res);
  }

}
