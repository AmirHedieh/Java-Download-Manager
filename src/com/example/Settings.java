package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Settings { //todo: place components in right locations

    //fields
    private JFrame frame = new JFrame("Settings");
    private JComboBox lookAndFeelChooser;
    private JComboBox downloadLimit;
    private JButton openFile;
    private JFileChooser address;
    private JButton ok = new JButton("Ok");
    private String dir = new String("F://Projects//term2//JDM//Files");

    //constructor
    public Settings(){
        frame.setSize(500,500);
        //BoxLayout layout = new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS);
        frame.setLayout(null);
        frame.setResizable(false);
        makeLookComboBox();
        makeDownloadLimitComboBox();
        makeFileChooser();
        addOkAction();
        frame.setVisible(true);
    }

    //methods

    private void makeFileChooser(){
        address = new JFileChooser();
        openFile = new JButton("Set Path");
        openFile.setSize(140,30);
        openFile.setLocation(100,100);
        openFile.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = address.showOpenDialog(frame);
                if( i == JFileChooser.APPROVE_OPTION){
                    java.io.File file = address.getCurrentDirectory();
                    dir = file.getAbsolutePath();
                }
            }
        });
        frame.getContentPane().add(openFile);
    }

    private void makeDownloadLimitComboBox(){ //add a combo box which contains some numbers for setting maximum number of download files
        String[] possibleNumbers = new String[4];
        possibleNumbers[0] = "Unlimited"; possibleNumbers[1] = "1"; possibleNumbers[2] = "4"; possibleNumbers[3] = "8";
        downloadLimit = new JComboBox(possibleNumbers);
        downloadLimit.setSize(140,27);
        downloadLimit.setLocation(10,40);
        frame.getContentPane().add(downloadLimit);
    }

    private void makeLookComboBox(){ //add a combo box including system look and feels to frame
        lookAndFeelChooser = new JComboBox(sysLookAndFeels());
        lookAndFeelChooser.setSize(140,27);
        lookAndFeelChooser.setLocation(10,10);
        frame.getContentPane().add(lookAndFeelChooser);
    }

    private String[] sysLookAndFeels(){ //return System look and feels: metal - nimbus - CDE/Motif - Windows - Windows Classic
        UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();
        String[] list = new String[plaf.length];
        for(int i = 0 ; i < plaf.length ; i++){
            list[i] = plaf[i].getName();
            //System.out.println(plaf[i].getName());
        }
        return list;
    }

    public void addOkAction(){
        ok.setSize(140,30);
        ok.setLocation(100,400);
        frame.getContentPane().add(ok);
        ok.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File settingFile = new File("Files//Settings.txt");
                if (settingFile.exists()){
                    System.out.println("File found");
                }
                BufferedWriter bufferedWriter = null;
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(settingFile));
                } catch (IOException e1) {
                    System.out.println("Can't Write setting File");
                }
                String newState = "";
                newState += lookAndFeelChooser.getSelectedItem() + " ";
                newState += downloadLimit.getSelectedItem() + " ";
                newState += dir;
                System.out.println(newState);
                try {
                    bufferedWriter.write(newState);
                    System.out.println("File wrote");
                } catch (IOException e1) {
                    System.out.println("Can't Write setting File");
                }
                finally {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e1) {
                    }
                }
            }
        });

    }


}
