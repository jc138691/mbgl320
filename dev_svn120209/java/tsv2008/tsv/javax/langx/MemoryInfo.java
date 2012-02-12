package javax.langx;
import java.text.NumberFormat;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 14:58:16
 */
public class MemoryInfo {
  private Runtime runtime = Runtime.getRuntime();
  private final static double SCALE = 1.e-6;
  private NumberFormat FORMAT = NumberFormat.getNumberInstance();
  public MemoryInfo() {
    System.gc();
    FORMAT.setMaximumFractionDigits(1);
  }
  public String getStatus() {
    StringBuffer buff = new StringBuffer();
    buff.setLength(0);
    buff.append("Free/Total memory [MB]: ");
    buff.append(toStringFreeMemory()).append("/");
    buff.append(toStringAllocatedMemory());
//        + " / " + nf_.formatLog((double) runtime.maxMemory() * SCALE)
    return buff.toString();
  }
  public String toString() {
    runtime = Runtime.getRuntime();
    FORMAT.setMinimumFractionDigits(1);
    return "Java Virtual Machine memory:"
      + " \n  Free                = " + toStringFreeMemory()
      + " \n  Currently Allocated = " + toStringAllocatedMemory()
      + " \n  Max                 = " + toStringMaxMemory();
  }
  public String toStringFreeMemory() {
    return FORMAT.format((double) runtime.freeMemory() * SCALE) + "MB";
  }
  public String toStringAllocatedMemory() {
    return FORMAT.format((double) runtime.totalMemory() * SCALE) + "MB";
  }
  public String toStringMaxMemory() {
    return FORMAT.format((double) runtime.maxMemory() * SCALE) + "MB";
  }
}