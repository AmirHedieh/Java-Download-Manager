package com.example;

import java.util.ArrayList;
import java.util.Collection;

/**
 * it is made just once to hold needed data to use them through other classes
 * all data saved at the end of program or
 * all data read at the start of program
 * affects here and changes its fields values
 */
public class SettingFileInfo {

    //for setting class
    public String saveDir;
    public String downloadLimit;
    public String lookAndFeel;
    public String language;
    //done
    //for MainDownloadPanel
    ArrayList<Download> downloads = new ArrayList<>();
    ArrayList<Download> removed = new ArrayList<>();
    ArrayList<String> restrictedSitesList = new ArrayList<>();

    //done
    public int addState = 0;
    public String fileInfo;
    public int checkContinue = 0; //when user goes in add menu program waits until user make a choice

    private static SettingFileInfo items;

    public void addDownloadToList(Download newDl){
        downloads.add(newDl);
    }
    public void removeFromDownloadList(int i){
        downloads.remove(i);
    }

    public void addToRemovedList(Download newDl){
        removed.add(newDl);
    }
    public void removeFromRemovedList(int i){
        removed.remove(i);
    }

    public void addToRestrictedSites(String s){
        restrictedSitesList.add(s);
    }
    public void removeFromRestrictedSites(String s){
        restrictedSitesList.remove(s);
    }


    public void setLanguage(String s){
        language = s;
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


}
