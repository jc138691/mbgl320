package dna.snp.flank;

import dna.snp.*;
import dna.contig.Contig;
import dna.table.DnaTableInfo;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 18/05/2009, 4:22:41 PM
 */
public class FlankArrFactory extends SnpTableFactory
{
  public void loadFlank(SnpArr snpArr, Contig contig, DnaTableInfo info, SnpOpt opt, int step) {
    SnpFilterFlankOpt flankOpt = opt.getFlankFilter();
    SnpFilterOpt snpOpt = opt.getSnpFilter();
    for (int snpIdx = 0; snpIdx < info.getMaxLen(); snpIdx++) { //scan columns
//      if (snpIdx == 681) {
//        int dbg = 1;
//      }
      Snp currSnp = snpArr.get(snpIdx);
      if (!currSnp.isSnp() || !currSnp.isValidSnp())
        continue;
      // found a valid SNP

      FlankArr flank = new FlankArr();
      for (int fnkIdx = snpIdx+step; fnkIdx >= 0  &&  fnkIdx < info.getMaxLen()
        ; fnkIdx=fnkIdx+step) { //step>0, scan RIGHT flank

        // dk090612: Do not stop at the next SNP!!! Different compiting SNPs will be compared later
//        Snp snp = snpArr.get(fnkIdx);
//        if (snp.isSnp() && snp.isValidSnp())
//          break;   // this is the next SNP, hence the end of this flank

        Locus loc = loadLocus(contig, fnkIdx, snpOpt.getTrimLen(), snpOpt.getMinQlty());
        Flank fnk = new Flank(loc);
        if (fnk.getNumReads() == 0)       // ignore empty locus
          continue; // THIS IS NOT AN ERROR!!!

        if (fnk.hasPadding()) {
          if (flankOpt.getMaxInsertsPcnt().on() &&  (flankOpt.getMaxInsertsPcnt().val() >= fnk.getInsertsPcnt())) {
            continue;   // ignore inserts
          }
        }

//        fnk = checkMaxPadding(fnk, flankOpt);
//        if (fnk == null)       // ignore excessive padding
//          continue; // THIS IS NOT AN ERROR!!!

        fnk = checkMinReads(fnk, flankOpt);
        if (fnk == null)
          break;  // failed this test

//        fnk = checkMaxMinorFreq(fnk, flankOpt);   // [dak090803] DO NOT STOP! Report IUPACs
//        if (fnk == null)
//          break;  // failed this test

        if (step > 0)
          flank.add(fnk);
        else
          flank.add(0, fnk);
      }
      if (step > 0)
        currSnp.setFlankR(flank);
      else
        currSnp.setFlankL(flank);
    }
  }

  public Flank checkMinReads(Flank fnk, SnpFilterFlankOpt opt) {
    if (!opt.getMinReads().on())
      return fnk;
    if (fnk.getNumReads() < opt.getMinReads().val()) {
      return null;      // todo: possibly return new FlankMinReads() to store help message
    }
    return fnk;
  }
  private Flank checkMaxMinorFreq(Flank fnk, SnpFilterFlankOpt opt) {
    if (!opt.getIupacMinMinorFreqPcnt().on())
      return fnk;
    if (fnk.getMinorFreqPcnt() > opt.getIupacMinMinorFreqPcnt().val()) {    // NOTE!!!  the greater sign  '>' (not '<')!!!
      return null;
    }
    return fnk;
  }
//  private Flank checkMaxPadding(Flank fnk, SnpFilterFlankOpt opt) {
//    if (!opt.getPaddingOn())
//      return fnk;
//    if (fnk.getPaddingPcnt() > opt.getPaddingPcnt()) {
//      return null;
//    }
//    return fnk;
//  }
}
