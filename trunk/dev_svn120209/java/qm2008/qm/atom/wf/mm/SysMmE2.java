package atom.wf.mm;
import atom.energy.Energy;
import atom.energy.ISysH;
import atom.energy.slater.SlaterLcr;
import atom.shell.Conf;
import atom.shell.Shell;
import math.func.FuncVec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/05/12, 10:38 AM
 */
public class SysMmE2  implements ISysH {
private AtomMm atomMm;
public SysMmE2(double z, SlaterLcr si) {
  super(z, si);
  atomMm = new AtomMm(si);
}
public double calcRk(Shell a, Shell b, Shell a2, Shell b2, int kk) {
  return atomMm.calcRk(a, b, a2, b2, kk);
}
public Energy calcH(Conf fc, Conf fc2) {


}
public int getNumElec() {
  return 0;
}
public FuncVec calcDensity(Conf fc, Conf fc2) {
  return null;
}
}
