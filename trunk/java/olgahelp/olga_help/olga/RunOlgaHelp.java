package olga;

import javax.utilx.log.Log;
import javax.swing.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:22:44.
 * */
public class RunOlgaHelp {
  public static Log log = Log.getLog(RunOlgaHelp.class);
  public static final String REFERENCE = "todo";
  static private final String appName = "OlgaHelp";
  static private final String appVer = "v1_091022";
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
          new RunOlgaHelp().run();
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
