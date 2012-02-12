package qm_station;
import javax.swingx.filechooserx.SingleFileFilter;
import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 15:54:27
 */
public class QMSFileFilter extends SingleFileFilter {
  public static Log log = Log.getLog(QMSFileFilter.class);
  public QMSFileFilter() {
    super("QM-STATION project file extension", "txt");
    String ext = QMSProject.getInstance().getProjectFileExtension();
    setExtension(ext);
    setDescription("QM-STATION project files (*." + getExtension() + ")");
  }
}
