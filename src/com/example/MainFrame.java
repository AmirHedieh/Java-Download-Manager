package com.example;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;

import static java.lang.System.exit;

/**
 * Main frame of the download manager
 * which contains several part such as toolbar and menu bar ...
 * @author Amirhossein Hediehloo
 */
public class MainFrame extends JFrame {

    private JPanel downloadMainPanel;
    private ExecutorService executor; // thread pool to manage downloads
    private ArrayList<WorkerThread> allWorkers = new ArrayList<>();
    private TrayIcon trayIcon;
    private SystemTray tray;
    private JPanel selectedDownload;
    ArrayList<Download> matchedSearches = new ArrayList<>();
    private String listType;
    //constructor
    public MainFrame(){
        DataReader dataReader = new DataReader(); //loads data from saves at the launch of program
        int cores;

        if(SettingFileInfo.getItems().downloadLimit.equals("Unlimited")){
            cores = Runtime.getRuntime().availableProcessors(); //using all available cores for downloading
        }
        else {
            cores = Integer.parseInt(SettingFileInfo.getItems().downloadLimit);
        }
        executor = Executors.newFixedThreadPool(cores);//creating a pool of downloadLimitSize threads

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

        paintMainDlPanel(1); //when program starts from a save the first panel that is shown is downloads Panel
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
                    toolTip[i] = "Sort-Right Click";
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
                    toolTip[i] = "Queue";
                    name[i] = "queue";
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
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(selectedDownload != null) {
                            String selectedName = selectedDownload.getName();
                            int num = Integer.parseInt(selectedName) ;
                            System.out.println(num);
                            for (int i = 0 ; i < Integer.parseInt(selectedName) ; i++){
                                if(!SettingFileInfo.getItems().downloads.get(i).isInProgress()){
                                    num--;
                                }
                            }
                            allWorkers.get(num).setPause(false);
                        }
                    }
                });
            }

            else if(name[i].equals("pause")){
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            if(selectedDownload != null) {
                                String selectedName = selectedDownload.getName();
                                int num = Integer.parseInt(selectedName) ;
                                System.out.println(num);
                                for (int i = 0 ; i < Integer.parseInt(selectedName) ; i++){
                                    if(!SettingFileInfo.getItems().downloads.get(i).isInProgress()){
                                        num--;
                                    }
                                }
                                    allWorkers.get(num).setPause(true);
                            }
                    }
               });
            }

            else if(name[i].equals("remove")){
                button.addActionListener(e -> {
                    if(selectedDownload != null) {
                        if(listType.equals("downloads")){
                            String selectedName = selectedDownload.getName();
                            int num = Integer.parseInt(selectedName);
                            SettingFileInfo.getItems().addToRemovedList(SettingFileInfo.getItems().downloads.get(num));
                            SettingFileInfo.getItems().removeFromDownloadList(num);
                            paintMainDlPanel(1);
                        }
                        else if(listType.equals("removed")){
                            String selectedName = selectedDownload.getName();
                            int num = Integer.parseInt(selectedName);
                            SettingFileInfo.getItems().removeFromRemovedList(num);
                            paintMainDlPanel(2);
                        }
                        else if(listType.equals("queue")){
                            String selectedName = selectedDownload.getName();
                            int num = Integer.parseInt(selectedName);
                            //SettingFileInfo.getItems().removeFromQueueList(num);
                            SettingFileInfo.getItems().downloads.get(num).setInQueue(false);
                            paintMainDlPanel(3);
                        }
                    }
                    else{
                        System.out.println("No Download Selected!");
                    }
                });
            }

            else if(name[i].equals("settings")) {
                button.addActionListener(e -> new Settings());
            }

            else if(name[i].equals("queue")) {
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(selectedDownload == null){
                            return;
                        }
                        String selectedName = selectedDownload.getName();
                        int num = Integer.parseInt(selectedName);
                        //SettingFileInfo.getItems().addToQueueList(SettingFileInfo.getItems().downloads.get(num));
                        SettingFileInfo.getItems().downloads.get(num).setInQueue(true);
                        selectedDownload = null;
                    }
                });
            }
            else if(name[i].equals("sort")){
                button.setComponentPopupMenu(makeSortMenu());
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
        if(SettingFileInfo.getItems().language.equals("Persian")){
            searchBar.setText("اینجا نام را وارد کنید");
        }
        searchBar.setForeground(Color.GRAY);
        searchBar.setBackground(Color.decode("#c8e2ba"));
        searchBar.setBorder(BorderFactory.createLineBorder(Color.gray));
        JButton searchButton  = new JButton();
        searchButton.setBackground(Color.decode("#c8e2ba"));
        searchButton.setPreferredSize(new Dimension(22,30));
        searchButton.setIcon(new ImageIcon("Files//search.png"));
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchBar.setText("");
            }
        });
        searchButton.addActionListener(e -> {
            //System.out.println(searchBar.getText());
            searchInDownloads(searchBar.getText());
        });
        toolPanel.add(searchBar);
        toolPanel.add(searchButton);
        return toolPanel;
        //todo : add line gap between different buttons like in original program
    }

    /**
     * make a menu containing sorts menus (by Size- Name- Time)
     * @return
     */
    private JPopupMenu makeSortMenu(){
        JPopupMenu mainMenu = new JPopupMenu();
        JMenuItem bySize = new JMenuItem("Size (G to L)");
        JMenuItem bySize2 = new JMenuItem("Size (L to G)");
        JMenuItem byName = new JMenuItem("Name (G to L)");
        JMenuItem byName2 = new JMenuItem("Name (L to G)");
        JMenuItem byTime = new JMenuItem("Time (G to L)");
        JMenuItem byTime2 = new JMenuItem("Time (L to G)");

        bySize.addActionListener(e->{
            Arranger.sortBySize(1);
            paintMainDlPanel(1);
        });
        bySize2.addActionListener(e->{
            Arranger.sortBySize(2);
            paintMainDlPanel(1);
        });
        byName.addActionListener(e -> {
            Arranger.sortByName(1);
            paintMainDlPanel(1);
        });
        byName2.addActionListener(e->{
            Arranger.sortByName(2);
            paintMainDlPanel(1);
        });
        byTime.addActionListener(e->{
            Arranger.sortByTime(1);
            paintMainDlPanel(1);
        });
        byTime2.addActionListener(e->{
            Arranger.sortByTime(2);
            paintMainDlPanel(1);
        });
        mainMenu.add(bySize);
        mainMenu.add(bySize2);
        mainMenu.add(byName);
        mainMenu.add(byName2);
        mainMenu.add(byTime);
        mainMenu.add(byTime2);
        return mainMenu;
    }

    private void searchInDownloads(String searchedText){
        matchedSearches = new ArrayList<>();
        ArrayList<Download> items = new ArrayList<>();
        if(listType.equals("downloads")){
            items = SettingFileInfo.getItems().downloads;
        }
        else if(listType.equals("removed")){
            items = SettingFileInfo.getItems().removed;
        }
        else if(listType.equals("queue")){
            for(int i = 0 ; i < SettingFileInfo.getItems().downloads.size() ; i++){
                if(SettingFileInfo.getItems().downloads.get(i).getisInQueue()){
                    items.add(SettingFileInfo.getItems().downloads.get(i));
                }
            }
        }
        for(int i = 0 ; i < items.size() ; i++ ){
            if(items.get(i).getName().toLowerCase().contains(searchedText.toLowerCase()) || items.get(i).getLink().toLowerCase().contains(searchedText.toLowerCase())){
                matchedSearches.add(items.get(i));
            }
        }
        paintMainDlPanel(4);
    }

    /**
     * three button on left side of UI are created and put there.
     * buttons include Processing-Queue-Removed
     * @return
     */
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
                    if(SettingFileInfo.getItems().language.equals("Persian")){
                        buttonName[i] = "                      در حال پردازش";
                    }
                    fileAddress[i] = "Files//EagleGetIcons//processing.png";
                    toolTip[i] = "Downloads in Progress";
                    break;
                case 1:
                    buttonName[i] = "Removed                         ";
                    if(SettingFileInfo.getItems().language.equals("Persian")){
                        buttonName[i] = "                      حذف شده ها";
                    }
                    fileAddress[i] = "Files//EagleGetIcons//Completed.png";
                    toolTip[i] = "Removed Downloads";
                    break;
                case 2:
                    buttonName[i] = "Queue                              ";
                    if(SettingFileInfo.getItems().language.equals("Persian")){
                        buttonName[i] = "                      صف";
                    }
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
            if(buttonName[i].equals("Processing                      ") || buttonName[i].equals("                      در حال پردازش")){
                button.addActionListener(e -> {
                   // System.out.println("Processing");
                    paintMainDlPanel(1);
                });

            }
            else  if(buttonName[i].equals("Removed                         ")|| buttonName[i].equals("                      حذف شده ها")){
                button.addActionListener(e -> {
                   // System.out.println("removed");
                    paintMainDlPanel(2);
                });
            }
            else  if(buttonName[i].equals("Queue                              ")|| buttonName[i].equals("                      صف")){
                button.addActionListener(e -> {
                    paintMainDlPanel(3);
                });
            }
            panel.add(button);
        }
        return panel;
    }

    /**
     * menu bar on the top of UI is creted
     * and added to main frame.
     * menu contains
     * 1-add new download
     * 2-exit
     * 3-sort
     * ....
     */
    private void addMenuBar(){ //adding Download and Help Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu downloadMenu = new JMenu("Download");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            downloadMenu.setText("دانلود");
        }
        downloadMenu.setMnemonic(KeyEvent.VK_D);

        JMenu helpMenu = new JMenu("Help");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            helpMenu.setText("کمک");
        }
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(downloadMenu);
        menuBar.add(helpMenu);
        JMenuItem add,play,pause,remove,sort,taskcleaner,vidsniffer,zipper,settings,exit;
        JMenuItem about,userGuide;
        add = new JMenuItem("Add New Download");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            add.setText("دانلود جدید");
        }
        KeyStroke ctrlAKeyStroke = KeyStroke.getKeyStroke("control A");
        add.setAccelerator(ctrlAKeyStroke);
        add.addActionListener(e -> addDownload());

        play = new JMenuItem("Resume");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            play.setText("ادامه");
        }
        KeyStroke ctrlXKeyStroke = KeyStroke.getKeyStroke("shift P");
        play.setAccelerator(ctrlXKeyStroke);
        play.addActionListener(e -> System.out.println("Resume"));

        pause = new JMenuItem("Pause");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            pause.setText("ایست");
        }
        KeyStroke ctrlPKeyStroke = KeyStroke.getKeyStroke("control P");
        pause.setAccelerator(ctrlPKeyStroke);
        pause.addActionListener(e -> System.out.println("Pause"));

        remove= new JMenuItem("Remove");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            remove.setText("حذف");
        }
        KeyStroke ctrlRKeyStroke = KeyStroke.getKeyStroke("control R");
        remove.setAccelerator(ctrlRKeyStroke);
        remove.addActionListener(e -> System.out.println("Remove"));

        sort = new JMenuItem("Sort");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            sort.setText("مرتب سازی");
        }
        KeyStroke ctrlOKeyStroke = KeyStroke.getKeyStroke("control O");
        sort.setAccelerator(ctrlOKeyStroke);
        sort.addActionListener(e -> System.out.println("Sort"));

        taskcleaner = new JMenuItem("Task Cleaner");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            taskcleaner.setText("پاکسازی عمل ها");
        }
        KeyStroke ctrlTKeyStroke = KeyStroke.getKeyStroke("control T");
        taskcleaner.setAccelerator(ctrlTKeyStroke);
        taskcleaner.addActionListener(e -> System.out.println("Task Cleaner"));

        vidsniffer = new JMenuItem("Video Sniffer");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            vidsniffer.setText("ویدئو اسنیفر");
        }
        KeyStroke ctrlVKeyStroke = KeyStroke.getKeyStroke("control V");
        vidsniffer.setAccelerator(ctrlVKeyStroke);
        vidsniffer.addActionListener(e -> System.out.println("Video Sniffer"));

        zipper = new JMenuItem("Export");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            zipper.setText("خروجی دادن");
        }
        KeyStroke ctrlMKeyStroke = KeyStroke.getKeyStroke("control Z");
        zipper.setAccelerator(ctrlMKeyStroke);
        zipper.addActionListener(e ->{
            new DataSaver();
            new ZipMaker();}
        );

        settings = new JMenuItem("Settings");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            settings.setText("تنظیمات");
        }
        KeyStroke ctrlSKeyStroke = KeyStroke.getKeyStroke("control S");
        settings.setAccelerator(ctrlSKeyStroke);
        settings.addActionListener(e -> new Settings());

        exit = new JMenuItem("Exit");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            exit.setText("خروج");
        }
        KeyStroke ctrlEKeyStroke = KeyStroke.getKeyStroke("control E");
        exit.setAccelerator(ctrlEKeyStroke);
        exit.addActionListener(e -> {
            exit(0);
            DataSaver saver = new DataSaver();
        });

        about = new JMenuItem("About");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            about.setText("درباره");
        }
        KeyStroke shiftAKeyStroke = KeyStroke.getKeyStroke("shift E");
        about.setAccelerator(shiftAKeyStroke);
        about.addActionListener(e -> showAbout());

        userGuide = new JMenuItem("User Guide");
        if(SettingFileInfo.getItems().language.equals("Persian")){
            userGuide.setText("راهنمای کاربر");
        }
        KeyStroke shiftHKeyStroke = KeyStroke.getKeyStroke("shift H");
        userGuide.setAccelerator(shiftHKeyStroke);
        userGuide.addActionListener(e -> showguide());

        downloadMenu.add(add); downloadMenu.add(play); downloadMenu.add(pause); downloadMenu.add(remove); downloadMenu.add(sort); downloadMenu.add(taskcleaner); downloadMenu.add(vidsniffer); downloadMenu.add(zipper);
        downloadMenu.add(settings); downloadMenu.add(exit);
        helpMenu.add(userGuide); helpMenu.add(about);
        this.setJMenuBar(menuBar);
    }

    /**
     * reads lookAndFeel from SettingFileINfo and set the current LookAndFeel to that
     * @throws ClassNotFoundException
     * @throws UnsupportedLookAndFeelException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
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
         ArrayList<Download> list = new ArrayList<>();
        if(type == 1){
             list = SettingFileInfo.getItems().downloads;
             listType = "downloads";
        }
        else if(type == 2){
             list = SettingFileInfo.getItems().removed;
            listType = "removed";
        }
        else if(type == 3){
             //list = SettingFileInfo.getItems().queue;
             for(int i = 0 ; i < SettingFileInfo.getItems().downloads.size() ; i++){
                 if(SettingFileInfo.getItems().downloads.get(i).getisInQueue()){
                     list.add(SettingFileInfo.getItems().downloads.get(i));
                 }
             }
             listType = "queue";
        }
        else if(type == 4){
            list = matchedSearches;
        }

        downloadMainPanel.removeAll();
        repaint();

        for(int i = 0 ; i < list.size() ; i++){
            String savePath = SettingFileInfo.getItems().saveDir;
            String fileLink = list.get(i).getLink();

            JPanel newDlPanel = makeUIforEachDownload(list.get(i),i,savePath);
            downloadMainPanel.add(newDlPanel);

            repaint();


            if( !list.get(i).getCompleted()){ // this if prevents from downloading a file that is downloaded completely before
                if(!list.get(i).isInProgress()) { //this if prevents from recalling download process for download when frame refreshes
                    list.get(i).setInProgress(true);
                    JFrame frame = this;
                    WorkerThread worker = new WorkerThread(frame, list.get(i), fileLink, savePath);
                    allWorkers.add(worker);
                    executor.execute(worker);
                }
            }
            else {
                list.get(i).getProgressBar().setValue(100);
                revalidate();
                repaint();
            }

            SettingFileInfo.getItems().setAddState(0); // change the add state to primal state
            SettingFileInfo.getItems().checkContinue = 0;
        }

    }

    public JPanel makeUIforEachDownload(Download download , int downloadNumber, String savePath){
        JPanel newDlPanel = new JPanel();
        newDlPanel.setLayout(null);
        newDlPanel.setName("" + downloadNumber);
        newDlPanel.setBackground(Color.decode("#ffe1ad"));
        // newDlPanel.setSize(new Dimension(785,80));
        //newDlPanel.setLocation(0,height);

        JLabel icon = new JLabel(new ImageIcon("Files//dlIcon.png"));
        icon.setSize(40,40);
        icon.setLocation(10,20);
        newDlPanel.add(icon);

        JLabel fileName = new JLabel( download.getName() + "     " + download.getSize() + " KB" );
        //JLabel fileLink = new JLabel(SettingFileInfo.getItems().downloads.get(i).link);
        fileName.setLocation(70,14);
        fileName.setSize(500,15);
        fileName.setForeground(Color.GRAY);
        newDlPanel.add(fileName);

        JProgressBar progressBar = download.getProgressBar();
        newDlPanel.add(progressBar);

        JButton open = new JButton(new ImageIcon("Files//open.png"));
        open.setSize(15,15);
        open.setBorder(BorderFactory.createEmptyBorder()); // remove the border of the button to make it looks like a flat image on panel
        open.setBackground(Color.decode("#ffe1ad"));
        open.setLocation(progressBar.getLocation().x + progressBar.getSize().width + 5,progressBar.getLocation().y);

        String fileDirecory = savePath + download.getName();
        open.addActionListener(e -> {
            File file = new File(fileDirecory);
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

        if(listType.equals("queue")){
            SpinnerModel MinModel = new SpinnerNumberModel(0,0,60,1);
            JSpinner minuteSpinner = new JSpinner(MinModel);
            minuteSpinner.setLocation(open.getLocation().x + 60,open.getLocation().y - 2);
            minuteSpinner.setValue(download.getQueueStartMinute());
            JLabel mLabel = new JLabel("Minute");
            mLabel.setSize(50,20);
            mLabel.setLocation(minuteSpinner.getLocation().x,minuteSpinner.getLocation().y  + 20);
            minuteSpinner.setSize(40,20);
            Download min = download;
            ChangeListener listener = new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    min.setQueueStartMinute((int)minuteSpinner.getValue());
                }
            };
            minuteSpinner.addChangeListener(listener);

            newDlPanel.add(mLabel);
            newDlPanel.add(minuteSpinner);


            SpinnerModel HourModel = new SpinnerNumberModel(0,0,12,1);
            JSpinner hourSpinner = new JSpinner(HourModel);
            hourSpinner.setLocation(open.getLocation().x + 20,open.getLocation().y - 2);
            hourSpinner.setSize(40,20);
            hourSpinner.setValue(download.getQueueStartHour());
            JLabel hLabel = new JLabel("Hour");
            hLabel.setLocation(hourSpinner.getLocation().x,hourSpinner.getLocation().y  + 20);
            hLabel.setSize(50,20);
            Download hour = download;
            ChangeListener listener2 = new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    hour.setQueueStartHour((int)hourSpinner.getValue());
                }
            };
            hourSpinner.addChangeListener(listener2);
            newDlPanel.add(hLabel);
            newDlPanel.add(hourSpinner);
        }
        return newDlPanel;
    }
    /**
     * process of downloading a file fom system.
     * reading file from source and writing to destination
     * it also set the value of progress bar
     * @param instance
     * @param jb
     * @param srcPath
     * @param dstPath
     */
    public void downloadFile(Download instance,JProgressBar jb, String srcPath , String dstPath){ //copy a file from src to dst path
        File src = new File(srcPath);
        File dst = new File(dstPath);
        revalidate();
        repaint();
        int SI = (int)src.length() / 100;
        if (!src.exists()) {
            //System.out.println("Source file does not exist.");
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
                if(process == 100){
                    instance.setCompleted(true);
                }
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
        downloadMainPanel.setSize(785,600);
        //downloadMainPanel.setLayout(new GridLayout(8,1,0,1));
        BoxLayout boxLayout = new BoxLayout(downloadMainPanel,BoxLayout.Y_AXIS);
        GridLayout gridLayout = new GridLayout(8,1,0,1);
        downloadMainPanel.setLayout(gridLayout);
        downloadMainPanel.setBackground(Color.decode("#d8e8d7"));
        return downloadMainPanel;
    }

    /**
     * adding system tray to program
     * @return
     */
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
        new NewDownloadPanel();
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
                Arranger.sortByTime(1);
                new DataSaver();
                System.exit(0);
            }
        });
    }
}
