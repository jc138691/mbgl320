package olga;

import project.ProjectFrame;
import project.Project;
import project.workflow.task.DefaultTaskUI;
import project.ucm.AdapterUCCToAL;
import project.ucm.AdapterUCCToALThread;
import project.ucm.UCRunGC;

import javax.utilx.log.Log;
import javax.utilx.log.PStreamToTextView;
import javax.swing.*;
import javax.swingx.textx.TextView;
import javax.swingx.progress.UCMonitorTaskProgress;
import javax.iox.SplitPStream;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.PrintStream;

import ucm.project.UCShowHelpAboutSnpStation;
import ucm.project.UCResetProject;
import ucm.project.UCOpenProject;
import ucm.project.UCSaveProject;
import ucm.io.UCShowImportOptUI;
import ucm.io.UCImportFiles;
import io.input.OlgaImportOptUI;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:22:44.
 */
public class OlgaFrame extends ProjectFrame {
  public static Log log = Log.getLog(OlgaFrame.class);

  public OlgaFrame(Project model) {
    super(model);
    setInstance(this);
    init();

    setupToolBar();               // TOOLBAR

    JMenu menu = addMenuFile();   // MENU | FILE
//    JMenu menu2 = addImportMenu(menu);
//    menu2.setToolTipText("Import data set");
//    addMenuImportData(menu2);
    setupMenuFile(menu);

//    menu.addSeparator();
    menu = addMenuSettings();      // MENU | SETTINGS
    addMenuLookFeel(menu, model);

//    menu = addMenuTools();         // MENU | TOOLS
//    addMenuRunGC(menu);
//    addMenuViewErrors(menu);

    menu = addMenuHelp();          // MENU | HELP
    JMenuItem helpAbout = addMenuAbout(menu);
    helpAbout.addActionListener(new UCShowHelpAboutSnpStation(this));
  }

  private void setupMenuFile(JMenu menuFile) {
//    menuFile.removeAll();
    ImageIcon icon = null;
    JMenuItem item = null;
    String help = null;

//    help = "Save Results As ...";
//    item = new JMenuItem(help);
//    item.addActionListener(new AdapterUCCToAL(new UCSaveResults()));
//    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
//    item.setMnemonic(KeyEvent.VK_A);
//    menuFile.addContig(item);
//    menuFile.addSeparator();

    help = "Import ...";
    item = new JMenuItem(help);
    item.setToolTipText("Show import options ...");
    item.addActionListener(new AdapterUCCToAL(new UCShowImportOptUI()));
    item.setMnemonic(KeyEvent.VK_I);
    menuFile.add(item);

//    help = "Export ...";
//    item = new JMenuItem(help);
//    item.setToolTipText("Show export options ...");
//    item.addActionListener(new AdapterUCCToAL(new UCShowExportOptUI()));
//    item.setMnemonic(KeyEvent.VK_E);
//    menuFile.add(item);
//    menuFile.addSeparator();

    help = "New Project ...";
    item = new JMenuItem(help);
    item.addActionListener(new AdapterUCCToAL(new UCResetProject()));
    item.setMnemonic(KeyEvent.VK_R);
    menuFile.add(item);

    help = "Open Project ...";
    item = new JMenuItem(help);
    item.addActionListener(new AdapterUCCToAL(new UCOpenProject()));
    item.setMnemonic(KeyEvent.VK_O);
    menuFile.add(item);

    help = "Save Project As ...";
    item = new JMenuItem(help);
    item.addActionListener(new AdapterUCCToAL(new UCSaveProject()));
    item.setMnemonic(KeyEvent.VK_P);
    menuFile.add(item);
    menuFile.addSeparator();

    JMenuItem fileExit = new JMenuItem("Exit");
    fileExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });

    //fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
    fileExit.setMnemonic(KeyEvent.VK_X);
    menuFile.add(fileExit);
    menuFile.setMnemonic(KeyEvent.VK_F);
  }
  private void init() {
    ImageIcon icon = loadImageIcon("images/dmitry_logo_16.gif");
//    ImageIcon icon = loadImageIcon("/kingroup/images/dmitry_logo_16.gif");
    if (icon != null)
      this.setIconImage(icon.getImage());

    // from http://www.j3d.org/tutorials/quick_fix/swing.html
    // Swing is lightweight and J3D is heavy weight.
    // This means that a Canvas3D will draw on top of Swing objects no matter what order Swing thinks it should draw in.
//    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    OlgaMainUI ui = OlgaMainUI.getInstance();
    setLayout(new BorderLayout());
    add(ui, BorderLayout.CENTER);

    // setup and show CONSOLE
    TextView view = new TextView();
    ui.setConsoleView(view);
    PrintStream out = new PStreamToTextView(view);

    SplitPStream split = new SplitPStream(System.out);
    split.add(out);
    System.setOut(split);
    Log.addGlobal(split);

    split = new SplitPStream(System.err);
    split.add(out);
    System.setErr(split);

    log.info("Test: OlgaFrame.log.info()");
    System.out.println("Test: System.out.println()");
    System.err.println("Test: System.err.println()");

    String welcome = "Welcome to OlgaHelp.";
    log.info(welcome);
    ui.setStatusWithTime(welcome);

    // setup and show EXPORT
    SnpStation project = SnpStationProject.getInstance();
//    OlgaExportOptView exOptView = new OlgaExportOptView(project);
//    DefaultTaskUI<SnpStation> taskUI = new DefaultTaskUI<SnpStation>(exOptView, OlgaFrame.getInstance());
//    ui.setExportUI(taskUI);

    // setup and show INPUT
    OlgaImportOptUI imOptView = new OlgaImportOptUI(project);
    DefaultTaskUI<SnpStation> taskUI = new DefaultTaskUI<SnpStation>(imOptView, OlgaFrame.getInstance(), JSplitPane.VERTICAL_SPLIT);
    taskUI.setApplyAction(new UCMonitorTaskProgress<SnpStation>(new UCImportFiles(taskUI)));
    ui.setImportUI(taskUI);
  }

  private void addMenuRunGC(JMenu menu) {
    JMenuItem menuItem = new JMenuItem("Garbage");
    menuItem.setToolTipText("Run garbage collector");
    menuItem.addActionListener(new AdapterUCCToALThread(new UCRunGC()));
//    menuItem.setMnemonic(KeyEvent.VK_G);
    menu.add(menuItem);
  }


  private void setupToolBar() {
    JToolBar toolBar = new JToolBar();
    JButton button = null;
    ImageIcon icon = null;

//    button = new JButton("NewPrj");
//    icon = loadNewFileIcon();
//    if (icon != null) {
//      button = new JButton(icon);
//    }
//    button = makeToolButton(button);
//    button.setToolTipText("New Project ...");
//    toolBar.addContig(button);
//    toolBar.addSeparator();

    button = new JButton("Import");
//    icon = loadImageIcon("images/document-open.png");
    icon = loadOpenFileIcon();
    if (icon != null) {
      button = new JButton(icon);
    }
    button = makeToolButton(button); // OPEN
    button.addActionListener(new AdapterUCCToAL(new UCShowImportOptUI()));
    button.setToolTipText("Show import options ...");
    toolBar.add(button);

    // SAVE PROJECT
//    button = new JButton("Export");
//    icon = loadSaveAsFileIcon();
//    if (icon != null) {
//      button = new JButton(icon);
//    }
//    button = makeToolButton(button); // SAVE
//    button.addActionListener(new AdapterUCCToAL(new UCShowExportOptUI()));
//    button.setToolTipText("Show export options ...");
//    toolBar.add(button);

    toolBar.addSeparator();

//    // SAVE
//    button = new JButton("SaveResults");
//    icon = loadSaveFileIcon();
//    if (icon != null) {
//      button = new JButton(icon);
//    }
//    button = makeToolButton(button); // SAVE
//    button.addActionListener(new AdapterUCCToALThread(new UCSaveResults()));
//    button.setToolTipText("Save results ...");
//    toolBar.addContig(button);

//    button = addToolButton(fileNameGroups_, "Kin groups", true);
//    button.addActionListener(new DisplayPartitionGuiHnd() );
//    button.setToolTipText("PartitionSequence into kin groups");
//    button.setEnabled(false);
//    kingroups_ = button;
    add(toolBar, BorderLayout.NORTH);
  }
  private JMenu addMenuScattering() {
    JMenu menu = new JMenu("Scattering");
    getJMenuBar().add(menu);
    menu.setMnemonic(KeyEvent.VK_S);
    return menu;
  }
  private JMenu addMenuJMtrx(JMenu to) {
    JMenu menu = new JMenu("J-Matrix ...");
    to.add(menu);
    menu.setMnemonic(KeyEvent.VK_J);
    return menu;
  }
//  private void addMenuJMPotR(JMenu menu) {
//    JMenuItem menuItem = new JMenuItem("Potential");
//    menuItem.setToolTipText("Potential Scattering");
//    menuItem.addActionListener(new AdapterUCCToALThread(new UCShowJmPotR_UI()));
//    menuItem.setMnemonic(KeyEvent.VK_P);
//    menu.addContig(menuItem);
//  }
}

