package javax.utilx.log.kiss;
import junit.framework.TestCase;

import javax.iox.FileX;
import javax.swingx.textx.TextView;
import javax.utilx.log.Log;
import javax.utilx.log.PStreamToTextView;
import java.util.HashSet;
import java.io.File;
import java.io.PrintStream;

import math.vec.VecDbgView;
import math.vec.Vec;
import math.vec.VecArr;
import math.vec.VecArrDbgView;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 12/09/2008, Time: 15:56:38
 */
public class KissLog {
  private static HashSet<PrintStream> staticPrStreams = new HashSet<PrintStream>();
  private HashSet<PrintStream> localPrStreams = new HashSet<PrintStream>();
  private boolean info = true;
  private boolean dbg = false;
  private boolean newLine = true;
  private Class logClass;

  public KissLog(Class cl) {
    this.logClass = cl;
  }

  public String format(String mssg) {
    return logClass.getName() + ": " + mssg;
  }

  final public void add(PrintStream ps) {
    staticPrStreams.add(ps);
  }
  final public int countPrintStreams(){
    return staticPrStreams.size();
  }
//  final public void addLocal(PrintStream ps) {
//    localPrStreams.add(ps);
//  }
  public void add(TextView view) {
    add(new PStreamToTextView(view));
  }
  final public String error(String s) {
    error2(s);
    return s;
  }
  final public String error2(String s) {
    s = format(s);
    if (!staticPrStreams.contains(System.err)) {
      System.err.println(s);
    }
    for (PrintStream ps : staticPrStreams)
      ps.println(s);
    for (PrintStream ps : localPrStreams)
      ps.println(s);
    return s;
  }
  final public KissLog info(String s) {
    info2(s);
    return this;
  }
  final public void info2(String s) {
    if (!info)
      return;
    s = format(s);
    if (!staticPrStreams.contains(System.out)) {
      System.out.println(s);
    }
    for (PrintStream ps : staticPrStreams)
      ps.println(s);
    for (PrintStream ps : localPrStreams)
      ps.println(s);
  }
  final public KissLog dbg(String s) {
    debug2(s);
    return this;
  }
  final public void debug2(String s) {
    if (!dbg)
      return;
    s = format(s);
    for (PrintStream ps : staticPrStreams) {
      if (newLine)
        ps.println(s);
      else
        ps.print(s);
    }
    for (PrintStream ps : localPrStreams) {
      if (newLine)
        ps.println(s);
      else
        ps.print(s);
    }
  }
//  final public boolean isInfo() {  return info;  }
//  final public void setInfo(boolean info) {    this.info = info;   }

  final public boolean getDbg() {    return dbg;  }
  public void setDbg(boolean dbg) {
//    Log.addGlobal(System.out);
    this.dbg = dbg;
  }
  final public void setDbg() {
    setDbg(true);
  }
  final public void setDebugOff() {  setDbg(false);     }

  final public void info(String s, int i)      {
    if (!info)
      return;
    info(s, Integer.toString(i));
  }
  final public void info(String s, boolean b)  {
    if (!info)
      return;
    info(s, Boolean.toString(b));
  }
  final public void info(String s, double d)   {
    if (!info)
      return;
    info(s, Float.toString((float)d));
  }
  final public void info(String s, Object obj) {
    if (!info)
      return;
    info(s + obj.toString());
  }

  final public KissLog dbg(String s, int i)     {
    if (!dbg)
      return this;
    return dbg(s, Integer.toString(i));
  }
  final public KissLog dbg(String s, boolean b) {
    if (!dbg)
      return this;
    return dbg(s, Boolean.toString(b));
  }
  final public KissLog dbg(String s, double d)  {
    if (!dbg) return this;
    return dbg(s, Float.toString((float)d));
  }
  final public KissLog dbg(String s, Object obj){
    if (!dbg)
      return this;
    return dbg(s + obj.toString());
  }
  final public KissLog dbg(String s, Vec v)     {
    if (!dbg)
      return this;
    return dbg(s, new VecDbgView(v).toString());
  }
  final public KissLog dbg(String s, VecArr v)  {
    if (!dbg)
      return this;
    return dbg(s, new VecArrDbgView(v).toString());
  }
  final public KissLog dbg(String s, FuncArr v)  {
    if (!dbg)
      return this;
    return dbg(s, new FuncArrDbgView(v).toString());
  }

  public void assertZero(String text, double val, double error) {
    float f = (float) Math.abs(val);
    dbg(text + f);
    TestCase.assertEquals(0, f, error);
  }

  public void saveToFile(String text, String dirName, String dir2, String fileName) {
    FileX.writeToFile(text, dirName, dir2, fileName);
  }
  final public KissLog setNewLine(boolean newLine) {
    this.newLine = newLine;
    return this;
  }
  final public KissLog eol() { // end of line
    return setNewLine(true);
  }
  final public KissLog inl() { // in-line
    return setNewLine(false);
  }
}
