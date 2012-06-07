package atom.energy;
import d1.IConf;
import math.func.FuncVec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/05/12, 4:36 PM
 */
public interface ISysH {
  Energy calcH(IConf fc, IConf fc2);
  int getNumElec();
  FuncVec calcDens(IConf fc, IConf fc2);
  double calcOver(IConf fc, IConf fc2);
}
