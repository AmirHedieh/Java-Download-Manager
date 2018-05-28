package com.example;

import java.util.ArrayList;
import java.util.Collection;

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

    public void addToQueueList(Download newDl){
        queue.add(newDl);
    }
    public void removeFromQueueList(int i){
        queue.remove(i);
    }
    public void addToRestrictedSites(String s){
        restrictedSitesList.add(s);
    }
    public void removeFromRestrictedSites(String s){
        restrictedSitesList.remove(s);
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

    public void sortByName(int sort){ //if sort = 1 it means sort by Greater to Lower | sort = 2 means sort by Lower To Greater
            ArrayList<String> names = new ArrayList<>();
            for(int j = 0 ; j < downloads.size() ; j++ ){
               names.add(downloads.get(j).getName());
            }
            names.sort(String.CASE_INSENSITIVE_ORDER);
            System.out.println(names);
            ArrayList<Download> sortedList = new ArrayList<>();
            if(sort == 1) {
                for (int i = 0; i < names.size(); i++) { // make a new arrayList with sorted elements by name
                    for (int j = 0; j < downloads.size(); j++) {
                        for (int k = 0; k < downloads.size(); k++) {
                        }
                        if (names.get(i).equals(downloads.get(j).getName())) {
                            sortedList.add(downloads.get(j));
                            break;
                        }
                    }
                }
            }
            if(sort == 2){
                for (int i = names.size() - 1 ; i > -1; i--) { // make a new arrayList with sorted elements by name
                    for (int j = 0; j < downloads.size(); j++) {
                        if (names.get(i).equals(downloads.get(j).getName())) {
                            sortedList.add(downloads.get(j));
                            break;
                        }
                    }
                }
            }
            downloads = sortedList;
    }
}
