package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * Main frame of the download manager
 * which contains several part such as toolbar and menu bar ...
 * @author Amirhossein Hediehloo
 */
public class MainFrame extends JFrame {

    private JPanel downloadMainPanel;

    private static int height = 0;
    private static int numOfDownloadItems = 0; //used to change downloadMainPanel layout when reaching max space
    private TrayIcon trayIcon;
    private SystemTray tray;
    private JPanel selectedDownload;
    private ArrayList<Download> list;
    private String listType;
    //constructor
    public MainFrame(){
        DataReader dataReader = new DataReader(); //loads data from saves at the launch of program
        addDataSaverToCloseOperation(); // add saving operation to close button

        this.setLocation(450,170); //location frame opens
        this.setSize(991,707);// original size from main program

        addSystemTray();

        Image image = Toolkit.getDefaultToolkit().getImage("Files//EagleGetIcons//icon.png");
        this.setIconImage(image);

        this.setResizable(false);

        try {
            setLook(); // set look and feel
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Wrong look and feel");
        } catch (InstantiationException e) {
            System.out.println("Instantiation Exception");
        } catch (IllegalAccessException e) {
            System.out.println("Illegal Access Exception");
        }

        this.getContentPane().setLayout(null);

        JComponent dlPanel = makeMainDownloadPanel();
        JComponent toolBar = makeToolbar();
        JComponent category = makeCategories();
        this.getContentPane().add(category);
        this.getContentPane().add(toolBar);
        this.getContentPane().add(dlPanel);

        addMenuBar();

        this.setVisible(true);
    }

    //methods

    /**
     * make a panel containing toolbar buttons like start, pause , remove, ...
     * @return JComponent (Here JPanel)
     */
    private JComponent makeToolbar(){
        JPanel toolPanel = new JPanel(); // a panel to put buttons on. with flow layout to put buttons in order form left side
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(15);
        toolPanel.setLayout(layout);
        toolPanel.setSize(785,50);
        toolPanel.setLocation(200,0);
        toolPanel.setBackground(Color.decode("#c8e2ba"));

        int numOfButtons = 10 ; // to change the num of buttons change thi value
        ArrayList<JButton> buttonList = new ArrayList<>(); // list of the buttons needed for toolbar.
        String[] fileAddress = new String[numOfButtons];
        String[] toolTip = new String[numOfButtons];
        String[] name = new String[numOfButtons];
        for(int i = 0 ; i < numOfButtons ; i++){ // make 2 Collection of Strings for use of opening Icon image and adding tool tip
            switch(i){
                case 0 :
                    fileAddress[i] = "Files//EagleGetIcons//add.png";
                    toolTip[i] = "Add New Task";
                    name[i] = "add";
                    break;
                case 1:
                    fileAddress[i] = "Files//EagleGetIcons//play.png";
                    toolTip[i] = "Start";
                    name[i] = "play";
                    break;
                case 2:
                    fileAddress[i] = "Files//EagleGetIcons//pause.png";
                    toolTip[i] = "Pause";
                    name[i] = "pause";
                    break;
                case 3:
                    fileAddress[i] = "Files//EagleGetIcons//remove.png";
                    toolTip[i] = "Remove an Existing Download";
                    name[i] = "remove";
                    break;
                case 4:
                    fileAddress[i] = "Files//EagleGetIcons//sort.png";
                    toolTip[i] = "Sort";
                    name[i] = "sort";
                    break;
                case 5:
                    fileAddress[i] = "Files//EagleGetIcons//taskcleaner.png";
                    toolTip[i] = "Task Cleaner";
                    name[i] = "TaskCleaner";
                    break;
                case 6:
                    fileAddress[i] = "Files//EagleGetIcons//vidsniffer.png";
                    toolTip[i] = "Video Sniffer";
                    name[i] = "VideoSniffer";
                    break;
                case 7:
                    fileAddress[i] = "Files//EagleGetIcons//grabber.png";
                    toolTip[i] = "Grabber";
                    name[i] = "grabber";
                    break;
                case 8:
                    fileAddress[i] = "Files//EagleGetIcons//batchdownload.png";
                    toolTip[i] = "Batch Download";
                    name[i] = "BatchDownload";
                    break;
                case 9:
                    fileAddress[i] = "Files//EagleGetIcons//settings.png";
                    toolTip[i] = "Settings";
                    name[i] = "settings";
                    break;
            }
        }
        for(int i = 0 ; i < numOfButtons ; i++){ // make button and add it to panel and list of buttons
            JButton button = new JButton();
            button.setIcon(new ImageIcon(fileAddress[i]));
            button.setToolTipText(toolTip[i]); // add a tool tip to each button
            button.setBackground(Color.decode("#c8e2ba")); // set button background same as panel color
            button.setBorder(BorderFactory.createEmptyBorder()); // remove the border of the button to make it looks like a flat image on panel
            //button.setIconTextGap(5);
            if(name[i].equals("add")){
                button.addActionListener(e -> addDownload());
            }

            else if(name[i].equals("play")){
                button.addActionListener(e -> System.out.println("Play Pressed"));
            }

            else if(name[i].equals("pause")){
                button.addActionListener(e -> System.out.println("Pause Pressed"));
            }

            else if(name[i].equals("remove")){
                button.addActionListener(e -> {
                    if(selectedDownload != null) {
                        if(listType.equals("download")){
                            downloadMainPanel.remove(selectedDownload);
                            //SettingFileInfo.getItems().removeFromDownloadList(selectedDownload);
                        }
                        System.out.println("remove Pressed");
                    }
                    else{
                        System.out.println("No Download Selected!");
                    }
                });
            }

            else if(name[i].equals("settings")) {
                button.addActionListener(e -> new Settings());
            }

            buttonList.add(button);
            toolPanel.add(button);
            if(i == 0 || i == 3 || i == 4 || i == 5 || i == 8){
                toolPanel.add(new JLabel("|"));
            }
        }
        //adding Search Bar in tool bar
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(170,30));
        searchBar.setText(" Enter File Name");
        searchBar.setForeground(Color.GRAY);
        searchBar.setBackground(Color.decode("#c8e2ba"));
        searchBar.setBorder(BorderFactory.createLineBorder(Color.gray));
        toolPanel.add(searchBar);
        return toolPanel;
        //todo : add line gap between different buttons like in original program
    }

    private JComponent makeCategories(){
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel,BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.setSize(200,800);
        panel.setLocation(0,0);
        panel.setBorder(BorderFactory.createBevelBorder(1));
        panel.setBackground(Color.decode("#32363f"));
        panel.add(new JLabel(new ImageIcon("Files//EagleGetIcons//logo.png")));

        int numOfButtons = 3; // to change number of buttons, change this value

        String[] buttonName = new String[numOfButtons];
        String[] fileAddress = new String[numOfButtons];
        String[] toolTip = new String[numOfButtons];

        for(int i = 0 ; i < numOfButtons ; i++) { // make 2 Collection of Strings for use of opening Icon image and adding tool tip
            switch (i) {
                case 0:
                    buttonName[i] = "Processing                      "; //todo: find a better way to make button in same size
                    fileAddress[i] = "Files//EagleGetIcons//processing.png";
                    toolTip[i] = "Downloads in Progress";
                    break;
                case 1:
                    buttonName[i] = "Removed                         ";
                    fileAddress[i] = "Files//EagleGetIcons//Completed.png";
                    toolTip[i] = "Removed Downloads";
                    break;
                case 2:
                    buttonName[i] = "Queue                              ";
                    fileAddress[i] = "Files//EagleGetIcons//queue.png";
                    toolTip[i] = "Queue";
                    break;
            }
        }
        //making buttons and set tool tip
        for(int i = 0 ; i < numOfButtons  ; i++){
            JButton button = new JButton(buttonName[i],new ImageIcon(fileAddress[i]));
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            panel.add(Box.createRigidArea(new Dimension(0,10)));// add gap in Y Axis
            button.setToolTipText(toolTip[i]); // add a tool tip to each button
            button.setForeground(Color.LIGHT_GRAY);
            button.setBackground(Color.decode("#32363f")); // set button background same as panel color
            button.setBorder(BorderFactory.createCompoundBorder()); // remove the border of the button to make it looks like a flat image on panel
            if(buttonName[i].equals("Processing                      ")){
                button.addActionListener(e -> {
                    System.out.println("Processing");
                    paintMainDlPanel(1);
                });

            }
            else  if(buttonName[i].equals("Removed                         ")){
                button.addActionListener(e -> {
                    System.out.println("removed");
                    paintMainDlPanel(2);
                });
            }
            else  if(buttonName[i].equals("Queue                              ")){
                button.addActionListener(e -> {
                    paintMainDlPanel(3);
                });
            }
            panel.add(button);
        }
        return panel;
    }

    private void addMenuBar(){ //adding Download and Help Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu downloadMenu = new JMenu("Download");
        downloadMenu.setMnemonic(KeyEvent.VK_D);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(downloadMenu);
        menuBar.add(helpMenu);
        JMenuItem add,play,pause,remove,sort,taskcleaner,vidsniffer,grabber,settings,exit;
        JMenuItem about,userGuide;
        add = new JMenuItem("Add New Download");
        KeyStroke ctrlAKeyStroke = KeyStroke.getKeyStroke("control A");
        add.setAccelerator(ctrlAKeyStroke);
        add.addActionListener(e -> addDownload());

        play = new JMenuItem("Resume");
        KeyStroke ctrlXKeyStroke = KeyStroke.getKeyStroke("shift P");
        play.setAccelerator(ctrlXKeyStroke);
        play.addActionListener(e -> System.out.println("Resume"));

        pause = new JMenuItem("Pause");
        KeyStroke ctrlPKeyStroke = KeyStroke.getKeyStroke("control P");
        pause.setAccelerator(ctrlPKeyStroke);
        pause.addActionListener(e -> System.out.println("Pause"));

        remove= new JMenuItem("Remove");
        KeyStroke ctrlRKeyStroke = KeyStroke.getKeyStroke("control R");
        remove.setAccelerator(ctrlRKeyStroke);
        remove.addActionListener(e -> System.out.println("Remove"));

        sort = new JMenuItem("Sort");
        KeyStroke ctrlOKeyStroke = KeyStroke.getKeyStroke("control O");
        sort.setAccelerator(ctrlOKeyStroke);
        sort.addActionListener(e -> System.out.println("Sort"));

        taskcleaner = new JMenuItem("Task Cleaner");
        KeyStroke ctrlTKeyStroke = KeyStroke.getKeyStroke("control T");
        taskcleaner.setAccelerator(ctrlTKeyStroke);
        taskcleaner.addActionListener(e -> System.out.println("Task Cleaner"));

        vidsniffer = new JMenuItem("Video Sniffer");
        KeyStroke ctrlVKeyStroke = KeyStroke.getKeyStroke("control V");
        vidsniffer.setAccelerator(ctrlVKeyStroke);
        vidsniffer.addActionListener(e -> System.out.println("Video Sniffer"));

        grabber = new JMenuItem("Media Grabber");
        KeyStroke ctrlMKeyStroke = KeyStroke.getKeyStroke("control M");
        grabber.setAccelerator(ctrlMKeyStroke);
        grabber.addActionListener(e -> System.out.println("Media Grabber"));

        settings = new JMenuItem("Settings");
        KeyStroke ctrlSKeyStroke = KeyStroke.getKeyStroke("control S");
        settings.setAccelerator(ctrlSKeyStroke);
        settings.addActionListener(e -> new Settings());

        exit = new JMenuItem("Exit");
        KeyStroke ctrlEKeyStroke = KeyStroke.getKeyStroke("control E");
        exit.setAccelerator(ctrlEKeyStroke);
        exit.addActionListener(e -> {
            exit(0);
            DataSaver saver = new DataSaver();
        });

        about = new JMenuItem("About");
        KeyStroke shiftAKeyStroke = KeyStroke.getKeyStroke("shift E");
        about.setAccelerator(shiftAKeyStroke);
        about.addActionListener(e -> showAbout());

        userGuide = new JMenuItem("User Guide");
        KeyStroke shiftHKeyStroke = KeyStroke.getKeyStroke("shift H");
        userGuide.setAccelerator(shiftHKeyStroke);
        userGuide.addActionListener(e -> showguide());

        downloadMenu.add(add); downloadMenu.add(play); downloadMenu.add(pause); downloadMenu.add(remove); downloadMenu.add(sort); downloadMenu.add(taskcleaner); downloadMenu.add(vidsniffer); downloadMenu.add(grabber);
        downloadMenu.add(settings); downloadMenu.add(exit);
        helpMenu.add(userGuide); helpMenu.add(about);
        this.setJMenuBar(menuBar);
    }


    public void setLook() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException { //read Setting file and update settings

        switch (SettingFileInfo.getItems().lookAndFeel){
            case "Metal":{
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                break;
            }
            case "Nimbus":{
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                break;
            }
            case "Windows":{
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                break;
            }
            case "WindowsClassic":{
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                break;
            }
            case "CDE/Motif":{
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                break;
            }
        }

    }

    /**
     * gets an int as type which determines that it must paint which one of panels( 1-download list 2-removed list 3-queue list)
     *then it paint a panel with these ArrayLists
     * @param type
     */
    private void paintMainDlPanel(int type){

        if(type == 1){
             list = SettingFileInfo.getItems().downloads;
             listType = "downloads";
        }
        else if(type == 2){
             list = SettingFileInfo.getItems().removed;
            listType = "removed";
        }
        else if(type == 3){
             list = SettingFileInfo.getItems().queue;
            listType = "queue";
        }

        downloadMainPanel.removeAll();
        repaint();

        for(int i = 0 ; i < list.size() ; i++){
            list.get(i).setNumberOfDownload(i);
            JPanel newDlPanel = new JPanel();
            newDlPanel.setLayout(null);
            newDlPanel.setBackground(Color.decode("#ffe1ad"));
           // newDlPanel.setSize(new Dimension(785,80));
            //newDlPanel.setLocation(0,height);

            JLabel icon = new JLabel(new ImageIcon("Files//dlIcon.png"));
            icon.setSize(40,40);
            icon.setLocation(10,20);
            newDlPanel.add(icon);

            File getSizeFile = new File(list.get(i).getLink());
            JLabel fileName = new JLabel( list.get(i).getName() + "     " + getSizeFile.length() / 1000 + " KB" );
            //JLabel fileLink = new JLabel(SettingFileInfo.getItems().downloads.get(i).link);
            fileName.setLocation(70,14);
            fileName.setSize(300,15);
            fileName.setForeground(Color.GRAY);
            newDlPanel.add(fileName);

            JProgressBar progressBar = new JProgressBar(0,100);
            progressBar.setSize(500,20);
            progressBar.setLocation(70,34);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            newDlPanel.add(progressBar);

            JButton open = new JButton(new ImageIcon("Files//open.png"));
            open.setSize(15,15);
            open.setBorder(BorderFactory.createEmptyBorder()); // remove the border of the button to make it looks like a flat image on panel
            open.setBackground(Color.decode("#ffe1ad"));
            open.setLocation(progressBar.getLocation().x + progressBar.getSize().width + 5,progressBar.getLocation().y);
            String savePath = SettingFileInfo.getItems().saveDir + list.get(i).getName();
            open.addActionListener(e -> {
                File file = new File(savePath);
                Desktop desktop = Desktop.getDesktop();
                if(file.exists()) {
                    try {
                        desktop.open(file);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            newDlPanel.add(open);
            newDlPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(selectedDownload != null) {
                        selectedDownload.setBackground(Color.decode("#ffe1ad"));
                    }
                    selectedDownload = newDlPanel;
                    newDlPanel.setBackground(Color.decode("#8dacef"));
                }
            });
           // height += 81; //cause next download file location come to the before one;
            downloadMainPanel.add(newDlPanel);

            repaint();
            String fileLink = list.get(i).getLink();
            new Thread(() -> {
                downloadFile(progressBar,fileLink,savePath);
            }).start();

            SettingFileInfo.getItems().setAddState(0); // change the add state to primal state
            SettingFileInfo.getItems().checkContinue = 0;
        }

        numOfDownloadItems++;
        if(numOfDownloadItems > 7){
           // downloadMainPanel.setLayout(new BoxLayout(downloadMainPanel,BoxLayout.Y_AXIS));
        }




    }

    public void downloadFile(JProgressBar jb, String srcPath , String dstPath){ //copy a file from src to dst path
        File src = new File(srcPath);
        File dst = new File(dstPath);
        revalidate();
        repaint();
        int SI = (int)src.length() / 100;
        if (!src.exists()) {
            System.out.println("Source file does not exist.");
            return;
        }
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buffer = new byte[SI];
            int process = 0;
            while (in.available() > 0) {
                jb.setValue(process);
                int count = in.read(buffer);
                out.write(buffer, 0, count);
                process++;
                repaint();
                revalidate();
            }
            dst.renameTo(src);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    /**
     * gets one panel and sets size and its position in the main frame
     */
    private JComponent makeMainDownloadPanel(){
        downloadMainPanel = new JPanel();
        downloadMainPanel.setLocation(200,50);
        downloadMainPanel.setSize(785,590);
        //downloadMainPanel.setLayout(new GridLayout(8,1,0,1));
        BoxLayout boxLayout = new BoxLayout(downloadMainPanel,BoxLayout.Y_AXIS);
        GridLayout gridLayout = new GridLayout(8,1,0,1);
        downloadMainPanel.setLayout(gridLayout);
        downloadMainPanel.setBackground(Color.decode("#d8e8d7"));
        return downloadMainPanel;
    }

    public boolean addSystemTray() {
        boolean result = false;
        if (SystemTray.isSupported()) {
            result = true;
            this.tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("Files//EagleGetIcons//icon.png");
            trayIcon = new TrayIcon(image,"EagleGet");
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setVisible(true);
                    setState(Frame.NORMAL);
                }
            });
            trayIcon.setImageAutoSize(true);
            addWindowStateListener(this.getWindowStateListener());
        }
        return result;


    }


    private WindowStateListener getWindowStateListener() {
        WindowStateListener windowStateListener = windowEvent -> {
            if (windowEvent.getNewState() == Frame.ICONIFIED) {
                try {
                    tray.add(trayIcon);
                    setVisible(false);
                } catch (AWTException ex) {
                    // System.out.println("unable to add to tray");
                }
            }
            if (windowEvent.getNewState() == Cursor.NE_RESIZE_CURSOR) {
                try {
                    tray.add(trayIcon);
                    setVisible(false);
                } catch (AWTException ex) {
                    // System.out.println("unable to add to system tray");
                }
            }
            if (windowEvent.getNewState() == Frame.MAXIMIZED_BOTH) {
                tray.remove(trayIcon);
                setVisible(true);
            }
            if (windowEvent.getNewState() == Frame.NORMAL) {
                tray.remove(trayIcon);
                setVisible(true);
            }
        };
        return windowStateListener;
    }

    private void addDownload(){
        //readSettingFile();
        new AddDownload();
        if(SettingFileInfo.getItems().addState == 1){
            paintMainDlPanel(1);

        }
    }

    private void showAbout(){
        JDialog frame = new JDialog();
        frame.setModal(true);
        frame.setSize(495,365);
        JLabel label = new JLabel();
        ImageIcon img = new ImageIcon("Files//About.png");
        label.setIcon(img);
        frame.add(label);
        frame.setVisible(true);
    }

    private void showguide(){
        JDialog frame = new JDialog();
        frame.setModal(true);
        frame.setSize(495,375);
        JLabel label = new JLabel();
        ImageIcon img = new ImageIcon("Files//guide.png");
        label.setIcon(img);
        frame.add(label);
        frame.setVisible(true);
    }


    /**
     * when closing program it calls DataSaver Class to save data
     */
    private void addDataSaverToCloseOperation(){
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                DataSaver saver = new DataSaver();
                System.exit(0);
            }
        });
    }

}
