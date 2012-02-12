package dna.table.view.opt;

import javax.awtx.ColorMinMax;
import java.awt.Color;

/**
 * jc138691, 03/03/2009, 4:08:55 PM
 */
public class BaseQltyOpt {
  private static int count = 0;
  public final static int FORMAT_PLAIN = count++;
  public final static int FORMAT_GRAY_FONT = count++;
  public final static int FORMAT_COLOR_FONT = count++;
  public final static int FORMAT_COLOR_BGRND = count++;
  private int viewFormat;

  private ColorMinMax gray;      // storing int in the rgb-int
  private ColorMinMax colorFont;
  private ColorMinMax colorBgrnd;

  public BaseQltyOpt() {
    init();
    loadDefaults();
  }

  public void loadDefaults() {
    gray.setMinRgb(255); // white
    gray.setMaxRgb(0);   // black
    colorFont.setMinRgb(new Color(0, 0, 200).getRGB());
    colorFont.setMaxRgb(new Color(0, 0, 0).getRGB());
    colorBgrnd.setMinRgb(new Color(0, 0, 255).getRGB());
    colorBgrnd.setMaxRgb(new Color(255, 255, 255).getRGB());
    viewFormat = FORMAT_COLOR_BGRND;
  }

  private void init() {
    gray = new ColorMinMax();
    colorFont = new ColorMinMax();
    colorBgrnd = new ColorMinMax();
  }

  public int getViewFormat() {
    return viewFormat;
  }

  public void setViewFormat(int viewFormat) {
    this.viewFormat = viewFormat;
  }

  public ColorMinMax getColorFont() {
    return colorFont;
  }

  public void setColorFont(ColorMinMax colorFont) {
    this.colorFont = colorFont;
  }

  public ColorMinMax getColorBgrnd() {
    return colorBgrnd;
  }

  public void setColorBgrnd(ColorMinMax colorBgrnd) {
    this.colorBgrnd = colorBgrnd;
  }

  public ColorMinMax getGray() {
    return gray;
  }

  public void setGray(ColorMinMax gray) {
    this.gray = gray;
  }
}
