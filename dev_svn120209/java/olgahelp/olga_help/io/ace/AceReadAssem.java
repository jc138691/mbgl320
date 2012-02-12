package io.ace;

import dna.ReadAssem;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 12/10/2009, 12:13:15 PM
 */
public class AceReadAssem extends ReadAssem {
  private int startPos;
  private boolean complemented;

  public AceReadAssem(String readName) {
    super(readName);
  }

  public void setStartPos(int startPos) {
    this.startPos = startPos;
    setR(1);
    setS(startPos);
  }

  public int getStartPos() {
    return startPos;
  }

  public void setComplemented(boolean complemented) {
    this.complemented = complemented;
  }

  public boolean isComplemented() {
    return complemented;
  }
}
