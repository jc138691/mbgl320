package math.func.arr;
import math.func.FuncVec;
import math.vec.Vec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 13:35:13
 */
public interface IFuncArr {
public Vec getX();
public FuncVec getFunc(int i);
public void setFunc(int i, FuncVec fv);
public int size();
}
