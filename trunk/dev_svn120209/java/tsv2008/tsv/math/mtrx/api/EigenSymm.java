package math.mtrx.api;
import math.mtrx.api.ejml.EigenSymmEjml;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:40:23
 */
public class EigenSymm
//  extends EigenSymmJama { //
  extends EigenSymmEjml {

//  18Jun12: ejml was about 8 times faster for 1000x1000 matrix

public EigenSymm(Mtrx mtrx) {
  super(mtrx, true);
}
public EigenSymm(Mtrx mtrx, boolean overwrite) {
  super(mtrx, overwrite);
}

}
