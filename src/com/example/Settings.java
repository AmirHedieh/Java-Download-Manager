package com.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class Settings { //todo: place components in right locations
 //todo:when user change L&F a pop up panel says "PLZ Re run program to change L&F"
    //fields
    private JDialog frame = new JDialog();
    private JComboBox lookAndFeelChooser;
    private JComboBox downloadLimit;
    private JButton openFile;
    private JFileChooser address;
    private JDialog restrictedSites;
    private JButton restrictedButton;
    private JButton ok = new JButton(new ImageIcon("Files//ok.png"));
    JPanel selectedPanel = null;
    String selectedPanelString = null;
    private JButton cancel = new JButton(new ImageIcon("Files//cancel.png"));
    private String dir = new String("F://Projects//term2//JDM//Files");

    //constructor
    public Settings(){
        frame.setLocation(670,300);
        frame.setTitle("Settings");
        frame.setModal(true);
        frame.setSize(500,300);
        //BoxLayout layout = new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS);
        frame.getContentPane().setBackground(Color.decode("#c8e2ba")); // green color
        //frame.getContentPane().setBackground(Color.decode("#32363f")); //gray color
        frame.setLayout(null);
        frame.setResizable(false);
        lookAndFeelChooser = new JComboBox(sysLookAndFeels());
        String[] possibleNumbers = new String[4];
        possibleNumbers[0] = "Unlimited"; possibleNumbers[1] = "1"; possibleNumbers[2] = "4"; possibleNumbers[3] = "8";
        downloadLimit = new JComboBox(possibleNumbers);
        updateSettings();
        makeLookComboBox();
        makeDownloadLimitComboBox();
        makeFileChooser();
        makeRestrictedSitesFrame();
        addOkAction();
        addCancelAction();
        addRestrcitedButtonAction();
        frame.setVisible(true);
    }

    //methods
    private void makeRestrictedSitesFrame(){
        restrictedSites = new JDialog();
        restrictedSites.setSize(700,500);
        restrictedSites.setLocationRelativeTo(null);
        restrictedSites.setLayout(null);
        restrictedSites.setModal(true);
        JButton add = new JButton("Add");
        JButton remove = new JButton("Remove");
        add.setSize(80,40);
        add.setLocation(5,5);
        remove.setSize(80,40);
        remove.setLocation(90,5);
        restrictedSites.add(add);
        add.addActionListener(e -> addSite());
        restrictedSites.add(remove);
        remove.addActionListener(e -> removeSite());
        JPanel sites = new JPanel();
        sites.setSize(675,395);
        sites.setLocation(5,50);
        LayoutManager layoutManager = new GridLayout(17,0,0,1);
        sites.setLayout(layoutManager);
        sites.setBackground(Color.decode("#ffc9ad"));
        restrictedSites.add(sites);
        if(SettingFileInfo.getItems().restrictedSitesList.size() != 0) {
            for (int i = 0; i < SettingFileInfo.getItems().restrictedSitesList.size(); i++) {
                JPanel item = new JPanel();
                item.setBackground(Color.decode("#55ff3d"));
                item.setLayout(null);
                JLabel link = new JLabel();
                link.setLocation(1,1);
                link.setSize(650,20);
                link.setText(SettingFileInfo.getItems().restrictedSitesList.get(i));
                item.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(selectedPanel!=null){
                            selectedPanel.setBackground(Color.decode("#55ff3d"));
                        }
                        selectedPanel = item;
                        item.setBackground(Color.decode("#2b60ff"));
                        selectedPanelString = link.getText();
                    }
                });
                item.add(link);
                sites.add(item);
            }
        }
        restrictedSites.repaint();
    }

    private void addSite(){
        JDialog siteAdder = new JDialog();
        siteAdder.setLayout(null);
        siteAdder.setSize(700,130);
        siteAdder.setLocationRelativeTo(null);
        siteAdder.setModal(true);

        JTextField link = new JTextField();
        link.setLocation(5,20);
        link.setSize(600,40);
        siteAdder.add(link);

        JButton button = new JButton("Add");
        button.setSize(70,38);
        button.setLocation(link.getX() + link.getSize().width + 2 ,link.getY());
        button.addActionListener(e -> {
            if(!link.getText().equals("")){
                SettingFileInfo.getItems().addToRestrictedSites(link.getText());
            }
            siteAdder.setVisible(false);
            restrictedSites.setVisible(false);
            makeRestrictedSitesFrame();
            restrictedSites.setVisible(true);
        });
        siteAdder.add(button);
        siteAdder.setVisible(true);
    }

    private void removeSite(){
        SettingFileInfo.getItems().removeFromRestrictedSites(selectedPanelString);
        restrictedSites.setVisible(false);
        makeRestrictedSitesFrame();
        restrictedSites.setVisible(true);
    }

    private void addRestrcitedButtonAction(){
        restrictedButton = new JButton("Restricted Sites");
        restrictedButton.setSize(140,30);
        restrictedButton.setLocation(180,160);
        restrictedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restrictedSites.setVisible(true);
            }
        });
        frame.add(restrictedButton);
    }
    private void makeFileChooser(){
        address = new JFileChooser();
        openFile = new JButton("Set Path");
        openFile.setSize(140,30);
        openFile.setLocation(180,120);
        openFile.addActionListener(e -> {
            int i = address.showOpenDialog(frame);
            if( i == JFileChooser.APPROVE_OPTION){
                File file = address.getCurrentDirectory();
                dir = file.getAbsolutePath();
            }
        });
        frame.getContentPane().add(openFile);
    }

    private void makeDownloadLimitComboBox(){ //add a combo box which contains some numbers for setting maximum number of download files
        JLabel label = new JLabel("Files at same time :");
        label.setSize(120,20);
        label.setLocation(60,80);
        frame.getContentPane().add(label);

        downloadLimit.setSize(140,27);
        downloadLimit.setLocation(180,75);
        frame.getContentPane().add(downloadLimit);
    }

    private void makeLookComboBox(){ //add a combo box including system look and feels to frame
        JLabel label = new JLabel("Look And Feel :");
        label.setSize(120,20);
        label.setLocation(60,35);
        frame.getContentPane().add(label);
        lookAndFeelChooser.setSize(140,27);
        lookAndFeelChooser.setLocation(180,30);
        frame.getContentPane().add(lookAndFeelChooser);
    }

    private String[] sysLookAndFeels(){ //return System look and feels: metal - nimbus - CDE/Motif - Windows - Windows Classic
        UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();
        String[] list = new String[plaf.length];
        for(int i = 0 ; i < plaf.length ; i++){
            list[i] = plaf[i].getName();
            //System.out.println(plaf[i].getName());
        }
        for(int i = 0 ; i < list.length ; i++){
            if(list[i].equals("Windows Classic")){
                list[i] = "WindowsClassic";
            }
        }
        return list;
    }

    private void addOkAction(){ //add action to OK button, it saves the setting in file directory in a text file
        ok.setSize(80,60);
        ok.setBackground(Color.decode("#c8e2ba"));
        ok.setLocation(280,195);
        frame.getContentPane().add(ok);
        ok.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SettingFileInfo.getItems().setLookAndFeel( "" + lookAndFeelChooser.getSelectedItem() );
                SettingFileInfo.getItems().setDownloadLimit( "" + downloadLimit.getSelectedItem() );
                SettingFileInfo.getItems().setSaveDir(dir);
                frame.setVisible(false);
            }
        });

    }

    private void addCancelAction(){
        cancel.setSize(80,60);
        cancel.setLocation(380,195);
        cancel.setBackground(Color.decode("#c8e2ba"));
        frame.getContentPane().add(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
    }



    public void updateSettings(){ // update settings
        switch (SettingFileInfo.getItems().lookAndFeel){
            case "Metal":{
                lookAndFeelChooser.setSelectedItem("Metal");
                break;
            }
            case "Nimbus":{
                lookAndFeelChooser.setSelectedItem("Nimbus");
                break;
            }
            case "Windows":{
                lookAndFeelChooser.setSelectedItem("Windows");
                break;
            }
            case "WindowsClassic":{
                lookAndFeelChooser.setSelectedItem("WindowsClassic");
                break;
            }
            case "CDE/Motif":{
                lookAndFeelChooser.setSelectedItem("CDE/Motif");
                break;
            }
        }

        switch (SettingFileInfo.getItems().downloadLimit){
            case "Unlimited":{
                downloadLimit.setSelectedItem("Unlimited");
                break;
            }
            case "1":{
                downloadLimit.setSelectedItem("1");
                break;
            }
            case "4":{
                downloadLimit.setSelectedItem("4");
                break;
            }
            case "8":{
                downloadLimit.setSelectedItem("8");
                break;
            }
        }

    }
}
