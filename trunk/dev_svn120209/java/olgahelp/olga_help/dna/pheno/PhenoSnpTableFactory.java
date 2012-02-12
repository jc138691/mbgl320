package dna.pheno;

import dna.snp.*;
import dna.contig.Contig;
import dna.seq.Seq;

import javax.swing.*;
import javax.utilx.log.Log;

import project.model.IntOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/08/2009, 3:14:23 PM
 */
public class PhenoSnpTableFactory extends SnpTableFactory {
  public static Log log = Log.getLog(PhenoMap.class);
  private PhenoMap phenoMap;

  public PhenoSnpTableFactory(PhenoMap phenoMap) {
    this.phenoMap = phenoMap;
  }

  // NOTE!!! This is exactly the same as SnpTableFactory.loadLocus except for the PHENO-lines.
  public Snp loadLocus(Contig contig, int colIdx, IntOpt trimLenOpt, IntOpt minQltyOpt) {
    LocusPheno res = new LocusPheno(phenoMap);          //PHENO
    boolean trimLenOn = trimLenOpt.getOn();
    int trimLen = trimLenOpt.getVal();
    boolean minQltyOn = minQltyOpt.getOn();
    int minQlty = minQltyOpt.getVal();

    int nRows = contig.size();
    for (int r = 0; r < nRows; r++) {
      Seq seq = contig.getByIdx(r);

      Pheno pheno = phenoMap.getPheno(seq.getIdKey());  // PHENO
      if (pheno == null) {                              // PHENO
        String error = "Unable to proceed: no matching phenotype for dna sequence id:" + seq.getId();
        log.error(error);
        JOptionPane.showMessageDialog(null, error);
        return null;
      }

      if (colIdx >= seq.size())
        continue;
      if (trimLenOn) {  // ignore starts/ends
        if (seq.getOffsetL(colIdx) < trimLen
          || seq.getOffsetR(colIdx) < trimLen)
          continue;
      }
      if (!seq.isPadding(colIdx)) {  // only for non-padding
        if (minQltyOn && (seq.getQlty(colIdx) < minQlty))
          continue;
      }
      res.count(seq.getBaseByte(colIdx), pheno);        //PHENO
    }
    return new SnpPheno(res);                           //PHENO
  }

}