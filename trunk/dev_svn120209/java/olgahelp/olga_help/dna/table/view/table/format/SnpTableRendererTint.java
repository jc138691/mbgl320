package dna.table.view.table.format;

import dna.snp.SnpOpt;
import dna.contig.Contig;

import java.awt.*;

import javax.swing.table.TableCellRenderer;
import javax.awtx.ColorTint;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/05/2009, 4:05:48 PM
 */
public class SnpTableRendererTint extends SnpTableRendererColor {

  public SnpTableRendererTint(Contig contig, SnpOpt opt, TableCellRenderer parent) {
    super(contig, opt, parent);
  }
  protected void renderCell(Component cell, Color color) {
    Color curr = cell.getBackground();
    curr = ColorTint.tint(curr, color);
    cell.setBackground(curr);
  }
}