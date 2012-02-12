package dna.contig.view;

import dna.table.view.table.DnaTableViewFactory;
import dna.table.view.table.format.SnpTableFormatFactory;
import dna.table.view.listeners.ContigArrViewMouseMotion;
import dna.table.PageOpt;
import dna.contig.ContigArr;
import dna.contig.Contig;
import dna.snp.SnpOpt;
import dna.snp.SnpArr;
import dna.snp.SnpTableFactory;
import dna.pheno.PhenoSnpTableFactory;

import javax.utilx.log.Log;

import olga.SnpStation;
import olga.SnpStationProject;
import io.caf.CafOpt;

import java.awt.*;

import ucm.seq.arr.view.UCShowPage;
import ucm.seq.arr.view.UCShowPageContig;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/08/2009, 2:36:39 PM
 */
public class PhenoContigArrView extends ContigArrView {
  public static Log log = Log.getLog(ContigArrView.class);

  public PhenoContigArrView(ContigArr arr, PageOpt pageOpt) {
    super(arr, pageOpt);
//    loadView();
  }

  public SnpArr makeSnps(Contig contig, SnpOpt opt) {
    PhenoSnpTableFactory snpFactory = new PhenoSnpTableFactory(contigArr.getPhenoArr());
    return snpFactory.makeFrom(contig, opt, true); // PHENOs!!!
  }

}


