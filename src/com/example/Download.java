package com.example;

import java.io.File;

public class Download {
    //fields
    private String link;
    private String name;
    private long size;
    private String time;

    public Download(String link, String name,String time){
        this.link = link;
        this.name = name;
        this.time = time;
        setSize();
    }

    //methods

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public long getSize() {
        return size;
    }

    private void setSize(){
        File file = new File(link);
        size = file.length() / 1000;
    }


    public String getTime(){
        return time;
    }
}
