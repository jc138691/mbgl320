package project;
import project.ucm.UCSelectLookFeel;
import project.ucm.AdapterUCCToAL;

import javax.swing.*;
import javax.swingx.buttonx.ButtonUtils;
import java.awt.event.*;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 13:56:49
 */
public class ProjectFrame extends JFrame {
  private static String IMAGE_22x22_DIR = "images/22x22/";
  private static String IMAGE_16x16_DIR = "images/16x16/";
//  private static String IMAGE_22x22_DIR = "images/22x22/";
//  private static String IMAGE_16x16_DIR = "images/16x16/";
  static private ProjectFrame instance = null;
  private final int MIN_WIDTH = 640;
  private final int MIN_HEIGHT = 480;
  static public ProjectFrame getInstance() {
    return instance;
  }
  static public void setInstance(ProjectFrame v) {
    instance = v;
  }
  protected ProjectFrame(Project model) {
    init();
    setTitle(model.getAppName() + " " + model.getAppVersion());
    setJMenuBar(new JMenuBar());
  }
  public void showSmallScreen() {
    pack();
    setSize(new Dimension(300, 300));
    setVisible(true);
  }

  public void showFullScreen() {
    Dimension screen = getToolkit().getScreenSize();
    pack();
//    frame.setSize(new Dimension(300, 300));
    setSize(screen);
    setVisible(true);
    // from http://www.rgagnon.com/javadetails/java-0222.html
    GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    if (device.isFullScreenSupported() ) {
      device.setFullScreenWindow(this);
    }
  }
  protected JMenu addMenuFile() {
    JMenu menu = new JMenu("File");
    getJMenuBar().add(menu);
    menu.setMnemonic(KeyEvent.VK_F);
    return menu;
  }
  protected JMenu addMenuHelp() {
    JMenu menu = new JMenu("Help");
    getJMenuBar().add(menu);
    menu.setMnemonic(KeyEvent.VK_H);
    return menu;
  }
  protected JMenu addMenuSettings() {
    JMenu menu = new JMenu("Settings");
    getJMenuBar().add(menu);
    menu.setMnemonic(KeyEvent.VK_S);
    return menu;
  }
  protected JMenu addOpenMenu(JMenu to) {
    JMenu menu = new JMenu("Open");
    to.add(menu);
    menu.setMnemonic(KeyEvent.VK_O);
    return menu;
  }
  protected JMenu addImportMenu(JMenu to) {
    JMenu menu = new JMenu("Import");
//    menu.setToolTipText("Import data set/table");
    to.add(menu);
    menu.setMnemonic(KeyEvent.VK_I);
    return menu;
  }
  protected JMenu addCalcMenu(JMenu to) {
    JMenu menu = new JMenu("Calculate");
    to.add(menu);
    menu.setMnemonic(KeyEvent.VK_C);
    return menu;
  }
  protected JMenu addMenuTools() {
    JMenu menu = new JMenu("Admin");
    getJMenuBar().add(menu);
    menu.setMnemonic(KeyEvent.VK_T);
    return menu;
  }
  private void init() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addComponentListener(new ComponentListener() {
      public void componentResized(ComponentEvent e) {
        int width = getWidth();
        int height = getHeight();
        boolean resize = false;
        if (width < MIN_WIDTH) {
          resize = true;
          width = MIN_WIDTH;
        }
        if (height < MIN_HEIGHT) {
          resize = true;
          height = MIN_HEIGHT;
        }
        if (resize) {
          setSize(width, height);
        }
      }
      public void componentMoved(ComponentEvent e) {
      }
      public void componentShown(ComponentEvent e) {
      }
      public void componentHidden(ComponentEvent e) {
      }
    });
  }
  protected void addMenuLookFeel(JMenu menu, Project model) {
    JRadioButtonMenuItem defaultLook = new JRadioButtonMenuItem("System default");
    JRadioButtonMenuItem crossLook = new JRadioButtonMenuItem("Cross platform");
    defaultLook.addActionListener(new AdapterUCCToAL(
      new UCSelectLookFeel(this, model, model.SYSTEM_LOOK)));
    crossLook.addActionListener(new AdapterUCCToAL(
      new UCSelectLookFeel(this, model, model.CROSS_PLATFORM_LOOK)));
    defaultLook.setMnemonic(KeyEvent.VK_S);
    JMenu look = new JMenu("Look and Feel");
    look.add(defaultLook);
    ButtonGroup lookGroup = new ButtonGroup();
    lookGroup.add(defaultLook);
    crossLook.setMnemonic(KeyEvent.VK_C);
    look.add(crossLook);
    lookGroup.add(crossLook);

//    metal.setMnemonic(KeyEvent.VK_M);
//    look.addLine(metal);
//    lookGroup.addLine(metal);
    look.setMnemonic(KeyEvent.VK_L);
    menu.add(look);
    if (model.getLookFeel().equals(ProjectModel.CROSS_PLATFORM_LOOK)) {
      crossLook.setSelected(true);
      crossLook.doClick();
    } else {
      defaultLook.setSelected(true);
      defaultLook.doClick();
    }
  }
  protected JMenuItem addMenuAbout(JMenu menu) {
    JMenuItem menuItem = new JMenuItem("About");
    menuItem.setMnemonic(KeyEvent.VK_A);
    menu.add(menuItem);
    return menuItem;
  }
  public static ImageIcon loadSaveAsFileIcon() {
    return loadImageIcon(IMAGE_22x22_DIR + "document-save-as.png");
  }
  public static ImageIcon loadSaveAsFileIcon16() {
    return loadImageIcon(IMAGE_16x16_DIR + "document-save-as.png");
  }
  public static ImageIcon loadGoDown16() {
    return loadImageIcon(IMAGE_16x16_DIR + "go-down.png");
  }
  public static ImageIcon loadGoBottom16() {
    return loadImageIcon(IMAGE_16x16_DIR + "go-bottom.png");
  }
  public static ImageIcon loadGoUp16() {
    return loadImageIcon(IMAGE_16x16_DIR + "go-up.png");
  }
  public static ImageIcon loadGoTop16() {
    return loadImageIcon(IMAGE_16x16_DIR + "go-top.png");
  }


  public static ImageIcon loadGoFirst16() {
    return loadImageIcon(IMAGE_16x16_DIR + "go-first.png");
  }
  public static ImageIcon loadGoNext16() {
    return loadImageIcon(IMAGE_16x16_DIR + "go-next.png");
  }
  public static ImageIcon loadGoPrev16() {
    return loadImageIcon(IMAGE_16x16_DIR + "go-previous.png");
  }
  public static ImageIcon loadGoLast16() {
    return loadImageIcon(IMAGE_16x16_DIR + "go-last.png");
  }

  public static ImageIcon loadEditFindIcon16() {
    return loadImageIcon(IMAGE_16x16_DIR + "edit-find.png");
  }
  public static ImageIcon loadSaveFileIcon() {
//    return loadImageIcon("images/save.gif"); // "images/save.gif" <- correct formatLog; DO NOT use File.separator
    return loadImageIcon(IMAGE_22x22_DIR + "document-save.png");
  }
  public static ImageIcon loadOpenFileIcon() {
    return loadImageIcon(IMAGE_22x22_DIR + "document-open.png");
//    return loadImageIcon("images/open.gif");
  }
  public static ImageIcon loadNewFileIcon() {
    return loadImageIcon(IMAGE_22x22_DIR + "document-new.png");
  }
  public static ImageIcon loadOpenHelpIcon() {
//    return loadImageIcon("images/help.gif");
    return loadImageIcon(IMAGE_22x22_DIR + "help-browser.png");
  }
  public static ImageIcon loadImageIcon(String fileName) {
    ImageIcon icon = null;
    java.net.URL url = ProjectFrame.class.getResource(fileName);
    Image img = null;
    if (url != null)
      img = Toolkit.getDefaultToolkit().getImage(url);
    if (img != null)
      icon = new ImageIcon(img);
    return icon;



//    ImageIcon icon = null;
//    File file = new File(fileName);
//    if (file.exists())
//      icon = new ImageIcon(fileName);
//    else {
//      // try loading from a jar file
//      java.net.URL url = ProjectFrame.class.getResource(fileName);
//      Image img = null;
//      if (url != null)
//        img = Toolkit.getDefaultToolkit().getImage(url);
//      if (img != null)
//        icon = new ImageIcon(img);
//    }
//    return icon;
  }


  public static JButton makeLocateFileButton() {
    ImageIcon icon = loadOpenFileIcon();
    JButton bttn;
    if (icon != null)
      bttn = new JButton(icon);
    else
      bttn = new JButton("...");
    bttn = makeToolButton(bttn);
    bttn.setToolTipText("Locate file ...");
    bttn.setPreferredSize(new Dimension(22, 22));
    return bttn;
  }
  public static JButton makeHelpButton() {
    ImageIcon icon = loadOpenHelpIcon();
    JButton bttn;
    if (icon != null) {
      bttn = new JButton(icon);
    }
    else
      bttn = new JButton("help");
    bttn = makeToolButton(bttn);
    bttn.setPreferredSize(new Dimension(25, 15));
    return bttn;
  }
  public static JButton makeToolButton(JButton button) {
    return ButtonUtils.makeToolButton(button);
  }
  public static JButton makeNavButtons(JButton button) {
    button.setPreferredSize(new Dimension(20, 20));
    return ButtonUtils.makeFlat(button);
  }
  protected void addMenuViewErrors(JMenu menu) {
    JMenuItem menuItem = new JMenuItem("Error log");
    menuItem.setToolTipText("View error log");

//    menuItem.addActionListener(new AdapterUCCToALThread(
//      new UCViewOutputStream(ProjectLogger.getInstance().getLineStream())));
//    menuItem.setMnemonic(KeyEvent.VK_E);
    menu.add(menuItem);
  }
}
