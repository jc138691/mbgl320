package ucm.seq.arr.view;

import dna.table.PageOpt;
import dna.contig.view.ContigArrUI;
import dna.contig.view.ContigArrView;
import olga.OlgaMainUI;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 22/09/2009, 2:02:50 PM
 */
public class UCShowPageSeqArr extends UCShowPage {
  public UCShowPageSeqArr(int action, PageOpt page) {
    super(action, page, OlgaMainUI.getInstance().getSeqArrUI());
  }
  public boolean run() {
    super.run();

    throw new RuntimeException("TODO: UCShowPageSeqArr.run()");
    // TODO!!!
//    ContigArrUI ui = OlgaMainUI.getInstance().getContigArrUI();
//    if (ui != null) {
//      ContigArrView view = ui.getContigTableView();
//      if (view != null)
//        view.setViewPos(new Point(0, 0));
//    }
//    return new UCRefreshContigTableView().run();
  }
}