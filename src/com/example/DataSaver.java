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
    String settingData = "";
    String downloadsData = "";
    String removedDownloadsData = "";
    String queueDownloadData = "";
    String restrictedSites = "";

    //constructor
    public  DataSaver(){
        makeSettingString();
        makeDownloadsSaveString();
        makeQueueSaveString();
        makeRemovedDownloadsSaveString();
        makeRestrictedSitesString();

        writeSettingFile();
        writeDownloadSaveFile();
        writeQueueSaveFile();
        writeRemovedDownloadsSaveFile();
        wrtieRestrictedSitesSaveFile();
    }

    //methods
    private void makeSettingString(){
        settingData += SettingFileInfo.getItems().lookAndFeel + " >> "+ SettingFileInfo.getItems().downloadLimit + " >> " + SettingFileInfo.getItems().saveDir;
    }

    private void makeDownloadsSaveString(){
        for(int i = 0 ; i < SettingFileInfo.getItems().downloads.size() ; i++) {
            downloadsData += SettingFileInfo.getItems().downloads.get(i).getLink() + " >> " +  SettingFileInfo.getItems().downloads.get(i).getName() + " >> " + SettingFileInfo.getItems().downloads.get(i).getTime() + "\r\n";
        }
    }

    private void makeRemovedDownloadsSaveString(){
        for(int i = 0 ; i < SettingFileInfo.getItems().removed.size() ; i++) {
            removedDownloadsData += SettingFileInfo.getItems().removed.get(i).getLink() + " >> " +  SettingFileInfo.getItems().removed.get(i).getName() + " >> " + SettingFileInfo.getItems().downloads.get(i).getTime() + "\r\n";
        }
    }

    private void makeQueueSaveString(){
        for(int i = 0 ; i < SettingFileInfo.getItems().queue.size() ; i++) {
            queueDownloadData += SettingFileInfo.getItems().queue.get(i).getLink() + " >> " +  SettingFileInfo.getItems().queue.get(i).getName()  + " >> " + SettingFileInfo.getItems().downloads.get(i).getTime() + "\r\n";
        }
    }

    private void makeRestrictedSitesString(){
        for(int i = 0 ; i < SettingFileInfo.getItems().restrictedSitesList.size() ; i++){
            restrictedSites += SettingFileInfo.getItems().restrictedSitesList.get(i) + "\r\n";
        }
    }

    private void writeSettingFile(){
        saveFile("Files//Settings.jdm",settingData);
    }

    private void writeDownloadSaveFile(){
        saveFile("Files//list.jdm",downloadsData);
    }

    private void writeRemovedDownloadsSaveFile(){
        saveFile("Files//removed.jdm",removedDownloadsData);
    }

    private void writeQueueSaveFile(){
        saveFile("Files//queue.jdm",queueDownloadData);
    }

    private void wrtieRestrictedSitesSaveFile(){
        saveFile("Files//filter.jdm",restrictedSites);
    }
    private void saveFile(String path,String string){
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println(string + "was not found to write!");
        }
        byte[] bytes = string.getBytes();
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            System.out.println("Couldn't write" + string);
        }
        finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                System.out.printf("Couldn't close output Stream!");
            }
        }
    }


}
