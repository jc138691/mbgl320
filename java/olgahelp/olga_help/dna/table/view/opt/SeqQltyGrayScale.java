package dna.table.view.opt;

import calc.interpol.InterpLinear;

import java.awt.*;

import javax.awtx.ColorMinMax;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/03/2009, 11:55:16 AM
 */
public class SeqQltyGrayScale {
  private InterpLinear alg;
  public SeqQltyGrayScale(int minQ, int maxQ, ColorMinMax opt) {
    alg = new InterpLinear(minQ, maxQ, opt.getMinRgb(), opt.getMaxRgb());
  }
//  public SeqQltyGrayScale(SnpStation project) {
//    BaseQltyOpt opt = project.getBaseQltyOpt();
//    ColorMinMax minMax = opt.getGray();
//    DnaTableInfo info = project.getDnaTableInfo();
//    alg = new InterpLinear(info.getMinQlty(), info.getMaxQlty()
//      , minMax.getMinRgb(), minMax.getMaxRgb());
//  }
  public Color makeColor(int qlty) {
    int color = (int)alg.calc(qlty);
    return new Color(color, color, color);
  }
}
