package math.func;
import java.util.ArrayList;
import static java.lang.Math.exp;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 10:13:41
 */
public class FactLn {
  private static ArrayList<Double> logs = new ArrayList<Double>();
  private static int STEP_FORWARD = 100; // to avoid going up by one
  private static FactLn instance;

  public static FactLn getInstance() {
    if (instance == null) {
      instance = new FactLn(100);
    }
    return instance;
  }
  private FactLn(int last) {
    loadLogs(last);
  }
  public double calc(int n) {
    if (n >= logs.size()) {
      loadLogs(n + STEP_FORWARD);
    }
    return logs.get(n);
  }
  public double calcFactDiv(int top, int bot) {
    return exp(calc(top) - calc(bot));
  }
  protected void loadLogs(int last) {
    double logF = 0.;
    int i = 1;
    if (logs.size() == 0) {// factorial 0!=1, 1!=1
      logs.add(logF);
    } else {
      logF = logs.get(logs.size() - 1);
      i = logs.size();
    }
    for (; i <= last; i++) {
      logF += Math.log(i);
      logs.add(logF);
    }
  }
  public static FactLn makeInstance(int size) {
    if (instance == null)
      instance = new FactLn(size);
    return instance;
  }
}