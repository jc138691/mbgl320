package atom.energy;
import atom.shell.Conf;
import math.func.FuncVec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/05/12, 4:36 PM
 */
public interface ISysH {
  Energy calcH(Conf fc, Conf fc2);
  int getNumElec();
  FuncVec calcDensity(Conf fc, Conf fc2);
  double calcOverlap(Conf fc, Conf fc2);
}
