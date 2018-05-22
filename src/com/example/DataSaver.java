package com.example;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * when closing program it saves all data including downloads , settings ,...
 * in program directory in a file so when we re launch the program data are saved
 */
public class DataSaver {

    //fields
    String settingData;

    //constructor
    public  DataSaver(){
        //save settings
        makeSettingString();
        writeSettingFile();

        //settings saved
    }

    //methods
    private void makeSettingString(){
        settingData += SettingFileInfo.getItems().lookAndFeel + " >> "+ SettingFileInfo.getItems().downloadLimit + " >> " + SettingFileInfo.getItems().saveDir;
    }

    private void writeSettingFile(){
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("Files//Settings.jdm");
        } catch (FileNotFoundException e) {
            System.out.println("Settings.jdm was not found!");
        }
        byte[] bytes = settingData.getBytes();
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            System.out.println("Couldnt write Setting.jdm");
        }
        finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                System.out.printf("Couldnt close output Stream!");
            }
        }
    }
}
