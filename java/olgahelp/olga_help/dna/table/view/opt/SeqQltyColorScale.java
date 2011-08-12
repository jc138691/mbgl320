package dna.table.view.opt;

import calc.interpol.InterpLinear;

import javax.awtx.ColorMinMax;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 13/03/2009, 1:19:25 PM
 */
public class SeqQltyColorScale {
  private InterpLinear red;
  private InterpLinear green;
  private InterpLinear blue;
  public SeqQltyColorScale(int minQ, int maxQ, ColorMinMax opt) {
    Color minC = new Color(opt.getMinRgb());
    Color maxC = new Color(opt.getMaxRgb());
    red = new InterpLinear(minQ, maxQ, minC.getRed(), maxC.getRed());
    green = new InterpLinear(minQ, maxQ, minC.getGreen(), maxC.getGreen());
    blue = new InterpLinear(minQ, maxQ, minC.getBlue(), maxC.getBlue());
  }
  public Color makeColor(int qlty) {
    int redC = (int)red.calc(qlty);
    int greenC = (int)green.calc(qlty);
    int blueC = (int)blue.calc(qlty);
    return new Color(redC, greenC, blueC);
  }
//  public Color makeMinColor() {
//    int redC = (int)red.getY();
//    int greenC = (int)green.getY();
//    int blueC = (int)blue.getY();
//    return new Color(redC, greenC, blueC);
//  }
}
