package javax.swingx.text_fieldx;
import javax.swing.*;
import javax.swingx.dialog.ApplyDialogUI;
import java.text.DecimalFormat;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 8/10/2008, Time: 15:44:25
 */
public class ScientificField extends FormattedNumberField
{
  public static void main(String[] args) {
    ScientificField test = new ScientificField();
    System.exit(0);
  }
  public ScientificField() {
    super(new Float(0), new Float(10), new DecimalFormat("0.###E0"));
    ScientificFieldTest test = new ScientificFieldTest();
  }
  public ScientificField(int columns, float min, float max) {
    super(new Float(min), new Float(max), new DecimalFormat("0.###E0"));
    setColumns(columns);
  }
  public ScientificField(int columns, float min, float max, DecimalFormat format) {
    super(new Float(min), new Float(max), format);
    setColumns(columns);
  }
  public ScientificField(int columns, float value, float min, float max) {
    super(new Float(min), new Float(max), new DecimalFormat("0.###E0"));
    setColumns(columns);
    setValue(new Float(value));
  }

  public float getInput()
  {
    return ((Float)getValue()).floatValue();
  }

  public void setValue(float v)
  {
    setValue(new Float(v));
  }

  private class ScientificFieldTest extends JFrame {

    public ScientificFieldTest() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JPanel panel = new JPanel(new GridLayout(7, 2));
      JPanel field = new JPanel();

//        field = new JPanel();
//        field.add(new FloatField(2, 3f));  // columns=2
//        panel.add(field);
//        panel.add(new JLabel("FloatField(columns=2, value=3)"));

      field = new JPanel();
      field.add(new ScientificField(1, 0f, 9f));  // number [0, 9]
      panel.add(field);
      panel.add(new JLabel("FloatField(columns=1, value=minIdx=0, maxIdx=9)"));

      field = new JPanel();
      field.add(new ScientificField(3, 1f, 0f, 9.9f));  // number [0, 9]
      panel.add(field);
      panel.add(new JLabel("FloatField(columns=3, 1, minIdx=0, maxIdx=9)"));

      field = new JPanel();
      field.add(new ScientificField(4, -1f, 0f, 9f)); // number [0, 9]
      panel.add(field);
      panel.add(new JLabel("FloatField(columns=4, -1, minIdx=0, maxIdx=9)"));

      field = new JPanel();
      field.add(new ScientificField(5, -1f, 9f, 0f)); // any number
      panel.add(field);
      panel.add(new JLabel("FloatField(columns=5, -1, minIdx=9, maxIdx=0 [note: minIdx > maxIdx!])"));

      field = new JPanel();
      field.add(new ScientificField(7, 0.00001f, 0.00000001f, 0.1f)); // any number
      panel.add(field);
      panel.add(new JLabel("FloatField(columns=5, -1, minIdx=9, maxIdx=0 [note: minIdx > maxIdx!])"));

      ApplyDialogUI dlg = new ApplyDialogUI(panel, this, true);
      dlg.setVisible(true);
    }
  }
}
