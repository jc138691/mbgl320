package dna;

import javax.utilx.FastId;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 07/04/2009, 4:58:11 PM
 */
public class DnaInterv {
  private FastId type;
  private FastId text;  // text is likely to be the same in many cases
  private int from;
  private int to;

  public DnaInterv(String type, int from, int to, String text) {
    setType(type);
    this.from = from;
    this.to = to;
    setText(text);
  }
  public DnaInterv(int from, int to, String text) {
    this.from = from;
    this.to = to;
    setText(text);
  }

  public String getText() {
    return text.getId();
  }

  public void setText(String text) {
    this.text = new FastId(text);
  }

  public int getFrom() {
    return from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public int getTo() {
    return to;
  }

  public void setTo(int to) {
    this.to = to;
  }

  public String getType() {
    return type.getId();
  }

  public void setType(String type) {
    this.type = new FastId(type);
  }
}
