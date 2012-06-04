package atom.wf.mm;
import atom.energy.Energy;
import atom.energy.ISysH;
import atom.energy.slater.SlaterLcr;
import atom.shell.Conf;
import atom.shell.ShPair;
import atom.shell.Shell;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/05/12, 10:38 AM
 */
public class SysMmE2  implements ISysH {
private final SlaterLcr si;
private final double atomZ;
// locals
private HkMm currHk;
private Conf currCf;
private Conf currCf2;

public SysMmE2(double z, SlaterLcr si) {
  atomZ = z;
  this.si = si;
}
public Energy calcH(Conf fc, Conf fc2) {
  loadCurr(fc, fc2);
  Energy res = currHk.calcH(atomZ);
  return res;
}
private void loadCurr(Conf fc, Conf fc2) {
  if (currCf == fc  &&  currCf2 == fc2  &&  currHk != null) {
    return; // stop recalculating the same configs
  }
  currCf = fc;
  currCf2 = fc2;
  Shell sa = ((ShPair)fc).a;
  Shell sb = ((ShPair)fc).b;
  Shell sa2 = ((ShPair)fc2).a;
  Shell sb2 = ((ShPair)fc2).b;
  WFQuadrLcr w = si.getQaudrLcr();
  int K = 0;
  currHk = new HkMm(w, sa.getWf(), sb.getWf(), sa2.getWf(), sb2.getWf(), K);
}
public int getNumElec() {
  return 0;
}
public FuncVec calcDensity(Conf fc, Conf fc2) {
  return null;
}
public double calcOverlap(Conf fc, Conf fc2) {
  loadCurr(fc, fc2);
  double res = currHk.calcOv();
  return res;
}
}