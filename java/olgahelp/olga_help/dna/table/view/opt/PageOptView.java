package dna.table.view.opt;

import javax.utilx.log.Log;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.panelx.GridBagView;
import javax.swing.*;

import dna.table.PageOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 03/04/2009, 5:00:19 PM
 */
public class PageOptView extends GridBagView
//public class PageOptView extends TaskOptView<SnpStation>
{
  public static Log log = Log.getLog(BaseFontOptView.class);
  private JLabel pageSizeLbl;
  private IntField pageSize;
  private static final int INT_SIZE = 5;

  public PageOptView(PageOpt model) {
    super("Page");
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    pageSizeLbl = new JLabel("Size");
    pageSize = new IntField(INT_SIZE, 1, 100000);
    pageSize.setToolTipText("Number of rows in the viewing page.");
  }
  private void assemble() {
    startRow(pageSizeLbl);    endRow(pageSize);
  }
  public void loadTo(PageOpt opt) {
//    PageOpt opt = model.getPageProxy();
    opt.setPageSize(pageSize.getInput());
  }
  public void loadFrom(PageOpt opt) {
//    PageOpt opt = model.getPageProxy();
    pageSize.setValue(opt.getPageSize());
  }
}