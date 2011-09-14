package atom.smodel;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,12/10/2010,10:17:17 AM
 */
public class HeSWaveAtom {
  // from Bartlett & Stelbovics 2010 PhysRev A81, 022715
  public static final double E_1s1s_1S = -2.87895;
  public static final double E_1s2s_3S = -2.17426;
  public static final double E_1s2s_1S = -2.14419;
  public static final double E_1s3s_3S = -2.06848;
  public static final double E_1s3s_1S = -2.06079;

  // http://pra.aps.org/abstract/PRA/v50/i5/p3793_1
  //Phys. Rev. A 50, 3793–3808 (1994)
  //One- and two-electron excitations of helium in the s-wave model
  //M. Draeger, G. Handke, W. Ihra, and H. Friedrich
  public static double[] E_1S = {
    -2.87902769,
    -2.14418810,
    -2.06078824,
    -2.03339181,
    -2.02107923,
    -2.01450740,
    -2.01059097,
    -2.00806972,
    -2.00634935,
    -2.00511985
  };
  public static double[] E_3S = {
    -2.17426480,
    -2.06849012,
    -2.03643858,
    -2.02258364,
    -2.01535790,
    -2.01111794,
    -2.00841911,
    -2.00659503,
    -2.00530238
};

  public static double[] E_SORTED = {
    -2.87902769,
    -2.17426480,
    -2.14418810,
    -2.06849012,
    -2.06078824,
    -2.03643858,
    -2.03339181,
    -2.02258364,
    -2.02107923,
    -2.01535790,
    -2.01450740,
    -2.01111794,
    -2.01059097,
    -2.00841911,
    -2.00806972,
    -2.00659503,
    -2.00634935,
    -2.00530238,
    -2.00511985
  };
}
