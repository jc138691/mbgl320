package olga;

import javax.swingx.filechooserx.SingleFileFilter;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:25:31.
 */
public class OlgaFileFilter extends SingleFileFilter {
  public static Log log = Log.getLog(OlgaFileFilter.class);
  public OlgaFileFilter() {
    super("OlgaHelp project file extension", "xml");
    String ext = SnpStationProject.getInstance().getProjectFileExtension();
    setExtension(ext);
    setDescription("OlgaHelp project files (*." + getExtension() + ")");
  }
}

