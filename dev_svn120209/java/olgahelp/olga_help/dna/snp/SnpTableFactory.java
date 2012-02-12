package dna.snp;

import dna.snp.flank.SnpMinBestFlankLen;
import dna.table.DnaTableInfo;
import dna.contig.Contig;
import dna.seq.Seq;
import dna.snp.flank.FlankArrFactory;
import dna.snp.flank.FlankArr;
import dna.snp.flank.SnpMinFlankLen;
import dna.snp.pheno.SnpPhenoOpt;
import dna.snp.pheno.SnpPhenoMinReads;
import dna.snp.pheno.SnpPhenoMaxPVal;
import dna.snp.pheno.SnpPhenoMinAssoc;

import javax.swing.*;
import javax.utilx.log.Log;

import project.model.IntOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/05/2009, 12:18:40 PM
 */
public class SnpTableFactory {
  public static Log log = Log.getLog(SnpTableFactory.class);
  public SnpArr makeFrom(Contig contig, SnpOpt opt, boolean setPadQltyOn) {
    DnaTableInfo info = contig.calcInfo();
//    if (setPadQltyOn)          // THIS turned out to be not useful
//      setPadQlty(contig, info, opt);
    SnpArr res = loadAllSnps(contig, info, opt);
//    SnpArr res = loadAllSnpsFAST(contig, info, opt);
    FlankArrFactory flankFactory = new FlankArrFactory();
    flankFactory.loadFlank(res, contig, info, opt, +1);
    flankFactory.loadFlank(res, contig, info, opt, -1);
    checkMinFlankLen(res, info, opt);
    checkMinBestFlankLen(res, info, opt);
    checkNghbrhood(res, info, opt);
    checkPhenos(res, info, opt);
    findBestSnp(res, info, opt);
    return res;
  }

//  private SnpArr setPadQlty(Contig contig, DnaTableInfo info, SnpOpt opt) {
//    SnpArr res = new SnpArr();
//    for (int i = 0; i < info.getMaxLen(); i++) { //scan columns
//      LocusPadQlty loc = loadLocusPadQlty(contig, i);
//      if (!loc.hasPadding())
//        continue;
//      int qlty = calcPadQlty(loc, opt);
//      contig.setPadQlty(i, qlty);
//    }
//    return res;
//  }

//  private int calcPadQlty(LocusPadQlty loc, SnpOpt opt) {
//    if (opt.getPadQltyRule() == SnpOpt.PADDING_QLTY_MEAN)
//      return loc.getMeanQlty();
//    return loc.getMedianQlty();
//  }

  public SnpArr loadAllSnps(Contig contig, DnaTableInfo info, SnpOpt opt) {
    SnpArr res = new SnpArr();
    SnpFilterOpt snpOpt = opt.getSnpFilter();
    for (int colIdx = 0; colIdx < info.getMaxLen(); colIdx++) { //scan columns
      Snp snp = loadLocus(contig, colIdx, snpOpt.getTrimLen(), snpOpt.getMinQlty());  //@Override
      snp = checkMinReads(snp, snpOpt);
      snp = checkMinMinorReads(snp, snpOpt);
      snp = checkMinMinorFreq(snp, snpOpt);
      snp = checkMaxPadding(snp, snpOpt);
      res.add(snp);
    }
    return res;
  }
  public Snp loadLocus(Contig contig, int colIdx, IntOpt trimLenOpt, IntOpt minQltyOpt) {
    Locus res = new Locus();
    boolean trimLenOn = trimLenOpt.getOn();
    int trimLen = trimLenOpt.getVal();
    boolean minQltyOn = minQltyOpt.getOn();
    int minQlty = minQltyOpt.getVal();
    int nRows = contig.size();
    for (int r = 0; r < nRows; r++) {
      Seq seq = contig.getByIdx(r);
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
      res.count(seq.getBaseByte(colIdx));
    }
//    if (colIdx == 57) { debugging
//      int dbg = 1;
//    }
    return new Snp(res);
  }

  private void checkMinFlankLen(SnpArr snpArr, DnaTableInfo info, SnpOpt opt) {
    SnpFilterFlankOpt flankOpt = opt.getFlankFilter();
    if (!flankOpt.getMinLen().getOn())
      return;
    for (int snpIdx = 0; snpIdx < info.getMaxLen(); snpIdx++) { //scan columns
      Snp snp = snpArr.get(snpIdx);
      if (!snp.isSnp() || !snp.isValidSnp())
        continue;
      // found a valid SNP
      if (flankOpt.getMinLen().on()  && snp.getMinFlankLen() < flankOpt.getMinLen().val()) {
        snp = new SnpMinFlankLen(snp, flankOpt.getMinLen().val());
        snpArr.set(snpIdx, snp);
      }
    }
  }
  private void checkMinBestFlankLen(SnpArr snpArr, DnaTableInfo info, SnpOpt opt) {
    SnpFilterFlankOpt flankOpt = opt.getFlankFilter();
    if (!flankOpt.getMinBestLen().getOn())
      return;
    for (int snpIdx = 0; snpIdx < info.getMaxLen(); snpIdx++) { //scan columns
      Snp snp = snpArr.get(snpIdx);
      if (!snp.isSnp() || !snp.isValidSnp())
        continue;
      // found a valid SNP
      if (flankOpt.getMinBestLen().on()  && snp.getMaxFlankLen() < flankOpt.getMinBestLen().val()) {
        snp = new SnpMinBestFlankLen(snp, flankOpt.getMinLen().val());
        snpArr.set(snpIdx, snp);
      }
    }
  }
  private void checkNghbrhood(SnpArr snpArr, DnaTableInfo info, SnpOpt opt) {
    SnpNghbrFilterOpt select = opt.getNghbrFilter();
    if (!select.getSize().getOn())
      return;

    for (int snpIdx = 0; snpIdx < info.getMaxLen(); snpIdx++) { //scan columns
      Snp snp = snpArr.get(snpIdx);
      if (!snp.isSnp() || !snp.isValidSnp())
        continue;
      // found a valid SNP
      snp = checkNeighbourhood(snp, select, +1);
      snp = checkNeighbourhood(snp, select, -1);
      snpArr.set(snpIdx, snp);
    }
  }
  private void checkPhenos(SnpArr snpArr, DnaTableInfo info, SnpOpt opt) {
    SnpPhenoOpt phenoOpt = opt.getPhenoOpt();
    if (!phenoOpt.getHasPhenoFile())
      return;
    for (int snpIdx = 0; snpIdx < info.getMaxLen(); snpIdx++) { //scan columns
      Snp snp = snpArr.get(snpIdx);
      if (!snp.isSnp() || !snp.isValidSnp())
        continue;
      // found a valid SNP
      if (!(snp instanceof SnpPheno)) {
        String error = "Unable to proceed: phenotypes are not loaded but getHasPhenoFile()==true.";
        log.error(error);
        JOptionPane.showMessageDialog(null, error);
        break;
      }
      snp = checkPhenoMinReads((SnpPheno)snp, phenoOpt);
      snp = checkPhenoMinAssoc((SnpPheno)snp, phenoOpt);
      snp = checkPhenoPVal((SnpPheno)snp, phenoOpt);
      snpArr.set(snpIdx, snp);
    }
  }
  private void findBestSnp(SnpArr snpArr, DnaTableInfo info, SnpOpt opt) {
    SnpFilterOpt select = opt.getSnpFilter();
    if (!select.getBestSnpPerContig())
      return;

    Snp best = null;
    int count = 0;
    for (int snpIdx = 0; snpIdx < info.getMaxLen(); snpIdx++) { //scan columns
      Snp snp = snpArr.get(snpIdx);
      if (!snp.isSnp() || !snp.isValidSnp())
        continue;
      count++;
      if (best == null  ||  best.getMinorFreqPcnt() < snp.getMinorFreqPcnt()) {
        best = snp;
      }
    }
    if (best == null  || count < 2)
      return; // nothing to do

    for (int snpIdx = 0; snpIdx < info.getMaxLen(); snpIdx++) { //scan columns
      Snp snp = snpArr.get(snpIdx);
      if (!snp.isSnp() || !snp.isValidSnp())
        continue;
      if (snp != best) {
        Snp tmp = new SnpNotTheBest(snp, best);
        snpArr.set(snpIdx, tmp);
      }
    }
  }


  public Snp checkMinReads(Snp snp, SnpFilterOpt opt) {
    if (!opt.getMinReads().getOn()
      || !snp.isSnp()  ||  !snp.isValidSnp())
      return snp;
    Snp res = new SnpMinReads(snp, opt.getMinReads().val());
    return selectIfInvalid(snp, res);
  }
  public Snp checkMinMinorReads(Snp snp, SnpFilterOpt opt) {
    if (!opt.getMinMinorReads().getOn()
      || !snp.isSnp()  ||  !snp.isValidSnp())
      return snp;
    Snp res = new SnpMinMinorReads(snp, opt.getMinMinorReads().val());
    return selectIfInvalid(snp, res);
  }

  public Snp checkMinMinorFreq(Snp snp, SnpFilterOpt opt) {
    if (!opt.getMinorFreqPcnt().on()
      || !snp.isSnp()  ||  !snp.isValidSnp())
      return snp;
    Snp res = new SnpMinMinorFreq(snp, opt.getMinorFreqPcnt().val());
    return selectIfInvalid(snp, res);
  }
  public Snp checkPhenoMinAssoc(SnpPheno snp, SnpPhenoOpt opt) {
    if (!opt.getMaxPValue().on()
      || !snp.isSnp()  ||  !snp.isValidSnp())
      return snp;
    Snp res = new SnpPhenoMinAssoc(snp, opt);
    return selectIfInvalid(snp, res);
  }
  public Snp checkPhenoPVal(SnpPheno snp, SnpPhenoOpt opt) {
    if (!opt.getMaxPValue().on()
      || !snp.isSnp()  ||  !snp.isValidSnp())
      return snp;
    Snp res = new SnpPhenoMaxPVal(snp, opt);
    return selectIfInvalid(snp, res);
  }
  public Snp checkPhenoMinReads(SnpPheno snp, SnpPhenoOpt opt) {
    if (!opt.getMinReads().on()
      || !snp.isSnp()  ||  !snp.isValidSnp())
      return snp;
    Snp res = new SnpPhenoMinReads(snp, opt);
    return selectIfInvalid(snp, res);
  }


  private static Snp selectIfInvalid(Snp snp, Snp newSnp) {
    if (!newSnp.isValidSnp())
      return newSnp;
    return snp;
  }

  public Snp checkMaxPadding(Snp snp, SnpFilterOpt opt) {
    if (!opt.getPaddingPcnt().on()
      || !snp.isSnp()  ||  !snp.isValidSnp())
      return snp;
    Snp res = new SnpMaxPadding(snp, opt.getPaddingPcnt().val());
    return selectIfInvalid(snp, res);
  }

  private Snp checkNeighbourhood(Snp snp, SnpNghbrFilterOpt opt, int step) {
    if (!opt.getSize().on()
      || !snp.isSnp()  ||  !snp.isValidSnp())
      return snp;
    int len = opt.getSize().val();
    FlankArr flank = snp.getFlankR();
    int startIdx = 0;
    if (step == -1) {
      flank = snp.getFlankL();
      startIdx = flank.size()-1;
    }
    if (opt.getMaxPaddingPcnt().on()) {
      int count = 0;
      for (int i = startIdx; i >= 0  &&  i < flank.size(); i += step) {
        if (count++ > len)
          break;
        Snp nbrSnp = flank.get(i);
        if (nbrSnp.getPaddingPcnt() > opt.getMaxPaddingPcnt().val()) {
          return new SnpMaxNghbrPadding(snp, opt.getMaxPaddingPcnt().val(), nbrSnp);
        }
      }
    }
    if (opt.getMaxMinorFreqPcnt().on()) {
      int count = 0;
      for (int i = startIdx; i >= 0  &&  i < flank.size(); i += step) {
        if (count++ > len)
          break;
        Snp nbrSnp = flank.get(i);
        if (nbrSnp.getMinorFreqPcnt() > opt.getMaxMinorFreqPcnt().val()) {
          return new SnpMaxNgbhrMinorFreq(snp, opt.getMaxMinorFreqPcnt().val(), nbrSnp);
        }
      }
    }
    return snp;
  }

//  public LocusPadQlty loadLocusPadQlty(Contig contig, int colIdx) {
//    LocusPadQlty res = new LocusPadQlty();
//    for (int r = 0; r < contig.size(); r++) {
//      Seq dna = contig.getByIdx(r);
//      if (colIdx >= dna.size())
//        continue;
//      res.count(dna.getBaseByte(colIdx), dna.getQlty(colIdx));
//    }
//    return res;
//  }
}
