package atom.test;
import junit.framework.TestCase;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 16:00:42
 */
public class LCRTestCase extends TestCase {
  public static Log log = Log.getLog(LCRTestCase.class);
  protected double NORM_ERROR = 1e-10;
//   int NUM_STEPS = 880;
//   double FIRST = -4;
//   double STEP = 1./64.;
  int NUM_STEPS = 440;
  double FIRST = -4;
  double LAST = 4;
  double STEP = 1. / 50.;

//   int NUM_STEPS = 440;
//   double FIRST = -4;
//   double STEP = 1./32.;

//   int NUM_STEPS = 220;
//   double FIRST = -4;
//   double STEP = 1./16.;
  protected StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
}
