package snpstation;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaFrame;

import javax.utilx.log.Log;
import javax.swing.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 28/10/2009, 1:13:20 PM
 */
public class RunSnpStation {
  public static Log log = Log.getLog(RunSnpStation.class);
  public static final String REFERENCE = "submitted for publication";
  static private final String appName = "SnpStation";
  static private final String appVer = "v1_100804b";
  // -Xmn100m -Xms100M -Xmx900M  :VM options

  public static void main(String[] args) {
    // http://java.sun.com/docs/books/tutorial/uiswing/examples/misc/InputVerificationDemoProject/src/misc/InputVerificationDemo.java
    // Turn off metal's use of bold fonts
    UIManager.put("swing.boldMetal", Boolean.FALSE);

    try {
      //Schedule a job for the event-dispatching thread:
      //creating and showing this application's GUI.
      //http://java.sun.com/docs/books/tutorial/uiswing/examples/misc/InputVerificationDemoProject/src/misc/InputVerificationDemo.java
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          new RunSnpStation().run();
        }
      });
    } catch(Exception e) {
      log.error("Exception" + e);
    } catch(java.lang.OutOfMemoryError e) {
      log.error("Exception" + e);
    }
  }

  public boolean run()
  {
    setupLog();
//    ProjectLogger logger = ProjectLogger.getInstance();
//    System.setErr(new PrintStream(logger.getOutputStream()));
//    Locale.setDefault(Locale.FRENCH);

    SnpStation project = SnpStationProject.makeInstance(appName, appVer);
    OlgaFrame frame = new OlgaFrame(project);
    frame.showSmallScreen();
//    frame.showFullScreen();
    return true;
  }
  private void setupLog() {


  }
}
