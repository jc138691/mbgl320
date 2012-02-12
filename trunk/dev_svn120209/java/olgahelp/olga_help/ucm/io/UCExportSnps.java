package ucm.io;

import project.ucm.UCController;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.iox.FileX;
import javax.swing.*;
import javax.langx.SysProp;

import dna.snp.view.SnpExportOptView;
import dna.snp.pheno.SnpPhenoOpt;
import dna.snp.SnpOpt;
import dna.snp.SnpTableFactory;
import dna.snp.SnpArr;
import dna.contig.ContigArr;
import dna.contig.Contig;
import dna.pheno.PhenoSnpTableFactory;
import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaMainUI;
import io.DnaFileOpt;

import java.io.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 19/10/2009, 10:09:38 AM
 */
public abstract class UCExportSnps //@Singleton
  implements UCController {
  public static Log log = Log.getLog(UCExportSnps.class);
  private static UCExportSnps instance;
  private static final int FLUSH_STEP = 1000;
  private SnpExportOptView optView;

  public static UCExportSnps getInstance() {
    return instance;
  }
  public static void setInstance(UCExportSnps obj) {
    instance = obj;
  }

  public boolean run() {log.dbg("UCExportSnps.run()");
    SnpStation project = SnpStationProject.getInstance();
    optView.loadTo(project);
    project.saveProjectToDefaultLocation();

    OlgaMainUI ui = OlgaMainUI.getInstance();
    TextView outView = ui.getConsoleView();
    ui.setConsoleView(outView); // get focus

    DnaFileOpt fileOpt = getFileOpt(project); //@Override
    File file = FileX.openReadFile(fileOpt.getFileName(), optView);
    if (file == null)
      return false;

    SnpPhenoOpt phenoOpt = project.getSnpOpt().getPhenoOpt();
    if (phenoOpt.getHasPhenoFile()  &&  ui.getPhenoMap() == null) {
      String error = "Unable to proceed in UCExportSnps.run(): \nphenoOpt.getHasPhenoFile()  &&  ui.getPhenoMap() == null";
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return false;
    }

    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();
    int monitorSize = 100;
    setExportMssg(monitor, monitorSize);

    SnpOpt snpOpt = project.getSnpOpt();
    File snpFile = FileX.openWriteFile(snpOpt.getExportFileName(), optView);
    File helpFile = FileX.openWriteFile(snpOpt.getExportFileName()+"_help", optView);
    if (snpFile == null  || helpFile == null)
      return false;
    try {
      BufferedReader from = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      BufferedWriter to = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(snpFile)));
      BufferedWriter toHelp = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(helpFile)));
      String info = "CAF file name=" + fileOpt.getFileName() + SysProp.EOL + snpOpt.toString() + SysProp.EOL;
      outView.println(info);
      to.write(info);
      toHelp.write(info);

      boolean all = (snpOpt.getExportNumContigs() == -1);
      for (int countIdx = 0; all  || countIdx < snpOpt.getExportNumContigs(); countIdx++) {
        if (countIdx > monitorSize) {
          monitorSize *= 10;
          setExportMssg(monitor, monitorSize);
        }
        if (monitor != null && monitor.isCanceled(countIdx, 0, monitorSize)) {
//          view.println("User canceled importing a CAF file.");
          return false;
        }
        ContigArr contigs = makeContigArr();
        if (!readOne(from, outView, contigs)
//        if (!reader.readOne(null, from, outView, contigs)
          ||  contigs.size() == 0)
          break;

        if (countIdx % FLUSH_STEP == 0) {
          to.flush();
          toHelp.flush();
        }
        Contig contig = contigs.getContig(0);  // one at a time
        SnpTableFactory snpFactory;
        if (phenoOpt.getHasPhenoFile() &&  ui.getPhenoMap() != null) {
          snpFactory = new PhenoSnpTableFactory(ui.getPhenoMap());
        } else {
          snpFactory = new SnpTableFactory();
        }
        SnpArr snps = snpFactory.makeFrom(contig, snpOpt, false);
        contig.setSnpArr(snps);

        String contigCount = "contig " + (countIdx+1) + ":";
        String text = snps.toString(contigCount, snpOpt, false);
        if (text.length() == 0)
          continue;

//        snpOpt
        String helpText = snps.toString(contigCount, snpOpt, true);
        outView.println(helpText);
        to.write(text);
        toHelp.write(helpText);
      }
      outView.println("Done.");
      ui.setStatusWithTime("Finished exporting SNPs.");
      to.flush();
      to.close();
      to = null;
      toHelp.flush();
      toHelp.close();
      toHelp = null;
    }
    catch (IOException e) {
      String error = e.toString()  + "\nwhile reading from " + file.getName();
      log.error(error);
      JOptionPane.showMessageDialog(optView, error);
      snpFile = null;  System.gc();
      return false;
    }
    catch (Exception e) {
      String error = e.toString()  + "\nwhile reading from " + file.getName();
      log.error(error);
      JOptionPane.showMessageDialog(optView, error);
      snpFile = null;  System.gc();
      return false;
    }
    snpFile = null;  System.gc();
    return true;
  }

  public ContigArr makeContigArr() {
      return new ContigArr();
  }
  public abstract DnaFileOpt getFileOpt(SnpStation project);
  public abstract boolean readOne(BufferedReader from, TextView view, ContigArr contigArr) throws IOException;

  private void setExportMssg(TaskProgressMonitor monitor, int monitorSize) {
    if (monitor == null)
      return;
//    monitor.setTitleText("Exporting SNPs from a CAF file.\nProcessing the first "+monitorSize+" contigs ...");
    monitor.setTitleText("First "+monitorSize+" contigs ...");
  }

  public void setOptView(SnpExportOptView optView) {
    this.optView = optView;
  }
}