package dna.snp;

import dna.snp.pheno.SnpPhenoOpt;

import javax.swingx.colorx.ColorOpt;
import javax.langx.SysProp;
import java.awt.*;

import project.model.IntOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/05/2009, 12:17:30 PM
 */
public class SnpOpt {
  private SnpPhenoOpt phenoOpt;
  private SnpFilterOpt snpFilter;
  private SnpFilterFlankOpt flankFilter;
  private SnpNghbrFilterOpt nghbrFilter;

  private static int count = 0;
  public final static int FORMAT_TINT_BGRND = count++;
  public final static int FORMAT_COLOR_BGRND = count++;

  private static int count2 = 0;
  public final static int PADDING_QLTY_MEAN = count2++;
  public final static int PADDING_QLTY_MEDIAN = count2++;

  private int viewFormat;
  private ColorOpt viewSnp;
  private String exportFileName;
  private String exportFileExt;
  private String exportPreview;
  private int exportNumContigs;
  private int padQltyRule;

  public SnpOpt() {
    init();
    loadDefaults();
  }

  private void init() {
    snpFilter = new SnpFilterOpt();
    phenoOpt = new SnpPhenoOpt();
    flankFilter = new SnpFilterFlankOpt();
    nghbrFilter = new SnpNghbrFilterOpt();
    viewSnp = new ColorOpt(new Color(0, 200, 0).getRGB());
  }
  public String toString() {
    String res = "SNPFilter: " + snpFilter + SysProp.EOL;
    res += ("FlankFilter: " +flankFilter.toString() + SysProp.EOL);
    res += ("NghbrFilter: " + nghbrFilter.toString() + SysProp.EOL);

    String str = phenoOpt.toString();
    if (str != null  &&  str.length() > 0)
      res += ("PhenotypeFilter: " + str + SysProp.EOL);

    return res;
  }

  public void loadDefaults() {
    setExportFileExt("snps");
    viewFormat = FORMAT_COLOR_BGRND;
    padQltyRule = PADDING_QLTY_MEDIAN;

    nghbrFilter.setSize(new IntOpt(2));
    nghbrFilter.setMaxMinorFreqPcnt(new IntOpt(10));
    nghbrFilter.setMaxPaddingPcnt(new IntOpt(30));
  }


  public SnpFilterOpt getSnpFilter() {
    return snpFilter;
  }

  public void setSnpFilter(SnpFilterOpt snpFilter) {
    this.snpFilter = snpFilter;
  }

  public SnpFilterFlankOpt getFlankFilter() {
    return flankFilter;
  }

  public void setFlankFilter(SnpFilterFlankOpt flankFilter) {
    this.flankFilter = flankFilter;
  }

  public int getViewFormat() {
    return viewFormat;
  }

  public void setViewFormat(int viewFormat) {
    this.viewFormat = viewFormat;
  }

  public ColorOpt getViewSnp() {
    return viewSnp;
  }

  public void setViewSnp(ColorOpt viewSnp) {
    this.viewSnp = viewSnp;
  }

  public void setExportFileName(String exportFileName) {
    this.exportFileName = exportFileName;
  }

  public String getExportFileName() {
    return exportFileName;
  }

  public void setExportFileExt(String exportFileExt) {
    this.exportFileExt = exportFileExt;
  }

  public String getExportFileExt() {
    return exportFileExt;
  }

  public String getExportPreview() {
    return exportPreview;
  }

  public void setExportPreview(String exportPreview) {
    this.exportPreview = exportPreview;
  }

  public int getExportNumContigs() {
    return exportNumContigs;
  }

  public void setExportNumContigs(int exportNumContigs) {
    this.exportNumContigs = exportNumContigs;
  }

  public void setPadQltyRule(int padQltyRule) {
    this.padQltyRule = padQltyRule;
  }

  public int getPadQltyRule() {
    return padQltyRule;
  }

  public SnpNghbrFilterOpt getNghbrFilter() {
    return nghbrFilter;
  }

  public void setNghbrFilter(SnpNghbrFilterOpt nghbrFilter) {
    this.nghbrFilter = nghbrFilter;
  }

  public SnpPhenoOpt getPhenoOpt() {
    return phenoOpt;
  }

  public void setPhenoOpt(SnpPhenoOpt phenoOpt) {
    this.phenoOpt = phenoOpt;
  }
}
