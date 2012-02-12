package qm_station;
import project.Project;
import project.ProjectFrame;
import project.ucm.AdapterUCCToAL;
import project.ucm.UCRunGC;
import project.ucm.AdapterUCCToALThread;

import javax.swing.*;
import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.iox.SplitPStream;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.PrintStream;

import qm_station.ucm.project.*;
import qm_station.ucm.UCShowJmPotR_UI;
import qm_station.ucm.UCShowJmPotLCR_UI;

import javax.utilx.log.PStreamToTextView;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 15:30:27
 */
public class QMSFrame extends ProjectFrame {
  public static Log log = Log.getLog(QMSFrame.class);

  public QMSFrame(Project model) {
    super(model);
    setInstance(this);
    init();

    setupToolBar();               // TOOLBAR

    JMenu menu = addMenuFile();   // MENU | FILE
//    JMenu menu2 = addImportMenu(menu);
//    menu2.setToolTipText("Import data set");
//    addMenuImportData(menu2);
    setupMenuFile(menu);

    menu = addMenuScattering();      // MENU | Scattering
    menu = addMenuJMtrx(menu);
    addMenuJMPotR(menu);
    addMenuJMPotLCR(menu);

//    menu.addSeparator();
    menu = addMenuSettings();      // MENU | SETTINGS
    addMenuLookFeel(menu, model);

    menu = addMenuTools();         // MENU | TOOLS
//    menu2 = addMenuTables(menu);
    addMenuRunGC(menu);
    addMenuViewErrors(menu);

    menu = addMenuHelp();          // MENU | HELP
    JMenuItem helpAbout = addMenuAbout(menu);
    helpAbout.addActionListener(new UCShowHelpAboutQMStation(this));
  }


  private void setupMenuFile(JMenu menuFile) {
//    menuFile.removeAll();
    ImageIcon icon = null;

    String help = "Save Results As ...";
    JMenuItem item = new JMenuItem(help);
    item.addActionListener(new AdapterUCCToAL(new UCSaveResults()));
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
    item.setMnemonic(KeyEvent.VK_A);
    menuFile.add(item);
    menuFile.addSeparator();

    help = "Reset Project ...";
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
//    ImageIcon icon = loadImageIcon("/tsvlib/project/images/dm.gif");
    ImageIcon icon = loadImageIcon("images/dmitry_logo_16.gif");
//    ImageIcon icon = loadImageIcon("/kingroup/images/dmitry_logo_16.gif");
    if (icon != null)
      this.setIconImage(icon.getImage());

    // from http://www.j3d.org/tutorials/quick_fix/swing.html
    // Swing is lightweight and J3D is heavy weight.
    // This means that a Canvas3D will draw on top of Swing objects no matter what order Swing thinks it should draw in.
//    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    QMSMainUI ui = QMSMainUI.getInstance();
    setLayout(new BorderLayout());
    add(ui, BorderLayout.CENTER);

    //
    TextView view = new TextView();
    ui.setOutView(view);
    PrintStream out = new PStreamToTextView(view);

    SplitPStream split = new SplitPStream(System.out);
    split.add(out);
    System.setOut(split);
    Log.addGlobal(split);

    split = new SplitPStream(System.err);
    split.add(out);
    System.setErr(split);

    log.info("Test: QMSFrame.log.info()");
    System.out.println("Test: System.out.println()");
    System.err.println("Test: System.err.println()");

    String welcome = "Welcome to Quantum Mechanics (QM)-Station.";
    log.info(welcome);
    ui.setStatusWithTime(welcome);

    // http://java.sun.com/docs/books/tutorial/uiswing/examples/misc/InputVerificationDemoProject/src/misc/InputVerificationDemo.java
    // Turn off metal's use of bold fonts 
    UIManager.put("swing.boldMetal", Boolean.FALSE);
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

    //button = addToolButton(fileNameLayout_, "Layout", false); // Apply
    //button.addActionListener(new KCtrDisplayKinshipFileFormatGui(mainView_) ); // open and display
    //button.setToolTipText(kinshipLayoutHelp_);

    button = new JButton("NewPrj");
    icon = loadNewFileIcon();
    if (icon != null) {
      button = new JButton(icon);
    }
    button = makeToolButton(button);
    button.setToolTipText("New Project ...");
    toolBar.add(button);

    button = new JButton("OpenPrj");
//    icon = loadImageIcon("images/document-open.png");
    icon = loadOpenFileIcon();
    if (icon != null) {
      button = new JButton(icon);
    }
    button = makeToolButton(button); // OPEN
    button.addActionListener(new AdapterUCCToAL(new UCOpenProject()));
    button.setToolTipText("Open Project ...");
    toolBar.add(button);

    // SAVE PROJECT
    button = new JButton("SaveProjectAs");
    icon = loadSaveAsFileIcon();
    if (icon != null) {
      button = new JButton(icon);
    }
    button = makeToolButton(button); // SAVE
    button.addActionListener(new AdapterUCCToAL(new UCSaveProject()));
    button.setToolTipText("Save Project As ...");
    toolBar.add(button);

    toolBar.addSeparator();

    // SAVE
    button = new JButton("SaveResults");
    icon = loadSaveFileIcon();
    if (icon != null) {
      button = new JButton(icon);
    }
    button = makeToolButton(button); // SAVE
    button.addActionListener(new AdapterUCCToALThread(new UCSaveResults()));
    button.setToolTipText("Save results ...");
    toolBar.add(button);

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
  private void addMenuJMPotR(JMenu menu) {
    JMenuItem menuItem = new JMenuItem("Potential");
    menuItem.setToolTipText("Potential Scattering");
    menuItem.addActionListener(new AdapterUCCToALThread(new UCShowJmPotR_UI()));
    menuItem.setMnemonic(KeyEvent.VK_P);
    menu.add(menuItem);
  }
  private void addMenuJMPotLCR(JMenu menu) {
    JMenuItem menuItem = new JMenuItem("Potential via LogCR");
    menuItem.setToolTipText("Potential Scattering using the LogCR transformation");
    menuItem.addActionListener(new AdapterUCCToALThread(new UCShowJmPotLCR_UI()));
    menuItem.setMnemonic(KeyEvent.VK_P);
    menu.add(menuItem);
  }
}

