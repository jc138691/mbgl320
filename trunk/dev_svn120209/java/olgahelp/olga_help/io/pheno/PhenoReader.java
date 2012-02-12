package io.pheno;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.iox.FileX;
import javax.swing.*;
import java.io.*;
import java.util.StringTokenizer;

import dna.pheno.PhenoMap;
import dna.pheno.PhenoRead;
import dna.snp.pheno.SnpPhenoOpt;
import olga.SnpStation;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/08/2009, 10:17:33 AM
 */
public class PhenoReader {
  public static Log log = Log.getLog(PhenoReader.class);
  private static final int PHENO_ITEM_NUM_TOKENS = 2;

  public static PhenoMap read(SnpStation project, TextView view) {
    SnpPhenoOpt opt = project.getSnpOpt().getPhenoOpt();
    PhenoMap res;

    String fileName = opt.getFileName();
    File file = FileX.openReadFile(fileName, view);
    if (file == null)
      return null;
    BufferedReader from;
    String taskInfo = "Importing Phenotypes ...";
    try {
      if (view != null)
        view.println(taskInfo + FileX.getFileName(file));
      from = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      res = readPage(taskInfo, opt, from, view);
    }
    catch (IOException e) {
      String error = e.toString()  + "\nwhile reading from " + file.getName();
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    return res;
  }


  private static PhenoMap readPage(String taskInfo, SnpPhenoOpt opt
    , BufferedReader from, TextView view) throws IOException {
    if (from == null)
      return null;
    PhenoMap res = new PhenoMap();

    TaskProgressMonitor monitor = null;
    if (taskInfo != null) {
      monitor = ProjectProgressMonitor.getInstance();
      monitor.setTitleText(taskInfo);
    }
    int monitorSize = 10000;

    for (int currIdx = 0;  ; currIdx++) {
      monitorSize = monitor(currIdx, monitor, monitorSize, view);
      if (monitorSize == -1)
        return null;
      String s = from.readLine();    log.dbg("#", currIdx).dbg(s);
      if (s == null)  {   // end of file
        break;
      }
      PhenoRead pheno = readItem(s);
      if (pheno == null) {
        break;
      }
      res.add(pheno);
    }
    view.println("Imported " + res.size() + " phenotype records.");
    view.println("Imported phenotype set: " + res.getPhenoSet());
    return res;
  }

  public static int monitor(int countIdx, TaskProgressMonitor monitor, int monitorSize, TextView view) {
    if (countIdx > monitorSize)
      monitorSize *= 10;
    if (monitor != null && monitor.isCanceled(countIdx, 0, monitorSize)) {
      view.println("User canceled importing.");
      return -1;
    }
    return monitorSize;
  }

  public static PhenoRead readItem(String s) {
    StringTokenizer tokens = new StringTokenizer(s, " \t", false);
    if (tokens.countTokens() != PHENO_ITEM_NUM_TOKENS) {
      String error = "Invalid phenotype record:\n" + s;
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return null;
    }
    PhenoRead res = new PhenoRead();
    String token = tokens.nextToken().trim();
    res.setReadId(token);
    token = tokens.nextToken().trim();
    res.setPheno(token);
    return res;
  }
}
