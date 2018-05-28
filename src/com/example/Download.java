package com.example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Download {
    //fields
    private String link;
    private String name;
    private long size;
    private String time;

    public Download(String link, String name){
        this.link = link;
        this.name = name;
        setSize();
        setTime();
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
    private void setTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        time = formatter.format(date);
        System.out.println(time);
    }
}
