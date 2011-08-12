package project.ucm;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 5/09/2008, Time: 10:48:40
 */
public class UCRunGC implements UCController {
  public boolean run() {
    System.gc();
    return true;
  }
}