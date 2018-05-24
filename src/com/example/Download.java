package com.example;

public class Download {
    //fields
    String link;
    String name;
    int downloadRate;
    int size;

    public Download(String link, String name){
        this.link = link;
        this.name = name;
    }

    //methods
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
