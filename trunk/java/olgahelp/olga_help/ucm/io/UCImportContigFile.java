package ucm.io;

import dna.contig.ContigArr;
import dna.contig.view.ContigArrUI;
import dna.contig.view.ContigArrView;
import dna.contig.view.PhenoContigArrView;
import dna.snp.pheno.SnpPhenoOpt;
import dna.table.PageOpt;
import olga.OlgaFrame;
import olga.OlgaMainUI;
import olga.SnpStation;
import olga.SnpStationProject;
import project.workflow.task.DefaultResView;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.UCRunTask;
import ucm.seq.arr.view.UCRefreshViews;

import javax.swing.*;
import javax.swingx.progress.UCMonitorTaskProgress;
import javax.swingx.textx.TextView;
import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 09/10/2009, 2:08:36 PM
 */
public abstract class UCImportContigFile extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCImportCafFile.class);

  public UCImportContigFile(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }

  protected void setupLogs() {  
//    add(log);
//    add(FuncPlotView.log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }

  public boolean run() {
    log.dbg("UCImportContigFile.run()");
    SnpStation project = SnpStationProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();

    DefaultResView out = getOutView();
    TextView usrView = out.getUsrView();
    out.setUsrView(usrView);  // set focus

    OlgaMainUI ui = OlgaMainUI.getInstance();
    SnpPhenoOpt phenoOpt = project.getSnpOpt().getPhenoOpt();
    if (phenoOpt.getHasPhenoFile() && ui.getPhenoMap() == null) {
      String fileName = phenoOpt.getFileName();
      if (fileName == null || fileName.length() == 0) {
        String error = "Unable to proceed: \nThe 'Phenotypes' box is selected but the phenotype filename is missing."
          + "\nGo to the 'Phenotypes' import options and select the phenotype file.";
        log.error(error);
        JOptionPane.showMessageDialog(usrView, error);
        return false;
      }
      UCRunTask uc = new UCImportPhenoFile(ui.getImportUI());
      uc = new UCMonitorTaskProgress<SnpStation>(uc);
      if (!uc.run()) {
        String error = "Unable to proceed: Error occurred while importing phenotypes.";
        log.error(error);
        JOptionPane.showMessageDialog(usrView, error);
        return false;
      }
    }

    phenoOpt.setPhenoList(null);
    if (ui.getPhenoMap() != null) {
      phenoOpt.setPhenoList(ui.getPhenoMap().getPhenoSet().toStrArr());
    }

    setupExportSnps();
    ContigArr inputArr = readContigs(project, usrView);   //@override
    if (inputArr == null)
      return false;

    ContigArrView view;
    PageOpt pageOpt = loadPageOpt(project); // @override
    if (phenoOpt.getHasPhenoFile()) {
      inputArr.setPhenoArr(ui.getPhenoMap());
      view = new PhenoContigArrView(inputArr, pageOpt);
    } else {
      view = new ContigArrView(inputArr, pageOpt);
    }
    ContigArrUI taskUI = new ContigArrUI(project, OlgaFrame.getInstance(), view);
    ui.setContigArrUI(taskUI);
    taskUI.setActionOnOptChange(new UCMonitorTaskProgress<SnpStation>(new UCRefreshViews(taskUI)));
    return true;
  }

  public abstract PageOpt loadPageOpt(SnpStation project);

  public abstract ContigArr readContigs(SnpStation project, TextView usrView);

  public abstract void setupExportSnps();
}