package scatt.jm_2008.jm.theory;
import flanagan.complex.Cmplx;

import javax.utilx.log.Log;
import static java.lang.Math.*;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 2/09/2008, Time: 11:58:19
 */
public class JmTheory {
  public static Log log = Log.getLog(JmTheory.class);
  public static Cmplx calcSCnL0(int n, double k, double lambda) { log.dbg("calcSCnL0(n=", n); log.dbg("k=", k);
    double E = k * k / 2.;
    return calcSCnL0byE(n, E, lambda);
  }
  public static double calcJnnL0byE(int n, double E, double lambda) { 
    log.dbg("calcJnnL0byE(n=", n); log.dbg("E=k^2/2=", E); log.dbg("lambda=", lambda);
    double x = lambda * lambda / 8.;           log.dbg("lambda^2/8=", x);
    double res = n * (n+1) * (E + x) / lambda; log.dbg("res=", res);
    return res;
  }
  public static Cmplx calcSCnL0byE(int n, double E, double lambda) { log.dbg("calcL0byE(n=", n); log.dbg("E=k^2/2=", E); log.dbg("lambda=", lambda);
    double x = lambda * lambda / 8.;          log.dbg("lambda^2/8=", x);
    double th = (E - x) / (E + x);            log.dbg("cos(theta)=", th);
    th = Math.acos(th);                       log.dbg("theta=", th);
    th *= (n+1);                              log.dbg("(n+1)*theta=", th);
    Cmplx res = new Cmplx(-cos(th) / (n+1), sin(th) / (n+1)); log.dbg("res=", res);
    return res;
  }

}