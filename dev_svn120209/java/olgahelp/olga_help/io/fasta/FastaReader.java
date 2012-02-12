package io.fasta;

import dna.table.DnaTableInfo;
import dna.seq.arr.SeqArr;
import dna.seq.Seq;
import dna.SeqStr;
import dna.seq.SeqBytes;

import javax.swing.*;
import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.iox.FileX;
import java.io.*;

import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 16/02/2009 at 14:01:01.
 */
public class FastaReader extends DnaFileReader<SeqArr> {
  private static final String CPP_COMMENTS_TAG = "//";
  public static Log log = Log.getLog(FastaReader.class);
  private String inStr;

  public static boolean loadInfo(SnpStation project, TextView view) {
    FastaOpt opt = project.getFastaOpt();
    String fileName = opt.getFileName();
    DnaTableInfo info = opt.getDnaTableInfo();
    return readAll("Analysing FASTA file ...", fileName, view, info);
  }
  public SeqArr read(SnpStation project,  TextView view) {  //log.setDbg();
    return super.read("FASTA", view, project.getFastaOpt());
  }
  public SeqArr makeResType() {
    return new SeqArr();
  }

//  public static SeqArr read(SnpStation project, TextView view) {
//    FastaOpt opt = project.getFastaOpt();
//    SeqArr res;
//    String fileName = opt.getFileName();
//    File file = FileX.openReadFile(fileName, view);
//    if (file == null)
//      return null;
//    BufferedReader from;
//    String taskInfo = "Importing FASTA file ...";
//    try {
//      if (view != null)
//        view.println(taskInfo + FileX.getFileName(file));
//      from = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//      res = readPage(opt, from);
//    }
//    catch (IOException e) {
//      String error = e.toCsv()  + "\nwhile reading from " + file.getName();
//      log.error(error);
//      JOptionPane.showMessageDialog(view, error);
//      return null;
//    }
//    return res;
//  }

  synchronized public static boolean loadBases(BufferedReader from, Seq res) throws IOException {
    StringBuffer buff = new StringBuffer();
    for (int countIdx = 0;  ; countIdx++) {
      String s = from.readLine();    //log.dbg("#", countIdx).dbg(s);
      if (s == null)  {   // end of file
        s = buff.toString();
        res.setSeq(s); //log.dbg("[EndOfFile] DNA=", s);
        return true;
      }
      s = removeCppComments(s);
      if (s.length() == 0) { // empty line marks the end
        s = buff.toString();
        res.setSeq(s);   //log.dbg("[EndOfDna] DNA=", s);
        return true;
      }
      buff.append(s); // collect
    }
  }
  synchronized public static String removeCppComments(String s) {
    s = s.trim();
    if (s.length() < 2)
      return s;
    int idx = s.indexOf(CPP_COMMENTS_TAG);
    if (idx == -1)
      return s;
    return s.substring(0, idx).trim();
  }

  private static String readSeq(BufferedReader from, Seq res, DnaTableInfo info) {  //log.setDbg();
    try {
      StringBuffer buff = new StringBuffer();
      for (int countIdx = 0;  ; countIdx++) {
        String s = from.readLine();    log.dbg("#", countIdx).dbg(s);
        if (s == null)  {   // end of file
          loadSeq(buff, res, info); // last dna
          return null;
        }
        s = s.trim();
        if (s.length() == 0)
          continue;  // ignore empty lines
        if (s.charAt(0) == '>') {  // this is id of the next fasta record
          loadSeq(buff, res, info);
          return s;
        }
        buff.append(s); // collect sequences
      }
    }
    catch (IOException e) {
      String error = e.toString()  + "\nwhile reading from a FASTA file";
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return null;
    }
  }


  private static boolean readAll(String taskName, String fileName, TextView view, DnaTableInfo info) {
    File file = FileX.openReadFile(fileName, view);
    if (file == null)
      return false;
    if (view != null)
      view.println(taskName + fileName);
    Seq dna = new SeqStr();
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();
    monitor.setTitleText(taskName);
    int monitorSize = 100;
    try {
      BufferedReader from = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      for (int countIdx = 0;  ; countIdx++) {
        monitorSize = monitor(countIdx, monitor, monitorSize, view);
        if (monitorSize == -1)
          return false;
        String s = readSeq(from, dna, info);
        if (s == null)  {   // end of file
          break;
        }
        if (s.charAt(0) == '>') {  // this is id
          String id = s.substring(1);  log.dbg("id>", id);
          dna = new SeqStr();
          dna.setId(id);
        }
      }
      view.println("Data Properties: " + info.toString());
    }
    catch (IOException e) {
      String error = e.toString()  + "\nwhile reading from " + file.getName();
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return false;
    }
    return true;
  }
//  public static SeqArr readPage(FastaOpt opt, BufferedReader from) throws IOException {
//    SeqArr res = new SeqArr();
//    int startIdx = opt.getPageOffsetIdx();
//    int endIdx = startIdx + opt.getPageSize() - 1;
//    String inStr = from.readLine();    log.dbg(inStr);
//    for (int idx = 0; idx <= endIdx; idx++) {
//      inStr = readOne(inStr, from, res);
//      if (inStr == null)  // end of line or an error
//        break;
//      if (idx < startIdx) {
//        res = new SeqArr(); // ignore
//      }
//    }
//    return res;
//  }

  public boolean readOne(BufferedReader from, TextView view, SeqArr resArr) throws IOException {
//  public static String readOne(String inStr
//    , BufferedReader from, SeqArr resArr) throws IOException {
    if (from == null)
      return false;
    if (inStr == null) {
      inStr = from.readLine();    log.dbg(inStr);
    }
    if (inStr == null) // end of file
      return false;
    Seq seq = new SeqBytes();
    if (inStr.charAt(0) == '>') {  // this is id
      String id = inStr.substring(1);  log.dbg("id>", id);
      seq = new SeqBytes();
      seq.setId(id);
      resArr.add(seq);
    }
    inStr = readSeq(from, seq, null);  log.inl().dbg(inStr).eol();
    return true;
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

  private static void loadSeq(StringBuffer buff, Seq seq, DnaTableInfo info) {
    if (buff.length() <= 0)
      return;
    String seqStr = buff.toString();  log.dbg("dna=", seqStr);
    seq.setSeq(seqStr);
    buff.setLength(0);
    SeqArr.updateInfo(info, seqStr);
  }

}
