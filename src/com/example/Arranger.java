package com.example;

import java.util.ArrayList;

/**
 * a class that has methods to sort a collection by names,size,startTime
 * each sort has 2 kind of sorting. 1 from higher(greater) to lower(lesser)
 */
public class Arranger {

    /**
     *sorts download panel by names
     * @param sort
     */
    public static void sortByName(int sort){ //if sort = 1 it means sort by Greater to Lower | sort = 2 means sort by Lower To Greater
        ArrayList<String> names = new ArrayList<>();
        for(int j = 0 ; j < SettingFileInfo.getItems().downloads.size() ; j++ ){
            names.add(SettingFileInfo.getItems().downloads.get(j).getName());
        }
        names.sort(String.CASE_INSENSITIVE_ORDER);
        ArrayList<Download> sortedList = new ArrayList<>();
        if(sort == 1) {
            for (int i = 0; i < names.size(); i++) { // make a new arrayList with sorted elements by name
                for (int j = 0; j < SettingFileInfo.getItems().downloads.size(); j++) {
                    for (int k = 0; k < SettingFileInfo.getItems().downloads.size(); k++) {
                    }
                    if (names.get(i).equals(SettingFileInfo.getItems().downloads.get(j).getName())) {
                        sortedList.add(SettingFileInfo.getItems().downloads.get(j));
                        break;
                    }
                }
            }
        }
        if(sort == 2){
            for (int i = names.size() - 1 ; i > -1; i--) { // make a new arrayList with sorted elements by name ( lower to greater)
                for (int j = 0; j < SettingFileInfo.getItems().downloads.size(); j++) {
                    if (names.get(i).equals(SettingFileInfo.getItems().downloads.get(j).getName())) {
                        sortedList.add(SettingFileInfo.getItems().downloads.get(j));
                        break;
                    }
                }
            }
        }
        SettingFileInfo.getItems().downloads = sortedList;
    }

    /**
     *sorts collection by its size
     * parameter sort = 1 means sort Greater to Lower
     * parameter sort = 2 means sort Lower to Greater
     * @param sort
     */
    public static void sortBySize(int sort){
        ArrayList<Long> sizes = new ArrayList<>();
        for(int i = 0 ; i < SettingFileInfo.getItems().downloads.size() ; i++){
            sizes.add(SettingFileInfo.getItems().downloads.get(i).getSize());
        }
        sizes.sort(Long::compareTo);
        ArrayList<Download> sortedList = new ArrayList<>();
        if( sort == 2) {
            for (int i = 0; i < sizes.size(); i++) {
                for (int j = 0; j < SettingFileInfo.getItems().downloads.size(); j++) {
                    if (sizes.get(i) == SettingFileInfo.getItems().downloads.get(j).getSize()) {
                        sortedList.add(SettingFileInfo.getItems().downloads.get(j));
                        break;
                    }
                }
            }
        }
        else if( sort == 1){
            for (int i = sizes.size() - 1; i > -1; i--) {
                for (int j = 0; j < SettingFileInfo.getItems().downloads.size(); j++) {
                    if (sizes.get(i) == SettingFileInfo.getItems().downloads.get(j).getSize()) {
                        sortedList.add(SettingFileInfo.getItems().downloads.get(j));
                        break;
                    }
                }
            }
        }
        SettingFileInfo.getItems().downloads = sortedList;
    }

    public static void sortByTime(int sort){ //if sort = 1 it means sort by Greater to Lower | sort = 2 means sort by Lower To Greater
        ArrayList<String> times = new ArrayList<>();
        for(int j = 0 ; j < SettingFileInfo.getItems().downloads.size() ; j++ ){
            times.add(SettingFileInfo.getItems().downloads.get(j).getTime());
        }
        times.sort(String.CASE_INSENSITIVE_ORDER);
        ArrayList<Download> sortedList = new ArrayList<>();
        if(sort == 1) {
            for (int i = 0; i < times.size(); i++) { // make a new arrayList with sorted elements by name
                for (int j = 0; j < SettingFileInfo.getItems().downloads.size(); j++) {
                    for (int k = 0; k < SettingFileInfo.getItems().downloads.size(); k++) {
                    }
                    if (times.get(i).equals(SettingFileInfo.getItems().downloads.get(j).getTime())) {
                        sortedList.add(SettingFileInfo.getItems().downloads.get(j));
                        break;
                    }
                }
            }
        }
        if(sort == 2){
            for (int i = times.size() - 1 ; i > -1; i--) { // make a new arrayList with sorted elements by name ( lower to greater)
                for (int j = 0; j < SettingFileInfo.getItems().downloads.size(); j++) {
                    if (times.get(i).equals(SettingFileInfo.getItems().downloads.get(j).getTime())) {
                        sortedList.add(SettingFileInfo.getItems().downloads.get(j));
                        break;
                    }
                }
            }
        }
        SettingFileInfo.getItems().downloads = sortedList;
    }
}
