package com.example;

import java.io.*;

/**
 * when user re run the program it reads saves so the program will continue
 * its states before closing the program
 * like settings , downloads , ...
 */
public class DataReader {

    //constructor
    public DataReader(){
        SettingFileInfo items = new SettingFileInfo(); // it is made just once to hold needed data to use them through other classes
        readSettingsFile();
    }

    private void readSettingsFile(){
        File settings = new File("Files//Settings.jdm");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(settings));
        } catch (FileNotFoundException e) {
            System.out.println("Settings.jdm was not found to read!");
        }
        try {
            String saves = bufferedReader.readLine();
            String[] splitSaves = saves.split(" >> ");
            System.out.println(splitSaves[0]);
            System.out.println(splitSaves[1]);
            System.out.println(splitSaves[2]);
            SettingFileInfo.getItems().setLookAndFeel(splitSaves[0]);
            SettingFileInfo.getItems().setDownloadLimit(splitSaves[1]);
            SettingFileInfo.getItems().setSaveDir(splitSaves[2]);

        } catch (IOException e) {
            System.out.println("Couldn't read setting.jdm");
        }


    }

}
