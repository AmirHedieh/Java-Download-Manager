package com.example;

public class Download {
    //fields
    private String link;
    private String name;
    private int numberOfDownload;
    private int downloadRate;
    private int size;

    public Download(String link, String name){
        this.link = link;
        this.name = name;

        
    }

    //methods

    public void setNumberOfDownload(int numberOfDownload) {
        this.numberOfDownload = numberOfDownload;
    }

    public int getNumberOfDownload() {
        return numberOfDownload;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public int getDownloadRate(){
        return downloadRate;
    }

    public int getSize() {
        return size;
    }
}
