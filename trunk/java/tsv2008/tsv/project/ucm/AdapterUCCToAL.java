package project.ucm;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 14:02:32
 */
public class AdapterUCCToAL implements ActionListener {
  private UCController uc;
  public AdapterUCCToAL(UCController uc) {
    this.uc = uc;
  }
  public void actionPerformed(ActionEvent e) {
    if (uc != null)
      uc.run();
//    System.gc(); // cleanup
  }
}