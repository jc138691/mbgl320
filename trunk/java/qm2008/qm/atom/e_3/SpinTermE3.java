package atom.e_3;

import atom.angular.Wign3j;
import atom.fano.FanoTermE2;
import atom.shell.DiEx;

import javax.utilx.log.Log;

import static math.Calc.prty;
import static math.Mathx.dlt;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,09/11/2010,12:04:56 PM
 */
public class SpinTermE3 {
  public static Log log = Log.getLog(SpinTermE3.class);
  public static final int ERR = 0;
  public static final int A1 = 1;
  public static final int A2 = 2;
  public static final int B1 = 3;
  public static final int B2 = 4;
  public static final int B3 = 5;

  public static final int A1_A1 = 10 * 1 + 1;
  public static final int A1_A2 = 10 * 1 + 2;
  public static final int A1_B1 = 10 * 1 + 3;
  public static final int A1_B2 = 10 * 1 + 4;
  public static final int A1_B3 = 10 * 1 + 5;

  public static final int A2_A1 = 10 * 2 + 1;
  public static final int A2_A2 = 10 * 2 + 2;
  public static final int A2_B1 = 10 * 2 + 3;
  public static final int A2_B2 = 10 * 2 + 4;
  public static final int A2_B3 = 10 * 2 + 5;

  public static final int B1_A1 = 10 * 3 + 1;
  public static final int B1_A2 = 10 * 3 + 2;
  public static final int B1_B1 = 10 * 3 + 3;
  public static final int B1_B2 = 10 * 3 + 4;
  public static final int B1_B3 = 10 * 3 + 5;

  public static final int B2_A1 = 10 * 4 + 1;
  public static final int B2_A2 = 10 * 4 + 2;
  public static final int B2_B1 = 10 * 4 + 3;
  public static final int B2_B2 = 10 * 4 + 4;
  public static final int B2_B3 = 10 * 4 + 5;

  public static final int B3_A1 = 10 * 5 + 1;
  public static final int B3_A2 = 10 * 5 + 2;
  public static final int B3_B1 = 10 * 5 + 3;
  public static final int B3_B2 = 10 * 5 + 4;
  public static final int B3_B3 = 10 * 5 + 5;


  public static DiEx calc(FanoTermE3 t, FanoTermE3 t2) {
    double d = 0;

    int totS = t.getTotS2();

    int b = t.get2B();    // NOTE!!! these are 2*S
    int r = t.get2R();
    int s = t.get2S();

    int b2 = t2.get2B();
    int r2 = t2.get2R();
    int s2 = t2.get2S();

    int type = getType(t);        log.dbg("type=", type);
    int type2 = getType(t2);      log.dbg("type2=", type2);
    int switchCase = type * 10 + type2;  log.dbg("type * 10 + type2=", switchCase);
    switch (switchCase) {
      case A1_A1:    log.dbg("case A1_A1");     //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        d = dlt(r, r2);
        break;
      case A1_A2:    log.dbg("case A1_A2");     //26Nov, 13Dec
        d = dlt(r, b2) * prty(1 - t2.b());
        break;
      case A1_B1:    log.dbg("case A1_B1");     //26Nov 
        d = prty(t2.s()) * t.hatSr() * t2.hatSs()
          * Wign3j.wign6j2(1, 1, r, 1, totS, s2);
        break;
      case A1_B2:    log.dbg("case A1_B2");     //26Nov, 13Dec, 15Dec(passed testAtomLiSlaterConfArr)
        d = 0.5 * dlt(1, totS) * prty(1 + t.r()) * t.hatSr();
        break;
      case A1_B3:    log.dbg("case A1_B3");      //26Nov
        d = 0.5 * dlt(1, totS) * t.hatSr();
        break;

      case A2_A1:     log.dbg("case A2_A1");     //26Nov, 13Dec
        d = dlt(r2, b) * prty(1 - t.b());
        break;
      case A2_A2:     log.dbg("case A2_A2");    //26Nov, 15Dec
        d = dlt(b, b2);
        break;
      case A2_B1:     log.dbg("case A2_B1");     //26Nov, 15Dec
        d = prty(1 - t.b() + t2.s()) * t.hatSb() * t2.hatSs()
          * Wign3j.wign6j2(1, 1, b, 1, totS, s2);
        break;
      case A2_B2:     log.dbg("case A2_B2");     //26Nov, 13Dec
        d = 0.5 * dlt(1, totS) * t.hatSb();
        break;
      case A2_B3:     log.dbg("case A2_B3");      //26Nov, 13Dec
        d = -0.5 * dlt(1, totS) * prty(t.b()) * t.hatSb();
        break;

      case B1_A1:     log.dbg("case B1_A1");       //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        d = prty(t.s()) * t2.hatSr() * t.hatSs()
          * Wign3j.wign6j2(1, 1, r2, 1, totS, s);
        break;
      case B1_A2:     log.dbg("case B1_A2");        //26Nov, 15Dec
        d = prty(1 - t2.b() + t.s()) * t2.hatSb() * t.hatSs()
          * Wign3j.wign6j2(1, 1, s, 1, totS, b2);
        break;
      case B1_B1:     log.dbg("case B1_B1");         //26Nov, 13Dec, 15Dec(passed testAtomLiSlaterConfArr)
        d = dlt(t.s(), t2.s());
        break;
      case B1_B2:     log.dbg("case B1_B2");        //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        d = dlt(1, totS) * dlt(0, t.s());
        break;
      case B1_B3:     log.dbg("case B1_B3");        //26Nov, 13Dec, 15Dec(passed testAtomLiSlaterConfArr)
        d = 0.5 * dlt(1, totS) * prty(t.s()) * t.hatSs();
        break;

      case B2_A1:    log.dbg("case B2_A1");      //26Nov, 15Dec
        d = 0.5 * dlt(1, totS) * prty(1 + t2.r()) * t2.hatSr();
        break;
      case B2_A2:    log.dbg("case B2_A2");      //26Nov, 13Dec
        d = 0.5 * dlt(1, totS) * t2.hatSb();
        break;
      case B2_B1:    log.dbg("case B2_B1");        //26Nov
        d = dlt(1, totS) * dlt(0, t2.s());
        break;
      case B2_B2:    log.dbg("case B2_B2");   //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        d = dlt(1, totS);
        break;
      case B2_B3:    log.dbg("case B2_B3");   //26Nov
        d = 0.5 * dlt(1, totS);
        break;

      case B3_A1:    log.dbg("case B3_A1");    //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        d = 0.5 * dlt(1, totS) * t2.hatSr();
        break;
      case B3_A2:    log.dbg("case B3_A2");     //26Nov, 13Dec
        d = -0.5 * dlt(1, totS) * prty(t2.b()) * t2.hatSb();
        break;
      case B3_B1:    log.dbg("case B3_B1");      //26Nov, 13Dec
        d = 0.5 * dlt(1, totS) * prty(t2.s()) * t2.hatSs();
        break;
      case B3_B2:    log.dbg("case B3_B2");      //26Nov , 15Dec(passed testAtomLiSlaterConfArr)
        d = 0.5 * dlt(1, totS);
        break;
      case B3_B3:    log.dbg("case B3_B3");      //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        d = dlt(1, totS);
        break;

      default:
        throw new IllegalArgumentException(log.error("this type is not valid or not done yet."));
    }
    if (!t.hasExc(t2)) {
      return new DiEx(d);
    }
    double e = 0;
    switch (switchCase) {
      case A1_A1:                   //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        e = Wign3j.wign6j2(1, 1, r, 1, totS, r2);
        e *= (t.hatSr() * t2.hatSr());
        e *= prty(1 - t.r() + t2.r());
        break;
      case A1_A2:                  //26Nov, 13Dec
        e = Wign3j.wign6j2(1, 1, b2, 1, totS, r);
        e *= (t.hatSr() * t2.hatSb());
        e *= prty(t.r());
        break;
      case A1_B1:                    //26Nov
        e = d * prty(1 - t2.s());
        break;
      case A1_B2:
        break;
      case A1_B3:                    //26Nov
        e = dlt(1, totS) * dlt(0, t.r());
        break;

      case A2_A1:              //26Nov, 13Dec
        e = Wign3j.wign6j2(1, 1, b, 1, totS, r2);
        e *= (t2.hatSr() * t.hatSb());
        e *= prty(t2.r());
        break;
      case A2_A2:               //26Nov, 15Dec
        e = Wign3j.wign6j2(1, 1, b, 1, totS, b2);
        e *= (t.hatSb() * t2.hatSb());
        e *= -1;
        break;
      case A2_B1:                 //26Nov, 15Dec
        e = d * prty(1 - t2.s());
        break;
      case A2_B2:                 //26Nov
        break;
      case A2_B3:                 //26Nov, 15Dec
        e = -dlt(1, totS) * dlt(0, t.b());
        break;

      case B1_A1:
        e = d * prty(1 - t.s());  //26Nov
        break;
      case B1_A2:                 //26Nov, 15Dec
        e = d * prty(1 - t.s());
        break;
      case B1_B1:                 //26Nov, 13Dec
        e = d * prty(1 - t2.s());
        break;
      case B1_B2:                 //26Nov
        break;
      case B1_B3:                 //26Nov, 13Dec(found bug, had x -1)
        e = d * prty(1 - t.s());
        break;

      case B2_A1:                   //26Nov
        break;
      case B2_A2:                   //26Nov
        break;
      case B2_B1:                    //26Nov
        break;
      case B2_B2:                    //26Nov
        break;                       
      case B2_B3:                    //26Nov
        break;

      case B3_A1:           //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        e = dlt(1, totS) * dlt(0, t2.r());
        break;
      case B3_A2:           //26Nov, 15Dec
        e = -dlt(1, totS) * dlt(0, t2.b());
        break;
      case B3_B1:             //26Nov, 13Dec(found bug, had x -1)
        e = d * prty(1 - t2.s());
        break;
      case B3_B2:         //26Nov
        break;
      case B3_B3:        //26Nov, 15Dec(passed testAtomLiSlaterConfArr)
        e = 0.5 * dlt(1, totS);
        break;

      default:
        throw new IllegalArgumentException(log.error("this type is not valid or not done yet."));
    }
    return new DiEx(d, e);
  }

  public static int getType(FanoTermE3 t) {
    if (t.b.idx <= t.r.idx && t.r.idx < t.s.idx) {
      log.dbg("lambda rho (S_rho) sigma,  lambda < rho < sigma"); // A1
      return A1;
    }
    if (t.r.idx < t.b.idx && t.b.idx < t.s.idx) {
      log.dbg("rho lambda (S_lambda) sigma,  rho <= lambda < sigma");// A2
      return A2;
    }
    if (t.r.idx <= t.s.idx && t.s.idx < t.b.idx) { // B1
      log.dbg("rho sigma (S_sigma) lambda,  rho <= sigma < lambda");
      return B1;
    }
    if (t.b.idx < t.r.idx && t.r.idx == t.s.idx) { // B2
      log.dbg("lambda, rho sigma (0),  lambda < rho = sigma");
      return B2;
    }
    if (t.r.idx < t.s.idx && t.b.idx == t.s.idx) { // B3
      log.dbg("rho, sigma lambda (0) ,  rho < sigma = lambda");
      return B3;
    }
    throw new IllegalArgumentException(log.error("this type is not valid or not done yet. t=" + t));
//    return ERR;
  }
}
