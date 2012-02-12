package ucm.io;

import io.fasta.FastaQltyReader;
import olga.SnpStation;
import project.workflow.task.DefaultTaskUI;
import dna.contig.ContigArr;
import dna.table.PageOpt;
import io.ace.AceReader;

import javax.swingx.textx.TextView;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 09/10/2009, 2:06:23 PM
 */
public class UCImportAceFile extends UCImportContigFile {
  protected void setupLogs() {
    add(log);
    add(AceReader.log);
    add(ContigArr.log);
    add(FastaQltyReader.log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public UCImportAceFile(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }
  public PageOpt loadPageOpt(SnpStation project) {
      return project.getAceOpt();
    }
  public ContigArr readContigs(SnpStation project, TextView usrView) {
    return new AceReader().read(project, usrView);
  }
  public void setupExportSnps() {
    UCExportSnps.setInstance(new UCExportSnpsFromAce());
  }
}
