package olga;

import project.Project;
import project.ProjectModel;

import javax.utilx.log.Log;
import java.awt.*;

import dna.table.view.opt.BaseQltyOpt;
import dna.table.view.opt.SeqSrchOpt;
import dna.table.view.opt.BaseFontOpt;
import dna.table.DnaTableInfo;
import dna.snp.SnpOpt;
import io.fasta.FastaOpt;
import io.caf.CafOpt;
import io.ace.AceOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:26:18.
 */
public class SnpStation extends Project {
  public static Log log = Log.getLog(SnpStation.class);
  public static final String PROJECT_FILE_EXTENSION = "xml";
  private boolean hasMaskFile;
  private String maskFileName;
  private String maskFileExt;
  private Color colorImportOpt;
  private Color colorExportOpt;
  private Color colorSeqView;
  private Color colorConsole;
  private BaseFontOpt baseFontOpt;
  private BaseQltyOpt baseQltyOpt;
  private SeqSrchOpt seqSrchOpt;
  private int importFileType;

  private FastaOpt fastaOpt;
  private CafOpt cafOpt;
  private AceOpt aceOpt;
  private SnpOpt snpOpt;
  private DnaTableInfo currTableInfo;
//  private Point contigTablePos;

//  private String resultsFileName;

  public SnpStation() {
    init();
  }

  private void init() {
  }
  public void loadDefault(String appName, String appVersion) {
    log.dbg("loadDefault(appName=", appName);
    log.dbg("appVersion=", appVersion);
    super.loadDefault(appName, appVersion);
    loadDefault();
  }
  public void loadDefault() {
    setErrorLogFileName("ErrorLog.txt");
    setProjectFileExtension(PROJECT_FILE_EXTENSION);
    super.loadDefault();

    colorExportOpt = new Color(250, 230, 230);
    colorImportOpt = new Color(230, 250, 230);
    colorSeqView = new Color(230, 230, 250);
    colorConsole = new Color(255, 255, 255);

    baseFontOpt = new BaseFontOpt();
    baseQltyOpt = new BaseQltyOpt();
    seqSrchOpt = new SeqSrchOpt();

    currTableInfo = new DnaTableInfo();
    fastaOpt = new FastaOpt();
    cafOpt = new CafOpt();
    aceOpt = new AceOpt();
    snpOpt = new SnpOpt();

    maskFileExt = "xml";

    setLookFeel(ProjectModel.SYSTEM_LOOK);
  }
  public void copyTo(ProjectModel to) {
    super.copyTo(to);
    if (!(to instanceof SnpStation)) {
      log.error("Copy destination is not an SnpStation object");
    }
    SnpStation model = (SnpStation) to;
    model.setErrorLogFileName(getErrorLogFileName());
    model.setProjectFileName(getProjectFileName());

    model.setCurrTableInfo(getCurrTableInfo());
    model.setFastaOpt(getFastaOpt());
    model.setCafOpt(getCafOpt());
    model.setAceOpt(getAceOpt());
    model.setSnpOpt(getSnpOpt());

    model.setImportFileType(getImportFileType());

    model.setHasMaskFile(getHasMaskFile());
    model.setMaskFileName(getMaskFileName());
    model.setMaskFileExt(getMaskFileExt());

    model.setColorImportOpt(getColorImportOpt());
    model.setColorExportOpt(getColorExportOpt());
    model.setColorSeqView(getColorSeqView());
    model.setColorConsole(getColorConsole());

    model.setBaseFontOpt(getBaseFontOpt());
    model.setBaseQltyOpt(getBaseQltyOpt());
    model.setSeqSrchOpt(getSeqSrchOpt());
  }

  public Color getColorImportOpt() {
    return colorImportOpt;
  }

  public void setColorImportOpt(Color colorImportOpt) {
    this.colorImportOpt = colorImportOpt;
  }

  public Color getColorExportOpt() {
    return colorExportOpt;
  }

  public void setColorExportOpt(Color colorExportOpt) {
    this.colorExportOpt = colorExportOpt;
  }

  public Color getColorSeqView() {
    return colorSeqView;
  }

  public void setColorSeqView(Color colorSeqView) {
    this.colorSeqView = colorSeqView;
  }

  public Color getColorConsole() {
    return colorConsole;
  }

  public void setColorConsole(Color colorConsole) {
    this.colorConsole = colorConsole;
  }

  public BaseQltyOpt getBaseQltyOpt() {
    return baseQltyOpt;
  }

  public void setBaseQltyOpt(BaseQltyOpt baseQltyOpt) {
    this.baseQltyOpt = baseQltyOpt;
  }

  public BaseFontOpt getBaseFontOpt() {
    return baseFontOpt;
  }

  public void setBaseFontOpt(BaseFontOpt baseFontOpt) {
    this.baseFontOpt = baseFontOpt;
  }

  public SeqSrchOpt getSeqSrchOpt() {
    return seqSrchOpt;
  }

  public void setSeqSrchOpt(SeqSrchOpt seqSrchOpt) {
    this.seqSrchOpt = seqSrchOpt;
  }

  public String getMaskFileName() {
    return maskFileName;
  }

  public void setMaskFileName(String maskFileName) {
    this.maskFileName = maskFileName;
  }

  public String getMaskFileExt() {
    return maskFileExt;
  }

  public void setMaskFileExt(String maskFileExt) {
    this.maskFileExt = maskFileExt;
  }

  public boolean getHasMaskFile() {
    return hasMaskFile;
  }

  public void setHasMaskFile(boolean hasMaskFile) {
    this.hasMaskFile = hasMaskFile;
  }


  public void setImportFileType(int importFileType) {
    this.importFileType = importFileType;
  }

  public int getImportFileType() {
    return importFileType;
  }

  public FastaOpt getFastaOpt() {
    return fastaOpt;
  }

  public void setFastaOpt(FastaOpt fastaOpt) {
    this.fastaOpt = fastaOpt;
  }

  public CafOpt getCafOpt() {
    return cafOpt;
  }

  public void setCafOpt(CafOpt cafOpt) {
    this.cafOpt = cafOpt;
  }

  public DnaTableInfo getCurrTableInfo() {
    return currTableInfo;
  }

  public void setCurrTableInfo(DnaTableInfo currTableInfo) {
    this.currTableInfo = currTableInfo;
  }

  public SnpOpt getSnpOpt() {
    return snpOpt;
  }

  public void setSnpOpt(SnpOpt snpOpt) {
    this.snpOpt = snpOpt;
  }

  public AceOpt getAceOpt() {
    return aceOpt;
  }

  public void setAceOpt(AceOpt aceOpt) {
    this.aceOpt = aceOpt;
  }
}
