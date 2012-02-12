package qm_station;
import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 15:28:42
 */
public class RunQMStation {
  public static Log log = Log.getLog(RunQMStation.class);
  //  public static final String REFERENCE = LogBB_2007_paper.REFERENCE;
  static private final String appName = "QM-STATION";
  static private final String appVer = "v1_081218c";

  public static void main(String[] args) {
    try {
      //Schedule a job for the event-dispatching thread:
      //creating and showing this application's GUI.
      //http://java.sun.com/docs/books/tutorial/uiswing/examples/misc/InputVerificationDemoProject/src/misc/InputVerificationDemo.java
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          new RunQMStation().run();
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

    QMS project = QMSProject.makeInstance(appName, appVer);
    QMSFrame frame = new QMSFrame(project);
    frame.showSmallScreen();
//    frame.showFullScreen();
    return true;
  }
  private void setupLog() {


  }
}
