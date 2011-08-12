package javax.utilx;
import java.util.Random;
import java.util.Date;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 13:28:50
 */
public class RandomSeed extends Random
{
  static private RandomSeed instance;
  private long seed = 2;
  static public RandomSeed getInstance()
  {
    if (instance == null)
      instance = new RandomSeed();
    return instance;
  }
  private RandomSeed() {
    seed = new Date().getTime();
    setSeed(seed);
  }

  public long getSeed()
  {
    return seed;
  }
  public double nextDouble(double L, double R) {
    return L + (R - L) * nextDouble();
  }
//  public int nextInt(int L, int R) {
//    return L + nextInt(R-L);
//  }

  public boolean makeRandomEventPercent(int percentError)
  {
    if (nextDouble() < 0.01 * percentError)
      return true;
    return false;
  }
  public boolean makeRandomEvent(double pr)
  {
    if (nextDouble() < pr)
      return true;
    return false;
  }
}
