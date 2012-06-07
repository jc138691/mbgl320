package atom.energy;
import atom.shell.IConfs;
import atom.shell.LsConfs;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.mtrx.jamax.EigenSymm;
import math.vec.Vec;
import math.vec.VecSort;

import javax.utilx.log.Log;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:24:45
*/
public class LsConfHMtrx extends IConfHMtrx {
public LsConfHMtrx(IConfs basis, final ISysH atom) {
  super(basis, atom);
}
public LsConfs getConfs() {
  return (LsConfs)super.getConfs();
}
}