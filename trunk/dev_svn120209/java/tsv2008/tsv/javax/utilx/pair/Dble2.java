package javax.utilx.pair;
import javax.langx.ToString;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/08/11,10:04 AM
 */
public class Dble2 extends ToString {
public double a;
public double b;
public Dble2(double a, double b) {
  this.a = a;
  this.b = b;
}
public Dble2() {
}
public double getA() {
  return a;
}
public double getB() {
  return b;
}
public void setA(double s) {
  a = s;
}
public void setB(double s) {
  b = s;
}
public String toString() {
  if (isShortStr())
    return "(" + (float)a + ", " + (float)b + ")";
  else
    return "(" + a + ", " + b + ")";
}

}
