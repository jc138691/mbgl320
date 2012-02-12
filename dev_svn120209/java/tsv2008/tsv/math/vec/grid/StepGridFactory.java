package math.vec.grid;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 10:33:14
 */
public class StepGridFactory {

  public static final StepGrid makeStepGrid(Func func, double first, double last, int size) {
    return new StepGrid(func.calc(first), func.calc(last), size);
  }
//  public static final StepGrid makeStepGridFromLog(double first, double last, int size) {
//    return new StepGrid(Math.log(first), Math.log(last), size);
//  }
//  public static final StepGrid makeStepGridFromExp(double first, double last, int size) {
//    return new StepGrid(Math.exp(first), Math.exp(last), size);
//  }
}