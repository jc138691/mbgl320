package dna.contig;

import dna.seq.Seq;
import dna.DnaAssemArr;
import dna.DnaFinals;
import dna.ReadAssem;
import dna.snp.SnpArr;
import dna.snp.SnpOpt;
import dna.seq.arr.SeqArr;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 21/04/2009, 12:49:44 PM
 */
public class Contig extends SeqArr {
  private DnaAssemArr assemArr;
  private SnpArr snpArr;

  public Contig() {
    assemArr = new DnaAssemArr();
    snpArr = new SnpArr();
  }

  public DnaAssemArr getAssemArr() {
    return assemArr;
  }
  public void addAssem(ReadAssem assem) {
    assemArr.add(assem);
  }

  public void setAssemArr(DnaAssemArr assemArr) {
    this.assemArr = assemArr;
  }

  public SnpArr getSnpArr() {
    return snpArr;
  }

  public void setSnpArr(SnpArr snpArr) {
    this.snpArr = snpArr;
  }

//  public void setPadQlty(int colIdx, int qlty) {
//    for (int r = 0; r < size(); r++) {
//      Seq dna = getByIdx(r);
//      if (colIdx >= dna.size())
//        continue;
//      if (dna.getBaseByte(colIdx) == DnaFinals.BASE_PAD)
//        dna.setQlty(colIdx, (byte)qlty);
//    }
//  }

  public String makeExportPreview(SnpOpt opt) {
    return "Contig id=" + getId() + snpArr.makeExportPreview(opt);
  }
}
