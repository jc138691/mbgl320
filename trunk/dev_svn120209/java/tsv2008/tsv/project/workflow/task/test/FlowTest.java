package project.workflow.task.test;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import javax.utilx.log.Log;

/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 3/10/2008, Time: 15:04:09
*/
public class FlowTest extends TestCase { //TestSuite suite= new TestSuite(MathTest.class);
protected static Log log;
protected static final double MAX_WF_DIFF_ERR_E7 = 1e-7;
protected static final double MAX_WF_DIFF_ERR_E8 = 1e-8;

protected static final double MAX_INTGRL_ERR_E10 = 1e-10;
protected static final double MAX_INTGRL_ERR_E11 = 1e-11;
protected static final double MAX_INTGRL_ERR_E12 = 1e-12;
protected static final double MAX_INTGRL_ERR_E13 = 1e-13;

// THIS is bad!!! a static is needed since FlowTest() is called
private static double maxErr = 1.e-8;
private static boolean lockMaxErr = false;    // this is not good!!!

//  private double maxErr = 1.e-8;
protected Class theClass;
private boolean showDbg = true;

public FlowTest() {
}

public FlowTest(Class theClass) {
  super();
  this.theClass = theClass;
}

public static double getMaxErr() {
  return maxErr;
}

public static void setMaxErr(double d) {
  if (lockMaxErr)
    throw new RuntimeException("lockMaxErr==true");
  maxErr = d;
}

public static void setLog(Log dbgLog) {
  log = dbgLog;
}

public void assertEquals(double expected, double actual) {
  assertEquals(null, expected, actual, false);
}

public void assertEquals(String help, double expected, double actual, boolean showDbg2) {
  String mssg = format(help, (float) expected, (float) actual);
  mssg += ", err=" + (float) (expected - actual);
  try {
    assertEquals(help, expected, actual, maxErr);
  } catch (AssertionFailedError e) {
    log.info(mssg);
    throw e;
  }
  if (showDbg2) {
    log.dbg(mssg);
  }
}

public void assertEquals(String help, double expected, double actual) {
  assertEquals(help, expected, actual, showDbg);
}

public void assertEqualsRel(String help, double expected, double actual) {
  assertEqualsRel(help, expected, actual, showDbg);
}

public void assertFloorRel(String help, double expected, double actual, double maxRelErr) {
  String mssg = format(help, (float) expected, (float) actual);
  mssg += " relErr=" + (float) ((expected - actual) / expected);
  mssg += " maxRelErr=" + (float) maxRelErr;
  assertFloor(mssg + "; assertFloor", expected, actual, Math.abs(maxRelErr * expected));
}

public void assertCeilRel(String help, double expected, double actual, double maxRelErr) {
  String mssg = format(help, (float) expected, (float) actual);
  mssg += " relErr=" + (float) ((expected - actual) / expected);
  mssg += " maxRelErr=" + (float) maxRelErr;
  assertCeil(mssg + "; assertCeiling", expected, actual, Math.abs(maxRelErr * expected));
}

public void assertFloor(String mssg, double floor, double actual, double delta) {
  if (Double.compare(floor, actual) == 0)
    return;
  if (floor > actual
    || !(Math.abs(floor - actual) <= delta))
    failNotEquals(mssg, new Double(floor), new Double(actual));
}

// Ceiling

public void assertCeil(String mssg, double ceil, double actual, double delta) {
  if (Double.compare(ceil, actual) == 0)
    return;
  if (ceil  < actual
    || !(Math.abs(ceil - actual) <= delta))
    failNotEquals(mssg, new Double(ceil), new Double(actual));
}

public void assertEqualsRel(String help, double expected, double actual, boolean showDbg2) {
  String mssg = format(help, (float) expected, (float) actual);
//    mssg += " err=" + (float) (expected - actual);
  mssg += " relErr=" + (float) ((expected - actual) / expected);
  mssg += " maxRelErr=" + (float) maxErr;
//    try {
  assertEquals(mssg + "; JUnit", expected, actual, Math.abs(maxErr * expected));
//      assertEquals(help, expected, actual, Math.abs(maxErr * expected));
//    } catch (AssertionFailedError e) {
//      throw e;
////      throw new AssertionFailedError(mssg);
//    }
  if (showDbg2) {
    log.dbg(mssg);
  }
}

public boolean ok() {
//    TestRunner tr = new TestRunner(new PStreamToTextView(usrOut));
  TestRunner tr = new TestRunner(System.out);
  TestResult res = tr.doRun(new TestSuite(theClass));    //This constructor creates a suite with all the methods starting with "test" that take no arguments.
  if (!res.wasSuccessful())
    throw new AssertionFailedError();
  return res.wasSuccessful();
}

public void setShowDbg(boolean showDbg) {
  this.showDbg = showDbg;
}

private static void setLockMaxErr(boolean lockMaxErr) {
  FlowTest.lockMaxErr = lockMaxErr;
}

public static void lockMaxErr(float err) {
  setMaxErr(err);
  setLockMaxErr(true);
}

public static void unlockMaxErr() {
  setLockMaxErr(false);
}
}
