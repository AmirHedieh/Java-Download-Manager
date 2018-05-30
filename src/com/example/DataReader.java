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
        initializeArrayList("downloads",readDownloadsData("Files//list.jdm"));
        initializeArrayList("removed",readDownloadsData("Files//removed.jdm"));
        initializeArrayList("queue",readDownloadsData("Files//queue.jdm"));
        initializeArrayList("restricted",readDownloadsData("Files//filter.jdm"));
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
            //System.out.println(splitSaves[0]);
            //System.out.println(splitSaves[1]);
            //System.out.println(splitSaves[2]);
            SettingFileInfo.getItems().setLookAndFeel(splitSaves[0]);
            SettingFileInfo.getItems().setDownloadLimit(splitSaves[1]);
            SettingFileInfo.getItems().setSaveDir(splitSaves[2]);
            SettingFileInfo.getItems().setLanguage(splitSaves[3]);
        } catch (IOException e) {
            System.out.println("Couldn't read setting.jdm");
        }
    }

    private String readDownloadsData(String path){
        File downloads = new File(path);
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(downloads);
            bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            System.out.println(path + " was not found to read!");
        }
        String line;
        String totalText = "";
        try {
            while((line = bufferedReader.readLine()) != null) {
                line += "\n";
                totalText += line;
            }
        } catch (IOException e) {
            System.out.println("Couldn't read " + path);
        }
        finally {
            try {
                fileReader.close();
                //System.out.println(totalText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return totalText;
        }
    }

    private void initializeArrayList(String panelType,String inputString){
        if(inputString.equals("")){
            return;
        }
        String[] lines = inputString.split("\n");
        for(int i = 0 ; i < lines.length ; i++){
            String[] splitString = lines[i].split(" >> ");
            if(panelType.equals("downloads")){
                Download newDl = new Download(splitString[0],splitString[1],splitString[2]);
                SettingFileInfo.getItems().addDownloadToList(newDl);
            }
            else if(panelType.equals("removed")){
                Download newDl = new Download(splitString[0],splitString[1],splitString[2]);
                SettingFileInfo.getItems().addToRemovedList(newDl);
            }
            else if(panelType.equals("queue")){
                Download newDl = new Download(splitString[0],splitString[1],splitString[2]);
                newDl.setQueueStartMinute(Integer.parseInt(splitString[3]));
                System.out.println(Integer.parseInt(splitString[3]));
                newDl.setQueueStartHour(Integer.parseInt(splitString[4]));
                System.out.println(Integer.parseInt(splitString[4]));
                SettingFileInfo.getItems().addToQueueList(newDl);
            }
            else if(panelType.equals("restricted")){
                SettingFileInfo.getItems().addToRestrictedSites(lines[i]);
            }
        }
    }

}
