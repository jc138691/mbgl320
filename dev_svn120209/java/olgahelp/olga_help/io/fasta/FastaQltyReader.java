package io.fasta;

import dna.seq.arr.SeqArr;
import dna.table.DnaTableInfo;
import dna.seq.Seq;
import dna.seq.SeqBytes;
import dna.SeqStr;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.swing.*;
import javax.iox.FileX;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import math.vec.ByteVec;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 20/02/2009 at 12:20:37.
 */
public class FastaQltyReader {
  public static Log log = Log.getLog(FastaQltyReader.class);
  private static Seq tmpSeq = new SeqBytes();

  public static boolean loadInfo(SnpStation project, TextView view) {
    FastaOpt opt = project.getFastaOpt();
    String fileName = opt.getQltyFileName();
    DnaTableInfo info = opt.getDnaTableInfo();
    return readAll("Analysing QUALITY file ...", fileName, view, info);
  }
  public static SeqArr read(SeqArr res, SnpStation project,  TextView view) {  //log.setDbg();
    FastaOpt opt = project.getFastaOpt();
    String fileName = opt.getQltyFileName();
    File file = FileX.openReadFile(fileName, view);
    if (file == null)
      return null;
    BufferedReader from;
    String taskInfo = "Importing QUALITY file ...";
    try {
      if (view != null)
        view.println(taskInfo + FileX.getFileName(file));
      from = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      res = readPage(res, opt, from, view);
    }
    catch (IOException e) {
      String error = e.toString()  + "\nwhile reading from " + file.getName();
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
      return null;
    }
    return res;
  }
  private static String readQlty(BufferedReader from, Seq res, DnaTableInfo info) {
    try {
      ArrayList<Integer> buff = new ArrayList<Integer>();
      for (int countIdx = 0;  ; countIdx++) {
        String s = from.readLine();    log.dbg("#", countIdx).dbg(s);
        if (s == null)  {   // end of file
          loadQltyFromBuff(buff, res, info); // last dna
          return null;
        }
        s = s.trim();
        if (s.charAt(0) == '>') {  // this is id of the next fasta record
          loadQltyFromBuff(buff, res, info);
          return s;
        }
        if (!appendInts(buff, s)) // collect sequences
          return null;
      }
    }
    catch (IOException e) {
      String error = e.toString()  + "\nwhile reading from a QUALITY file";
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
        monitorSize = FastaReader.monitor(countIdx, monitor, monitorSize, view);
        if (monitorSize == -1)
          return false;
        String s = readQlty(from, dna, info);
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

  public static SeqArr readPage(SeqArr res, FastaOpt opt, BufferedReader from, TextView view) throws IOException {
    int startIdx = opt.getPageOffsetIdx();
    int endIdx = startIdx + opt.getPageSize() - 1;
    String inStr = from.readLine();    log.dbg(inStr);
    for (int idx = 0; idx <= endIdx; idx++) {
      Seq seq = null;
      if (idx >= startIdx) {
        seq = res.getByIdx(idx - startIdx);
      }
      inStr = readOne(inStr, from, seq, view);
      if (inStr == null)  // end of line or an error
        break;
    }
    return res;
  }

  public static String readOne(String inStr
    , BufferedReader from, Seq res, TextView view) throws IOException {
    if (from == null  ||  inStr == null)
      return null;
    Seq seq = null;
    if (res != null) {
      seq = res;
    }
    else {
      seq = new SeqBytes();
    }
    if (inStr.charAt(0) == '>') {  // this is id
      String id = inStr.substring(1);  log.dbg("id>", id);
      if (res != null && !res.getId().equals(id)) {
        String error = "Expecting '" + res.getId() + "' \nbut got '" + id + "'";
        log.error(error);
        JOptionPane.showMessageDialog(view, error);
      }
    }
    inStr = readQlty(from, seq, null);  log.inl().dbg(inStr).eol();
    return inStr;
  }

  public static boolean appendInts(ArrayList<Integer> buff, String s) {
    StringTokenizer tokens = new StringTokenizer(s, " \t", false);
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken().trim();
      int q = -1;
      try {
        q = Integer.parseInt(token);
        buff.add(q);
      } catch (NumberFormatException e) {
        String error = e.toString() + "\n while parsing token >" + token + "<." ;
        log.error(error);
        JOptionPane.showMessageDialog(null, error);
        return false;
      }
    }
    return true;
  }
  private static boolean loadQltyFromBuff(ArrayList<Integer> buff, Seq seq
    , DnaTableInfo info) {
    byte[] qlty = ByteVec.toArray(buff.toArray());
    buff.clear();
    if (seq != null) {
//      if (qlty.length != seq.size()) {
//        String error = "Mismatch between sequence lengths: expecting len=" + seq.size()
//          + " but read " + qlty.length;
//        log.error(error);
//        JOptionPane.showMessageDialog(null, error);
//        return false;
//      }
    }
    else {
      seq = tmpSeq; // this is just to collect info
    }
    seq.setQltyArr(qlty);
    SeqArr.updateQltyInfo(info, seq);
    return true;
  }


  synchronized public static boolean loadQlty(BufferedReader from, Seq res)  throws IOException {
    ArrayList<Integer> buff = new ArrayList<Integer>();
    for (int countIdx = 0;  ; countIdx++) {
      String s = from.readLine();    log.dbg("#", countIdx).dbg(s);
      if (s == null)  {   // end of file
        byte[] qlty = ByteVec.toArray(buff.toArray());
        res.setQltyArr(qlty);
        return true;
      }
      s = FastaReader.removeCppComments(s);
      if (s.length() == 0) { // empty line marks the end
        byte[] qlty = ByteVec.toArray(buff.toArray());
        res.setQltyArr(qlty);
        return true;
      }
      if (!FastaQltyReader.appendInts(buff, s)) // collect sequences
        return false;
    }
  }

}
