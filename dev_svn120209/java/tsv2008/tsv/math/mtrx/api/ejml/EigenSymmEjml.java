package math.mtrx.api.ejml;
import math.mtrx.api.Mtrx;
import math.mtrx.MtrxFactory;
import math.vec.VecDbgView;
import org.ejml.alg.dense.decomposition.eig.SymmetricQRAlgorithmDecomposition;
import org.ejml.data.Complex64F;
import org.ejml.data.DenseMatrix64F;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 18/06/12, 9:10 AM
 */
public class EigenSymmEjml extends SymmetricQRAlgorithmDecomposition {
public static Log log = Log.getLog(EigenSymmEjml.class);
private Mtrx mtrx;
private DenseMatrix64F mWork;
private boolean decompOk;
private double[] vals;
private Mtrx mVec;
private Mtrx mD;

public EigenSymmEjml(Mtrx mtrx) {
  super(true);                                   //log.setDbg();
  this.mtrx = mtrx;                              //log.dbg("mtrx=\n", new MtrxDbgView(mtrx));
  mWork = new DenseMatrix64F(mtrx.getArray());   //log.dbg("mWork=\n", mWork);
  decompOk = decompose(mWork);
}
public EigenSymmEjml(Mtrx mtrx, boolean overwrite) {
  super(true);                                   //log.setDbg();
  this.mtrx = mtrx;                              //log.dbg("mtrx=\n", new MtrxDbgView(mtrx));
  mWork = new DenseMatrix64F(mtrx.getArray());   //log.dbg("mWork=\n", mWork);
  decompOk = decompose(mWork);
}
public double[] getRealEVals () {
  if (vals == null) {
    int n = getNumberOfEigenvalues();
    vals = new double[n];
    for (int i = 0; i < vals.length; i++) {
      Complex64F cv = getEigenvalue(i);
      if (!cv.isReal()) {
        throw new IllegalArgumentException(log.error("!cv.isReal()"));
      }
      vals[i] = cv.getReal();    log.dbg("vals=", new VecDbgView(vals));
    }

    // SORT ONLY ONCE!!!
    mVec = getV();             //log.dbg("mVec=\n", new MtrxDbgView(mVec));
    if (mVec != null  &&  vals != null) {// SORT ONLY ONCE!!!
      MtrxFactory.sort(vals, mVec.getArray());
    }
  }
  return vals;
}
public Mtrx getV () {
  if (mVec == null) {
    int nc = getNumberOfEigenvalues(); // number of columns
    double[][] arr = new double[nc][nc];
    for (int c = 0; c < nc; c++) {  // store by columns
      DenseMatrix64F vec = getEigenVector(c);
      for (int r = 0; r < nc; r++) {
        arr[r][c] = vec.get(r);
      }
    }
    mVec = new Mtrx(arr);        //log.dbg("mVec=\n", new MtrxDbgView(mVec));

    // SORT ONLY ONCE!!!
    vals = getRealEVals();       log.dbg("vals=", new VecDbgView(vals));
    if (mVec != null  &&  vals != null) { // SORT ONLY ONCE!!!
      MtrxFactory.sort(vals, mVec.getArray());
    }
  }
  return mVec;
}

// see EigenSymmJama
public Mtrx getD () {
  if (mD == null) {
    vals = getRealEVals();
    int n = vals.length;
    mD = new Mtrx(n, n);
    for (int i = 0; i < vals.length; i++) {
      mD.set(i, i, vals[i]);
    }
  }
  return mD;
}
}
