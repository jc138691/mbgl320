package ucm.io;

import io.DnaFileOpt;
import io.ace.AceContigArr;
import io.ace.AceReader;
import olga.SnpStation;

import javax.swingx.textx.TextView;
import java.io.BufferedReader;
import java.io.IOException;

import dna.contig.ContigArr;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 19/10/2009, 10:10:24 AM
 */
public class UCExportSnpsFromAce extends UCExportSnps {
  private AceReader reader;
  public UCExportSnpsFromAce() {
    reader = new AceReader();
  }
  public DnaFileOpt getFileOpt(SnpStation project) {
    return project.getAceOpt();
  }
  public ContigArr makeContigArr() {
    return new AceContigArr();
  }
  public boolean readOne(BufferedReader from, TextView view, ContigArr contigArr) throws IOException {
    boolean ok = reader.readOne(from, view, (AceContigArr)contigArr);
    if (!ok)
       reader.init(); // otherwise the last contig would be read as the future first contig
    return ok;
  }
}
