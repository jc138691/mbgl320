package io.fasta;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.iox.FileX;
import javax.swing.*;

import io.DnaFileOpt;

import java.io.*;

import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 12/10/2009, 1:36:02 PM
 */
public abstract class DnaFileReader<T> {
  public static Log log = Log.getLog(DnaFileReader.class);
  public T read(String fileType, TextView view, DnaFileOpt opt) {  //log.setDbg();
    String fileName = opt.getFileName();
    File file = FileX.openReadFile(fileName, view);
    if (file == null)
      return null;
    BufferedReader from;
    String taskInfo = "Importing "+fileType+" file ...";
    T res;
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
  public T readPage(String taskInfo
    , DnaFileOpt opt, BufferedReader from, TextView view) throws IOException {
    T res = makeResType();  // override
    int startIdx = opt.getPageOffsetIdx();
    int endIdx = startIdx + opt.getPageSize() - 1;

    TaskProgressMonitor monitor = null;
    if (taskInfo != null) {
      monitor = ProjectProgressMonitor.getInstance();
      monitor.setTitleText(taskInfo);
    }
    int monitorSize = endIdx+1;

    for (int idx = 0; idx <= endIdx; idx++) {
      if (idx == 5) {
        int dbg = 1;
      }
      if (idx > monitorSize)
        monitorSize *= 10;
      if (monitor != null && monitor.isCanceled(idx, 0, monitorSize)) {
        view.println("User canceled " + taskInfo);
        break;
      }
      if (!readOne(from, view, res))
        break;
      if (idx < startIdx) {
        res = makeResType(); // ignore
      }
//      else {
//        Contig last = res.getLast();
//        if (last != null)
//          last.setId(Integer.toCsv(idx+1));
//      }
    }
    return res;
  }
  public abstract  T makeResType();
  public abstract boolean readOne(BufferedReader from, TextView view, T resArr) throws IOException;
}
