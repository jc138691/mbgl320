package project.workflow.task;

import project.ModelDAO;

import javax.swingx.panelx.GridBagView;
import javax.swing.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 15:34:36
 */
abstract public class TaskOptView<T> extends GridBagView
  implements ModelDAO<T>
{
  public TaskOptView() {
    setBorder(BorderFactory.createEmptyBorder());
  }
  public TaskOptView(String title) {
    super(title);
  }
}