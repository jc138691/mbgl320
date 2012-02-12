package project.ucm;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 14:02:46
 */
public class AdapterUCCToALThread implements ActionListener {
  private UCController uc;
  public AdapterUCCToALThread(UCController uc) {
    this.uc = uc;
  }
  public void actionPerformed(ActionEvent e) {
    Runnable runnable = new Runnable() {
      public void run() {
        if (uc != null)
          uc.run();
//        System.gc(); // cleanup
      }
    };
    new Thread(runnable).start();
  }
}