package ucm.io;

import dna.snp.view.SnpExportOptView;
import project.ucm.UCController;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 19/10/2009, 10:47:30 AM
 */
public class UCSelectExportSnpsType implements UCController  {
  public static Log log = Log.getLog(UCSelectExportSnpsType.class);
  private SnpExportOptView optView;

  public UCSelectExportSnpsType(SnpExportOptView view) {
    this.optView = view;
  }

  public boolean run() {
    //this is where we get the correct type
    UCExportSnps uc = UCExportSnps.getInstance();
    uc.setOptView(optView);
    return uc.run();
  }
}
