package com.example;

import java.util.ArrayList;

/**
 * it is made just once to hold needed data to use them through other classes
 */
public class SettingFileInfo {

    //for setting class
    public String saveDir;
    public String downloadLimit;
    public String lookAndFeel;
    //done
    //for MainDownloadPanel
    ArrayList<Download> downloads = new ArrayList<>();
    ArrayList<Download> removed = new ArrayList<>();
    ArrayList<Download> queue = new ArrayList<>();
    //done
    public int addState = 0;
    public String fileInfo;
    public int checkContinue = 0; //when user goes in add menu program waits until user make a choice

    private static SettingFileInfo items;

    public void addDownloadToList(Download newDL){
        downloads.add(newDL);
    }

    public void removeFromDownloadList(int i){
        downloads.remove(i);
    }
    public void setLookAndFeel(String s){
        lookAndFeel = s;
    }

    public void setDownloadLimit(String s){
        downloadLimit = s;
    }

    public void setSaveDir(String s){
        saveDir = s;
    }

    public SettingFileInfo(){
        items = this;
    }

    public static SettingFileInfo getItems() {
        return items;
    }

    public void setCheckContinue(int i){
        checkContinue = i;
    }

    public void setAddState(int i){
        addState = i;
    }

    public void setFileInfo(String s){
        fileInfo = s;
    }
}
