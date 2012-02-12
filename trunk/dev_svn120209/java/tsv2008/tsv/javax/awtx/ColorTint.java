package javax.awtx;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 07/05/2009, 4:56:03 PM
 */
public class ColorTint {
  public static Color tint(Color target, Color tint) {
    int r = tint.getRed();
    int g = tint.getGreen();
    int b = tint.getBlue();
//    int sum = r + g + b;
//    if (sum == 0) {
//      sum = 1;
//    }
    r = mix(r, target.getRed());
    g = mix(g, target.getGreen());
    b = mix(b, target.getBlue());
//    int newSum = r + g + b;
//    if (newSum == 0) {
//      newSum = 1;
//    }
//    r = r * sum / newSum;
//    g = g * sum / newSum;
//    b = b * sum / newSum;
    return new Color(r, g, b);
  }

  private static int mix(int c, int tint) {
    return (c + tint) / 2;
  }
}
