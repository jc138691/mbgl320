package io.input;

import olga.SnpStation;
import project.workflow.task.TaskOptView;
import project.ProjectFrame;

import javax.swing.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/03/2009, 2:19:15 PM
 */
public abstract class FileSelectOptView extends TaskOptView<SnpStation> {
  protected static final int FILE_NAME_LEN = 40;
  protected static final int EXT_LEN = 4;
//  protected static final int EXT_SIZE = 36;
  protected JTextField fileName;
  protected JTextField fileExt;
  protected JButton selectBttn;
  public FileSelectOptView() {
    init();
  }

  private void init() {
    fileName = new JTextField(FILE_NAME_LEN);
    fileExt = new JTextField(EXT_LEN);
    selectBttn = ProjectFrame.makeLocateFileButton();
  }

}