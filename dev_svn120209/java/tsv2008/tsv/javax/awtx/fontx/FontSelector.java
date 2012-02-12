package javax.awtx.fontx;

/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swingx.panelx.GridBagView;

/*
 * This applet displays a String with the user's selected
 * fontname, style and size attributes.
*/

// from http://java.sun.com/docs/books/tutorial/2d/text/fonts.html
public class FontSelector extends GridBagView
  implements ChangeListener, ItemListener {

  private JTextField sampleText;
  private JComboBox fonts, styles;
  private JSpinner sizes;
  private String fontName = "Dialog";
  private int fontStyle = 0;
  private int fontSize = 12;
  private ChangeListener changeListener;
  private boolean reportChange = true;
  private String[] styleNames = {"Plain", "Bold", "Italic", "Bold Italic"};

  public FontSelector() {
//    super("Font");
    init();
  }
  public String getFontName() {
    return fontName;
  }
  public void setFontName(String name) {
    this.fontName = name;
    reportChange = false;  // setting new value from loadFrom()
    fonts.setSelectedItem(fontName);
    reportChange = true;   // NOTE!!!
  }
  public int getFontStyle() {
    return fontStyle;
  }
  public void setFontStyle(int style) {
    this.fontStyle = style;
    reportChange = false;  // setting new value from loadFrom()
    styles.setSelectedItem(styleNames[fontStyle]);
    reportChange = true;   // NOTE!!!
  }
  public int getFontSize() {
    return fontSize;
  }
  public void setFontSize(int size) {
    this.fontSize = size;
    reportChange = false;  // setting new value from loadFrom()
    sizes.getModel().setValue(size);
    reportChange = true;   // NOTE!!!
  }
  public void init() {
    startRow(new JLabel("Family"));
    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    fonts = new JComboBox(gEnv.getAvailableFontFamilyNames());
    fonts.setSelectedItem(fontName);
    fonts.setMaximumRowCount(5);
    fonts.addItemListener(this);
    endRow(fonts);

    startRow(new JLabel("Style"));
    styles = new JComboBox(styleNames);
    styles.addItemListener(this);
    endRow(styles);

    startRow(new JLabel("Size"));
    sizes = new JSpinner(new SpinnerNumberModel(12, 6, 24, 1));
    sizes.addChangeListener(this);
    endRow(sizes);

    sampleText = new JTextField("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    sampleText.setFont(new Font(fontName, fontStyle, fontSize));
    sampleText.setBackground(Color.white);

    startRow(new JLabel("Sample"));
    endRow(sampleText);
  }

  /*
  * Detect a state change in any of the settings and create a new
  * Font with the corresponding settings. Set it on the test component.
  */
  public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange() != ItemEvent.SELECTED) {
      return;
    }
    if (e.getSource() == fonts) {
      fontName = (String)fonts.getSelectedItem();
    } else {
      fontStyle = styles.getSelectedIndex();
    }
    sampleText.setFont(new Font(fontName, fontStyle, fontSize));
    if (reportChange)
      changeListener.stateChanged(null);
  }

  public void stateChanged(ChangeEvent e) {
    try {
      String size = sizes.getModel().getValue().toString();
      fontSize = Integer.parseInt(size);
      sampleText.setFont(new Font(fontName, fontStyle, fontSize));
    } catch (NumberFormatException nfe) {
    }
    if (reportChange)
      changeListener.stateChanged(e);
  }

  public static void main(String s[]) {
    JFrame f = new JFrame("FontSelector");
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    JPanel fontSelector = new FontSelector();
    f.add(fontSelector, BorderLayout.NORTH);
    f.pack();
    f.setVisible(true);
  }

  public void setChangeListener(ChangeListener changeListener) {
    this.changeListener = changeListener;
  }

  public void setSampleText(String text) {
    sampleText.setText(text);
  }
}


//class TextTestPanel extends JComponent {
//  public void setFont(Font font) {
//    super.setFont(font);
//    repaint();
//  }
//  public void paintComponent(Graphics g) {
//    super.paintComponent(g);
//    g.setColor(Color.white);
//    g.fillRect(0, 0, getWidth(), getHeight());
//    g.setColor(Color.black);
//    g.setFont(getFont());
//    FontMetrics metrics = g.getFontMetrics();
//    String text = "The quick brown fox jumped over the lazy dog";
//    int x = getWidth()/2 - metrics.stringWidth(text)/2;
//    int y = getHeight();
//    g.drawString(text, x, y);
//  }
//}
