package math.mtrx.api;
import math.mtrx.api.ejml.EigenEjml;
import math.mtrx.api.jama.EigenJama;
import math.mtrx.api.jama.MtrxJama;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:40:23
 */
public class EigenSymm
//  extends EigenJama { //
  extends EigenEjml {
 //TODO: Remember to switch Jama/ejml in Mtrx

//  18Jun12: ejml was about 8 mult faster for 1000x1000 matrix

public EigenSymm(Mtrx mtrx) {
  super(mtrx, true);
}
public EigenSymm(Mtrx mtrx, boolean overwrite) {
  super(mtrx, overwrite);
}

public Mtrx getV () {
  return new Mtrx(super.getV());
}
public Mtrx getD () {
  return new Mtrx(super.getD());
}

}
