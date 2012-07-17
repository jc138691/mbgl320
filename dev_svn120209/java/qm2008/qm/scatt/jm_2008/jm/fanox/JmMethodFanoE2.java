package scatt.jm_2008.jm.fanox;

import atom.energy.ConfOvMtrx;
import atom.energy.part_wave.PotHMtrx;
import math.mtrx.api.Mtrx;
import math.mtrx.MtrxDbgView;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.e2.JmMthdBasisHyE2;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 02/06/2010, 9:11:39 AM
 *
 * TODO: This is working correctly, see HySWaveViaOverlap_OLD
 */
public class JmMethodFanoE2 extends JmMthdBasisHyE2 {
  public static Log log = Log.getLog(JmMethodFanoE2.class);
  private ConfOvMtrx chiOv;
  private PotHMtrx targetH;

  public JmMethodFanoE2(JmCalcOptE1 potOpt) {
    super(potOpt);
  }

  protected Mtrx calcC() {
//    double[][] sC = sysConfH.getEigVec().getArr2D();   log.dbg("C_ij=", new MtrxDbgView(sysConfH.getEigVec()));
    Mtrx sC = sysConfH.getEigVec();   log.dbg("C_ij=", new MtrxDbgView(sysConfH.getEigVec()));
    return sC;
  }

  // THIS works only for two electrons, see  JmMethodJmBasisE3 for a general case of any electrons
  @Override protected Mtrx calcX() {
    // [for JmMethodFanoX] Next line was MOVED to calcC();
//    double[][] sC = sysConfH.getEigVec().getArr2D();   log.dbg("C_ij=", new MtrxDbgView(sysConfH.getEigVec()));
//    double[][] sC = calcC();   log.dbg("C_ij=", new MtrxDbgView(sysConfH.getEigVec()));
    Mtrx sC = calcC();   log.dbg("C_ij=", new MtrxDbgView(sysConfH.getEigVec()));

//    double[][] chiOv = this.chiOv.getArr2D();
//    double[][] tC = targetH.getEigVec().getArr2D();  log.dbg("D_ij=", new MtrxDbgView(targetH.getEigVec()));
    Mtrx tC = targetH.getEigVec();  log.dbg("D_ij=", new MtrxDbgView(targetH.getEigVec()));
    int sN = getSysBasisSize();
    int cN = getChNum();
    Mtrx res = new Mtrx(cN, sN);
    for (int g = 0; g < cN; g++) {  // target channel/state
      for (int i = 0; i < sN; i++) {  // system states
        double sum = 0;
        for (int t2 = 0; t2 < cN; t2++) {
          for (int j = 0; j < sN; j++) {
//            double c = sC[j][i] * tC[t2][g]; //log.dbg("c = ", c); // note order [j][i]
            double c = sC.get(j, i) * tC.get(t2, g); //log.dbg("c = ", c); // note order [j][i]
//            double d = chiOv[j][t2];         //log.dbg("d = ", d);
            double d = chiOv.get(j, t2);         //log.dbg("d = ", d);
            sum += (c * d);                  //log.dbg("sum = ", sum);
          }
        }
        res.set(g, i, sum);                           //log.dbg("X[" + g + ", " + i + "]=", sum);
      }
    }
    return res;
  }

  public void setChiOv(ConfOvMtrx chiOv) {
    this.chiOv = chiOv;
  }

  public void setTargetH(PotHMtrx targetH) {
    this.targetH = targetH;
  }
}

