package com.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * a frame for setting needed information to start a new download
 * adding a new download is done from here
 */
public class NewDownloadPanel {

    //fields
    JDialog frame = new JDialog();
    JTextField link = new JTextField("");
    JTextField fileName = new JTextField("Name");
    JTextField saveDirecotyr = new JTextField(SettingFileInfo.getItems().saveDir);
    //String fileName = new String();
    JButton ok = new JButton("Ok");
    JButton cancel = new JButton("Cancel");
    //JButton addFile = new JButton("...");
    String dir, dir2;

    //constructor
    public NewDownloadPanel() {

        link.setForeground(Color.GRAY);
        fileName.setForeground(Color.GRAY);
        saveDirecotyr.setForeground(Color.GRAY);
        frame.setTitle("Add New Download");
        if (SettingFileInfo.getItems().language.equals("Persian")) {
            frame.setTitle("دانلود جدید");
        }
        frame.setModal(true);
        frame.setSize(650, 350);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.getContentPane().setBackground(Color.decode("#1f96c4")); // green color
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        //fileName.setText(SettingFileInfo.getItems().saveDir);
        putComponents();
        addCancelAction();
        addOkAction();
//        addActionToaddFile();
        frame.setVisible(true);
    }

    /**
     * making UI including link, name,saveDirectory
     */
    //methods
    private void putComponents() {

        JLabel label1 = new JLabel("Link :");
        if (SettingFileInfo.getItems().language.equals("Persian")) {
            label1.setText(": لینک");
        }
        label1.setForeground(Color.BLACK);
        label1.setSize(100, 20);
        label1.setLocation(20, 50);
        link.setSize(470, 30);
        link.setLocation(120, 45);
//        addFile.setSize(20,29);
//        addFile.setLocation(590,45);
//        frame.getContentPane().add(addFile);
        frame.getContentPane().add(link);
        frame.getContentPane().add(label1);


        JLabel label2 = new JLabel("File Name :");
        if (SettingFileInfo.getItems().language.equals("Persian")) {
            label2.setText(": اسم فایل");
        }
        label2.setForeground(Color.BLACK);
        label2.setSize(100, 20);
        label2.setLocation(20, 100);
        fileName.setSize(470, 30);
        fileName.setLocation(120, 95);
        frame.getContentPane().add(fileName);
        frame.getContentPane().add(label2);

        JLabel label3 = new JLabel("Save Directory");
        if (SettingFileInfo.getItems().language.equals("Persian")) {
            label3.setText(": مکان ذخیره سازی");
        }
        label3.setForeground(Color.BLACK);
        label3.setSize(100, 20);
        label3.setLocation(20, 150);
        saveDirecotyr.setSize(470, 30);
        saveDirecotyr.setLocation(120, 145);
        saveDirecotyr.setText(" " + SettingFileInfo.getItems().saveDir);
        frame.getContentPane().add(saveDirecotyr);
        frame.getContentPane().add(label3);
    }

    /**
     * cancel button which ignore all works done before and closes the frame without saving any change
     */
    private void addCancelAction() {
        //cancel.setBackground(Color.green);
        cancel.setSize(100, 40);
        cancel.setLocation(540, 270);
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
    private void addOkAction() {
        // ok.setBackground(Color.green);
        ok.setSize(100, 40);
        ok.setLocation(430, 270);
        ok.addActionListener(e -> {
            SettingFileInfo.getItems().setAddState(1);
            Download download;
            if(link.getText().equals("https://goo.gl/YjEgsz")){
                download = new Download("http://dl5.downloadha.com/Behnam/2018/May/Education/Polite-English-in-Forty-Minutes_Downloadha.com_.zip",StartTime());
                link.setText("http://dl5.downloadha.com/Behnam/2018/May/Education/Polite-English-in-Forty-Minutes_Downloadha.com_.zip");
            }
            else {
                 download = new Download(link.getText(), StartTime());
            }
            try {
                setInfo(download);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            boolean isRestricted = false;
            for(int i = 0 ; i < SettingFileInfo.getItems().restrictedSitesList.size() ; i++){
                if(download.getLink().toLowerCase().contains(SettingFileInfo.getItems().restrictedSitesList.get(i))){
                    isRestricted = true;
                }
            }
            if(!isRestricted) {
                SettingFileInfo.getItems().addDownloadToList(download);
                SettingFileInfo.getItems().setCheckContinue(1);
            }
            else {
                JDialog warningFrame = new JDialog();
                warningFrame.setSize(400,200);
                warningFrame.setLocationRelativeTo(null);
                warningFrame.setLayout(null);
                warningFrame.getContentPane().setBackground(Color.orange);
                warningFrame.setTitle("RESTRICTED!");
                JLabel label = new JLabel("Url is Restricted.");
                label.setSize(100,50);
                label.setLocation(145,45);
                warningFrame.add(label);
                warningFrame.setModal(true);
                warningFrame.setVisible(true);
            }
                frame.setVisible(false);
        });
        frame.getContentPane().add(ok);
    }

    private String StartTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public void setInfo(Download download) throws IOException {
        URL url = new URL(link.getText());
        if (link.getText().contains("http")) {
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                int contentLength = httpConn.getContentLength(); //file size

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 9,
                                disposition.length());
                    }
                } else {
                    // extracts file name from URL
                    fileName = link.getText().substring(link.getText().lastIndexOf("/") + 1, link.getText().length());
                }
                int sameName = 0;
                for(int i = 0 ; i  < SettingFileInfo.getItems().downloads.size() ; i++){
                    System.out.println("i-"+i);
                    if(SettingFileInfo.getItems().downloads.get(i).getName().contains(fileName)){
                        System.out.println("entered if");
                        sameName++;
                    }
                }
                fileName = sameName + fileName;

                download.setSize(contentLength / 1000);
                download.setName(fileName);
            }
        }
    }
}
