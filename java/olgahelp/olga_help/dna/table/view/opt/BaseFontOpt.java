package dna.table.view.opt;

import java.awt.*;

/**
 * jc138691, 03/03/2009, 4:10:40 PM
 */
public class BaseFontOpt {
  private String name = "";
  private int style;
  private int size;

  public BaseFontOpt() {
    loadDefaults();
  }

  public void loadDefaults() {
    name = "Dialog";
    style = 0;
    size = 12;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getStyle() {
    return style;
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Font getFont() {
    return new Font(name, style, size);    
  }
}
