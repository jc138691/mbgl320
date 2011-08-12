package ucm.seq.arr.view;

import dna.table.PageOpt;
import dna.contig.view.ContigArrUI;
import dna.contig.view.ContigArrView;
import olga.OlgaMainUI;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 22/09/2009, 1:59:24 PM
 */
public class UCShowPageContig extends UCShowPage {
  public UCShowPageContig(int action, PageOpt page) {
    super(action, page, OlgaMainUI.getInstance().getContigArrUI());
  }
  public boolean run() {
    super.run();

    ContigArrUI ui = OlgaMainUI.getInstance().getContigArrUI();
    if (ui != null) {
      ContigArrView view = ui.getContigTableView();
      if (view != null)
        view.setViewPos(new Point(0, 0));
    }
    return new UCRefreshContigTableView().run();
  }
}
