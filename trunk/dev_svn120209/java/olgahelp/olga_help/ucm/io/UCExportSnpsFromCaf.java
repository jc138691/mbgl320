package ucm.io;

import olga.SnpStation;

import io.DnaFileOpt;
import io.caf.CafReader;

import javax.swingx.textx.TextView;
import java.io.BufferedReader;
import java.io.IOException;

import dna.contig.ContigArr;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 21/05/2009, 4:50:58 PM
 */
public class UCExportSnpsFromCaf extends UCExportSnps {
  private CafReader reader;
  public UCExportSnpsFromCaf() {
    reader = new CafReader();
  }
  public DnaFileOpt getFileOpt(SnpStation project) {
    return project.getCafOpt();
  }
  public boolean readOne(BufferedReader from, TextView view, ContigArr contigArr) throws IOException {
    return reader.readOne(from, view, contigArr);
  }
}