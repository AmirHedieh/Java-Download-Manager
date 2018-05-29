package com.example;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddDownload {

    //fields
    JDialog frame = new JDialog();
    JTextField link = new JTextField(" Give a link");
    JTextField fileName = new JTextField("Name");
    JTextField saveDirecotyr = new JTextField(SettingFileInfo.getItems().saveDir);
    //String fileName = new String();
    JButton ok = new JButton("Ok");
    JButton cancel = new JButton("Cancel");
    JButton addFile = new JButton("...");
    String dir,dir2;

    //constructor
    public AddDownload(){

        link.setForeground(Color.GRAY);
        fileName.setForeground(Color.GRAY);
        saveDirecotyr.setForeground(Color.GRAY);
        frame.setTitle("Add New Download");
        frame.setModal(true);
        frame.setSize(650,350);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.getContentPane().setBackground(Color.decode("#1f96c4")); // green color
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        //fileName.setText(SettingFileInfo.getItems().saveDir);
        putComponents();
        addCancelAction();
        addOkAction();
        addActionToaddFile();
        frame.setVisible(true);
    }

    //methods
    private void putComponents(){

        JLabel label1 = new JLabel("Link :");
        label1.setForeground(Color.BLACK);
        label1.setSize(100,20);
        label1.setLocation(20,50);
        link.setSize(470,30);
        link.setLocation(120,45);
        addFile.setSize(20,29);
        addFile.setLocation(590,45);
        frame.getContentPane().add(addFile);
        frame.getContentPane().add(link);
        frame.getContentPane().add(label1);


        JLabel label2 = new JLabel("File Name :");
        label2.setForeground(Color.BLACK);
        label2.setSize(100,20);
        label2.setLocation(20,100);
        fileName.setSize(470,30);
        fileName.setLocation(120,95);
        frame.getContentPane().add(fileName);
        frame.getContentPane().add(label2);

        JLabel label3 = new JLabel("Save Directory");
        label3.setForeground(Color.BLACK);
        label3.setSize(100,20);
        label3.setLocation(20,150);
        saveDirecotyr.setSize(470,30);
        saveDirecotyr.setLocation(120,145);
        saveDirecotyr.setText(" " + SettingFileInfo.getItems().saveDir);
        frame.getContentPane().add(saveDirecotyr);
        frame.getContentPane().add(label3);
    }

    private void addCancelAction(){
        //cancel.setBackground(Color.green);
        cancel.setSize(100,40);
        cancel.setLocation(540,270);
        cancel.addActionListener(e -> {
            SettingFileInfo.getItems().setCheckContinue(1);
            frame.setVisible(false);
        });
        frame.getContentPane().add(cancel);
    }

    /**
     * add an action to Ok button in Add menu
     * Action:
     * set add State field in SettingFileInfo Class to 1 which means new download is added
     * set fileInfo field in SettingFileInfo Class to String s which include file info like name- directory-...
     */
    private void addOkAction(){
       // ok.setBackground(Color.green);
        ok.setSize(100,40);
        ok.setLocation(430,270);
        ok.addActionListener(e -> {
                SettingFileInfo.getItems().setAddState(1);
                SettingFileInfo.getItems().addDownloadToList(new Download(link.getText(),fileName.getText(),setStartTime()));
                SettingFileInfo.getItems().setCheckContinue(1);
                frame.setVisible(false);
        });
        frame.getContentPane().add(ok);
    }

    private String setStartTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        Date date = new Date();
        return formatter.format(date);
    }

    private void addActionToaddFile(){
        JFileChooser address = new JFileChooser();
        addFile.addActionListener(e -> {
            int j = address.showOpenDialog(frame);
            if( j == JFileChooser.APPROVE_OPTION){
                java.io.File file = address.getSelectedFile();
                dir = file.getAbsolutePath();
                file = address.getCurrentDirectory();
                dir2 = file.getAbsolutePath();
                String s = dir.substring(dir2.length() + 1);
                fileName.setText(s);
                link.setText(dir);
            }
        });
    }

}
