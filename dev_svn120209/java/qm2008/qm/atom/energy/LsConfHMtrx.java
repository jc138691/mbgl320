package atom.energy;
import atom.shell.AConfs;
import atom.shell.LsConfs;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:24:45
*/
public class LsConfHMtrx extends IConfHMtrx {
public LsConfHMtrx(AConfs basis, final ISysH atom) {
  super(basis, atom);
}
public LsConfs getConfs() {
  return (LsConfs)super.getConfs();
}
}