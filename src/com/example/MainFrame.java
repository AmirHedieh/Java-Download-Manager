package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Main frame of the download manager
 * which contains several part such as toolbar and menu bar ...
 * @author Amirhossein Hediehloo
 */
public class MainFrame extends JFrame {

    private JPanel downloadMainPanel;
    private static int height = 0;
    private TrayIcon trayIcon;
    private SystemTray tray;

    //constructor
    public MainFrame(){
        this.setSize(991,707);// original size from main program
        addSystemTray();
        //BorderLayout frameLayout = new BorderLayout();
        //this.setResizable(false); //todo turn it on
        readSettingFile();
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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setUndecorated(true); //todo : add ur own close-minimize/maximize icons
        // this.setLocationRelativeTo(makeCategories());
        JComponent toolBar = makeToolbar();
        JComponent category = makeCategories();
        JComponent dlPanel = makeDownloadPanel();
        this.getContentPane().add(category, BorderLayout.WEST);
        this.getContentPane().add(toolBar,BorderLayout.NORTH);
        this.getContentPane().add(dlPanel,BorderLayout.CENTER);
       // this.getContentPane().setComponentZOrder(category,0);
        //this.getContentPane().setComponentZOrder(toolBar,1);

        addMenuBar();
        this.setVisible(true);
    }

    //methods

    /**
     * make a panel containing toolbar buttons like start, pause , remove, ...
     * @return JComponent (Here JPanel)
     */
    private JComponent makeToolbar(){ //todo : add Accelerator and Mnemonic
        JPanel toolPanel = new JPanel(); // a panel to put buttons on. with flow layout to put buttons in order form left side
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(15);
        toolPanel.setLayout(layout);
        toolPanel.setSize(799,64); //todo : modify or remove
        toolPanel.setLocation(200,0); //todo : modify or remove
        //toolPanel.setBorder(BorderFactory.createBevelBorder(1));//todo : modify or remove
        toolPanel.setBackground(Color.decode("#c8e2ba"));

        int numOfButtons = 10 ; // to change the num of buttons change thi value
        ArrayList<JButton> buttonList = new ArrayList<>(); // list of the buttons needed for toolbar. //todo: mabye it must be deleted
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
            if(name[i].equals("add")){
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        readSettingFile();
                        new AddDownload();
                        if(SettingFileInfo.getItems().addState == 1){
                             AddNewItemDownloadToFrame();
                            //String[] fileInfo = SettingFileInfo.getItems().fileInfo.split("<>"); //[0]-fileLink [1]-fileName
                            //downloadFile(downloadMainPanel,fileInfo[0],SettingFileInfo.getItems().saveDir + fileInfo[1]);

                        }
                    }

                });
            }

            else if(name[i].equals("play")){
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Play Pressed");
                    }
                });
            }

            else if(name[i].equals("pause")){
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Pause Pressed");
                    }
                });
            }

            else if(name[i].equals("remove")){
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("remove Pressed");
                    }
                });
            }

            else if(name[i].equals("settings")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Settings();
                    }
                });
            }
            buttonList.add(button);
            toolPanel.add(button);
            if(i == 0 || i == 3 || i == 4 || i == 5 || i == 8){ //todo: if u can add a  better seperator
                toolPanel.add(new JLabel("|"));
            }
        }
        //adding Search Bar in tool bar
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(170,30));
        searchBar.setText(" Enter File Name");
        searchBar.setBackground(Color.decode("#c8e2ba"));
        searchBar.setBorder(BorderFactory.createLineBorder(Color.gray));
        toolPanel.add(searchBar);
        return toolPanel;
        //todo : add line gap between different buttons like in original program
    }

    private JComponent makeCategories(){
        //setting panel
        //JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel,BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        //layout.
        panel.setSize(200,800); //todo : modify or remove
        panel.setLocation(0,0); //todo : modify or remove
        panel.setBorder(BorderFactory.createBevelBorder(1));//todo : modify or remove
        panel.setBackground(Color.decode("#32363f"));
        //gbc.gridheight = 1;
        //gbc.gridheight = 1;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
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
                    buttonName[i] = "Completed                       ";
                    fileAddress[i] = "Files//EagleGetIcons//completed.png";
                    toolTip[i] = "Completed Downloads";
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
            panel.add(button);
        }
        return panel;
    }

    private void addMenuBar(){ //adding Download and Help Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu downloadMenu = new JMenu("Download");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(downloadMenu);
        menuBar.add(helpMenu);
        JMenuItem add,play,pause,remove,sort,taskcleaner,vidsniffer,grabber,settings,exit;
        JMenuItem about,userGuide;
        add = new JMenuItem("Add New Download");
        play = new JMenuItem("Resume");
        pause = new JMenuItem("Pause");
        remove= new JMenuItem("Remove");
        sort = new JMenuItem("Sort");
        taskcleaner = new JMenuItem("Task Cleaner");
        vidsniffer = new JMenuItem("Video Sniffer");
        grabber = new JMenuItem("Media Grabber");
        settings = new JMenuItem("Settings");
        exit = new JMenuItem("Exit");
        about = new JMenuItem("About");
        userGuide = new JMenuItem("User Guide");
        downloadMenu.add(add); downloadMenu.add(play); downloadMenu.add(pause); downloadMenu.add(remove); downloadMenu.add(sort); downloadMenu.add(taskcleaner); downloadMenu.add(vidsniffer); downloadMenu.add(grabber);
        downloadMenu.add(settings); downloadMenu.add(exit);
        helpMenu.add(userGuide); helpMenu.add(about);
        this.setJMenuBar(menuBar);
    }

    private void readSettingFile(){
        File settingFile = new File("Files//Settings.txt");
        if (settingFile.exists()){
            //System.out.println("File found");
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(settingFile));
        } catch (IOException e1) {
            System.out.println("Can't Write setting File");
        }
        String input = null;
        try {
            input = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("Couldn't mak String");
        }
       // System.out.println(input);
        String[] splitString = input.split(" ");

        SettingFileInfo items = new SettingFileInfo();
        items.lookAndFeel = splitString[0];
        items.downloadLimit = splitString[1];
        items.saveDir = splitString[2];

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

    private void AddNewItemDownloadToFrame(){
        String[] fileInfo = SettingFileInfo.getItems().fileInfo.split("<>"); //[0]-fileLink [1]-fileName

        JPanel newDlPanel = new JPanel();
        newDlPanel.setLayout(null);
        newDlPanel.setSize(new Dimension(785,80));
        newDlPanel.setLocation(0,height);

        JLabel icon = new JLabel(new ImageIcon("Files//dlIcon.png"));
        icon.setSize(40,40);
        icon.setLocation(10,20);
        newDlPanel.add(icon);

        JLabel fileName = new JLabel(fileInfo[1]);
        JLabel fileLink = new JLabel(fileInfo[0]);
        fileName.setLocation(70,10);
        fileName.setSize(100,15);
        fileName.setForeground(Color.GRAY);
        newDlPanel.add(fileName);

        JProgressBar progressBar = new JProgressBar(0,100);
        progressBar.setSize(500,20);
        progressBar.setLocation(70,15);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        newDlPanel.add(progressBar);

        height += 81; //cause next download file location come to the before one
        newDlPanel.setBackground(Color.decode("#ffe1ad"));
        downloadMainPanel.add(newDlPanel);
        //revalidate();
        //setVisible(false);
        //setVisible(true);
        //System.out.println("Repainted");
         downloadFile(progressBar,fileInfo[0],SettingFileInfo.getItems().saveDir + fileInfo[1]);
        while (true){
            System.out.println("h");
            break;
        }
        SettingFileInfo.getItems().setAddState(0); // change the add state to primal state
        SettingFileInfo.getItems().checkContinue = 0;
        while (true){
            break;
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
        System.out.println("Copying \'" + src.getName() + "\' to \'" + dst.getName() + "\' ...");
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buffer = new byte[SI];
            int process = 0;
            while (in.available() > 0) {
                jb.setValue(process);
                int count = in.read(buffer);
                out.write(buffer, 0, count);
                //System.out.println(count + " bytes copied.");
                process++;
                repaint();
                revalidate();
            }
            System.out.println("Copy finished.\n");
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private JComponent makeDownloadPanel(){
        downloadMainPanel = new JPanel();
        //JScrollPane scrollPane = new JScrollPane();
        //scrollPane.setLocation(740,0);
        //scrollPane.setSize(20,550);
        //downloadMainPanel.add(scrollPane); //todo:add scroll
        downloadMainPanel.setLocation(200,64);
        downloadMainPanel.setSize(790,585);
        //downloadMainPanel.setPreferredSize(new Dimension(500,500));
        //BoxLayout layout = new BoxLayout(downloadMainPanel,BoxLayout.Y_AXIS);
        GridLayout layout = new GridLayout(0,1);
        downloadMainPanel.setLayout(null);
        downloadMainPanel.setBackground(Color.decode("#d8e8d7"));
        //downloadMainPanel.setBorder(BorderFactory.createBevelBorder(1));// todo: mabye must be removed
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
        WindowStateListener windowStateListener = new WindowStateListener() {
            public void windowStateChanged(WindowEvent windowEvent) {
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
            }
        };
        return windowStateListener;
    }


}
